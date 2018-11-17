package cz.kota.tagfs;

import java.util.List;


public class KotasInfoParser implements MetadataAccessor {

    public Item updateItemFromMetadata(List<String> dataLines, Item item) {
        if (dataLines.size() > 0) item.addAttribute(KotasNames.ATTR_MUSIC_INFO, dataLines.get(0));
        if (dataLines.size() > 1) item.addAttribute(KotasNames.ATTR_METADATA_INFO, dataLines.get(1));
        if (dataLines.size() > 2) item.addAttribute(KotasNames.ATTR_QUALITY, dataLines.get(2));
        if (dataLines.size() > 3) item.addAttribute(KotasNames.ATTR_SOURCE, dataLines.get(3));
        return item;
    }

    public String generateMetadata(Item item) {
        // no output expected
        return null;
    }

}
