package org.aws.gome.assignment.services.storage;

import java.util.HashMap;
import java.util.Map;

public class StorageServiceFile {

    private String ID;
    private byte[] data;
    private Map<String, String> metadata;

    public StorageServiceFile() {
        metadata = new HashMap<>();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName()).append(":\n");
        sb.append("\t").append(ID).append("\n");
        sb.append("\t").append(data.length / 1000f).append(" KB\n");
        for (String key : metadata.keySet()) {
            sb.append("\t").append(key).append(":").append(metadata.get(key)).append("\n");
        }

        return sb.toString();
    }
}
