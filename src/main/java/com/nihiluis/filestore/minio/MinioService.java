package com.nihiluis.filestore.minio;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.MinioException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

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

    public void putObject(String objectName, java.io.File file) throws Exception {
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .contentType(Files.probeContentType(file.toPath()))
                .stream(Files.newInputStream(file.toPath()), file.length(), -1)
                .build()
        );
    }

    // need a method here that generates a file url to download from minio AI!

    public String getBucketName() {
        return bucketName;
    }
}
