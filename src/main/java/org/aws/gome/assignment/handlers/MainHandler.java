package org.aws.gome.assignment.handlers;

import org.aws.gome.assignment.services.photos.PhotoAnalyzerService;
import org.aws.gome.assignment.services.photos.RekognitionService;
import org.aws.gome.assignment.services.storage.S3Service;
import org.aws.gome.assignment.services.storage.StorageService;
import org.aws.gome.assignment.services.storage.StorageServiceFile;
import org.aws.gome.assignment.utils.Constants;
import org.aws.gome.assignment.utils.Utils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Component
public class MainHandler {

    private final PhotoAnalyzerService photoAnalyzerService = new RekognitionService();

    private final StorageService storageService = new S3Service();
    private final HashMap<String, String> storageContext = new HashMap<String, String>() {{
        put(S3Service.BUCKET_NAME, "photos-aws-practice");
    }};

    public void uploadPhoto(MultipartFile file) {
        StorageServiceFile storageFile;
        HashMap<String, String> fileMetadata = new HashMap<>();

        // file extension support check
        Optional<String> extension = Utils.getExtension(file.getOriginalFilename());
        List<String> supportedFormats = photoAnalyzerService.getSupportedFormats();
        if (!extension.isPresent() || !supportedFormats.contains(extension.get())) {
            System.err.println("file " + file.getOriginalFilename() + " has unsupported format.\n");
            return;
        }

        // analyze photo, create a storage file and upload it.
        try {
            List<String> labels = photoAnalyzerService.detectLabels(file.getBytes());
            fileMetadata.put(Constants.LABELS, Utils.ConvertListStringToString(labels, ':'));

            storageFile = new StorageServiceFile(file.getOriginalFilename(),
                                                 file.getBytes(),
                                                 fileMetadata);

            storageService.storeFile(storageContext, storageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done uploading");
    }

    public void getPhotos() {
        List<StorageServiceFile> files = storageService.fetchAllFiles(storageContext);
        for (StorageServiceFile file : files) {
            System.out.println(file);
        }
    }

}
