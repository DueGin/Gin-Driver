package com.ginDriver.main.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件表
 *
 * @author DueGin
 * @TableName file
 */
@TableName(value = "file")
@Data
public class File implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * md5文件ID
     */
    private Long md5FileId;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 文件状态
     */
    private Integer status;

    /**
     * 是否为私有(0:不私有，1:私有)
     */
    private Integer self;

    /**
     * 文件类型(枚举ID，0：其他)
     */
    private Integer type;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 软删除
     */
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}