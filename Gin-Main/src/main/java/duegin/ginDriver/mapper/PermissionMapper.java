package duegin.ginDriver.mapper;


import com.mybatisflex.core.BaseMapper;
import duegin.ginDriver.domain.po.Permission;

import java.util.List;

/**
 * @author DueGin
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 获取用户权限列表
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> getUserPermissions(Long userId);
}
