package com.ginDriver.main.domain.po;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 媒体资源信息 实体类。
 *
 * @author DueGin
 * @since 1.0
 */
@Data
@ApiModel(value = "媒体资源信息", description = "媒体资源信息")
@Table(value = "media_exif")
public class MediaExif {

    @Id(keyType = KeyType.Auto)
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
     * 行政区域编码
     */
    @ApiModelProperty("行政区域编码")
    private Integer adcode;

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
