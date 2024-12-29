package com.psm.domain.Dependent.Review.Single.review.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Dependent.Review.Single.review.entity.ReviewBO;
import com.psm.domain.Dependent.Review.Single.review.repository.ReviewDB;
import com.psm.domain.Dependent.Review.Single.review.service.ReviewService;
import com.psm.domain.Dependent.Review.Single.review.types.enums.TargetTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewDB reviewDB;


    @Override
    public void addReview(ReviewBO reviewBO) {
        reviewDB.save(reviewBO.toDO());
    }

    @Override
    public Page<ReviewBO> getReviews(Integer current, Integer size, TargetTypeEnum targetType, Long targetId) {
        return ReviewBO.fromDOPage(reviewDB.selectReviewsbyPage(current, size, targetType, targetId));
    }

    @Override
    public Boolean deleteReview(Long id, Long srcUserId) {
        return reviewDB.removeReview(id, srcUserId);
    }
}
