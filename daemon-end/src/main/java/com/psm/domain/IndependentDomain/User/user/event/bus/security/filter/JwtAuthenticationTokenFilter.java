package com.psm.domain.IndependentDomain.User.user.event.bus.security.filter;

import com.psm.domain.IndependentDomain.User.user.entity.LoginUser.LoginUser;
import com.psm.domain.IndependentDomain.User.user.event.bus.security.utils.JWT.JWTUtil;
import io.jsonwebtoken.Claims;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
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

@Slf4j
@Setter
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter{
    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 登录多级缓存
     */
    private final Cache loginCache;

    public JwtAuthenticationTokenFilter (CacheManager cacheManager) {
        this.loginCache = cacheManager.getCache("loginCache");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        };

        //解析token
        String userid;
        try {
            Claims claims = jwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        };

        // 如果多级缓存沒有命中，則从redis中获取用户信息,如何没有则抛出异常
        String cachekey = "login:" + userid;
        LoginUser loginUser = loginCache.get(cachekey, LoginUser.class);
        if(Objects.isNull(loginUser)){throw new RuntimeException("User not logged in");};

        // 如果命中就刷新缓存
        loginCache.put(cachekey, loginUser);

        //存入SecurityContextHolder,并跳过验证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //放行
        filterChain.doFilter(request, response);
    }
}
