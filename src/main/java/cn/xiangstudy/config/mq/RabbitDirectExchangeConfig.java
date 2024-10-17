package cn.xiangstudy.config.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 直连交换机（Direct Exchange）
 * @author zhangxiang
 * @date 2024-10-17 10:36
 */
@Slf4j
@Configuration
public class RabbitDirectExchangeConfig {

    private Queue directQueue;

    private DirectExchange directExchange;

    @Bean
    public Queue directQueue() {
        directQueue = new Queue("zxDirectQueue", true);
        return directQueue;
    }

    @Bean
    public DirectExchange directExchange() {
        directExchange = new DirectExchange("zxDirectExchange", true, false);
        return directExchange;
    }

    @Bean
    public Binding bindDirect() {
        return BindingBuilder.bind(directQueue).to(directExchange).with("directRouting");
    }
}

