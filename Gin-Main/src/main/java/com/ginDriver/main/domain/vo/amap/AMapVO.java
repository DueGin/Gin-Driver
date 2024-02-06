package com.ginDriver.main.domain.vo.amap;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 高德地图数据展示类
 *
 * @author DueGin
 */
@Data
public class AMapVO {

    /**
     * Minio url
     */
    @ApiModelProperty("minio url")
    private String url;

    /**
     * 文件名字(对象名字)
     */
    @ApiModelProperty("文件名字(对象名字)")
    private String fileName;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

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
     * 媒体格式
     */
    @ApiModelProperty("媒体格式")
    private String mimeType;

    /**
     * 媒体拍摄时间
     */
    @ApiModelProperty("媒体拍摄时间")
    private LocalDateTime originalDateTime;

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
