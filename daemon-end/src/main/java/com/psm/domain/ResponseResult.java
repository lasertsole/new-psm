package com.psm.domain;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseResult<T> {
    /**
     *状态码
     */
    private Integer code;

    /**
     *提示信息,如果有错误,前端可以获取该字段进行提示
     */
    private String msg;

    /**
     *查询到的结果数据
     */
    private T data;

    public ResponseResult(T data) {
        this.code = HttpStatus.OK.value();
        this.data = data;
        this.msg = "success";
    }

    public ResponseResult(HttpStatus httpStatus, String msg) {
        this.code = httpStatus.value();
        this.msg = msg;
    }

    public ResponseResult(HttpStatus httpStatus, String msg, T data) {
        this.code = httpStatus.value();
        this.msg = msg;
        this.data = data;
    }
}
