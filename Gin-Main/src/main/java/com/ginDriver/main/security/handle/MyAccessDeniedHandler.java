package com.ginDriver.main.security.handle;


import com.ginDriver.common.utils.ResponseUtils;
import com.ginDriver.main.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限不足回调
 * @author DueGin
 */
@Slf4j
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        Authentication authentication = SecurityUtils.getAuthentication();
        log.info(String.valueOf(authentication));
        log.info("权限不足, auth: {}, AccessDeniedException: {}", authentication, e);
        ResponseUtils.result(response,HttpServletResponse.SC_FORBIDDEN,"不好意思，你的权限不足！");
    }
}