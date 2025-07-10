package cn.xiangstudy.nong;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.xiangstudy.pojo.RobotInfo;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangxiang
 * @date 2025-06-18 08:56
 */
public class RobotConfig {

    private static final String Token = "";

    private static final String HOST = "http://cloud.fznft.com";

    private static final Integer SPEED = 5;

    public static void stop(int robotId) {
        get(String.format("mgr/orbitalRobot/stop?orbitalRobotId=%s", robotId));
    }

    public static void right(int robotId) {
        get(String.format("mgr/orbitalRobot/right?orbitalRobotId=%s&speed=%s", robotId, SPEED));
    }

    public static void left(int robotId) {
        get(String.format("mgr/orbitalRobot/left?orbitalRobotId=%s&speed=%s", robotId, SPEED));
    }

    public static Long location(int robotId) {
        Long result = 0L;
        String body = get(String.format("mgr/orbitalRobot/distance?orbitalRobotId=%s", robotId));
        JSONObject response = JSON.parseObject(body);
        JSONObject data = response.getJSONObject("data");

        if (data != null && data.getBoolean("isOk")) {
            result = data.getLong("distance");
        }
        return result;
    }

    public static void setLocation(int robotId, Long distance, Integer num) {
        Map<String, Object> body = new HashMap<>();
        body.put("orbitalRobotId", robotId);
        body.put("coordinate", distance);
        body.put("presetName", "定位点" + num);
        body.put("nameNum", num);
        body.put("num", num);
        body.put("type", 1);
        post("/mgr/orbitalRobot/preset", body);
    }

    // 去脉冲
    public static void toLocation(int robotId, Long distance) {
        String url = "/mgr/orbitalRobot/coordinate?orbitalRobotId=" + robotId + "&coordinate=" + distance + "&speed=" + SPEED;
        get(url);
    }

    public static List<RobotInfo> findOrbitalRobotId(String keyword, Integer isOnline) throws UnsupportedEncodingException {

        keyword = URLEncoder.encode(keyword, "UTF-8");
        Integer farmId = 284;
        Integer limit = 10;
        Integer page = 1;
        String url = String.format("mgr/orbitalRobot/list?keyword=%s&farmId=%d&limit=%d&page=%d", keyword, farmId, limit, page);
        String body = get(url);

        List<JSONObject> responseList = JSONObject.parseObject(body).getJSONObject("data").getJSONArray("list").toJavaList(JSONObject.class);

        /**
         // 1:在线，0:不在线
         List<Map<String, Object>> collect = list.stream().filter(obj -> obj.getInteger("isOnline").equals(isOnline))
         .map(obj -> {
         Map<String, Object> one = new HashMap<>();
         one.put("name", obj.getString("name"));
         one.put("orbitalRobotId", obj.getString("orbitalRobotId"));
         one.put("orbitalLength", obj.getLong("orbitalLength"));
         return one;
         }).collect(Collectors.toList());

         collect.forEach(single -> {
         System.out.println(single.get("name") + " ---> " + single.get("orbitalRobotId") + ": " + single.get("orbitalLength"));
         });
         */

        return responseList.stream().filter(obj -> obj.getInteger("isOnline").equals(isOnline))
                .map(obj -> RobotInfo.builder()
                        .robotName(obj.getString("name"))
                        .robotId(obj.getInteger("orbitalRobotId"))
                        .trackDistance(obj.getLong("orbitalLength"))
                        .build()).collect(Collectors.toList());

    }


    private static String get(String url) {
        return HttpUtil.createGet(HOST + "/" + url)
                .header("Token", Token)
                .timeout(5000)
                .execute()
                .body();
    }

    private static String post(String url, Map<String, Object> info) {
        HttpRequest header = HttpUtil.createPost(HOST + url).header("token", Token);
        if (!info.isEmpty()) {
            header.form(info);
        }
        return header.execute().body();
    }

    public static Long currentLocation(int robotId) {
        Long result = -1L;
        String bodyStr = get(String.format("/mgr/orbitalRobot/distance?orbitalRobotId=%d", robotId));
        JSONObject data = JSON.parseObject(bodyStr).getJSONObject("data");
        Boolean isOk = data.getBoolean("isOk");
        if (isOk) {
            result = data.getLong("distance");
        }
        return result;
    }

}
