package com.ginDriver.main.domain.dto.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author DueGin
 */
@Data
@ApiModel("菜单DTO")
public class MenuDTO {

    @NotNull
    @ApiModelProperty("菜单ID")
    private Long id;

    /**
     * 菜单名称
     */
    @ApiModelProperty("菜单名称")
    private String name;

    /**
     * 父菜单ID
     */
    @ApiModelProperty("父菜单ID")
    private Long parentId;

    /**
     * 菜单图标
     */
    @ApiModelProperty("菜单图标")
    private String icon;

    /**
     * 路由地址
     */
    @NotNull
    @ApiModelProperty("路由地址")
    private String path;

    /**
     * 组件名称
     */
    @NotNull
    @ApiModelProperty("组件名称")
    private String componentName;

    /**
     * 组件路径
     */
    @ApiModelProperty("组件路径")
    private String componentPath;

    /**
     * 布局组件ID
     */
    @ApiModelProperty("布局组件ID")
    private Long layoutComponentId;

    /**
     * 权限标识
     */
    @ApiModelProperty("权限标识")
    private String role;

    /**
     * 菜单状态（1正常 0停用）
     */
    @ApiModelProperty("菜单状态（1正常 0停用）")
    private Integer status;

    /**
     * 是否隐藏(1:隐藏，0:不隐藏)
     */
    @ApiModelProperty("是否隐藏(1:隐藏，0:不隐藏)")
    private Integer hidden;

    /**
     * 菜单类型(字典ID)
     */
    @NotNull
    @ApiModelProperty("菜单类型(字典ID)")
    private Long type;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sorted;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
}
