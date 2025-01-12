package com.nihiluis.filestore;

import java.io.InputStream;
import java.util.Map;

public record FileWithMetadata(InputStream content, Map<String, String> metadata, String contentType, long size) {
}
