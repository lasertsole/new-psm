package com.psm.service;

import com.psm.domain.User;
import com.psm.enums.SexEnum;
import com.psm.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserEnumsTest {
    @Autowired
    UserMapper userMapper;
    @Test
    public void testEnum(){//测试枚举类型
        User user = new User();
        user.setSex(SexEnum.MALE);
        user.setPhone("123");
        user.setEmail("123");
        user.setName("iii");
        user.setPassword("yyy");
        int result =  userMapper.insert(user);
        System.out.println(result);
    }
}
