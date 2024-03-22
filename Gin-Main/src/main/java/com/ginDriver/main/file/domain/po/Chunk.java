package com.ginDriver.main.file.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("文件分片")
public class Chunk implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分片id")
    @TableId
    private Long chunkId;

    @ApiModelProperty("分片MD5值")
    private String md5;

    public Chunk() {
    }

    public Chunk(String md5) {
        this.md5 = md5;
    }
}
