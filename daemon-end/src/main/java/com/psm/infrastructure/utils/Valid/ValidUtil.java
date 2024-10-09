package com.psm.infrastructure.utils.Valid;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import java.security.InvalidParameterException;
import java.util.Map;

@Component
public class ValidUtil {
    @Autowired
    private Validator validator;

    public <T> void validate(Map map, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        // 使用DataBinder进行绑定和验证
        DataBinder dataBinder = new DataBinder(clazz.newInstance());
        dataBinder.setValidator(validator);

        // 绑定数据
        dataBinder.bind(new MutablePropertyValues(map));

        // 验证
        dataBinder.validate();

        // 获取验证结果
        BindingResult bindingResult = dataBinder.getBindingResult();

        if (bindingResult.hasErrors()) {
            StringBuilder errLogs = new StringBuilder();
            for (org.springframework.validation.FieldError error : bindingResult.getFieldErrors()) {
                String field = error.getField();
                String message = error.getDefaultMessage();
                errLogs.append("字段 ").append(field).append(" 验证失败: ").append(message).append("   ");
            }
            throw new InvalidParameterException(errLogs.toString());
        }
    }
}
