package com.psm.domain.Showcase;

import com.psm.annotation.ValidJson;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

public class ShowcaseDTO implements Serializable {
    private static final long serialVersionUID = 3962323961628208910L;

    @NotNull(message = "The title cannot be empty")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The title format is incorrect")
    @Size(max = 20, message = "The title length must not exceed 20 characters")
    private String title;

    @NotNull(message = "The content cannot be empty")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The content format is incorrect")
    @Size(max = 255, message = "The content length must not exceed 255 characters")
    private String content;

    @NotNull(message = "The cover cannot be empty")
    @Pattern(regexp = "^(?:\\/[\\w\\-]+)+\\/?$", message = "The cover format is incorrect")
    @Size(max = 255, message = "The cover length must not exceed 255 characters")
    private String cover;

    @NotNull(message = "The video cannot be empty")
    @Pattern(regexp = "^(?:\\/[\\w\\-]+)+\\/?$", message = "The video URL format is incorrect")
    @Size(max = 255, message = "The video length must not exceed 255 characters")
    private String video;

    @NotNull(message = "The category cannot be empty")
    @ValidJson
    @Size(max = 255, message = "The category length must not exceed 255 characters")
    private String category;
}
