package com.nihiluis.filestore;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import java.time.Instant;

/**
 * Stores metadata for a file saved on an S3/minio instance.
 */
@Entity
public class RemoteStoredFile extends PanacheEntity {
    public String originalFilename;
    public String storedFilename;
    public String contentType;
    public long fileSize;
    public String bucketName;
    public String storagePath;
    public Instant uploadDate;
    public String uploaderId;
    public String checksum;
}
