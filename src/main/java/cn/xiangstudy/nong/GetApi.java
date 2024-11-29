package cn.xiangstudy.nong;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author zhangxiang
 * @date 2024-11-06 11:29
 */
public class GetApi {


    public static String isGet(String url, Map<String, Object> map) {
        HttpResponse execute = HttpUtil.createGet(URL + url)
                .header("appId", APP_ID)
                .header("appSecret", APP_SECRET)
                .form(map).execute();
        return execute.body();
    }

    public static String isPost(String url, String str) {
        HttpResponse execute = HttpUtil.createPost(URL + url)
                .header("appId", APP_ID)
                .header("appSecret", APP_SECRET)
                .body(str).execute();
        return execute.body();
    }
}

