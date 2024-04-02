package com.ginDriver.main.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * md5文件表
 *
 * @author DueGin
 * @TableName md5_file
 */
@TableName(value = "md5_file")
@Data
public class Md5File implements Serializable {

    @TableId
    private Long id;

    /**
     * 文件md5值
     */
    private String md5;

    /**
     * 存放路径
     */
    private String src;

    /**
     * 对象存储名称
     */
    private String objectName;

    /**
     * 引用次数
     */
    private Integer ref;

    /**
     * 文件内容类型
     */
    private String contentType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}