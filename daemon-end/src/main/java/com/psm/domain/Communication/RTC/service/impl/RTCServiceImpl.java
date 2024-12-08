package com.psm.domain.Communication.RTC.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.domain.Communication.RTC.service.RTCService;
import com.psm.infrastructure.MQ.rocketMQ.MQPublisher;
import com.psm.infrastructure.SocketIO.SocketIOApi;
import com.psm.infrastructure.SocketIO.POJOs.RoomInvitation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class RTCServiceImpl implements RTCService {
    @Autowired
    private MQPublisher mqPublisher;

    @Autowired
    private SocketIOApi socketIOApi;

    @Override
    public void inviteJoinRoom(RoomInvitation roomInvitation) throws ClientException {
        mqPublisher.publish(roomInvitation, "inviteJoinRoom", "RTC", roomInvitation.getRoomType());
    }

    @Override
    public void receiveRoomInvitation(RoomInvitation roomInvitation) {
        SocketIOClient tarClient = socketIOApi.getLocalUserSocket(roomInvitation.getTarUserId());
        if (Objects.isNull(tarClient)) return;

        tarClient.sendEvent("receiveRoomInvitation", roomInvitation);
    };
}
