package com.ginDriver.main.schedule;

import com.ginDriver.main.domain.po.Md5File;
import com.ginDriver.main.service.FileService;
import com.ginDriver.main.service.Md5FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * @author DueGin
 */
@Slf4j
@Component
public class FileJob {

    @Resource
    private Md5FileService md5FileService;

    @Resource
    private FileService fileService;

    @Scheduled(cron = "0 0 2 * * ?")
    public void deleteFileRef0() {
        log.info("deleteFileRef0");
        List<Md5File> md5FileList =  md5FileService.getMd5FileRef0List();
        md5FileList.forEach(md5File -> {
            File file = new File(md5File.getSrc());
            if (file.exists()) {
                file.delete();
            }
            if(StringUtils.isNotBlank(md5File.getObjectName())){
                fileService.deleteFile("media", md5File.getObjectName());
            }
            md5FileService.lambdaUpdate().eq(Md5File::getId, md5File.getId()).remove();
        });
    }
}
