package com.ginDriver.core.domain.po;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author DueGin
 */
@Data
@TableName("user")
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @NotNull
    private Long id;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String userAccount;

    private String username;

    private String password;

    /**
     * 头像文件名称
     */
    private String avatar;

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

    /**
     * 软删除
     */
    @TableLogic
    private Integer deleted;

    // 1:启用 ， 0：禁用
    @TableField(exist = false)
    private Integer enabled = 1;

    // 1：锁住， 0：未锁
    @TableField(exist = false)
    private Integer locked = 0;

    /**
     * table not exists
     */
    @TableField(exist = false)
    private List<String> perms;

    /**
     * table not exists
     */
    @TableField(exist = false)
    private String uuid;

    /**
     * table not exists
     */
    @TableField(exist = false)
    private String verifyCode;

    /**
     * table not exists
     */
    @TableField(exist = false)
    private Boolean rememberMe;

    /**
     * table not exists
     */
    @TableField(exist = false)
    private String token;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String authority : perms) {
            SimpleGrantedAuthority auth = new SimpleGrantedAuthority(authority);
            authorities.add(auth);
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return locked == 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled == 1;
    }
}
