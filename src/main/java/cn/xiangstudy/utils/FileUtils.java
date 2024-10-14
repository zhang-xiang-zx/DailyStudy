package cn.xiangstudy.utils;

import java.io.*;
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

    /**
     * @description: 写入文本内容
     * @author: zhangxiang
     * @date: 2024/10/14 16:04
     * @param: [filePath, content]
     * @return: void
     */
    public static boolean write(String filePath, String content) throws IOException{
        File file = new File(filePath);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream,StandardCharsets.UTF_8));
        bufferedWriter.write(content);
//        bufferedWriter.newLine(); 新行
        bufferedWriter.close();
        fileOutputStream.close();
        return true;
    }

}

