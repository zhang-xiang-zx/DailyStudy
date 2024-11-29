package cn.xiangstudy.nong;

import cn.xiangstudy.utils.DateUtils;

import java.util.*;
import java.util.concurrent.*;

/**
 * 机器人跑全场
 * @author zhangxiang
 * @date 2024-11-29 09:58
 */
public class FirstRunRobot {
    private static volatile boolean running = true;
    private static volatile Integer sum = 0;
    private static volatile List<Integer> robotIds = new ArrayList<>();
    private static volatile Map<String, String> map = new HashMap<>();

    public static void main(String[] args) {
        init();
        int size = robotIds.size();
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            System.out.println("完成台数：" + sum);
            System.out.println("未完成台数：" + size);
            System.out.println("正在执行机器：" + robotIds);
            if (sum == size) {
                System.out.println("全部执行完毕");
                running = false;
            }
            if (running) {
                for (int robotId : robotIds) {
                    threadPoolExecutor.submit(() -> firstRun(robotId));
                }
            }
        };

        // 任务
        scheduledExecutorService.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduledExecutorService.shutdown();
            threadPoolExecutor.shutdown();
            try {
                if (!scheduledExecutorService.awaitTermination(1, TimeUnit.MINUTES) ||
                        !threadPoolExecutor.awaitTermination(1, TimeUnit.MINUTES)) {
                    scheduledExecutorService.shutdownNow();
                    threadPoolExecutor.shutdownNow();
                }
            } catch (Exception e) {
                scheduledExecutorService.shutdownNow();
                threadPoolExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }));
    }

    private static void init() {
        robotIds.add(111);
        robotIds.add(112);
        robotIds.add(113);
        for (int robotId : robotIds) {
            map.put(String.valueOf(robotId), "0");
        }
    }

    private static void firstRun(int robotId) {
        String oldLocation = map.get(String.valueOf(robotId));
        String newLocation = currentLocation(robotId);
        try {
            System.out.println(robotId + " 正在执行");
            stop(robotId);
            Thread.sleep(5000);
            right(robotId);
            if (!oldLocation.equals("0")) {
                Long lOld = Long.valueOf(oldLocation);
                Long lNew = Long.valueOf(newLocation);
                Long result = lOld - lNew;
                if (result == 0) {
//                    sum++;
                    count();
                    robotIds.remove(Integer.valueOf(robotId));
                    System.out.println(robotId + " 已经执行完毕");
                }
            }
            map.put(String.valueOf(robotId), newLocation);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private static void stop(int robotId) {
        String s = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(robotId + "号：" + s + " stop");
    }

    private static void right(int robotId) {
        String s = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(robotId + "号：" + s + " right");
    }

    private static void left(int robotId) {
        String s = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(robotId + "号：" + s + " left");
    }

    private static String currentLocation(int robotId) {
        return "15000";
    }

    private synchronized static void count() {
        sum++;
    }
}
