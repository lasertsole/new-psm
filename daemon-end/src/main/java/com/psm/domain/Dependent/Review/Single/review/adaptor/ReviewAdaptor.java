package com.psm.domain.Dependent.Review.Single.review.adaptor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Dependent.Review.Single.review.entity.ReviewBO;
import com.psm.domain.Dependent.Review.Single.review.types.enums.TargetTypeEnum;
import com.psm.utils.page.PageBO;

public interface ReviewAdaptor {
    /**
     * 添加评论
     *
     * @param reviewBO 评论BO实体
     */
    void addReview(ReviewBO reviewBO) throws InstantiationException, IllegalAccessException;

    /**
     * 获取评论
     *
     * @param pageBO 页参数
     * @param targetType 目标类型
     * @param targetId 目标id
     * @return 一页评论BO实体
     */
    Page<ReviewBO> getReviews(PageBO pageBO, TargetTypeEnum targetType, Long targetId) throws InstantiationException, IllegalAccessException;

    /**
     * 删除评论
     *
     * @param reviewBO 评论BO实体,包含评论id，评论来源用户id
     * @return 是否删除成功
     */
    Boolean deleteReview(ReviewBO reviewBO) throws InstantiationException, IllegalAccessException;
}
