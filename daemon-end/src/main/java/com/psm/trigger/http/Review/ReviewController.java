package com.psm.trigger.http.Review;

import com.psm.domain.Independent.Review.Single.review.adaptor.ReviewAdaptor;
import com.psm.domain.Independent.Review.Single.review.pojo.entity.ReviewBO;
import com.psm.domain.Independent.Review.Single.review.pojo.entity.ReviewDTO;
import com.psm.domain.Independent.Review.Single.review.types.enums.TargetTypeEnum;
import com.psm.domain.Independent.User.Single.user.adaptor.UserAdaptor;
import com.psm.types.common.Response.ResponseDTO;
import com.psm.utils.page.PageBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private UserAdaptor userAdaptor;

    @Autowired
    private ReviewAdaptor reviewAdaptor;

    @PostMapping
    public ResponseDTO addReview(ReviewDTO reviewDTO) {
        try {
            reviewAdaptor.addReview(reviewDTO.toBO());
            return ResponseDTO.ok("SUCCESS");
        } catch (InstantiationException | IllegalAccessException e) {
            return ResponseDTO.ok("Invalid parameter");
        } catch (Exception e) {
            return ResponseDTO.ok("INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    };

    @GetMapping
    public ResponseDTO getReviews(
        @RequestParam Integer current,
        @RequestParam Integer size,
        @RequestParam TargetTypeEnum targetType,
        @RequestParam String targetId,
        @RequestParam(required = false) Optional<String> attachUserId
    ) {
        try {
            PageBO pageBO = new PageBO(current, size);
            return ResponseDTO.ok(reviewAdaptor.getReviews(pageBO, targetType, Long.parseLong(targetId), attachUserId.map(Long::parseLong).orElse(null)));
        } catch (InstantiationException | IllegalAccessException e) {
            return ResponseDTO.ok("Invalid parameter");
        } catch (Exception e) {
            return ResponseDTO.ok("INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }

    @DeleteMapping
    public ResponseDTO deleteReview(String id) {
        try {
            ReviewBO reviewBO = new ReviewBO();
            reviewBO.setId(Long.parseLong(id));
            reviewBO.setSrcUserId(userAdaptor.getAuthorizedUserId());

            return ResponseDTO.ok(reviewAdaptor.deleteReview(reviewBO));
        } catch (InstantiationException | IllegalAccessException e) {
            return ResponseDTO.ok("Invalid parameter");
        } catch (Exception e) {
            return ResponseDTO.ok("INTERNAL_SERVER_ERROR:" + e.getCause());
        }
    }
}
