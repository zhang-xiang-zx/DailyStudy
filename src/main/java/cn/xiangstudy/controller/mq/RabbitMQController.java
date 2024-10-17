package cn.xiangstudy.controller.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangxiang
 * @date 2024-10-17 11:15
 */
@RestController
@RequestMapping("/mq")
public class RabbitMQController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendDirect")
    public String sendDirectMessage(String str) {
        rabbitTemplate.convertAndSend("zxDirectExchange", "directRouting", str);
        return "success";
    }

    @GetMapping("/send")
    public String sendTopicMsg(String exchange, String routingKey, String str) {
        rabbitTemplate.convertAndSend(exchange, routingKey, str);
        return "success";
    }
}

