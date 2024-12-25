package com.psm.infrastructure.DB;

import com.psm.domain.IndependentDomain.User.user.entity.OAuth2ThirdAccount.OAuth2ThirdAccountDO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OAuth2ThirdAccountMapper extends BaseDBMapper<OAuth2ThirdAccountDO> {
}
