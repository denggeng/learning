package com.github.denggeng.learning.core;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

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
}
