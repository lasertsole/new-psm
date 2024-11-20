package com.psm.domain.Chat.entity;

import java.io.Serializable;

public class ChatVO implements Serializable {
    private Long id;
    private Long tgtUserId;
    private Long srcUserId;
    private String timestamp;
    private String content;
}
