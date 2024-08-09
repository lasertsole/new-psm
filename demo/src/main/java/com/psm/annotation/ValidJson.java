package com.psm.annotation;

import com.psm.annotation.impl.JsonValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = JsonValidator.class)
@Documented
public @interface ValidJson {
    String message() default "Invalid JSON format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
