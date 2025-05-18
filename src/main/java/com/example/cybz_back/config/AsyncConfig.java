package com.example.cybz_back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean("taskExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数（默认保持活跃的线程数）
        executor.setCorePoolSize(10);
        // 最大线程数（队列满时扩容的最大值）
        executor.setMaxPoolSize(50);
        // 队列容量（超过核心线程数时，新任务进入队列）
        executor.setQueueCapacity(100);
        // 线程名前缀
        executor.setThreadNamePrefix("Async-Thread-");
        // 空闲线程存活时间（秒）
        executor.setKeepAliveSeconds(60);
        // 拒绝策略（默认抛异常）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
