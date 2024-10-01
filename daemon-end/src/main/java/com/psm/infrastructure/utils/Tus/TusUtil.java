package com.psm.infrastructure.utils.Tus;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import me.desair.tus.server.upload.UploadInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class TusUtil {
    @Autowired
    private TusProperties tusProperties;

    @Autowired
    private TusFileUploadService tusFileUploadService;

    public boolean isUploadCompleted(HttpServletRequest servletRequest){
        String uploadUrl = servletRequest.getRequestURI();

        UploadInfo info;

        try {
            info = this.tusFileUploadService.getUploadInfo(uploadUrl);
        } catch (IOException | TusException e) {
            return false;
        }

        if (info == null || info.isUploadInProgress()) return false;

        return true;
    }

    public String getFolderName(HttpServletRequest servletRequest) throws TusException, IOException {
        String uploadUrl = servletRequest.getRequestURI();

        UploadInfo info;

        info = this.tusFileUploadService.getUploadInfo(uploadUrl);
        return info.getId().getOriginalObject().toString();
    }

    public Long getExpirationPeriod(){
        return tusProperties.getExpirationPeriod();
    }
}
