package com.ginDriver.main.domain.vo;


import com.ginDriver.main.domain.po.LayoutComponent;
import lombok.Data;

import java.util.List;

/**
 * @author DueGin
 */
@Data
public class MenuVO {

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
     * 子菜单
     */
    private List<MenuVO> children;

    /**
     * 组件名称
     */
    private String componentName;

    /**
     * 组件路径
     */
    private String componentPath;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 布局组件ID
     */
    private Long layoutComponentId;

    /**
     * 布局组件
     */
    private LayoutComponent layoutComponent;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 角色权限
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 菜单类型(0:启动台菜单，1:组资源菜单，2:媒体管理菜单，3:其他)
     */
    private Long type;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 菜单状态（1正常 0停用）
     */
    private Integer status;

    /**
     * 是否隐藏(1:隐藏，0:不隐藏)
     */
    private Integer hidden;

    /**
     * 排序
     */
    private Integer sorted;

    /**
     * 备注
     */
    private String remark;
}
