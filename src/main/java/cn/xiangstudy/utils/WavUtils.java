package cn.xiangstudy.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.sound.sampled.*;
import java.io.*;

/**
 * @author zhangxiang
 * @date 2024-10-14 17:14
 */
public class WavUtils {

    public static void playByResourcePath(String resourcePath) throws Exception{
        Resource resource = new ClassPathResource(resourcePath);

        InputStream inputStream = resource.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        play(bufferedInputStream);
    }

    public static void play(InputStream inputStream) throws Exception{
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        // 音频秒数（微秒）
        long microsecondLength = clip.getMicrosecondLength();
        long second = microsecondLength / 1_000_000 * 1000;
        clip.start();
        Thread.sleep(second);
    }

}

