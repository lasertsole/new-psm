package com.psm.annotation.impl;

import com.alibaba.fastjson2.JSON;
import com.psm.annotation.ValidJson;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JsonValidator implements ConstraintValidator<ValidJson, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        try {
            // 使用 FastJSON 解析 JSON 字符串
            JSON.parse(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
