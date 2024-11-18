package com.psm.domain.Chat.adaptor;


import com.psm.domain.Chat.entity.ChatBO;

public interface ChatAdaptor {

    /**
     * 校验并转换
     *
     * @param chatBO 聊天DTO实体
     */
    void validateAndConvert(ChatBO chatBO);
}
