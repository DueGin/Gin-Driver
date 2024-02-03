package com.ginDriver.main.config.threadPool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 使用spring自带的线程池
 *
 * @author DueGin
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "gin-pool")
public class ThreadPoolProperties {

    /**
     * 核心线程数
     */
    private int corePoolSize = 8;

    /**
     * 最大线程数
     */
    private int maxPoolSize = 16;

    /**
     * 任务队列的大小
     */
    private int queueCapacity = 200;

    /**
     * 线程前缀名
     */
    private String namePrefix = "task-";

    /**
     * 线程存活时间
     */
    private int keepAliveSeconds = 60;

}