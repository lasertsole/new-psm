package com.psm.infrastructure.SocketIO;

import com.corundumstudio.socketio.SocketIOClient;

import java.util.*;

import com.psm.infrastructure.SocketIO.POJOs.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SocketIOApi {
    private final Cache roomCache;
    public SocketIOApi(CacheManager cacheManager) {
        this.roomCache = cacheManager.getCache("socketRoomCache");
    }

    // 存储用户id和对应的socket的映射（因为websocket连接在本地主机，所以不需要考虑多节点问题）
    private final Map<String, SocketIOClient> localUserIdMapClient = new ConcurrentHashMap<>();

    public void addLocalUser(String namespace, String key, SocketIOClient client) {
        localUserIdMapClient.put(namespace + key, client);
    }

    public SocketIOClient getLocalUserSocket(String namespace, String key) {
        return localUserIdMapClient.get(namespace + key);
    }

    public void removeLocalUser(String namespace, String userId) {
        localUserIdMapClient.remove(namespace + userId); //尝试从 Map 中移除一个不存在的键（key），操作是安全的，不会抛出异常
    }

    public Boolean createSocketRoom(String namespace, Room room) {
        String key = namespace + room.getRoomId();
        if (Objects.nonNull(roomCache.get(key))) return false;

        Set<String> userIdSet = new HashSet<String>();
        userIdSet.add(room.getRoomOwnerId());
        room.setMemberIdSet(userIdSet);
        roomCache.put(key, room);

        return true;
    }

    public Room getSocketRoom(String namespace, String roomId) {
        return roomCache.get(namespace + roomId, Room.class);
    }

    public void addUserToSocketRoom(String namespace, String roomId, String userId) {
        Room room = getSocketRoom(namespace, roomId);
        room.getMemberIdSet().add(userId);
        roomCache.put(namespace + roomId, room);
    }

    public void removeUserFromSocketRoom(String namespace, String roomId, String userId) {
        Room room = getSocketRoom(namespace, roomId);
        Set<String> memberIdSet = room.getMemberIdSet();
        memberIdSet.remove(userId);
        if (memberIdSet.isEmpty()) {
            roomCache.evict(namespace + roomId);
        } else {
            roomCache.put(namespace + roomId, room);
        }
    }

    public void destroySocketRoom(String namespace, String roomId) {
        roomCache.evict(namespace + roomId);
    }
}
