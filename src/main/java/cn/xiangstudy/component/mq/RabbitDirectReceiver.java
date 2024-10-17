package cn.xiangstudy.component.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zhangxiang
 * @date 2024-10-17 11:18
 */
@Slf4j
@Component
@RabbitListener(queues = "zxDirectQueue")
public class RabbitDirectReceiver {

    @RabbitHandler
    public void process(String msg){
        log.info("第一台消息：{}",msg);
    }
}

