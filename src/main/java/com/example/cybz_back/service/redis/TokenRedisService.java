package com.example.cybz_back.service.redis;

public interface TokenRedisService {
    void saveToken(String token, Long userId);

    Long getUserId(String token);

    String getToken(Long userId);

    void deleteUserid(String token);

    void deleteUserid(Long userId);

    boolean checkToken(String token);

    boolean checkUserId(Long userId);

}
