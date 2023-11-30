package duegin.ginDriver.mapper;


import duegin.ginDriver.domain.model.Group;

import java.util.List;

/**
* @author DueGin
*/
public interface GroupMapper {

    Boolean insert(Group group);

    Boolean deleteByGroupId(Long groupId);

    Boolean modifyByGroupId(Group group);

    Long selectCreatorIdByGroupId(Long groupId);


    ////// group_user_role //////

    /**
     * 根据用户ID查询用户加入的组
     * @return 用户加入的组集合
     */
    List<Group> selectAllByUserId(Long userId);

    /**
     * group_user_role新增<br/>
     * 用户加入组时添加，用户新建组时也添加
     * @return true: 加入组成功，false: 加入组失败
     */
    Boolean insertGroupUserRole(Long groupId, Long userId, Long roleId);

    Boolean deleteGroupUserRoleByGroupIdAndUserId(Long groupId, Long userId);
}




