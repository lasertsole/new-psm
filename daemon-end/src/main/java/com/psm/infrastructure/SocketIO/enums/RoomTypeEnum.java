package com.psm.infrastructure.SocketIO.enums;

import com.alibaba.fastjson2.annotation.JSONCreator;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RoomTypeEnum {
    DRTC("DRTC"),   // 一对一rtc
    DMRTC("DMRTC"), // 一对多rtc
    MMRTC("MMRTC"), // 多对多rtc
    GM("GM");       // websocket群聊

    @JSONField(name = "roomType")
    private final String name;

    @JSONCreator
    public static RoomTypeEnum fromName(String name) {
        return Arrays.stream(values())
                .filter(type -> type.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown RoomTypeEnum name: " + name));
    }
}
