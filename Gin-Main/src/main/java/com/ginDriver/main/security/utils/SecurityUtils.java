package com.ginDriver.main.security.utils;


import com.ginDriver.core.domain.bo.UserBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 安全服务工具类
 *
 * @author ruoyi
 */
@Slf4j
public class SecurityUtils {

    public static Long getUserId() {
        return getLoginUser().getId();
    }

    /**
     * 获取用户
     **/
    public static UserBO getLoginUser() {
        try {
            Authentication authentication = getAuthentication();
            return (UserBO) authentication.getPrincipal();
        } catch (Exception e) {
            log.error("获取登录用户异常");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取角色KV集合
     */
    public static Map<String, String> getRoleMap() {
        return (Map<String, String>) getAuthentication().getDetails();
    }

    /**
     * 获取系统角色
     */
    public static String getSysRole() {
        return getRoleMap().get("sys");
    }

    /**
     * 获取组角色KV集合
     */
    public static Map<String, String> getGroupRoleMap() {
        Map<String, String> roleMap = getRoleMap();
        return roleMap.entrySet().stream()
                .filter(e -> !e.getKey().equals("sys"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 获取组角色列表
     */
    public static List<String> getGroupRoleList() {
        Map<String, String> roleMap = getRoleMap();
        return roleMap.entrySet().stream()
                .filter(e -> !e.getKey().equals("sys"))
                .map(e-> e.getKey() + "=" + e.getValue())
                .collect(Collectors.toList());
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
}
