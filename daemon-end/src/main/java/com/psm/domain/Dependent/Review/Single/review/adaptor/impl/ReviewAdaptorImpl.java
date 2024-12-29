package com.psm.domain.Dependent.Review.Single.review.adaptor.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.domain.Dependent.Review.Single.review.adaptor.ReviewAdaptor;
import com.psm.domain.Dependent.Review.Single.review.entity.ReviewBO;
import com.psm.domain.Dependent.Review.Single.review.service.ReviewService;
import com.psm.domain.Dependent.Review.Single.review.types.enums.TargetTypeEnum;
import com.psm.utils.Valid.ValidUtil;
import com.psm.utils.page.PageBO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.InvalidParameterException;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Adaptor
public class ReviewAdaptorImpl implements ReviewAdaptor {
    @Autowired
    ValidUtil validUtil;

    @Autowired
    private ReviewService reviewService;

    @Override
    public void addReview(@Valid ReviewBO reviewBO) throws InstantiationException, IllegalAccessException {
        Long id = reviewBO.getId();
        Long srcUserId = reviewBO.getSrcUserId();
        TargetTypeEnum targetType = reviewBO.getTargetType();
        Long targetId = reviewBO.getTargetId();
        String content = reviewBO.getContent();
        String timestamp = reviewBO.getTimestamp();
        if(
            Objects.isNull(id)
            || Objects.isNull(srcUserId)
            || Objects.isNull(targetType)
            || Objects.isNull(targetId)
            || Objects.isNull(content)
            || Objects.isNull(timestamp)
        )
            throw new InvalidParameterException("Invalid parameter");

        reviewService.addReview(reviewBO);
    }

    @Override
    public Page<ReviewBO> getReviews(PageBO pageBO, TargetTypeEnum targetType, Long targetId) throws InstantiationException, IllegalAccessException {
        Integer current = pageBO.getCurrent();
        Integer size = pageBO.getSize();
        if(
            Objects.isNull(current)
            || Objects.isNull(size)
            || Objects.isNull(targetType)
            || Objects.isNull(targetId)
        )
            throw new InvalidParameterException("Invalid parameter");

        validUtil.validate(Map.of("current", current, "size", size, "targetType", targetType, "targetId", targetId), Page.class);

        return reviewService.getReviews(current, size, targetType, targetId);
    }

    @Override
    public Boolean deleteReview(@Valid ReviewBO reviewBO) throws InstantiationException, IllegalAccessException {
        Long id = reviewBO.getId();
        Long srcUserId = reviewBO.getSrcUserId();
        if (
            Objects.isNull(id)
            || Objects.isNull(srcUserId)
        )
            throw new InvalidParameterException("Invalid parameter");

        validUtil.validate(Map.of("id", id, "srcUserId", srcUserId), ReviewBO.class);

        return reviewService.deleteReview(id, srcUserId);
    }
}
