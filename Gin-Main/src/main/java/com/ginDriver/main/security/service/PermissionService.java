package com.ginDriver.main.security.service;


import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.domain.po.Permission;
import com.ginDriver.main.mapper.PermissionMapper;
import com.ginDriver.main.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author DueGin
 */
@Service
public class PermissionService extends MyServiceImpl<PermissionMapper, Permission> {

    @Resource
    PermissionMapper permissionMapper;

    @Resource
    RoleMapper roleMapper;

    /**
     * 获取用户角色和权限集合
     *
     * @param userId 用户ID
     * @return 角色和权限集合
     */
    public Set<String> getUserAuthorities(Long userId) {

        List<String> permissions = permissionMapper.getUserPermissions(userId);
        List<String> userRole = roleMapper.getUserRole(userId);

        //合并两个 list
        userRole.stream().collect(Collectors.toCollection(() -> permissions));

        return new HashSet<>(permissions);
    }
}
