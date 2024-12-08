package com.psm.domain.Communication.RTC.adaptor;

import com.psm.infrastructure.SocketIO.POJOs.RoomInvitation;
import org.apache.rocketmq.client.apis.ClientException;

public interface RTCAdaptor {
    /**
     * 邀请用户加入房间
     *
     * @param roomInvitation 房间邀请函
     */
    void inviteJoinRoom(RoomInvitation roomInvitation) throws ClientException;
}
