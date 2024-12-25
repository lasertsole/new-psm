package com.psm.domain.DependentDomain.Review.repository.impl;

import com.psm.app.annotation.spring.Repository;
import com.psm.domain.DependentDomain.Review.entity.ReviewDO;
import com.psm.domain.DependentDomain.Review.repository.ReviewDB;
import com.psm.infrastructure.DB.ReviewMapper;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Repository
public class ReviewDBImpl extends BaseDBRepositoryImpl<ReviewMapper, ReviewDO> implements ReviewDB {
    @Autowired
    private ReviewMapper reviewMapper;

}
