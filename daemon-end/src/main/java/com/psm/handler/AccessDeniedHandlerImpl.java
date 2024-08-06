package com.psm.handler;

import com.alibaba.fastjson2.JSON;
import com.psm.domain.UtilsDom.ResponseDTO;
import com.psm.utils.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseDTO result = new ResponseDTO(HttpStatus.FORBIDDEN, "你的权限不足");
        String json = JSON.toJSONString(result);
        // 异常处理
        WebUtil.renderString(response, json);
    }
}
