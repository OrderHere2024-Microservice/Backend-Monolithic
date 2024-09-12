package com.backend.orderhere.service;

import com.backend.orderhere.service.storageService.S3StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class S3StorageServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3StorageService s3StorageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(s3Client.utilities()).thenReturn(mock(S3Utilities.class));
    }

    @Test
    void testDeleteFileThrowsException() {
        String bucketName = "test-bucket";
        String imageUrl = "https://s3.amazonaws.com/test-bucket/uuid-test-image.jpg";

        doThrow(S3Exception.builder().message("S3 error").build())
                .when(s3Client).deleteObject(any(DeleteObjectRequest.class));

        Exception exception = assertThrows(Exception.class, () -> {
            s3StorageService.deleteFile(bucketName, imageUrl);
        });

        assertEquals("Error occurred while trying to delete file: " + imageUrl, exception.getMessage());
    }
}