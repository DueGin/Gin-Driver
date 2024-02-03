package com.ginDriver.main.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 媒体资源信息VO。
 *
 * @author DueGin
 * @since 1.0
 */
@Data
@ApiModel(value = "媒体资源信息展示类", description = "媒体资源信息展示类")
public class MediaExifVO {

    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 媒体ID
     */
    @ApiModelProperty("媒体ID")
    private Long mediaId;

    /**
     * 媒体拍摄时间
     */
    @ApiModelProperty("媒体拍摄时间")
    private LocalDateTime originalDateTime;

    /**
     * 媒体宽度
     */
    @ApiModelProperty("媒体宽度")
    private Integer width;

    /**
     * 媒体高度
     */
    @ApiModelProperty("媒体高度")
    private Integer height;

    /**
     * 媒体类型
     */
    @ApiModelProperty("媒体类型")
    private String mimeType;

    /**
     * 拍摄设备
     */
    @ApiModelProperty("拍摄设备")
    private String model;

    /**
     * 省编码
     */
    @ApiModelProperty("省编码")
    private Integer provinceAdcode;

    /**
     * 省份名称
     */
    @ApiModelProperty("省份名称")
    private String provinceName;

    /**
     * 城市编码
     */
    @ApiModelProperty("城市编码")
    private Integer cityAdcode;

    /**
     * 城市名称
     */
    @ApiModelProperty("城市名称")
    private String cityName;

    /**
     * 区域编码
     */
    @ApiModelProperty("区域编码")
    private Integer districtAdcode;

    /**
     * 区域名称
     */
    @ApiModelProperty("区域名称")
    private String districtName;

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
}