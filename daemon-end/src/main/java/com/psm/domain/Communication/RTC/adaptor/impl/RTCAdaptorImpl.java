package com.psm.domain.Communication.RTC.adaptor.impl;

import com.psm.app.annotation.spring.Adaptor;
import com.psm.domain.Communication.RTC.adaptor.RTCAdaptor;
import com.psm.domain.Communication.RTC.service.RTCService;
import com.psm.infrastructure.SocketIO.POJOs.RoomInvitation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Adaptor
public class RTCAdaptorImpl implements RTCAdaptor {
    @Autowired
    private RTCService rtcService;

    @Override
    public void inviteJoinRoom(RoomInvitation roomInvitation) throws ClientException {
        rtcService.inviteJoinRoom(roomInvitation);
    }
}