package cn.xiangstudy.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author zhangxiang
 * @date 2024-10-11 17:35
 */
public class FileUtils {

    /**
     * @description: 读取文本内容
     * @author: zhangxiang
     * @date: 2024/10/14 15:31
     * @param: [filePath]
     * @return: java.lang.String
     */
    public static String read(String filePath) throws IOException{
        File file = new File(filePath);
        InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null){
            result.append(line);
        }
        bufferedReader.close();
        inputStreamReader.close();
        return result.toString();
    }
}

