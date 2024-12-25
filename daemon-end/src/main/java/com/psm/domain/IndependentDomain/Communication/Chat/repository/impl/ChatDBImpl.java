package com.psm.domain.IndependentDomain.Communication.Chat.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.app.annotation.spring.Repository;
import com.psm.domain.IndependentDomain.Communication.Chat.entity.ChatDO;
import com.psm.domain.IndependentDomain.Communication.Chat.repository.ChatDB;
import com.psm.infrastructure.DB.ChatMapper;
import com.psm.infrastructure.DB.cacheEnhance.BaseDBRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Repository
public class ChatDBImpl extends BaseDBRepositoryImpl<ChatMapper, ChatDO> implements ChatDB {
    @Autowired
    private ChatMapper chatMapper;

    @Override
    public void insert(ChatDO chatDO) {
        save(chatDO);
    }

    @Override
    public Page<ChatDO> patchInitMessage(String timestamp, Long userId, Integer current, Integer size) {
        Page<ChatDO> page = new Page<>(current, size);
        LambdaQueryWrapper<ChatDO> wrapper = new LambdaQueryWrapper<>();

        wrapper
            .gt(ChatDO::getTimestamp, timestamp)
            .and(w->w.eq(ChatDO::getTgtUserId, userId).or(w2->w2.eq(ChatDO::getSrcUserId, userId)))
            .orderByAsc(ChatDO::getTimestamp);

        chatMapper.selectPage(page, wrapper);

        return page;
    }
}
