package com.ginDriver.main.domain.dto.layoutComponent;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.main.domain.po.LayoutComponent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 布局组件分页DTO
 * @author DueGin
 */
@Data
@ApiModel("布局组件分页DTO")
public class LayoutComponentPageDTO extends Page<LayoutComponent> {

    /**
     * 组件名称
     */
    @ApiModelProperty(value = "组件名称")
    private String name;

    /**
     * 组件路径
     */
    @ApiModelProperty(value = "组件路径")
    private String path;

    /**
     * 是否存在插槽，0:不存在，1:存在
     */
    @ApiModelProperty(value = "是否存在插槽，0:不存在，1:存在")
    private Integer hasSlot;

    /**
     * 是否存在路由插槽，0:不存在，1:存在
     */
    @ApiModelProperty(value = "是否存在路由插槽，0:不存在，1:存在")
    private Integer hasRouter;
}
