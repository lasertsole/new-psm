package com.psm.infrastructure.RepositoryImpl.Review.review;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Independent.Review.Single.review.pojo.entity.ReviewDO;
import com.psm.domain.Independent.Review.Single.review.types.enums.TargetTypeEnum;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepository;

public interface ReviewDB extends BaseDBRepository<ReviewDO> {
    /**
     * 根据评论对象类型和评论对象id获取评论列表
     *
     * @param current 当前页码
     * @param size 每页项数
     * @param targetType 目标类型
     * @param targetId 目标id
     * @return 一页评论DO实体
     */
    Page<ReviewDO> selectReviewsbyPage(Integer current, Integer size, TargetTypeEnum targetType, Long targetId, Long attachUserId);

    /**
     * 根据评论id删除评论
     *
     * @param id 评论id
     * @param srcUserId 评论用户id
     * @return 是否删除成功
     */
    Boolean removeReview(Long id, Long srcUserId);
}
