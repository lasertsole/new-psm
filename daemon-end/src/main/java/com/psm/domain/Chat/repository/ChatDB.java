package com.psm.domain.Chat.repository;

import com.psm.domain.Chat.entity.ChatDO;

public interface ChatDB {
    // 插入聊天记录
    void insert(ChatDO chatDO);
}
