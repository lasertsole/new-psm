package com.psm.domain.Chat.event.EventListener;

import com.alibaba.fastjson2.JSON;
import com.psm.domain.Chat.entity.ChatDAO;
import com.psm.domain.Chat.service.ChatService;
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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Slf4j
@Component
public class ChatEventListener {
    @Autowired
    ChatService chatService;

    @Autowired
    private ClientConfiguration clientConfiguration;

    @Autowired
    private ClientServiceProvider provider;

    // 订阅消息的过滤规则，表示订阅所有Tag的消息。
    private final String tag = "OneToOne";

    // 订阅消息的过滤表达式，表示订阅所有消息。
    private final FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);

    // 为消费者指定所属的消费者分组，Group需要提前创建。
    private final String consumerGroup = "DefaultConsumerGroup";

    // 指定需要订阅哪个目标Topic，Topic需要提前创建。
    private final String topic = "forwardMS";

    // PushConsumer对象，其作用为消费消息。
    private PushConsumer pushConsumer;


    @PostConstruct
    public void init() throws ClientException {
        // 初始化PushConsumer，需要绑定消费者分组ConsumerGroup、通信参数以及订阅关系。
        pushConsumer = provider.newPushConsumerBuilder()
                .setClientConfiguration(clientConfiguration)
                // 设置消费者分组。
                .setConsumerGroup(consumerGroup)
                // 设置预绑定的订阅关系。
                .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))

                // 设置消费监听器。
                .setMessageListener(messageView -> {
                    // 获取消息体
                    ByteBuffer buffer = messageView.getBody();
                    // 将 ByteBuffer 转换为字节数组
                    byte[] bodyBytes = new byte[buffer.remaining()];
                    buffer.duplicate().get(bodyBytes);  // 使用 duplicate() 避免影响原缓冲区的位置

                    // 将字节数组转换为字符串
                    String jsonString = new String(bodyBytes, StandardCharsets.UTF_8);
                    ChatDAO messageBody = JSON.parseObject(jsonString, ChatDAO.class);



                    return ConsumeResult.SUCCESS;
                })

                .build();
    }

    @PreDestroy
    public void destroy() throws IOException {
        pushConsumer.close();
    }
}
