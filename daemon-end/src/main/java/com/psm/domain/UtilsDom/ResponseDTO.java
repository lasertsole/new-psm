package com.psm.domain.UtilsDom;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@NoArgsConstructor
public class ResponseDTO {
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
    private Map<String, Object> data;

    public ResponseDTO(Map<String, Object> data) {
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

    public ResponseDTO(HttpStatus httpStatus, String msg, Map<String, Object> data) {
        this.code = httpStatus.value();
        this.msg = msg;
        this.data = data;
    }

    public ResponseDTO(Integer code, String msg, Map<String, Object> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
