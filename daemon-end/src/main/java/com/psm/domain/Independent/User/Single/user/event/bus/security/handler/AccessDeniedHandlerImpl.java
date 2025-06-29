package com.psm.domain.Independent.User.Single.user.event.bus.security.handler;

import com.alibaba.fastjson2.JSON;
import com.psm.domain.Independent.User.Single.user.event.bus.security.utils.WebUtil;
import com.psm.types.common.Response.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseDTO result = new ResponseDTO(HttpStatus.FORBIDDEN, "Your permissions are insufficient");
        String json = JSON.toJSONString(result);
        // 异常处理
        WebUtil.renderString(response, json);
    }
}
