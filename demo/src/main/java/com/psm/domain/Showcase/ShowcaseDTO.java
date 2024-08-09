package com.psm.domain.Showcase;

import com.psm.annotation.ValidJson;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

public class ShowcaseDTO implements Serializable {
    private static final long serialVersionUID = 3962323961628208910L;

    @NotNull(message = "标题不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "标题格式错误")
    @Size(max = 20, message = "标题长度不能超过20")
    private String title;

    @NotNull(message = "内容不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "内容格式错误")
    @Size(max = 255, message = "内容长度不能超过255")
    private String content;

    @NotNull(message = "封面不能为空")
    @Pattern(regexp = "^(?:\\/[\\w\\-]+)+\\/?$", message = "封面地址格式错误")
    @Size(max = 255, message = "封面地址长度不能超过255")
    private String cover;

    @NotNull(message = "视频不能为空")
    @Pattern(regexp = "^(?:\\/[\\w\\-]+)+\\/?$", message = "视频地址格式错误")
    @Size(max = 255, message = "视频地址长度不能超过255")
    private String video;

    @NotNull(message = "分类不能为空")
    @ValidJson
    @Size(max = 255, message = "分类长度不能超过255")
    private String category;
}
