package com.psm.infrastructure.ES.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.psm.infrastructure.ES.properties.ESProperties;
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

    @Autowired
    private ESProperties esProperties;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(esProperties.getHost(), esProperties.getPort(), esProperties.getScheme()));
        builder.setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
            .setSocketTimeout(esProperties.getSocketTimeout())
            .setConnectTimeout(esProperties.getConnectTimeout())
            .setConnectionRequestTimeout(esProperties.getConnectionRequestTimeout())
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
