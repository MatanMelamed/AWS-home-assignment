package org.aws.gome.assignment.handlers;

import org.aws.gome.assignment.services.storage.S3Service;
import org.aws.gome.assignment.services.storage.StorageService;
import org.aws.gome.assignment.services.storage.StorageServiceFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Component
public class MainHandler {

    private final StorageService storageService = new S3Service();
    private final HashMap<String, String> storageContext = new HashMap<String, String>() {{
        put(S3Service.BUCKET_NAME, "photos-aws-practice");
    }};

    public void uploadPhoto(MultipartFile file) {
        StorageServiceFile storageFile;
        HashMap<String, String> fileMetadata = new HashMap<>();
        fileMetadata.put("test1-key", "test1-value");
        fileMetadata.put("test2-key", "test2 value");

        try {
            storageFile = new StorageServiceFile(file.getOriginalFilename(),
                                                 file.getBytes(),
                                                 fileMetadata);

            storageService.storeFile(storageContext, storageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getPhotos() {
        List<StorageServiceFile> files = storageService.fetchAllFiles(storageContext);
        for (StorageServiceFile file : files) {
            System.out.println(file);
        }
    }
}
