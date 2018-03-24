package cz.kota.tagfs;

import java.util.HashMap;
import java.util.Map;

public class Repository {
    private String rootDir;
    private String metafileName;
    private MetadataAccessor metadataAccessor;
    private Map<String, MetadataAccessor> additionalMetafiles;
    private ItemNameParser nameParser;

    public String getRootDir() {
        return rootDir;
    }

    public String getMetafileName() {
        return metafileName;
    }

    public MetadataAccessor getMetadataAccessor() {
        return metadataAccessor;
    }

    public Map<String, MetadataAccessor> getAdditionalMetafiles() {
        if (additionalMetafiles == null) {
            additionalMetafiles = new HashMap<String, MetadataAccessor>();
        }
        return additionalMetafiles;
    }

    public ItemNameParser getNameParser() {
        return nameParser;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public void setMetafile(String filename, MetadataAccessor accessor) {
        this.metafileName = filename;
        this.metadataAccessor = accessor;
    }

    public void addAdditionalMetafile(String filename, MetadataAccessor accessor) {
        getAdditionalMetafiles().put(filename, accessor);
    }

    public void setNameParser(ItemNameParser nameParser) {
        this.nameParser = nameParser;
    }

}
