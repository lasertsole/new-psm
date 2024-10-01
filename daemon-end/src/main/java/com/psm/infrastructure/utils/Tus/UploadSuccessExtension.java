package com.psm.infrastructure.utils.Tus;

import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.HttpMethod;
import me.desair.tus.server.RequestHandler;
import me.desair.tus.server.RequestValidator;
import me.desair.tus.server.exception.TusException;
import me.desair.tus.server.exception.UploadNotFoundException;
import me.desair.tus.server.upload.UploadInfo;
import me.desair.tus.server.upload.UploadStorageService;
import me.desair.tus.server.util.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class UploadSuccessExtension extends AbstractTusExtension {
    @Autowired
    private TusProperties tusProperties;

    @Override
    public String getName() {
        return "UploadSuccess";
    }

    @Override
    public Collection<HttpMethod> getMinimalSupportedHttpMethods() {
        return Collections.singletonList(HttpMethod.PATCH);
    }

    @Override
    protected void initValidators(List<RequestValidator> requestValidators) {
    }

    @Override
    protected void initRequestHandlers(List<RequestHandler> requestHandlers) {
        requestHandlers.add(new AbstractExtensionRequestHandler() {
            @Override
            protected void appendExtensions(StringBuilder extensionBuilder) {
                addExtension(extensionBuilder, "UploadSuccess");
            }
        });
        requestHandlers.add(new AbstractRequestHandler() {

            @Override
            public boolean supports(HttpMethod method) {
                return HttpMethod.PATCH.equals(method);
            }

            @Override
            public void process(HttpMethod method, TusServletRequest servletRequest, TusServletResponse servletResponse, UploadStorageService uploadStorageService, String ownerKey) throws IOException, TusException, UploadNotFoundException {
                String uploadUrl = servletRequest.getRequestURI();
                UploadInfo info = uploadStorageService.getUploadInfo(uploadUrl, ownerKey);

                //判断文件是否上传成功,未上传完成则返回
                if (info == null || info.isUploadInProgress()) return;

                //上传完成后，将上传完成的data文件重命名为原文件
                Path dirPath = tusProperties.getTusDataPath().resolve("uploads").resolve(info.getId().getOriginalObject().toString());

                Path filePath = dirPath.resolve(info.getFileName());

                try (InputStream uploadedBytes = uploadStorageService.getUploadedBytes(uploadUrl, null)) {
                    FileUtils.copyToFile(uploadedBytes, filePath.toFile());
                } catch (Exception e) {
                    try {
                        uploadStorageService.terminateUpload(info);
                        return;
                    } catch (IOException | TusException ee) {
                        log.error("delete upload", ee);
                        return;
                    }
                }

                //将本地文件所在的文件夹路径保存在上下文
                TargetFolderPathContextHolder.setTargetFolderPath(dirPath);
            }
        });
    }
}
