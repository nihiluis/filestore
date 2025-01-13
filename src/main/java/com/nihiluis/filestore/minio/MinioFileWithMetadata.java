package com.nihiluis.filestore.minio;

import java.io.InputStream;
import java.util.Map;

public record MinioFileWithMetadata(InputStream content, Map<String, String> metadata, String contentType, long size) {
}
