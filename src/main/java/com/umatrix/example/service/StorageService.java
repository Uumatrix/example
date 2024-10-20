package com.umatrix.example.service;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class StorageService {

    private final AmazonS3 client;

    @Value("${aws.bucket.name}")
    private String bucketName;

    public StorageService(AmazonS3 client) {
        this.client = client;
    }


    public String upload(MultipartFile file, String folder) {
        File tempFile = null;
        try {
            String originalName = file.getOriginalFilename();
            if (originalName == null) {
                throw new RuntimeException("File must have an original name");
            }
            tempFile = convertMultipartfileToFile(file);
            String s3Key = folder + originalName;
            client.putObject(bucketName, s3Key, tempFile);
            return s3Key;
        } catch (AmazonServiceException e) {
            throw new RuntimeException(e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                if (!tempFile.delete()) {
                    System.out.println("Failed to delete temporary file: " + tempFile.getAbsolutePath());
                }
            }
        }
    }

    public InputStream download(String s3Key) {
        S3Object object = client.getObject(bucketName, s3Key);
        return object.getObjectContent();
    }

    public void delete(String s3Key) {
        try {
            client.deleteObject(bucketName, s3Key);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public File convertMultipartfileToFile(MultipartFile file) {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return convFile;
    }
}
