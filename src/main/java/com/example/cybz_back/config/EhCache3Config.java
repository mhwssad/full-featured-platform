package com.example.cybz_back.config;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;

@Configuration
@ConditionalOnProperty(name = "cache.type", havingValue = "ehcache", matchIfMissing = true)
// 如果没有其他缓存管理器，则启用 EhCache 缓存
public class EhCache3Config {

    @Value("${default.cache.ehcache.expireTime}")
    private Integer expireTime;

    @Value("${default.cache.ehcache.maxSize}")
    private Integer maxSize;

    @Bean
    public CacheManager ehCache3Manager() {
        // 获取 EhCache 的 CachingProvider
        CachingProvider provider = Caching.getCachingProvider();
        javax.cache.CacheManager cacheManager = provider.getCacheManager();

        // 定义缓存配置
        CacheConfiguration<Object, Object> cacheConfig = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                        Object.class,
                        Object.class,
                        ResourcePoolsBuilder
                                .newResourcePoolsBuilder()
                                .heap(maxSize) // 最大条目数
                )
                .withExpiry(
                        // 使用 ExpiryPolicyBuilder 创建过期策略
                        ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(expireTime))
                )
                .build();

        // 将配置绑定到缓存名称 "myCache"
        javax.cache.configuration.Configuration<Object, Object> jcacheConfig =
                Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfig);
        cacheManager.createCache("ehCache", jcacheConfig);

        // 创建 Spring 的 JCacheCacheManager
        return new JCacheCacheManager(cacheManager);
    }
}
  