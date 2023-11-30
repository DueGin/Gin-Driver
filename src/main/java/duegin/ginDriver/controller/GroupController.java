package duegin.ginDriver.controller;

import duegin.ginDriver.domain.vo.Result;
import duegin.ginDriver.service.manager.GroupUserManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author DueGin
 */
@Api("组管理")
@Slf4j
@RestController
@RequestMapping("group")
public class GroupController {
    @Resource
    private GroupUserManager groupUserManager;

    @ApiOperation("加入组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组ID", required = true),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true)
    })
    @PostMapping("enter")
    public Result<Void> enterGroup(Long groupId, Long userId, Long roleId) {
        groupUserManager.enterGroup(groupId, userId, roleId);
        return Result.ok();
    }

    @ApiOperation("退出组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组ID", required = true),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true)
    })
    @PostMapping("exit")
    public Result<Void> exitGroup(Long groupId, Long userId) {
        groupUserManager.exitGroup(groupId, userId);
        return Result.ok();
    }


    // todo 用户角色修改，组管理员才能修改

}
