package com.ginDriver.main.controller;

import com.ginDriver.common.utils.JwtTokenUtils;
import com.ginDriver.core.domain.bo.UserBO;
import com.ginDriver.core.domain.po.User;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.domain.vo.UserVO;
import com.ginDriver.main.domain.dto.user.UserParam;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author DueGin
 */
@Api(tags = "用户")
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

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
        String token = userService.login(user);

        // 设置token头浏览器可见
        response.setHeader("Access-Control-Expose-Headers", JwtTokenUtils.TOKEN_HEADER);

        // 设置响应头
        response.setHeader(JwtTokenUtils.TOKEN_HEADER, JwtTokenUtils.TOKEN_PREFIX + token);

        UserBO bo = SecurityUtils.getLoginUser();
        UserVO vo = new UserVO();
        if (bo != null) {
            BeanUtils.copyProperties(bo, vo, "roleMap", "roleList");
            vo.setSysRole(SecurityUtils.getSysRole());
            // 获取组角色列表
            List<String> groupRoleList = SecurityUtils.getGroupRoleList();
            vo.setGroupRoleList(groupRoleList);
        }
        return ResultVO.ok(vo);
    }

    @ApiOperation("注册")
    @ApiImplicitParams({@ApiImplicitParam(name = "userParam", value = "注册信息", required = true)})
    @PostMapping("register")
    public ResultVO<Void> register(@RequestBody UserParam userParam) {
        log.info(userParam.toString());
        User u = new User();
        BeanUtils.copyProperties(userParam, u);
        // 处理注册
        userService.register(u);

        return ResultVO.ok();
    }


}
