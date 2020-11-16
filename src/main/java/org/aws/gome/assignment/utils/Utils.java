package org.aws.gome.assignment.utils;

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
        Optional<String> s = getExtension("no-extention.p");
        System.out.println(s);
        System.out.println(s.isPresent());
        System.out.println(s.get());
    }
}
