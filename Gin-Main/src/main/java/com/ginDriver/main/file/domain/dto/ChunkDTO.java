package com.ginDriver.main.file.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ChunkDTO {
    // id
    private String id;

    private String name;

    private String type;

    private String lastModifiedDate;

    // 总分片数
    private Integer chunks;

    // 当前分片
    private Integer chunk;

    private Long size;

    /**
     * 文件id
     */
    private Long chunkId;

    private String md5;

    /**
     * 分片内容
     */
    private MultipartFile upload;

    /**
     * 真实存储路径
     */
    private String realPath;

    /**
     * 是否已存在 1:存在，null:未赋值，0:不存在
     */
    private Short exist;

    private String uploadToken;
}
