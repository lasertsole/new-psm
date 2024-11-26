package com.psm.infrastructure.SocketIO;

import com.corundumstudio.socketio.SocketIOClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SocketIOGlobalVariable {
    // 存储用户id和对应的socket的映射（因为websocket连接在本地主机，所以不需要考虑多节点问题）
    public static final Map<String, SocketIOClient> userIdMapClient = new ConcurrentHashMap<>();
}
