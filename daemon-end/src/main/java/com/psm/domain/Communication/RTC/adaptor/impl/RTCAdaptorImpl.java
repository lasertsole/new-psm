package com.psm.domain.Communication.RTC.adaptor.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.psm.app.annotation.spring.Adaptor;
import com.psm.domain.Communication.RTC.adaptor.RTCAdaptor;
import com.psm.domain.Communication.RTC.service.RTCService;
import com.psm.domain.User.user.entity.User.UserBO;
import com.psm.infrastructure.SocketIO.POJOs.RTCSwap;
import com.psm.infrastructure.SocketIO.POJOs.RoomInvitation;
import com.psm.utils.Long.LongUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.InvalidParameterException;

@Slf4j
@Adaptor
public class RTCAdaptorImpl implements RTCAdaptor {
    @Autowired
    private RTCService rtcService;

    @Override
    public boolean createRoom(SocketIOClient srcClient, String roomId) {
        if(
            !LongUtils.stringCanBeConvertedToLong(roomId)
            || roomId.length() > 10
            || roomId.length() < 6
        )
            throw new InvalidParameterException("Invalid parameter");

        return rtcService.createRoom(srcClient, roomId);
    }

    @Override
    public String inviteJoinRoom(SocketIOClient srcClient, String targetUserId) throws ClientException {
        return rtcService.inviteJoinRoom(srcClient, targetUserId);
    }

    @Override
    public String agreeJoinRoom(SocketIOClient srcClient, @Valid RoomInvitation roomInvitation) throws ClientException {
        String roomId = roomInvitation.getRoomId();
        String srcUserId = roomInvitation.getSrcUserId();
        String tarUserId = roomInvitation.getTarUserId();
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
        String tarUserId = roomInvitation.getTarUserId();
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
        )
            throw new InvalidParameterException("Invalid parameter");

        return rtcService.swapSDP(srcClient, rtcSwap);
    }

    @Override
    public String swapCandidate(SocketIOClient srcClient, @Valid RTCSwap rtcSwap) throws ClientException {
        if(
            !LongUtils.stringCanBeConvertedToLong(rtcSwap.getRoomId())
            || !LongUtils.stringCanBeConvertedToLong(rtcSwap.getSrcUserId())
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