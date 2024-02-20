package com.ginDriver.main.schedule;

import com.ginDriver.main.domain.po.Dustbin;
import com.ginDriver.main.service.DustbinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author DueGin
 */
@Slf4j
@Component
public class DustbinJob {
    @Resource
    private DustbinService dustbinService;

    private static final BlockingQueue<Dustbin> DELETE_QUEUE = new LinkedBlockingQueue<>();

    @Scheduled(cron = "0 * * * * ?")
    public void checkAndDeleteExpire() {
        LocalDateTime thirtyDayBefore = LocalDateTime.now().minusDays(30);
        List<Dustbin> list = dustbinService.getBaseMapper()
                .selectAllByDate(thirtyDayBefore.getYear(),
                        thirtyDayBefore.getMonthValue(),
                        thirtyDayBefore.getDayOfMonth()
                );

        if (!list.isEmpty()) {
            // 正序
            list.sort((d1, d2) -> {
                if (d1.getCreateTime().equals(d2.getCreateTime())) {
                    return 0;
                }
                return d1.getCreateTime().isBefore(d2.getCreateTime()) ? -1 : 1;
            });

            DELETE_QUEUE.addAll(list);
        }

    }

    public void consume() {
        new Thread(() -> {
            log.info("垃圾桶消费者启动..");
            try {
                while (true) {
                    // 当队列中有元素时才消费
                    Dustbin dustbin = DELETE_QUEUE.take();
                    LocalDateTime delTime = dustbin.getCreateTime();
                    LocalDateTime thirtyDayBefore = LocalDateTime.now().minusDays(30);
                    if(thirtyDayBefore.isBefore(delTime)) {
                        long seconds = Duration.between(thirtyDayBefore, delTime).toSeconds();
                        TimeUnit.SECONDS.sleep(seconds);
                    }
                    // 执行删除
                    dustbinService.removeById(dustbin);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
