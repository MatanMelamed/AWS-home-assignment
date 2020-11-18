package org.aws.gome.assignment.services.storage;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URL;
import java.util.*;


@Component
public class S3Service implements StorageService {

    public static final String BUCKET_NAME = "bucket_name";
    S3Client client;

    public S3Service() {
        this.client = S3Client.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(Region.US_WEST_2)
                .build();

    }

    private boolean isContextValid(Map<String, String> context) {
        if (!context.containsKey(BUCKET_NAME)) {
            System.err.printf("S3Service storeFile context doesn't contain %s", BUCKET_NAME);
            return false;
        }
        return true;
    }

    @Override
    public void storeFile(Map<String, String> storageContext, StorageServiceFile file) {
        if (!isContextValid(storageContext)) { return; }

        for (String s : file.getMetadata().keySet()) {
            System.out.println(file.getMetadata().get(s));
        }
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(storageContext.get(BUCKET_NAME))
                    .key(file.getID())
                    .metadata(file.getMetadata())
                    .build();

            client.putObject(request, RequestBody.fromBytes(file.getData()));

        } catch (S3Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public String getFileUrl(Map<String, String> storageContext, String fileKey) {
        if (!isContextValid(storageContext)) { return ""; }
        String results = null;

        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(storageContext.get(BUCKET_NAME))
                .key(fileKey)
                .build();
        try {
            URL url = client.utilities().getUrl(request);
            results = url.toString();
        } catch (SdkException e) {
            System.err.println(e.getMessage());
        }
        return results;
    }
}

