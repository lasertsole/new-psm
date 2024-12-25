package com.psm.domain.DependentDomain.Review.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.DependentDomain.Review.types.convertor.ReviewConvertor;
import com.psm.domain.DependentDomain.Review.types.enums.TargetTypeEnum;
import com.psm.types.common.DTO.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDTO implements Serializable, DTO<ReviewBO> {
    private String id;
    private String srcUserId;
    private TargetTypeEnum targetType;
    private String targetId;
    private String timestamp;
    private String content;
    private String createTime;

    @Override
    public ReviewBO toBO() {
        return ReviewConvertor.INSTANCE.DTO2BO(this);
    }
}
