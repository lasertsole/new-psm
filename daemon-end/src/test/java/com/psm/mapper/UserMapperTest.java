package com.psm.mapper;

import com.psm.domain.User.UserDAO;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsert(){
        UserDAO user = new UserDAO();
        user.setName("张三");
        user.setPassword("123456");
        user.setPhone("12345678901");
        user.setEmail("12345678901@qq.com");
        user.setCreateTime("2021-01-01 00:00:00");
        user.setModifyTime("2021-01-01 00:00:00");
        int result = userMapper.insert(user);
        System.out.println("result" + result);
    }
    @Test
    public void testSelect(){
        System.out.println(userMapper.selectById(1L));
    }

    @Test
    public void testUpdate(){
        UserDAO user = new UserDAO();
        user.setId(1L);
        user.setName("张三");
        user.setPassword("123456");
        user.setPhone("12345678901");
        user.setEmail("12345678901@qq.com");
        user.setCreateTime("2021-01-01 00:00:00");
        user.setModifyTime("2021-01-01 00:00:00");
        int result= userMapper.updateById(user);
    }

    @Test
    public void testDelete(){
        int result = userMapper.deleteById(1L);
        System.out.println("result" + result);
    }
}
