package com.github.denggeng.learning.core;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TestCrack
 * Created by dengg on 2017-05-18.
 */
public class TestCrack {

    @Test
    public void extra() {
        File folder = new File("D:\\tmp\\baicizan\\mp3");
        String extraFolder = "D:\\tmp\\baicizan\\extra";
        File[] files = folder.listFiles();
        for (File file : files) {
            String path = file.getPath();
            System.out.println(path);
            try {
                ZPackage zPackage = new ZPackage(path, 1);
                ZPacks.unpack(zPackage, extraFolder);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void extraSingle(File zpkFile, String preZpkFolder, String prefixExtraFolder) {
        String fullApkPath = zpkFile.getPath();
        String shortZpkPath = fullApkPath.replace(preZpkFolder, "");
        String fullExtractPath = prefixExtraFolder + shortZpkPath.replace(".zpk", "");
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

    @Test
    public void testExtractOne() {
        File file = new File("D:\\study\\words\\1\\0\\zp_130_1_0_14_170326020049.zpk");
        extraSingle(file, "D:\\study\\words", "D:\\study\\extract");
    }

    private void treeExtract(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File innerFile : files) {
                    treeExtract(innerFile);
                }
            }

        } else {
            if (file.getPath().endsWith(".zpk")) {
                extraSingle(file, "D:\\study\\words\\zpack", "D:\\study\\words\\extract");
            }
        }
    }

    @Test
    public void testExtractAll() {
        File wordsDir = new File("D:\\study\\words\\zpack");
        treeExtract(wordsDir);
    }

    @Test
    public void testSubList() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        List<String> list1 = list.subList(0, 1);
        System.out.println(list1.size());
    }
}
