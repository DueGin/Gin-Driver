package com.ginDriver.main.domain.po;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 菜单权限表 实体类。
 *
 * @author DueGin
 * @since 1.0
 */
@Data
@Table(value = "menu")
public class Menu {

    @NotNull
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件名称
     */
    private String componentName;

    /**
     * 组件路径
     */
    private String componentPath;

    /**
     * 布局组件ID
     */
    private Long layoutComponentId;

    /**
     * 权限标识
     */
    private String role;

    /**
     * 菜单状态（1正常 0停用）
     */
    private Integer status;

    /**
     * 是否隐藏(1:隐藏，0:不隐藏)
     */
    private Integer hidden;

    /**
     * 菜单类型(字典ID)
     */
    private Long type;

    /**
     * 排序
     */
    private Integer sorted;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
