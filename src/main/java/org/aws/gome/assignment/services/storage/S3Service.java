package org.aws.gome.assignment.services.storage;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URL;
import java.util.*;

import static java.lang.System.exit;

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

    private void checker(Map<String, String> context) {
        if (!context.containsKey(BUCKET_NAME)) {
            System.err.printf("S3Service storeFile context doesn't contain %s", BUCKET_NAME);
            System.exit(-1);
        }
    }

    @Override
    public void storeFile(Map<String, String> context, StorageServiceFile file) {
        checker(context);

        for (String s : file.getMetadata().keySet()) {
            System.out.println(file.getMetadata().get(s));
        }
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(context.get(BUCKET_NAME))
                    .key(file.getID())
                    .metadata(file.getMetadata())
                    .build();

            client.putObject(request, RequestBody.fromBytes(file.getData()));

        } catch (S3Exception e) {
            System.err.println(e.getMessage());
            exit(1);
        }
    }

    @Override
    public String getFileUrl(Map<String, String> context, String fileKey) {
        checker(context);

        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(context.get(BUCKET_NAME))
                .key(fileKey)
                .build();

        URL url = client.utilities().getUrl(request);
        return url.toString();
    }
}

