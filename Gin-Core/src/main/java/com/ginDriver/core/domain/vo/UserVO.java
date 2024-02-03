package com.ginDriver.core.domain.vo;

import lombok.Data;

import java.util.List;


/**
 * @author DueGin
 */
@Data
public class UserVO {
    private Long id;
    private String avatar;
    private String avatarUrl;
    private String username;
    private String phone;
    private String email;
    private String sysRole;
    private List<String> groupRoleList;
}
