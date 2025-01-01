package com.psm.domain.Independent.Review.Single.review.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Independent.Review.Single.review.pojo.entity.ReviewDO;
import com.psm.domain.Independent.Review.Single.review.types.enums.TargetTypeEnum;

public interface ReviewRepository {
    Boolean DBAddReview(ReviewDO reviewDO);

    Page<ReviewDO> DBSelectReviews(Integer current, Integer size, TargetTypeEnum targetType, Long targetId, Long attachUserId);

    Boolean DBRemoveReview(Long id, Long srcUserId);
}
