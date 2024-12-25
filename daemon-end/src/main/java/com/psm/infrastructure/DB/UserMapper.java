package com.psm.infrastructure.DB;

import com.psm.domain.IndependentDomain.User.user.entity.User.UserDO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseDBMapper<UserDO> {
    @Select("select * from tb_users where id = #{id}")
    UserDO selectById(Long id);
}
