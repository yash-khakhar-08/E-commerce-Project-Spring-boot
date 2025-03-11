/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Yash
 */

@Service
public class FileService {
    
    @Autowired
    public AmazonS3 amazonS3;
    
    @Value("${aws.s3.bucket.products}")
    private String productBucket;
    
    public Boolean uploadFileToS3(MultipartFile file,Integer bucketType) throws IOException{
        
        String fileName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        
        PutObjectRequest putObjectRequest = new PutObjectRequest(productBucket,fileName,inputStream,objectMetadata);
        
        PutObjectResult saveData = amazonS3.putObject(putObjectRequest);
        
        return !ObjectUtils.isEmpty(saveData);
        
    }
    
}
