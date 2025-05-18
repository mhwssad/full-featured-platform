package com.example.cybz_back.service.cybz.mail.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.example.cybz_back.dto.EmailMessage;
import com.example.cybz_back.exception.FrequentRequestException;
import com.example.cybz_back.mq.producer.RabbitMQProducerService;
import com.example.cybz_back.service.cybz.mail.MailRedisService;
import com.example.cybz_back.service.redis.CaptchaRedisService;
import com.example.cybz_back.service.redis.FrequencyRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailRedisServiceImpl implements MailRedisService {
    private final CaptchaRedisService captchaRedisService;
    private final FrequencyRedisService frequencyRedisService;
    private final RabbitMQProducerService rabbitMQProducerService;


    public void sendCaptchaMail(String email) {
        if (!frequencyRedisService.checkFrequency(email)) {
            throw new FrequentRequestException("请勿频繁发送验证码");
        }
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        captchaRedisService.saveCaptcha(email, lineCaptcha.getCode());
        EmailMessage message = new EmailMessage(email, lineCaptcha.getCode());
        rabbitMQProducerService.sendEmailMessage(message);

    }

}
