package com.ginDriver.main.controller;

import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.main.domain.dto.group.UpdateGroupUserDTO;
import com.ginDriver.main.domain.po.GroupUser;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.service.GroupService;
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
    public ResultVO<Void> enterGroup(@NotNull @ApiParam("组ID") Long groupId, @NotNull @ApiParam("用户ID") Long userId) {
        log.info("groupId: {}, userId: {}", groupId, userId);
        return groupService.enterGroup(groupId, userId);
    }

    @ApiOperation("退出组")
    @PostMapping("exit")
    public ResultVO<Void> exitGroup(@RequestParam @ApiParam("组ID") Long groupId) {
        log.info(String.valueOf(groupId));
        return groupService.exitGroup(groupId, SecurityUtils.getUserId());
    }

    @ApiOperation("移除组用户")
    @PostMapping("remove")
    public ResultVO<Void> removeGroupUser(@NotNull @ApiParam("组ID") Long groupId, @NotNull @ApiParam("需要移除的用户ID") Long removedUserId) {
        log.info("groupId: {}, removeUserId: {}", groupId, removedUserId);
        return groupService.removeGroupUser(groupId, removedUserId);
    }

    @ApiOperation("更新组内用户个人信息")
    @PutMapping("update")
    public ResultVO<Void> updateGroupUser(@Valid UpdateGroupUserDTO updateGroupUserDTO) {
        log.info(String.valueOf(updateGroupUserDTO));
        GroupUser groupUser = new GroupUser(updateGroupUserDTO);
        return groupService.updateGroupByGroupUser(groupUser);
    }
}
