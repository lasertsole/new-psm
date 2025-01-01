package com.psm.infrastructure.RepositoryImpl.Review.review.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Independent.Review.Single.review.pojo.entity.ReviewDO;
import com.psm.domain.Independent.Review.Single.review.types.enums.TargetTypeEnum;
import com.psm.infrastructure.DB.ReviewMapper;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepositoryImpl;
import com.psm.infrastructure.RepositoryImpl.Review.review.ReviewDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Slf4j
@Repository
public class ReviewDBImpl extends BaseDBRepositoryImpl<ReviewMapper, ReviewDO> implements ReviewDB {
    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public Page<ReviewDO> selectReviewsbyPage(Integer current, Integer size, TargetTypeEnum targetType, Long targetId, Long attachUserId) {
        LambdaQueryWrapper<ReviewDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
            .eq(ReviewDO::getTargetType, targetType)
            .and(w->w.eq(ReviewDO::getTargetId, targetId))
            .orderByDesc(ReviewDO::getCreateTime);

        if (Objects.nonNull(attachUserId)) {
            wrapper.eq(ReviewDO::getAttachId, attachUserId);
        } else {
            wrapper.isNull(ReviewDO::getAttachId);
        }

        return reviewMapper.selectPage(new Page<>(current, size), wrapper);
    }

    @Override
    public Boolean removeReview(Long id, Long srcUserId) {
        LambdaQueryWrapper<ReviewDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
            .eq(ReviewDO::getId, id)
            .and(w->w.eq(ReviewDO::getSrcUserId, srcUserId));

        if (reviewMapper.delete(wrapper)<= 0) {
            return false;
        };
        return true;
    }
}
