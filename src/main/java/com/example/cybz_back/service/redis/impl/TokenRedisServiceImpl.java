package com.example.cybz_back.service.redis.impl;

import com.example.cybz_back.service.redis.TokenRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenRedisServiceImpl implements TokenRedisService {

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${default.jwt.expiration}")
    private long expiration;

    public static final String TOKEN_KEY_PREFIX = "token:";

    private String generateRedisKey(String email) {
        return TOKEN_KEY_PREFIX + email;
    }

    @Override
    public void saveToken(String token, Long userId) {
        String user = String.valueOf(userId);
        stringRedisTemplate.opsForValue().set(generateRedisKey(token), user, expiration, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(generateRedisKey(user), token, expiration, TimeUnit.SECONDS);
    }

    @Override
    public Long getUserId(String token) {
        String value = stringRedisTemplate.opsForValue().get(generateRedisKey(token));
        return value != null ? Long.valueOf(value) : null; // 处理null
    }

    @Override
    public String getToken(Long userId) {
        return stringRedisTemplate.opsForValue().get(generateRedisKey(String.valueOf(userId)));
    }

    @Override
    public void deleteUserid(String token) {

        stringRedisTemplate.delete(generateRedisKey(token));
        Long userId = getUserId(token);
        if (userId != null) {
            stringRedisTemplate.delete(generateRedisKey(String.valueOf(getUserId(String.valueOf(userId)))));
        }
    }

    @Override
    public void deleteUserid(Long userId) {
        stringRedisTemplate.delete(generateRedisKey(String.valueOf(getUserId(String.valueOf(userId)))));
        String token = getToken(userId);
        if (token != null) {
            stringRedisTemplate.delete(generateRedisKey(token));
        }
    }


    @Override
    public boolean checkToken(String token) {
        return stringRedisTemplate.opsForValue().get(generateRedisKey(token)) != null;
    }

    @Override
    public boolean checkUserId(Long userId) {
        return stringRedisTemplate.opsForValue().get(generateRedisKey(String.valueOf(userId))) != null;
    }
}
