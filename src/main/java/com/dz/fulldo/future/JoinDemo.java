package com.dz.fulldo.future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JoinDemo {
    private static final Logger logger = LoggerFactory.getLogger("flag");
    private static final int SLEEP_GAP = 500;
    static class HotWarterThread extends Thread {
        public HotWarterThread() {
            super("烧水-thread");
        }

        @Override
        public void run() {
            try {
                logger.info("洗好水壶");
                logger.info("灌上凉水");
                logger.info("放在火上");
                Thread.sleep(SLEEP_GAP);
                logger.info("水开了");
            } catch (Exception e) {
                logger.info("热水异常中断了");
            }
            logger.info("热水结束");
        }
    }

    static class WashThread extends Thread {
        public WashThread() {
            super("清洗-Thread");
        }

        @Override
        public void run() {
            try {
                logger.info("洗茶壶");
                logger.info("洗茶杯");
                logger.info("拿茶叶");
                Thread.sleep(SLEEP_GAP);
                logger.info("洗完了");
            } catch (Exception e) {
                logger.info("清洗异常中断了");
            }
            logger.info("清洗结束");
        }
    }

    public static void main(String[] args) {
        Thread hThread = new HotWarterThread();
        Thread wThread = new WashThread();
        hThread.start();
        wThread.start();

        try {
            hThread.join();
            wThread.join();
            Thread.currentThread().setName("主线程");
            logger.info("泡一壶好茶");
        } catch (InterruptedException e) {
            logger.info(Thread.currentThread().getName()+"异常中断");
        }
        logger.info(Thread.currentThread().getName()+"运行结束");
    }
}
