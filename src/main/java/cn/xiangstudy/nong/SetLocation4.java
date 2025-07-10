package cn.xiangstudy.nong;

import cn.xiangstudy.pojo.RobotInfo;
import cn.xiangstudy.utils.DateUtils;
import com.google.common.base.Stopwatch;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * 机器按照设置定位点
 *
 * @author zhangxiang
 * @date 2024-11-29 14:41
 */
public class SetLocation4 {

    private static Map<Integer, String> locationMap = new ConcurrentHashMap<>();


    public void robotSetLocation(List<RobotInfo> robotInfoList) {

        for (RobotInfo robotInfo : robotInfoList) {
            locationMap.put(robotInfo.getRobotId(), robotInfo.getRobotName());
        }

        CountDownLatch countDownLatch = new CountDownLatch(robotInfoList.size());
        Stopwatch stopwatch = Stopwatch.createStarted();

        for (RobotInfo robot : robotInfoList) {

            String robotName = robot.getRobotName();
            int robotId = robot.getRobotId();

            new Thread(() -> {

                log(String.format("%s --> 开始任务", robotName));

                // 初始位置
                Long firstLocation = RobotConfig.location(robotId);

                try {
                    Thread.sleep(2000);
                    firstLocation = RobotConfig.location(robotId);
                }catch (Exception e){
                    e.printStackTrace();
                }

                log(String.format("%d -> 初始位置: %d", robotId, firstLocation));

                // 跑全场,拿长度
                Long trackDistance = getTrackDistance(robotId);
                log(String.format("%s ->到达终点, 全长: %s", robotId, trackDistance));

                List<Map<Integer, Long>> lactationList = new ArrayList<>();

                // 根据全长计算定位点
                int num = 1;
                while (firstLocation <= trackDistance) {

                    long isOutLocation = firstLocation + 640;

                    if (isOutLocation <= trackDistance) {
                        Map<Integer, Long> map = new HashMap<>();
                        map.put(num++, firstLocation);
                        lactationList.add(map);
                    }

                    firstLocation += 640;
                }

                lactationList.sort((o1, o2) ->
                        Long.compare(
                                o2.values().iterator().next(),
                                o1.values().iterator().next()
                        )
                );

                // 依次去定位点
                for (Map<Integer, Long> map : lactationList) {
                    map.forEach((k, v) -> {

                        RobotConfig.toLocation(robotId, v);

                        try {
                            Thread.sleep(5000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Long currentLocation = RobotConfig.currentLocation(robotId);
                        while (!Objects.equals(currentLocation, v)) {
                            try {
                                Thread.sleep(10000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            currentLocation = RobotConfig.currentLocation(robotId);

                        }

                        // 设置定位点
                        log(String.format("%s ->设置定位点%s: %s", robotId, k, v));
                        RobotConfig.setLocation(robotId, v, k);

                    });

                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // 回去
                RobotConfig.left(robotId);

                locationMap.remove(robotId);
                countDownLatch.countDown();
                log(String.format("%s 结束任务", robotId));
                log(String.format("还在运行的机器：%s", locationMap.toString()));


            }).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        stopwatch.stop();
        log("全部运行结束，耗时" + stopwatch);
    }

    // 跑全长
    private static Long getTrackDistance(int robotId) {

        Long result = 0L;
        Long oldLocation = null;
        boolean daoGuoZero = true;

        int timeout = 2000;

        try {
            while (true) {

                Thread.sleep(timeout);
                Long location = RobotConfig.location(robotId);

                //先走回 起点
                if (!daoGuoZero) {
                    if (location == 0) {
                        daoGuoZero = true;
                    } else {
                        RobotConfig.left(robotId);
                        continue;
                    }
                }

                timeout = 10000;

                //出去
                RobotConfig.right(robotId);

                //判断是否到终点
                if (oldLocation != null && Math.abs(oldLocation - location) < 100) {

                    result = RobotConfig.location(robotId);
                    break;
                } else {
                    oldLocation = location;
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    private static void log(String msg) {
        String date = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.printf("%s %s%n", date, msg);
    }
}
