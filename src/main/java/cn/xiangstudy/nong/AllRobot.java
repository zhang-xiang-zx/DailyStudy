package cn.xiangstudy.nong;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangxiang
 * @date 2024-12-16 15:14
 */
public class AllRobot {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> robotIds = Lists.newArrayList(231);
        CountDownLatch countDownLatch = new CountDownLatch(robotIds.size());
        Stopwatch stopwatch = Stopwatch.createStarted();
        for(Integer robotId : robotIds) {
            Robot build = Robot.builder().robotID(robotId).speed(10).build();
            new Thread(() -> {
                try {
                    build.runAllDistant();
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
        countDownLatch.await();
        stopwatch.stop();
        System.out.printf("全部运行结束，耗时->%s",stopwatch);
    }
}
