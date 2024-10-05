package com.psm.domain.Model.entity;

import com.psm.domain.Model.infrastructure.ModelConvertor;
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
    private static final long serialVersionUID = 6433981210940280248L;

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String cover;
    private String entity;

    private Category category;
    private String createTime;
    private String modifyTime;

    @Override
    public ModelVO toVO() {
        return ModelConvertor.INSTANCE.BO2VO(this);
    }
}
