package com.psm.infrastructure.MQ.rocketMQ.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientConfigurationBuilder;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
public class RKMQConfig {
    // 接入点地址，需要设置成Proxy的地址和端口列表，一般是xxx:8080;xxx:8081
    private static final String proxyEndpoints = "localhost:8081";

    @Bean
    public ClientConfiguration getConfig(){
        ClientConfigurationBuilder builder = ClientConfiguration.newBuilder().setEndpoints(proxyEndpoints);
        builder.setRequestTimeout(Duration.ofSeconds(30)); // 设置请求代理服务器超时时间为 30秒
        return builder.build();
    }

    @Bean
    public ClientServiceProvider getProvider(){
        return ClientServiceProvider.loadService();
    }
}
