package com.psm.infrastructure.RepositoryImpl.Chat.chat;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Independent.Communication.Single.Chat.pojo.entity.ChatDO;

public interface ChatDB {
    /**
     * 插入聊天记录
     *
     * @param chatDO 聊天记录
     */
    void insert(ChatDO chatDO);

    /**
     * 客户端初始化时获取聊天记录
     *
     * @param timestamp 时间戳
     * @param current 当前页
     * @param userId 用户id
     * @param size 每页大小
     * @return 聊天记录Page对象
     */
    Page<ChatDO> patchInitMessage(String timestamp, Long userId, Integer current, Integer size);
}
