package com.nanjingtaibai.system.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.nanjingtaibai.response.Result;
import com.nanjingtaibai.system.entity.OssEntity;
import com.nanjingtaibai.system.service.AliOssService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.UUID;

@Service
public class AliOssServiceImpl implements AliOssService, InitializingBean {

    @Autowired
    private OssEntity ossEntity;

    /**
     * Endpoint以杭州为例，其它Region请按实际情况填写。
     */
    private String endpoint;
    /**
     * 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
     */
    private String accessKeyId;
    private String accessKeySecret;
    /**
     * <yourObjectName>从OSS下载文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
     */
    private String bucketName;

    @Override
    public void afterPropertiesSet() throws Exception {
        endpoint=ossEntity.getEndpoint();
        accessKeyId=ossEntity.getAccessKeyId();
        accessKeySecret=ossEntity.getAccessKeySecret();
        bucketName=ossEntity.getBucketName();
    }

    /**
     * 创建存储空间
     */
    @Override
    public void createBucket() {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        if(ossClient.doesBucketExist(bucketName)){
            throw new RuntimeException(bucketName+"bucketName已经存在");
        }

        // 创建存储空间。
        ossClient.createBucket(bucketName);

        // 关闭OSSClient。
        ossClient.shutdown();

    }

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return
     */
    @Override
    public Result upload(MultipartFile file) throws IOException {
        String uploadUrl=null;
        String original=null;
        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            if(!ossClient.doesBucketExist(bucketName))
            {
                ossClient.createBucket(bucketName);
                ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicReadWrite);
            }

            //获取上传的文件流
            InputStream inputStream=file.getInputStream();

            //构建日期的文件夹路径
            String datePath = new DateTime().toString("yyyy/MM/dd");

            //获取上传文件的全名称
            original = file.getOriginalFilename().replaceAll("/","").replaceAll("_","");
            String realName=original.substring(0,original.lastIndexOf("."));

            //获取UUID
            String fileName=realName+"_"+UUID.randomUUID().toString().replaceAll("-","");

            //获取上传文件的扩展名
            String fileType = original.substring(original.lastIndexOf("."));

            //拼接文件名称
            String newName=fileName+fileType;

            //生成文件夹
            fileName=datePath+"/"+newName;

            //如果要实现图片预览效果，一定要设置以下几点
            //1、设置文件的ACL权限。公共读写。
            //2\一定要设置文本类型
            ObjectMetadata objectMetadata=new ObjectMetadata();
            objectMetadata.setObjectAcl(CannedAccessControlList.PublicRead);
            objectMetadata.setContentType(getcontentType(fileType));
            //每次上传的文件名字肯定是不能相同的。
            ossClient.putObject(bucketName, fileName,inputStream,objectMetadata);

            // 关闭OSSClient。
            ossClient.shutdown();

            //默认十年不过期
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);

            //bucket名称  文件名   过期时间
            uploadUrl = ossClient.generatePresignedUrl(bucketName, fileName, expiration).toString();

            //获取url地址
            //uploadUrl = "https://" + bucketName + "." + endPoint + "/" + fileName;

        }catch (Exception e){
            e.printStackTrace();
        }
        return  Result.ok().data("name",original).data("url",uploadUrl.substring(0, uploadUrl.indexOf("?")));
        //return  uploadUrl;
    }

    /**
     * 下载文件
     *
     * @param fileName
     * @throws IOException
     */
    @Override
    public void download(String fileName) throws IOException {
        // <yourObjectName>从OSS下载文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        String objectName = fileName;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元信息。
        OSSObject ossObject = ossClient.getObject(bucketName, objectName);
        // 调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容。
        InputStream content = ossObject.getObjectContent();
        if (content != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                System.out.println("\n" + line);
            }
            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            content.close();
        }
        // 关闭OSSClient。
        ossClient.shutdown();

    }

    /**
     * 列举文件
     */
    @Override
    public void listFile() {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // ossClient.listObjects返回ObjectListing实例，包含此次listObject请求的返回结果。
        ObjectListing objectListing = ossClient.listObjects(bucketName);
        // objectListing.getObjectSummaries获取所有文件的描述信息。
        for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(" - " + objectSummary.getKey() + "  " +
                    "(size = " + objectSummary.getSize() + ")");
        }

        // 关闭OSSClient。
        ossClient.shutdown();
    }



    /**
     * 删除文件
     *
     * @param fileName
     */
    @Override
    public void deleteFile(String fileName) {
// <yourObjectName>从OSS下载文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        String objectName = fileName;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件。
        ossClient.deleteObject(bucketName, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Override
    public void doesObjectExist(String fileName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        boolean found = ossClient.doesObjectExist(bucketName, fileName);
        System.out.println(found);
    }

    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xls") ||
                FilenameExtension.equalsIgnoreCase(".xlsx")) {
            return "application/vnd.ms-excel";
        }
        if (FilenameExtension.equalsIgnoreCase(".pdf")) {
            return "application/pdf";
        }
        if (FilenameExtension.equalsIgnoreCase(".mpeg") ||
                FilenameExtension.equalsIgnoreCase(".mpg") ||
                FilenameExtension.equalsIgnoreCase(".mpe") ||
                FilenameExtension.equalsIgnoreCase(".mp3")) {
            return "application/mpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".rmvb")) {
            return "application/vnd.rn-realvideo";
        }
        if (FilenameExtension.equalsIgnoreCase(".swf") ||
                FilenameExtension.equalsIgnoreCase(".cab") ) {
            return "application/x-shockwave-flash";
        }
        if (FilenameExtension.equalsIgnoreCase(".rar") ||
                FilenameExtension.equalsIgnoreCase(".zip") ) {
            return "application/octet-stream";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpg";
    }


}
