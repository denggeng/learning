package com.github.denggeng.learning.word;

import com.github.denggeng.learning.core.ZPackage;
import com.github.denggeng.learning.core.ZPacks;

import java.io.File;
import java.io.IOException;

/**
 * 解包
 *
 * @author Kenny
 */
public class UnPack {

    public static void main(String[] args) {
        System.out.println(args[0]);
        new UnPack().extractAll(args[0], args[1]);
    }

    private void extraSingle(File zpkFile, String preZpkFolder, String prefixExtraFolder) {
        String fullApkPath = zpkFile.getPath();
        String shortZpkPath = fullApkPath.replace(preZpkFolder, "");
        String fullExtractPath = prefixExtraFolder + shortZpkPath.replace(".zpk", "");
        System.out.println("preZpkFolder:" + preZpkFolder);
        System.out.println("prefixExtraFolder:" + prefixExtraFolder);
        System.out.println("shortZpkPath:" + shortZpkPath);
        System.out.println("fullExtractPath:" + fullExtractPath);
        //Path extraPath = Paths.get(fullExtractPath);
        File extraFolder = new File(fullExtractPath);

        if (!extraFolder.exists()) {
            boolean mkdirs = extraFolder.mkdirs();
        }
        ZPackage zPackage = null;
        try {
            zPackage = new ZPackage(fullApkPath, 1);
            ZPacks.unpack(zPackage, extraFolder.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void treeExtract(File file, String prefixZpkFolder, String prefixExtraFolder) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File innerFile : files) {
                    treeExtract(innerFile, prefixZpkFolder, prefixExtraFolder);
                }
            }

        } else {
            if (file.getPath().endsWith(".zpk")) {
                extraSingle(file, prefixZpkFolder, prefixExtraFolder);
            }
        }
    }

    public void extractAll(String sourceDir, String targetDir) {
        String prefixZpkFolder = sourceDir;
        String prefixExtraFolder = targetDir;
        File wordsDir = new File(prefixZpkFolder);
        treeExtract(wordsDir, prefixZpkFolder, prefixExtraFolder);
    }


}
