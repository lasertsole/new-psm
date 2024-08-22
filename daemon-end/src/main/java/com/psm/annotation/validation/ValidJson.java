package com.psm.annotation.validation;

import com.psm.annotation.validation.impl.JsonValidator;

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
