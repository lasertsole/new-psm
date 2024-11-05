package com.psm.types.handler;

import com.psm.types.utils.VO.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class BindExceptionHandler {
    @ExceptionHandler(BindException.class)
    public ResponseEntity handle(BindException e){
        List<FieldError> fieldErrors = e.getFieldErrors();
        Map errors = new LinkedHashMap<>();
        for (FieldError error : fieldErrors){
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(new ResponseVO(HttpStatus.BAD_REQUEST, "parameters is incorrect", errors),HttpStatus.BAD_REQUEST);
    }
}
