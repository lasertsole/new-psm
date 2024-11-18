package com.psm.domain.Model.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.app.annotation.validation.ValidFileSize;
import com.psm.app.annotation.validation.ValidImage;
import com.psm.domain.Model.model.types.convertor.Model3dConvertor;
import com.psm.types.enums.VisibleEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model3dBO implements Serializable {
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

    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The cover format is incorrect")
    @Size(max = 255, message = "The cover length must not exceed 255 characters")
    private String cover;

    @ValidImage
    @ValidFileSize(maxSize = 10 * 1024)//最大10MB
    private MultipartFile coverFile;

    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The entity format is incorrect")
    @Size(max = 255, message = "The entity length must not exceed 255 characters")
    private String entity;

    private VisibleEnum visible;

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long storage;

    @Size(max = 15, message = "The category length must not exceed 15 characters")
    private String style;//模型风格

    @Size(max = 15, message = "The category length must not exceed 15 characters")
    private String type;//模型类型

    private String createTime;

    private String modifyTime;

    public Model3dBO(Model3dDTO model3dDTO) {
        BeanUtils.copyProperties(Model3dConvertor.INSTANCE.DTO2BO(model3dDTO), this);
    }

    public static Model3dBO from(Model3dDTO model3dDTO) {
        return new Model3dBO(model3dDTO);
    }
}
