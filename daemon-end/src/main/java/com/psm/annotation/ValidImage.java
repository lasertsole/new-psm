package com.psm.annotation;

import com.psm.annotation.impl.ImageValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageValidator.class)
@Documented
public @interface ValidImage {
    String message() default "Invalid image format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
