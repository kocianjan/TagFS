package cz.kota.tagfs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Item {

    private String name;
    private String path;
    private Map<String,String> attributes;
    private List<String> tags;

    public Item(String path) {
        this.path = path;
        this.name = new File(path).getName();
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<String, String>();
        }
        return attributes;
    }

    public List<String> getTags() {
        if (tags == null) {
            tags = new ArrayList<String>();
        }
        return tags;
    }

    public void addAttributes(Map<String,String> items) {
        getAttributes().putAll(items);
    }

    public void addAttribute(String name, String value) {
        getAttributes().put(name, value);
    }

    public void addTags(List<String> items) {
        getTags().addAll(items);
    }

}
