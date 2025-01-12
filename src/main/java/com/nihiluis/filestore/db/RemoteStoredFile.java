package com.nihiluis.filestore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public RemoteStoredFile() {
    }

    public RemoteStoredFile(String originalFilename, String storedFilename, String contentType, 
                          long fileSize, String bucketName, String storagePath) {
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.bucketName = bucketName;
        this.storagePath = storagePath;
        this.uploadDate = Instant.now();
    }

    public static RemoteStoredFile create(String originalFilename, String storedFilename, 
                                        String contentType, long fileSize, 
                                        String bucketName, String storagePath) {
        var file = new RemoteStoredFile(
            originalFilename, storedFilename, contentType, 
            fileSize, bucketName, storagePath
        );
        // id is populated back into the file variable.
        file.persist();
        return file;
    }

    public static RemoteStoredFile findById(Long id) {
        return find("id", id).firstResult();
    }

    @Override
    public String toString() {
        return "RemoteStoredFile{" +
                "id=" + id +
                ", originalFilename='" + originalFilename + '\'' +
                ", storedFilename='" + storedFilename + '\'' +
                ", contentType='" + contentType + '\'' +
                ", fileSize=" + fileSize +
                ", bucketName='" + bucketName + '\'' +
                ", storagePath='" + storagePath + '\'' +
                ", uploadDate=" + uploadDate +
                ", uploaderId='" + uploaderId + '\'' +
                ", checksum='" + checksum + '\'' +
                '}';
    }
}
