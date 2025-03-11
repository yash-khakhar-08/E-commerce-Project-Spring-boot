/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Yash
 */

@Configuration
public class AwsConfig {
    
    @Value("${aws.access.key}")
    private String accessKey;
    
    @Value("${aws.secret.key}") 
    private String secretKey; 
    
    @Value("${aws.region}") 
    private String region;
    
    @Bean
    public AmazonS3 amazonS3(){
        
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
        
        return AmazonS3Client.builder().
                withRegion(region).
                withCredentials(new AWSStaticCredentialsProvider(credentials)).
                build();
        
    }
    
}
