package com.ginDriver.main.domain.po;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 文件垃圾箱 实体类。
 *
 * @author DueGin
 * @since 1.0
 */
@Data
@ApiModel(value = "文件垃圾箱", description = "文件垃圾箱")
@TableName(value = "dustbin")
public class Dustbin {

    @NotNull
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
     * 文件类型(1：图片，2：视频，3：电影，4：其他)
     */
    @ApiModelProperty(value = "文件类型(1：图片，2：视频，3：电影，4：其他)")
    private Integer type;

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
