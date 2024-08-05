package com.psm.annotation;

import com.psm.annotation.validator.BooleanFieldValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BooleanFieldValidator.class)
public @interface BooleanField {
    String message() default "Sex field must be a boolean value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
