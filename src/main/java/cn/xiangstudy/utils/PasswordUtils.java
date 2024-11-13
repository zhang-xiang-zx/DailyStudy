package cn.xiangstudy.utils;

import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author zhangxiang
 * @date 2024-11-13 11:11
 */
public class PasswordUtils {

    public static String encryptPassword(String password) {
        String salt = UUID.randomUUID().toString().substring(0, 20);
        String addSalt = salt + password;
        return salt + encryptMD5(addSalt);
    }

    public static boolean checkPassword(String password, String encryptPassword) {
        String salt = encryptPassword.substring(0, 20);
        String addSalt = salt + password;
        String encryptPwd = encryptMD5(addSalt);
        System.out.println("li:"+encryptPwd);
        return encryptPassword.equals(salt + encryptPwd);
    }

    private static String encryptMD5(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

}
