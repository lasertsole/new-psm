package com.psm.infrastructure.ES.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.elasticsearch.client.RestClientBuilder;

@Slf4j
@Configuration
public class ESClientConfig {
    private ElasticsearchClient elasticsearchClient;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "http"));
        builder.setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
            .setSocketTimeout(60000) // 设置套接字超时时间为60秒
            .setConnectTimeout(60000) // 设置连接超时时间为60秒
            .setConnectionRequestTimeout(60000) // 设置连接请求超时时间为60秒
        );
        RestClient restClient = builder.build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    @PreDestroy
    public void exit() {
        if (elasticsearchClient != null) {
            elasticsearchClient.shutdown();
        }
    }
}
