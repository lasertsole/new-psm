package com.psm.infrastructure.RepositoryImpl.Review.review.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Dependent.Review.Single.review.entity.ReviewDO;
import com.psm.domain.Dependent.Review.Single.review.repository.ReviewRepository;
import com.psm.domain.Dependent.Review.Single.review.types.enums.TargetTypeEnum;
import com.psm.infrastructure.RepositoryImpl.Review.review.ReviewDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Repository
public class ReviewRepositoryImpl implements ReviewRepository {
    @Autowired
    private ReviewDB reviewDB;

    @Override
    public Boolean DBAddReview(ReviewDO reviewDO) {
        return reviewDB.save(reviewDO);
    }

    @Override
    public Page<ReviewDO> DBSelectReviews(Integer current, Integer size, TargetTypeEnum targetType, Long targetId, Long attachUserId) {
        return reviewDB.selectReviewsbyPage(current, size, targetType, targetId, attachUserId);
    }

    @Override
    public Boolean DBRemoveReview(Long id, Long srcUserId) {
        return reviewDB.removeReview(id, srcUserId);
    }
}
