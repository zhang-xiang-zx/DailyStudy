package cn.xiangstudy.tt;


import cn.hutool.core.text.StrSplitter;
import com.fazecast.jSerialComm.SerialPort;

import java.util.Arrays;
import java.util.List;

/**
 * @author zhangxiang
 * @date 2024-10-29 12:04
 */
public class Port {
    public static void main(String[] args) {
        List<SerialPort> list = Arrays.asList(SerialPort.getCommPorts());
        for (SerialPort serialPort : list) {
            System.out.println(serialPort);
            if (serialPort.getSystemPortName().equals("COM1")) {
                boolean b = openSerialPort(serialPort);
                if (b) {
                    read(serialPort);
                    String str = verifyPath();
                    sendData(serialPort, str);
                }
            }
        }
    }

    private static void sendData(SerialPort port, String data) {
        byte[] bytes = data.getBytes();
        port.writeBytes(bytes, bytes.length);
    }

    private static boolean openSerialPort(SerialPort port) {
        boolean isOpen;
        int baudRate = 9600; //波特率
        int parity = SerialPort.EVEN_PARITY; // 校验位
        int dataBits = 8; // 数据位
        int stopBits = SerialPort.ONE_STOP_BIT; // 停止位
        if (!port.isOpen()) {
            boolean a = port.setComPortParameters(baudRate, dataBits, stopBits, parity);
            boolean b = port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 20000, 0);
            isOpen = port.openPort();
            System.out.println("开启：" + isOpen);
            return isOpen;
        } else {
            System.out.println("失败");
            isOpen = false;
        }
        return isOpen;
    }

    private static String verifyPath() {
        String str = "55011000000000";
        List<String> list = Arrays.asList(StrSplitter.splitByLength(str, 2));
        int sum = 0;
        for (String o : list) {
            int i = Integer.parseInt(o, 16);
            sum += i;
//            System.out.println(o + "--->" + i);
        }
//        System.out.println(sum);

        String hexString = Integer.toHexString(sum % 256);
        if (hexString.length() <= 1) {
            hexString = "0" + hexString;
        }
        System.out.println(hexString);

        return str + hexString;
    }

    private static void read(SerialPort port) {
        new Thread(() -> {
            while (true) {
                if (port.isOpen()) {
                    byte[] readBuff = new byte[1024];
                    int num = port.readBytes(readBuff, readBuff.length);
                    if (num > 0) {
                        for (int i = 0; i < num; i++) {
                            System.out.println(readBuff[i]);
                        }
                    }
                }
            }
        }).start();
    }
}

