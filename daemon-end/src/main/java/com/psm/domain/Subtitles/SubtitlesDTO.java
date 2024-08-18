package com.psm.domain.Subtitles;

import com.psm.annotation.ValidImage;
import com.psm.annotation.ValidJson;

import com.psm.annotation.ValidVideo;
import jakarta.validation.constraints.NotNull;
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
    private static final long serialVersionUID = -5708327791818804872L;

    @NotNull(message = "The title cannot be empty")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The title format is incorrect")
    @Size(max = 20, message = "The title length must not exceed 20 characters")
    private String title;

    @NotNull(message = "The content cannot be empty")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The content format is incorrect")
    @Size(max = 255, message = "The content length must not exceed 255 characters")
    private String content;

    @NotNull(message = "The cover cannot be empty")
    @ValidImage
    private MultipartFile cover;

    @NotNull(message = "The video cannot be empty")
    @ValidVideo
    private MultipartFile video;

    @NotNull(message = "The category cannot be empty")
    @ValidJson
    @Size(max = 255, message = "The category length must not exceed 255 characters")
    private String category;
}
