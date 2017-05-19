package com.github.denggeng.learning.core;

import java.io.File;
import java.io.IOException;

public class FileHelper {
    public static boolean createFile(String str) {
        int lastIndexOf = str.lastIndexOf(File.separator);
        if (lastIndexOf > 0 && !makePath(str.substring(0, lastIndexOf + 1))) {
            return false;
        }
        File file = new File(str);
        if (file.exists()) {
            return false;
        }
        try {
            file.createNewFile();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            for (File deleteFile : file.listFiles()) {
                deleteFile(deleteFile);
            }
        }
        file.delete();
    }

    public static void deleteFile(String str) {
        File file = new File(str);
        if (file != null) {
            deleteFile(file);
        }
    }

    public static boolean makePath(String str) {
        File file = new File(str);
        return file == null ? false : file.exists() ? true : file.mkdirs();
    }

    public static boolean mkdir(String str) {
        File file = new File(str);
        return file.isDirectory() ? true : file.mkdir();
    }

    public static boolean renameFile(String str, String str2) {
        File file = new File(str);
        File file2 = new File(str2);
        return (file == null || file2 == null) ? false : file.renameTo(file2);
    }

    public static String[] splitext(String str) {
        if (str.lastIndexOf(46) > 0) {
            return new String[]{str.substring(0, str.lastIndexOf(46)), str.substring(str.lastIndexOf(46) + 1)};
        }
        return new String[]{str, ""};
    }
}
