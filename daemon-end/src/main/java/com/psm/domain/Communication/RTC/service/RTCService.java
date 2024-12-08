package com.psm.domain.Communication.RTC.service;

import com.psm.infrastructure.SocketIO.POJOs.RoomInvitation;
import org.apache.rocketmq.client.apis.ClientException;

public interface RTCService {
    /**
     * 邀请用户加入房间
     *
     * @param roomInvitation 房间邀请函
     */
    void inviteJoinRoom(RoomInvitation roomInvitation) throws ClientException;

    /**
     * 处理房间邀请函
     *
     * @param roomInvitation 房间邀请函
     */
    void forwardRoomInvitation(RoomInvitation roomInvitation);
}
