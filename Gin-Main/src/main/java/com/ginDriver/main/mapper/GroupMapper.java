package com.ginDriver.main.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ginDriver.main.domain.po.Group;
import com.ginDriver.main.domain.vo.GroupVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author DueGin
 */
public interface GroupMapper extends BaseMapper<Group> {

    Boolean save(Group group);

    Boolean deleteByGroupId(Long groupId);

    Boolean modifyByGroupId(Group group);

    Long selectCreatorIdByGroupId(Long groupId);


    ////// group_user_role //////

    /**
     * 根据用户ID查询用户加入的组
     *
     * @return 用户加入的组集合
     */
    List<GroupVO> selectAllByUserId(Long userId);

    Long selectGURoleByGroupIdAndUserId(Long groupId, Long userId);


    /**
     * group_user_role新增<br/>
     * 用户加入组时添加，用户新建组时也添加
     *
     * @return true: 加入组成功，false: 加入组失败
     */
    Boolean insertGroupUserRole(@Param("groupId") Long groupId,
                                @Param("userId") Long userId,
                                @Param("roleId") Long roleId,
                                @Param("groupUsername") String groupUsername
    );

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

    Boolean modifyGURUsernameByGroupIdAndUserId(Long groupId, Long userId, String groupUsername);
}




