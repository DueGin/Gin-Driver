package com.ginDriver.main.domain.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author DueGin
 */
@Data
@ApiModel("用户登录/注册信息")
public class UserParam {
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 验证码uuid
     */
    @ApiModelProperty("验证码uuid")
    private String uuid;

    /**
     * 验证码
     */
    @ApiModelProperty("验证码")
    private String verifyCode;

}
