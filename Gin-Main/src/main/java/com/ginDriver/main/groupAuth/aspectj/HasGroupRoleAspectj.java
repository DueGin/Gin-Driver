package com.ginDriver.main.groupAuth.aspectj;

import com.ginDriver.main.domain.po.Group;
import com.ginDriver.main.domain.vo.GroupVO;
import com.ginDriver.main.groupAuth.HasGroupRole;
import com.ginDriver.main.groupAuth.constants.GroupAuthEnum;
import com.ginDriver.main.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;



/**
 * 资源组权限校验
 *
 * @author DueGin
 */
@Slf4j
@Aspect
@Component
public class HasGroupRoleAspectj {

//    private static final String ADMIN = "ROLE_GROUP_ADMIN";
//    private static final String USER = "ROLE_GROUP_USER";
//    private static final String VISITOR = "ROLE_GROUP_VISITOR";
//    private static final String DISABLED = "ROLE_GROUP_DISABLED";

    @Before("@annotation(hasGroupRole)")
    public void before(JoinPoint joinPoint, HasGroupRole hasGroupRole) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        GroupAuthEnum needRole = hasGroupRole.value();
        Object[] args = joinPoint.getArgs();
        Long groupId = null;
        for (Object arg : args) {
            String name = arg.getClass().getName();
            if (name.equals("groupId") && arg instanceof Long) {
                groupId = (Long) arg;
                break;
            } else if(arg instanceof Group || arg instanceof GroupVO){
                Method getId = arg.getClass().getMethod("getId");
                groupId = (Long) getId.invoke(arg);
            }
        }

        // 形参中没有groupId
        if (groupId == null) {
            log.error("groupId找不到");
            throw new RuntimeException("groupId找不到");
        }

        // 校验权限
        if (!checkGroupAuth(groupId, needRole)) {
            log.error("资源组权限不足，需要" + needRole + "身份");
            throw new AccessDeniedException("资源组权限不足");
        }
        log.info("资源组权限校验通过");
    }

    /**
     * 校验组用户权限
     *
     * @param groupId  组ID
     * @param needRole 需要的角色权限
     * @return true - 通过，false - 不通过
     */
    public static Boolean checkGroupAuth(Long groupId, GroupAuthEnum needRole) {
        Map<String, String> roleMap = SecurityUtils.getRoleMap();
        String s = roleMap.get(String.valueOf(groupId));
        // 判断role
        switch (s) {
            case "ROLE_GROUP_ADMIN":
                return true;
            case "ROLE_GROUP_USER":
                return !needRole.equals(GroupAuthEnum.ADMIN);
            case "ROLE_GROUP_VISITOR":
                return !needRole.equals(GroupAuthEnum.ADMIN) && !needRole.equals(GroupAuthEnum.USER);
            case "ROLE_GROUP_DISABLED":
                return false;
            default:
                log.warn("未设置权限");
                return false;
        }
    }
}
