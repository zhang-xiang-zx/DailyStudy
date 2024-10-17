package cn.xiangstudy.config.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 主题交换机（Topic Exchange）
 * @author zhangxiang
 * @date 2024-10-17 14:22
 */
@Configuration
public class RabbitTopicExchangeConfig {

    private Queue topicQueueOne;
    private Queue topicQueueTwo;

    private TopicExchange topicExchange;

    @Bean
    public Queue topicQueue(){
        topicQueueOne = new Queue("zxTopicQueue",true);
        return topicQueueOne;
    }

    @Bean
    public Queue topicQueueTwo(){
        topicQueueTwo = new Queue("xzTopicQueue",true);
        return topicQueueTwo;
    }

    @Bean
    public TopicExchange topicExchange(){
        topicExchange = new TopicExchange("zxTopicExchange");
        return topicExchange;
    }

    @Bean
    public Binding bindingTopic(){
        return BindingBuilder.bind(topicQueueOne).to(topicExchange).with("zhang.*");
    }

    @Bean
    public Binding bindingTopicTwo(){
        return BindingBuilder.bind(topicQueueTwo).to(topicExchange).with("zhang.#");
    }
}

