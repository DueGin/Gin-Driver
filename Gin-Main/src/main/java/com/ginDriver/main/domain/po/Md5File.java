package com.ginDriver.main.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author DueGin
 */
@ApiModel(description = "MD5文件实体类")
@TableName(value = "md5_file")
@Data
public class Md5File implements Serializable {

    @TableId
    @ApiModelProperty(value = "唯一标识符", required = true, example = "1")
    private Long id;

    /**
     * 文件md5值
     */
    @ApiModelProperty(value = "文件md5值", required = true, example = "d41d8cd98f00b204e9800998ecf8427e")
    private String md5;

    /**
     * 存放路径
     */
    @ApiModelProperty(value = "存放路径", required = true, example = "/path/to/file.txt")
    private String src;

    /**
     * 对象存储名称
     */
    @ApiModelProperty(value = "对象存储名称", required = true, example = "file.txt")
    private String objectName;

    /**
     * 引用次数
     */
    @ApiModelProperty(value = "引用次数", required = true, example = "10")
    private Integer ref;

    /**
     * 文件内容类型
     */
    @ApiModelProperty(value = "文件内容类型", required = true, example = "text/plain")
    private String contentType;

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private static final long serialVersionUID = 1L;
}
