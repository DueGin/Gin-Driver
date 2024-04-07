package com.ginDriver.main.file.upload;


import com.ginDriver.main.file.FileManager;
import com.ginDriver.main.file.constants.UploadStatus;
import com.ginDriver.main.file.domain.dto.ChunkDTO;
import com.ginDriver.main.file.domain.dto.UploadStatusDTO;
import com.ginDriver.main.file.generator.LocalFilePathGenerator;
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
        UploadStatus res;

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
        String filePath = path + "/";


        BufferedOutputStream os = null;

        try {
            // 分片文件
            if (chunks != null && chunk != null && chunks != 0) {
                String tmpFileName = getChunkName(chunk, name);
                File tmpFile = new File(chunkPath, tmpFileName);

                if (!tmpFile.exists()) {
                    // 没有文件夹则创建
                    mkdir(tmpFile);
                    file.transferTo(tmpFile); // 保存分片
                    log.info(name + "分片" + chunkDto.getChunk() + "上传成功！");
                }

                chunkDto.setRealPath(chunkPath + tmpFileName);

                // 存入集合列表中
                FileManager.uploaded
//                        .computeIfAbsent(userId, k -> new ConcurrentHashMap<>())
                        .computeIfAbsent(chunkDto.getMd5(), k -> new ArrayList<>())
                        .add(chunkDto.getChunk());

                res = UploadStatus.SUCCESS_CHUNK;

            } else {
                // 非分片文件
                int lastIndex = name.lastIndexOf('.');
                String filePathName;
                if (lastIndex != 0) {
                    String ext = name.substring(lastIndex);
                    // 合并后文件名，原文件名_md5值
                    filePathName = filePath + chunkDto.getMd5() + ext;
                } else {
                    // 合并后文件名，原文件名_md5值
                    filePathName = filePath + chunkDto.getMd5();
                }
                File wholeFile = new File(filePathName); // 通过md5命名文件
                mkdir(wholeFile);

                // 保存文件
                file.transferTo(wholeFile);

                return new UploadStatusDTO(UploadStatus.SUCCESS_END, filePathName, chunkDto.getMd5());
            }

            // 获取已上传的分片数量
            List<Integer> chunkNumberList = FileManager.uploaded.get(chunkDto.getMd5());
            int size = 0;
            if (chunkNumberList != null) {
                size = chunkNumberList.size();
            }

            // 分片收集完成后，才开始合并
            if (chunks == size) {
                int lastIndex = name.lastIndexOf('.');
                String filePathName;
                if (lastIndex != 0) {
                    String ext = name.substring(lastIndex);
                    // 合并后文件名，原文件名_md5值
                    filePathName = filePath + chunkDto.getMd5() + ext;
                } else {
                    // 合并后文件名，原文件名_md5值
                    filePathName = filePath + chunkDto.getMd5();
                }

                File wholeFile = new File(filePathName);
                // 创建文件夹
                mkdir(wholeFile);
                FileOutputStream fileOutputStream = new FileOutputStream(wholeFile);
                os = new BufferedOutputStream(fileOutputStream);

                // 读取存储的分片
                for (int i = 0; i < chunks; i++) {
                    File tmpFile = new File(chunkPath, getChunkName(i + 1, name));
                    while (!tmpFile.exists()) {
                        log.error("分片文件不存在 ==> 文件路径: {}", chunkPath + getChunkName(i + 1, name));
                        TimeUnit.MILLISECONDS.sleep(500);
                    }
                    byte[] bytes = FileUtils.readFileToByteArray(tmpFile);
                    os.write(bytes);
                    os.flush();
                    // 删除分片
                    tmpFile.delete();
                }

                res = UploadStatus.SUCCESS_END;
                os.flush();
                // todo 暂时全部分片都用整个文件的md5，后面改为在某个时间段内记录上传的分片序号，用于做断点续传，只传没传的
                return new UploadStatusDTO(res, filePathName, chunkDto.getMd5());
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

    private static void mkdir(File wholeFile) {
        // 没有文件夹则创建
        if (!wholeFile.getParentFile().exists()) {
            wholeFile.getParentFile().mkdirs();
        }
    }

    private String getChunkName(Integer index, String name) {
        return name + "_" + index;
    }
}
