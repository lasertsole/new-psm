package com.psm.infrastructure.DB;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.psm.domain.User.user.entity.UserExtension.UserExtensionDAO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserExtensionMapper extends BaseMapper<UserExtensionDAO> {
}
