package com.psm.infrastructure.config;

import com.psm.infrastructure.utils.Tus.TusProperties;
import com.psm.infrastructure.utils.Tus.UploadCreationExtension;
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

    @Autowired
    private UploadCreationExtension uploadCreationExtension;

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
                .withUploadExpirationPeriod(tusProperties.getExpirationPeriod())//设置过期时间,单位毫秒
                //开启线程本地缓存，加速上传
                .withThreadLocalCache(true)
                //停止自带的extension creation
                .disableTusExtension("creation")
                //添加自定义的extension实现
                .addTusExtension(uploadCreationExtension)//解决通过网关转发的url和后端获取的url不一致，导致无法正常上传
                .addTusExtension(uploadSuccessExtension);
    }
}