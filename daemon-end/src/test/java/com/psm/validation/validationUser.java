package com.psm.validation;

import com.psm.domain.User.user.entity.User.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import java.util.Map;

@Slf4j
@SpringBootTest
public class validationUser {
    @Autowired
    private Validator validator;

    @Test
    void validate() {
        // 创建UserDTO实例
        UserDTO userDTO = new UserDTO();

        // 使用DataBinder进行绑定和验证
        DataBinder dataBinder = new DataBinder(userDTO);
        dataBinder.setValidator(validator);

        // 绑定数据
        dataBinder.bind(new MutablePropertyValues(
                Map.of("email", "3132225629@qq.com", "phone", true)
        ));

        // 验证
        dataBinder.validate();

        // 获取验证结果
        BindingResult bindingResult = dataBinder.getBindingResult();

        if (bindingResult.hasErrors()) {
            for (org.springframework.validation.FieldError error : bindingResult.getFieldErrors()) {
                String field = error.getField();
                String message = error.getDefaultMessage();
                log.error("字段 {} 验证失败: {}", field, message);
            }
        } else {
            // 验证成功，继续处理
            log.info("用户信息验证成功！");
        }
    }
}
