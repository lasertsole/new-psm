package com.psm.domain.Communication.RTC.adaptor.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.domain.Communication.RTC.adaptor.RTCAdaptor;
import com.psm.domain.Communication.RTC.service.RTCService;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.infrastructure.SocketIO.POJOs.RTCSwap;
import com.psm.infrastructure.SocketIO.POJOs.Room;
import com.psm.infrastructure.SocketIO.POJOs.RoomInvitation;
import com.psm.utils.Long.LongUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.InvalidParameterException;
import java.util.Objects;

@Slf4j
@Adaptor
public class RTCAdaptorImpl implements RTCAdaptor {
    @Autowired
    private RTCService rtcService;

    @Override
    public boolean createRoom(SocketIOClient srcClient, @Valid Room room) {
        return rtcService.createRoom(srcClient, room);
    }

    @Override
    public String inviteJoinRoom(SocketIOClient srcClient, RoomInvitation roomInvitation) throws ClientException {
        return rtcService.inviteJoinRoom(srcClient, roomInvitation);
    }

    @Override
    public String agreeJoinRoom(SocketIOClient srcClient, @Valid RoomInvitation roomInvitation) throws ClientException {
        String roomId = roomInvitation.getRoomId();
        String srcUserId = roomInvitation.getSrcUserId();
        String tarUserId = roomInvitation.getTgtUserId();
        if(
                !LongUtils.stringCanBeConvertedToLong(roomId)
                || !LongUtils.stringCanBeConvertedToLong(tarUserId)
                || !tarUserId.equals(String.valueOf(((UserBO) srcClient.get("userInfo")).getId()))
                || srcUserId.equals(tarUserId)
        )
            throw new InvalidParameterException("Invalid parameter");

        return rtcService.agreeJoinRoom(srcClient, roomInvitation);
    }

    @Override
    public String rejectJoinRoom(SocketIOClient srcClient, @Valid RoomInvitation roomInvitation) throws ClientException {
        String roomId = roomInvitation.getRoomId();
        String srcUserId = roomInvitation.getSrcUserId();
        String tarUserId = roomInvitation.getTgtUserId();
        if(
                !LongUtils.stringCanBeConvertedToLong(roomId)
                || !LongUtils.stringCanBeConvertedToLong(tarUserId)
                || !tarUserId.equals(String.valueOf(((UserBO) srcClient.get("userInfo")).getId()))
                || srcUserId.equals(tarUserId)
        )
            throw new InvalidParameterException("Invalid parameter");

        return rtcService.rejectJoinRoom(srcClient, roomInvitation);
    }

    @Override
    public String swapSDP(SocketIOClient srcClient, @Valid RTCSwap rtcSwap) throws ClientException {
        if(
                !LongUtils.stringCanBeConvertedToLong(rtcSwap.getRoomId())
                || !LongUtils.stringCanBeConvertedToLong(rtcSwap.getSrcUserId())
                || !LongUtils.stringCanBeConvertedToLong(rtcSwap.getTgtUserId())
                || Objects.isNull(rtcSwap.getSrcUserName())
        )
            throw new InvalidParameterException("Invalid parameter");

        return rtcService.swapSDP(srcClient, rtcSwap);
    }

    @Override
    public String swapCandidate(SocketIOClient srcClient, @Valid RTCSwap rtcSwap) throws ClientException {
        if(
                !LongUtils.stringCanBeConvertedToLong(rtcSwap.getRoomId())
                || !LongUtils.stringCanBeConvertedToLong(rtcSwap.getSrcUserId())
                || !LongUtils.stringCanBeConvertedToLong(rtcSwap.getTgtUserId())
                || Objects.isNull(rtcSwap.getSrcUserName())
        )
            throw new InvalidParameterException("Invalid parameter");

        return rtcService.swapCandidate(srcClient, rtcSwap);
    }

    @Override
    public String leaveRoom(SocketIOClient srcClient, @Valid RTCSwap rtcSwap) throws ClientException {
        if(
            !LongUtils.stringCanBeConvertedToLong(rtcSwap.getRoomId())
            || !LongUtils.stringCanBeConvertedToLong(rtcSwap.getSrcUserId())
        )
            throw new InvalidParameterException("Invalid parameter");

        return rtcService.leaveRoom(srcClient, rtcSwap);
    }
}