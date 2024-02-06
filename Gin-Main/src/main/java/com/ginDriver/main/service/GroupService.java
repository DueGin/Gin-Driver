package com.ginDriver.main.service;

import com.ginDriver.core.domain.po.User;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.domain.po.Group;
import com.ginDriver.main.domain.po.GroupUser;
import com.ginDriver.main.groupAuth.HasGroupRole;
import com.ginDriver.main.groupAuth.constants.GroupAuthEnum;
import com.ginDriver.main.mapper.GroupMapper;
import com.ginDriver.main.mapper.UserMapper;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.service.manager.GroupUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author DueGin
 */
@Slf4j
@Service
public class GroupService extends MyServiceImpl<GroupMapper, Group> {
    @Resource
    private GroupUserManager groupUserManager;

    @Resource
    private UserMapper userMapper;

    @Resource
    private GroupMapper groupMapper;


    /**
     * 创建组
     */
    public ResultVO<Void> createGroup(Group group) {
        return groupUserManager.createGroup(group) ? ResultVO.ok("创建组成功！") : ResultVO.fail("创建组失败");
    }

    /**
     * 删除组<br/>
     * 只有组拥有者才能删除
     *
     * @param groupId    组ID
     * @param groupGodId 组拥有者ID
     */
    @Transactional
    public ResultVO<Void> deleteGroup(Long groupId, Long groupGodId) {
        GroupMapper groupMapper = (GroupMapper) super.getBaseMapper();
        // 判断是否为组的拥有者
        Long creatorId = groupMapper.selectCreatorIdByGroupId(groupId);
        if (creatorId.equals(groupGodId)) {
            groupMapper.deleteByGroupId(groupId);
            groupMapper.deleteGroupUserRoleByGroupId(groupId);
            // todo 需要重新颁发token，同时设置剩下的时间，前端需要在响应拦截器重新设置token
            // todo 通知其他在该组的用户，并且删除他们的角色权限
            return ResultVO.ok("删除组成功！");
        }
        return ResultVO.fail("你不是组的拥有者");
    }

    /**
     * 更新组信息
     */
    @HasGroupRole(GroupAuthEnum.ADMIN)
    public ResultVO<Void> updateGroup(Group group) {
        return updateById(group) ? ResultVO.ok("更新成功") : ResultVO.fail("更新失败");
    }


    /**
     * 更换组拥有者
     *
     * @param groupId  组ID
     * @param oldGodId 旧组拥有者ID
     * @param newGodId 新组拥有者ID
     */
    @Transactional
    public ResultVO<Void> changeGroupGod(Long groupId, Long oldGodId, Long newGodId) {
        Long dbOldGodId = groupMapper.selectCreatorIdByGroupId(groupId);
        if (!dbOldGodId.equals(oldGodId)) {
            return ResultVO.fail("你不是组的拥有者");
        }

        Long roleId = groupMapper.selectGURoleByGroupIdAndUserId(groupId, newGodId);
        if (roleId == null) {
            return ResultVO.fail("该用户不是该组成员");
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
        return ResultVO.ok("更换老大成功！");
    }

    /**
     * 移除组用户<br/>
     * 子哟组管理员才能管理组用户
     *
     * @param groupId       组ID
     * @param removedUserId 要移除的组用户ID
     */
    @HasGroupRole(GroupAuthEnum.ADMIN)
    public ResultVO<Void> removeGroupUser(Long groupId, Long removedUserId) {
        // 判断权限
//        Boolean checked = checkGroupAuth(groupId, ADMIN);
//        if (!checked) {
//            return ResultVO.fail("权限不足！");
//        }

        // 移除组用户
        groupMapper.deleteGURByGroupIdAndUserId(groupId, removedUserId);

        return ResultVO.ok("移除组用户成功！");
    }

    /**
     * 修改组内用户角色
     *
     * @param groupId 组ID
     * @param userId  需要修改用户角色的ID
     * @param roleId  角色ID
     */
    @HasGroupRole(GroupAuthEnum.ADMIN)
    public ResultVO<Void> editGroupUserRole(Long groupId, Long userId, Long roleId) {
        // 判断权限
//        Boolean checked = checkGroupAuth(groupId, ADMIN);
//        if (!checked) {
//            return ResultVO.fail("权限不足！");
//        }

        // 用户无法修改自己的权限，因为已经是组内管理员了
        if (userId.equals(SecurityUtils.getUserId())) {
            return ResultVO.fail("你确定要修改你至高无上的权限吗？");
        }

        return groupMapper.modifyGURoleByGroupIdAndUserId(groupId, userId, roleId) ? ResultVO.ok("修改成功！") : ResultVO.fail("修改失败");
    }

    /**
     * 组内成员更新组个人信息
     *
     * @param groupUser 组个人信息
     */
    public ResultVO<Void> updateGroupByGroupUser(GroupUser groupUser) {
        Long userId = SecurityUtils.getUserId();
        Long groupId = groupUser.getGroupId();
        Long roleId = groupMapper.selectGURoleByGroupIdAndUserId(groupId, userId);
        // 判断是否存在该组
        if (roleId == null) {
            return ResultVO.fail("你干嘛，你都不在这个组里面");
        }
        return groupMapper.modifyGURUsernameByGroupIdAndUserId(groupId, userId, groupUser.getGroupUsername()) ? ResultVO.ok("修改成功！") : ResultVO.fail("修改失败");
    }

    /**
     * 用户加入组
     *
     * @return true - 加入成功，false - 加入失败
     */
    public ResultVO<Void> enterGroup(Long groupId, Long userId) {
        // 查询是否已加入
        Long roleId = groupMapper.selectGURoleByGroupIdAndUserId(groupId, userId);
        if (roleId != null) {
            return ResultVO.fail("已经加入该组了，请勿重复加入！");
        }

        // 查询是否存在这个组
        Long groupGodId = groupMapper.selectCreatorIdByGroupId(groupId);
        if (groupGodId == null) {
            return ResultVO.fail("不存在该组");
        }

        User user = userMapper.selectByUserId(userId);

        return groupMapper.insertGroupUserRole(groupId, userId, 6L, user.getUsername()) ? ResultVO.ok("加入成功") : ResultVO.fail("加入失败");
    }

    /**
     * 用户退出组
     *
     * @return true - 退出成功，false - 退出失败
     */
    public ResultVO<Void> exitGroup(Long groupId, Long userId) {
        // 判断是否为组的创建者，不让他退出（这块逻辑没想好怎么处理）
        Long creatorId = groupMapper.selectCreatorIdByGroupId(groupId);
        if (creatorId.equals(userId)) {
            return ResultVO.fail("组拥有者不能退出组");
        }

        // 删除group_user_role表
        return groupMapper.deleteGURByGroupIdAndUserId(groupId, userId) ? ResultVO.ok("退出成功！") : ResultVO.fail("退出失败");
    }
}
