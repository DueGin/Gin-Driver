package com.ginDriver.main.file.domain.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("文件-分片联系实体")
public class FileChunk implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文件id")
    private Long fileId;

    @ApiModelProperty("分片id")
    private Long chunkId;

    @ApiModelProperty("当前是第几个分片")
    private Integer chunk;

    public FileChunk() {
    }

    public FileChunk(Long fileId, Long chunkId, Integer chunk) {
        this.fileId = fileId;
        this.chunkId = chunkId;
        this.chunk = chunk;
    }
}
