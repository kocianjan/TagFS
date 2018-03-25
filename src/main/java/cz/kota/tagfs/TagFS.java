package cz.kota.tagfs;

import java.util.List;


public class TagFS {

    public static void main(String[] args) {
        try {
            String rootDir = "d:/music";
            System.out.printf("TagFS launched for dir %s\n", rootDir);
            ItemAccessor ia = ItemAccessor.getInstance();
            Repository rep = ia.initRepository(rootDir);
            List<Item> items = ia.readRepository(rep);
            System.out.printf("Items in directory %s:\n", rootDir);
//        for (Item item: items) {
//            System.out.printf("  %s\n", item.getName());
//        }
            ia.exportItemsCsv(items, System.out);
        }
        catch (Exception e) {
            System.out.printf("Error in TagFS app: %s\n", e);
        }
    }

}
