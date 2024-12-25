package com.psm.domain.DependentDomain.Review.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.DependentDomain.Review.types.convertor.ReviewConvertor;
import com.psm.domain.DependentDomain.Review.types.enums.TargetTypeEnum;
import com.psm.types.common.BO.BO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewBO implements Serializable, BO<ReviewDTO, ReviewDO> {
    private Long id;
    private Long srcUserId;
    private TargetTypeEnum targetType;
    private Long targetId;
    private String timestamp;
    private String content;
    private String createTime;

    @Override
    public ReviewDTO toDTO() {
        return ReviewConvertor.INSTANCE.BO2DTO(this);
    }

    @Override
    public ReviewDO toDO() {
        return ReviewConvertor.INSTANCE.BO2DO(this);
    }
}
