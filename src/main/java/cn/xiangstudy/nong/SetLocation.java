package cn.xiangstudy.nong;

import cn.xiangstudy.utils.DateUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 机器按照时间设置定位点
 * @author zhangxiang
 * @date 2024-11-29 14:41
 */
public class SetLocation {
    private static volatile boolean running = true;
    private static volatile Integer sum = 0;
    private static volatile List<Integer> robotIds = new ArrayList<>();
    private static volatile Map<String, String> map = new HashMap<>();
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
        robotIds.add(111);
        robotIds.add(112);
//        robotIds.add(113);
        size = robotIds.size();
        for (int robotId : robotIds) {
            map.put(String.valueOf(robotId), "0");
        }
    }

    private static void single(int robotId) {
        try{
            int i = 0;
            while (true){
                String oldLocation = map.get(String.valueOf(robotId));
                String newLocation;
                stop(robotId);
                Thread.sleep(1500);
                right(robotId);
                Thread.sleep(3500);
                stop(robotId);
                Thread.sleep(1000);
                newLocation = currentLocation(robotId);
                System.out.println(robotId + "：" +oldLocation + " , " + newLocation);
                long r = Long.parseLong(newLocation) - Long.parseLong(oldLocation);
                if(r > 0){
                    setLocation(robotId,newLocation);
                    System.out.println(robotId + "设置了："+newLocation);
                }
                if(!oldLocation.equals("0")){
//                    long r = Long.parseLong(newLocation) - Long.parseLong(oldLocation);
                    if(r == 0){
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
        String s = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(robotId + "号：" + s + " stop");
    }

    private static void right(int robotId) {
        String s = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(robotId + "号：" + s + " right");
    }

    private static void setLocation(int robotId,String location) {
        String s = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(robotId + "号：" + s + " setLocation");
    }

    private static String currentLocation(int robotId) {
        if(robotId == 111) {
            return "0";
        }
        return "1000";
    }

    private synchronized static void put(String key, String value) {
        map.put(key, value);
    }
}
