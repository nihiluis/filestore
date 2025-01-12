package com.nihiluis.filestore;

import java.io.InputStream;
import java.util.Map;

public class FileWithMetadata {
    private final InputStream content;
    private final Map<String, String> metadata;
    private final String contentType;
    private final long size;

    public FileWithMetadata(InputStream content, Map<String, String> metadata, 
                          String contentType, long size) {
        this.content = content;
        this.metadata = metadata;
        this.contentType = contentType;
        this.size = size;
    }

    public InputStream getContent() {
        return content;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSize() {
        return size;
    }
}
