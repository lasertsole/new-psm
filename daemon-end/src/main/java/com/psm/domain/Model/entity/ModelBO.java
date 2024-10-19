package com.psm.domain.Model.entity;

import com.psm.domain.Model.infrastructure.convertor.ModelConvertor;
import com.psm.domain.Model.valueObject.Category;
import com.psm.infrastructure.utils.VO.BO2VOable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelBO implements BO2VOable<ModelVO>, Serializable {
    @Serial
    private static final long serialVersionUID = -5438707322519502525L;

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String cover;
    private String entity;
    private Short visible = 0;
    private Long storage;

    private Category category;
    private String createTime;
    private String modifyTime;

    @Override
    public ModelVO toVO() {
        return ModelConvertor.INSTANCE.BO2VO(this);
    }
}
