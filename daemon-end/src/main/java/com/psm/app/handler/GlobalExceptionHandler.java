package com.psm.app.handler;

import com.psm.types.common.DTO.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseDTO handleRuntimeException(RuntimeException ex) {
        return new ResponseDTO(HttpStatus.UNAUTHORIZED, "{\"message\": \"" + ex.getMessage() + "\"}");
    }

    // 可以添加更多的异常处理器
    @ExceptionHandler(Exception.class)
    public ResponseDTO handleException(Exception ex) {
        return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "{\"message\": \"" + ex.getMessage() + "\"}");
    }
}
