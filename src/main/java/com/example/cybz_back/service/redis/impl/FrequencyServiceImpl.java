package com.example.cybz_back.service.redis.impl;

import com.example.cybz_back.service.redis.FrequencyRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class FrequencyServiceImpl implements FrequencyRedisService {
    @Value("${default.frequency.number}")
    private Integer defaultFrequency;  // 默认的访问频率

    @Value("${default.frequency.frequency-time}")
    private Integer defaultFrequencyTime;  // 默认的访问频率时间

    @Value("${default.frequency.banned}")
    private Integer defaultBanned;  // 默认的封禁时间

    private final RedisTemplate<String, Integer> redisTemplate;
    public static final String FREQUENCY_KEY_PREFIX = "frequency:";
    public static final String BANNED_KEY_PREFIX = "banned:";

    private String getFrequencyKey(String key) {
        return FREQUENCY_KEY_PREFIX + key;
    }

    private String getBannedKey(String key) {
        return BANNED_KEY_PREFIX + key;
    }


    public void saveFrequency(String key) {
        if (isBanned(key)) {
            return;
        }
        String frequencyKey = getFrequencyKey(key);
        Integer currentFrequency = getFrequency(key);
        if (currentFrequency == null) {
            redisTemplate.opsForValue().set(frequencyKey, 1, defaultFrequencyTime, TimeUnit.SECONDS);
        } else {
            // 增加计数并检查是否超过限制
            redisTemplate.opsForValue().increment(frequencyKey);
            if (currentFrequency >= defaultFrequency) {
                banUser(key);
            }
        }

    }

    public Integer getFrequency(String key) {
        return redisTemplate.opsForValue().get(getFrequencyKey(key));
    }

    public void deleteFrequency(String key) {
        redisTemplate.delete(getFrequencyKey(key));
    }

    public boolean checkFrequency(String key) {
        Integer frequency = getFrequency(key);
        if (frequency == null) {
            return true;
        }
        return frequency < defaultFrequency;
    }

    /**
     * 封禁用户
     */
    private void banUser(String key) {
        String bannedKey = getBannedKey(key);
        redisTemplate.opsForValue().set(bannedKey, 1, defaultBanned, TimeUnit.SECONDS);
        // 清除频率计数
        deleteFrequency(key);
    }

    /**
     * 检查用户是否被封禁
     */
    private boolean isBanned(String key) {
        return redisTemplate.hasKey(getBannedKey(key));
    }

    /**
     * 解除封禁
     */
    public void unbanUser(String key) {
        redisTemplate.delete(getBannedKey(key));
    }
}
