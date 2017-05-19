package com.github.denggeng.learning.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.github.denggeng.learning.core.ZPackage.FileEntry;

public class ZPacks {
    public static void extract(ZPackage zPackage, String str, String str2) throws IOException {
        OutputStream bufferedOutputStream;
        IOException e;
        Throwable th;
        IOException iOException;
        InputStream bufferedInputStream;
        try {
            bufferedInputStream = new BufferedInputStream(zPackage.openFile(str));
            try {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(str2));
                try {
                    byte[] bArr = new byte[4096];
                    while (true) {
                        int read = bufferedInputStream.read(bArr);
                        if (read <= 0) {
                            break;
                        }
                        bufferedOutputStream.write(bArr, 0, read);
                    }
                    if (bufferedInputStream != null) {
                        bufferedInputStream.close();
                    }
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.flush();
                        bufferedOutputStream.close();
                    }
                } catch (IOException e2) {
                    e = e2;
                    try {
                        throw e;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (bufferedInputStream != null) {
                        bufferedInputStream.close();
                    }
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.flush();
                        bufferedOutputStream.close();
                    }
                    throw th;
                }
            } catch (IOException e3) {
                iOException = e3;
                bufferedOutputStream = null;
                e = iOException;
                throw e;
            } catch (Throwable th4) {
                th = th4;
                bufferedOutputStream = null;
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            bufferedOutputStream = null;
            iOException = e4;
            bufferedInputStream = null;
            e = iOException;
            throw e;
        } catch (Throwable th5) {
            bufferedOutputStream = null;
            Throwable th6 = th5;
            bufferedInputStream = null;
            th = th6;
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
            if (bufferedOutputStream != null) {
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
        }
    }

    public static void merge(ZPackage zPackage, ZPackage zPackage2) throws IOException {
        byte[] bArr = new byte[4096];
        for (int i = 0; i < zPackage.getFileCount(); i++) {
            FileEntry fileEntry = zPackage.getFileEntry(i);
            InputStream openFile = zPackage.openFile(fileEntry.filename);
            OutputStream createFile = zPackage2.createFile(fileEntry.filename, fileEntry.availableSize);
            while (true) {
                int read = openFile.read(bArr);
                if (read <= 0) {
                    break;
                }
                createFile.write(bArr, 0, read);
            }
            openFile.close();
            createFile.close();
        }
        zPackage2.flush();
    }

    public static void pack(ZPackage zPackage, String str) throws IOException {
        File file = new File(str);
        if (file.exists()) {
            packImpl(zPackage, file);
            zPackage.close();
            return;
        }
        throw new IOException("path not exists: " + str);
    }

    private static void packImpl(ZPackage zPackage, File file) throws IOException {
        System.out.println("packImpl" + file.getPath());
        if (file.isDirectory()) {
            for (File packImpl : file.listFiles()) {
                packImpl(zPackage, packImpl);
            }
        } else if (file.isFile() && !file.isHidden()) {
            zPackage.addFile(file.getPath(), file.getPath());
            System.out.println("inject " + file.getPath());
        }
    }

    public static void unpack(ZPackage zPackage) throws IOException {
        unpack(zPackage, FileHelper.splitext(zPackage.getPackageFilename())[0]);
    }

    public static void unpack(ZPackage zPackage, String str) throws IOException {
        if (FileHelper.makePath(str)) {
            for (int i = 0; i < zPackage.getFileCount(); i++) {
                FileEntry fileEntry = zPackage.getFileEntry(i);
                String str2 = str + File.separator + fileEntry.filename;
                FileHelper.createFile(str2);
                extract(zPackage, fileEntry.filename, str2);
            }
            return;
        }
        throw new IOException("Cannot create unpack dir: " + str);
    }
}
