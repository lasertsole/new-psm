package com.psm.domain.Chat.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.Serializable;

public class ChatDTO implements Serializable {
    private static final Validator validator = new LocalValidatorFactoryBean(); // 使用Spring的LocalValidatorFactoryBean

    private void validateField(String fieldName) {
        ConstraintViolation<ChatDTO> violation = validator.validateProperty(this, fieldName)
                .stream()
                .findFirst()
                .orElse(null);

        if (violation != null) {
            // 处理验证错误
            throw new IllegalArgumentException(violation.getMessage());
        }
    }
}
