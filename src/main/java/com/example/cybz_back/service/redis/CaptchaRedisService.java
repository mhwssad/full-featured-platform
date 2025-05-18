package com.example.cybz_back.service.redis;

public interface CaptchaRedisService {
    void saveCaptcha(String email, String captcha);

    String getCaptcha(String email);

    void deleteCaptcha(String email);

    boolean checkCaptcha(String email, String captcha);
}
