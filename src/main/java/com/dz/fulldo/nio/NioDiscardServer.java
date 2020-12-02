package com.dz.fulldo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioDiscardServer {
    public static void startServer() throws IOException {
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

        // 6.while循环轮询，select()方法会阻塞直到有就绪事件到达
        while(selector.select() > 0) {
            // 7. 获取选择键集合
            Set<SelectionKey> selectKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectKeys.iterator();
            while(keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                // 8.根据具体的IO事件类型，执行对应的业务操作
                if (key.isAcceptable()) {
                    // 9.获取客户端安连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 10. 切换非阻塞模式
                    socketChannel.configureBlocking(false);
                    // 11.注册可读事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isConnectable()) {
                } else if (key.isReadable()) {
                    // 12. 若选择键的IO事件是“可读”事件，读取数据
                    SocketChannel socketChannel = (SocketChannel)key.channel();
                    // 13. 读取数据，然后丢弃
                    processRead(socketChannel);
                } else if (key.isWritable()) {
                }
                // 14. 处理完成后，移除选择健
                keyIterator.remove();
            }
        }

        // 15. 关闭连接
        serverSocketChannel.close();
    }

    public static void main(String[] args) throws IOException {
        startServer();
    }

    private static void processRead(SocketChannel socketChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int length = 0;
        while((length = socketChannel.read(byteBuffer)) > 0) {
            byteBuffer.flip();
            System.out.println(new String(byteBuffer.array(), 0, length));
            byteBuffer.clear();
        }
        socketChannel.close();
    }
}
