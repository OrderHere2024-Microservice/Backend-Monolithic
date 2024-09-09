package com.backend.orderhere.service.storageService;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Profile("!local")
public class S3StorageService implements StorageService {

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

    @Override
    public String uploadFile(MultipartFile file, String bucketName) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID() + "-" + originalFileName;

        // Convert MultipartFile to File
        File convertedFile = convertMultipartFileToFile(file);
        s3client.putObject(bucketName, uniqueFileName, convertedFile);

        // Clean up temporary file after upload
        convertedFile.delete();

        return s3client.getUrl(bucketName, uniqueFileName).toString();
    }

    @Override
    public void deleteFile(String bucketName, String fileName) {
        try {
            s3client.deleteObject(bucketName, fileName);
            System.out.println("File deleted successfully.");
        } catch (AmazonServiceException e) {
            throw new RuntimeException("Failed to delete file from S3. " + e.getErrorMessage(), e);
        } catch (AmazonClientException e) {
            throw new RuntimeException("Client encountered an error while deleting the file. " + e.getMessage(), e);
        }
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
