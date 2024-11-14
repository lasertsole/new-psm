package com.psm.domain.Chat.adaptor;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.domain.Chat.entity.ChatDTO;
import org.apache.rocketmq.client.apis.ClientException;

public interface ChatAdaptor {
    /**
     * description: 建立连接
     * @param client 客户端
     */
    void connectDM(SocketIOClient client);

    /**
     * description: 断开连接
     * @param client 客户端
     */
    void disconnectDM(SocketIOClient client);

    /**
     * description: 给指定用户发送通知
     * date: 2021年-09月-09日 14:09
     * author: moye
     *
     * @param client 客户端
     * @param message 消息体
     */
    void sendMessageDM(SocketIOClient client, ChatDTO message) throws ClientException;
}
