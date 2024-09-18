package com.psm.infrastructure.utils.VO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ResponseVO implements Serializable {
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

    public ResponseVO(Object data) {
        this.code = HttpStatus.OK.value();
        this.data = data;
        this.msg = "success";
    }

    public ResponseVO(BO2VOable bo2Voable) {
        this(bo2Voable.toVO());
    }

    public ResponseVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseVO(HttpStatus httpStatus, String msg) {
        this(httpStatus.value(), msg);
    }

    public ResponseVO(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseVO(HttpStatus httpStatus, String msg, Object data) {
        this(httpStatus.value(), msg, data);
    }

    public ResponseVO(Integer code, String msg, BO2VOable bo2Voable){
        this(code, msg, bo2Voable.toVO());
    }

    public ResponseVO(HttpStatus httpStatus, String msg, BO2VOable bo2Voable){
        this(httpStatus.value(), msg, bo2Voable.toVO());
    }

    public static ResponseVO ok(String msg, Object data){
        return new ResponseVO(HttpStatus.OK, msg, data);
    }

    public static ResponseVO ok(Object data){
        return new ResponseVO(HttpStatus.OK, "success", data);
    }

    public static ResponseVO ok(String msg){
        return new ResponseVO(HttpStatus.OK, msg);
    }

    public static ResponseVO ok(BO2VOable bo2Voable){ return new ResponseVO(HttpStatus.OK, "success", bo2Voable);}
}
