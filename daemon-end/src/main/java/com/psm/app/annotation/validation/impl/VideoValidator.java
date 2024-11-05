package com.psm.app.annotation.validation.impl;

import com.psm.app.annotation.validation.ValidVideo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

public class VideoValidator implements ConstraintValidator<ValidVideo, MultipartFile> {
    private static final Tika tika = new Tika();

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }
        try {
            // 使用 FastJSON 解析 JSON 字符串
            String mimeType = tika.detect(file.getInputStream());
            return isVideo(mimeType);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isVideo(String mimeType) {
        return mimeType.startsWith("video/");
    }
}
