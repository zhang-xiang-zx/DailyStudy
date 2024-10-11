package cn.xiangstudy.hik.controller;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author zhangxiang
 * @date 2024-10-11 14:45
 */
public interface oneService {

    public void getVideo(String ip, String admin, String password, String filePath, Date startTime, Date endTime, int channel);
}
