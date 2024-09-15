package com.psm.domain.User.infrastructure.handler;

import com.psm.domain.User.entity.LoginUser.LoginUser;
import com.psm.domain.User.infrastructure.utils.JWTUtil;
import com.psm.infrastructure.utils.Redis.RedisCache;
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
            String registerationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
            String id = ((OAuth2User) authentication.getPrincipal()).getAttribute("id").toString();

            String uniqueThirdId = registerationId + id;

            // 从redis中获取LoginUser
            LoginUser loginUser = redisCache.getCacheObject("third:"+uniqueThirdId);
            // 获取到LoginUser就立即删除redis中的数据
            redisCache.deleteObject("third:"+uniqueThirdId);

            // 获取用户id
            String userId = loginUser.getUser().getId().toString();

            // 使用用户id生成JWT
            String jwt = jwtUtil.createJWT(userId);

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