package com.psm.domain.Communication.Chat.event.valueObject;

import com.psm.domain.Communication.Chat.entity.ChatBO;
import lombok.Value;

@Value
public class DMForwardEvent {
    ChatBO chatBO;
}
