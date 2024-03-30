package com.ginDriver.main.file.upload;


import com.ginDriver.main.file.FileManager;
import com.ginDriver.main.file.constants.UploadStatus;
import com.ginDriver.main.file.domain.dto.ChunkDTO;
import com.ginDriver.main.file.domain.dto.UploadStatusDTO;
import com.ginDriver.main.file.generator.LocalFilePathGenerator;
import com.ginDriver.main.file.utils.EncoderUtil;
import com.ginDriver.main.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author DueGin
 */
@Slf4j
public class LocalFileUploadHandler extends FileUploadHandler {


    LocalFileUploadHandler() {
        super(new LocalFilePathGenerator());
    }

    /**
     * @param chunkDto 分片DTO
     * @return 状态码
     */
    @Override
    public UploadStatusDTO upload(ChunkDTO chunkDto) {
        // 获取分片信息
        MultipartFile file = chunkDto.getFile();
        Integer chunks = chunkDto.getChunks();
        Integer chunk = chunkDto.getChunk();
        String name = chunkDto.getName();
        String uploadId = chunkDto.getUploadId();
        UploadStatus res = UploadStatus.ERROR_UNKNOWN;

        if (file == null || name == null) {
            // 传过来没有文件
            return new UploadStatusDTO(UploadStatus.FAIL_NOT_FOUND_BODY);
        }

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


        BufferedOutputStream os = null;

        try {
            // 分片文件
            if (chunks != null && chunk != null && chunks != 0) {
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

                res = UploadStatus.SUCCESS_CHUNK;

            } else {
                // 非分片文件
                File wholeFile = new File(filePath, name); // 通过md5命名文件
                // 没有文件夹则创建
                if (!wholeFile.getParentFile().exists()) {
                    wholeFile.getParentFile().mkdirs();
                }

                // 保存文件
                file.transferTo(wholeFile);

                return new UploadStatusDTO(UploadStatus.SUCCESS_END, filePath + name, chunkDto.getMd5());
            }

            // 获取已上传的分片数量
            Map<String, List<ChunkDTO>> userUploadMap = FileManager.uploaded.get(userId);
            int size = 0;
            if (userUploadMap != null) {
                List<ChunkDTO> chunkDTOS = userUploadMap.get(uploadId);
                if (chunkDTOS != null) {
                    size = chunkDTOS.size();
                }
            }

            // 分片收集完成后，才开始合并
            if (chunks == size) {
                File wholeFile = new File(filePath, name);
                FileOutputStream fileOutputStream = new FileOutputStream(wholeFile);
                os = new BufferedOutputStream(fileOutputStream);

                // 读取存储的分片
                for (int i = 0; i < chunks; i++) {
                    File tmpFile = new File(chunkPath, name + "_" + i);
                    while (!tmpFile.exists()) {
                        log.error("分片文件不存在 ==> 文件路径: {}", chunkPath + name + "_" + i);
                        TimeUnit.MILLISECONDS.sleep(500);
                    }
                    byte[] bytes = FileUtils.readFileToByteArray(tmpFile);
                    os.write(bytes);
                    os.flush();
                    // 删除分片， todo 后台异步删除
                    tmpFile.delete();
                }

                res = UploadStatus.SUCCESS_END;
                os.flush();
                // todo 暂时全部分片都用整个文件的md5，后面改为在某个时间段内记录上传的分片序号，用于做断点续传，只传没传的
                return new UploadStatusDTO(res, filePath + name, chunkDto.getMd5());
            }
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            res = UploadStatus.ERROR_UNKNOWN;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new UploadStatusDTO(res);
    }

}
