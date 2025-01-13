package com.nihiluis.filestore.db;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;

/**
 * Stores metadata for a file saved on an S3/minio instance.
 */
@Entity(name = "remote_stored_file")
public class RemoteStoredFile extends PanacheEntityBase {
    @Column(name = "original_filename")
    public String originalFilename;
    @Column(name = "stored_filename") 
    public String storedFilename;
    @Column(name = "content_type")
    public String contentType;
    @Column(name = "file_size")
    public long fileSize;
    @Column(name = "bucket_name")
    public String bucketName;
    @Column(name = "storage_path")
    public String storagePath;
    @Column(name = "upload_date")
    public Instant uploadDate;
    @Column(name = "uploader_id")
    public String uploaderId;
    @Column(name = "checksum")
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
