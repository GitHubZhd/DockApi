package com.pengshu.crawler.config;

import lombok.Setter;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Created by ps on 2017/7/27.
 */
@Configuration
@ConfigurationProperties(prefix = "executor.config")
public class TaskExecutorConfig implements AsyncConfigurer {
    @Setter
    private int corePoolSize;
    @Setter
    private int maxPoolSize;
    @Setter
    private int queueCapacity;
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor=new ThreadPoolTaskExecutor();
        //线程池维护线程的最少数量
        taskExecutor.setCorePoolSize(corePoolSize);
        //线程池维护线程的最大数量
        taskExecutor.setMaxPoolSize(maxPoolSize);
        //线程池所使用的缓冲队列
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
