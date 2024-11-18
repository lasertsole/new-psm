package com.psm.domain.Model.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.utils.VO.DTO2VOable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import com.psm.domain.Model.model.types.convertor.Model3dConvertor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model3dDTO implements Serializable, DTO2VOable<Model3dVO> {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private MultipartFile coverFile;
    private String cover;
    private String entity;
    private String style;
    private String type;
    private Integer visible;
    private Long storage;
    private String createTime;

    @Override
    public Model3dVO toVO() {
        return Model3dConvertor.INSTANCE.DTO2VO(this);
    }
}
