package com.psm.domain.Chat.Event.EventPublisher;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ChatEventPublisher implements CommandLineRunner{
    @Autowired
    private ClientConfiguration clientConfiguration;

    @Autowired
    private ClientServiceProvider provider;

    // 消息发送的目标Topic名称，需要提前创建。
    String topic = "forwardMS";

    Producer producer;
    @PostConstruct
    public void init() throws ClientException {
        // 初始化Producer时需要设置通信配置以及预绑定的Topic。
        producer = provider.newProducerBuilder()
                .setTopics(topic)
                .setClientConfiguration(clientConfiguration)
                .build();
    }

    public void publish() throws ClientException {
        // 普通消息发送。
        Message message = provider.newMessageBuilder()
                .setTopic(topic)
                // 设置消息索引键，可根据关键字精确查找某条消息。
                .setKeys("messageKey")
                // 设置消息Tag，用于消费端根据指定Tag过滤消息。
                .setTag("messageTag")
                // 消息体。
                .setBody("messageBody".getBytes())
                .build();
        try {
            // 发送消息，需要关注发送结果，并捕获失败等异常。
            SendReceipt sendReceipt = producer.send(message);
            log.info("Send message successfully, messageId={}", sendReceipt.getMessageId());
        } catch (ClientException e) {
            log.error("Failed to send message", e);
        }
    }

    @PreDestroy
    public void destroy() throws IOException {
        producer.close();
    }

    @Override
    public void run(String... args) throws Exception {
        publish();
    }
}
