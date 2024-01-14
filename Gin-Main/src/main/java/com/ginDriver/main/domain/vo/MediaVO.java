package com.ginDriver.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author DueGin
 */
@Data
public class MediaVO {
    /**
     * 媒体ID
     */
    private Long id;

    /**
     * 上传用户
     */
    private Long userId;

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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
