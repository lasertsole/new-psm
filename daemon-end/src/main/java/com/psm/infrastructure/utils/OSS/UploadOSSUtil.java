package com.psm.infrastructure.utils.OSS;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.aliyun.oss.model.*;
import com.aliyun.oss.OSSException;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class UploadOSSUtil {
    // 导入OSS配置
    @Autowired
    OSSUtilProperties ossUtilsProperties;

    /**
     * 分片上传(支持断点继传)
     *
     * @param multipartFile
     * @param folderPath
     * @return String
     * @throws Exception
     */
    public String multipartUpload(MultipartFile multipartFile, String folderPath) throws Exception {

        String endpoint = ossUtilsProperties.getEndpoint();
        String accessKeyId = ossUtilsProperties.getAccessKeyId();
        String accessKeySecret = ossUtilsProperties.getAccessKeySecret();
        String bucketName = ossUtilsProperties.getBucketName();

        // 获取源文件名
        String oriFileName = multipartFile.getOriginalFilename();

        // 获取文件扩展名
        int dotIndex = multipartFile.getOriginalFilename().lastIndexOf('.');
        String fileExtension = oriFileName.substring(dotIndex);

        // 文件在OSS中的名字
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))//随机化名字
                + "-"
                + UUID.randomUUID().toString().replaceAll("-","")//随机化名字
                + fileExtension;//获取扩展名

        // 构建完整的文件路径
        String fullFilePath;
        if(StringUtils.isBlank(folderPath)){
            fullFilePath = fileName;
        }
        else{
            fullFilePath = folderPath + "/" + fileName;
        }

        // 获取MultipartFile的输入流
        InputStream inputStream = multipartFile.getInputStream();

        // 创建一个临时文件用于上传
        Path tempFilePath = Files.createTempFile(fileName, null);
        Files.copy(inputStream, tempFilePath, StandardCopyOption.REPLACE_EXISTING);

        //开启oss客户端
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            ObjectMetadata meta = new ObjectMetadata();
            // 指定上传的内容类型。
             meta.setContentType(multipartFile.getContentType());

            // 文件上传时设置访问权限ACL。
            // meta.setObjectAcl(CannedAccessControlList.Private);

            // 通过UploadFileRequest设置多个参数。
            // 依次填写Bucket名称（例如examplebucket）以及Object完整路径（例如exampledir/exampleobject.txt），Object完整路径中不能包含Bucket名称。
            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, fullFilePath);

            // 通过UploadFileRequest设置单个参数。
            // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
            uploadFileRequest.setUploadFile(tempFilePath.toFile().getAbsolutePath());
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
            throw new RuntimeException("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason."
                    + "Error Message:" + oe.getErrorMessage()
                    + "Error Code:" + oe.getErrorCode()
                    + "Request ID:" + oe.getRequestId()
                    + "Host ID:" + oe.getHostId());
        } catch (Throwable ce) {
            throw new RuntimeException("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
        } finally {
            // 关闭OSSClient。
            if (ossClient != null) {
                ossClient.shutdown();
            }
            // 清理临时文件
            Files.deleteIfExists(tempFilePath);
        }
    };

    /**
     * 删除单个文件
     * @param filePath
     * @return Boolean
     * @throws Exception
     */
    public Boolean deleteFile(String filePath) throws Exception {
        String endpoint = ossUtilsProperties.getEndpoint();
        String accessKeyId = ossUtilsProperties.getAccessKeyId();
        String accessKeySecret = ossUtilsProperties.getAccessKeySecret();
        String bucketName = ossUtilsProperties.getBucketName();

        //开启oss客户端
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(bucketName, filePath);

            return true;
        } catch (OSSException oe) {
            throw new RuntimeException("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason."
                    + "Error Message:" + oe.getErrorMessage()
                    + "Error Code:" + oe.getErrorCode()
                    + "Request ID:" + oe.getRequestId()
                    + "Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            throw new RuntimeException("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network."
                    + "Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    };

    /**
     * 根据文件完整路径删除文件
     * @param fullFilePath
     * @param folderPath
     * @return Boolean
     * @throws Exception
     */
    public Boolean deleteFileByFullUrl(String fullFilePath, String folderPath) throws Exception {
        //https://new-psm.oss-cn-guangzhou.aliyuncs.com/users/avatars/2024-09-16-23-15-38-c1b398860d1740da8acf54bd60f63aba.webp
        try{
            URL url = new URL(fullFilePath);
            // 获取协议
            String protocol = url.getProtocol();

            // 获取主机名
            String host = url.getHost();

            // 获取路径
            String path = url.getPath();

            // 获取路径
            String endpoint = ossUtilsProperties.getEndpoint();//endpoint.split("//")[0]

            // 获取bucketName
            String bucketName = ossUtilsProperties.getBucketName();

            // 拼接templateUrl
            String templateUrl = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1];

            // 拼接fullUrlPrefix
            StringBuilder fullUrlPrefix = new StringBuilder();
            fullUrlPrefix.append(protocol).append("://").append(host);

            // 判断是否为模板url
            if (!templateUrl.equals(fullUrlPrefix.toString())){
                return false;
            }

            // 去掉开头的斜杠
            String trimmedPath = path.startsWith("/") ? path.substring(1) : path;

            // 分割路径
            String[] pathParts = trimmedPath.split("/");

            // 取前两个部分并重新拼接
            StringBuilder extractedPath = new StringBuilder();
            for (int i = 0; i < 2; i++) {
                if (i > 0) {
                    extractedPath.append("/");
                }
                extractedPath.append(pathParts[i]);
            }

            // 添加开头的斜杠
            String finalPath = "/" + extractedPath.toString();

            if (!finalPath.equals("/"+folderPath)){
                return false;
            }

            // 删除文件
            String accessKeyId = ossUtilsProperties.getAccessKeyId();
            String accessKeySecret = ossUtilsProperties.getAccessKeySecret();
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);//开启oss客户端
            ossClient.deleteObject(bucketName, trimmedPath);// 删除文件或目录。如果要删除目录，目录必须为空。

            return true;
        }
        catch (Exception e){
            return false;
        }
        
    };

    /**
     * 批量删除文件
     * @param deletedFiles
     * @return Boolean
     * @throws Exception
     */
    public Boolean deletedBatchFiles(List<String> deletedFiles) throws Exception {
        String endpoint = ossUtilsProperties.getEndpoint();
        String accessKeyId = ossUtilsProperties.getAccessKeyId();
        String accessKeySecret = ossUtilsProperties.getAccessKeySecret();
        String bucketName = ossUtilsProperties.getBucketName();

        //开启oss客户端
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(deletedFiles).withEncodingType("url"));

            List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
            try {
                for(String obj : deletedObjects) {
                    String deleteObj =  URLDecoder.decode(obj, "UTF-8");
                    System.out.println(deleteObj);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return true;
        } catch (OSSException oe) {
            throw new RuntimeException("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason."
                    + "Error Message:" + oe.getErrorMessage()
                    + "Error Code:" + oe.getErrorCode()
                    + "Request ID:" + oe.getRequestId()
                    + "Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            throw new RuntimeException("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network."
                    + "Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    };
}
