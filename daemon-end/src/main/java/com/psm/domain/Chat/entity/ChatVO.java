package com.psm.domain.Chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatVO implements Serializable {
    private String id;
    private String tgtUserId;
    private String srcUserId;
    private String timestamp;
    private String content;
}
