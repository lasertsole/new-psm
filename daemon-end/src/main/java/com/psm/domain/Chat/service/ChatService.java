package com.psm.domain.Chat.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.domain.Chat.entity.ChatDTO;
import org.apache.rocketmq.client.apis.ClientException;

/**聊天领域服务
 *
 * @author moye
 * @date 2024/08/21
 */
public interface ChatService {
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

    /**
     * description: 处理一对一消息
     * author: moye
     *
     * @param message 消息体
     */
    void processMessageDM(ChatDTO message);
}
