package com.psm.domain.Communication.RTC.event.valueObject;

import com.psm.infrastructure.SocketIO.POJOs.RTCSwap;
import lombok.Value;

@Value
public class LeaveRoomEvent {
    RTCSwap rtcSwap;
}
