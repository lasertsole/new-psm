package com.psm.infrastructure.DB;

import com.psm.domain.Dependent.Review.Single.review.entity.ReviewDO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewMapper extends BaseDBMapper<ReviewDO> {
}
