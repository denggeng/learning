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
    public void play() {
        String bip = "D:\\tmp\\baicizan\\mp3\\zp_1828_11_0_12_161221133010.mp3";
        Mp3File mp3file = null;
        try {
            mp3file = new Mp3File(bip);
            System.out.println("Length of this mp3 is: " + mp3file.getLengthInSeconds() + " seconds");
            System.out.println("Bitrate: " + mp3file.getBitrate() + " kbps " + (mp3file.isVbr() ? "(VBR)" : "(CBR)"));
            System.out.println("Sample rate: " + mp3file.getSampleRate() + " Hz");
            System.out.println("Has ID3v1 tag?: " + (mp3file.hasId3v1Tag() ? "YES" : "NO"));
            System.out.println("Has ID3v2 tag?: " + (mp3file.hasId3v2Tag() ? "YES" : "NO"));
            System.out.println("Has custom tag?: " + (mp3file.hasCustomTag() ? "YES" : "NO"));

            if (mp3file.hasId3v1Tag()) {
                ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                System.out.println("Track: " + id3v1Tag.getTrack());
                System.out.println("Artist: " + id3v1Tag.getArtist());
                System.out.println("Title: " + id3v1Tag.getTitle());
                System.out.println("Album: " + id3v1Tag.getAlbum());
                System.out.println("Year: " + id3v1Tag.getYear());
                System.out.println("Genre: " + id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")");
                System.out.println("Comment: " + id3v1Tag.getComment());
            }

            if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                System.out.println("Track: " + id3v2Tag.getTrack());
                System.out.println("Artist: " + id3v2Tag.getArtist());
                System.out.println("Title: " + id3v2Tag.getTitle());
                System.out.println("Album: " + id3v2Tag.getAlbum());
                System.out.println("Year: " + id3v2Tag.getYear());
                System.out.println("Genre: " + id3v2Tag.getGenre() + " (" + id3v2Tag.getGenreDescription() + ")");
                System.out.println("Comment: " + id3v2Tag.getComment());
                System.out.println("Lyrics: " + id3v2Tag.getLyrics());
                System.out.println("Composer: " + id3v2Tag.getComposer());
                System.out.println("Publisher: " + id3v2Tag.getPublisher());
                System.out.println("Original artist: " + id3v2Tag.getOriginalArtist());
                System.out.println("Album artist: " + id3v2Tag.getAlbumArtist());
                System.out.println("Copyright: " + id3v2Tag.getCopyright());
                System.out.println("URL: " + id3v2Tag.getUrl());
                System.out.println("Encoder: " + id3v2Tag.getEncoder());
                byte[] albumImageData = id3v2Tag.getAlbumImage();
                if (albumImageData != null) {
                    System.out.println("Have album image data, length: " + albumImageData.length + " bytes");
                    System.out.println("Album image mime type: " + id3v2Tag.getAlbumImageMimeType());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }

    }
}
