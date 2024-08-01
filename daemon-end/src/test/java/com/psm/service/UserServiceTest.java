package com.psm.service;

import com.psm.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testGetCount(){//查询总记录数
        long count = userService.count();
        System.out.println("总记录数" + count);
    }

    @Test
    public void testInsertAll(){
        List<User> list = new ArrayList<>();
        for (int i = 1; i<=10; i++){
            User user = new User();
            user.setPhone("20"+i);
            user.setName("ybc"+i);
            user.setPassword("555");
            user.setEmail("20"+i+"@qq.com");
            list.add(user);
        }
        boolean result = userService.saveBatch(list);
        System.out.println("批量插入成功" + result);
    }
}
