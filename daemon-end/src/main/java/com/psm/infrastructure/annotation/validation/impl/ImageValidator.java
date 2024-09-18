package com.psm.infrastructure.annotation.validation.impl;

import com.psm.infrastructure.annotation.validation.ValidImage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

public class ImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {
    private static final Tika tika = new Tika();
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }
        try {
            // 使用 FastJSON 解析 JSON 字符串
            String mimeType = tika.detect(file.getInputStream());
            return isImage(mimeType);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isImage(String mimeType) {
        return mimeType.startsWith("image/");
    }
}
