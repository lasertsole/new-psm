package com.psm.app.annotation.validation.impl;

import com.psm.app.annotation.validation.ValidFileSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileSizeValidator implements ConstraintValidator<ValidFileSize, MultipartFile>{

    private static final long DEFAULT_MAX_SIZE = 2048000L; // 默认最大文件大小 200MB (200 * 1024 KB)
    private long maxSizeInBytes; // 最大文件大小，单位字节

    private static final long DEFAULT_MIN_SIZE = 0L; // 默认最大文件大小 0MB
    private long minSizeInBytes; // 最大文件大小，单位字节

    @Override
    public void initialize(ValidFileSize constraintAnnotation) {
        // 从注解中获取最大文件大小，如果没有指定则使用默认值
        this.maxSizeInBytes = constraintAnnotation.maxSize() == -1L ? DEFAULT_MAX_SIZE * 1024 : constraintAnnotation.maxSize() * 1024;

        // 从注解中获取最小文件大小，如果没有指定则使用默认值
        this.minSizeInBytes = constraintAnnotation.minSize() == -1L ? DEFAULT_MIN_SIZE * 1024 : constraintAnnotation.minSize() * 1024;
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        try {
            if (file == null || file.isEmpty()) {
                return true;
            }

            return file.getSize() <= maxSizeInBytes && file.getSize() >= minSizeInBytes;
        }catch (Exception e){
            return false;
        }
    }
}
