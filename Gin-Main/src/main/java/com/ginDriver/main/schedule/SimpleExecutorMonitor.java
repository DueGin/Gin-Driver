package com.ginDriver.main.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author DueGin
 */
@Slf4j
@Component
public class SimpleExecutorMonitor {

    @Resource
    private ThreadPoolTaskExecutor serviceExecutor;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void serviceExecutorMonitor() {
        int size = serviceExecutor.getThreadPoolExecutor().getQueue().size();
        log.info("线程池队列长度 ===> {}", size);
    }
}
