package cn.xiangstudy.tt;
/**
 * 可用
 * @author zhangxiang
 * @date 2024-09-24 16:02
 */
import java.io.IOException;

public class FrpController {
    private static final String FRPC_PATH = "C:\\soft\\frp\\frpc.exe";
    private static final String CONFIG_PATH = "C:\\soft\\frp\\frpc.toml";

    public static void main(String[] args) {
        stopFrpProcess();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startFrpProcess();
        System.out.println("success");
    }

    /**
     * 停止正在运行的frpc进程
     */
    private static void stopFrpProcess() {
        try {
            Process exec = Runtime.getRuntime().exec("taskkill /F /IM frpc.exe");
            exec.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to stop frpc process.");
            e.printStackTrace();
        }
    }

    /**
     * 启动frpc进程
     * 在Java中使用Runtime.exec()方法时，传入命令的方式有两种：一种是传入一个字符串数组，另一种是传入一个单一的字符串。
     * 但是，这两种方式在功能上有所不同，特别是当涉及到带有参数的命令时。
     * 当你使用字符串数组形式时，可以更清晰地定义每个单独的参数，这对于带有多个参数或者标志的命令尤其有用。
     * 例如，在你的例子中，frpc.exe是主命令，-c是选项标志，而frpc.toml是配置文件的路径。
     * 这种方式可以让Java正确地解析这些参数，即使它们包含空格或其他特殊字符。
     */
    private static void startFrpProcess() {
        try {
            String[] cmd = {FRPC_PATH, "-c", CONFIG_PATH};
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            System.err.println("Failed to start frpc with config file.");
            e.printStackTrace();
        }
    }
}

