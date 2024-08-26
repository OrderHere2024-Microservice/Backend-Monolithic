package com.backend.orderhere.service;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class MinioService {

  private final MinioClient minioClient;

  public MinioService() {
    this.minioClient = MinioClient.builder()
            .endpoint("http://127.0.0.1:9000")
            .credentials("minioadmin", "minioadmin")
            .build();
  }

  public void createBucket(String bucketName) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
    boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    if (!found) {
      minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

      String policy = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}";
      minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policy).build());
    }
  }

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
}
