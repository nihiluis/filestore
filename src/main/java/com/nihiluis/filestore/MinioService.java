package com.nihiluis.filestore;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.MinioException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@ApplicationScoped
public class MinioService {

    @Inject
    MinioClient minioClient;

    @ConfigProperty(name = "minio.bucket-name")
    String bucketName;

    public FileWithMetadata getObject(String objectName) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        // First get the metadata
        StatObjectResponse stat = minioClient.statObject(
            StatObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build()
        );

        // Then get the content stream
        InputStream is = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build()
        );

        return new FileWithMetadata(
            is,
            stat.userMetadata(),
            stat.contentType(),
            stat.size()
        );
    }

}
