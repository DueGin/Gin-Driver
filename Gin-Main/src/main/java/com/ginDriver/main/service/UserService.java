package com.ginDriver.main.service;

import cn.hutool.core.util.IdUtil;
import com.ginDriver.common.utils.JwtTokenUtils;
import com.ginDriver.common.verifyCode.service.IVerifyCodeService;
import com.ginDriver.core.domain.bo.UserBO;
import com.ginDriver.core.domain.po.User;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.exception.ApiException;
import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.domain.vo.*;
import com.ginDriver.main.mapper.RoleMapper;
import com.ginDriver.main.mapper.UserMapper;
import com.ginDriver.main.security.service.RoleService;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.service.manager.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author DueGin
 */
@Slf4j
@Service
public class UserService extends MyServiceImpl<UserMapper, User> {

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

    @Resource
    private RoleService roleService;

    @Resource
    private FileService fileService;

    //region login

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
        String token = JwtTokenUtils.createToken(user.getUsername(), roleList, roleMap, rememberMe);

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

    //endregion


    public Boolean register(User user) {
        // 校验注册表单
        if (!checkUserForm(user)) {
            return false;
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


    public ResultVO<Void> addUser(User user) {
        return userManager.saveUser(user) ? ResultVO.ok("新增成功！") : ResultVO.fail("新增失败");
    }

    public ResultVO<PageVO<List<SysUserVO>>> queryPage(User user, Integer pageNum, Integer pageSize) {
        log.info(String.valueOf(user));
        Integer count = userMapper.count(user);

        List<User> list = userMapper.page(user, pageSize * (pageNum - 1), pageSize);
        List<Long> userIdList = list.stream().map(User::getId).collect(Collectors.toList());
        List<RoleVO> roleVOList = roleMapper.selectWithUserRoleByUserId(userIdList);
        Map<Long, RoleVO> sysRoleMap = roleVOList.stream().collect(Collectors.toMap(RoleVO::getUserId, r -> r));

        List<GroupRoleVO> groupRoleVOList = roleMapper.selectWithGroupUserRoleByUserId(userIdList);
        Map<Long, List<GroupRoleVO>> groupRoleListMap = groupRoleVOList.stream().collect(Collectors.groupingBy(GroupRoleVO::getUserId));


        List<SysUserVO> vos = list.stream().map(u -> {
            Long userId = u.getId();
            SysUserVO vo = new SysUserVO();
            BeanUtils.copyProperties(u, vo);
            // 系统角色
            vo.setSysRole(sysRoleMap.get(userId).getRoleName());

            // 组角色
            List<GroupRoleVO> groupRoleVOS = groupRoleListMap.get(userId);
            if (groupRoleVOS != null) {
                List<String> groupRoleList = groupRoleVOS.stream()
                        .map(r -> r.getGroupId() + "=" + r.getRoleName())
                        .collect(Collectors.toList());
                vo.setGroupRoleList(groupRoleList);
            } else {
                vo.setGroupRoleList(Collections.emptyList());
            }

            return vo;
        }).collect(Collectors.toList());

        PageVO<List<SysUserVO>> pageVO = new PageVO<>();
        pageVO.setTotal(count);
        pageVO.setPage(pageNum);
        pageVO.setRows(vos);
        return ResultVO.ok(pageVO);
    }

    public ResultVO<Void> deleteUser(Long userId, boolean isAdmin) {

        if (isAdmin) {
            // 判断删的是不是自己
            Long uid = SecurityUtils.getUserId();
            if (uid.equals(userId)) {
                return ResultVO.fail("歪，别删自己啊");
            }
        }

        super.removeById(userId);
        return ResultVO.ok("删除成功！");
    }

    public ResultVO<Void> updateUserInfo(User user, boolean isAdmin) {
        boolean isSelf = false;
        if (isAdmin) {
            // 判断删的是不是自己
            Long userId = SecurityUtils.getUserId();
            if (user.getId().equals(userId)) {
                isSelf = true;
            }
        }

        // 更新当前登录用户信息
        Boolean modified = userMapper.updateUserById(user);
        String msg;
        if (modified) {
            msg = "修改成功！";
            if (!isAdmin || isSelf) {
                UserBO bo = SecurityUtils.getLoginUser();
                if (bo != null) {
                    BeanUtils.copyProperties(user, bo);
                } else {
                    bo = new UserBO();
                    BeanUtils.copyProperties(user, bo);
                }
            }
        } else {
            msg = "修改失败！";
        }

        return ResultVO.ok(msg);
    }

    public ResultVO<FileVO> uploadAvatar(MultipartFile file) {
        UserBO bo = SecurityUtils.getLoginUser();
        if (bo == null) {
            throw new ApiException("登录用户异常");
        }

        ResultVO<FileVO> vo = fileService.upload(FileService.FileType.system, file);
        if (StringUtils.isNotBlank(bo.getAvatar())) {
            Boolean deleted = fileService.deleteFile(FileService.FileType.system, bo.getAvatar());
            if (!deleted) {
                log.error("userId: {}, avatar: {} ==> 文件删除失败！", bo.getId(), bo.getAvatar());
            }
        }
        return vo;
    }


}
