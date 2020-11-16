package org.aws.gome.assignment.services.storage;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

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
    public String storeFile(Map<String, String> context, StorageServiceFile file) {
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

            PutObjectResponse response = client.putObject(request, RequestBody.fromBytes(file.getData()));

            return response.eTag();

        } catch (S3Exception e) {
            System.err.println(e.getMessage());
            exit(1);
        }

        return "";
    }

    @Override
    public StorageServiceFile fetchFile(Map<String, String> context, String objectKey) {
        checker(context);

        StorageServiceFile result = new StorageServiceFile();

        try {
            // Create a GetObjectRequest instance
            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .key(objectKey)
                    .bucket(context.get(BUCKET_NAME))
                    .build();

            // ID
            result.setID(objectKey);

            // Metadata
            ResponseInputStream<GetObjectResponse> response = client.getObject(objectRequest);
            result.setMetadata(response.response().metadata());

            // Data
            ResponseBytes<GetObjectResponse> objectBytes = client.getObjectAsBytes(objectRequest);
            result.setData(objectBytes.asByteArray());

            return result;

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            exit(1);
        }
        return null;
    }

    @Override
    public List<StorageServiceFile> fetchAllFiles(Map<String, String> context) {
        checker(context);
        ArrayList<StorageServiceFile> results = new ArrayList<>();

        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(context.get(BUCKET_NAME))
                    .build();

            ListObjectsResponse res = client.listObjects(listObjects);
            List<S3Object> objects = res.contents();

            for (S3Object myValue : objects) {
                results.add(fetchFile(context, myValue.key()));
            }
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

        return results;
    }
}

