package com.psm.domain.Communication.RTC.event.valueObject;

import com.psm.infrastructure.SocketIO.POJOs.RoomInvitation;
import lombok.Value;

@Value
public class InviteJoinRoomEvent {
    RoomInvitation roomInvitation;
}
