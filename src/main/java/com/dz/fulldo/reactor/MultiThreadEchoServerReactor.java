package com.dz.fulldo.reactor;

import com.dz.fulldo.nio.SystemConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadEchoServerReactor {
    final ServerSocketChannel serverSocket;
    AtomicInteger next = new AtomicInteger(0);
    // 选择器集合，引入多个选择器
    Selector[] selectors = new Selector[2];
    // 引入多个子反应器
    SubReactor[] subReactors = null;

    MultiThreadEchoServerReactor() throws IOException {
        selectors[0] = Selector.open();
        selectors[1] = Selector.open();

        serverSocket = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(SystemConfig.SOCKET_SERVER_PORT);
        serverSocket.socket().bind(address);

        serverSocket.configureBlocking(false);

        SelectionKey sk = serverSocket.register(selectors[0], SelectionKey.OP_ACCEPT);
        sk.attach(new AcceptorHandler());

        SubReactor subReactor1 = new SubReactor(selectors[0]);
        SubReactor subReactor2 = new SubReactor(selectors[1]);
        subReactors = new SubReactor[]{subReactor1, subReactor2};
    }

    private void startService() {
        // 一子反应器对应一个线程
        new Thread(subReactors[0]).start();
        new Thread(subReactors[1]).start();
    }

    class AcceptorHandler implements Handler {
        @Override
        public void process() {
            // 接收新连接
            // 需要为新连接，创建一个输入输出的handler处理器
            try {
                SocketChannel channel = serverSocket.accept();
                if (null != channel) {
                    new MultiThreadEchoHandler(selectors[next.get()], channel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (next.incrementAndGet() == selectors.length) {
                next.set(0);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        MultiThreadEchoServerReactor serverReactor = new MultiThreadEchoServerReactor();
        serverReactor.startService();
    }
}
