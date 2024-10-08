package com.psm.validation;

import com.psm.domain.User.entity.User.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@Slf4j
@SpringBootTest
public class validationUser {
    @Autowired
    private Validator validator;

    @Test
    void validate() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("moye");
        userDTO.setPhone("1234");

        BindingResult bindingResult = new BeanPropertyBindingResult(userDTO, "email");

        validator.validate(userDTO, bindingResult);

        if (bindingResult.hasFieldErrors("email")){
            String errorMsg = bindingResult.getFieldError("email").getDefaultMessage();
            log.error(errorMsg);
        }

        if (bindingResult.hasFieldErrors("phone")){
            String errorMsg = bindingResult.getFieldError("phone").getDefaultMessage();
            log.error(errorMsg);
        }
        log.error(String.valueOf(bindingResult.hasFieldErrors("email")));
    }
}
