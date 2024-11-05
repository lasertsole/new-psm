package com.psm.infrastructure.DB;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.psm.domain.User.user.entity.OAuth2ThirdAccount.OAuth2ThirdAccountDAO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OAuth2ThirdAccountMapper extends BaseMapper<OAuth2ThirdAccountDAO> {
}
