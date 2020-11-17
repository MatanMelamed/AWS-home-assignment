package org.aws.gome.assignment.services.database;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

public class DynamoDbService implements DatabaseService {

    public static final String TABLE_NAME = "table_name";
    public static final String PARTITION_KEY = "partition_key";

    DynamoDbClient dbClient;

    public DynamoDbService() {
        dbClient = DynamoDbClient.builder()
                .region(Region.US_WEST_2)
                .build();
    }

    private void checker(Map<String, String> context) {
        if (!context.containsKey(TABLE_NAME)) {
            System.err.printf("%s checker :: %s not in context", this.getClass().getName(), TABLE_NAME);
        }

        if (!context.containsKey(PARTITION_KEY)) {
            System.err.printf("%s checker :: %s not in context", this.getClass().getName(), PARTITION_KEY);
        }
    }

    @Override
    public List<String> getValuesByKey(Map<String, String> context, String itemKey, String itemAttr) {
        checker(context);

        Map<String, AttributeValue> results = null;

        HashMap<String, AttributeValue> itemKeyMap = new HashMap<>();
        itemKeyMap.put(context.get(PARTITION_KEY), AttributeValue.builder().s(itemKey).build());

        GetItemRequest request = GetItemRequest.builder()
                .key(itemKeyMap)
                .tableName(context.get(TABLE_NAME))
                .attributesToGet(itemAttr)
                .build();

        try {
            results = dbClient.getItem(request).item();

            if (results == null) {
                throw new Exception("No item found with the key " + itemKey);
            }

            if (!results.containsKey(itemAttr)) {
                throw new Exception("Item does not contain attribute " + itemAttr);
            }

            return results.get(itemAttr).ss();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void addValuesToKey(Map<String, String> context, String itemKey, String itemAttr, List<String> attrValues) {

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
        } catch (ResourceNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }


}