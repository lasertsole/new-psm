package com.psm.domain.Model.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.infrastructure.annotation.validation.ValidFileSize;
import com.psm.infrastructure.annotation.validation.ValidImage;
import com.psm.infrastructure.annotation.validation.ValidJson;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModelDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4377624191532031433L;

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long id;

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long userId;

    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The title format is incorrect")
    @Size(max = 20, message = "The title length must not exceed 20 characters")
    private String title;

    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The content format is incorrect")
    @Size(max = 255, message = "The content length must not exceed 255 characters")
    private String content;

    @ValidImage
    @ValidFileSize(maxSize = 10 * 1024)//最大10MB
    private MultipartFile cover;

    @ValidJson
    @Size(max = 255, message = "The category length must not exceed 255 characters")
    private String category;

    @Min(value = 0, message = "The visable must be greater than or equal to 0")
    @Max(value = 2, message = "The visable must be less than or equal to 2")
    private Integer visible;
}
