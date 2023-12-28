package duegin.ginDriver.controller.sys;

import com.ginDriver.core.domain.vo.ResultVO;
import duegin.ginDriver.domain.po.Role;
import duegin.ginDriver.security.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author DueGin
 */
@RestController
@RequestMapping("sys/role")
@PreAuthorize("hasRole('admin')")
public class SysRoleController {

    @Resource
    private RoleService roleService;

    @PutMapping("modify")
    @PreAuthorize("hasRole('admin')")
    public ResultVO<Void> modifyUserRole(Long userId, Long roleId) {
        return roleService.modifySysRole(userId, roleId);
    }


    @PostMapping("save")
    @PreAuthorize("hasRole('admin')")
    public ResultVO<Void> addRole(Role role) {
        return roleService.save(role) ? ResultVO.ok("新增成功") : ResultVO.fail("新增失败");
    }

    @DeleteMapping("delete/{roleId}")
    @PreAuthorize("hasRole('admin')")
    public ResultVO<Void> deleteRole(@PathVariable String roleId){
        return roleService.removeById(roleId) ? ResultVO.ok("删除成功") : ResultVO.fail("删除失败");
    }
}
