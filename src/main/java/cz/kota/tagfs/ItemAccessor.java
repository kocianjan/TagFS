package cz.kota.tagfs;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        return rep;
    }

    public List<Item> readRepository(Repository rep) {
        Collection<File> itemDirs = listItemDirs(rep.getRootDir());
        ArrayList<Item> items = new ArrayList<Item>();
        for (File itemDir: itemDirs) {
            Item item = readItem(itemDir, rep);
            items.add(item);
        }
        return items;
    }

    private Item readItem(File itemDir, Repository rep) {
        Item item = new Item(itemDir.getAbsolutePath());
        ItemNameParser nameParser = rep.getNameParser();
        if (nameParser != null) {
            item.addAttributes(nameParser.parseAttributes(item.getName()));
        }
        for (String mfName: rep.getAdditionalMetafiles().keySet()) {
            updateItemFromMetafile(new File(itemDir, mfName), rep.getAdditionalMetafiles().get(mfName), item);
        }

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

    private Collection<File> listItemDirs(String rootPath) {
        File rootDir = new File(rootPath);
        ArrayList<File> itemDirs = new ArrayList<File>();
        getLeafDirs(rootDir, itemDirs);
        return itemDirs;
    }

    private void getLeafDirs(File dir, ArrayList<File> leafs) {
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
                getLeafDirs(subdir, leafs);
            }
        }
    }

    public void exportItemsCsv(List<Item> items, Appendable appendable)
        throws IOException
    {
        ArrayList<String> result = new ArrayList<String>();
        if (items.isEmpty()) {
            return;
        }
        CSVPrinter printer = new CSVPrinter(appendable, CSVFormat.DEFAULT);
        List<String> attributeNames = new ArrayList<String>();
        attributeNames.addAll(items.get(0).getAttributes().keySet());
        writeHeader(attributeNames, printer);
        for (Item item: items) {
            writeItem(item, attributeNames, printer);
        }
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

    private String serializeCollection(Collection<String> items, String separator) {
        StringBuffer buf = new StringBuffer();
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
