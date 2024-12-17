package cn.xiangstudy.nong;

import cn.xiangstudy.utils.DateUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Builder
@Data
public class Robot {
    private Integer robotID;

    private Integer speed;

    private Integer oldLocation;

    private Integer newLocation;

    private void left() {
        RobotRequest.get(String.format("mgr/orbitalRobot/left?orbitalRobotId=%s&speed=%s", robotID, speed));
    }

    private void right() {
        RobotRequest.get(String.format("mgr/orbitalRobot/right?orbitalRobotId=%s&speed=%s", robotID, speed));
    }

    private void stop() {
        RobotRequest.get(String.format("mgr/orbitalRobot/stop?orbitalRobotId=%s", robotID));
    }

    private Integer location() {
        Integer result = null;
        String body = RobotRequest.get(String.format("mgr/orbitalRobot/distance?orbitalRobotId=%s", robotID));
        JSONObject response = JSON.parseObject(body);
        JSONObject data = response.getJSONObject("data");
        if (data.getBoolean("isOk")) {
            result = data.getInteger("distance");
        }
        return result;
    }


    private void log(String msg) {
        String str = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        System.out.printf("%s %s\n", str, msg);
    }


    public void runAllDistant() throws InterruptedException {
        //判断是否在0点出发
        boolean daoGuoZero = false;

        int timeout = 1000;
        while (true) {

            Thread.sleep(timeout);
            newLocation = location();

            //先走回 起点
            if (!daoGuoZero) {
                if (newLocation == 0) {
                    daoGuoZero = true;
                } else {
                    left();
                    continue;
                }
            }

            timeout = 60000;

            //出去
            right();

            //判断是否到终点
            if (oldLocation != null && Math.abs(newLocation - oldLocation) < 2000) {
                log(String.format("%d->到达终点", robotID));
                break;
            } else {
                oldLocation = newLocation;
                log(String.format("%d->当前位置：%d", robotID, newLocation));
            }

        }

        //回去
        left();
        log(String.format("%d->返回起点", robotID));

    }

    public void setPoint() throws InterruptedException {
        int num = 1;
        int count = 0;
        boolean onStart = false;

        while (true) {
            if (!onStart) {
                newLocation = location();
                if (newLocation == 0) {
                    onStart = true;
                } else {
                    left();
                    Thread.sleep(5000);
                    continue;
                }
            }

            right();
            Thread.sleep(6000);
            stop();
            newLocation = location();
            if (newLocation == -1) {
                while (count < 3) {
                    newLocation = location();
                    count++;
                    Thread.sleep(3000);
                }
                if (newLocation == -1) {
                    left();
                    break;
                }
            }
            if (oldLocation != null && newLocation - oldLocation < 1000) {
                left();
                log(String.format("%d->已经到达终点,正在返回起点", robotID));
                break;
            }
            oldLocation = newLocation;
            setLocation(robotID, newLocation, num);
            log(String.format("%s 设置定位点%d,脉冲:%d", robotID, num, newLocation));
        }
    }

    public void restart(){
        RobotRequest.get(String.format("mgr/orbitalRobot/restart?orbitalRobotId=%s", robotID));
        log(String.format("%s 重启", robotID));
    }

    private static void setLocation(int robotId, Integer distance, Integer num) {
        Map<String, Object> body = new HashMap<>();
        body.put("orbitalRobotId", robotId);
        body.put("coordinate", distance);
        body.put("presetName", "定位点" + num);
        body.put("nameNum", num);
        body.put("num", num);
        body.put("type", 1);
        RobotRequest.post("mgr/orbitalRobot/preset", body);
    }

}

