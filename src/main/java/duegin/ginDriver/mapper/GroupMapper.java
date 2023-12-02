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
     *
     * @return 用户加入的组集合
     */
    List<Group> selectAllByUserId(Long userId);

    Long selectGURoleByGroupIdAndUserId(Long groupId, Long userId);


    /**
     * group_user_role新增<br/>
     * 用户加入组时添加，用户新建组时也添加
     *
     * @return true: 加入组成功，false: 加入组失败
     */
    Boolean insertGroupUserRole(Long groupId, Long userId, Long roleId);

    /**
     * 通过组ID删除用户、组、角色关联表信息<br/>
     * 组创建者删除组
     *
     * @return true: 删除组成功，false: 删除组失败
     */
    Boolean deleteGroupUserRoleByGroupId(Long groupId);

    /**
     * 通过组ID和用户ID删除用户加入的组
     *
     * @return true: 退出组成功，false: 退出组失败
     */
    Boolean deleteGURByGroupIdAndUserId(Long groupId, Long userId);


    Boolean modifyGURoleByGroupIdAndUserId(Long groupId, Long userId, Long roleId);
}




