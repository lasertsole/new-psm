package com.psm.domain.Independent.Review.Single.review.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Independent.Review.Single.review.pojo.entity.ReviewBO;
import com.psm.domain.Independent.Review.Single.review.types.enums.TargetTypeEnum;

public interface ReviewService {
    /**
     * 添加评论
     *
     * @param reviewBO 评论实体
     * @return 是否删除成功
     */
    Boolean addReview(ReviewBO reviewBO);

    /**
     * 获取评论
     *
     * @param current 当前页码
     * @param size 每页项数
     * @param targetType 目标类型
     * @param targetId 目标id
     * @return 一页评论DO实体
     */
    Page<ReviewBO> getReviews(Integer current, Integer size, TargetTypeEnum targetType, Long targetId, Long attachUserId);

    /**
     * 删除评论
     *
     * @param id 评论id
     * @param srcUserId 评论用户id
     * @return 是否删除成功
     */
    Boolean deleteReview(Long id, Long srcUserId);
}
