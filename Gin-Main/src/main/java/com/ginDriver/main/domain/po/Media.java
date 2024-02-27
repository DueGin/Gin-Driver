package com.ginDriver.main.domain.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 媒体资源
 * @author DueGin
 * @TableNameName media
 */
@Data
@TableName("media")
public class Media implements Serializable {
    /**
     * 媒体ID
     */
    @TableId(type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 上传用户
     */
    @NotNull(groups = Insert.class)
    private Long userId;

    /**
     * 文件名
     */
    @NotNull(groups = Insert.class)
    private String fileName;

    /**
     * 文件存储路径
     */
    @NotNull(groups = Insert.class)
    private String src;

    /**
     * 组ID
     */
    private Long groupId;


    /**
     * 文件状态
     */
    @NotNull(groups = Insert.class)
    private Integer status;

    /**
     * 是否为私有媒体
     */
    @NotNull(groups = Insert.class)
    private Integer self;

    /**
     * 文件类型
     */
    private String contentType;

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

    private static final long serialVersionUID = 1L;
}