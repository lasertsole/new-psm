package com.psm.domain.User.user.Event.valueObject;

import lombok.Value;

@Value
public class SocketLoginEvent {
    String userId;
    String ip;
}
