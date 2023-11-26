package duegin.ginDriver.service;


import java.util.Set;


/**
 * @author DueGin
 */
public interface IPermissionService {
    /**
     * 获取用户角色和权限集合
     *
     * @param userId 用户ID
     * @return 角色和权限集合
     */
    Set<String> getUserAuthorities(Long userId);
}
