package com.nihiluis.filestore.minio;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.MinioException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@ApplicationScoped
public class MinioService {

    @Inject
    MinioClient minioClient;

    @ConfigProperty(name = "minio.bucket-name")
    private String bucketName;

    @ConfigProperty(name = "minio.storage-path")
    private String storagePath;

    public InputStream getObjectContent(String objectName) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    public MinioFileWithMetadata getObject(String objectName) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        var stat = minioClient.statObject(
            StatObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build()
        );

        var is = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build()
        );

        return new MinioFileWithMetadata(
            is,
            stat.userMetadata(),
            stat.contentType(),
            stat.size()
        );
    }

    public void putObject(String objectName, File file, String contentType) throws Exception {
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .contentType(contentType != null ? contentType : "application/octet-stream")
                .stream(Files.newInputStream(file.toPath()), file.length(), -1)
                .build()
        );
    }

    public String getPresignedObjectUrl(String objectName) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return minioClient.getPresignedObjectUrl(
            io.minio.GetPresignedObjectUrlArgs.builder()
                .method(io.minio.http.Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(60) // 1 minute expiration
                .build()
        );
    }

    public String getBucketName() {
        return bucketName;
    }
}
