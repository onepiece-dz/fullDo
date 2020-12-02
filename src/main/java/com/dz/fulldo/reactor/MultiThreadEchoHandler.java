package com.dz.fulldo.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadEchoHandler implements Handler {
    final SocketChannel channel;
    final SelectionKey sk;
    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    static final int RECIEVING = 0, SENDING = 1;
    int state = RECIEVING;

    // 引入线程池
    static ExecutorService pool = Executors.newFixedThreadPool(4);

    MultiThreadEchoHandler(Selector selector, SocketChannel c) throws IOException {
        channel = c;
        c.configureBlocking(false);
        // 取得选择键， 再设置感兴趣的IO事件
        sk = channel.register(selector, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    public void process() {
        pool.execute(new AsyncTask());
    }

    public synchronized void asyncRun() {
        try {
            // 处理输入输出
            if (state == SENDING) {
                // 写入通道
                channel.write(byteBuffer);
                // 写完后，准备开始从通道读，byteBuffer切换成写入模式
                byteBuffer.clear();
                // 注册read就绪事件
                sk.interestOps(SelectionKey.OP_READ);
                // 写完后，进入接收状态
                state = RECIEVING;
            } else if (state == RECIEVING) {
                // 从通道读
                int length = 0;
                while ((length = channel.read(byteBuffer)) > 0) {
                    System.out.println(new String(byteBuffer.array(), 0, length));
                }
                // 读完后，准备开始写入通道，byteBuffer切换成读取模式
                byteBuffer.flip();
                // 注册write就绪事件
                sk.interestOps(SelectionKey.OP_WRITE);
                state = SENDING;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class AsyncTask implements Runnable {
        @Override
        public void run() {
            MultiThreadEchoHandler.this.asyncRun();
        }
    }
}
