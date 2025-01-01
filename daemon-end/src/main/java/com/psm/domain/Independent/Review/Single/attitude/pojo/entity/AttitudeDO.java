package com.psm.domain.Independent.Review.Single.attitude.pojo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Independent.Review.Single.attitude.types.convertor.AttitudeConvertor;
import com.psm.domain.Independent.Review.Single.review.types.enums.TargetTypeEnum;
import com.psm.domain.Independent.Review.Single.attitude.types.enums.AttitudeTypeEnum;
import com.psm.types.common.POJO.DO;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AutoDefine
@NoArgsConstructor
@AllArgsConstructor
@Table(value="tb_attitudes", comment="态度表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttitudeDO implements DO<AttitudeBO, AttitudeDTO> {
    private Long id;
    private Long srcUserId;
    private TargetTypeEnum targetType;
    private Long targetId;
    private AttitudeTypeEnum attitudeType;
    private String createTime;

    @Override
    public AttitudeBO toBO() {
        return AttitudeConvertor.INSTANCE.DO2BO(this);
    }

    @Override
    public AttitudeDTO toDTO() {
        return AttitudeConvertor.INSTANCE.DO2DTO(this);
    }
}
