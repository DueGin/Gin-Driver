package com.ginDriver.main.domain.dto.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author DueGin
 */
@Data
@ApiModel("组成员更新组内个人信息")
public class UpdateGroupUserDTO {

    @NotBlank
    @ApiModelProperty("组ID")
    private Long groupId;

    @NotBlank
    @ApiModelProperty("用户ID")
    private Long userId;

    @NotBlank
    @ApiModelProperty("组内个人用户名")
    private String groupUsername;
}
