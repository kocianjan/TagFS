package cz.kota.tagfs;

import java.util.HashMap;
import java.util.Map;

public class KotasNameParser
    implements ItemNameParser
{

    /**
     * Supports formats:
     * * artist - year - album
     * * artist - album (year)
     * * artist - album
     * * album
     * @param itemName
     * @return
     */
    public Map<String,String> parseAttributes(String itemName) {
        HashMap<String,String> attrs = new HashMap<String, String>();
        int index = itemName.indexOf('-');
        if (index < 0) {
            attrs.put("Album", itemName.trim());
        }
        else {
            String artist, album = null, year = null;
            artist = itemName.substring(0, index).trim();
            itemName = itemName.substring(index + 1).trim();
            index = itemName.indexOf('-');
            if (index > 0) {
                year = itemName.substring(0, index).trim();
                if (isNumber(year)) {
                    album = itemName.substring((index + 1)).trim();
                } else {
                    year = null;
                }
            }
            if (album == null) {
                index = itemName.indexOf('(');
                if (index > 0) {
                    int endIndex = itemName.indexOf(')', index + 1);
                    if (endIndex > 0) {
                        year = itemName.substring(index + 1, endIndex);
                        if (isNumber(year)) {
                            album = itemName.substring(0, index).trim();
                        } else {
                            year = null;
                        }
                    }
                }
            }
            if (album == null) {
                album = itemName.trim();
            }
            attrs.put("Artist", artist);
            attrs.put("Album", album);
            if (year != null) {
                attrs.put("Year", year);
            }
        }
        return attrs;
    }

    private boolean isNumber(String value) {
        try {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public String generateName(Map<String,String> attributes) {
        String def = "";
        String artist = attributes.getOrDefault("Artist", def);
        String year = attributes.getOrDefault("Year", def);
        String album = attributes.getOrDefault("Album", def);
        return String.format("%s - %s - %s", artist, year, album);
    }

}
