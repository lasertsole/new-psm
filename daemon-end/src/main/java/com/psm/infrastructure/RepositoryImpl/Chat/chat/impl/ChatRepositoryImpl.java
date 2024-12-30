package com.psm.infrastructure.RepositoryImpl.Chat.chat.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Independent.Communication.Single.Chat.entity.ChatDO;
import com.psm.domain.Independent.Communication.Single.Chat.repository.ChatRepository;
import com.psm.infrastructure.RepositoryImpl.Chat.chat.ChatDB;
import com.psm.infrastructure.RepositoryImpl.User.user.UserDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Slf4j
@Repository
public class ChatRepositoryImpl implements ChatRepository {
    @Autowired
    private ChatDB chatDB;

    @Autowired
    private UserDB userDB;

    @Override
    public void DBInsert(ChatDO chatDO) {
        if(Objects.isNull(userDB.selectById(chatDO.getId()))) { return;} // 验证用户是否存在，如果不存在则不插入
        chatDB.insert(chatDO);
    }

    @Override
    public Page<ChatDO> DBPatchInitMessage(String timestamp, Long userId, int current, Integer size) {
        return chatDB.patchInitMessage(timestamp, userId, current, size);
    }
}
