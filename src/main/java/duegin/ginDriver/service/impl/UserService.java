package duegin.ginDriver.service.impl;

import cn.hutool.core.util.IdUtil;
import duegin.ginDriver.common.code.service.IVerifyCodeService;
import duegin.ginDriver.common.security.utils.SecurityUtils;
import duegin.ginDriver.common.utils.JwtTokenUtils;
import duegin.ginDriver.domain.model.User;
import duegin.ginDriver.domain.vo.PageVO;
import duegin.ginDriver.domain.vo.Result;
import duegin.ginDriver.domain.vo.SysUserVO;
import duegin.ginDriver.domain.vo.UserVO;
import duegin.ginDriver.mapper.RoleMapper;
import duegin.ginDriver.mapper.UserMapper;
import duegin.ginDriver.service.IUserService;
import duegin.ginDriver.service.manager.UserManager;
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
 */
@Slf4j
@Service
public class UserService implements IUserService {

    private final String usernameReg = "^[\u4e00-\u9fa5a-zA-Z0-9]{4,12}$";

    @Resource
    private IVerifyCodeService verifyCodeService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UserManager userManager;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    //region login

    /**
     * 处理用户名密码登录
     *
     * @param user 登录表单
     * @return token
     */
    @Override
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

        List<String> perms = user.getPerms();

        Map<String, String> roleMap = new HashMap<>();


        // 系统级用户的角色
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            roleMap.put("sys", authority.getAuthority());
        }

        // 组用户角色
        List<Map<String, Object>> groupRoles = roleMapper.selectGroupRoleByUserId(user.getUserId());
        if (!CollectionUtils.isEmpty(groupRoles)) {
            for (Map<String, Object> groupRole : groupRoles) {
                Long groupId = (Long) groupRole.get("groupId");
                String roleName = (String) groupRole.get("roleName");
                String role = groupId + "_" + roleName;
                perms.add(role);
                roleMap.put(String.valueOf(groupId), roleName);
            }
        }


        // 根据用户名，角色创建token并返回token
        String token = JwtTokenUtils.createToken(user.getUsername(), perms, roleMap, rememberMe);

        log.info("token=>> " + token);

        user.setToken(token);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(userVO, null, user.getAuthorities());
        // 存储权限kv
        userToken.setDetails(roleMap);
        SecurityContextHolder.getContext().setAuthentication(userToken);

        return token;
    }

    //endregion

    @Override
    public Boolean register(User user) {
        // 校验注册表单
        if (!checkUserForm(user)) {
            return false;
        }

        try {
            verifyCodeService.verify(user.getUuid(), user.getVerifyCode());
        } catch (LoginException e) {
            log.error("注册验证码错误");
        }

        verifyCodeService.deleteVerifyCode(user.getUuid());

        user.setUserId(IdUtil.getSnowflakeNextId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        // 入库user表，分配USER角色给他
        return userManager.saveUser(user);
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


    public Result<Void> addUser(User user) {
        return userManager.saveUser(user) ? Result.ok("新增成功！") : Result.fail("新增失败");
    }

    public Result<PageVO<List<SysUserVO>>> queryPage(User user, Integer pageNum, Integer pageSize) {
log.info(String.valueOf(user));
        Integer count = userMapper.count(user);

        List<User> list = userMapper.page(user, pageSize * (pageNum - 1), pageSize);
        List<SysUserVO> vos = new ArrayList<>();
        list.forEach(u-> {
            SysUserVO vo = new SysUserVO();
            BeanUtils.copyProperties(u, vo);
            vos.add(vo);
        });

        PageVO<List<SysUserVO>> pageVO = new PageVO<>();
        pageVO.setTotal(count);
        pageVO.setPage(pageNum);
        pageVO.setRows(vos);
        return Result.ok(pageVO);
    }

    @Transactional
    public Result<Void> deleteUser(Long userId) {
        userMapper.deleteByUserId(userId);
        roleMapper.deleteUserRoleByUserId(userId);
        return Result.ok("删除成功！");
    }

    public Result<Void> modifyUserInfo(User user) {
        userMapper.updateUserById(user);

        return Result.ok("修改成功！");
    }

}
