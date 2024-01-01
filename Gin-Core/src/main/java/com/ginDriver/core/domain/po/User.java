package com.ginDriver.core.domain.po;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author DueGin
 */
@Data
@Table("user")
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

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

    /**
     * 软删除
     */
    private Integer deleted;

    // 1:启用 ， 0：禁用
    @Column(ignore = true)
    private Integer enabled = 1;

    // 1：锁住， 0：未锁
    @Column(ignore = true)
    private Integer locked = 0;

    /**
     * table not exists
     */
    @Column(ignore = true)
    private List<String> perms;

    /**
     * table not exists
     */
    @Column(ignore = true)
    private String uuid;

    /**
     * table not exists
     */
    @Column(ignore = true)
    private String verifyCode;

    /**
     * table not exists
     */
    @Column(ignore = true)
    private Boolean rememberMe;

    /**
     * table not exists
     */
    @Column(ignore = true)
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
