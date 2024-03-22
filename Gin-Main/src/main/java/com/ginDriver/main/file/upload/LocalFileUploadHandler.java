package com.ginDriver.main.file.upload;


import com.ginDriver.main.file.FileManager;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.file.constants.UploadStatus;
import com.ginDriver.main.file.domain.dto.ChunkDTO;
import com.ginDriver.main.file.generator.LocalFilePathGenerator;
import com.ginDriver.main.file.utils.EncoderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LocalFileUploadHandler extends FileUploadHandler {


    LocalFileUploadHandler() {
        filePathGenerator = new LocalFilePathGenerator();
    }

    /**
     * @param chunkDto 分片DTO
     * @return 状态码
     */
    @Override
    public UploadStatus upload(ChunkDTO chunkDto) {
        String path;
        // 如果存储路径没有在配置文件配置，则用路径生成器生成
        if (FileManager.storagePath != null) {
            path = FileManager.storagePath;
        } else {
            // 执行路径生成策略
            path = filePathGenerator.generate();
        }

        Long userId = SecurityUtils.getUserId();

        // 分片存储路径
        String chunkPath = path + "chunks/";
        // 完整文件存储路径
        String filePath = path + userId + "/";

        // 获取分片信息
        MultipartFile file = chunkDto.getUpload();
        Integer chunks = chunkDto.getChunks();
        Integer chunk = chunkDto.getChunk();
        String name = chunkDto.getName();

        BufferedOutputStream os = null;
        UploadStatus res = UploadStatus.ERROR_UNKNOWN;


        try {
            // 对分片进行存储
            if (file != null && name != null) {
                if (chunks != null && chunk != null) {
                    // 分片存储
                    String tmpFileName = name + "_" + chunk;
//                    String tmpFileName = chunkDto.getMd5();
                    File tmpFile = new File(chunkPath, tmpFileName);

                    if (!tmpFile.exists()) {
                        // 没有文件夹则创建
                        if (!tmpFile.getParentFile().exists()) {
                            tmpFile.getParentFile().mkdirs();
                        }
                        file.transferTo(tmpFile); // 保存分片
                        log.info(name + "分片" + chunkDto.getChunk() + "上传成功！");
                    }

                    chunkDto.setMd5(EncoderUtil.fileToMd5(tmpFile));
                    chunkDto.setRealPath(chunkPath + tmpFileName);

                    // 存入集合列表中
                    FileManager.uploaded
                            .computeIfAbsent(userId, k -> new ConcurrentHashMap<>())
                            .computeIfAbsent(name, k -> new ArrayList<>())
                            .add(chunkDto);

                    res = UploadStatus.CHUNK_SUCCESS;

                } else {
                    // 非分片文件
                    File wholeFile = new File(filePath, name); // 通过md5命名文件
                    // 没有文件夹则创建
                    if (!wholeFile.getParentFile().exists()) {
                        wholeFile.getParentFile().mkdirs();
                    }

                    // 保存文件
                    file.transferTo(wholeFile);

                    String md5 = EncoderUtil.fileToMd5(wholeFile);

                    // 存入集合
                    FileManager.wholeFiles
                            .computeIfAbsent(userId, k -> new ConcurrentHashMap<>())
                            .put(name, new com.ginDriver.main.file.domain.po.File(name, md5, userId));

                    res = UploadStatus.SUCCESS_WHOLE;
                }

                // 文件合并
                // 在最后一个分片等待所有分片完成上传
                if (chunk != null && chunks != null && chunk == chunks - 1) {
                    File wholeFile = new File(filePath, name);
                    os = new BufferedOutputStream(new FileOutputStream(wholeFile));

                    // 读取存储的分片
                    for (int i = 0; i < chunks; i++) {
                        File tmpFile = new File(chunkPath, name + "_" + i);
                        while (!tmpFile.exists()) {
                            TimeUnit.MILLISECONDS.sleep(500);
                        }
                        byte[] bytes = FileUtils.readFileToByteArray(tmpFile);
                        os.write(bytes);
                        os.flush();
//                        tmpFile.delete();
                    }
                    res = UploadStatus.SUCCESS_END;
                    os.flush();

                    String md5 = EncoderUtil.fileToMd5(wholeFile);

                    // 将完整的文件也存入集合列表
                    FileManager.wholeFiles
                            .computeIfAbsent(userId, k -> new ConcurrentHashMap<>())
                            .put(name, new com.ginDriver.main.file.domain.po.File(name, md5, userId));

                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return res;
    }

}
