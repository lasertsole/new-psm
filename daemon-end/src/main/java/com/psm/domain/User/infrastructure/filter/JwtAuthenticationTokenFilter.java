package com.psm.domain.User.infrastructure.filter;

import com.psm.domain.User.entity.LoginUser.LoginUser;
import com.psm.domain.User.infrastructure.utils.JWTUtil;
import com.psm.infrastructure.utils.Redis.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Setter
@Component
@ConfigurationProperties(prefix = "spring.security.jwt")//配置和jwt一样的过期时间
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter{
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * jwt有效期
     */
    private Long expiration;

    /**
     * jwt刷新时间
     */
    private Long refreshExpiration;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        String userid;
        try {
            Claims claims = jwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }

        //从redis中获取用户信息
        String redisKey = "login:" + userid;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("User not logged in");
        }

        //如果JWT验证信息过期时间小于一小时则重置JWT
        if(redisCache.getExpire(redisKey, TimeUnit.SECONDS) <= refreshExpiration/1000){
            String jwt = jwtUtil.createJWT(loginUser.getUser().getId().toString());
            redisCache.setCacheObject(redisKey,loginUser, Math.toIntExact(expiration / 1000 / 3600), TimeUnit.HOURS);

            //重置的token通过返回头的方式通知客户端
            response.setHeader("token",jwt);
        }

        //存入SecurityContextHolder,并跳过验证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //放行
        filterChain.doFilter(request, response);
    }
}
