package org.aws.gome.assignment.services.database;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@Component
public class DynamoDbService implements DatabaseService {

    public static final String TABLE_NAME = "table_name";
    public static final String PARTITION_KEY = "partition_key";

    DynamoDbClient dbClient;

    public DynamoDbService() {
        dbClient = DynamoDbClient.builder()
                .region(Region.US_WEST_2)
                .build();
    }

    private boolean isContextValid(Map<String, String> context) {
        if (!context.containsKey(TABLE_NAME)) {
            System.err.printf("%s checker :: %s not in context", this.getClass().getName(), TABLE_NAME);
            return false;
        }

        if (!context.containsKey(PARTITION_KEY)) {
            System.err.printf("%s checker :: %s not in context", this.getClass().getName(), PARTITION_KEY);
            return false;
        }

        return true;
    }

    @Override
    public List<String> getAttributeValuesByKey(Map<String, String> context, String itemKey, String itemAttr) {
        if (!isContextValid(context)) {return null;}

        List<String> results = null;

        HashMap<String, AttributeValue> itemKeyMap = new HashMap<>();
        itemKeyMap.put(context.get(PARTITION_KEY), AttributeValue.builder().s(itemKey).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName(context.get(TABLE_NAME))
                .key(itemKeyMap)
                .attributesToGet(itemAttr)
                .build();

        try {
            Map<String, AttributeValue> itemAttrs = dbClient.getItem(request).item();

            if (itemAttrs == null) {
                throw new Exception("No item found with the key " + itemKey);
            }

            if (!itemAttrs.containsKey(itemAttr)) {
                throw new Exception("Item does not contain attribute " + itemAttr);
            }

            results = itemAttrs.get(itemAttr).ss();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    @Override
    public void addAttributeValuesToKey(Map<String, String> context, String itemKey, String itemAttr, List<String> attrValues) {
        if (!isContextValid(context)) {return;}

        HashMap<String, AttributeValue> itemKeyMap = new HashMap<>();
        itemKeyMap.put(context.get(PARTITION_KEY), AttributeValue.builder().s(itemKey).build());

        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#Attr", itemAttr);

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":values", AttributeValue.builder().ss(attrValues).build());

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(context.get(TABLE_NAME))
                .key(itemKeyMap)
                .updateExpression("add #Attr :values")
                .expressionAttributeNames(expressionAttributeNames)
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        try {
            dbClient.updateItem(request);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
    }
}