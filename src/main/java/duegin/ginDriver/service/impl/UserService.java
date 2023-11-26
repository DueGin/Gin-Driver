package duegin.ginDriver.service.impl;

import duegin.ginDriver.common.code.service.IVerifyCodeService;
import duegin.ginDriver.common.utils.JwtTokenUtils;
import duegin.ginDriver.domain.model.User;
import duegin.ginDriver.domain.vo.UserVO;
import duegin.ginDriver.mapper.UserMapper;
import duegin.ginDriver.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private UserMapper userMapper;

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

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, user.getPassword(), new ArrayList<>());

            // 交给manager发证(他会调用userDetailService去验证)
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (LoginException e) {
            log.error("登录失败");
            e.printStackTrace();
            return null;
        }

        log.info("开始制作token");
        String token;
        if (authenticate.isAuthenticated()) {
            token = successfulAuthentication(authenticate);
        } else {
            token = "";
        }

        // 返回token
        return token;
    }

    public void register(User user) {
        // 校验注册表单
        if (!checkUserForm(user)) {
            return;
        }
        // 入库user表，分配USER角色给他


    }

    private Boolean checkUserForm(User user) {
        if (user == null) {
            return false;
        }

        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return false;
        }

        return true;
    }

    private void saveUser(User user){
        userMapper.insert(user);

    }

    /**
     * 校验成功后，发放token
     *
     * @param authentication 校验结果
     * @return token
     */
    private String successfulAuthentication(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        List<String> roles = new ArrayList<>();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }

        // 根据用户名，角色创建token并返回token
        String token = JwtTokenUtils.createToken(user.getUsername(), roles, user.getIsRememberMe());

        log.info("token=>> " + token);

        user.setToken(token);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(userVO, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(userToken);

        return token;
    }

}
