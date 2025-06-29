package com.psm.infrastructure.DB;

import com.psm.domain.Independent.User.Single.user.pojo.entity.User.UserDO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseDBMapper<UserDO> {
    @Select("select * from tb_users where id = #{id}")
    UserDO selectById(Long id);
}
