package com.psm.utils.OSS;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class UploadOSSUtil {
    @Autowired
    OSSUtilsProperties ossUtilsProperties;

    public String upload(MultipartFile multipartFile) throws IOException {
        String endpoint = ossUtilsProperties.getEndpoint();
        String accessKeyId = ossUtilsProperties.getAccessKeyId();
        String accessKeySecret = ossUtilsProperties.getAccessKeySecret();
        String bucketName = ossUtilsProperties.getBucketName();

        // 获取上传文件的输入流
        InputStream inputStream = multipartFile.getInputStream();

        // 避免文件覆盖
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) + multipartFile.getOriginalFilename();

        //上传文件到OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);

        //文件访问路径
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;

        //关闭OSSClient
        ossClient.shutdown();
        return url;//返回文件访问路径
    }
}
