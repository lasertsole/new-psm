package com.psm.trigger.routine;

import com.psm.infrastructure.Tus.properties.TusProperties;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
@EnableScheduling
public class AutoClear {
    @Autowired
    private TusProperties tusProperties;

    @Autowired
    TusFileUploadService tusFileUploadService;

    @Scheduled(fixedDelayString = "PT24H")
    public void clearUp() {
        //删除过期文件
        Path locksDir = tusProperties.getTusDataPath().resolve("locks");
        if (Files.exists(locksDir)) {
            try {
                tusFileUploadService.cleanup();
            } catch (IOException e) {
                log.error("Error cleaning file upload directory:", e);
            }
        }
    }
}
