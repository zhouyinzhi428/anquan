package com.nanjingtaibai.system.controller;

import com.nanjingtaibai.response.Result;
import com.nanjingtaibai.system.service.AliOssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api
@RestController
@RequestMapping("alioss/")
public class AliOssController {
    @Autowired
    private AliOssService aliOssService;

    @ApiOperation(value = "上传图片文件")
    @PostMapping("uploadFile")
    public Result uploadImgFile(MultipartFile file) throws IOException {
       return aliOssService.upload(file);
        //return Result.ok().data("url",s);
    }

    @ApiOperation(value = "删除替换之后的头像")
    @PostMapping("deleteFile")
    public Result deleteImgFile(String file){
        try {
            String[] split = file.split(".com/");
            System.out.println(split[1]);
            aliOssService.deleteFile(split[1]);
            return Result.ok();
        }catch (Exception e){
            //需要打印错误日志到本地系统中(服务器系统)
            return Result.error();
        }
    }
}
