package com.dz.fulldo.future;

import com.google.common.util.concurrent.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GuavaFutureDemo {
    private static final Logger logger = LoggerFactory.getLogger("flag");
    private static final int SLEEP_GAP = 500;

    public static  String getCurThreadName() {
        return Thread.currentThread().getName();
    }

    static class HotWarterJob implements Callable<Boolean> {
        @Override
        public Boolean call() throws Exception {
            try {
                logger.info("洗好水壶");
                logger.info("灌上凉水");
                logger.info("放在火上");
                Thread.sleep(SLEEP_GAP);
                logger.info("水开了");
            } catch (Exception e) {
                logger.info("热水异常中断了");
                return false;
            }
            logger.info("热水结束");
            return true;
        }
    }

    static class WashJob implements Callable<Boolean> {
        @Override
        public Boolean call() throws Exception {
            try {
                logger.info("洗茶壶");
                logger.info("洗茶杯");
                logger.info("拿茶叶");
                Thread.sleep(SLEEP_GAP);
                logger.info("洗完了");
            } catch (Exception e) {
                logger.info("清洗异常中断了");
                return false;
            }
            logger.info("清洗结束");
            return true;
        }
    }

    static class MainJob implements Runnable {
        boolean warterOk = false;
        boolean cupOk = false;
        int gap = SLEEP_GAP / 10;

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(gap);
                    logger.info("读书中。。。");
                } catch (InterruptedException e) {
                    logger.info(getCurThreadName() + "发送异常被中中断");
                }

                if (warterOk && cupOk) {
                    drinkTea(warterOk, cupOk);
                }
            }
        }

        public void drinkTea(boolean warterOk, boolean cupOk) {
            if (warterOk && cupOk) {
                logger.info("泡茶喝");
            } else if (!warterOk) {
                logger.info("烧水失败了，可能没煤气了");
            } else if (!cupOk) {
                logger.info("杯子洗不了，可能被打碎了");
            }
        }
    }

    public static void main(String[] args) {
        // 创建一个新的线程十六，作为泡茶主线程
        MainJob mainJob = new MainJob();
        Thread mainThread = new Thread(mainJob);
        mainThread.setName("主线程");
        mainThread.start();

        // 烧水的业务逻辑实例
        Callable<Boolean> hotJob = new HotWarterJob();
        // 清洗的业务逻辑实例
        Callable<Boolean> washJob = new WashJob();

        // 创建java线程池
        ExecutorService jPool = Executors.newFixedThreadPool(10);

        // 包装java线程池
        ListeningExecutorService gPool = MoreExecutors.listeningDecorator(jPool);

        // 提交烧水的业务逻辑实例，到guava线程池获取异步任务
        ListenableFuture<Boolean> hotFuture = gPool.submit(hotJob);
        ListenableFuture<Boolean> washFuture = gPool.submit(washJob);
        Futures.addCallback(hotFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(@Nullable Boolean r) {
                if (r) {
                    mainJob.warterOk = true;
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                logger.info("烧水失败了，没有茶喝了");
            }
        }, gPool);

        Futures.addCallback(washFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(@Nullable Boolean r) {
                if (r) {
                    mainJob.cupOk = true;
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                logger.info("杯子洗失败了，没有茶喝了");
            }
        }, gPool);
    }
}
