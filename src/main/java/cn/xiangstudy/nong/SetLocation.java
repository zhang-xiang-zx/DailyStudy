package cn.xiangstudy.nong;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.xiangstudy.utils.DateUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 机器按照时间设置定位点
 * @author zhangxiang
 * @date 2024-11-29 14:41
 */
public class SetLocation {
    static String tokenValue = "gCtaYle1AH7Xjrr7Caf4pcDfDJPVcZmSfJp02EdiKUc19ewPeJlLDlrlHubSSkVpPYhek1MglnnM8HbSwXzbaCcytuv/sUr9SbtV0YJTSiew46VSMI2aK2x3Qdr2j9XeKa9yIS/ASr54XO8keeJKzg==";
    private static volatile boolean running = true;
    private static volatile Integer sum = 0;
    private static volatile List<Integer> robotIds = new ArrayList<>();
    private static volatile Map<String, Long> map = new HashMap<>();
    private static volatile Integer size;
    public static void main(String[] args) {
        init();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(int robotId : robotIds) {
            executorService.submit(() -> {
                single(robotId);
            });
        }
        executorService.shutdown();
    }

    private static void init() {
        robotIds.add(242);
        size = robotIds.size();
        for (int robotId : robotIds) {
            map.put(String.valueOf(robotId), 0L);
        }
    }

    private static void single(int robotId) {
        try{
            int i = 0;
            int locationNum = 1;
            while (true){
                Long oldLocation = map.get(String.valueOf(robotId));
                Long newLocation;
                stop(robotId);
                Thread.sleep(3500);
                right(robotId);
                Thread.sleep(10500);
                stop(robotId);
                Thread.sleep(3000);
                newLocation = currentLocation(robotId);
                long r = newLocation - oldLocation;
                if(r > 0){
                    setLocation(robotId,newLocation,locationNum);
                    locationNum++;
                }
                if(oldLocation != 0){
//                    long r = Long.parseLong(newLocation) - Long.parseLong(oldLocation);
                    if(r < 1000){
                        break;
                    }
                }else{
                    i++;
                    if(i > 3){
                        break;
                    }
                }
                put(String.valueOf(robotId), newLocation);
            }
        }catch (Exception e){
            e.printStackTrace();
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
        HttpResponse execute = HttpUtil.createGet("http://cloud.fznft.com/mgr/orbitalRobot/right?orbitalRobotId=" + robotId + "&speed=8")
                .header("Token", tokenValue)
                .execute();
        String s = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(robotId + "号：" + s + " right");
    }

    private static void setLocation(int robotId,Long distance,Integer locationNum) {
        Map<String, Object> body = new HashMap<>();
        body.put("orbitalRobotId", robotId);
        body.put("coordinate", distance);
        body.put("presetName", "定位点" + locationNum);
        body.put("nameNum", locationNum);
        body.put("num", locationNum);
        body.put("type", 1);
        HttpResponse execute = HttpUtil.createPost("http://cloud.fznft.com/mgr/orbitalRobot/preset")
                .form(body)
                .header("Token", tokenValue)
                .execute();
        String s = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(robotId + "号：" + s + "设置了点位：" + locationNum + "脉冲：" + distance);
    }

    private static Long currentLocation(int robotId) {
        Long result = null;
        HttpResponse execute = HttpUtil.createGet("http://cloud.fznft.com/mgr/orbitalRobot/distance?orbitalRobotId=" + robotId)
                .header("Token", tokenValue)
                .execute();

        String body = execute.body();

        JSONObject jsonObject = JSON.parseObject(body);
        String o = String.valueOf(jsonObject.get("data"));
        JSONObject jsonObject1 = JSON.parseObject(o);
        String isOk = jsonObject1.getString("isOk");
        if (isOk.equals("true")) {
            result = jsonObject1.getLong("distance");
        }
        System.out.println(robotId + "机器人当前脉冲位置：" + result);
        return result;
    }

    private synchronized static void put(String key, Long value) {
        map.put(key, value);
    }
}
