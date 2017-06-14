package com.eat.configs.integration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Configuration {

    private final String ACCESS_KEY_ID = "AKIAI7CJWZYAS6QAL5SA";
    private final String SECRET_ACCESS_KEY = "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";

    @Bean(name = "amazonS3")
    public AmazonS3 getAmazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY);
        AmazonS3 amazonS3 = new AmazonS3Client(credentials);
        return amazonS3;
    }

}