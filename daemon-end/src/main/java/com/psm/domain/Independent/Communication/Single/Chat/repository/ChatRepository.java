package com.psm.domain.Independent.Communication.Single.Chat.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psm.domain.Independent.Communication.Single.Chat.pojo.entity.ChatDO;

public interface ChatRepository {
    /**
     * 数据库插入一条聊天记录
     *
     * @param chatDO 聊天记录
     */
    void DBInsert(ChatDO chatDO);

    /**
     * 客户端初始化时获取聊天记录
     *
     * @param timestamp 时间戳
     * @param current 当前页
     * @param userId 用户id
     * @param size 每页大小
     * @return 聊天记录Page对象
     */
    Page<ChatDO> DBPatchInitMessage(String timestamp, Long userId, int current, Integer size);
}
