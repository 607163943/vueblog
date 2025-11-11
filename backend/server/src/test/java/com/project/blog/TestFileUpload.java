package com.project.blog;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
public class TestFileUpload {
    private MinioClient minioClient;
    @BeforeEach
    public void initMinioClient() {
        minioClient = MinioClient.builder()
                .endpoint("http://localhost:19000/")
                .credentials("minioadmin", "minioadmin")
                .build();
    }

    @Test
    public void test() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 查看目标桶是否存在
        System.out.println(minioClient.bucketExists(BucketExistsArgs.builder().bucket("vueblog-img").build()));
    }
}
