package com.ginDriver.main.service;

import com.ginDriver.common.minio.service.MinioComponent;
import com.ginDriver.core.domain.vo.ResultVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

/**
 * @author DueGin
 */
@Slf4j
@Service
public class FileService {

    @Resource
    private MinioComponent minioComponent;

    public ResultVO<Void> upload(FileType fileType, MultipartFile file) {
        String fileName = fileType.name;
        String objName = uploadWithType(fileType, file);
        String objectUrl = minioComponent.getObjectUrl(fileName, objName);
        return ResultVO.ok(objectUrl);
    }

    public String getObjUrl(FileType fileType, String objectName) {
        return minioComponent.getObjectUrl(fileType.name, objectName);
    }

    public String getObjUrl(FileType fileType, String objectName, Integer expire) {
        return minioComponent.getObjectUrl(fileType.name, objectName, expire);
    }


    /**
     * 返回访问地址
     *
     * @param fileType 文件类型
     * @param file     文件
     * @return 对象名字
     */
    public String uploadWithType(FileType fileType, MultipartFile file) {
        String name = fileType.name;
        String objName = file.getName() + "_" + UUID.randomUUID().toString().replaceAll("-", "");
        try {
            minioComponent.putObject(name, objName, file.getInputStream(), file.getContentType());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return objName;
    }

    public Boolean deleteFile(FileType fileType, String objectName){
        return minioComponent.remove(fileType.name, objectName);
    }

    @AllArgsConstructor
    public enum FileType {
        system("system", 1),
        movie("movie", 2),
        media("media", 3),
        other("other", 4);
        public final String name;
        public final Integer value;
    }
}
