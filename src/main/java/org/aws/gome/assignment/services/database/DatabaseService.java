package org.aws.gome.assignment.services.database;

import java.util.List;
import java.util.Map;

public interface DatabaseService {

    void addAttributeValuesToKey(Map<String, String> context, String itemKey, String itemAttr, List<String> attrValues);

    List<String> getAttributeValuesByKey(Map<String, String> context, String itemKey, String itemAttr);
}
