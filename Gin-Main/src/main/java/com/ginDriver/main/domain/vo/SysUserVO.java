package com.ginDriver.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author DueGin
 */
@Data
public class SysUserVO {

    private Long userId;

    private String username;

    private String password;

    private String phone;

    private String email;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
