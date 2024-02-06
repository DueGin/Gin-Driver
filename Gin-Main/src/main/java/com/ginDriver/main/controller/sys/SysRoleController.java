package com.ginDriver.main.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.result.BusinessController;
import com.ginDriver.main.domain.po.Role;
import com.ginDriver.main.security.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author DueGin
 */
@BusinessController
@RequestMapping("sys/role")
@PreAuthorize("hasRole('ADMIN')")
public class SysRoleController {

    @Resource
    private RoleService roleService;

    @GetMapping("list")
    public ResultVO<List<Role>> list(Integer type){
        List<Role> roleList = roleService.list(new QueryWrapper<Role>().lambda()
                .eq(Role::getType, type)
        );

        return ResultVO.ok(roleList);
    }

    @PutMapping("modify")
    public ResultVO<Void> modifyUserRole(Long userId, Long roleId) {
        return roleService.modifySysRole(userId, roleId);
    }


    @PostMapping("save")
    public ResultVO<Void> addRole(Role role) {
        return roleService.save(role) ? ResultVO.ok("新增成功") : ResultVO.fail("新增失败");
    }

    @DeleteMapping("delete/{roleId}")
    public ResultVO<Void> deleteRole(@PathVariable String roleId){
        return roleService.removeById(roleId) ? ResultVO.ok("删除成功") : ResultVO.fail("删除失败");
    }
}
