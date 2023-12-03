package duegin.ginDriver.mapper;


import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
 * @author DueGin
 */
public interface RoleMapper {

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
}
