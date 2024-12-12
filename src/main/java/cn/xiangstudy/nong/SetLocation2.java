package cn.xiangstudy.nong;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.xiangstudy.utils.DateUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * 机器按照时间设置定位点
 *
 * @author zhangxiang
 * @date 2024-11-29 14:41
 */
public class SetLocation2 {
    private static final String URL = "http://cloud.fznft.com";
    private static final Integer SPEED = 8;
    private static final String TOKEN = "gCtaYle1AH7Xjrr7Caf4pcDfDJPVcZmSfJp02EdiKUc19ewPeJlLDlrlHubSSkVpPYhek1MglnnM8HbSwXzbaCcytuv/sUr9SbtV0YJTSiew46VSMI2aK2x3Qdr2j9XeKa9yIS/ASr54XO8keeJKzg==";
    private static Map<Integer, Long> locationMap = new ConcurrentHashMap<>();


    public static void main(String[] args) throws InterruptedException {
        List<Integer> robotIds = Lists.newArrayList(220);
        CountDownLatch countDownLatch = new CountDownLatch(robotIds.size());
        Stopwatch stopwatch = Stopwatch.createStarted();

        for (int robotId : robotIds) {
            new Thread(() -> {
                try {
                    log(String.format("%s 开始任务", robotId));
                    single(robotId);
                    locationMap.remove(robotId);
                    countDownLatch.countDown();
                    log(String.format("%s 结束任务", robotId));
                    log(String.format("还在运行的机器：%s", locationMap.toString()));

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

        countDownLatch.await();
        stopwatch.stop();
        log("全部运行结束，耗时" + stopwatch);
//        ExecutorService executorService = Executors.newFixedThreadPool(5);
//        for (int robotId : robotIds) {
//            executorService.submit(() -> {
//                single(robotId);
//            });
//        }
//        executorService.shutdown();
    }

    private static void init() {

    }

    private static void single(int robotId) throws InterruptedException {
        int num = 1;
        int count = 0;
        boolean onStart = false;
        while (true) {
            Long firstLocation = currentLocation(robotId);
            if (!onStart) {
                if (firstLocation == 0) {
                    onStart = true;
                    locationMap.put(robotId, 0L);
                } else {
                    left(robotId);
                    Thread.sleep(5000);
                    continue;
                }
            }

            right(robotId);
            Thread.sleep(6000);
            stop(robotId);
            Long currentLocation = currentLocation(robotId);
            if (currentLocation == -1) {
                while (count < 3) {
                    currentLocation = currentLocation(robotId);
                    count++;
                    Thread.sleep(3000);
                }
                if (currentLocation == -1) {
                    left(robotId);
                    break;
                }
            }
            Long oldLocation = locationMap.get(robotId);
            if (currentLocation - oldLocation < 1000) {
                left(robotId);
                break;
            }
            log(String.format("%s 设置定位点%d,脉冲:%d",robotId,num,currentLocation));
            setLocation(robotId, currentLocation, num++);
            locationMap.put(robotId, currentLocation);
        }
    }

    private static String get(String url) {
        return HttpUtil.createGet(URL + url)
                .header("token", TOKEN)
                .execute().body();
    }

    private static String post(String url, Map<String, Object> info) {
        HttpRequest header = HttpUtil.createPost(URL + url).header("token", TOKEN);
        if (!info.isEmpty()) {
            header.form(info);
        }
        return header.execute().body();
    }

    private static void stop(int robotId) {
        get(String.format("/mgr/orbitalRobot/stop?orbitalRobotId=%d", robotId));
    }

    private static void right(int robotId) {
        get(String.format("/mgr/orbitalRobot/right?orbitalRobotId=%d&speed=%d", robotId, SPEED));
    }

    private static void left(int robotId) {
        get(String.format("/mgr/orbitalRobot/left?orbitalRobotId=%d&speed=%d", robotId, SPEED));
    }

    private static void setLocation(int robotId, Long distance, Integer num) {
        Map<String, Object> body = new HashMap<>();
        body.put("orbitalRobotId", robotId);
        body.put("coordinate", distance);
        body.put("presetName", "定位点" + num);
        body.put("nameNum", num);
        body.put("num", num);
        body.put("type", 1);
        post("/mgr/orbitalRobot/preset", body);
    }

    private static Long currentLocation(int robotId) {
        Long result = -1L;
        String bodyStr = get(String.format("/mgr/orbitalRobot/distance?orbitalRobotId=%d", robotId));
        JSONObject data = JSON.parseObject(bodyStr).getJSONObject("data");
        Boolean isOk = data.getBoolean("isOk");
        if (isOk) {
            result = data.getLong("distance");
        }
        return result;
    }

    private static void log(String msg) {
        String date = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.printf("%s %s%n", date, msg);
    }
}
