package com.psm.config;

import com.psm.domain.UtilsDom.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class BindExceptionHandler {
    @ExceptionHandler(BindException.class)
    public ResponseEntity handle(BindException e){
        List<FieldError> fieldErrors = e.getFieldErrors();
        Map errors = new LinkedHashMap<>();
        for (FieldError error : fieldErrors){
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(new ResponseDTO(HttpStatus.BAD_REQUEST, "参数校验错误", errors),HttpStatus.BAD_REQUEST);
    }
}
