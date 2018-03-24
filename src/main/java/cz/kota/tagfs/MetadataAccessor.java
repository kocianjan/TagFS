package cz.kota.tagfs;

public interface MetadataAccessor {
    public Item parseMetadata(String data);
    public String generateMetadata(Item item);
}
