package com.psm.infrastructure.SocketIO.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "socketio.app.configuration")
public class SocketAppProperties {
    private String DMExpireDay;
    private Integer DMMaxInitCountInPage;
}
