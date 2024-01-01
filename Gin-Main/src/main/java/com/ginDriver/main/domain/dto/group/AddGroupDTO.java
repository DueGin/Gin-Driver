package com.ginDriver.main.domain.dto.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author DueGin
 */
@Data
@ApiModel("添加组")
public class AddGroupDTO {

    /**
     * 组名
     */
    @ApiModelProperty(value = "组名", required = true)
    private String groupName;

    /**
     * 简介
     */
    @ApiModelProperty(value = "简介", required = true)
    private String description;

    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;

}
