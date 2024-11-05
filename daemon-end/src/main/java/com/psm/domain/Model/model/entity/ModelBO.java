package com.psm.domain.Model.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Model.model.types.convertor.ModelConvertor;
import com.psm.domain.Model.model.valueObject.Category;
import com.psm.types.enums.VisibleEnum;
import com.psm.infrastructure.utils.VO.BO2VOable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModelBO implements BO2VOable<ModelVO>, Serializable {
    @Serial
    private static final long serialVersionUID = -8473171808333662751L;

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String cover;
    private String entity;
    private VisibleEnum visible;
    private Long storage;

    private Category category;
    private String createTime;
    private String modifyTime;

    @Override
    public ModelVO toVO() {
        return ModelConvertor.INSTANCE.BO2VO(this);
    }
}
