package com.psm.types.handler;

import com.psm.types.utils.VO.ResponseVO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseVO handleRuntimeException(RuntimeException ex) {
        return new ResponseVO(HttpStatus.UNAUTHORIZED, "{\"message\": \"" + ex.getMessage() + "\"}");
    }

    // 可以添加更多的异常处理器
    @ExceptionHandler(Exception.class)
    public ResponseVO handleException(Exception ex) {
        return new ResponseVO(HttpStatus.INTERNAL_SERVER_ERROR, "{\"message\": \"" + ex.getMessage() + "\"}");
    }
}
