package cn.xiangstudy.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhangxiang
 * @date 2024-10-30 9:17
 */
public class StringUtils {

    /**
     * @description: 按指定个数分割字符串为list
     * @author: zhangxiang
     * @date: 2024/10/30 9:37
     * @param: [str, interval]
     * @return: java.util.List<java.lang.String>
     */
    public static List<String> strSplitToList(String str, int interval) {
        if(InspectUtils.isNull(str)){
            return new ArrayList<>();
        }
        int length = str.length();
        int chunks = (length + interval - 1) / interval;
        String[] strArray = new String[chunks];

        for (int i = 0, j = 0; i < length; i += interval) {
            strArray[j++] = str.substring(i, Math.min(i + interval, length));
        }
        return Arrays.asList(strArray);
    }

}

