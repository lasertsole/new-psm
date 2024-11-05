package com.psm.infrastructure.DB;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.psm.domain.User.follower.entity.FollowerDAO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FollowerMapper extends BaseMapper<FollowerDAO> {
}
