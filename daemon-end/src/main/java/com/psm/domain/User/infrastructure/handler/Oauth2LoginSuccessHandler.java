package com.psm.domain.User.infrastructure.handler;

import com.alibaba.fastjson2.JSON;
import com.psm.utils.DTO.ResponseDTO;
import com.psm.utils.JWT.JWTUtil;
import com.psm.utils.ResponseWrapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class Oauth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    // 前端地址
    @Value("${server.front-end-url.base}")
    private String frontEndBaseUrl;

    // 前端登录页面
    @Value("${server.front-end-url.third-login-page}")
    private String thirdLoginPage;

    @Value("${server.protocol}")
    private String protocol;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken){
            String registerationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
            String id = ((OAuth2User) authentication.getPrincipal()).getAttribute("id").toString();

            String uniqueThirdId = registerationId + id;

            String jwt = jwtUtil.createJWT(uniqueThirdId);

            Cookie tokenCookie = new Cookie("token", jwt);
            tokenCookie.setPath("/");
            if("https".equals(protocol)){
                tokenCookie.setSecure(true); // 只在HTTPS环境下发送
            }

            tokenCookie.setMaxAge(60 * 60 * 24); // 设置过期时间为一天

            // 添加Cookie到响应
            response.addCookie(tokenCookie);

            // 使用 ResponseWrapper 包装 HttpServletResponse
            response.sendRedirect(frontEndBaseUrl+thirdLoginPage);
        }
    }
}