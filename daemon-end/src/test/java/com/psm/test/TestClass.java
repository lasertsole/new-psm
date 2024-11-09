package com.psm.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.psm.domain.Chat.entity.ChatDAO;
import com.psm.domain.User.user.entity.User.UserDAO;
import com.psm.infrastructure.DB.ChatMapper;
import com.psm.infrastructure.DB.Model3dMapper;
import com.psm.infrastructure.DB.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class TestClass {
    @Autowired
    Model3dMapper modelMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ChatMapper chatMapper;

    @Test
    public void getChatById() {
        LambdaQueryWrapper<ChatDAO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatDAO::getId, 1L);

//        ChatDAO chatDAO = new ChatDAO(1L, 1L, 2L);
//        chatMapper.insert(chatDAO);
        ChatDAO chatDAO = chatMapper.selectOne(queryWrapper);
//        ChatDAO chatDAO = chatMapper.selectById(1L);
        log.info("chatDAO is {}", chatDAO);
    }

    @Test
    public void getUserById() {
        LambdaQueryWrapper<UserDAO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDAO::getId, 345342558316138496L);
        UserDAO userDAO = userMapper.selectOne(queryWrapper);
        log.info("userDAO is {}", userDAO);
    }
}
