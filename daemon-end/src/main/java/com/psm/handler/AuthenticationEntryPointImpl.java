package com.psm.handler;

import com.alibaba.fastjson2.JSON;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.utils.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ResponseDTO result = new ResponseDTO(HttpStatus.UNAUTHORIZED, "用户认证失败，请重新登录");
        String json = JSON.toJSONString(result);
        // 异常处理
        WebUtil.renderString(response, json);
    }
}
