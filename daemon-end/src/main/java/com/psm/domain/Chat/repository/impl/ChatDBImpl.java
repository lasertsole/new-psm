package com.psm.domain.Chat.repository.impl;

import com.psm.app.annotation.spring.Repository;
import com.psm.domain.Chat.entity.ChatDAO;
import com.psm.domain.Chat.repository.ChatDB;
import com.psm.infrastructure.DB.ChatMapper;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class ChatDBImpl extends BaseDBRepositoryImpl<ChatMapper, ChatDAO> implements ChatDB {
    @Autowired
    private ChatMapper chatMapper;

    public void insert(ChatDAO chatDAO) {
        save(chatDAO);
    }
}
