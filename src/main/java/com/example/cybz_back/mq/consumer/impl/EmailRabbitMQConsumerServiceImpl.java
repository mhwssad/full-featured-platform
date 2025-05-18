package com.example.cybz_back.mq.consumer.impl;

import com.example.cybz_back.dto.EmailMessage;
import com.example.cybz_back.exception.EmailProcessingException;
import com.example.cybz_back.mq.consumer.EmailRabbitMQConsumerService;
import com.example.cybz_back.service.redis.FrequencyRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailRabbitMQConsumerServiceImpl implements EmailRabbitMQConsumerService {
    private final JavaMailSender mailSender;
    private final FrequencyRedisService frequencyRedisService;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @RabbitListener(queues = "email.queue")
    public void processEmailMessage(EmailMessage emailMessage) {
        try {
            // 构造简单邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(emailMessage.getEmail());
            String subject = "【次元宝藏】您的验证码";
            String content = "【次元宝藏】您的验证码为：" + emailMessage.getCaptcha() + "，验证码有效时间为5分钟。" + "如非本人请忽略。";
            message.setSubject(subject);
            message.setText(content);

            // 发送邮件
//            mailSender.send(message);

            // 记录频率
            frequencyRedisService.saveFrequency(emailMessage.getEmail());

        } catch (MailAuthenticationException e) {
            throw new EmailProcessingException("邮件认证失败: " + emailMessage.getEmail(), e);
        } catch (MailSendException e) {
            throw new EmailProcessingException("邮件发送失败: " + emailMessage.getEmail(), e);
        } catch (Exception e) {
            throw new EmailProcessingException("处理邮件消息时发生未知错误: " + emailMessage.getEmail(), e);
        }
    }
}
