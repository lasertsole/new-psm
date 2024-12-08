package com.psm.domain.Communication.Chat.adaptor;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.domain.Communication.Chat.entity.ChatBO;
import org.apache.rocketmq.client.apis.ClientException;

public interface ChatAdaptor {
    /**
     * 初始化聊天记录
     *
     * @param srcClient 客户端
     * @param timestamp 时间戳
     */
    void patchInitMessage(SocketIOClient srcClient, String timestamp);

    /**
     * 发送聊天信息
     *
     * @param srcClient 客户端
     * @param chatBO 聊天信息
     * @return 本次聊天记录的时间戳
     */
    String sendMessage(SocketIOClient srcClient, ChatBO chatBO)  throws ClientException;
}
