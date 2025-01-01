package com.psm.domain.Independent.Review.Single.review.pojo.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.psm.app.annotation.validation.MustNull;
import com.psm.domain.Independent.Review.Single.review.types.convertor.ReviewConvertor;
import com.psm.domain.Independent.Review.Single.review.types.enums.TargetTypeEnum;
import com.psm.types.common.POJO.BO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewBO implements BO<ReviewDTO, ReviewDO> {

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long id;

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long srcUserId;

    private TargetTypeEnum targetType;

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long targetId;

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long attachId;

    @Min(value = 1, message = "The id must be greater than or equal to 1")
    private Long replyId;

    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$", message = "The cover format is incorrect")
    @Size(max = 255, message = "The cover length must not exceed 255 characters")
    private String content;

    @MustNull
    private String createTime;

    @MustNull
    private List<ReviewBO> attaches;

    @MustNull
    private List<ReviewBO> replies;

    @MustNull
    private Integer likeNum;

    @MustNull
    private Integer dislikeNum;

    @Override
    public ReviewDTO toDTO() {
        return ReviewConvertor.INSTANCE.BO2DTO(this);
    }

    @Override
    public ReviewDO toDO() {
        return ReviewConvertor.INSTANCE.BO2DO(this);
    }

    public static Page<ReviewBO> fromDOPage(Page<ReviewDO> reviewDOPage) {
        Page<ReviewBO> reviewBOPage = new Page<>();
        BeanUtils.copyProperties(reviewDOPage, reviewBOPage);
        reviewBOPage.setRecords(reviewDOPage.getRecords().stream().map(ReviewConvertor.INSTANCE::DO2BO).toList());
        return reviewBOPage;
    }
}
