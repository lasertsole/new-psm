package com.psm.domain.Communication.RTC.event.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.psm.domain.Communication.RTC.service.RTCService;
import com.psm.infrastructure.SocketIO.POJOs.RTCSwap;
import com.psm.infrastructure.SocketIO.POJOs.RoomInvitation;
import com.psm.types.common.Event.Event;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Slf4j
@Component
public class RTCEventListener {
    @Autowired
    private RTCService rtcService;

    @Autowired
    private ClientConfiguration clientConfiguration;

    @Autowired
    private ClientServiceProvider provider;

    @Value("${server.workerId}")
    private String workerId;

    @Value("${server.datacenterId}")
    private String datacenterId;

    // 指定需要订阅哪个目标Topic，Topic需要提前创建。
    private final String topic = "RTC";

    private PushConsumer pushConsumer;

    @PostConstruct
    public void init() throws ClientException {
        // 初始化PushConsumer，需要绑定消费者分组ConsumerGroup、通信参数以及订阅关系。
        pushConsumer = provider.newPushConsumerBuilder()
                .setClientConfiguration(clientConfiguration)
                // 设置消费者分组(广播模式每个服务器主题不同)。
                .setConsumerGroup(workerId+datacenterId)
                // 设置预绑定的订阅关系。
                .setSubscriptionExpressions(Collections.singletonMap(topic, new FilterExpression("*", FilterExpressionType.TAG)))    // 订阅消息的过滤规则，表示订阅所有Tag的消息。
                // 设置消费监听器。
                .setMessageListener(messageView -> {
                    try{
                        // 获取消息体
                        ByteBuffer buffer = messageView.getBody();
                        // 将 ByteBuffer 转换为字节数组
                        byte[] bodyBytes = new byte[buffer.remaining()];
                        buffer.duplicate().get(bodyBytes);  // 使用 duplicate() 避免影响原缓冲区的位置

                        // 将字节数组转换为字符串
                        String jsonString = new String(bodyBytes, StandardCharsets.UTF_8);

                        // 获取消息的标签
                        String tag = messageView.getTag().orElse("");

                        switch (tag) {
                            case "inviteJoinRoom":
                                Event<RoomInvitation> inviteJoinRoomEvent = JSON.parseObject(jsonString, Event.class, JSONReader.Feature.SupportClassForName);
                                rtcService.forwardInviteJoinRoom(inviteJoinRoomEvent.getContent());
                                break;
                            case "agreeJoinRoom":
                                Event<RoomInvitation> agreeJoinRoomEvent = JSON.parseObject(jsonString, Event.class, JSONReader.Feature.SupportClassForName);
                                rtcService.forwardAgreeJoinRoom(agreeJoinRoomEvent.getContent());
                                break;
                            case "rejectJoinRoom":
                                Event<RoomInvitation> rejectJoinRoomEvent = JSON.parseObject(jsonString, Event.class, JSONReader.Feature.SupportClassForName);
                                rtcService.forwardRejectJoinRoom(rejectJoinRoomEvent.getContent());
                                break;
                            case "swapSDP":
                                Event<RTCSwap> swapSDPEvent = JSON.parseObject(jsonString, Event.class, JSONReader.Feature.SupportClassForName);
                                rtcService.forwardSwapSDP(swapSDPEvent.getContent());
                                break;
                            case "swapCandidate":
                                Event<RTCSwap> swapCandidateEvent = JSON.parseObject(jsonString, Event.class, JSONReader.Feature.SupportClassForName);
                                rtcService.forwardSwapCandidate(swapCandidateEvent.getContent());
                                break;
                            case "leaveRoom":
                                Event<RTCSwap> leaveRoomEvent = JSON.parseObject(jsonString, Event.class, JSONReader.Feature.SupportClassForName);
                                rtcService.forwardLeaveRoom(leaveRoomEvent.getContent()); // 注意这里可能是 forwardLeaveRoom
                                break;
                            default:
                                break;
                        };

                        return ConsumeResult.SUCCESS;
                    } catch (Exception e) {
                        log.error("error is "+ e);
                        return ConsumeResult.FAILURE;
                    }
                })
                .build();
    }

    @PreDestroy
    public void destroy() throws IOException {
        pushConsumer.close();
    }
}
