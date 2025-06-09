package com.alkaid.emailservice_re.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
@EnableAsync
public class EmailAsyncConfig {
    
    @Bean("emailTaskExecutor")
    public ThreadPoolTaskExecutor emailTaskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(16);
        executor.setKeepAliveSeconds(60);
        executor.setQueueCapacity(256);
        executor.setThreadNamePrefix("Email-Async-");

        // 初始化线程池
        executor.initialize();
        return executor;
    }
}
