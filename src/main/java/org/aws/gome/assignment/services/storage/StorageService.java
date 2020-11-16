package org.aws.gome.assignment.services.storage;

import java.util.List;
import java.util.Map;

public interface StorageService {
    String storeFile(Map<String, String> context, StorageServiceFile file);

    StorageServiceFile fetchFile(Map<String, String> context, String objectKey);

    List<StorageServiceFile> fetchAllFiles(Map<String, String> context);
}