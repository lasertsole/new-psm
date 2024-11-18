package com.psm.domain.Chat.service;

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
}
