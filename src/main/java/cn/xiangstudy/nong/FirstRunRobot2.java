package cn.xiangstudy.nong;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.xiangstudy.utils.DateUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 机器人跑全场
 *
 * @author zhangxiang
 * @date 2024-11-29 09:58
 */
@Slf4j
public class FirstRunRobot2 {

    public final static String tokenValue = "gCtaYle1AH7Xjrr7Caf4pa9sbbXuODPhsAPlPrFjHb67TwB8i752LYLepZTX8qiiVjSyQyMR1v2cMFEKcKDjuil3PvJwT/UDFAREJH1bzYoa0SdGPn3mUpFGY2UXlLKnN4nzLWVgIOU1VQLRfMhoAg==";

    public static void main(String[] args) throws InterruptedException {

        List<Integer> robotIdList = Lists.newArrayList(1, 2, 3);
        CountDownLatch countDownLatch = new CountDownLatch(robotIdList.size());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("任务开始,任务数:{}", robotIdList.size());

        for (Integer robotId : robotIdList) {

            new Thread(() -> {
                log.info("轨道机开始{}", robotId);

                boolean success = false;
                try {
                    success = doIt(robotId);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                log.info("轨道机结束{},结果:{}", robotId, success);

                countDownLatch.countDown();

            }).start();

        }

        countDownLatch.await();
        stopWatch.stop();
        log.info("任务结束,耗时:{} 毫秒", stopWatch.getTotalTimeMillis());

    }

    private static boolean doIt(Integer robotId) throws InterruptedException {

        boolean result = false;
        Integer oldLocation = null;
        //判断是否在0点出发
        boolean daoGuoZero = false;

        int timeout = 1000;
        while (true) {

            Thread.sleep(timeout);
            Integer location = location(robotId);

            //先走回 起点
            if (!daoGuoZero) {
                if (location == 0) {
                    daoGuoZero = true;
                } else {
                    left(robotId);
                    continue;
                }
            }

            timeout = 10000;

            //出去
            right(robotId);

            //判断是否到终点
            if (oldLocation != null && Math.abs(oldLocation - location) < 2000) {
                log.info("到达终点{}", robotId);
                break;
            } else {
                oldLocation = location;
            }

        }

        //回家
        left(robotId);
        log.info("开回起点{}", robotId);


        return result;


    }

    private static final String HOST = "http://cloud.fznft.com";
    private static final Integer SPEED = 10;


    private static void stop(int robotId) {
        get(String.format("mgr/orbitalRobot/stop?orbitalRobotId=%s", robotId));
    }

    private static void right(int robotId) {
        get(String.format("mgr/orbitalRobot/right?orbitalRobotId=%s&speed=%s", robotId, SPEED));
    }

    private static void left(int robotId) {
        get(String.format("mgr/orbitalRobot/left?orbitalRobotId=%s&speed=%s", robotId, SPEED));
    }

    private static Integer location(int robotId) {
        Integer result = null;
        String body = get(String.format("mgr/orbitalRobot/distance?orbitalRobotId=%s", robotId));
        JSONObject response = JSON.parseObject(body);
        JSONObject data = response.getJSONObject("data");
        if (data.getBoolean("isOk")) {
            result = data.getInteger("distance");
        }
        return result;
    }

    private static String get(String url) {
        return HttpUtil.createGet(HOST + "/" + url)
                .header("Token", tokenValue)
                .timeout(5000)
                .execute()
                .body();
    }


}
