package com.github.denggeng.learning.core;

import com.mpatric.mp3agic.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.commons.io.IOUtils;
import org.assertj.core.util.Compatibility;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * TestCrack
 * Created by dengg on 2017-05-18.
 */
public class TestCrack {

    @Test
    public void getJpg() {
        try {
            FileInputStream fis = new FileInputStream("D:\\tmp\\baicizan\\mp3\\zp_1828_11_0_12_161221133010.mp3");
            byte[] startBytes = {-128,};
            byte[] endBytes = {-128, 0};
            byte[] tmp = new byte[2];
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\tmp\\baicizan\\1.jpg");
            boolean start = false;
            for (; IOUtils.read(fis, tmp) > 0; ) {
                System.out.println(((int) tmp[0] + " " + ((int) tmp[1])));
                if (((int) tmp[0]) == 0xff && ((int) tmp[1]) == 0xd8) {
                    start = true;
                } else if (((int) tmp[0]) == 0xff && ((int) tmp[1]) == 0xd9) {
                    start = false;
                }
                if (start) {
                    fileOutputStream.write(tmp);
                }
            }
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMulti() {

        read("D:\\tmp\\baicizan\\mp3\\zp_185_11_0_13_161221131838.mp3");
        read("D:\\tmp\\baicizan\\mp3\\zp_188_11_0_10_161221131838.mp3");
        read("D:\\tmp\\baicizan\\mp3\\zp_295_11_0_10_161221131934.mp3");
        read("D:\\tmp\\baicizan\\mp3\\zp_395_11_0_9_161221132040.mp3");
        read("D:\\tmp\\baicizan\\mp3\\zp_1828_11_0_12_161221133010.mp3");
        read("D:\\tmp\\baicizan\\mp3\\zp_2081_11_0_11_161221133206.mp3");
        read("D:\\tmp\\baicizan\\mp3\\zp_3023_11_0_10_161221133619.mp3");
        read("D:\\tmp\\baicizan\\mp3\\zp_3909_11_0_6_161221133921.mp3");

    }

    public void read(String bip) {
        Mp3File mp3file = null;
        try {
            mp3file = new Mp3File(bip);
            //System.out.println("Length of this mp3 is: " + mp3file.getLengthInSeconds() + " seconds");
            //System.out.println("Bitrate: " + mp3file.getBitrate() + " kbps " + (mp3file.isVbr() ? "(VBR)" : "(CBR)"));
            //System.out.println("Sample rate: " + mp3file.getSampleRate() + " Hz");
            //System.out.println("Has ID3v1 tag?: " + (mp3file.hasId3v1Tag() ? "YES" : "NO"));
            //System.out.println("Has ID3v2 tag?: " + (mp3file.hasId3v2Tag() ? "YES" : "NO"));
            //System.out.println("Has custom tag?: " + (mp3file.hasCustomTag() ? "YES" : "NO"));
            String cuntomTag = new String(mp3file.getCustomTag(), "UTF-8");
            String[] cuntomTags = cuntomTag.split("\\n");
            System.out.println("start>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+bip);
            System.out.println("Custom tag: " + cuntomTag);
            //System.out.println("Custom tag0: " + cuntomTags[0] + "#" + cuntomTags[1] + "#" + cuntomTags[2] + "#" + cuntomTags[3]);
            System.out.println("end>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }

    }
}
