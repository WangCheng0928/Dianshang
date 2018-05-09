package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {
    private Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);
    public String upload(MultipartFile file,String path){
        String fileName=file.getOriginalFilename();
        //扩展名
        String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName= UUID.randomUUID().toString()+"."+fileExtensionName;
        //使用的是logback里面的方式
        logger.info("开始上传文件，上传文件的文件名:{},上传的路径是:{},新文件名:{}",fileName,path,uploadFileName);

        File fileDir=new File(path);
        //创建目录
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        //创建文件
        File targetFile=new File(path,uploadFileName);

        try {
            file.transferTo(targetFile);
            //文件上传成功
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //已经上传到ftp服务器上
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }
        return targetFile.getName();
    }



}
