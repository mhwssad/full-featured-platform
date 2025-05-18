package com.example.cybz_back.mq.producer.impl;

import com.example.cybz_back.dto.EmailMessage;
import com.example.cybz_back.mq.producer.RabbitMQProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQProducerServiceImpl implements RabbitMQProducerService {
    private final RabbitTemplate rabbitTemplate;

    public void sendEmailMessage(EmailMessage emailMessage) {
        rabbitTemplate.convertAndSend(
                "email.exchange",
                "email.routing.key",
                emailMessage
        );
    }
}
