package com.psm.domain.DependentDomain.Review.service.impl;

import com.psm.domain.DependentDomain.Review.repository.ReviewDB;
import com.psm.domain.DependentDomain.Review.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewDB reviewDB;

}
