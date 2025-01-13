package com.nihiluis.filestore.dto;

public class FileUploadResponse {
    public String storedFilename;
    public String originalFilename;
    public String contentType;
    public long size;

    public FileUploadResponse(String storedFilename, String originalFilename, String contentType, long size) {
        this.storedFilename = storedFilename;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.size = size;
    }
} 