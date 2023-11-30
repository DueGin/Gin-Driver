package duegin.ginDriver.common.security.config;


import duegin.ginDriver.common.security.filter.JwtAuthorizationFilter;
import duegin.ginDriver.common.security.handle.MyAccessDeniedHandler;
import duegin.ginDriver.common.security.handle.MyLogoutSuccessHandler;
import duegin.ginDriver.common.security.handle.UnAuthenticationEntryPoint;
import duegin.ginDriver.common.security.properties.SecurityProperties;
import duegin.ginDriver.common.security.service.UserDetailsServiceImpl;
import duegin.ginDriver.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;


/**
 * @author DueGin
 */
@Configuration
@EnableWebSecurity
@AutoConfiguration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * security properties
     * 在配置文件中配置
     */
    private final SecurityProperties securityProperties;

    /***根据用户名找到用户*/
    @Resource
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Resource
    private UnAuthenticationEntryPoint unAuthenticationEntryPoint;

    @Resource
    private MyAccessDeniedHandler accessDeniedHandler;

    @Resource
    private MyLogoutSuccessHandler logoutSuccessHandler;

    @Resource
    private UserMapper userMapper;

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                // antMatchers (这里的路径)
                // permitAll 这里是允许所有人 访问
                .antMatchers(securityProperties.getExcludes()).permitAll()
                // 映射任何请求
                .anyRequest()

                // 指定任何经过身份验证的用户都允许使用URL
                .authenticated()

                // 指定支持基于表单的身份验证
                .and().formLogin().permitAll()

                // 允许配置异常处理。可以自己传值进去
                // 使用WebSecurityConfigurerAdapter时，将自动应用此WebSecurityConfigurerAdapter
                .and().exceptionHandling()

                // 设置要使用的AuthenticationEntryPoint
                // unAuthenticationEntryPoint 验证是否登录
                .authenticationEntryPoint(unAuthenticationEntryPoint)

                // 指定要使用的AccessDeniedHandler   处理拒绝访问失败。
                .accessDeniedHandler(accessDeniedHandler)

                /* 提供注销支持。 使用WebSecurityConfigurerAdapter时，将自动应用此WebSecurityConfigurerAdapter 。
                 *  默认设置是访问URL “ / logout”将使HTTP会话无效，清理配置的所有rememberMe()身份验证，清除SecurityContextHolder ，
                 *  然后重定向到“ / login？success”，从而注销用户
                 */
                // 退出登录成功处理器
                .and().logout().logoutSuccessHandler(logoutSuccessHandler)

                .and()
                // 处理HTTP请求的BASIC授权标头，然后将结果放入SecurityContextHolder
                .addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), userMapper), UsernamePasswordAuthenticationFilter.class)

                // 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }


    /**
     * 密码加密
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 因为使用了BCryptPasswordEncoder来进行密码的加密，所以身份验证的时候也的用他来判断哈
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }

    /**
     * 使用密码模式必须要的auth管理器
     * nested exception is java.lang.IllegalArgumentException: authenticationManager must be specified 问题解决
     */
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
