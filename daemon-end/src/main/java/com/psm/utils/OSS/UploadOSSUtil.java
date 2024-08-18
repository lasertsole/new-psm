package com.psm.utils.OSS;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.aliyun.oss.model.*;
import com.aliyun.oss.OSSException;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@Component
public class UploadOSSUtil {
    @Autowired
    OSSUtilsProperties ossUtilsProperties;

    /**
     * 简单上传
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public String upload(MultipartFile multipartFile) throws IOException {
        return upload(multipartFile, "");
    }

    /**
     * 简单上传
     *
     * @param multipartFile
     * @param folderPath
     * @return
     * @throws IOException
     */
    public String upload(MultipartFile multipartFile, String folderPath) throws IOException {
        String endpoint = ossUtilsProperties.getEndpoint();
        String accessKeyId = ossUtilsProperties.getAccessKeyId();
        String accessKeySecret = ossUtilsProperties.getAccessKeySecret();
        String bucketName = ossUtilsProperties.getBucketName();

        // 获取上传文件的输入流
        InputStream inputStream = multipartFile.getInputStream();

        // 避免文件覆盖
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) + multipartFile.getOriginalFilename();

        // 构建完整的文件路径
        String fullFilePath;
        if(StringUtils.isBlank(folderPath)){
            fullFilePath = fileName;
        }
        else{
            fullFilePath = folderPath + "/" + fileName;
        }

        //上传文件到OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fullFilePath, inputStream);

        //文件访问路径
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] +"/" + fullFilePath;

        //关闭OSSClient
        ossClient.shutdown();
        return url;//返回文件访问路径
    }

    /**
     * 分片上传(支持断点继传)
     *
     * @param multipartFile
     * @param folderPath
     * @return
     * @throws Exception
     */
    public String multipartUpload(MultipartFile multipartFile, String folderPath) throws Exception {

        String endpoint = ossUtilsProperties.getEndpoint();
        String accessKeyId = ossUtilsProperties.getAccessKeyId();
        String accessKeySecret = ossUtilsProperties.getAccessKeySecret();
        String bucketName = ossUtilsProperties.getBucketName();

        // 文件名
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))
                + UUID.randomUUID().toString().replaceAll("-","");

        // 获取上传文件的输入流
        // 创建一个临时文件
        File tempFile = File.createTempFile(fileName, null);

        // 将multipartFile的内容转移到临时文件
        multipartFile.transferTo(tempFile);

        // 临时文件路径
        String tempFilePath = tempFile.getAbsolutePath();

        // 构建完整的文件路径
        String fullFilePath;
        if(StringUtils.isBlank(folderPath)){
            fullFilePath = fileName;
        }
        else{
            fullFilePath = folderPath + "/" + fileName;
        }

        //上传文件到OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            ObjectMetadata meta = new ObjectMetadata();
            // 指定上传的内容类型。
            // meta.setContentType("text/plain");

            // 文件上传时设置访问权限ACL。
            // meta.setObjectAcl(CannedAccessControlList.Private);

            // 通过UploadFileRequest设置多个参数。
            // 依次填写Bucket名称（例如examplebucket）以及Object完整路径（例如exampledir/exampleobject.txt），Object完整路径中不能包含Bucket名称。
            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, fullFilePath);

            // 通过UploadFileRequest设置单个参数。
            // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
            uploadFileRequest.setUploadFile(tempFilePath);
            // 指定上传并发线程数，默认值为1。
            uploadFileRequest.setTaskNum(5);
            // 指定上传的分片大小，单位为字节，取值范围为100 KB~5 GB。默认值为100 KB。
            uploadFileRequest.setPartSize(1 * 1024 * 1024);
            // 开启断点续传，默认关闭。
            uploadFileRequest.setEnableCheckpoint(true);

            // 记录本地分片上传结果的文件。上传过程中的进度信息会保存在该文件中，如果某一分片上传失败，再次上传时会根据文件中记录的点继续上传。上传完成后，该文件会被删除。
            // 如果未设置该值，默认与待上传的本地文件同路径，名称为${uploadFile}.ucp。
            //uploadFileRequest.setCheckpointFile("yourCheckpointFile");

            // 文件的元数据。
            uploadFileRequest.setObjectMetadata(meta);
            // 设置上传回调，参数为Callback类型。
            //uploadFileRequest.setCallback("yourCallbackEvent");

            // 断点续传上传。
            ossClient.uploadFile(uploadFileRequest);

            //文件访问路径
            String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] +"/" + fullFilePath;

            return url;

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());

            return null;
        } catch (Throwable ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());

            return null;
        } finally {
            // 关闭OSSClient。
            if (ossClient != null) {
                ossClient.shutdown();
            }
            // 删除临时文件
            tempFile.delete();
        }
    }
}
