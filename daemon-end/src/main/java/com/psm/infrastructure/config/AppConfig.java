package com.psm.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@EnableAsync
@Configuration
public class AppConfig {
    /**
     * 异步线程池配置
     *
     * @return ThreadPoolTaskExecutor
     */
    @Bean
    public ThreadPoolTaskExecutor asyncThreadPoolExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数
        threadPoolTaskExecutor.setCorePoolSize(33);

        // 最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(66);

        // 队列容量
        threadPoolTaskExecutor.setQueueCapacity(120);

        // 线程活跃时间（秒）
        threadPoolTaskExecutor.setKeepAliveSeconds(30);

        // 线程名称前缀
        threadPoolTaskExecutor.setThreadNamePrefix("async-thread-");

        // 拒绝策略
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 设置虚拟线程工厂
        threadPoolTaskExecutor.setThreadFactory(runnable -> {
            Thread thread = Thread.ofVirtual().name("async-thread-").unstarted(runnable);
            return thread;
        });

        // 初始化
        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    };
}
