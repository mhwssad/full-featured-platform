package com.example.cybz_back.service.redis.impl;

import com.example.cybz_back.service.redis.CaptchaRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CaptchaRedisServiceImpl implements CaptchaRedisService {

    private static final String CAPTCHA_KEY_PREFIX = "captcha:";

    @Value("${default.captcha.expiration}")
    private long expiration;


    private final StringRedisTemplate stringRedisTemplate;

    private String generateRedisKey(String email) {
        return CAPTCHA_KEY_PREFIX + email;
    }

    @Override
    public void saveCaptcha(String email, String captcha) {
        stringRedisTemplate.opsForValue().set(generateRedisKey(email), captcha, expiration, TimeUnit.SECONDS);
    }

    @Override
    public String getCaptcha(String email) {
        String redisKey = generateRedisKey(email);
        return stringRedisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public void deleteCaptcha(String email) {
        stringRedisTemplate.delete(generateRedisKey(email));
    }

    @Override
    public boolean checkCaptcha(String email, String captcha) {
        String redisKey = generateRedisKey(email);
        String redisCaptcha = stringRedisTemplate.opsForValue().get(redisKey);
        deleteCaptcha(email);
        return captcha.equals(redisCaptcha);
    }
}
