package duegin.ginDriver.mapper;


import java.util.List;

/**
 * @author DueGin
 */
public interface PermissionMapper {
    /**
     * 获取用户权限列表
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> getUserPermissions(Long userId);
}
