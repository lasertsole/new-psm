package com.psm.infrastructure.MQ.rocketMQ;

import com.alibaba.fastjson2.JSON;
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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class MQPublisher {
    @Autowired
    private ClientConfiguration clientConfiguration;

    @Autowired
    private ClientServiceProvider provider;

    private Producer producer;
    @PostConstruct
    private void init() throws ClientException {
        // 初始化Producer时需要设置通信配置以及预绑定的Topic。
        producer = provider.newProducerBuilder()
                .setClientConfiguration(clientConfiguration)
                .build();
    }

    public void publish(Object body, String tag, String topic, String key) throws ClientException {
        if (!(body instanceof Serializable)) {
            throw new IllegalArgumentException("The object must implement Serializable interface.");
        }

        // 普通消息发送。
        Message message = provider.newMessageBuilder()
                // 消息体。
                .setBody(JSON.toJSONString(body).getBytes(StandardCharsets.UTF_8))
                // 设置消息Tag，用于消费端根据指定Tag过滤消息。
                .setTag(tag)
                // 设置主题
                .setTopic(topic)
                // 设置消息索引键，可根据关键字精确查找某条消息。
                .setKeys(key)
                .build();
        try {
            // 发送消息，需要关注发送结果，并捕获失败等异常。
            SendReceipt sendReceipt = producer.send(message);
        } catch (ClientException e) {
            log.error("Failed to send message", e);
        }
    }

    @PreDestroy
    private void destroy() throws IOException {
        producer.close();
    }
}
