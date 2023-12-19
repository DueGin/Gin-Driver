package duegin.ginDriver.controller;

import duegin.ginDriver.common.security.utils.SecurityUtils;
import duegin.ginDriver.domain.dto.group.AddGroupDTO;
import duegin.ginDriver.domain.dto.group.UpdateGroupDTO;
import duegin.ginDriver.domain.po.Group;
import duegin.ginDriver.domain.vo.GroupVO;
import duegin.ginDriver.domain.vo.Result;
import duegin.ginDriver.mapper.GroupMapper;
import duegin.ginDriver.service.impl.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author DueGin
 */
@Api(tags = "组管理")
@Slf4j
@RestController
@RequestMapping("group")
@Validated
public class GroupController {
    @Resource
    private GroupService groupService;

    @Resource
    private GroupMapper groupMapper;

    @ApiOperation("创建组")
    @PostMapping("create")
    public Result<Void> createGroup(@RequestBody @ApiParam(value = "组信息", required = true) AddGroupDTO groupParam) {
        log.info(String.valueOf(groupParam));
        Group group = new Group();
        BeanUtils.copyProperties(groupParam, group);
        group.setUserId(SecurityUtils.getUserId());
        return groupService.createGroup(group);
    }

    @ApiOperation("删除组")
    @DeleteMapping("{groupId}")
    public Result<Void> deleteGroup(@PathVariable @ApiParam("组ID") Long groupId) {
        log.info(String.valueOf(groupId));
        return groupService.deleteGroup(groupId, SecurityUtils.getUserId());
    }

    @ApiOperation("修改组信息")
    @PutMapping("modify")
    public Result<Void> modifyGroup(@RequestBody @Valid UpdateGroupDTO groupParam) {
        log.info(String.valueOf(groupParam));
        Group group = new Group();
        BeanUtils.copyProperties(groupParam, group);
        return groupService.updateGroup(group);
    }

    @ApiOperation("更换组拥有者")
    @PostMapping("god/change")
    public Result<Void> changeGroupGod(@NotBlank Long groupId, @NotBlank Long userId) {
        log.info("groupId: {}, groupNewGodId: {}", groupId, userId);
        Long oldGodId = SecurityUtils.getUserId();
        return groupService.changeGroupGod(groupId, oldGodId, userId);
    }

    @ApiOperation("查询当前用户加入的组列表")
    @GetMapping("user_group_list")
    public Result<List<GroupVO>> userGroupList() {
        List<GroupVO> groups = groupMapper.selectAllByUserId(SecurityUtils.getUserId());
        return Result.ok(groups);
    }

    @ApiOperation("修改组成员角色")
    @PostMapping("edit/role")
    public Result<Void> editRole(@NotBlank @ApiParam("组ID") Long groupId,
                                 @NotBlank @ApiParam("待修改用户角色的ID") Long userId,
                                 @NotBlank @ApiParam("授予的组角色ID") Long roleId) {
        log.info("groupId: {}, userId: {}, roleId: {}", groupId, userId, roleId);
        return groupService.editGroupUserRole(groupId, userId, roleId);
    }

}
