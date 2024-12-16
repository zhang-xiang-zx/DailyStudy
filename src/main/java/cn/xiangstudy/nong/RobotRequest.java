package cn.xiangstudy.nong;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

import java.util.Map;

/**
 * @author zhangxiang
 * @date 2024-12-16 14:41
 */
public class RobotRequest {

    private static final String HOST = "http://cloud.fznft.com";
    private static final String TOKEN = "gCtaYle1AH7Xjrr7Caf4pa9sbbXuODPhsAPlPrFjHb67TwB8i752LYLepZTX8qiiVjSyQyMR1v2cMFEKcKDjuil3PvJwT/UDFAREJH1bzYoa0SdGPn3mUpFGY2UXlLKnN4nzLWVgIOU1VQLRfMhoAg==";

    public static String get(String url) {
        return HttpUtil.createGet(HOST + "/" + url)
                .header("token", TOKEN)
                .timeout(5000)
                .execute().body();
    }

    public static String post(String url, Map<String,Object> data) {
        HttpRequest header = HttpUtil.createPost(HOST + "/" + url)
                .header("token", TOKEN);
        if (data.isEmpty()){
            header.form(data);
        }
        return header.timeout(5000).execute().body();
    }
}
