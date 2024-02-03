package com.ginDriver.main.domain.po;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 组件布局 实体类。
 *
 * @author DueGin
 * @since 1.0
 */
@Data
@ApiModel(value = "组件布局", description = "组件布局")
@Table(value = "layout_component")
public class LayoutComponent {

    @Id(keyType = KeyType.Auto)
    private Long id;

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

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
