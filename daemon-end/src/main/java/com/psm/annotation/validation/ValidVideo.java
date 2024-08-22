package com.psm.annotation.validation;

import com.psm.annotation.validation.impl.VideoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VideoValidator.class)
@Documented
public @interface ValidVideo {
    String message() default "Invalid video format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
