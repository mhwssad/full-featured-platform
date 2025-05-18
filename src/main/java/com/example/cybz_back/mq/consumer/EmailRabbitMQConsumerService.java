package com.example.cybz_back.mq.consumer;

import com.example.cybz_back.dto.EmailMessage;

public interface EmailRabbitMQConsumerService {
    void processEmailMessage(EmailMessage emailMessage);
}
