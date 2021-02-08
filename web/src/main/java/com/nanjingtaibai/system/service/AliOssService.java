package com.nanjingtaibai.system.service;

import com.nanjingtaibai.response.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AliOssService {
    /**
     * 创建存储空间
     */
    void createBucket();

    /**
     * 上传文件
     * @param file 文件对象
     * @return
     */
    Result upload(MultipartFile file) throws IOException;

    /**
     * 下载文件
     * @throws IOException
     */
    void download(String fileName) throws IOException;

    /**
     * 列举文件
     */
    void listFile();

    /**
     * 删除文件
     */
    void deleteFile(String fileName);

    void doesObjectExist(String fileName);
}
