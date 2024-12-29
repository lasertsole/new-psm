package com.psm.domain.Independent.Communication.Single.Chat.event.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.psm.domain.Independent.Communication.Single.Chat.entity.ChatBO;
import com.psm.domain.Independent.Communication.Single.Chat.service.ChatService;
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
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class ChatEventListener {
    @Autowired
    ChatService chatService;

    @Autowired
    private ClientConfiguration clientConfiguration;

    @Autowired
    private ClientServiceProvider provider;

    // 为消费者指定所属的消费者分组，Group需要提前创建。
    private final String consumerGroup = "DefaultConsumerGroup";

    @Value("${server.workerId}")
    private String workerId;

    @Value("${server.datacenterId}")
    private String datacenterId;

    // 指定需要订阅哪个目标Topic，Topic需要提前创建。
    private final String topic = "CHAT";

    // PushConsumer对象，其作用为以push方式消费消息,设置为群组模式。
    private PushConsumer pushClusterConsumer;

    // PushConsumer对象，其作用为以push方式消费消息,设置为广播模式。
    private PushConsumer pushBroadCastConsumer;

    @PostConstruct
    public void init() throws ClientException {
        // 初始化PushConsumer，需要绑定消费者分组ConsumerGroup、通信参数以及订阅关系。
        pushClusterConsumer = provider.newPushConsumerBuilder()
                .setClientConfiguration(clientConfiguration)
                // 设置消费者分组。
                .setConsumerGroup(consumerGroup)
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
                            case "DMForward":
                                Event<ChatBO> event = JSON.parseObject(jsonString, Event.class, JSONReader.Feature.SupportClassForName);
                                chatService.storageMessageDM(event.getContent());
                                break;
                            default:
                                log.warn("Unknown tag: {}", tag);
                                break;
                        };

                        return ConsumeResult.SUCCESS;
                    }  catch(Exception e) {
                        log.error("error is "+ e);
                        return ConsumeResult.FAILURE;
                    }
                })
                .build();

        pushBroadCastConsumer = provider.newPushConsumerBuilder()
            .setClientConfiguration(clientConfiguration)
            // 设置消费者分组(广播模式每个服务器主题不同)。
            .setConsumerGroup(workerId+datacenterId)
            // 设置预绑定的订阅关系。
            .setSubscriptionExpressions(Collections.singletonMap(topic, new FilterExpression("*", FilterExpressionType.TAG)))
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

                    // 获取消息的keys
                    Set<String> keys =  new HashSet<>(messageView.getKeys());

                    switch (tag) {
                        case "DMForward":
                            if(keys.contains(workerId+datacenterId)) break;// 如果消息来自本服务器，则跳过

                            Event<ChatBO> event = JSON.parseObject(jsonString, Event.class, JSONReader.Feature.SupportClassForName);
                            chatService.receiveMessage(event.getContent());
                            break;
                        default:
                            break;
                    }

                    return ConsumeResult.SUCCESS;
                } catch(Exception e) {
                    log.error("error is "+ e);
                    return ConsumeResult.FAILURE;
                }
            })
            .build();
    }

    @PreDestroy
    public void destroy() throws IOException {
        pushClusterConsumer.close();
        pushBroadCastConsumer.close();
    }
}
