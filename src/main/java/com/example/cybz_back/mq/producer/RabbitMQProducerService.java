package com.example.cybz_back.mq.producer;

import com.example.cybz_back.dto.EmailMessage;

public interface RabbitMQProducerService {
    void sendEmailMessage(EmailMessage emailMessage);
}
