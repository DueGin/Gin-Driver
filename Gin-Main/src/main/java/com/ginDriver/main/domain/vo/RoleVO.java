package com.ginDriver.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author DueGin
 */
@Data
public class RoleVO {
    /**
     * 角色表id
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

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
}
