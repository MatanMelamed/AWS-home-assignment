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
public class MainHandler {

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
    private final char METADATA_DELIMITER = ':';

    public void uploadPhoto(MultipartFile file) {
        // file extension support check
        Optional<String> extension = Utils.getExtension(file.getOriginalFilename());
        List<String> supportedFormats = photoAnalyzerService.getSupportedFormats();
        if (!extension.isPresent() || !supportedFormats.contains(extension.get())) {
            System.err.println("file " + file.getOriginalFilename() + " has unsupported format.\n");
            return;
        }

        try {
            // analyze photo, create a storage file and upload it.
            List<String> labels = photoAnalyzerService.detectLabels(file.getBytes(), 75.0f);

            // store label to photos name in database
            for (String label : labels) {
                databaseService.addValuesToKey(databaseContext,
                                               label.toLowerCase(),
                                               DB_ATTR,
                                               Collections.singletonList(file.getOriginalFilename()));
            }

            // update photo metadata
            HashMap<String, String> fileMetadata = new HashMap<>();
            fileMetadata.put(Constants.LABELS, Utils.ConvertListStringToString(labels, METADATA_DELIMITER));

            // create a storage file to store
            StorageServiceFile storageFile = new StorageServiceFile(file.getOriginalFilename(),
                                                                    file.getBytes(),
                                                                    fileMetadata);

            // store the storage file
            storageService.storeFile(storageContext, storageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done uploading");
    }

    public List<String> getPhotosUrlsByLabel(String label) {
        // get photos name by label
        List<String> photosNames = databaseService.getValuesByKey(databaseContext, label.toLowerCase(), DB_ATTR);

        if (photosNames == null) {
            System.out.println("No Photos Found.");
            return null;
        }

        // get photos urls by photos names
        List<String> photosUrls = photosNames.stream()
                .map(name -> storageService.getFileUrl(storageContext, name))
                .collect(Collectors.toList());

//        List<StorageServiceFile> photos = storageService.fetchListedFiles(storageContext, photosNames);
//
//        for (StorageServiceFile file : photos) {
//            System.out.println(file);
//        }
        return photosUrls;
    }

}
