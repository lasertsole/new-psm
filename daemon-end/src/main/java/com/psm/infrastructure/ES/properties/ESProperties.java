package com.psm.infrastructure.ES.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.data.elastic-search")
public class ESProperties {
    private String host;
    private int port;
    private String scheme;
    private int socketTimeout;
    private int connectTimeout;
    private int connectionRequestTimeout;
}
