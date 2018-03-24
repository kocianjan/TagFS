package cz.kota.tagfs;

import java.util.Map;

public interface ItemNameParser {
    public Map<String,String> parseAttributes(String itemName);
    public String generateName(Map<String,String> attributes);
}
