package duegin.ginDriver.common.security.service;


import duegin.ginDriver.domain.model.User;
import duegin.ginDriver.mapper.RoleMapper;
import duegin.ginDriver.mapper.UserMapper;
import duegin.ginDriver.service.IPermissionService;
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
    private IPermissionService permissionService;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 用户登录校验
     *
     * @param username 用户名
     * @return 从数据库load的user
     * @throws UsernameNotFoundException 查询不到此用户异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username: {}", username);
        User user = userMapper.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("没有此用户：" + username);
        }
        user.setPerms(getAuthorities(user.getUserId()));
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
