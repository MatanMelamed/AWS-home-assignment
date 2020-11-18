package org.aws.gome.assignment.services.storage;

import java.util.Map;

public interface StorageService {
    void storeFile(Map<String, String> context, StorageServiceFile file);

    String getFileUrl(Map<String, String> context, String fileKey);
}