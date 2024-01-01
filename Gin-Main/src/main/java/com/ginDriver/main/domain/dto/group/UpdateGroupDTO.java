package com.ginDriver.main.domain.dto.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author DueGin
 */
@Data
@ApiModel("组更新信息")
public class UpdateGroupDTO {

    /**
     * 组ID
     */
    @ApiModelProperty(value = "组ID", required = true)
    private Long id;

    /**
     * 组名
     */
    @NotNull
    @ApiModelProperty(value = "组名",required = true)
    private String groupName;

    /**
     * 简介
     */
    @ApiModelProperty("简介")
    private String description;
}
