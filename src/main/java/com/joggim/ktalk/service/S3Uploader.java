package com.joggim.ktalk.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.joggim.ktalk.common.exception.CustomException;
import com.joggim.ktalk.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.UUID;

@Service
public class S3Uploader {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadMp3(InputStream inputStream, String folder) {
        String filename = folder + "/" + UUID.randomUUID() + ".mp3";

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("audio/mpeg");

        try {
            amazonS3.putObject(bucketName, filename, inputStream, metadata);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return amazonS3.getUrl(bucketName, filename).toString();
    }
}

