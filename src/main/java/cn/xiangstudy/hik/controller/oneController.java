package cn.xiangstudy.hik.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhangxiang
 * @date 2024-10-11 11:08
 */
@RestController
@RequestMapping("/tt")
public class oneController {

    @Autowired
    private oneService oneService;

    /**
     * @description: 需要配置正确的sdk的lib路径，在官网下载的sdk中的库文件就可以当成sdk；还要记得demo示例的lib中找到examples.jar和jna.jar
     * @author: zhangxiang
     * @date: 2024/10/11 15:40
     * @param: []
     * @return: void
     */
    @GetMapping("/video")
    public void getVideo(){
        Date startTime;
        Date endTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            startTime = simpleDateFormat.parse("20241008172000");
            endTime = simpleDateFormat.parse("20241008172200");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String ip = "192.168.0.75";
        String user = "admin";
        String password = "aa123456";
//        String filePath = "D:\\StudyCode\\BackEnd\\JavaCode\\Maven\\StudyDemo\\DailyStudy\\Download\\a.mp4";
        String filePath = "C:\\soft\\video\\testOne.mp4";
        int channel = 33;
        System.out.println("调用到video");
        oneService.getVideo(ip,user,password,filePath,startTime,endTime,channel);
    }
}

