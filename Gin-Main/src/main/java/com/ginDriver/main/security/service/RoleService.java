package com.ginDriver.main.security.service;


import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.domain.po.Role;
import com.ginDriver.main.mapper.RoleMapper;
import com.ginDriver.main.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author DueGin
 */
@Slf4j
@Service
public class RoleService extends MyServiceImpl<RoleMapper, Role> {

    private static final String ADMIN = "ROLE_ADMIN";
    private static final String USER = "ROLE_USER";
    private static final String VISITOR = "ROLE_VISITOR";
    private static final String DISABLED = "ROLE_DISABLED";

    @Resource
    private RoleMapper roleMapper;


    public Map<Long, Role> getRoleMap() {
        return getRoleMap(null);
    }

    public Map<Long, Role> getRoleMap(Collection<Long> roleIds) {
        return super.lambdaQuery()
                .in(!CollectionUtils.isEmpty(roleIds), Role::getId, roleIds)
                .list()
                .stream()
                .collect(Collectors.toMap(Role::getId, s -> s));
    }

    /**
     * 只有管理员才能修改角色，且不能修改自己的
     *
     * @param userId 被修改用户ID
     * @param roleId 角色ID
     */
    public ResultVO<Void> modifySysRole(Long userId, Long roleId) {
        Boolean checked = checkSysRole(ADMIN);
        if (!checked) {
            return ResultVO.fail("权限不足");
        }

        if (SecurityUtils.getUserId().equals(userId)) {
            return ResultVO.fail("无法修改自己的角色");
        }

        RoleMapper mapper = this.getBaseMapper();

        Boolean modified = mapper.modifyUserRole(userId, roleId);

        return modified ? ResultVO.ok("修改成功") : ResultVO.fail("修改失败");
    }

    public Boolean checkSysRole(String needSysRole) {
        String sysRole = SecurityUtils.getSysRole();
        if (sysRole != null) {
            // 判断role
            switch (sysRole) {
                case ADMIN:
                    return true;
                case USER:
                    return !needSysRole.equals(ADMIN);
                case VISITOR:
                    return !needSysRole.equals(ADMIN) && !needSysRole.equals(USER);
                case DISABLED:
                    return false;
                default:
                    log.warn("未设置权限");
                    return false;
            }
        }
        log.warn("未设置权限");
        return false;
    }

    /**
     * 获取当前角色及其低级权限角色
     *
     * @param sysRole 当前角色
     * @return [当前角色, ...比当前角色权限低级的]
     */
    public List<Role> getYouAndUnderRole(String sysRole) {
        List<Role> roleList = lambdaQuery().eq(Role::getType, 1).list();
        switch (sysRole) {
            case ADMIN:
                return roleList;
            case USER:
                return roleList.stream()
                        .filter(r -> !ADMIN.equals(r.getRoleName()))
                        .collect(Collectors.toList());
            case VISITOR:
                return roleList.stream()
                        .filter(r -> !ADMIN.equals(r.getRoleName()) && !USER.equals(r.getRoleName()))
                        .collect(Collectors.toList());
            case DISABLED:
                return roleList.stream()
                        .filter(r -> DISABLED.equals(r.getRoleName()))
                        .collect(Collectors.toList());
            default:
                log.warn("未设置权限");
                return Collections.emptyList();
        }
    }

}
