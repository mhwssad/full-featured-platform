package com.example.cybz_back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 使用Jackson2JsonRedisSerializer序列化value
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        // StringRedisSerializer序列化key
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisTemplate<String, Integer> redisTemplateInteger(RedisConnectionFactory factory) {
        RedisTemplate<String, Integer> template = new RedisTemplate<>();

        // 配置键的序列化器为 StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());

        // 配置值的序列化器为 GenericToStringSerializer，确保 Integer 类型正确序列化
        template.setValueSerializer(new GenericToStringSerializer<>(Integer.class));

        // 设置哈希键和哈希值的序列化器（如果需要支持哈希操作）
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericToStringSerializer<>(Integer.class));

        // 设置连接工厂
        template.setConnectionFactory(factory);

        return template;
    }
}
