package com.psm.infrastructure.utils.Tus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.HttpMethod;
import me.desair.tus.server.RequestHandler;
import me.desair.tus.server.creation.*;
import me.desair.tus.server.upload.UploadInfo;
import me.desair.tus.server.upload.UploadStorageService;
import me.desair.tus.server.util.TusServletRequest;
import me.desair.tus.server.util.TusServletResponse;
import me.desair.tus.server.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class UploadCreationExtension extends CreationExtension {
    @Autowired
    private TusProperties tusProperties;

    private UploadInfo buildUploadInfo(HttpServletRequest servletRequest) {
        UploadInfo info = new UploadInfo(servletRequest);
        Long length = Utils.getLongHeader(servletRequest, "Upload-Length");
        if (length != null) {
            info.setLength(length);
        }

        String metadata = Utils.getHeader(servletRequest, "Upload-Metadata");
        if (StringUtils.isNotBlank(metadata)) {
            info.setEncodedMetadata(metadata);
        }

        return info;
    }

    @Override
    protected void initRequestHandlers(List<RequestHandler> requestHandlers) {
        requestHandlers.add(new CreationHeadRequestHandler());
        requestHandlers.add(new CreationPatchRequestHandler());
        requestHandlers.add(new CreationPostRequestHandler(){
            //解决通过网关转发的url和后端获取的url不一致，导致无法正常上传
            @Override
            public void process(HttpMethod method, TusServletRequest servletRequest, TusServletResponse servletResponse, UploadStorageService uploadStorageService, String ownerKey) throws IOException {
                UploadInfo info = buildUploadInfo(servletRequest);

                info = uploadStorageService.create(info, ownerKey);
                //We've already validated that the current request URL matches our upload URL so we can safely use it.
                String uploadUri = servletRequest.getRequestURI();
                //It's important to return relative UPLOAD URLs in the Location header in order to support HTTPS proxies
                //that sit in front of the web app
                String servletPath = tusProperties.getProtocol() + "://" + tusProperties.getAddress() + ":" + tusProperties.getPort();
                String url = servletPath + uploadUri + (StringUtils.endsWith(uploadUri, "/") ? "" : "/") + info.getId();
                servletResponse.setHeader("Location", url);
                servletResponse.setStatus(HttpServletResponse.SC_CREATED);
                log.info("Created upload with ID {} at {} for ip address {} with location {}", new Object[]{info.getId(), info.getCreationTimestamp(), info.getCreatorIpAddresses(), url});
            }
        });
        requestHandlers.add(new CreationOptionsRequestHandler());
    }
}
