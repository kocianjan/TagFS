package cz.kota.tagfs;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;

public class ItemAccessor {

    public Collection<Item> readRepository(Repository rep) {
        File rootDir = new File(rep.getRootDir());
        ArrayList<File> itemDirs = new ArrayList<File>();
        getLeafDirs(rootDir, itemDirs);
        ArrayList<Item> items = new ArrayList<Item>();
        for (File itemDir: itemDirs) {
            Item item = new Item(itemDir.getAbsolutePath());
            items.add(item);
        }
        return items;
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

}
