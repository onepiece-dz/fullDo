package com.dz.fulldo.nio;

import com.dz.fulldo.SystemConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioDemo {
    public static void main(String[] args) {
        try {
            // 1.获取Selector实例
            Selector selector = Selector.open();
            // 2.获取通道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 3.设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            // 4.绑定连接
            serverSocketChannel.bind(new InetSocketAddress(SystemConfig.SOCKET_SERVER_PORT));
            // 5.将通道注册到选择器上，并设定舰艇事件为：“接收连接”事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            // while循环轮询，select()方法会阻塞直到有就绪事件到达
            while(selector.select() > 0) {
                Set<SelectionKey> selectKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectKeys.iterator();
                while(keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    // 根据具体的IO事件类型，执行对应的业务操作
                    if (key.isAcceptable()) {

                    } else if (key.isConnectable()) {

                    } else if (key.isReadable()) {

                    } else if (key.isWritable()) {

                    }
                    // 处理完成后，移除选择健
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
