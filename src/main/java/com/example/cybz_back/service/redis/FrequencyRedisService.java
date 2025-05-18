package com.example.cybz_back.service.redis;

public interface FrequencyRedisService {
    void saveFrequency(String key);

    Integer getFrequency(String key);

    void deleteFrequency(String key);

    boolean checkFrequency(String key);

    void unbanUser(String key);
}
