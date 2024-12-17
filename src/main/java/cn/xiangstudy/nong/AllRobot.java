package cn.xiangstudy.nong;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhangxiang
 * @date 2024-12-16 15:14
 */
public class AllRobot {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> robotIds = Lists.newArrayList(231, 232, 233, 234, 235, 236);
        CountDownLatch countDownLatch = new CountDownLatch(robotIds.size());
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (Integer robotId : robotIds) {
            Robot build = Robot.builder().robotID(robotId).speed(10).build();
            new Thread(() -> {
//                try {
//                    build.runAllDistant();
//                    countDownLatch.countDown();
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
                build.restart();
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        stopwatch.stop();
        System.out.printf("全部运行结束，耗时->%s", stopwatch);

//        try {
//            findOrbitalRobotId();
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }


    }

    private static void findOrbitalRobotId() throws UnsupportedEncodingException {
        String keyword = "2号楼4";
        keyword = URLEncoder.encode(keyword, "UTF-8");
        Integer farmId = 284;
        Integer limit = 10;
        Integer page = 1;
        String url = String.format("mgr/orbitalRobot/list?keyword=%s&farmId=%d&limit=%d&page=%d", keyword, farmId, limit, page);
        String body = RobotRequest.get(url);
        List<JSONObject> list = JSONObject.parseObject(body).getJSONObject("data").getJSONArray("list").toJavaList(JSONObject.class);
//        list.stream().map(jsonObject -> jsonObject.get("orbitalRobotId").toString()).forEach(System.out::println);
//        List<Object> collect = list.stream().flatMap(item -> Stream.of(item.get("name"), item.get("orbitalRobotId"))).collect(Collectors.toList());
        // 1:在线，0:不在线
        List<Map<String, Object>> collect = list.stream().filter(obj -> obj.getInteger("isOnline") == 1)
                .map(obj -> {
                    Map<String, Object> one = new HashMap<>();
                    one.put("name", obj.getString("name"));
                    one.put("orbitalRobotId", obj.getString("orbitalRobotId"));
                    return one;
                }).collect(Collectors.toList());
        System.out.println(collect);
        collect.stream().map(obj -> obj.get("orbitalRobotId")).forEach(a -> System.out.printf("%s ", a));
    }

    private static void login() {

        InputStream inputStream = HttpUtil.createGet("http://cloud.fznft.com/sys/captcha.jpg?t=1734403503580")
                .timeout(5000)
                .execute().bodyStream();

    }
}
