package com.backend.orderhere.service.storageService;

import io.minio.*;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@Profile("local")
public class MinioStorageService implements StorageService {

  private final MinioClient minioClient;

  @Value("${storage.bucketName}")
  private String bucketName;

  public MinioStorageService() throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
    this.minioClient = MinioClient.builder()
            .endpoint("http://127.0.0.1:9000")
            .credentials("minioadmin", "minioadmin")
            .build();
  }

  @PostConstruct
  public void initialize() throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
    createBucket(bucketName);
  }

  public void createBucket(String bucketName) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
    boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    if (!found) {
      minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

      String policy = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}";
      minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policy).build());
    }
  }

  @Override
  public String uploadFile(MultipartFile file, String bucketName) throws Exception {
    String originalName = file.getOriginalFilename();
    String uniqueFileName = UUID.randomUUID() + "-" + originalName;
    try (InputStream inputStream = file.getInputStream()) {
      minioClient.putObject(
              PutObjectArgs.builder().bucket(bucketName).object(uniqueFileName)
                      .stream(inputStream, file.getSize(), -1)
                      .contentType("image/jpeg")
                      .build());

      return "http://127.0.0.1:9000/" + bucketName + "/" + uniqueFileName;
    }
  }

  @Override
  public void deleteFile(String bucketName, String imageUrl) throws Exception {
    try {
      String baseUrl = "http://127.0.0.1:9000/" + bucketName + "/";
      String fileName = imageUrl.replace(baseUrl, "");

      minioClient.removeObject(
              RemoveObjectArgs.builder()
                      .bucket(bucketName)
                      .object(fileName)
                      .build());
    } catch (MinioException e) {
      throw new Exception("Error occurred while trying to delete file: " + imageUrl, e);
    }
  }

}
