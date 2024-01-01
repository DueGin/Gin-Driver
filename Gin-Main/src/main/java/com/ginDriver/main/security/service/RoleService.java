package com.ginDriver.main.security.service;


import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.domain.po.Role;
import com.ginDriver.main.mapper.RoleMapper;
import com.ginDriver.main.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

        RoleMapper mapper = (RoleMapper) this.getMapper();

        Boolean modified = mapper.modifyUserRole(userId, roleId);

        return modified ? ResultVO.ok("修改成功") : ResultVO.fail("修改失败");
    }

    public Boolean checkSysRole(String needSysRole) {
        String sysRole = SecurityUtils.getRole().get("sys");
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
}
