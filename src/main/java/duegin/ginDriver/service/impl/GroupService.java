package duegin.ginDriver.service.impl;

import duegin.ginDriver.domain.model.Group;
import duegin.ginDriver.domain.vo.Result;
import duegin.ginDriver.mapper.GroupMapper;
import duegin.ginDriver.mapper.RoleMapper;
import duegin.ginDriver.service.manager.GroupUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author DueGin
 */
@Slf4j
@Service
public class GroupService {
    @Resource
    private GroupUserManager groupUserManager;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private GroupMapper groupMapper;


    public Result<Void> createGroup(Group group) {
        return groupUserManager.createGroup(group) ? Result.ok("创建组成功！") : Result.fail("创建组失败");
    }

    @Transactional
    @NotNull
    public Result<Void> deleteGroup(Long groupId, Long userId) {
        Long creatorId = groupMapper.selectCreatorIdByGroupId(groupId);
        if (creatorId.equals(userId)) {
            groupMapper.deleteByGroupId(groupId);
            groupMapper.deleteGroupUserRoleByGroupId(groupId);
            return Result.ok("删除组成功！");
        }

        return Result.fail("你不是组的拥有者");
    }


    public Result<Void> updateGroup(Group group) {
        // todo 判断权限
//        List<String> roles = SecurityUtils.getRole();
//        String canRole = "ROLE_GROUP_" + group.getGroupId()

        return groupMapper.modifyByGroupId(group) ? Result.ok("更新成功") : Result.fail("更新失败");
    }

    @Transactional
    public Result<Void> changeGroupGod(Long groupId, Long oldGodId, Long newGodId) {
        Long dbOldGodId = groupMapper.selectCreatorIdByGroupId(groupId);
        if (!dbOldGodId.equals(oldGodId)) {
            return Result.fail("你不是组的拥有者");
        }

        // 修改组表
        groupMapper.modifyByGroupId(new Group()
                .setGroupId(groupId)
                .setUserId(newGodId)
        );

        // 旧拥有者改为组普通用户
        groupMapper.modifyGURoleByGroupIdAndUserId(groupId, oldGodId, 5L);
        // 新拥有者改为组管理员
        groupMapper.modifyGURoleByGroupIdAndUserId(groupId, newGodId, 4L);

        // 修改权限表
        return Result.ok("更换老大成功！");
    }


    public Result<Void> removeGroupUser(Long groupId, Long removedUserId) {
        // 校验权限 todo 写一个aop校验组权限 或者 工具类通用方法
//        Long userId = SecurityUtils.getUserId();
//        String roleName = roleMapper.selectGroupRoleByUserIdAndGroupId(userId, groupId);

        // 移除组用户
        groupMapper.deleteGURByGroupIdAndUserId(groupId, removedUserId);

        return Result.ok("移除组用户成功！");
    }

    /**
     * 用户加入组
     *
     * @return true - 加入成功，false - 加入失败
     */
    @NotNull
    public Result<Void> enterGroup(Long groupId, Long userId) {
        // 查询是否已加入
        Long roleId = groupMapper.selectGURoleByGroupIdAndUserId(groupId, userId);
        if (roleId != null) {
            return Result.fail("已经加入该组了，请勿重复加入！");
        }

        return groupMapper.insertGroupUserRole(groupId, userId, 5L) ? Result.ok("加入成功") : Result.fail("加入失败");
    }

    /**
     * 用户退出组
     *
     * @return true - 退出成功，false - 退出失败
     */
    @NotNull
    public Result<Void> exitGroup(Long groupId, Long userId) {
        // 判断是否为组的创建者，不让他退出（这块逻辑没想好怎么处理）
        Long creatorId = groupMapper.selectCreatorIdByGroupId(groupId);
        if (creatorId.equals(userId)) {
            return Result.fail("组拥有者不能退出组");
        }

        // 删除group_user_role表
        return groupMapper.deleteGURByGroupIdAndUserId(groupId, userId) ? Result.ok("退出成功！") : Result.fail("退出失败");
    }
}
