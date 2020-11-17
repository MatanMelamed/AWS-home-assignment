package org.aws.gome.assignment.services.storage;

import java.util.List;
import java.util.Map;

public interface StorageService {
    // TODO: delete unused code
    void storeFile(Map<String, String> context, StorageServiceFile file);

    String getFileUrl(Map<String, String> context, String fileKey);
}