package duegin.ginDriver.controller;

import duegin.ginDriver.common.security.utils.SecurityUtils;
import duegin.ginDriver.domain.model.Group;
import duegin.ginDriver.domain.param.group.AddGroupParam;
import duegin.ginDriver.domain.param.group.UpdateGroupParam;
import duegin.ginDriver.domain.vo.Result;
import duegin.ginDriver.mapper.GroupMapper;
import duegin.ginDriver.service.impl.GroupService;
import io.swagger.annotations.*;
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
    private GroupService groupService;

    @Resource
    private GroupMapper groupMapper;

    @ApiOperation("创建组")
    @PostMapping("create")
    public Result<Void> createGroup(@RequestBody @ApiParam(value = "组信息", required = true) AddGroupParam groupParam) {
        log.info(String.valueOf(groupParam));
        Group group = new Group(groupParam);
        group.setUserId(SecurityUtils.getUserId());
        return groupService.createGroup(group);
    }

    @DeleteMapping("{groupId}")
    public Result<Void> deleteGroup(@PathVariable @ApiParam("组ID") Long groupId) {
        log.info(String.valueOf(groupId));
        groupService.deleteGroup(groupId, SecurityUtils.getUserId());
        return Result.ok();
    }


    @PutMapping("modify")
    public Result<Void> modifyGroup(@RequestBody @Valid UpdateGroupParam groupParam) {
        log.info(String.valueOf(groupParam));
        Group group = new Group(groupParam);

        return groupService.updateGroup(group);
    }

    @PostMapping("god/change")
    public Result<Void> changeCreator(Long groupId, Long userId) {
        log.info("groupId: {}, groupNewGodId: {}", groupId, userId);
        Long oldGodId = SecurityUtils.getUserId();
        return groupService.changeGroupGod(groupId, oldGodId, userId);
    }

    @PostMapping("user/remove")
    public Result<Void> removeGroupUser(Long groupId, Long removedUserId) {
        log.info("groupId: {}, removeUserId: {}", groupId, removedUserId);

        return groupService.removeGroupUser(groupId, removedUserId);
    }

    @GetMapping("user_group_list")
    public Result<List<Group>> userGroupList() {
        List<Group> groups = groupMapper.selectAllByUserId(SecurityUtils.getUserId());
        return Result.ok(groups);
    }

    // todo 通过分享链接加入组，或者通过用户名查找邀请加入
    @ApiOperation("加入组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "组ID", required = true),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true),
    })
    @PostMapping("user/enter")
    public Result<Void> enterGroup(@NotNull Long groupId, @NotNull Long userId) {
        log.info("groupId: {}, userId: {}", groupId, userId);
        return groupService.enterGroup(groupId, userId);
    }

    @ApiOperation("退出组")
    @PostMapping("user/exit")
    public Result<Void> exitGroup(@RequestParam @ApiParam("组ID") Long groupId) {
        log.info(String.valueOf(groupId));
        return groupService.exitGroup(groupId, SecurityUtils.getUserId());
    }


    @PostMapping("edit/role")
    public Result<Void> editRole() {
        return Result.ok();
    }

}
