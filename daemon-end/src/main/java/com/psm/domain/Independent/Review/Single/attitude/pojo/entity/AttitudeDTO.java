package com.psm.domain.Independent.Review.Single.attitude.pojo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Independent.Review.Single.attitude.types.convertor.AttitudeConvertor;
import com.psm.domain.Independent.Review.Single.review.types.enums.TargetTypeEnum;
import com.psm.domain.Independent.Review.Single.attitude.types.enums.AttitudeTypeEnum;
import com.psm.types.common.POJO.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttitudeDTO implements DTO<AttitudeBO> {
    private String id;
    private String srcUserId;
    private TargetTypeEnum targetType;
    private String targetId;
    private AttitudeTypeEnum attitudeType;
    private String createTime;

    @Override
    public AttitudeBO toBO() {
        return AttitudeConvertor.INSTANCE.DTO2BO(this);
    }
}
