package cz.kota.tagfs;

import java.util.Collection;


public class TagFS {

    public static void main(String[] args) {
        String rootDir = "d:/music";
        System.out.printf("TagFS launched for dir %s\n", rootDir);
        Repository rep = new Repository();
        rep.setRootDir(rootDir);
        ItemAccessor ia = new ItemAccessor();
        Collection<Item> items = ia.readRepository(rep);
        System.out.printf("Items in directory %s:\n", rootDir);
        for (Item item: items) {
            System.out.printf("  %s\n", item.getName());
        }
    }
}
