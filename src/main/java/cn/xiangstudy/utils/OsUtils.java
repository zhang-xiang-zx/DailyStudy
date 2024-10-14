package cn.xiangstudy.utils;

import cn.xiangstudy.constant.Const;

/**
 * @author zhangxiang
 * @date 2024-10-14 14:23
 */
public class OsUtils {
    private static final String OS = System.getProperty(Const.OS_NAME).toLowerCase();

    public static boolean isWindows(){
        return OS.equals(Const.WINDOWS);
    }

    public static boolean isLinux(){
        return OS.equals(Const.LINUX);
    }

}

