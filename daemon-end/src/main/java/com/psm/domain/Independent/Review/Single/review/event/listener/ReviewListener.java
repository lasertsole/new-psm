package com.psm.domain.Independent.Review.Single.review.event.listener;

import com.psm.domain.Independent.Review.Single.review.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReviewListener {
    @Autowired
    private ReviewService reviewService;

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
    private final String topic = "REVIEW";

    // PushConsumer对象，其作用为以push方式消费消息,设置为群组模式。
    private PushConsumer pushClusterConsumer;

    // PushConsumer对象，其作用为以push方式消费消息,设置为广播模式。
    private PushConsumer pushBroadCastConsumer;

//    @PostConstruct
//    public void init() throws ClientException {
//        // 初始化PushConsumer，需要绑定消费者分组ConsumerGroup、通信参数以及订阅关系。
//        pushClusterConsumer = provider.newPushConsumerBuilder()
//                .setClientConfiguration(clientConfiguration)
//                // 设置消费者分组。
//                .setConsumerGroup(consumerGroup)
//                // 设置预绑定的订阅关系。
//                .setSubscriptionExpressions(Collections.singletonMap(topic, new FilterExpression("*", FilterExpressionType.TAG)))
//
//                // 设置消费监听器。
//                .setMessageListener(messageView -> {
//                    try {
//                        // 获取消息体
//                        ByteBuffer buffer = messageView.getBody();
//                        // 将 ByteBuffer 转换为字节数组
//                        byte[] bodyBytes = new byte[buffer.remaining()];
//                        buffer.duplicate().get(bodyBytes);  // 使用 duplicate() 避免影响原缓冲区的位置
//
//                        // 将字节数组转换为字符串
//                        String jsonString = new String(bodyBytes, StandardCharsets.UTF_8);
//
//                        // 获取消息的标签
//                        String tag = messageView.getTag().orElse("");
//
//                        switch (tag) {
//                            case "uploadModel3D":
//                                Event<Model3dBO> uploadModel3DEvent = JSON.parseObject(jsonString, Event.class, JSONReader.Feature.SupportClassForName);
//                                Model3dBO model3dBO = uploadModel3DEvent.getContent();
//                                reviewService.processUploadModel3D(model3dBO.getUserId(), model3dBO.getStorage(), model3dBO.getVisible());
//                                break;
//                            default:
//                                break;
//                        }
//
//                        return ConsumeResult.SUCCESS;
//                    } catch (Exception e) {
//                        log.info("error is "+ e);
//                        return ConsumeResult.FAILURE;
//                    }
//                })
//                .build();
//
//        pushBroadCastConsumer = provider.newPushConsumerBuilder()
//                .setClientConfiguration(clientConfiguration)
//                // 设置消费者分组(广播模式每个服务器主题不同)。
//                .setConsumerGroup(workerId+datacenterId)
//                // 设置预绑定的订阅关系。
//                .setSubscriptionExpressions(Collections.singletonMap(topic, new FilterExpression("*", FilterExpressionType.TAG)))
//                // 设置消费监听器。
//                .setMessageListener(messageView -> {
//                    try {
//                        // 获取消息体
//                        ByteBuffer buffer = messageView.getBody();
//                        // 将 ByteBuffer 转换为字节数组
//                        byte[] bodyBytes = new byte[buffer.remaining()];
//                        buffer.duplicate().get(bodyBytes);  // 使用 duplicate() 避免影响原缓冲区的位置
//
//                        // 将字节数组转换为字符串
//                        String jsonString = new String(bodyBytes, StandardCharsets.UTF_8);
//
//                        // 获取消息的标签
//                        String tag = messageView.getTag().orElse("");
//
//                        // 获取消息的keys
//                        Set<String> keys =  new HashSet<>(messageView.getKeys());
//
//                        switch (tag) {
//                            case "socketLogin":
//                                if(keys.contains(workerId+datacenterId)) break;// 如果消息来自本服务器，则跳过
//
//                                Event<UserDTO> socketLoginEvent = JSON.parseObject(jsonString, Event.class, JSONReader.Feature.SupportClassForName);
//                                UserDTO userDTO = socketLoginEvent.getContent();
//                                reviewService.forwardSocketLogin(userDTO.getId());
//                                break;
//                            default:
//                                break;
//                        };
//
//                        return ConsumeResult.SUCCESS;
//                    } catch (Exception e) {
//                        log.error("error is "+ e);
//                        return ConsumeResult.FAILURE;
//                    }
//                })
//                .build();
//    }
//
//    @PreDestroy
//    public void destroy() throws IOException {
//        pushClusterConsumer.close();
//        pushBroadCastConsumer.close();
//    }
}
