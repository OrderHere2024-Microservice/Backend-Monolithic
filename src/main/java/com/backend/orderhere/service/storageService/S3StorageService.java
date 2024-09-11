package com.backend.orderhere.service.storageService;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

@Service
@Profile("!local")
public class S3StorageService implements StorageService {

    private final S3Client s3Client;

    public S3StorageService() {
        this.s3Client = S3Client.builder()
                .credentialsProvider(ProfileCredentialsProvider.create())
                .endpointOverride(URI.create("https://s3.amazonaws.com"))
                .region(Region.AP_SOUTHEAST_2)  // Define your region
                .build();
    }

    @Override
    public String uploadFile(MultipartFile file, String bucketName) throws IOException {
        String originalName = file.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID() + "-" + originalName;

        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueFileName)
                    .contentType("image/jpeg")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));

            return s3Client.utilities().getUrl(GetUrlRequest.builder().bucket(bucketName).key(uniqueFileName).build()).toString();
        }
    }

    @Override
    public void deleteFile(String bucketName, String imageUrl) throws Exception {
        try {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            throw new Exception("Error occurred while trying to delete file: " + imageUrl, e);
        }
    }
}