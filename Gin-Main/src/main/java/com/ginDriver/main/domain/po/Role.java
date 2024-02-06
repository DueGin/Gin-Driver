package com.ginDriver.main.domain.po;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @author DueGin
 * @TableNameName role
 */
@Data
@Accessors(chain = true)
public class Role implements Serializable {
    /**
     * 角色表id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 类型（1: 系统角色，2: 组角色）
     */
    private Integer type;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 状态
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}