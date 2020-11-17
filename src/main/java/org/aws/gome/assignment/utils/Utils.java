package org.aws.gome.assignment.utils;

import org.aws.gome.assignment.services.database.DynamoDbService;
import org.aws.gome.assignment.services.storage.S3Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Utils {

    public static String ConvertListStringToString(List<String> list, char delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s).append(delimiter);
        }

        return sb.toString();
    }

    public static Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }


    public static void main(String[] args) {
//        DynamoDbService service = new DynamoDbService();
//        HashMap<String, String> context = new HashMap<>();
//        context.put(DynamoDbService.TABLE_NAME, "aws-hm-search-values");
//        context.put(DynamoDbService.PARTITION_KEY, "search_key");
////        service.addValuesToKey(context, "ball", "photos", Arrays.asList("h", "ds"));
//        List<String> res = service.getValuesByKey(context, "ball", "photos");
//        for (String s : res) {
//            System.out.println(s);
//        }

        S3Service s3Service = new S3Service();
        HashMap<String, String> s3ServiceContext = new HashMap<>();
        s3ServiceContext.put(S3Service.BUCKET_NAME, "photos-aws-practice");
        String s = s3Service.getFileUrl(s3ServiceContext, "soccer1.jpg");
        System.out.println(s);
    }
}