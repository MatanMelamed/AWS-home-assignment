package org.aws.gome.assignment.services.photos;

import java.util.List;

public interface PhotoAnalyzerService {

    public List<String> detectLabels(byte[] photoBytes);

    List<String> getSupportedFormats();
}
