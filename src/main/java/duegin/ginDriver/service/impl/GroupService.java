package duegin.ginDriver.service.impl;

import duegin.ginDriver.common.security.utils.SecurityUtils;
import duegin.ginDriver.domain.po.Group;
import duegin.ginDriver.domain.po.GroupUser;
import duegin.ginDriver.domain.po.User;
import duegin.ginDriver.domain.vo.Result;
import duegin.ginDriver.mapper.GroupMapper;
import duegin.ginDriver.mapper.UserMapper;
import duegin.ginDriver.service.manager.GroupUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author DueGin
 */
@Slf4j
@Service
public class GroupService {
    @Resource
    private GroupUserManager groupUserManager;

    @Resource
    private UserMapper userMapper;

    @Resource
    private GroupMapper groupMapper;

    private static final String ADMIN = "ROLE_GROUP_ADMIN";
    private static final String USER = "ROLE_GROUP_USER";
    private static final String VISITOR = "ROLE_GROUP_VISITOR";
    private static final String DISABLED = "ROLE_GROUP_DISABLED";

//    @AllArgsConstructor
//    private enum RoleAuthEnum {
//        ADMIN("ROLE_GROUP_ADMIN"),
//        USER("ROLE_GROUP_USER"),
//        VISITOR("ROLE_GROUP_VISITOR"),
//        DISABLED("ROLE_GROUP_DISABLED");
//        private final String roleName;
//    }


    /**
     * 创建组
     */
    public Result<Void> createGroup(Group group) {
        return groupUserManager.createGroup(group) ? Result.ok("创建组成功！") : Result.fail("创建组失败");
    }

    /**
     * 删除组<br/>
     * 只有组拥有者才能删除
     *
     * @param groupId    组ID
     * @param groupGodId 组拥有者ID
     */
    @Transactional
    public Result<Void> deleteGroup(Long groupId, Long groupGodId) {
        // 判断是否为组的拥有者
        Long creatorId = groupMapper.selectCreatorIdByGroupId(groupId);
        if (creatorId.equals(groupGodId)) {
            groupMapper.deleteByGroupId(groupId);
            groupMapper.deleteGroupUserRoleByGroupId(groupId);
            // todo 需要重新颁发token，同时设置剩下的时间，前端需要在响应拦截器重新设置token
            // todo 通知其他在该组的用户，并且删除他们的角色权限
            return Result.ok("删除组成功！");
        }
        return Result.fail("你不是组的拥有者");
    }

    /**
     * 更新组信息
     */
    public Result<Void> updateGroup(Group group) {
        // 判断权限
        Boolean checked = checkGroupAuth(group.getId(), ADMIN);
        if (!checked) {
            return Result.fail("权限不足！");
        }

        return groupMapper.modifyByGroupId(group) ? Result.ok("更新成功") : Result.fail("更新失败");
    }

    /**
     * 校验组用户权限
     *
     * @param groupId  组ID
     * @param needRole 需要的角色权限
     * @return true - 通过，false - 不通过
     */
    public static Boolean checkGroupAuth(Long groupId, String needRole) {
        Map<String, String> roleMap = SecurityUtils.getRole();
        String s = roleMap.get(String.valueOf(groupId));
        // 判断role
        switch (s) {
            case ADMIN:
                return true;
            case USER:
                return !needRole.equals(ADMIN);
            case VISITOR:
                return !needRole.equals(ADMIN) && !needRole.equals(USER);
            case DISABLED:
                return false;
            default:
                log.warn("未设置权限");
                return false;
        }
    }


    /**
     * 更换组拥有者
     *
     * @param groupId  组ID
     * @param oldGodId 旧组拥有者ID
     * @param newGodId 新组拥有者ID
     */
    @Transactional
    public Result<Void> changeGroupGod(Long groupId, Long oldGodId, Long newGodId) {
        Long dbOldGodId = groupMapper.selectCreatorIdByGroupId(groupId);
        if (!dbOldGodId.equals(oldGodId)) {
            return Result.fail("你不是组的拥有者");
        }

        Long roleId = groupMapper.selectGURoleByGroupIdAndUserId(groupId, newGodId);
        if (roleId == null) {
            return Result.fail("该用户不是该组成员");
        }

        // 修改组表
        groupMapper.modifyByGroupId(new Group()
                .setId(groupId)
                .setUserId(newGodId)
        );

        // 旧拥有者改为组普通用户
        groupMapper.modifyGURoleByGroupIdAndUserId(groupId, oldGodId, 6L);
        // 新拥有者改为组管理员
        groupMapper.modifyGURoleByGroupIdAndUserId(groupId, newGodId, 5L);

        // 修改权限表
        return Result.ok("更换老大成功！");
    }

    /**
     * 移除组用户<br/>
     * 子哟组管理员才能管理组用户
     *
     * @param groupId       组ID
     * @param removedUserId 要移除的组用户ID
     */
    public Result<Void> removeGroupUser(Long groupId, Long removedUserId) {
        // 判断权限
        Boolean checked = checkGroupAuth(groupId, ADMIN);
        if (!checked) {
            return Result.fail("权限不足！");
        }

        // 移除组用户
        groupMapper.deleteGURByGroupIdAndUserId(groupId, removedUserId);

        return Result.ok("移除组用户成功！");
    }

    /**
     * 修改组内用户角色
     *
     * @param groupId 组ID
     * @param userId  需要修改用户角色的ID
     * @param roleId  角色ID
     */
    public Result<Void> editGroupUserRole(Long groupId, Long userId, Long roleId) {
        // 判断权限
        Boolean checked = checkGroupAuth(groupId, ADMIN);
        if (!checked) {
            return Result.fail("权限不足！");
        }

        // 用户无法修改自己的权限，因为已经是组内管理员了
        if (userId.equals(SecurityUtils.getUserId())) {
            return Result.fail("你确定要修改你至高无上的权限吗？");
        }

        return groupMapper.modifyGURoleByGroupIdAndUserId(groupId, userId, roleId) ? Result.ok("修改成功！") : Result.fail("修改失败");
    }

    /**
     * 组内成员更新组个人信息
     *
     * @param groupUser 组个人信息
     */
    public Result<Void> updateGroupByGroupUser(GroupUser groupUser) {
        Long userId = SecurityUtils.getUserId();
        Long groupId = groupUser.getGroupId();
        Long roleId = groupMapper.selectGURoleByGroupIdAndUserId(groupId, userId);
        // 判断是否存在该组
        if (roleId == null) {
            return Result.fail("你干嘛，你都不在这个组里面");
        }
        return groupMapper.modifyGURUsernameByGroupIdAndUserId(groupId, userId, groupUser.getGroupUsername()) ? Result.ok("修改成功！") : Result.fail("修改失败");
    }

    /**
     * 用户加入组
     *
     * @return true - 加入成功，false - 加入失败
     */
    public Result<Void> enterGroup(Long groupId, Long userId) {
        // 查询是否已加入
        Long roleId = groupMapper.selectGURoleByGroupIdAndUserId(groupId, userId);
        if (roleId != null) {
            return Result.fail("已经加入该组了，请勿重复加入！");
        }

        // 查询是否存在这个组
        Long groupGodId = groupMapper.selectCreatorIdByGroupId(groupId);
        if (groupGodId == null) {
            return Result.fail("不存在该组");
        }

        User user = userMapper.selectByUserId(userId);

        return groupMapper.insertGroupUserRole(groupId, userId, 6L, user.getUsername()) ? Result.ok("加入成功") : Result.fail("加入失败");
    }

    /**
     * 用户退出组
     *
     * @return true - 退出成功，false - 退出失败
     */
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
