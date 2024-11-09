package com.psm.domain.Model.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.types.convertor.Model3dConvertor;
import com.psm.types.enums.VisibleEnum;
import com.psm.utils.VO.BO2VOable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model3dBO implements BO2VOable<Model3dVO>, Serializable {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String cover;
    private String entity;
    private VisibleEnum visible;
    private Long storage;

    private String style;//模型风格
    private String type;//模型类型
    private String createTime;
    private String modifyTime;

    @Override
    public Model3dVO toVO() {
        return Model3dConvertor.INSTANCE.BO2VO(this);
    }
}
