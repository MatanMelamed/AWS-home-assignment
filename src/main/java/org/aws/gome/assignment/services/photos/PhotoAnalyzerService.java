package org.aws.gome.assignment.services.photos;

import java.util.List;

public interface PhotoAnalyzerService {

    List<String> detectLabels(byte[] photoBytes, float confidenceThreshold);

    List<String> getSupportedFormats();
}
