package com.psm.infrastructure.Cache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.data.redission")
public class RedissionProperties {
    private int threads;
    private int connectionPoolSize;
    private int connectionMinimumIdleSize;
}
