package com.nihiluis.filestore.dto;

public class PresignedUrlResponse {
    public String url;
    public String objectName;

    public PresignedUrlResponse(String url, String objectName) {
        this.url = url;
        this.objectName = objectName;
    }
} 