package com.ginDriver.main.domain.vo;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 媒体资源垃圾箱 实体类。
 *
 * @author DueGin
 * @since 1.0
 */
@Data
@ApiModel(value = "媒体资源垃圾箱", description = "媒体资源垃圾箱")
@TableName(value = "dustbin")
public class DustbinVO {

    @ApiModelProperty(value = "")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文件ID
     */
    @ApiModelProperty(value = "文件ID")
    private Long fileId;

    /**
     * 删除者ID
     */
    @ApiModelProperty(value = "删除者ID")
    private Long userId;

    /**
     * 文件名
     */
    @ApiModelProperty(value = "文件名")
    private String fileName;

    /**
     * 文件类型(枚举ID)
     */
    @ApiModelProperty(value = "文件类型(枚举ID)")
    private Integer type;

    /**
     * 媒体minio地址
     */
    @ApiModelProperty("媒体minio地址")
    private String url;

    /**
     * 媒体格式
     */
    @ApiModelProperty("媒体格式")
    private String mimeType;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
