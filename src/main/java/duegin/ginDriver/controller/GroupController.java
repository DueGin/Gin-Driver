package duegin.ginDriver.controller;

import duegin.ginDriver.common.security.utils.SecurityUtils;
import duegin.ginDriver.domain.model.Group;
import duegin.ginDriver.domain.param.group.AddGroupParam;
import duegin.ginDriver.domain.param.group.UpdateGroupParam;
import duegin.ginDriver.domain.vo.Result;
import duegin.ginDriver.domain.vo.UserVO;
import duegin.ginDriver.mapper.GroupMapper;
import duegin.ginDriver.service.manager.GroupUserManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    @Resource
    private GroupMapper groupMapper;

    @ApiOperation("创建组")
//    @ApiImplicitParam(name = "group", value = "组信息", required = true)
    @PostMapping("create")
    public Result<Void> createGroup(@RequestBody AddGroupParam groupParam) {
        log.info(String.valueOf(groupParam));
        Group group = new Group(groupParam);
        group.setUserId(SecurityUtils.getUserId());
        groupUserManager.createGroup(group);
        return Result.ok();
    }

    @DeleteMapping("{groupId}")
    public Result<Void> deleteGroup(@PathVariable Long groupId) {
        log.info(String.valueOf(groupId));
        UserVO loginUser = SecurityUtils.getLoginUser();
        groupUserManager.deleteGroup(groupId, loginUser.getUserId());
        return Result.ok();
    }

    @PutMapping("modify")
    public Result<Void> modifyGroup(@RequestBody @Valid UpdateGroupParam groupParam){
        log.info(String.valueOf(groupParam));
        Group group = new Group(groupParam);
        groupMapper.modifyByGroupId(group);
        return Result.ok();
    }

    @GetMapping("list/{userId}")
    public Result<List<Group>> userGroupList(@PathVariable Long userId){
        log.info(String.valueOf(userId));
        List<Group> groups = groupMapper.selectAllByUserId(userId);
        return Result.ok(groups);
    }

    @ApiOperation("加入组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组ID", required = true),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true)
    })
    @PostMapping("enter")
    public Result<Void> enterGroup(@NotNull Long groupId, @NotNull Long userId, @NotNull Long roleId) {
        groupUserManager.enterGroup(groupId, userId, roleId);
        return Result.ok();
    }

    @ApiOperation("退出组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组ID", required = true),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true)
    })
    @PostMapping("exit")
    public Result<Void> exitGroup(@NotNull Long groupId, @NotNull Long userId) {
        groupUserManager.exitGroup(groupId, userId);
        return Result.ok();
    }


//    @PostMapping("edit/role")
//    public Result<Void> editRole(){
//
//    }

}
