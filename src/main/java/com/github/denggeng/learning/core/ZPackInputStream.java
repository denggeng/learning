package com.github.denggeng.learning.core;

import java.io.IOException;
import java.io.InputStream;

public class ZPackInputStream extends InputStream {
    private long mOffset;
    final ZPackage mPackage;
    private int mReadPos = 0;
    private int mSize;

    public ZPackInputStream(ZPackage zPackage, long j, int i) {
        this.mPackage = zPackage;
        this.mOffset = j;
        this.mSize = i;
    }

    @Override
    public int available() throws IOException {
        return this.mSize;
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public int read() throws IOException {
        if (this.mReadPos >= this.mSize || this.mSize == 0) {
            return -1;
        }
        int read = this.mPackage.read(this.mOffset + ((long) this.mReadPos));
        this.mReadPos++;
        return read;
    }

    @Override
    public int read(byte[] bArr, int i, int i2) throws IOException {
        if ((i | i2) < 0 || i > bArr.length || bArr.length - i < i2) {
            throw new IOException("ArrayIndexOutOffBound");
        }
        int i3 = this.mReadPos + i2 > this.mSize ? this.mSize - this.mReadPos : i2;
        if (i3 > 0) {
            int read = this.mPackage.read(this.mOffset + ((long) this.mReadPos), bArr, i, i3);
            if (read >= 0) {
                this.mReadPos += read;
                return read;
            }
        }
        return -1;
    }
}
