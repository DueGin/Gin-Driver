package com.ginDriver.main.file.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("文件实体")
public class File implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文件id")
    @TableId
    private Long fileId;

    @ApiModelProperty("文件名字")
    private String name;

    @ApiModelProperty("文件MD5值")
    private String md5;

    @ApiModelProperty("文件所属用户id")
    private Long userId;

    @ApiModelProperty("文件版本")
    private String version;

    public File() {
    }

    public File(Long fileId, Long userId) {
        this.fileId = fileId;
        this.userId = userId;
    }

    public File(String name, String md5, Long userId) {
        this.name = name;
        this.md5 = md5;
        this.userId = userId;
    }
}
