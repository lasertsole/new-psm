package com.psm.infrastructure.utils.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
public class ResponseDTO implements Serializable {
    private static final long serialVersionUID = -378144855071152805L;

    /**
     *状态码
     */
    private Integer code = null;

    /**
     *提示信息,如果有错误,前端可以获取该字段进行提示
     */
    private String msg = null;

    /**
     *查询到的结果数据
     */
    private Object data = null;

    public ResponseDTO(Object data) {
        this.code = HttpStatus.OK.value();
        this.data = data;
        this.msg = "success";
    }

    public ResponseDTO(HttpStatus httpStatus, String msg) {
        this.code = httpStatus.value();
        this.msg = msg;
    }

    public ResponseDTO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseDTO(HttpStatus httpStatus, String msg, Object data) {
        this.code = httpStatus.value();
        this.msg = msg;
        this.data = data;
    }

    public ResponseDTO(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResponseDTO ok(String msg, Object data){
        return new ResponseDTO(HttpStatus.OK, msg, data);
    }

    public static ResponseDTO ok(Object data){
        return new ResponseDTO(HttpStatus.OK, "success", data);
    }

    public static ResponseDTO ok(String msg){
        return new ResponseDTO(HttpStatus.OK, msg);
    }
}
