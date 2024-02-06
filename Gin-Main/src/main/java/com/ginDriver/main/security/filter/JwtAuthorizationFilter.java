package com.ginDriver.main.security.filter;


import com.ginDriver.common.utils.JwtTokenUtils;
import com.ginDriver.core.domain.bo.UserBO;
import com.ginDriver.core.domain.po.User;
import com.ginDriver.main.cache.redis.TokenRedis;
import com.ginDriver.main.cache.redis.UserRedis;
import com.ginDriver.main.security.properties.SecurityProperties;
import com.ginDriver.main.service.UserService;
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

    private final UserService userService;

    private final TokenRedis tokenRedis;

    private final UserRedis userRedis;
    private final SecurityProperties properties;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, SecurityProperties properties, UserService userService, UserRedis userRedis, TokenRedis tokenRedis) {
        super(authenticationManager);
        this.userService = userService;
        this.tokenRedis = tokenRedis;
        this.userRedis = userRedis;
        this.properties = properties;
    }

    /**
     * 过滤具体逻辑
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        // 过滤白名单路由
        String uri = request.getRequestURI();
        for (String exclude : properties.getExcludes()) {
            if (uri.equals("/api" + exclude)){
                super.doFilterInternal(request, response, chain);
                return;
            }
        }

        // 拿出请求头的token
        String tokenHeader = request.getHeader(JwtTokenUtils.TOKEN_HEADER);

        // 如果请求头中没有Authorization信息则直接放行了
        if (tokenHeader == null || !tokenHeader.startsWith(JwtTokenUtils.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = JwtTokenUtils.getToken(tokenHeader);

        // token是否在黑名单中 或 已经退出登录的token
        boolean had = this.tokenRedis.hasBlankToken(token);
        if (had) {
            log.error("token在黑名单中 ==> token: {}", token);
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication;
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
        Long userId = Long.valueOf(JwtTokenUtils.getUserId(token));
        List<String> roleList = JwtTokenUtils.getRoleList(token);
        String sysRole = JwtTokenUtils.getSysRole(token);
        Map<String, String> roleMap = JwtTokenUtils.getRoleMap(token);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (userId != null) {
            authorities.add(new SimpleGrantedAuthority(sysRole));

            UserBO bo = userRedis.getUserBO(userId);
            if (bo == null) {
                User user = userService.getById(userId);
                bo = new UserBO();
                BeanUtils.copyProperties(user, bo);
                bo.setRoleList(roleList);
                bo.setRoleMap(roleMap);
                // 缓存登录用户信息
                userRedis.setUserBO(bo);
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(bo, null, authorities);
            authenticationToken.setDetails(roleMap);
            return authenticationToken;
        }
        return null;
    }

}
