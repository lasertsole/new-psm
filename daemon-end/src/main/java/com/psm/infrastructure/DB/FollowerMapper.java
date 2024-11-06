package com.psm.infrastructure.DB;

import com.github.yulichang.base.MPJBaseMapper;
import com.psm.domain.User.follower.entity.FollowerDAO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FollowerMapper extends MPJBaseMapper<FollowerDAO> {
}
