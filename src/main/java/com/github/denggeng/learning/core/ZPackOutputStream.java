package com.github.denggeng.learning.core;

import com.github.denggeng.learning.core.ZPackage.FileEntry;
import java.io.IOException;
import java.io.OutputStream;

public class ZPackOutputStream extends OutputStream {
    private FileEntry mEntry;
    private long mOffset;
    final ZPackage mPackage;
    private int mSize;
    private int mWritePos = 0;

    public ZPackOutputStream(ZPackage zPackage, FileEntry fileEntry) {
        this.mPackage = zPackage;
        this.mOffset = fileEntry.byteOffset;
        this.mSize = fileEntry.packSize;
        this.mEntry = fileEntry;
    }

    @Override
    public void write(int i) throws IOException {
        if (this.mWritePos >= this.mSize) {
            throw new IOException("Cannt write anymore");
        }
        this.mPackage.write(this.mOffset + ((long) this.mWritePos), i);
        this.mWritePos++;
    }

    @Override
    public void write(byte[] bArr, int i, int i2) throws IOException {
        if (this.mWritePos + i2 > this.mSize) {
            throw new IOException("Cannt write anymore");
        }
        this.mPackage.write(this.mOffset + ((long) this.mWritePos), bArr, i, i2);
        this.mWritePos += i2;
        if (!this.mPackage.setFileAvailableSize(this.mEntry, this.mWritePos)) {
            this.mSize = 0;
        }
    }
}
