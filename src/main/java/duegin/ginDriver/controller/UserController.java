package duegin.ginDriver.controller;

import duegin.ginDriver.common.security.utils.SecurityUtils;
import duegin.ginDriver.common.utils.JwtTokenUtils;
import duegin.ginDriver.domain.model.User;
import duegin.ginDriver.domain.vo.Result;
import duegin.ginDriver.domain.vo.UserVO;
import duegin.ginDriver.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author DueGin
 */
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 登录
     *
     * @param user 用户登录信息
     * @return 用户信息
     */
    @PostMapping("login")
    public UserVO login(@RequestBody User user, HttpServletResponse response) {
        log.info(user.toString());
        // 处理登录
        String token = userService.login(user);
        // 设置响应头
        response.setHeader(JwtTokenUtils.TOKEN_HEADER, JwtTokenUtils.TOKEN_PREFIX + token);
        return SecurityUtils.getLoginUser();
    }

    @PostMapping("register")
    public Result<Void> register(@RequestBody User user){
        log.info(user.toString());
        // 处理注册


        return null;
    }


}
