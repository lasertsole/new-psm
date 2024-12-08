package com.psm.infrastructure.SocketIO.POJOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.io.Serializable;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomInvitation implements Serializable {
    String roomId;
    String roomOwnerId;
    String roomName;
    String roomType;
    String srcUserId;
    String tarUserId;
}
