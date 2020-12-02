package com.dz.fulldo.reactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

public class SubReactor implements Runnable {

    // 每个线程负责一个选择器的查询和选择
    final Selector selector;
    SubReactor(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> keySet = selector.selectedKeys();
                for (SelectionKey key: keySet) {
                    // 反应器负责dispatch收到的事件
                    dispatch(key);
                }
                keySet.clear();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    void dispatch(SelectionKey sk) {
        Handler handler = (Handler) sk.attachment();
        if (null != handler) {
            handler.process();
        }
    }
}
