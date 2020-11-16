package org.aws.gome.assignment.services.photos;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.Label;

import java.util.ArrayList;
import java.util.List;

public class RekognitionService implements PhotoAnalyzerService {

    RekognitionClient rekClient;

    static final List<String> supportedFormats = new ArrayList<String>() {{
        add("jpg");
        add("png");
    }};

    public RekognitionService() {
        rekClient = RekognitionClient.builder()
                .region(Region.US_WEST_2)
                .build();
    }

    @Override
    public List<String> detectLabels(byte[] photoBytes) {

        List<String> results = new ArrayList<>();

        SdkBytes sdkBytes = SdkBytes.fromByteArray(photoBytes);
        Image image = Image.builder().bytes(sdkBytes).build();

        DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
                .image(image)
                .maxLabels(10)
                .build();

        DetectLabelsResponse labelsResponse = rekClient.detectLabels(detectLabelsRequest);

        for (Label l : labelsResponse.labels()) {
//            System.out.println(l.name() + ": " + l.confidence().toString());
            results.add(l.name());
        }

        return results;
    }

    @Override
    public List<String> getSupportedFormats() {
        return supportedFormats;
    }
}
