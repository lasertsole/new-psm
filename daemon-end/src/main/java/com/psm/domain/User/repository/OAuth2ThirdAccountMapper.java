package com.psm.domain.User.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.psm.domain.User.entity.OAuth2User.OAuth2ThirdAccount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OAuth2ThirdAccountMapper extends BaseMapper<OAuth2ThirdAccount> {
}
