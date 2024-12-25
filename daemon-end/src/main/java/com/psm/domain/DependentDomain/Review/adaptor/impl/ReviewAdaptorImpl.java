package com.psm.domain.DependentDomain.Review.adaptor.impl;

import com.psm.app.annotation.spring.Adaptor;
import com.psm.domain.DependentDomain.Review.adaptor.ReviewAdaptor;
import com.psm.domain.DependentDomain.Review.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Adaptor
public class ReviewAdaptorImpl implements ReviewAdaptor {
    @Autowired
    private ReviewService reviewService;
}
