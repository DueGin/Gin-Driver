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
 * 媒体资源垃圾箱 实体类。
 *
 * @author DueGin
 * @since 1.0
 */
@Data
@ApiModel(value = "媒体资源垃圾箱", description = "媒体资源垃圾箱")
@Table(value = "dustbin")
public class Dustbin {

    @ApiModelProperty(value = "")
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 媒体ID
     */
    @ApiModelProperty(value = "媒体ID")
    @Column(value = "media_id")
    private Long mediaId;

    /**
     * 删除者ID
     */
    @ApiModelProperty(value = "删除者ID")
    @Column(value = "user_id")
    private Long userId;

    /**
     * 文件名
     */
    @ApiModelProperty(value = "文件名")
    @Column(value = "file_name")
    private String fileName;

    /**
     * 文件类型(1：图片，2：视频，3：电影，4：其他)
     */
    @ApiModelProperty(value = "文件类型(1：图片，2：视频，3：电影，4：其他)")
    @Column(value = "type")
    private Integer type;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @Column(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @Column(value = "update_time")
    private LocalDateTime updateTime;


}
