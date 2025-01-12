package com.nihiluis.filestore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import java.time.Instant;

/**
 * Stores metadata for a file saved on an S3/minio instance.
 */
@Entity
public class RemoteStoredFile extends PanacheEntityBase {
    public String originalFilename;
    public String storedFilename;
    public String contentType;
    public long fileSize;
    public String bucketName;
    public String storagePath;
    public Instant uploadDate;
    public String uploaderId;
    public String checksum;

    // AI! generate 
}
