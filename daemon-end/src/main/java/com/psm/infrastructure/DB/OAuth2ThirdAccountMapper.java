package com.psm.infrastructure.DB;

import com.psm.domain.User.user.entity.OAuth2ThirdAccount.OAuth2ThirdAccountDAO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OAuth2ThirdAccountMapper extends BaseDBMapper<OAuth2ThirdAccountDAO> {
}
