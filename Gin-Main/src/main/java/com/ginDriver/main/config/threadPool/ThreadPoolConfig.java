package com.ginDriver.main.config.threadPool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author DueGin
 */
@Slf4j
@EnableAsync
@Configuration
public class ThreadPoolConfig {
    @Resource
    private ThreadPoolProperties properties;

    // todo 做成那种在配置文件配置map，然后遍历每种线程池类型生成线程池，不同类型线程池可以自定义配置参数
    @Bean(name = "serviceExecutor")
    public Executor serviceExecutor() {
        System.out.println(properties.getCorePoolSize());
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 最大线程数
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        // 核心线程数
        executor.setCorePoolSize(properties.getCorePoolSize());
        // 任务队列的大小
        executor.setQueueCapacity(properties.getQueueCapacity());
        // 线程前缀名
        executor.setThreadNamePrefix(properties.getNamePrefix());
        // 线程存活时间
        executor.setKeepAliveSeconds(properties.getKeepAliveSeconds());

        /*
          拒绝处理策略
          CallerRunsPolicy()：交由调用方线程运行，比如 main 线程。
          AbortPolicy()：直接抛出异常。
          DiscardPolicy()：直接丢弃。
          DiscardOldestPolicy()：丢弃队列中最老的任务。
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        //线程初始化
        executor.initialize();
        return executor;
    }
}
