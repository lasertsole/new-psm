package com.psm.infrastructure.DB;

import com.psm.domain.User.user.entity.User.UserDAO;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseDBMapper<UserDAO> {
    @Select("select * from tb_users where id = #{id}")
    UserDAO selectById(Long id);
}
