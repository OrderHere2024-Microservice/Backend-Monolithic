package com.backend.orderhere.service.storageService;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Profile("development")
public class S3StorageService {
    /*
    *  Probably have to re-write this class
    *
    * */
    private final AmazonS3 s3client;

    public S3StorageService(@Value("${aws.accessKey}") String accessKey,
                            @Value("${aws.secretKey}") String secretKey,
                            @Value("${aws.region}") String region) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        this.s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.fromName(region))
                .build();
    }

    public PutObjectResult uploadFile(String bucketName, String key, File file) {
        return s3client.putObject(bucketName, key, file);
    }

    public void deleteFile(String bucketName, String key) {
        try {
            s3client.deleteObject(bucketName, key);
            System.out.println("File deleted successfully.");
        } catch (AmazonServiceException e) {
            System.err.println("Failed to delete the file from S3. " + e.getErrorMessage());
            throw e;
        } catch (AmazonClientException e) {
            System.err.println("Client encountered an error while deleting the file. " + e.getMessage());
            throw e;
        }
    }

    public File getFile(String bucketName, String key, String destinationPath) throws IOException {
        S3Object s3Object = s3client.getObject(bucketName, key);
        InputStream inputStream = s3Object.getObjectContent();
        File destinationFile = new File(destinationPath);
        try (FileOutputStream outputStream = new FileOutputStream(destinationFile)) {
            byte[] readBuffer = new byte[1024];
            int readLength;
            while ((readLength = inputStream.read(readBuffer)) > 0) {
                outputStream.write(readBuffer, 0, readLength);
            }
        } catch (IOException e) {
            throw new IOException("Error occurred while downloading the file from S3", e);
        }
        return destinationFile;
    }
}
