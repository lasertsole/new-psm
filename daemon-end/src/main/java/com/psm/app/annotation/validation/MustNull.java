package com.psm.app.annotation.validation;

import com.psm.app.annotation.validation.impl.MustNullValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MustNullValidator.class)
@Documented
public @interface MustNull {
    String message() default "this value must be empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
