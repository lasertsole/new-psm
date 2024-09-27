package com.psm.infrastructure.config;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Paths;

@Configuration
@Slf4j
public class TusConfig {

    private final String tusDataPath = Paths.get("..", "tus").toAbsolutePath().toString();;

    private final Long expirationPeriod = 1L;// 超时时间设置为1天

    @PreDestroy
    public void exit() throws IOException {
        // cleanup any expired uploads and stale locks
        tusFileUploadService().cleanup();
    }

    @Bean
    public TusFileUploadService tusFileUploadService() {
        return new TusFileUploadService()
                .withStoragePath(tusDataPath + "/models")
                .withUploadUri("/models/upload")
                .withUploadExpirationPeriod(expirationPeriod * 1000 * 60 * 60 * 24);
    }
}