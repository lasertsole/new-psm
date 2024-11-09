package com.psm.infrastructure.DB;

import com.github.yulichang.base.MPJBaseMapper;
import com.psm.domain.User.user.entity.User.UserDAO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends MPJBaseMapper<UserDAO> {
    @Select("select * from tb_users where id = #{id}")
    UserDAO selectById(Long id);
}
