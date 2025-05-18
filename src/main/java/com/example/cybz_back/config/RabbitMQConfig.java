package com.example.cybz_back.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // 邮件队列
    @Bean
    public Queue emailQueue() {
        return new Queue("email.queue", true); // durable=true 持久化
    }

    // 邮件交换机
    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange("email.exchange", true, false);
    }

    // 绑定队列到交换机
    @Bean
    public Binding emailBinding(Queue emailQueue, DirectExchange emailExchange) {
        return BindingBuilder.bind(emailQueue)
                .to(emailExchange)
                .with("email.routing.key");
    }

    // JSON消息转换器
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
