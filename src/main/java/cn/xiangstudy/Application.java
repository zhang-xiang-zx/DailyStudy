package cn.xiangstudy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhangxiang
 * @date 2024-09-12 16:05
 */
@SpringBootApplication
@MapperScan("cn.xiangstudy.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}

