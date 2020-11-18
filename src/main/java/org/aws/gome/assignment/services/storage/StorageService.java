package org.aws.gome.assignment.services.storage;

import java.util.Map;

public interface StorageService {
    void storeFile(Map<String, String> storageContext, StorageServiceFile file);

    String getFileUrl(Map<String, String> storageContext, String fileKey);
}