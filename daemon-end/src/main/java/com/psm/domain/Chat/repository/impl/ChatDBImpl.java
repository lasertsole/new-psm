package com.psm.domain.Chat.repository.impl;

import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Chat.entity.ChatDO;
import com.psm.domain.Chat.repository.ChatDB;
import com.psm.infrastructure.DB.ChatMapper;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class ChatDBImpl extends BaseDBRepositoryImpl<ChatMapper, ChatDO> implements ChatDB {
    @Autowired
    private ChatMapper chatMapper;

    public void insert(ChatDO chatDO) {
        save(chatDO);
    }
}
