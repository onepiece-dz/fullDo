package com.dz.fulldo.reactor;

import com.sun.org.apache.regexp.internal.RE;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 新连接处理器
 */
public class EchoHandler implements Runnable {
    SocketChannel socketChannel;
    SelectionKey selectionKey;
    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    static final int RECIEVING = 0, SENDING = 1;
    int state = RECIEVING;

    EchoHandler(Selector selector, SocketChannel c) throws IOException {
        this.socketChannel = c;
        socketChannel.configureBlocking(false);
        selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        selector.wakeup();
    }
    @Override
    public void run() {
        try {
            // 处理输入输出
            if (state == SENDING) {
                // 写入通道
                socketChannel.write(byteBuffer);
                // 写完后，准备开始从通道读，byteBuffer切换成写入模式
                byteBuffer.clear();
                // 注册read就绪事件
                selectionKey.interestOps(SelectionKey.OP_READ);
                // 写完后，进入接收状态
                state = RECIEVING;
            } else if (state == RECIEVING) {
                // 从通道读
                int length = 0;
                while ((length = socketChannel.read(byteBuffer)) > 0) {
                    System.out.println(new String(byteBuffer.array(), 0, length));
                }
                // 读完后，准备开始写入通道，byteBuffer切换成读取模式
                byteBuffer.flip();
                // 注册write就绪事件
                selectionKey.interestOps(SelectionKey.OP_WRITE);
                state = SENDING;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
