package cz.kota.tagfs;

import java.util.List;


public class StaticMetadataAccessor implements MetadataAccessor {

    private String attrName;
    private String attrValue;

    public StaticMetadataAccessor(String attrName, String attrValue) {
        this.attrName = attrName;
        this.attrValue = attrValue;
    }

    public Item updateItemFromMetadata(List<String> dataLines, Item item) {
        item.addAttribute(attrName, attrValue);
        return item;
    }

    public String generateMetadata(Item item) {
        // no output expected
        return null;
    }

}
