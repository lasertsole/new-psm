package com.psm.domain.User.user.event.bus.security.handler;

import com.psm.domain.User.user.event.bus.security.utils.Oauth2UserIdContextHolder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class Oauth2LoginErrorHander implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 清理ThreadLocal
        Oauth2UserIdContextHolder.removeUserId();

        // 返回错误
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("第三方登录失败: " + exception.getMessage());
    }
}
