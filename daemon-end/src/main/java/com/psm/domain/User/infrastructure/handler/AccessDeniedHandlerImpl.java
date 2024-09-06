package com.psm.domain.User.infrastructure.handler;

import com.alibaba.fastjson2.JSON;
import com.psm.infrastructure.utils.DTO.ResponseDTO;
import com.psm.infrastructure.utils.WebUtil;
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
