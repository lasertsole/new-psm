package com.psm.app.annotation.validation.impl;

import com.psm.app.annotation.validation.MustNull;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class MustNullValidator implements ConstraintValidator<MustNull, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return Objects.isNull(value);
    }
}
