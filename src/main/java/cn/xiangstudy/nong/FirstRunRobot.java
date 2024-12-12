package cn.xiangstudy.nong;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.xiangstudy.utils.DateUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 机器人跑全场
 *
 * @author zhangxiang
 * @date 2024-11-29 09:58
 */
public class FirstRunRobot {
    static String tokenValue = "gCtaYle1AH7Xjrr7Caf4pcDfDJPVcZmSfJp02EdiKUc19ewPeJlLDlrlHubSSkVpPYhek1MglnnM8HbSwXzbaCcytuv/sUr9SbtV0YJTSiew46VSMI2aK2x3Qdr2j9XeKa9yIS/ASr54XO8keeJKzg==";
    private static volatile boolean running = true;
    private static Integer sum = 0;
    private static volatile List<Integer> robotIds = new ArrayList<>();
    private static ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
    private static Integer size;
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        init();
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            System.out.println("完成台数：" + sum);
            System.out.println("未完成台数：" + size);
            System.out.println("正在执行机器：" + robotIds);
            if (sum.equals(size)) {
                System.out.println("全部执行完毕");
                running = false;
            }
            if (running) {
                for (int robotId : robotIds) {
                    threadPoolExecutor.submit(() -> firstRun(robotId));
                }
            } else {
                threadPoolExecutor.shutdown();
                scheduledExecutorService.shutdown();
            }
        };

        // 任务
        scheduledExecutorService.scheduleAtFixedRate(task, 0, 2, TimeUnit.MINUTES);
        // 关闭
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            scheduledExecutorService.shutdown();
//            threadPoolExecutor.shutdown();
//            try {
//                if (!scheduledExecutorService.awaitTermination(1, TimeUnit.MINUTES) ||
//                        !threadPoolExecutor.awaitTermination(1, TimeUnit.MINUTES)) {
//                    scheduledExecutorService.shutdownNow();
//                    threadPoolExecutor.shutdownNow();
//                }
//            } catch (Exception e) {
//                scheduledExecutorService.shutdownNow();
//                threadPoolExecutor.shutdownNow();
//                Thread.currentThread().interrupt();
//            }
//        }));
    }

    private static void init() {
        robotIds.add(246);
        robotIds.add(248);
        robotIds.add(249);
        size = robotIds.size();
        for (int robotId : robotIds) {
            map.put(String.valueOf(robotId), 0);
        }
    }

    private static void firstRun(int robotId) {
        Integer oldLocation = map.get(String.valueOf(robotId));
        Integer newLocation = currentLocation(robotId);
        try {
            stop(robotId);
            Thread.sleep(5000);
            right(robotId);
            if (oldLocation != 0) {
                Long lOld = Long.valueOf(oldLocation);
                Long lNew = Long.valueOf(newLocation);
                Long result = lNew - lOld;
                if (result < 2000) {
                    end(robotId);
                }
            }
            put(String.valueOf(robotId), newLocation);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private static void stop(int robotId) {
        HttpResponse execute = HttpUtil.createGet("http://cloud.fznft.com/mgr/orbitalRobot/stop?orbitalRobotId=" + robotId)
                .header("Token", tokenValue)
                .execute();
        String s = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(robotId + "号：" + s + " stop");
    }

    private static void right(int robotId) {
        HttpResponse execute = HttpUtil.createGet("http://cloud.fznft.com/mgr/orbitalRobot/right?orbitalRobotId=" + robotId + "&speed=10")
                .header("Token", tokenValue)
                .execute();
        String s = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(robotId + "号：" + s + " right");
    }

    private static void left(int robotId) {
        String s = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(robotId + "号：" + s + " left");
    }

    private static Integer currentLocation(int robotId) {
        Integer result = null;
        HttpResponse execute = HttpUtil.createGet("http://cloud.fznft.com/mgr/orbitalRobot/distance?orbitalRobotId=" + robotId)
                .header("Token", tokenValue)
                .execute();

        String body = execute.body();

        JSONObject jsonObject = JSON.parseObject(body);
        String o = String.valueOf(jsonObject.get("data"));
        JSONObject jsonObject1 = JSON.parseObject(o);
        String isOk = jsonObject1.getString("isOk");
        if (isOk.equals("true")) {
            result = jsonObject1.getInteger("distance");
        }
        System.out.println(robotId + "机器人当前脉冲位置：" + result);
        return result;
    }

    private static void put(String key, Integer value) {
        map.put(key, value);
    }

    private static void end(int robotId) {
        lock.lock();
        sum++;
        robotIds.remove(Integer.valueOf(robotId));
        size--;
        lock.unlock();
    }
}
