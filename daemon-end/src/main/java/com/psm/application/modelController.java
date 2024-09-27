package com.psm.application;

import com.psm.infrastructure.utils.VO.ResponseVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import me.desair.tus.server.upload.UploadInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/models")
public class modelController {
    @Autowired
    TusFileUploadService tusFileUploadService;

    @PostMapping("/upload")
    public ResponseVO uploadModel(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse){
        try{
            tusFileUploadService.process(servletRequest, servletResponse);
        }catch (Exception e){
            return new ResponseVO(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        //access response header Location,Upload-Offset,Upload-length
        servletResponse.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Location,Upload-Offset,Upload-Length");
        String uploadUrl = servletRequest.getRequestURI();
        log.info("uploadUrl:{}", uploadUrl);
        UploadInfo info;
        try {
            info = this.tusFileUploadService.getUploadInfo(uploadUrl);
        } catch (IOException | TusException e) {
            log.error("get upload info", e);
            return new ResponseVO(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseVO.ok("上传成功");
    }
}
