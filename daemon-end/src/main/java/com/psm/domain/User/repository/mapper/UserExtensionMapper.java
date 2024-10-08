package com.psm.domain.User.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.psm.domain.User.entity.UserExtension.UserExtensionDAO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserExtensionMapper extends BaseMapper<UserExtensionDAO> {
}
