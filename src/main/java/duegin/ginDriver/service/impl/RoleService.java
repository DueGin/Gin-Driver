package duegin.ginDriver.service.impl;


import duegin.ginDriver.domain.vo.Result;
import duegin.ginDriver.mapper.RoleMapper;
import duegin.ginDriver.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author DueGin
 */
@Slf4j
@Service
public class RoleService implements IRoleService {
    @Resource
    private RoleMapper roleMapper;

    public Result<Void> modifyUserRole(Long userId, Long roleId){
//        String sysRole = SecurityUtils.getRole().get("sys");
//        if(sysRole != null){
//
//        }
        return Result.ok();
    }
}
