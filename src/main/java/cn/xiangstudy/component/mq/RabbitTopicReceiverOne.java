package cn.xiangstudy.component.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zhangxiang
 * @date 2024-10-17 14:55
 */
@Slf4j
@Component
@RabbitListener(queues = "zxTopicQueue")
public class RabbitTopicReceiverOne {

    @RabbitHandler
    public void process(String msg){
        log.info("zxTopicQueue收到：{}",msg);
    }
}

