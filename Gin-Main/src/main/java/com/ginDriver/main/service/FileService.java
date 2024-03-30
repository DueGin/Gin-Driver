package com.ginDriver.main.service;

import com.ginDriver.common.minio.service.MinioComponent;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.constant.FileType;
import com.ginDriver.main.domain.po.File;
import com.ginDriver.main.domain.vo.FileVO;
import com.ginDriver.main.mapper.FileMapper;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.UUID;

/**
 * @author DueGin
 */
@Slf4j
@Service
public class FileService extends MyServiceImpl<FileMapper, File> {

    @Resource
    private MinioComponent minioComponent;

    /**
     * 上传文件
     *
     * @param fileType 文件类型(桶名称)
     * @param file     文件
     * @return 对象名字
     */
    public ResultVO<FileVO> upload(FileType fileType, MultipartFile file) {
        String bucketName = fileType.name();
        String objName = uploadWithType(fileType, file);
        String objectUrl = minioComponent.getObjectUrl(bucketName, objName);

        return ResultVO.ok(new FileVO()
                .setUrl(objectUrl)
                .setFileName(objName)
        );
    }


    public void getObjectFile(String bucketName, String objectName, OutputStream os, Double quality, Double scale, Integer height, Integer weight) {
        try (InputStream is = minioComponent.getObject(bucketName, objectName)) {
            Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(is);

            if (quality != null) {
                builder.outputQuality(quality);
            }

            if (scale != null) {
                builder.scale(scale);
            }
            if (weight != null && height != null) {
                builder.size(weight, height);
            }

            builder.toOutputStream(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileBase64(String bucketName, String objectName, Double quality, Double scale, Integer height, Integer weight) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        getObjectFile(bucketName, objectName, os, quality, scale, height, weight);

        // 生成base64
        byte[] thumbnailBytes = os.toByteArray();
        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(thumbnailBytes);
    }

    /**
     * 获取文件URL
     *
     * @param bucketName 桶名称
     * @param objectName 文件名称
     * @return Minio URL
     */
    public String getObjUrl(String bucketName, String objectName) {
        return minioComponent.getObjectUrl(bucketName, objectName);
    }

    /**
     * 获取文件URL
     *
     * @param fileType   文件类型(桶名称)
     * @param objectName 文件名称
     * @return Minio URL
     */
    public String getObjUrl(FileType fileType, String objectName) {
        return minioComponent.getObjectUrl(fileType.name(), objectName);
    }

    /**
     * 获取文件URL
     *
     * @param fileType   文件类型(桶名称)
     * @param objectName 文件名称
     * @param expire     链接过期时间
     * @return Minio URL
     */
    public String getObjUrl(FileType fileType, String objectName, Integer expire) {
        return minioComponent.getObjectUrl(fileType.name(), objectName, expire);
    }

    /**
     * 获取文件URL
     *
     * @param fileType   文件类型(桶名称)
     * @param objectName 文件名称
     * @param expire     链接过期时间
     * @return Minio URL
     */
    public String getObjUrl(String fileType, String objectName, Integer expire) {
        return minioComponent.getObjectUrl(fileType, objectName, expire);
    }


    /**
     * 上传文件
     *
     * @param fileType 文件类型(桶名称)
     * @param file     文件
     * @return 对象名字
     */
    public String uploadWithType(FileType fileType, MultipartFile file) {
        String name = fileType.name();
        String filename = file.getResource().getFilename();
        String objName;
        if (StringUtils.isNotBlank(filename)) {
            String[] fileNameSplit = filename.split("\\.");
            String prefixFilename = fileNameSplit[0];
            for (int i = 1; i < fileNameSplit.length - 1; i++) {
                prefixFilename += fileNameSplit[i];
            }
            objName = prefixFilename + "_" + UUID.randomUUID().toString().replaceAll("-", "") + "." + fileNameSplit[fileNameSplit.length - 1];
        } else {
            objName = UUID.randomUUID().toString().replaceAll("-", "");
        }
        try {
            minioComponent.putObject(name, objName, file.getInputStream(), file.getContentType());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return objName;
    }

    /**
     * 上传文件
     *
     * @param fileType    文件类型(桶名称)
     * @param fileName    文件名
     * @param contentType 文件类型
     * @param is          文件输入流
     * @return 对象名字
     */
    public String uploadWithType(FileType fileType, String fileName, String contentType, InputStream is) {
        String name = fileType.name();
        String objName;
        if (StringUtils.isNotBlank(fileName)) {
            String[] fileNameSplit = fileName.split("\\.");
            String prefixFilename = fileNameSplit[0];
            for (int i = 1; i < fileNameSplit.length - 1; i++) {
                prefixFilename += fileNameSplit[i];
            }
            objName = prefixFilename + "_" + UUID.randomUUID().toString().replaceAll("-", "") + "." + fileNameSplit[fileNameSplit.length - 1];
        } else {
            objName = UUID.randomUUID().toString().replaceAll("-", "");
        }
        minioComponent.putObject(name, objName, is, contentType);

        return objName;
    }

    /**
     * 删除文件
     *
     * @param fileType   文件类型(桶名称)
     * @param objectName 文件名
     * @return {@code true}-执行成功，{@code false}-执行失败
     */
    public Boolean deleteFile(FileType fileType, String objectName) {
        return minioComponent.remove(fileType.name(), objectName);
    }

    /**
     * 删除文件
     *
     * @param bucketName 桶名称
     * @param objectName 文件名
     * @return {@code true}-执行成功，{@code false}-执行失败
     */
    public Boolean deleteFile(String bucketName, String objectName) {
        return minioComponent.remove(bucketName, objectName);
    }

}
