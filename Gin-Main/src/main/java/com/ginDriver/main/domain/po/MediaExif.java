package com.ginDriver.main.domain.po;

import com.mybatisflex.annotation.Column;
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

    @ApiModelProperty(value = "")
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 文件id
     */
    @ApiModelProperty(value = "文件id")
    @Column(value = "media_id")
    private Long mediaId;

    @ApiModelProperty(value = "")
    @Column(value = "width")
    private Integer width;

    @ApiModelProperty(value = "")
    @Column(value = "height")
    private Integer height;

    @ApiModelProperty(value = "")
    @Column(value = "mime_type")
    private String mimeType;

    @ApiModelProperty(value = "")
    @Column(value = "create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "")
    @Column(value = "model")
    private String model;


}
