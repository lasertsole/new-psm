package com.psm.domain.Chat.Event.EventListener;

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
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class ChatEventListener {
    @Autowired
    private ClientConfiguration clientConfiguration;

    @Autowired
    private ClientServiceProvider provider;

    // 订阅消息的过滤规则，表示订阅所有Tag的消息。
    private final String tag = "*";

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
                    String messageBody = String.valueOf(messageView.getBody());
                    log.info("Received message body: {}", messageBody);

                    // 处理消息并返回消费结果。
                    log.info("Consume message successfully, messageId={}, messageBody={}", messageView.getMessageId(), messageBody);
                    return ConsumeResult.SUCCESS;
                })

                .build();
    }

    @PreDestroy
    public void destroy() throws IOException {
        pushConsumer.close();
    }
}
