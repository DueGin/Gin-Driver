package com.ginDriver.main.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author DueGin
 */
@Data
@ApiModel("媒体VO")
public class MediaVO {
    /**
     * 媒体ID
     */
    @ApiModelProperty("媒体ID")
    private Long id;

    /**
     * 上传用户
     */
    private Long userId;

    /**
     * 组ID
     */
    private Long groupId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件类型1：图片，2：视频，3：电影，4：其他
     */
    private Integer type;

    /**
     * 文件路径
     */
    private String src;

    /**
     * 媒体minio地址
     */
    private String url;

    /**
     * 文件状态
     */
    private Integer status;

    /**
     * 是否为私有媒体
     */
    private Integer self;

    /**
     * 媒体格式
     */
    private String mimeType;

    /**
     * 媒体拍摄时间
     */
    private LocalDate mediaDate;

    /**
     * 媒体宽度(像素)
     */
    private Integer width;

    /**
     * 媒体高度(像素)
     */
    private Integer height;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
