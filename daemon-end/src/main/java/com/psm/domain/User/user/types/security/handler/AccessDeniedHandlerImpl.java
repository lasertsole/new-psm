package com.psm.domain.User.user.types.security.handler;

import com.alibaba.fastjson2.JSON;
import com.psm.types.utils.VO.ResponseVO;
import com.psm.infrastructure.utils.Web.WebUtil;
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
        ResponseVO result = new ResponseVO(HttpStatus.FORBIDDEN, "Your permissions are insufficient");
        String json = JSON.toJSONString(result);
        // 异常处理
        WebUtil.renderString(response, json);
    }
}
