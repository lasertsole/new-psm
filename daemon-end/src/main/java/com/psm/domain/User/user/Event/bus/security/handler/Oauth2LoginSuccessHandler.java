package com.psm.domain.User.user.Event.bus.security.handler;

import com.psm.domain.User.user.Event.bus.security.utils.JWT.JWTUtil;
import com.psm.domain.User.user.Event.bus.security.utils.Oauth2UserIdContextHolder;
import com.psm.infrastructure.Cache.RedisCache;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class Oauth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    // 前端地址
    @Value("${server.front-end-url.socket}")
    private String socket;

    // 前端登录页面
    @Value("${server.front-end-url.third-login-page}")
    private String thirdLoginPage;

    // 协议
    @Value("${server.protocol}")
    private String protocol;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken){
            // 从上下文获取用户id
            Long id = Oauth2UserIdContextHolder.getUserId();

            // 使用用户id生成JWT
            String jwt = jwtUtil.createJWT(id.toString());

            // 移除上下文,防止污染线程池的上下文
            Oauth2UserIdContextHolder.removeUserId();

            // 创建Cookie
            Cookie tokenCookie = new Cookie("token", jwt);
            tokenCookie.setPath("/");
            if("https".equals(protocol)){
                tokenCookie.setSecure(true); // 只在HTTPS环境下发送
            }

            tokenCookie.setMaxAge(60 * 60 * 24); // 设置过期时间为一天

            // 添加Cookie到响应
            response.addCookie(tokenCookie);

            // 重定向回前端
            response.sendRedirect(protocol+"://"+socket+thirdLoginPage);
        }
    }
}