package com.ginDriver.main.domain.po;


import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 媒体资源
 * @author DueGin
 * @TableName media
 */
@Data
@Table("media")
public class Media implements Serializable {
    /**
     * 媒体ID
     */
    @Id
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

//    /**
//     * 文件类型1：图片，2：视频，3：电影，4：其他
//     */
//    @NotNull(groups = Insert.class)
//    private Integer type;

    /**
     * 文件路径
     */
    @NotNull(groups = Insert.class)
    private String src;


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
     * 媒体格式
     */
    @NotNull(groups = Insert.class)
    private String format;

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
    @Column(isLogicDelete = true)
    private Integer deleted;

    private static final long serialVersionUID = 1L;
}