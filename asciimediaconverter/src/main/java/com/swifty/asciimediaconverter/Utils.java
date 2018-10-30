package com.swifty.asciimediaconverter;

import java.io.File;

/**
 * Created by Swifty Wang on 30/10/2018.
 */
public class Utils {

    public static boolean checkFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            return true;
        }
        return false;
    }

    public static boolean deleteDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f.getPath());
            }
        }
        return file.delete();
    }

    public static void mkdirs(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
