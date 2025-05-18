package com.example.cybz_back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.Map;

@Configuration
@EnableCaching  //开启缓存功能
@ConditionalOnProperty(name = "cache.type", havingValue = "redis") // 如果配置 cache.type=redis，则启用 Redis 缓存
public class RedisCacheConfig {

    @Value("${default.cache.redis.expireTime}")
    private Integer expireTime; // 全局缓存过期时间

    @Value("${default.cache.redis.userExpireTime}")
    private Integer userExpireTime;
    @Value("${default.cache.redis.productExpireTime}")
    private Integer productExpireTime;

    @Bean
    @Primary
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 默认缓存配置
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(expireTime)) // 全局缓存过期时间
                .disableCachingNullValues() // 不缓存 null 值
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer() // 使用 JSON 序列化缓存值
                        )
                );

        // 创建缓存管理器
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultCacheConfig) // 默认配置
                .withInitialCacheConfigurations(Map.of(
                        "users", RedisCacheConfiguration.
                                defaultCacheConfig().
                                entryTtl(Duration.ofMinutes(userExpireTime)), // 为 "users" 缓存单独设置 TTL
                        "products", RedisCacheConfiguration.
                                defaultCacheConfig().
                                entryTtl(Duration.ofHours(productExpireTime))
                ))
                .build();
    }
}
