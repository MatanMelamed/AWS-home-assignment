package org.aws.gome.assignment.services.storage;

import java.util.List;
import java.util.Map;

public interface StorageService {
    // TODO: delete unused code
    void storeFile(Map<String, String> context, StorageServiceFile file);

    StorageServiceFile fetchFile(Map<String, String> context, String fileKey);

    List<StorageServiceFile> fetchAllFiles(Map<String, String> context);

    List<StorageServiceFile> fetchListedFiles(Map<String, String> context, List<String> fileNames);

    String getFileUrl(Map<String, String> context, String fileKey);

    List<String> getListedFilesUrls(Map<String, String> context, List<String> fileNames);

}