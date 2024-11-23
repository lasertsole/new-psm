package com.psm.domain.Chat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.corundumstudio.socketio.SocketIOClient;
import com.psm.domain.Chat.entity.ChatBO;

/**聊天领域服务
 *
 * @author moye
 * @date 2024/08/21
 */
public interface ChatService {
    /**
     * description: 处理一对一消息
     * author: moye
     *
     * @param message 消息体
     */
    void processMessageDM(ChatBO message);

    /**
     * 初始化聊天记录
     *
     * @param srcClient 客户端
     * @param userId    用户id
     * @param timestamp 时间戳
     */
    void patchInitMessage(SocketIOClient srcClient, Long userId, String timestamp);
}
