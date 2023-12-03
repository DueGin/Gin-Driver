package duegin.ginDriver.controller;

import duegin.ginDriver.common.security.utils.SecurityUtils;
import duegin.ginDriver.domain.model.GroupUser;
import duegin.ginDriver.domain.param.group.UpdateGroupUserParam;
import duegin.ginDriver.domain.vo.Result;
import duegin.ginDriver.service.impl.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author DueGin
 */
@Api(tags = "组内用户")
@Slf4j
@Validated
@RestController
@RequestMapping("group/user")
public class GroupUserController {
    @Resource
    private GroupService groupService;

    // todo 通过分享链接加入组，或者通过用户名查找邀请加入
    @ApiOperation("加入组")
    @PostMapping("enter")
    public Result<Void> enterGroup(@NotNull @ApiParam("组ID") Long groupId, @NotNull @ApiParam("用户ID") Long userId) {
        log.info("groupId: {}, userId: {}", groupId, userId);
        return groupService.enterGroup(groupId, userId);
    }

    @ApiOperation("退出组")
    @PostMapping("exit")
    public Result<Void> exitGroup(@RequestParam @ApiParam("组ID") Long groupId) {
        log.info(String.valueOf(groupId));
        return groupService.exitGroup(groupId, SecurityUtils.getUserId());
    }

    @ApiOperation("移除组用户")
    @PostMapping("remove")
    public Result<Void> removeGroupUser(@NotNull @ApiParam("组ID") Long groupId, @NotNull @ApiParam("需要移除的用户ID") Long removedUserId) {
        log.info("groupId: {}, removeUserId: {}", groupId, removedUserId);
        return groupService.removeGroupUser(groupId, removedUserId);
    }

    @ApiOperation("更新组内用户个人信息")
    @PutMapping("update")
    public Result<Void> updateGroupUser(@Valid UpdateGroupUserParam updateGroupUserParam) {
        log.info(String.valueOf(updateGroupUserParam));
        GroupUser groupUser = new GroupUser(updateGroupUserParam);
        return groupService.updateGroupByGroupUser(groupUser);
    }
}
