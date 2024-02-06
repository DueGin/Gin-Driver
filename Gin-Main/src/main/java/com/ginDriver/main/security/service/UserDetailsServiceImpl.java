package com.ginDriver.main.security.service;


import com.ginDriver.core.domain.po.User;
import com.ginDriver.main.mapper.RoleMapper;
import com.ginDriver.main.mapper.UserMapper;
import com.ginDriver.main.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * @author DueGin
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private PermissionService permissionService;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    /**
     * 用户登录校验
     *
     * @param userAccount 用户名
     * @return 从数据库load的user
     * @throws UsernameNotFoundException 查询不到此用户异常
     */
    @Override
    public UserDetails loadUserByUsername(String userAccount) throws UsernameNotFoundException {
        log.info("userAccount: {}", userAccount);
        User user = userService.getUserByAccount(userAccount);
        if (user == null) {
            throw new UsernameNotFoundException("没有此用户：" + userAccount);
        }
        user.setPerms(getAuthorities(user.getId()));
        return user;
    }

    /**
     * 获取权限
     */
    private List<String> getAuthorities(Long userId) {
        Set<String> permissions = permissionService.getUserAuthorities(userId);
        List<String> list = new ArrayList<>();
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(permissions.toArray(new String[0]));
        for (GrantedAuthority grantedAuthority : authorities) {
            list.add(grantedAuthority.getAuthority());
        }
        return list;
    }

}
