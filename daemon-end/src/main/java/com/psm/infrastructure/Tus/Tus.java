package com.psm.infrastructure.Tus;

import com.psm.infrastructure.Tus.properties.TusProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import me.desair.tus.server.upload.UploadInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
@Component
public class Tus {
    @Autowired
    private TusProperties tusProperties;

    @Autowired
    private TusFileUploadService tusFileUploadService;

    public boolean isUploadCompleted(HttpServletRequest servletRequest){
        String uploadUrl = servletRequest.getRequestURI();

        UploadInfo info;

        try {
            info = tusFileUploadService.getUploadInfo(uploadUrl);
        } catch (IOException | TusException e) {
            return false;
        }

        if (info == null || info.isUploadInProgress()) return false;

        return true;
    }

    public String getFolderName(HttpServletRequest servletRequest) throws TusException, IOException {
        String uploadUrl = servletRequest.getRequestURI();

        UploadInfo info = tusFileUploadService.getUploadInfo(uploadUrl);
        return info.getId().getOriginalObject().toString();
    }

    public String getFolderName(String fullName) {
       return fullName.split("/")[0];
    }

    public String getFileName(HttpServletRequest servletRequest) throws TusException, IOException {
        String uploadUrl = servletRequest.getRequestURI();
        UploadInfo info = tusFileUploadService.getUploadInfo(uploadUrl);

        return info.getFileName();
    }

    public String getFileName(String fullName) {
        return fullName.split("/")[1];
    }

    public String getFullName(HttpServletRequest servletRequest) throws TusException, IOException {
        String uploadUrl = servletRequest.getRequestURI();
        UploadInfo info = tusFileUploadService.getUploadInfo(uploadUrl);

        return info.getId().getOriginalObject().toString() + "/" + info.getFileName();
    }

    public String getStoragePathName() {
        return tusProperties.getTusDataPath().toString();
    }

    public String getAbsolutePathName() {
        return Paths.get(getStoragePathName()).toAbsolutePath().normalize().toString();
    }

    public String getAbsoluteFilePathName(String fullName) {
        return (getAbsolutePathName() + "/uploads/" + fullName).toString();
    }

    public Long getExpirationPeriod(){
        return tusProperties.getExpirationPeriod();
    }

    public Long getFileSize(String fullName) {
        // 获取文件绝对路径
        String absoluteFilePathName = getAbsoluteFilePathName(fullName);

        // 创建 File 对象
        File file = new File(absoluteFilePathName);

        // 检查文件是否存在
        if (!file.exists() || !file.isFile()) {
            throw new RuntimeException("文件不存在或无法访问");
        }

        return file.length();
    }
}
