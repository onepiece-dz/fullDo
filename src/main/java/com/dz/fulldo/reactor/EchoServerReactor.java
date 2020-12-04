package com.dz.fulldo.reactor;

import com.dz.fulldo.SystemConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class EchoServerReactor implements Runnable {
    private Selector selector;
    ServerSocketChannel serverSocketChannel;

    EchoServerReactor() throws IOException {
        // 1.获取Selector实例,
        selector = Selector.open();
        // 2.获取通道
        serverSocketChannel = ServerSocketChannel.open();
        // 3.设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 4.绑定连接
        serverSocketChannel.bind(new InetSocketAddress(SystemConfig.SOCKET_SERVER_PORT));
        // 5.将通道注册到选择器上，并设定监听事件为：“接收连接”事件
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey.attach(new AcceptorHandler());
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                // 此处是阻塞的
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                for (SelectionKey selectionKey: selected) {
                    // 反应器负责分发收到的事件
                    dispatch(selectionKey);
                }
                selected.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void dispatch(SelectionKey k) {
        Runnable handler = (Runnable)k.attachment();
        // 调用之前绑定到选择键的handler处理器对象
        if (null != handler) {
            handler.run();
        }
    }

    class AcceptorHandler implements Runnable {
        @Override
        public void run() {
            // 接收新连接
            // 需要为新连接，创建一个输入输出的handler处理器
            try {
                SocketChannel channel = serverSocketChannel.accept();
                if (null != channel) {
                    new EchoHandler(selector, channel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
       new Thread(new EchoServerReactor()).start();
        //new EchoServerReactor().run();
    }
}
