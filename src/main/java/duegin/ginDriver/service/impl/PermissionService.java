package duegin.ginDriver.service.impl;


import duegin.ginDriver.mapper.PermissionMapper;
import duegin.ginDriver.mapper.RoleMapper;
import duegin.ginDriver.service.IPermissionService;
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
public class PermissionService implements IPermissionService {

    @Resource
    PermissionMapper permissionMapper;

    @Resource
    RoleMapper roleMapper;

    @Override
    public Set<String> getUserAuthorities(Long userId) {

        List<String> permissions = permissionMapper.getUserPermissions(userId);
        List<String> userRole = roleMapper.getUserRole(userId);

        //合并两个 list
        userRole.stream().sequential().collect(Collectors.toCollection(() -> permissions));

        return new HashSet<>(permissions);
    }
}
