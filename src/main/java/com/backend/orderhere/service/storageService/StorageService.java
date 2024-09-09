package com.backend.orderhere.service.storageService;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String uploadFile(MultipartFile file, String bucketName) throws Exception;

    void deleteFile(String bucketName, String fileName) throws Exception;

}
