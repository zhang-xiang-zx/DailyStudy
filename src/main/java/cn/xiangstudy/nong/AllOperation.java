package cn.xiangstudy.nong;

import cn.xiangstudy.pojo.RobotInfo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author zhangxiang
 * @date 2025-06-19 15:31
 */
public class AllOperation {

    public static void main(String[] args) {

        // 0: 不在线; 1: 在线
        Integer isOnline = 1;

        List<RobotInfo> executeRobotList = new ArrayList<>();

        List<RobotInfo> robotInfoList;
        try {
            System.out.println("查找轨道机IP：");
            Scanner scanner = new Scanner(System.in);
            String key = scanner.next();
            robotInfoList = RobotConfig.findOrbitalRobotId(key, isOnline);

            robotInfoList.forEach(System.out::println);


        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

//        List<Integer> robotIdList = new ArrayList<>();

        System.out.println("添加操作机器人:");
        Scanner scanner = new Scanner(System.in);

        while (true) {

            int input = scanner.nextInt();

            if (input == 0) {
                System.out.println("退出添加");
                break;
            }

            RobotInfo singleRobotInfo = robotInfoList.stream().filter(obj -> obj.getRobotId().equals(input)).findFirst().get();

            executeRobotList.add(singleRobotInfo);

//            robotIdList.add(input);
            System.out.println("添加: " + input);


        }

//        robotIdList.forEach(System.out::println);

        System.out.println("选择操作:\n1: 跑全场\n2: 设置定位点\n3：跑全场设置定位点");
        switch (scanner.nextInt()) {
            case 1:
                System.out.println("first run");
                FirstRunRobot2 robotFirstRun = new FirstRunRobot2();
                robotFirstRun.RobotFirstRun(executeRobotList);
                break;
            case 2:
                System.out.println("set location");
                SetLocation3 setLocation = new SetLocation3();
                setLocation.robotSetLocation(executeRobotList);
                break;
            case 3:
                System.out.println("all setLactation");
                SetLocation4 setLocation4 = new SetLocation4();
                setLocation4.robotSetLocation(executeRobotList);
                break;
            default:
                System.out.println("default");
                break;
        }

    }
}
