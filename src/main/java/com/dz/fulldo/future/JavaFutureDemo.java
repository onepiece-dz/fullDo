package com.dz.fulldo.future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class JavaFutureDemo {
    private static final Logger logger = LoggerFactory.getLogger("flag");
    private static final int SLEEP_GAP = 500;

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
                logger.info("清洗异常中断了");
            } catch (Exception e) {
                return false;
            }
            logger.info("清洗结束");
            return true;
        }
    }

    static void drinkTea(boolean warterOk, boolean cupOk) {
        if (warterOk && cupOk) {
            logger.info("泡茶喝");
        } else if (!warterOk) {
            logger.info("烧水失败了，可能没煤气了");
        } else if (!cupOk) {
            logger.info("杯子洗不了，可能被打碎了");
        }
    }

    public static void main(String[] args) {
        Callable<Boolean> hJob = new HotWarterJob();
        FutureTask<Boolean> hTask = new FutureTask<>(hJob);
        Thread hTread = new Thread(hTask);

        Callable<Boolean> wJob = new WashJob();
        FutureTask<Boolean> wTask = new FutureTask<>(wJob);
        Thread wTread = new Thread(wTask);

        hTread.start();
        wTread.start();
        Thread.currentThread().setName("主线程");
        try {
            boolean warterOk = hTask.get();
            boolean cupOk = wTask.get();
            drinkTea(warterOk, cupOk);
        } catch (InterruptedException e) {
            logger.info(Thread.currentThread().getName()+"发送异常被中断");
        } catch (ExecutionException e) {
            logger.info("失败了");
        }

        logger.info(Thread.currentThread().getName() +"运行结束.");
    }
}
