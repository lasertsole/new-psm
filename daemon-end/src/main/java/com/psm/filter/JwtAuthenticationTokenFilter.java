package com.psm.filter;

import com.alibaba.fastjson2.JSON;
import com.psm.domain.Auth.LoginUser;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.utils.JWTUtil;
import com.psm.utils.RedisCache;
import com.psm.utils.ResponseWrapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter{
    @Autowired
    RedisCache redisCache;

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
            Claims claims = JWTUtil.parseJWT(token);
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

        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 使用 ResponseWrapper 包装 HttpServletResponse
        ResponseWrapper responseWrapper = new ResponseWrapper(response);

        //放行
        filterChain.doFilter(request, responseWrapper);

        // 获取原本要返回的 JSON 数据
        ResponseDTO responseDTO = responseWrapper.getContentAsObject(ResponseDTO.class);
        Map<String, Object> resultMap;

        if(responseDTO.getData()== null){
            resultMap = new HashMap<>();
        }
        else{
            resultMap = responseDTO.getData();
        }

        if(Objects.isNull(redisCache.getCacheObject(redisKey))){
            responseWrapper.setResponseContent(JSON.toJSONString(responseDTO));
            return;
        }

        //如果redis缓存的验证信息过期剩余时间小于一小时则重置过期时间
        if(redisCache.getExpire(redisKey) <= 3600){
            redisCache.setExpire(redisKey,1, TimeUnit.DAYS);
        }

        //如果JWT验证信息过期时间小于一小时则重置JWT
        if(redisCache.getExpire(redisKey, TimeUnit.SECONDS) <= 3600){
            String jwt = JWTUtil.createJWT(loginUser.getUser().getId().toString());
            redisCache.setCacheObject(redisKey,loginUser,1, TimeUnit.DAYS);

            resultMap.put("token",jwt);
            responseDTO.setData(resultMap);

            responseWrapper.setResponseContent(JSON.toJSONString(responseDTO));
        }
    }
}
