package duegin.ginDriver.mapper;


import com.mybatisflex.core.BaseMapper;
import duegin.ginDriver.domain.po.Role;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
 * @author DueGin
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 获取用户角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<String> getUserRole(Long userId);

    /**
     * 查询用户所有组权限
     * @return [{groupId: 组ID, roleName: 用户角色}]
     */
    @MapKey("groupId")
    List<Map<String, Object>> selectGroupRoleByUserId(Long userId);

    String selectGroupRoleByUserIdAndGroupId(Long userId, Long groupId);


    ////// sys //////
    Boolean deleteUserRoleByUserId(Long userId);

    Boolean modifyUserRole(Long userId, Long roleId);

}
