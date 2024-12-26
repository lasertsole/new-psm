package com.psm.trigger.http.Review;

import com.psm.domain.DependentDomain.Review.adaptor.ReviewAdaptor;
import com.psm.domain.DependentDomain.Review.entity.ReviewBO;
import com.psm.domain.DependentDomain.Review.entity.ReviewDTO;
import com.psm.domain.DependentDomain.Review.types.enums.TargetTypeEnum;
import com.psm.domain.IndependentDomain.User.user.adaptor.UserAdaptor;
import com.psm.types.common.DTO.ResponseDTO;
import com.psm.utils.page.PageBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        @RequestParam String targetId
    ) {
        try {
            PageBO pageBO = new PageBO(current, size);
            return ResponseDTO.ok(reviewAdaptor.getReviews(pageBO, targetType, Long.parseLong(targetId)));
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
