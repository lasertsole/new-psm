package com.psm.infrastructure.DB;

import com.psm.domain.User.follower.entity.FollowerDAO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FollowerMapper extends BaseDBMapper<FollowerDAO> {
}
