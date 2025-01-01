package com.psm.domain.Independent.Review.Single.review.pojo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Independent.Review.Single.review.types.convertor.ReviewConvertor;
import com.psm.domain.Independent.Review.Single.review.types.enums.TargetTypeEnum;
import com.psm.types.common.POJO.DO;
import com.tangzc.autotable.annotation.Index;
import com.tangzc.mpe.annotation.InsertFillTime;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.ColumnId;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.bind.metadata.annotation.BindEntity;
import com.tangzc.mpe.bind.metadata.annotation.JoinCondition;
import com.tangzc.mpe.bind.metadata.annotation.JoinOrderBy;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AutoDefine
@NoArgsConstructor
@AllArgsConstructor
@Table(value="tb_reviews", comment="评论表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDO implements DO<ReviewBO, ReviewDTO> {
    @ColumnId(comment = "id主键")
    private Long id;

    @Column(comment = "评论用户id", notNull = true)
    private Long srcUserId;

    @Column(comment = "评论对象类型", notNull = true)
    private TargetTypeEnum targetType;

    @Column(comment = "评论对象id", notNull = true)
    private Long targetId;

    @Column(comment = "附着评论id")
    private Long attachId;

    @Column(comment = "回复评论id")
    private Long replyId;

    @Column(comment = "评论内容", length = 255, notNull = true)
    private String content;

    @Index(name = "tb_reviews_createTime_index")
    @InsertFillTime
    @Column(comment = "创建时间")
    private String createTime;

    @BindEntity(conditions = @JoinCondition(selfField = ReviewDODefine.id, joinField = ReviewDODefine.attachId), orderBy = @JoinOrderBy(field = ReviewDODefine.createTime, isAsc = false))
    private List<ReviewDO> attaches;

    @BindEntity(conditions = @JoinCondition(selfField = ReviewDODefine.id, joinField = ReviewDODefine.replyId), orderBy = @JoinOrderBy(field = ReviewDODefine.createTime, isAsc = false))
    private List<ReviewDO> replies;

    private Integer likeNum;
    private Integer dislikeNum;

    @Override
    public ReviewBO toBO() {
        return ReviewConvertor.INSTANCE.DO2BO(this);
    }

    @Override
    public ReviewDTO toDTO() {
        return ReviewConvertor.INSTANCE.DO2DTO(this);
    }
}
