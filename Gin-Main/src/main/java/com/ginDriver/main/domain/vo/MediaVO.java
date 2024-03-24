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
     * 文件ID
     */
    @ApiModelProperty("文件ID")
    private Long fileId;

    /**
     * 上传用户
     */
    @ApiModelProperty("上传用户")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 文件名
     */
    @ApiModelProperty("文件名")
    private String fileName;

    /**
     * 文件类型(枚举字典ID，0：其他)
     */
    @ApiModelProperty("文件类型")
    private Integer type;

    /**
     * 文件路径
     */
    @ApiModelProperty("文件路径")
    private String src;

    /**
     * 媒体minio地址
     */
    @ApiModelProperty("媒体minio地址")
    private String url;

    /**
     * 文件状态
     */
    @ApiModelProperty("文件状态")
    private Integer status;

    /**
     * 是否为私有媒体
     */
    @ApiModelProperty("是否为私有媒体")
    private Integer self;

    /**
     * 媒体格式
     */
    @ApiModelProperty("媒体格式")
    private String mimeType;

    /**
     * 媒体拍摄时间
     */
    @ApiModelProperty("媒体拍摄时间")
    private LocalDate originalDateTime;

    /**
     * 媒体宽度(像素)
     */
    @ApiModelProperty("媒体宽度(像素)")
    private Integer width;

    /**
     * 媒体高度(像素)
     */
    @ApiModelProperty("媒体高度(像素)")
    private Integer height;

    /**
     * 拍摄设备
     */
    @ApiModelProperty("拍摄设备")
    private String model;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private String longitude;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private String latitude;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}