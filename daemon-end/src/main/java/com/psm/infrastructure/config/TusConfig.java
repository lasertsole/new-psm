package com.psm.infrastructure.config;

import com.psm.infrastructure.utils.Tus.TusProperties;
import com.psm.infrastructure.utils.Tus.UploadSuccessExtension;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@Slf4j
public class TusConfig {
    @Autowired
    private TusProperties tusProperties;

    @Autowired
    private UploadSuccessExtension uploadSuccessExtension;

    @PreDestroy
    public void exit() throws IOException {
        // cleanup any expired uploads and stale locks
        tusFileUploadService().cleanup();
    }

    @Bean
    public TusFileUploadService tusFileUploadService() {
        return new TusFileUploadService()
                .withStoragePath(tusProperties.getTusDataPath().toAbsolutePath().toString())
                .withUploadUri("/[a-zA-Z]+/upload")
                .withUploadExpirationPeriod(tusProperties.getExpirationPeriod())
                .addTusExtension(uploadSuccessExtension);
    }
}