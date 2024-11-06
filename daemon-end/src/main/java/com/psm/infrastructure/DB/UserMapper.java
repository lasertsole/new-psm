package com.psm.infrastructure.DB;

import com.github.yulichang.base.MPJBaseMapper;
import com.psm.domain.User.user.entity.User.UserDAO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends MPJBaseMapper<UserDAO> {
}
