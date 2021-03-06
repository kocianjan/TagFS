package cz.kota.tagfs;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Logger;

public class ItemAccessor {

    private static Logger logger = Logger.getLogger("cz.kota.tagfs.ItemAccessor");
    private static ItemAccessor instance;

    public static ItemAccessor getInstance() {
        if (instance == null) {
            instance = new ItemAccessor();
        }
        return instance;
    }

    public Repository initRepository(String rootDir) {
        Repository rep = new Repository();
        rep.setRootDir(rootDir);
        rep.setNameParser(new KotasNameParser());
        rep.addAdditionalMetafile("_info.txt", new KotasInfoParser());
        rep.addAdditionalMetafile("_grabbed.txt", new StaticMetadataAccessor(KotasNames.ATTR_SOURCE, "grabbed"));
        rep.addIgnoreDirName("!INBOX");
        rep.addIgnoreDirName("__mix");
        return rep;
    }

    public List<Item> readRepository(Repository rep) {
        ignores = new HashSet<>();
        ignores.addAll(Arrays.asList(ignoresArr));
        ignores.add(rep.getMetafileName());
        ignores.addAll(rep.getAdditionalMetafiles().keySet());
        ignores.remove(null);

        Collection<File> itemDirs = listItemDirs(rep.getRootDir(), rep.getIgnoreDirNames());
        ArrayList<Item> items = new ArrayList<>();
        for (File itemDir: itemDirs) {
            Item item = readItem(itemDir, rep);
            items.add(item);
        }
        return items;
    }

    private Item readItem(File itemDir, Repository rep) {
        Item item = new Item(itemDir.getAbsolutePath());
        ItemNameParser nameParser = rep.getNameParser();
        // TODO: temporary, remove
        updateItemFromDirContent(item, itemDir);
        if (nameParser != null) {
            item.addAttributes(nameParser.parseAttributes(item.getName()));
        }
        for (String mfName: rep.getAdditionalMetafiles().keySet()) {
            updateItemFromMetafile(new File(itemDir, mfName), rep.getAdditionalMetafiles().get(mfName), item);
        }

        return item;
    }

    private String[] ignoresArr = new String[] {
            ".mp3", ".m3u", ".flac", ".jpg", ".jpeg", ".m4a", ".sfv", ".htm", ".html", ".md5", ".nfo",
            "lyrics.txt", "thumbs.db", "desktop.ini", "tracklist.txt", "texty.txt", "seznam.txt"
    };
    private HashSet<String> ignores = new HashSet<>();
    private Item updateItemFromDirContent(Item item, File itemDir) {
        String[] fileNames = itemDir.list();
        StringBuilder buf = new StringBuilder();
        for (String filename: fileNames) {
            boolean ignore = false;
            String lcFilename = filename.toLowerCase();
            for (String suffix: ignores) {
                if (lcFilename.endsWith(suffix)) {
                    ignore = true;
                    break;
                }
            }
            if (!ignore) {
                buf.append(filename).append("|");
            }
        }
        item.addAttribute("metafiles", buf.toString());
        return item;
    }

    private void updateItemFromMetafile(File file, MetadataAccessor accessor, Item item) {
        if (file.exists()) {
            try {
                List<String> data = Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
                accessor.updateItemFromMetadata(data, item);
            }
            catch (IOException e) {
                logger.warning("Error reading data from metafile " + file.getAbsolutePath() + ": " + e);
            }
        }
    }

    private Collection<File> listItemDirs(String rootPath, Collection<String> ignoreDirNames) {
        File rootDir = new File(rootPath);
        ArrayList<File> itemDirs = new ArrayList<>();
        getLeafDirs(rootDir, itemDirs, ignoreDirNames);
        return itemDirs;
    }

    private void getLeafDirs(File dir, ArrayList<File> leafs, Collection<String> ignoreDirNames) {
        if (ignoreDirNames.contains(dir.getName())) {
            return;
        }
        File[] subdirs = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        if (subdirs.length == 0) {
            leafs.add(dir);
        }
        else {
            for (File subdir: subdirs) {
                getLeafDirs(subdir, leafs, ignoreDirNames);
            }
        }
    }

    public void exportItemsCsv(List<Item> items, Appendable appendable)
        throws IOException
    {
        ArrayList<String> result = new ArrayList<>();
        if (items.isEmpty()) {
            return;
        }
        CSVPrinter printer = new CSVPrinter(appendable, CSVFormat.DEFAULT);
        List<String> attributeNames = getAttributeNames(items);
        writeHeader(attributeNames, printer);
        for (Item item: items) {
            writeItem(item, attributeNames, printer);
        }
    }

    private List<String> getAttributeNames(List<Item> items) {
        Set<String> attributes = new HashSet<>();
        for (Item item: items) {
            attributes.addAll(item.getAttributes().keySet());
        }
        return new ArrayList<>(attributes);
    }

    private void writeHeader(List<String> attributeNames, CSVPrinter printer)
        throws IOException
    {
        for (String attName: attributeNames) {
            printer.print(attName);
        }
        printer.print("Tags");
        printer.print("ItemName");
        printer.print("ItemPath");
        printer.println();
    }

    private void writeItem(Item item, List<String> attributeNames, CSVPrinter printer)
        throws IOException
    {
        for (String attName: attributeNames) {
            printer.print(item.getAttributes().getOrDefault(attName, ""));
        }
        printer.print(serializeCollection(item.getTags(), "|"));
        printer.print(item.getName());
        printer.print(item.getPath());
        printer.println();
    }

    private String serializeCollection(Collection<?> items, String separator) {
        StringBuilder buf = new StringBuilder();
        for (Object item: items) {
            buf.append(item).append(separator);
        }
        if (buf.length() == 0) {
            return "";
        } else {
            return buf.substring(0, buf.length() - separator.length());
        }
    }

}
