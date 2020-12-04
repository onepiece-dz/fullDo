package com.dz.fulldo.nio;

import com.dz.fulldo.SystemConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioDiscardClient {
    public static void startClient() throws IOException {
        InetSocketAddress address = new InetSocketAddress(SystemConfig.SOCKET_SERVER_PORT);
        // 1.获取通道
        SocketChannel socketChannel = SocketChannel.open(address);
        // 2.切换非阻塞模式
        socketChannel.configureBlocking(false);
        // 不断地自旋、等待连接完成，或者做一些其他的事情
        while (!socketChannel.finishConnect()) {
        }
        // 3.分配指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("hello world".getBytes());
        byteBuffer.flip();
        // 4.发送
        socketChannel.write(byteBuffer);
        socketChannel.shutdownOutput();
        socketChannel.close();
    }

    public static void main(String[] args) throws IOException {
        startClient();
    }
}
