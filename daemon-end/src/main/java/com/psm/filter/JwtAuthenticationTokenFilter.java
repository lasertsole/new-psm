package com.psm.filter;

import com.alibaba.fastjson2.JSON;
import com.psm.domain.Auth.LoginUser;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.utils.JWTUtil;
import com.psm.utils.Redis.RedisCache;
import com.psm.utils.ResponseWrapper;
import io.jsonwebtoken.Claims;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
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

@Setter
@Component
@ConfigurationProperties(prefix = "jwt")//配置和jwt一样的过期时间
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter{
    @Autowired
    RedisCache redisCache;

    @Autowired
    JWTUtil jwtUtil;

    /**
     * jwt有效期
     */
    public Long expiration;

    /**
     * jwt刷新时间
     */
    public Long refreshExpiration;

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

        //存入SecurityContextHolder,并跳过验证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 使用 ResponseWrapper 包装 HttpServletResponse
        ResponseWrapper responseWrapper = new ResponseWrapper(response);

        //放行
        filterChain.doFilter(request, responseWrapper);

        // 获取原本要返回的 JSON 数据
        ResponseDTO responseDTO = responseWrapper.getContentAsObject(ResponseDTO.class);
        Map<String, Object> resultMap;

        if(Objects.isNull(responseDTO)){
            responseDTO = new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"Server error: ");
            return;
        }

        //如果原本要返回的 JSON 数据为空则创建一个空的 Map,否则获取原本要返回的 JSON 数据
        if(Objects.isNull(responseDTO.getData())){
            resultMap = new HashMap<>();
        }
        else{
            resultMap = responseDTO.getData();
        }

        //如果redis缓存的验证信息过期则直接返回
        if(Objects.isNull(redisCache.getCacheObject(redisKey))){
            //设置返回内容
            responseWrapper.setResponseContent(JSON.toJSONString(responseDTO));

            //发送内容到客户端
            responseWrapper.sendResponse();
            return;
        }

        //如果JWT验证信息过期时间小于一小时则重置JWT
        if(redisCache.getExpire(redisKey, TimeUnit.SECONDS) <= refreshExpiration/1000){
            String jwt = jwtUtil.createJWT(loginUser.getUser().getId().toString());
            redisCache.setCacheObject(redisKey,loginUser, Math.toIntExact(expiration / 1000 / 3600), TimeUnit.HOURS);

            resultMap.put("token",jwt);
            responseDTO.setData(resultMap);
        }

        //设置返回内容
        responseWrapper.setResponseContent(JSON.toJSONString(responseDTO));

        //发送内容到客户端
        responseWrapper.sendResponse();
    }
}
