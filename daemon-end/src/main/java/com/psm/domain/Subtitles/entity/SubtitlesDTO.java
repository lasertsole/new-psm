package com.psm.domain.Subtitles.entity;

import com.psm.annotation.validation.ValidFileSize;
import com.psm.annotation.validation.ValidImage;
import com.psm.annotation.validation.ValidJson;

import com.psm.annotation.validation.ValidVideo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubtitlesDTO implements Serializable {
    private static final long serialVersionUID = 2545599656348291740L;

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

    @ValidVideo
    @ValidFileSize(maxSize = 200 * 1024)//最大200MB
    private MultipartFile video;

    @ValidJson
    @Size(max = 255, message = "The category length must not exceed 255 characters")
    private String category;
}
