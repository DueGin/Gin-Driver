package com.ginDriver.main.domain.vo;


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
    private List<MenuVO> childrenList;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String componentPath;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 菜单类型(0:启动台菜单，1:组资源菜单，2:媒体管理菜单，3:其他)
     */
    private Integer type;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 排序
     */
    private Integer sorted;

    /**
     * 备注
     */
    private String remark;
}
