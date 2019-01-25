package br.ufsc.barcodescanner.utils;

import java.io.File;

public class FileUtils {

    public static void clearDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
            dir.delete();
        }
    }

}
