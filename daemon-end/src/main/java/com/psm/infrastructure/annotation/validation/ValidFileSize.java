package com.psm.infrastructure.annotation.validation;


import com.psm.infrastructure.annotation.validation.impl.FileSizeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileSizeValidator.class)
@Documented
public @interface ValidFileSize {
    Class<?>[] groups() default {};
    String message() default "Invalid file size";
    Class<? extends Payload>[] payload() default {};

    // 添加 maxSize 属性，并指定默认值
    long maxSize() default -1L; // 使用负数表示未指定，默认值

    // 添加 minSize 属性，并指定默认值
    long minSize() default -1L; // 使用负数表示未指定，默认值
}
