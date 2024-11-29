package com.psm.Client;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

@Slf4j
public class ESClient {
    private static final RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "http"))
            .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                    .setSocketTimeout(60000) // 设置为60秒
                    .setConnectTimeout(60000) // 设置为60秒
            ).build();
    private static final ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
    private static final ElasticsearchClient ESCLIENT = new ElasticsearchClient(transport);
    public static ElasticsearchClient getESClient() {
        return ESCLIENT;
    }
}
