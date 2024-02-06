package com.ginDriver.main.service.manager;

import cn.hutool.core.util.IdUtil;
import com.ginDriver.common.utils.JwtTokenUtils;
import com.ginDriver.common.verifyCode.service.IVerifyCodeService;
import com.ginDriver.core.domain.bo.UserBO;
import com.ginDriver.core.domain.po.User;
import com.ginDriver.core.exception.ApiException;
import com.ginDriver.main.mapper.RoleMapper;
import com.ginDriver.main.mapper.UserMapper;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import java.util.*;

/**
 * @author DueGin
 * 系统用户管理
 */
@Slf4j
@Service
public class UserManager {

    @Resource
    private UserMapper userMapper;


    private final String usernameReg = "^[\u4e00-\u9fa5a-zA-Z0-9]{4,12}$";

    @Resource
    private IVerifyCodeService verifyCodeService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserService userService;


    /**
     * 新增系统用户
     * 创建用户，并分配该用户系统角色为普通用户
     *
     * @param user 新增用户信息
     */
    @Transactional
    public Boolean saveUser(User user) {
        // 插入用户表
        userMapper.insert(user);
        // 插入用户角色表，并给他一个默认角色（普通用户）
        userMapper.insertUserRole(user.getId(), 2L);
        return true;
    }


    // region login

    /**
     * 处理用户名密码登录
     *
     * @param user 登录表单
     * @return token
     */

    public String login(User user) {
        Authentication authenticate;
        try {
            // 校验验证码
            verifyCodeService.verify(user.getUuid(), user.getVerifyCode());

            verifyCodeService.deleteVerifyCode(user.getUuid());
        } catch (LoginException e) {
            log.error("登录验证码错误");
            e.printStackTrace();
            return null;
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, user.getPassword(), new ArrayList<>());

        // 交给manager发证(他会调用userDetailService去验证)
        authenticate = authenticationManager.authenticate(authenticationToken);

        log.info("开始制作token");
        String token;
        if (authenticate.isAuthenticated()) {
            token = successfulAuthentication(authenticate, user.getRememberMe());
        } else {
            token = "";
        }

        // 返回token
        return token;
    }

    /**
     * 校验成功后，发放token
     *
     * @param authentication 校验结果
     * @param rememberMe     是否记住我
     * @return token
     */
    private String successfulAuthentication(Authentication authentication, Boolean rememberMe) {
        // userDetailService返回的userDetail，并不是登录表单的user数据
        User user = (User) authentication.getPrincipal();

        List<String> roleList = new ArrayList<>();
        Map<String, String> roleMap = new HashMap<>();


        // 系统级用户的角色
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            roleMap.put("sys", authority.getAuthority());
            roleList.add("sys=" + authority.getAuthority());
        }

        // 组用户角色
        List<Map<String, Object>> groupRoles = roleMapper.selectGroupRoleByUserId(user.getId());
        if (!CollectionUtils.isEmpty(groupRoles)) {
            for (Map<String, Object> groupRole : groupRoles) {
                Long groupId = (Long) groupRole.get("groupId");
                String roleName = (String) groupRole.get("roleName");
                String role = groupId + "=" + roleName;
                roleList.add(role);
                roleMap.put(String.valueOf(groupId), roleName);
            }
        }


        // 根据用户名，角色创建token并返回token
        String token = JwtTokenUtils.createToken(String.valueOf(user.getId()), roleList, roleMap, rememberMe);

        log.info("token=>> " + token);

        user.setToken(token);
        UserBO userBO = new UserBO();
        BeanUtils.copyProperties(user, userBO);
        userBO.setRoleList(roleList);
        userBO.setRoleMap(roleMap);
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(userBO, null, user.getAuthorities());
        // 存储权限kv
        userToken.setDetails(roleMap);
        SecurityContextHolder.getContext().setAuthentication(userToken);

        return token;
    }

    // endregion


    public void register(User user) {
        // 校验注册表单
        if (!checkUserForm(user)) {
            return;
        }

        try {
            verifyCodeService.verify(user.getUuid(), user.getVerifyCode());
        } catch (LoginException e) {
            log.error("注册验证码错误");
            throw new ApiException("验证码错误");
        }

        verifyCodeService.deleteVerifyCode(user.getUuid());

        user.setId(IdUtil.getSnowflakeNextId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        // 入库user表，分配USER角色给他
        userService.saveUser(user);
    }

    private Boolean checkUserForm(User user) {
        if (user == null) {
            return false;
        }

        // 校验表单
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return false;
        }

        // 校验验证码
        if (StringUtils.isBlank(user.getUuid()) || StringUtils.isBlank(user.getVerifyCode())) {
            return false;
        }

        return true;
    }
}
