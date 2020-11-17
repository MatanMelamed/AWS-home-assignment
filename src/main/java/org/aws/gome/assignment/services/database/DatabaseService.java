package org.aws.gome.assignment.services.database;

import java.util.List;
import java.util.Map;

public interface DatabaseService {

    void addValuesToKey(Map<String, String> context, String itemKey, String itemAttr, List<String> attrValues);

    List<String> getValuesByKey(Map<String, String> context, String itemKey, String itemAttr);
}
