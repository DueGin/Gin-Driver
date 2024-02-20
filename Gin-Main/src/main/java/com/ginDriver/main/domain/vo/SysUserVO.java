package com.ginDriver.main.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author DueGin
 */
@Data
@ApiModel("系统用户")
public class SysUserVO {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long id;

    /**
     * 登录用户名
     */
    @ApiModelProperty("登录用户名")
    private String userAccount;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 头像文件名称
     */
    @ApiModelProperty("头像URL")
    private String avatar;

    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 系统角色ID
     */
    @ApiModelProperty("系统角色ID")
    private Long sysRoleId;

    /**
     * 组角色列表
     */
    @ApiModelProperty("组角色列表")
    private List<String> groupRoleList;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
