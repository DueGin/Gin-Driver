package duegin.ginDriver.controller.sys;

import duegin.ginDriver.domain.vo.Result;
import duegin.ginDriver.service.impl.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author DueGin
 */
@RestController
@RequestMapping("sys/role")
public class SysRoleController {

    @Resource
    private RoleService roleService;

    @PutMapping("modify")
    @PreAuthorize("hasRole('admin')")
    public Result<Void> modifyUserRole(Long userId, Long roleId){
        return roleService.modifyUserRole(userId, roleId);
    }

}
