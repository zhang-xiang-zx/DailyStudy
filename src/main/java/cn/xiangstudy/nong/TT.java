package cn.xiangstudy.nong;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 机器人设置定位点
 * @author zhangxiang
 * @date 2024-09-12 11:38
 */
@Slf4j
public class TT {
    static String tokenValue = "";

    public static void main(String[] args) throws InterruptedException {

//        192, 193
//        int[] robotID = { 192, 193 ,200 ,201 ,202 ,203 ,205 ,206 ,210 ,212 ,213 ,215 ,217 ,226 ,227 ,228 ,229 ,230 ,238 ,240 ,244 ,245 ,246 ,247 ,248 ,249 ,251};
//        for(int id : robotID){
//            int i = 1;
//            single(i,id);
//        }

        //111
        int[] robotID = {115, 114, 113, 112};
//        for(int id : robotID){
//            log.info("{}机器在运行",id);
//            int i = 1;
//            single(i,id);
//
//        }

        ExecutorService executorService = Executors.newFixedThreadPool(robotID.length);

        // 提交任务
        for (int id : robotID) {
            int i = 1;
            log.info("{}机器在运行", id);
            executorService.submit(() -> {
                try {
                    single(i, id);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // 关闭线程池
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

    }

    private static void single(int i, int robotID) throws InterruptedException {
//        int i = 1;
        Integer resultDistance = null;
        while (true) {
            Integer distance = selectDistance(robotID);
            if (resultDistance != null) {
                int r = distance - resultDistance;
                if (Math.abs(r) < 2700) {
                    break;
                }
            }
            resultDistance = distance;
            if (distance != null) {
                Map<String, Object> body = new HashMap<>();
                body.put("orbitalRobotId", robotID);
                body.put("coordinate", distance);
                body.put("presetName", "定位点" + i);
                body.put("nameNum", i);
                body.put("num", i);
                body.put("type", 1);

                create(body);

                Thread.sleep(10000);

                right(robotID);
//                left(robotID);

                Thread.sleep(10000);

                stop(robotID);

                Thread.sleep(8500);
                i++;
            }
        }

        log.info("{}机器结束", robotID);
    }

    // 查询当前位置
    private static int selectDistance(int robotID) {
        Integer result = null;
        HttpResponse execute = HttpUtil.createGet("http://cloud.fznft.com/mgr/orbitalRobot/distance?orbitalRobotId=" + robotID)
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
        log.info("机器{}：返回的距离：{}", robotID, result);
        return result;
    }

    private static void right(int robotID) {
        HttpResponse execute = HttpUtil.createGet("http://cloud.fznft.com/mgr/orbitalRobot/right?orbitalRobotId=" + robotID + "&speed=20")
                .header("Token", tokenValue)
                .execute();
    }

    private static void left(int robotID) {
        HttpResponse execute = HttpUtil.createGet("http://cloud.fznft.com/mgr/orbitalRobot/left?orbitalRobotId=" + robotID + "&speed=20")
                .header("Token", tokenValue)
                .execute();
    }

    private static void stop(int robotID) {
        HttpResponse execute = HttpUtil.createGet("http://cloud.fznft.com/mgr/orbitalRobot/stop?orbitalRobotId=" + robotID)
                .header("Token", tokenValue)
                .execute();
    }

    private static void create(Map body) {

        HttpResponse execute = HttpUtil.createPost("http://cloud.fznft.com/mgr/orbitalRobot/preset")
                .form(body)
                .header("Token", tokenValue)
                .execute();
//        log.info("{}", execute.body());
    }
}


