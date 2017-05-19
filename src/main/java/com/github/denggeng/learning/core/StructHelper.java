package com.github.denggeng.learning.core;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StructHelper {
    private static int read(InputStream inputStream) throws IOException {
        int read = inputStream.read();
        if (read != -1) {
            return read;
        }
        throw new EOFException();
    }

    static int readInt(InputStream inputStream) throws IOException {
        return ((((read(inputStream) << 0) | 0) | (read(inputStream) << 8)) | (read(inputStream) << 16)) | (read(inputStream) << 24);
    }

    static long readLong(InputStream inputStream) throws IOException {
        return 0x0L | (0xFFL & read(inputStream)) << 0 | (0xFFL & read(inputStream)) << 8 | (0xFFL & read(inputStream)) << 16 | (0xFFL & read(inputStream)) << 24 | (0xFFL & read(inputStream)) << 32 | (0xFFL & read(inputStream)) << 40 | (0xFFL & read(inputStream)) << 48 | (0xFFL & read(inputStream)) << 56;
    }

    static String readString(InputStream inputStream) throws IOException {
        return new String(streamToBytes(inputStream, (int) readLong(inputStream)), "utf8");
    }

    private static byte[] streamToBytes(InputStream inputStream, int i) throws IOException {
        byte[] bArr = new byte[i];
        int i2 = 0;
        while (i2 < i) {
            int read = inputStream.read(bArr, i2, i - i2);
            if (read == -1) {
                break;
            }
            i2 += read;
        }
        if (i2 == i) {
            return bArr;
        }
        throw new IOException("Expected " + i + " bytes, read " + i2 + " bytes");
    }

    static void writeInt(OutputStream outputStream, int i) throws IOException {
        outputStream.write((i >> 0) & 255);
        outputStream.write((i >> 8) & 255);
        outputStream.write((i >> 16) & 255);
        outputStream.write((i >> 24) & 255);
    }

    static void writeLong(OutputStream outputStream, long j) throws IOException {
        outputStream.write((byte) ((int) (j >>> 0)));
        outputStream.write((byte) ((int) (j >>> 8)));
        outputStream.write((byte) ((int) (j >>> 16)));
        outputStream.write((byte) ((int) (j >>> 24)));
        outputStream.write((byte) ((int) (j >>> 32)));
        outputStream.write((byte) ((int) (j >>> 40)));
        outputStream.write((byte) ((int) (j >>> 48)));
        outputStream.write((byte) ((int) (j >>> 56)));
    }

    static void writeString(OutputStream outputStream, String str) throws IOException {
        byte[] bytes = str.getBytes("utf8");
        writeLong(outputStream, (long) bytes.length);
        outputStream.write(bytes, 0, bytes.length);
    }
}
