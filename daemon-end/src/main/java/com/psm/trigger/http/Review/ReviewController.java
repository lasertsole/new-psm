package com.psm.trigger.http.Review;

import com.psm.domain.DependentDomain.Review.adaptor.ReviewAdaptor;
import com.psm.domain.DependentDomain.Review.entity.ReviewDTO;
import com.psm.types.common.DTO.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewAdaptor reviewAdaptor;

    @PostMapping
    public ResponseDTO addReview(ReviewDTO reviewDTO) {
        return null;
    }

    @DeleteMapping
    public ResponseDTO deleteReview(ReviewDTO reviewDTO) {
        return null;
    }
}
