package com.psm.application;

import com.psm.infrastructure.utils.VO.ResponseVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import me.desair.tus.server.upload.UploadInfo;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/models")
public class modelController {
    @Autowired
    TusFileUploadService tusFileUploadService;

    private final Path tusDataPath = Paths.get("..", "uploads");

    @RequestMapping(value = {"/upload/**"}, method = {RequestMethod.POST, RequestMethod.PATCH, RequestMethod.HEAD,
            RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.GET})
    @CrossOrigin(exposedHeaders = {"Location", "Upload-Offset", "Upload-Length"})//暴露header
    public ResponseVO uploadModel(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse){
        try{
            tusFileUploadService.process(servletRequest, servletResponse);
        }catch (Exception e){
            return new ResponseVO(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        //access response header Location,Upload-Offset,Upload-length
        servletResponse.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Location,Upload-Offset,Upload-Length");
        String uploadUrl = servletRequest.getRequestURI();

        UploadInfo info;
        try {
            info = this.tusFileUploadService.getUploadInfo(uploadUrl);
        } catch (IOException | TusException e) {
            return new ResponseVO(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        
        //上传完成后，将上传完成的data文件重命名为原文件
        if (info != null && !info.isUploadInProgress()) {
            Path parPath = tusDataPath.resolve("uploads").resolve(info.getId().getOriginalObject().toString());

            Path filePath = parPath.resolve(info.getFileName());
            log.info(filePath.toString());

            try (InputStream uploadedBytes = tusFileUploadService.getUploadedBytes(uploadUrl, null)) {
                FileUtils.copyToFile(uploadedBytes, filePath.toFile());
            } catch (Exception e) {
                try {
                    this.tusFileUploadService.deleteUpload(uploadUrl);
                } catch (IOException | TusException ee) {
                    log.error("delete upload", ee);
                    return new ResponseVO(HttpStatus.BAD_REQUEST, "删除错误的文件失败");
                }
                return new ResponseVO(HttpStatus.BAD_REQUEST, "重命名文件失败");
            }
        }

        return ResponseVO.ok("上传成功");
    }
}
