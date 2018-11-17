package cz.kota.tagfs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;


public class TagFS {

    public static void main(String[] args) {
        try {
            String rootDir = "d:/music";
            String outputFile = "d:/music/tagfs_export.csv";
            System.out.printf("TagFS launched for dir %s\n", rootDir);
            ItemAccessor ia = ItemAccessor.getInstance();
            Repository rep = ia.initRepository(rootDir);
            List<Item> items = ia.readRepository(rep);
            System.out.printf("Exporting items in directory %s to file %s:\n", rootDir, outputFile);
//        for (Item item: items) {
//            System.out.printf("  %s\n", item.getName());
//        }
            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile));
            //ia.exportItemsCsv(items, System.out);
            ia.exportItemsCsv(items, outputWriter);
        }
        catch (Exception e) {
            System.err.printf("Error in TagFS app: %s\n", e);
            e.printStackTrace(System.err);
        }
    }

}
