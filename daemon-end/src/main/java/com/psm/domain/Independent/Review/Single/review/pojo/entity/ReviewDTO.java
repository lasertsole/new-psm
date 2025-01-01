package com.psm.domain.Independent.Review.Single.review.pojo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.domain.Independent.Review.Single.review.types.convertor.ReviewConvertor;
import com.psm.domain.Independent.Review.Single.review.types.enums.TargetTypeEnum;
import com.psm.types.common.POJO.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDTO implements DTO<ReviewBO> {
    private String id;
    private String srcUserId;
    private TargetTypeEnum targetType;
    private String targetId;
    private String attachId;
    private String replyId;
    private String content;
    private String createTime;
    private List<ReviewDTO> attaches;
    private List<ReviewDTO> replies;
    private Integer likeNum;
    private Integer dislikeNum;

    @Override
    public ReviewBO toBO() {
        return ReviewConvertor.INSTANCE.DTO2BO(this);
    }
}
