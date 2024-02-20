package com.ginDriver.main.controller;

import com.ginDriver.common.utils.JwtTokenUtils;
import com.ginDriver.core.domain.bo.UserBO;
import com.ginDriver.core.domain.po.User;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.domain.vo.UserVO;
import com.ginDriver.core.log.GinLog;
import com.ginDriver.core.result.BusinessController;
import com.ginDriver.main.cache.redis.TokenRedis;
import com.ginDriver.main.domain.dto.user.UpdateUserDTO;
import com.ginDriver.main.domain.dto.user.UserParam;
import com.ginDriver.main.domain.vo.FileVO;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.service.FileService;
import com.ginDriver.main.service.UserService;
import com.ginDriver.main.service.manager.UserManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author DueGin
 */
@Api(tags = "用户")
@Slf4j
@BusinessController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserManager userManager;

    @Resource
    private FileService fileService;

    @Resource
    private TokenRedis tokenRedis;

    /**
     * 登录
     *
     * @param user 用户登录信息
     * @return 用户信息
     */
    @ApiOperation("登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "user", value = "登录用户信息", required = true)})
    @PostMapping("login")
    public ResultVO<UserVO> login(@RequestBody User user, HttpServletResponse response) {
        log.info(user.toString());

        // 处理登录
        String token = userManager.login(user);

        // 设置token头浏览器可见
        response.setHeader("Access-Control-Expose-Headers", JwtTokenUtils.TOKEN_HEADER);

        // 设置响应头
        response.setHeader(JwtTokenUtils.TOKEN_HEADER, JwtTokenUtils.TOKEN_PREFIX + token);

        UserBO bo = SecurityUtils.getLoginUser();
        UserVO vo = new UserVO();
        if (bo != null) {
            BeanUtils.copyProperties(bo, vo, "roleMap", "roleList");
            vo.setSysRole(SecurityUtils.getSysRole());

            // 设置头像
            if (StringUtils.isNotBlank(bo.getAvatar())) {
                String avatarUrl = fileService.getObjUrl(FileService.FileType.system, bo.getAvatar());
                vo.setAvatarUrl(avatarUrl);
            }

            // 获取组角色列表
            List<String> groupRoleList = SecurityUtils.getGroupRoleList();
            vo.setGroupRoleList(groupRoleList);
        }
        return ResultVO.ok(vo);
    }

    @ApiOperation("注册")
    @ApiImplicitParams({@ApiImplicitParam(name = "userParam", value = "注册信息", required = true)})
    @PostMapping("register")
    public void register(@RequestBody UserParam userParam) {
        log.info(userParam.toString());
        User u = new User();
        BeanUtils.copyProperties(userParam, u);
        // 处理注册
        userManager.register(u);
    }

    @GetMapping("logout")
    public void logout(HttpServletRequest request) {
        String tokenHeader = request.getHeader(JwtTokenUtils.TOKEN_HEADER);
        String token = JwtTokenUtils.getToken(tokenHeader);
        tokenRedis.setBlankToken(token);
        log.info("登出成功！");
    }

    @ApiOperation("获取登录用户详情")
    @GetMapping("detail")
    public ResultVO<UserVO> detail() {
        UserBO bo = SecurityUtils.getLoginUser();
        UserVO vo = new UserVO();
        if (bo != null) {
            BeanUtils.copyProperties(bo, vo);
            String avatar = bo.getAvatar();
            if (StringUtils.isNotBlank(avatar)) {
                String avatarUrl = fileService.getObjUrl(FileService.FileType.system, avatar);
                vo.setAvatarUrl(avatarUrl);
            }
            Map<String, String> roleMap = bo.getRoleMap();
            vo.setSysRole(roleMap.get("sys"));
            List<String> roleList = bo.getRoleList();
            List<String> groupRoleList = roleList.stream()
                    .filter(s -> !s.startsWith("sys="))
                    .collect(Collectors.toList());
            vo.setGroupRoleList(groupRoleList);
        }


        return ResultVO.ok(vo);
    }

    @PostMapping("avatar/upload")
    public ResultVO<FileVO> uploadAvatar(MultipartFile file) {
        return userService.uploadAvatar(file);
    }

    @GinLog
    @PutMapping("update")
    public ResultVO<Void> update(@RequestBody @Valid UpdateUserDTO user) {
        return userService.updateUserInfo(user, false);
    }

    @GinLog
    @DeleteMapping("remove/{id}")
    public ResultVO<Void> remove(@PathVariable Long id) {
        return userService.deleteUser(id, false);
    }
}
