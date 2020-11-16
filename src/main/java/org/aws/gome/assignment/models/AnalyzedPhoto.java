package org.aws.gome.assignment.models;

import java.util.ArrayList;

public class AnalyzedPhoto {

    private byte[] data;
    private String name;
    private ArrayList<String> labels;

    public AnalyzedPhoto(byte[] data, String name, ArrayList<String> labels) {
        this.data = data;
        this.name = name;
        this.labels = labels;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }
}
