package cn.xiangstudy.hik.controller;

import cn.xiangstudy.hik.HCNetSDK;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhangxiang
 * @date 2024-10-11 14:46
 */
@Service
public class oneServiceImpl implements oneService {
    static HCNetSDK hCNetSDK = null;
    static int lUserID = -1;

    @Override
    public void getVideo(String ip, String user, String password, String filePath, Date startTime, Date endTime, int channel) {

        if (hCNetSDK == null) {
            if (!createSDKInstance()) {
                System.out.println("Load SDK fail");
                return;
            }
        }
        //SDK初始化，一个程序只需要调用一次
        boolean initSuc = hCNetSDK.NET_DVR_Init();

        //设备登录
        lUserID = loginDevice(ip, (short) 8000, user, password);

        boolean result = downVideo(startTime, endTime, filePath, channel);
        System.out.println("最终结果：" + result);
    }


    private static boolean createSDKInstance() {
        if (hCNetSDK == null) {
            synchronized (HCNetSDK.class) {
                String strDllPath = "";
                try {
                    strDllPath = "C:\\soft\\hik\\HCNetSDK.dll"; //绝对路径
                    hCNetSDK = (HCNetSDK) Native.loadLibrary(strDllPath, HCNetSDK.class);
                } catch (Exception ex) {
                    System.out.println("loadLibrary: " + strDllPath + " Error: " + ex.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 登录设备，支持 V40 和 V30 版本，功能一致。
     *
     * @param ip   设备IP地址
     * @param port SDK端口，默认为设备的8000端口
     * @param user 设备用户名
     * @param psw  设备密码
     * @return 登录成功返回用户ID，失败返回-1
     */
    private static int loginDevice(String ip, short port, String user, String psw) {
        // 创建设备登录信息和设备信息对象
        HCNetSDK.NET_DVR_USER_LOGIN_INFO loginInfo = new HCNetSDK.NET_DVR_USER_LOGIN_INFO();
        HCNetSDK.NET_DVR_DEVICEINFO_V40 deviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V40();

        // 设置设备IP地址
        byte[] deviceAddress = new byte[HCNetSDK.NET_DVR_DEV_ADDRESS_MAX_LEN];
        byte[] ipBytes = ip.getBytes();
        System.arraycopy(ipBytes, 0, deviceAddress, 0, Math.min(ipBytes.length, deviceAddress.length));
        loginInfo.sDeviceAddress = deviceAddress;

        // 设置用户名和密码
        byte[] userName = new byte[HCNetSDK.NET_DVR_LOGIN_USERNAME_MAX_LEN];
        byte[] password = psw.getBytes();
        System.arraycopy(user.getBytes(), 0, userName, 0, Math.min(user.length(), userName.length));
        System.arraycopy(password, 0, loginInfo.sPassword, 0, Math.min(password.length, loginInfo.sPassword.length));
        loginInfo.sUserName = userName;

        // 设置端口和登录模式
        loginInfo.wPort = port;
        loginInfo.bUseAsynLogin = false; // 同步登录
        loginInfo.byLoginMode = 0; // 使用SDK私有协议

        // 执行登录操作
        int userID = hCNetSDK.NET_DVR_Login_V40(loginInfo, deviceInfo);
        if (userID == -1) {
            System.err.println("登录失败，错误码为: " + hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println(ip + " 设备登录成功！");
            // 处理通道号逻辑
            int startDChan = deviceInfo.struDeviceV30.byStartDChan;
            System.out.println("预览起始通道号: " + startDChan);
        }
        return userID; // 返回登录结果
    }

    private static boolean downVideo(Date startTime, Date endTime, String filePath, int channel) {
        int i = hCNetSDK.NET_DVR_GetFileByTime(lUserID, channel, getHKTime(startTime), getHKTime(endTime), filePath);

        System.out.println("获取nvr结果：" + i);

        if (i >= 0) {
            boolean downloadFlag = hCNetSDK.NET_DVR_PlayBackControl(i, hCNetSDK.NET_DVR_PLAYSTART, 0, null);
            int tmp = -1;
            IntByReference pos = new IntByReference();
            while (true) {
                boolean backFlag = hCNetSDK.NET_DVR_PlayBackControl(i, hCNetSDK.NET_DVR_PLAYGETPOS, 0, pos);
                if (!backFlag) {//防止单个线程死循环
                    return downloadFlag;
                }
                int produce = pos.getValue();
                if ((produce % 10) == 0 && tmp != produce) {//输出进度
                    tmp = produce;
                    System.out.println("hksdk(视频)-视频下载进度:" + "==" + produce + "%");
                }
                if (produce == 100) {//下载成功
                    hCNetSDK.NET_DVR_StopGetFile(i);
                    i = -1;
                    return true;
                }
                if (produce > 100) {//下载失败
                    hCNetSDK.NET_DVR_StopGetFile(i);
                    i = -1;
                    System.out.println("hksdk(视频)-海康sdk由于网络原因或DVR忙,下载异常终止!错误原因:" + hCNetSDK.NET_DVR_GetLastError());
                    return false;
                }
            }
        } else {
            System.out.println("hksdk(视频)-下载失败：" + hCNetSDK.NET_DVR_GetLastError());
            return false;
        }
    }

    private static HCNetSDK.NET_DVR_TIME getHKTime(Date time) {
        HCNetSDK.NET_DVR_TIME structTime = new HCNetSDK.NET_DVR_TIME();
        String str = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);
        String[] times = str.split("-");
        structTime.dwYear = Integer.parseInt(times[0]);
        structTime.dwMonth = Integer.parseInt(times[1]);
        structTime.dwDay = Integer.parseInt(times[2]);
        structTime.dwHour = Integer.parseInt(times[3]);
        structTime.dwMinute = Integer.parseInt(times[4]);
        structTime.dwSecond = Integer.parseInt(times[5]);
        return structTime;
    }
}

