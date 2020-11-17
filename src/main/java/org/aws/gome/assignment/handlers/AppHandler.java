package org.aws.gome.assignment.handlers;

import org.aws.gome.assignment.services.database.DatabaseService;
import org.aws.gome.assignment.services.database.DynamoDbService;
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
import java.util.*;
import java.util.stream.Collectors;


@Component
public class AppHandler {

    // photo label analyzing service
    private final PhotoAnalyzerService photoAnalyzerService = new RekognitionService();

    // label to photos name storing service
    private final DatabaseService databaseService = new DynamoDbService();
    private final HashMap<String, String> databaseContext = new HashMap<String, String>() {{
        put(DynamoDbService.TABLE_NAME, "aws-hm-search-values");
        put(DynamoDbService.PARTITION_KEY, "search_key");
    }};
    private final String DB_ATTR = "photos";


    // photo storage service
    private final StorageService storageService = new S3Service();
    private final HashMap<String, String> storageContext = new HashMap<String, String>() {{
        put(S3Service.BUCKET_NAME, "photos-aws-practice");
    }};

    private boolean isFileExtensionSupported(String fileName) {
        Optional<String> extension = Utils.getFileExtension(fileName);
        List<String> supportedFormats = photoAnalyzerService.getSupportedFormats();
        return !extension.isPresent() || !supportedFormats.contains(extension.get());
    }

    private void analyzeAndSavePhotoLabels(StorageServiceFile storagePhoto) {
        // analyze photo, create a storage file and upload it.
        List<String> labels = photoAnalyzerService.detectLabels(storagePhoto.getData(), Constants.PhotoLabelConfidence);

        // store label to photos name in database
        for (String label : labels) {
            databaseService.addAttributeValuesToKey(databaseContext,
                                                    label.toLowerCase(),
                                                    DB_ATTR,
                                                    Collections.singletonList(storagePhoto.getID()));
        }
    }

    public void uploadPhoto(MultipartFile file) {
        if (isFileExtensionSupported(file.getOriginalFilename())) {
            System.err.println("file " + file.getOriginalFilename() + " has unsupported format.\n");
            return;
        }

        try {
            StorageServiceFile storagePhoto = new StorageServiceFile();
            storagePhoto.setID(file.getOriginalFilename());
            storagePhoto.setData(file.getBytes());

            analyzeAndSavePhotoLabels(storagePhoto);

            storageService.storeFile(storageContext, storagePhoto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getPhotosUrlsByLabel(String label) {
        List<String> photosNames = databaseService.getAttributeValuesByKey(databaseContext, label.toLowerCase(), DB_ATTR);

        return photosNames.stream()
                .map(name -> storageService.getFileUrl(storageContext, name))
                .collect(Collectors.toList());
    }
}
