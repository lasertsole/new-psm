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
    public SocketIOApi(CacheManager cacheManager) {
        this.roomCache = cacheManager.getCache("socketRoomCache");
    }

    // 存储用户id和对应的socket的映射（因为websocket连接在本地主机，所以不需要考虑多节点问题）
    private final Map<String, SocketIOClient> localUserIdMapClient = new ConcurrentHashMap<>();

    public void addLocalUser(String userId, SocketIOClient client) {
        localUserIdMapClient.put(userId, client);
    }

    public SocketIOClient getLocalUserSocket(String userId) {
        return localUserIdMapClient.get(userId);
    }

    public void removeLocalUser(String userId) {
        localUserIdMapClient.remove(userId); //尝试从 Map 中移除一个不存在的键（key），操作是安全的，不会抛出异常
    }

    private final Cache roomCache;

    public Boolean createSocketRoom(Room room) {
        String key = "socketRoom:"+room.getRoomId();
        if (Objects.nonNull(roomCache.get(key))) return false;

        Set<String> userIdSet = new HashSet<String>();
        userIdSet.add(room.getRoomOwnerId());
        room.setMemberIdSet(userIdSet);
        roomCache.put(key, room);

        return true;
    }

    public Room getSocketRoom(String roomId) {
        return (Room) Optional.ofNullable(roomCache.get("socketRoom:"+roomId)).orElse(null);
    }

    public void addUserToSocketRoom(String roomId, String userId) {
        Room room = getSocketRoom(roomId);
        room.getMemberIdSet().add(userId);
        roomCache.put("socketRoom:"+roomId, room);
    }

    public void removeUserFromSocketRoom(String roomId, String userId) {
        Room room = getSocketRoom(roomId);
        Set<String> memberIdSet = room.getMemberIdSet();
        memberIdSet.remove(userId);
        if (memberIdSet.isEmpty()) {
            roomCache.evict("socketRoom:"+roomId);
        } else {
            roomCache.put("socketRoom:"+roomId, room);
        }
    }

    public void destroySocketRoom(String roomId) {
        roomCache.evict("socketRoom:"+roomId);
    }
}
