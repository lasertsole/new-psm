package com.psm.infrastructure.utils.VO;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.infrastructure.utils.MybatisPlus.Page.PageVO;
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

    protected static Object processData(Object data) {
        if(data instanceof BO2VOable) {
            data = ((BO2VOable) data).toVO();
        }
        else if (data instanceof List) {
            data = ((List) data).stream().map(item -> {
                if(item instanceof BO2VOable) {
                    return ((BO2VOable) item).toVO();
                }
                else{
                    return item;
                }
            }).toList();
        }
        else if (data instanceof Page<?>) {
            Page<?> page = (Page<?>) data;
            List records = page.getRecords().stream().map(item -> {
                if(item instanceof BO2VOable) {
                    return ((BO2VOable) item).toVO();
                }
                else{
                    return item;
                }
            }).toList();

            PageVO pageVO =  new PageVO();
            BeanUtils.copyProperties(page, pageVO);
            pageVO.setRecords(records);

            data = pageVO;
        }

        return data;
    }

    public ResponseVO(Object data) {
        data = processData(data);
        this.code = HttpStatus.OK.value();
        this.data = data;
        this.msg = "success";
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
        data = processData(data);
        this.data = data;
    }

    public ResponseVO(HttpStatus httpStatus, String msg, Object data) { this(httpStatus.value(), msg, data);}

    public static ResponseVO ok(Object data){ return new ResponseVO(HttpStatus.OK, "success", data);}

    public static ResponseVO ok(String msg){ return new ResponseVO(HttpStatus.OK, msg);}

    public static ResponseVO ok(String msg, Object data){
        return new ResponseVO(HttpStatus.OK, msg, data);
    }
}
