package com.psm.domain.Chat.adaptor;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.domain.Chat.entity.ChatBO;

public interface ChatAdaptor {

    /**
     * 校验并转换
     *
     * @param chatBO 聊天DTO实体
     */
    void validateAndConvert(ChatBO chatBO);

    /**
     * 初始化聊天记录
     *
     * @param srcClient 客户端
     * @param timestamp 时间戳
     */
    void patchInitMessage(SocketIOClient srcClient, String timestamp);
}
