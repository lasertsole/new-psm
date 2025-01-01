package com.psm.infrastructure.DB;

import com.psm.domain.Independent.Review.Single.review.pojo.entity.ReviewDO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewMapper extends BaseDBMapper<ReviewDO> {
}
