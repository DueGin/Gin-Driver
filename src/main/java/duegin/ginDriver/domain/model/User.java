package duegin.ginDriver.domain.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author DueGin
 */
@Data
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private String username;

    private String password;

    private String phone;

    private String email;

    // 1:启用 ， 0：禁用
    private Integer enabled = 1;

    // 1：锁住， 0：未锁
    private Integer locked = 0;

    private List<String> perms;

    /**
     * table not exists
     */
    private String uuid;

    /**
     * table not exists
     */
    private String verifyCode;

    /**
     * table not exists
     */
    private Boolean isRememberMe = false;

    /**
     * table not exists
     */
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
