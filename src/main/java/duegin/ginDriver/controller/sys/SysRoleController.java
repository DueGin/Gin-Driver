package duegin.ginDriver.controller.sys;

import duegin.ginDriver.domain.po.Role;
import duegin.ginDriver.domain.vo.Result;
import duegin.ginDriver.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public Result<Void> modifyUserRole(Long userId, Long roleId) {
        return roleService.modifySysRole(userId, roleId);
    }


    @PostMapping("save")
    public Result<Void> addRole(Role role) {
        return roleService.save(role) ? Result.ok("新增成功") : Result.fail("新增失败");
    }

    @DeleteMapping("delete/{roleId}")
    public Result<Void> deleteRole(@PathVariable String roleId){
        return roleService.removeById(roleId) ? Result.ok("删除成功") : Result.fail("删除失败");
    }
}
