package com.psm.annotation.validator;

import com.psm.annotation.BooleanField;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class BooleanFieldValidator implements ConstraintValidator<BooleanField, Object> {

    @Override
    public void initialize(BooleanField constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            Field field = value.getClass().getDeclaredField("sex");
            field.setAccessible(true);
            Object fieldValue = field.get(value);

            return fieldValue instanceof Boolean;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}
