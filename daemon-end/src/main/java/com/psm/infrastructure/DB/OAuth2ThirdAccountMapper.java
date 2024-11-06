package com.psm.infrastructure.DB;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.psm.domain.User.user.entity.OAuth2ThirdAccount.OAuth2ThirdAccountDAO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OAuth2ThirdAccountMapper extends MPJBaseMapper<OAuth2ThirdAccountDAO> {
}
