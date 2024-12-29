package com.psm.domain.Dependent.Review.Single.review.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Dependent.Review.Single.review.types.convertor.ReviewConvertor;
import com.psm.domain.Dependent.Review.Single.review.types.enums.TargetTypeEnum;
import com.psm.types.common.BO.BO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

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

    public static Page<ReviewBO> fromDOPage(Page<ReviewDO> reviewDOPage) {
        Page<ReviewBO> reviewBOPage = new Page<>();
        BeanUtils.copyProperties(reviewDOPage, reviewBOPage);
        reviewBOPage.setRecords(reviewDOPage.getRecords().stream().map(ReviewConvertor.INSTANCE::DO2BO).toList());
        return reviewBOPage;
    }
}
