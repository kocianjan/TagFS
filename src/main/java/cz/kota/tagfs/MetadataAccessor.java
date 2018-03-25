package cz.kota.tagfs;

import java.util.List;

public interface MetadataAccessor {
    public Item updateItemFromMetadata(List<String> dataLines, Item item);
    public String generateMetadata(Item item);
}
