package com.ginDriver.main.schedule;

import com.ginDriver.main.constant.FileType;
import com.ginDriver.main.domain.dto.dustbin.DustbinRemoveJobDTO;
import com.ginDriver.main.domain.po.Dustbin;
import com.ginDriver.main.service.DustbinService;
import com.ginDriver.main.service.manager.DustbinManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author DueGin
 */
@Slf4j
@Component
public class DustbinJob {
    @Resource
    private DustbinService dustbinService;

    @Resource
    private DustbinManager dustbinManager;

    private static final BlockingQueue<DustbinRemoveJobDTO> DELETE_QUEUE = new LinkedBlockingQueue<>();

    @Scheduled(cron = "0 0 1 * * ?")
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

            List<DustbinRemoveJobDTO> jobDTOList = list.stream().map(d -> {
                DustbinRemoveJobDTO dto = new DustbinRemoveJobDTO();
                dto.setIds(Collections.singletonList(d.getId()));
                FileType fileType = FileType.getFileTypeByIdx(d.getType());
                // 文件类型无法确定的，不做处理
                if (fileType == null) {
                    log.error("清理垃圾箱时文件类型有误 ==> {}", d);
                    return null;
                }
                dto.setFileType(fileType.ordinal());
                dto.setRemoveTime(d.getCreateTime().plusDays(30));
                return dto;
            }).collect(Collectors.toList());

            DELETE_QUEUE.addAll(jobDTOList);
        }

    }

    public void consume() {
        new Thread(() -> {
            log.info("垃圾桶消费者启动..");
            try {
                while (true) {
                    // 当队列中有元素时才消费
                    DustbinRemoveJobDTO dustbin = DELETE_QUEUE.take();
                    if (dustbin == null) {
                        continue;
                    }
                    LocalDateTime delTime = dustbin.getRemoveTime();
                    LocalDateTime now = LocalDateTime.now();
                    if (now.isBefore(delTime)) {
                        long seconds = Duration.between(now, delTime).toSeconds();
                        TimeUnit.SECONDS.sleep(seconds);
                    }
                    // 执行删除
                    dustbinManager.remove(dustbin);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
