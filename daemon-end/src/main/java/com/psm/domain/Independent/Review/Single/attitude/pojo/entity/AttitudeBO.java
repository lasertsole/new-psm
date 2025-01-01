package com.psm.domain.Independent.Review.Single.attitude.pojo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.app.annotation.validation.MustNull;
import com.psm.domain.Independent.Review.Single.attitude.types.convertor.AttitudeConvertor;
import com.psm.domain.Independent.Review.Single.review.types.enums.TargetTypeEnum;
import com.psm.domain.Independent.Review.Single.attitude.types.enums.AttitudeTypeEnum;
import com.psm.types.common.POJO.BO;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttitudeBO implements BO<AttitudeDTO, AttitudeDO> {
    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long id;

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long srcUserId;

    private TargetTypeEnum targetType;

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long targetId;

    private AttitudeTypeEnum attitudeType;

    @MustNull
    private String createTime;

    @Override
    public AttitudeDTO toDTO() {
        return AttitudeConvertor.INSTANCE.BO2DTO(this);
    }

    @Override
    public AttitudeDO toDO() {
        return AttitudeConvertor.INSTANCE.BO2DO(this);
    }
}
