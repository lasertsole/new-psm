package com.psm.infrastructure.DB;

import com.github.yulichang.base.MPJBaseMapper;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionDAO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserExtensionMapper extends MPJBaseMapper<UserExtensionDAO> {
}
