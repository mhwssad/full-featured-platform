package com.example.cybz_back.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnProperty(name = "cache.type", havingValue = "caffeine") // 如果配置 cache.type=caffeine，则启用 Caffeine 缓存
public class CaffeineCacheConfig {
    @Value("${default.cache.caffe.expireTime}")
    private Integer expireTime;

    @Value("${default.cache.caffe.maxSize}")
    private Integer maxSize;

    @Bean
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(expireTime, TimeUnit.MINUTES) // 设置缓存过期时间
                .maximumSize(maxSize)); // 设置缓存最大容量
        return cacheManager;
    }
}
