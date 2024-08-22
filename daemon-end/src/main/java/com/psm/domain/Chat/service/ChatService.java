package com.psm.domain.Chat.service;

import org.springframework.beans.factory.InitializingBean;

import java.util.Observer;
import java.util.Map;

/**聊天领域服务
 *
 * @author moye
 * @date 2024/08/21
 */
public interface ChatService extends Observer, InitializingBean {
    /**
     * description: 给容器内所有的客户端发送通知
     * date: 2021年-09月-09日 14:08
     * author: zhanghang
     *
     * @param msg
     * @return void
     */
    void sendMessageToAllUser(Map<String,Object> msg);

    /**
     * description: 给指定用户发送通知
     * date: 2021年-09月-09日 14:09
     * author: zhanghang
     *
     * @param username
     * @param msg
     * @return void
     */
    void sendMessage(String username, Map<String,Object> msg);
}
