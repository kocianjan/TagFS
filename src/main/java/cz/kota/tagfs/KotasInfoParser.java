package cz.kota.tagfs;

import java.util.List;


public class KotasInfoParser implements MetadataAccessor {

    public Item updateItemFromMetadata(List<String> dataLines, Item item) {
        if (dataLines.size() > 0) item.addAttribute("musicInfo", dataLines.get(0));
        if (dataLines.size() > 1) item.addAttribute("metadataInfo", dataLines.get(1));
        if (dataLines.size() > 2) item.addAttribute("mp3Quality", dataLines.get(2));
        if (dataLines.size() > 3) item.addAttribute("source", dataLines.get(3));
        return item;
    }

    public String generateMetadata(Item item) {
        // no output expected
        return null;
    }

}
