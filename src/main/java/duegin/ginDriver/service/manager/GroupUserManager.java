package duegin.ginDriver.service.manager;

import duegin.ginDriver.mapper.GroupMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author DueGin
 * 组用户管理
 */
@Service
@Slf4j
public class GroupUserManager {

    @Resource
    private GroupMapper groupMapper;

    /**
     * 用户加入组
     * @return true - 加入成功，false - 加入失败
     */
    @NotNull
    public Boolean enterGroup(Long groupId, Long userId, Long roleId){
        return groupMapper.insertGroupUserRole(groupId, userId, roleId);
    }

    /**
     * 用户退出组
     * @return true - 退出成功，false - 退出失败
     */
    @NotNull
    public Boolean exitGroup(Long groupId, Long userId){
        // 判断是否为组的创建者，不让他退出（这块逻辑没想好怎么处理）
        Long creatorId = groupMapper.selectCreatorIdByGroupId(groupId);
        if(creatorId.equals(userId)){
            return false;
        }

        // 删除group_user_role表
        return groupMapper.deleteGroupUserRoleByGroupIdAndUserId(groupId, userId);
    }

}
