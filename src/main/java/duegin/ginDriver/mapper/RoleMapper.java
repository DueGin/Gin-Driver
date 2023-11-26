package duegin.ginDriver.mapper;


import java.util.List;

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
}
