package com.psm.domain.Communication.Chat.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.domain.Communication.Chat.entity.ChatBO;
import org.apache.rocketmq.client.apis.ClientException;

/**聊天领域服务
 *
 * @author moye
 * @date 2024/08/21
 */
public interface ChatService {
    /**
     * description: 处理一对一消息
     *
     * @param message 消息体
     */
    void storageMessageDM(ChatBO message);

    /**
     * 初始化聊天记录
     *
     * @param srcClient 客户端
     * @param userId    用户id
     * @param timestamp 时间戳
     */
    void patchInitMessage(SocketIOClient srcClient, Long userId, String timestamp);

    /**
     * 发送聊天信息
     *
     * @param srcClient 客户端
     * @param chatBO 聊天信息
     * @return 本次聊天记录的时间戳
     */
    String sendMessage(SocketIOClient srcClient, ChatBO chatBO) throws ClientException;

    /**
     * 接收聊天信息
     * @param chatBO
     */
    void receiveMessage(ChatBO chatBO);
}
