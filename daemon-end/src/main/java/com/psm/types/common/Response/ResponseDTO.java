package com.psm.types.common.Response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.types.common.POJO.BO;
import com.psm.utils.page.PageDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO implements Serializable {
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

    protected static Object processData(Object data) {
        if(data instanceof BO) {
            data = ((BO) data).toDTO();
        }
        else if (data instanceof List) {
            data = ((List) data).stream().map(item -> {
                if(item instanceof BO) {
                    return ((BO) item).toDTO();
                }
                else{
                    return item;
                }
            }).toList();
        }
        else if (data instanceof Page<?>) {
            Page<?> page = (Page<?>) data;
            List records = page.getRecords().stream().map(item -> {
                if(item instanceof BO) {
                    return ((BO) item).toDTO();
                }
                else{
                    return item;
                }
            }).toList();

            PageDTO pageVO =  new PageDTO();
            BeanUtils.copyProperties(page, pageVO);
            pageVO.setRecords(records);

            data = pageVO;
        }

        return data;
    }

    public ResponseDTO(Object data) {
        data = processData(data);
        this.code = HttpStatus.OK.value();
        this.data = data;
        this.msg = "success";
    }

    public ResponseDTO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseDTO(HttpStatus httpStatus, String msg) {
        this(httpStatus.value(), msg);
    }

    public ResponseDTO(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        data = processData(data);
        this.data = data;
    }

    public ResponseDTO(HttpStatus httpStatus, String msg, Object data) { this(httpStatus.value(), msg, data);}

    public static ResponseDTO ok(Object data){ return new ResponseDTO(HttpStatus.OK, "success", data);}

    public static ResponseDTO ok(String msg){ return new ResponseDTO(HttpStatus.OK, msg);}

    public static ResponseDTO ok(String msg, Object data){
        return new ResponseDTO(HttpStatus.OK, msg, data);
    }
}
