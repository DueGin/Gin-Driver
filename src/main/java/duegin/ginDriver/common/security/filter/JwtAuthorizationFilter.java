package duegin.ginDriver.common.security.filter;


import duegin.ginDriver.common.utils.JwtTokenUtils;
import duegin.ginDriver.domain.model.User;
import duegin.ginDriver.domain.vo.UserVO;
import duegin.ginDriver.mapper.UserMapper;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 处理HTTP请求的BASIC授权标头，然后将结果放入SecurityContextHolder
 *
 * @author crush
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private final UserMapper userMapper;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserMapper userMapper) {
        super(authenticationManager);
        this.userMapper = userMapper;
    }

    /**
     * 过滤具体逻辑
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        // 拿出请求头的token
        String tokenHeader = request.getHeader(JwtTokenUtils.TOKEN_HEADER);

        // 如果请求头中没有Authorization信息则直接放行了
        if (tokenHeader == null || !tokenHeader.startsWith(JwtTokenUtils.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = JwtTokenUtils.getToken(tokenHeader);

        UsernamePasswordAuthenticationToken authentication = null;
        try {
            authentication = getAuthentication(token);
        } catch (ExpiredJwtException e) {
            log.info("token过期==>{}, msg==>{}", token, e.getMessage());
            chain.doFilter(request, response);
            return;
        }


        // 如果请求头中有token，则进行解析，并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        super.doFilterInternal(request, response, chain);
    }

    /**
     * 这里从token中获取用户信息并新建一个token
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        // 从jwt token中拿出username、角色，然后解析出权限
        String username = JwtTokenUtils.getUsername(token);
        List<String> roles = JwtTokenUtils.getRoles(token);
        Map<String, String> groupRoles = JwtTokenUtils.getGroupRoles(token);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (username != null) {
            // token有用户信息，token未过期
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
            // todo 缓存登录用户信息
            User user = userMapper.selectByUsername(username);
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userVO, null, authorities);
            authenticationToken.setDetails(groupRoles);
            return authenticationToken;
        }
        return null;
    }

}
