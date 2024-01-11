package com.example.crypto.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ScheduledThreadPoolExecutor;

@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration {

    @Value("${crypto.executor.core-pool-size}")
    private Integer corePoolSize;

    @Value("${crypto.executor.max-pool-size}")
    private Integer maxPoolSize;

    @Bean
    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor() {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor =
                new ScheduledThreadPoolExecutor(corePoolSize);
        scheduledThreadPoolExecutor.setMaximumPoolSize(maxPoolSize);
        return scheduledThreadPoolExecutor;
    }
}
