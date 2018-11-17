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
        HashMap<String,String> attrs = new HashMap<>();
        String separator = " - ";
        int sepLen = separator.length();
        int index = itemName.indexOf(separator);
        if (index < 0) {
            attrs.put(KotasNames.ATTR_ALBUM, itemName.trim());
        }
        else {
            String artist, album = null, year = null;
            artist = itemName.substring(0, index).trim();
            itemName = itemName.substring(index + sepLen).trim();
            index = itemName.indexOf(separator);
            if (index > 0) {
                year = itemName.substring(0, index).trim();
                if (isNumber(year)) {
                    album = itemName.substring((index + sepLen)).trim();
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
            attrs.put(KotasNames.ATTR_ARTIST, artist);
            attrs.put(KotasNames.ATTR_ALBUM, album);
            if (year != null) {
                attrs.put(KotasNames.ATTR_YEAR, year);
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
        String artist = attributes.getOrDefault(KotasNames.ATTR_ARTIST, def);
        String year = attributes.getOrDefault(KotasNames.ATTR_YEAR, def);
        String album = attributes.getOrDefault(KotasNames.ATTR_ALBUM, def);
        return String.format("%s - %s - %s", artist, year, album);
    }

}
