package com.psm.infrastructure.utils.Tus;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
@Component
public class TusProperties {
    private final Path tusDataPath = Paths.get("..", "uploads");
    private final Long expirationPeriod = 1* 1000 * 60 * 60 * 24L;// 超时时间设置为1天
    @Value("${server.protocol}")
    private String protocol;
    @Value("${server.address}")
    private String address;
    @Value("${server.port}")
    private String port;
}
