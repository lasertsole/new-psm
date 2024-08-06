package com.psm.annotation;

import com.psm.annotation.impl.JsonValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = JsonValidator.class)
@Documented
public @interface ValidJson {
    String message() default "Invalid JSON format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
