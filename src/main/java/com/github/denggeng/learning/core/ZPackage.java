package com.github.denggeng.learning.core;

import ch.qos.logback.core.CoreConstants;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.zip.InflaterInputStream;

public class ZPackage {
    static final /* synthetic */ boolean $assertionsDisabled = (!ZPackage.class.desiredAssertionStatus());
    private static final int CURRENT_VERSION = 808465200;
    private static final Charset DEFAULT_CHARSET = Charset.forName("utf8");
    private static final int ENTRY_SIZE = 48;
    public static final int FILE_COMPRESS = 2;
    public static final int FILE_DELETE = 1;
    public static final int FILE_FLAG_USER0 = 1024;
    public static final int FILE_FLAG_USER1 = 2048;
    private static final int HASH_SEED = 131;
    private static final int HEADER_SIZE = 128;
    private static final int MIN_CHUNK_SIZE = 4096;
    public static final int OPEN_READONLY = 1;
    public static final int OPEN_READWRITE = 4;
    private static final int PACKAGE_FILE_SIGN = 1262571610;
    private byte[] mChunkData;
    private boolean mDirty = false;
    private ArrayList<FileEntry> mFileEntries = new ArrayList();
    private Hashtable<String, FileEntry> mHashTable = new Hashtable();
    private PackageHeader mHeader = new PackageHeader();
    private int mMode = 1;
    private long mPackageEnd;
    private RandomAccessFile mPackageFile;
    private String mPackageFilename;

    private static abstract class Struct {
        private Struct() {
        }

        abstract void read(InputStream inputStream) throws IOException;

        void read(byte[] bArr) throws IOException {
            read(new ByteArrayInputStream(bArr));
        }

        abstract void write(OutputStream outputStream) throws IOException;

        byte[] write() throws IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(48);
            write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }

    public static class FileEntry extends Struct {
        int availableSize;
        long byteOffset;
        int chunkSize;
        long contentHash;
        String filename;
        int flag;
        long nameHash;
        int originSize;
        int packSize;
        int reserved;

        public FileEntry() {
            super();
        }

        public int getAvailableSize() {
            return this.availableSize;
        }

        public long getByteOffset() {
            return this.byteOffset;
        }

        public String getFileName() {
            return this.filename;
        }

        @Override
        void read(InputStream inputStream) throws IOException {
            this.byteOffset = StructHelper.readLong(inputStream);
            this.nameHash = StructHelper.readLong(inputStream);
            this.packSize = StructHelper.readInt(inputStream);
            this.originSize = StructHelper.readInt(inputStream);
            this.flag = StructHelper.readInt(inputStream);
            this.chunkSize = StructHelper.readInt(inputStream);
            this.contentHash = StructHelper.readLong(inputStream);
            this.availableSize = StructHelper.readInt(inputStream);
            this.reserved = StructHelper.readInt(inputStream);
        }

        @Override
        public String toString() {
            return "FileEntry [filename=" + this.filename + ", byteOffset=" + this.byteOffset + ", nameHash=" + this.nameHash + ", packSize=" + this.packSize + ", originSize=" + this.originSize + ", flag=" + this.flag + ", chunkSize=" + this.chunkSize + ", contentHash=" + this.contentHash + ", availableSize=" + this.availableSize + ", reserved=" + this.reserved + "]";
        }

        @Override
        void write(OutputStream outputStream) throws IOException {
            StructHelper.writeLong(outputStream, this.byteOffset);
            StructHelper.writeLong(outputStream, this.nameHash);
            StructHelper.writeInt(outputStream, this.packSize);
            StructHelper.writeInt(outputStream, this.originSize);
            StructHelper.writeInt(outputStream, this.flag);
            StructHelper.writeInt(outputStream, this.chunkSize);
            StructHelper.writeLong(outputStream, this.contentHash);
            StructHelper.writeInt(outputStream, this.availableSize);
            StructHelper.writeInt(outputStream, this.reserved);
        }
    }

    private static class PackageHeader extends Struct {
        static final int RESERVED_COUNT = 18;
        int allFileEntrySize;
        int allFilenameSize;
        int chunkSize;
        int fileCount;
        long fileEntryOffset;
        int fileEntrySize;
        long filenameOffset;
        int flag;
        int headerSize;
        int originFilenamesSize;
        int[] reserved;
        int sign;
        int version;

        private PackageHeader() {
            super();
            this.reserved = new int[RESERVED_COUNT];
        }

        @Override
        void read(InputStream inputStream) throws IOException {
            this.sign = StructHelper.readInt(inputStream);
            this.version = StructHelper.readInt(inputStream);
            this.headerSize = StructHelper.readInt(inputStream);
            this.fileCount = StructHelper.readInt(inputStream);
            this.fileEntryOffset = StructHelper.readLong(inputStream);
            this.filenameOffset = StructHelper.readLong(inputStream);
            this.allFileEntrySize = StructHelper.readInt(inputStream);
            this.allFilenameSize = StructHelper.readInt(inputStream);
            this.originFilenamesSize = StructHelper.readInt(inputStream);
            this.chunkSize = StructHelper.readInt(inputStream);
            this.flag = StructHelper.readInt(inputStream);
            this.fileEntrySize = StructHelper.readInt(inputStream);
            for (int i = 0; i < RESERVED_COUNT; i++) {
                this.reserved[i] = StructHelper.readInt(inputStream);
            }
        }

        @Override
        public String toString() {
            return "PackageHeader [sign=" + this.sign + ", version=" + this.version + ", headerSize=" + this.headerSize + ", fileCount=" + this.fileCount + ", fileEntryOffset=" + this.fileEntryOffset + ", filenameOffset=" + this.filenameOffset + ", allFileEntrySize=" + this.allFileEntrySize + ", allFilenameSize=" + this.allFilenameSize + ", originFilenamesSize=" + this.originFilenamesSize + ", chunkSize=" + this.chunkSize + ", flag=" + this.flag + ", fileEntrySize=" + this.fileEntrySize + ", reserved=" + Arrays.toString(this.reserved) + "]";
        }

        @Override
        void write(OutputStream outputStream) throws IOException {
            StructHelper.writeInt(outputStream, this.sign);
            StructHelper.writeInt(outputStream, this.version);
            StructHelper.writeInt(outputStream, this.headerSize);
            StructHelper.writeInt(outputStream, this.fileCount);
            StructHelper.writeLong(outputStream, this.fileEntryOffset);
            StructHelper.writeLong(outputStream, this.filenameOffset);
            StructHelper.writeInt(outputStream, this.allFileEntrySize);
            StructHelper.writeInt(outputStream, this.allFilenameSize);
            StructHelper.writeInt(outputStream, this.originFilenamesSize);
            StructHelper.writeInt(outputStream, this.chunkSize);
            StructHelper.writeInt(outputStream, this.flag);
            StructHelper.writeInt(outputStream, this.fileEntrySize);
            for (int i = 0; i < RESERVED_COUNT; i++) {
                StructHelper.writeInt(outputStream, this.reserved[i]);
            }
        }
    }

    public ZPackage(String str, int i) throws IOException {
        this.mPackageFilename = str;
        this.mMode = i;
        if ((i & 1) > 0) {
            try {
                this.mPackageFile = new RandomAccessFile(str, "r");
            } catch (IOException e) {
                this.mPackageFile = null;
                throw e;
            }
        } else if ((i & 4) > 0) {
            this.mPackageFile = new RandomAccessFile(str, "rw");
            if (this.mPackageFile.length() == 0) {
                this.mHeader.sign = PACKAGE_FILE_SIGN;
                this.mHeader.version = CURRENT_VERSION;
                this.mHeader.headerSize = 128;
                this.mHeader.fileCount = 0;
                this.mHeader.fileEntrySize = 48;
                this.mHeader.fileEntryOffset = 128;
                this.mHeader.allFileEntrySize = 0;
                this.mHeader.filenameOffset = 128;
                this.mHeader.allFilenameSize = 0;
                this.mHeader.chunkSize = MIN_CHUNK_SIZE;
                this.mChunkData = new byte[MIN_CHUNK_SIZE];
                return;
            }
        }
        readHeader();
        readFileEntries();
        readFilenames();
        checkFileEntries();
        this.mPackageEnd = this.mHeader.filenameOffset + ((long) this.mHeader.allFilenameSize);
        if ((i & 4) > 0) {
            this.mChunkData = new byte[this.mHeader.chunkSize];
        }
    }

    private int addFileEntry(int i, FileEntry fileEntry) {
        synchronized (this) {
            this.mDirty = true;
            this.mFileEntries.add(i, fileEntry);
            PackageHeader packageHeader = this.mHeader;
            packageHeader.fileCount++;
            this.mHashTable.put(fileEntry.filename, fileEntry);
        }
        return i;
    }

    private void checkFileEntries() {
        if ((this.mMode & 1) <= 0) {
            Iterator it = this.mFileEntries.iterator();
            while (it.hasNext()) {
                if ((((FileEntry) it.next()).flag & 1) > 0) {
                    it.remove();
                    this.mDirty = true;
                }
            }
            this.mHeader.fileCount = this.mFileEntries.size();
        }
    }

    private byte[] decompress(InputStream inputStream) throws IOException {
        InflaterInputStream inflaterInputStream = new InflaterInputStream(inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(FILE_FLAG_USER0);
        byte[] bArr = new byte[FILE_FLAG_USER0];
        while (true) {
            int read = inflaterInputStream.read(bArr);
            if (read <= 0) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    private byte[] decompress(byte[] bArr) throws IOException {
        return decompress(new ByteArrayInputStream(bArr));
    }

    private int getFileIndex(String str) {
        FileEntry fileEntry = (FileEntry) this.mHashTable.get(str);
        return fileEntry == null ? -1 : this.mFileEntries.indexOf(fileEntry);
    }

    private void inplaceMoveAhead(long j, int i, int i2) throws IOException {
        synchronized (this) {
            if (i > 0 && i2 > 0) {
                long j2 = j - ((long) i2);
                long j3 = j;
                int i3 = i;
                while (i3 > 0) {
                    int i4 = i3 > this.mHeader.chunkSize ? this.mHeader.chunkSize : i3;
                    read(j3, this.mChunkData, 0, i4);
                    write(j2, this.mChunkData, 0, i4);
                    j3 += (long) i4;
                    j2 += (long) i4;
                    i3 -= i4;
                }
            }
        }
    }

    private int insertFileEntry(FileEntry fileEntry) {
        int addFileEntry;
        synchronized (this) {
            int size = this.mFileEntries.size();
            long j = (long) this.mHeader.headerSize;
            for (int i = 0; i < size; i++) {
                FileEntry fileEntry2 = (FileEntry) this.mFileEntries.get(i);
                if (fileEntry2.byteOffset >= ((long) fileEntry.packSize) + j && (((long) fileEntry.packSize) + j <= this.mHeader.fileEntryOffset || j >= this.mHeader.filenameOffset + ((long) this.mHeader.allFilenameSize))) {
                    fileEntry.byteOffset = j;
                    addFileEntry = addFileEntry(i, fileEntry);
                    break;
                }
                j = fileEntry2.byteOffset + ((long) fileEntry2.packSize);
            }
            if (size == 0 || this.mHeader.fileEntryOffset > ((long) fileEntry.packSize) + j) {
                fileEntry.byteOffset = j;
                if (fileEntry.byteOffset + ((long) fileEntry.packSize) > this.mPackageEnd) {
                    this.mPackageEnd = fileEntry.byteOffset + ((long) fileEntry.packSize);
                }
            } else {
                fileEntry.byteOffset = this.mPackageEnd;
                this.mPackageEnd += (long) fileEntry.packSize;
            }
            addFileEntry = addFileEntry(this.mFileEntries.size(), fileEntry);
        }
        return addFileEntry;
    }

    private void readFileEntries() throws IOException {
        synchronized (this) {
            this.mFileEntries.clear();
            if (this.mHeader.fileCount != 0) {
                byte[] bArr = new byte[this.mHeader.allFileEntrySize];
                read(this.mHeader.fileEntryOffset, bArr);
                InputStream byteArrayInputStream = this.mHeader.allFileEntrySize == this.mHeader.fileEntrySize * this.mHeader.fileCount ? new ByteArrayInputStream(bArr) : new ByteArrayInputStream(decompress(bArr));
                for (int i = 0; i < this.mHeader.fileCount; i++) {
                    FileEntry fileEntry = new FileEntry();
                    fileEntry.read(byteArrayInputStream);
                    this.mFileEntries.add(fileEntry);
                }
                byteArrayInputStream.close();
            }
        }
    }

    private void readFilenames() throws IOException {
        synchronized (this) {
            if (this.mHeader.fileCount != 0) {
                byte[] bArr = new byte[this.mHeader.allFilenameSize];
                read(this.mHeader.filenameOffset, bArr);
                BufferedReader bufferedReader = this.mHeader.allFilenameSize == this.mHeader.originFilenamesSize ? new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bArr), DEFAULT_CHARSET)) : new BufferedReader(new InputStreamReader(new ByteArrayInputStream(decompress(bArr)), DEFAULT_CHARSET));
                for (int i = 0; i < this.mHeader.fileCount; i++) {
                    String readLine = bufferedReader.readLine();
                    long stringHash = stringHash(readLine, HASH_SEED);
                    FileEntry fileEntry = (FileEntry) this.mFileEntries.get(i);
                    fileEntry.filename = readLine;
                    fileEntry.nameHash = stringHash;
                    this.mHashTable.put(readLine, fileEntry);
                }
            }
        }
    }

    private void readHeader() throws IOException {
        synchronized (this) {
            long length = this.mPackageFile.length();
            byte[] bArr = new byte[128];
            read(0, bArr, 0, 128);
            this.mHeader.read(bArr);
            if (this.mHeader.sign != PACKAGE_FILE_SIGN || this.mHeader.fileEntryOffset < ((long) this.mHeader.headerSize) || this.mHeader.fileEntryOffset + ((long) this.mHeader.allFileEntrySize) > length || this.mHeader.filenameOffset < this.mHeader.fileEntryOffset + ((long) this.mHeader.allFileEntrySize) || this.mHeader.filenameOffset + ((long) this.mHeader.allFilenameSize) > length) {
                throw new IOException("Package Header Error");
            }
        }
    }

    private void removeFileEntry(int i) {
        synchronized (this) {
            this.mDirty = true;
            FileEntry fileEntry = (FileEntry) this.mFileEntries.remove(i);
            PackageHeader packageHeader = this.mHeader;
            packageHeader.fileCount--;
            this.mHashTable.remove(Long.valueOf(fileEntry.nameHash));
        }
    }

    private static long stringHash(String str, int i) {
        long j = 0;
        for (int i2 = 0; i2 < str.length(); i2++) {
            char charAt = str.charAt(i2);
            if (charAt == CoreConstants.ESCAPE_CHAR) {
                charAt = '/';
            }
            j = (j * ((long) i)) + ((long) Character.toLowerCase(charAt));
        }
        return j;
    }

    private void writeHeader() throws IOException {
        synchronized (this) {
            write(0, this.mHeader.write());
        }
    }

    private void writeRawFile(FileEntry fileEntry, InputStream inputStream) throws IOException {
        synchronized (this) {
            this.mPackageFile.seek(fileEntry.byteOffset);
            int i = fileEntry.originSize;
            while (i > 0) {
                int i2 = i > this.mHeader.chunkSize ? this.mHeader.chunkSize : i;
                inputStream.read(this.mChunkData, 0, i2);
                this.mPackageFile.write(this.mChunkData, 0, i2);
                i -= i2;
            }
        }
    }

    private void writeTables(boolean z) throws IOException {
        int i = 0;
        synchronized (this) {
            if ($assertionsDisabled || this.mHeader.fileCount == this.mFileEntries.size()) {
                if (this.mFileEntries.isEmpty()) {
                    this.mHeader.fileCount = 0;
                    this.mHeader.allFileEntrySize = 0;
                    this.mHeader.allFilenameSize = 0;
                    this.mHeader.fileEntryOffset = 128;
                    this.mHeader.filenameOffset = this.mHeader.fileEntryOffset;
                    this.mHeader.originFilenamesSize = 0;
                } else {
                    int size = this.mFileEntries.size() * 48;
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(size);
                    for (int i2 = 0; i2 < this.mFileEntries.size(); i2++) {
                        ((FileEntry) this.mFileEntries.get(i2)).write(byteArrayOutputStream);
                    }
                    byte[] toByteArray = byteArrayOutputStream.toByteArray();
                    StringBuilder stringBuilder = new StringBuilder();
                    while (i < this.mFileEntries.size()) {
                        stringBuilder.append(((FileEntry) this.mFileEntries.get(i)).filename).append("\n");
                        i++;
                    }
                    byte[] bytes = stringBuilder.toString().getBytes(DEFAULT_CHARSET);
                    i = bytes.length;
                    FileEntry fileEntry = getFileEntry(this.mFileEntries.size() - 1);
                    long j = ((long) fileEntry.packSize) + fileEntry.byteOffset;
                    if (!z) {
                        this.mHeader.fileEntryOffset = j;
                    } else if (j >= this.mHeader.filenameOffset + ((long) this.mHeader.allFilenameSize) || (((long) size) + j) + ((long) i) <= this.mHeader.fileEntryOffset) {
                        this.mHeader.fileEntryOffset = j;
                    } else {
                        this.mHeader.fileEntryOffset = this.mHeader.filenameOffset + ((long) this.mHeader.allFilenameSize);
                    }
                    this.mPackageFile.seek(this.mHeader.fileEntryOffset);
                    this.mPackageFile.write(toByteArray);
                    this.mPackageFile.write(bytes);
                    this.mHeader.allFileEntrySize = size;
                    this.mHeader.filenameOffset = this.mHeader.fileEntryOffset + ((long) this.mHeader.allFileEntrySize);
                    this.mHeader.allFilenameSize = i;
                    this.mHeader.originFilenamesSize = i;
                    this.mPackageEnd = this.mHeader.filenameOffset + ((long) this.mHeader.allFilenameSize);
                    writeHeader();
                }
            } else {
                throw new AssertionError();
            }
        }
    }

    public void addFile(String str, String str2) throws IOException {
        IOException e;
        Throwable th;
        synchronized (this) {
            if ((this.mMode & 1) > 0) {
                throw new IOException("add file to read-only package");
            }
            int i = this.mHeader.chunkSize;
            File file = new File(str2);
            if (file.exists()) {
                int length = (int) file.length();
                InputStream bufferedInputStream;
                try {
                    bufferedInputStream = new BufferedInputStream(new FileInputStream(str2));
                    try {
                        int fileIndex = getFileIndex(str);
                        if (fileIndex >= 0) {
                            removeFileEntry(fileIndex);
                        }
                        FileEntry fileEntry = new FileEntry();
                        fileEntry.filename = str;
                        fileEntry.nameHash = stringHash(str, HASH_SEED);
                        fileEntry.packSize = length;
                        fileEntry.originSize = length;
                        fileEntry.flag = 0;
                        fileEntry.chunkSize = i;
                        fileEntry.contentHash = 0;
                        fileEntry.availableSize = length;
                        fileEntry.reserved = 0;
                        this.mDirty = true;
                        insertFileEntry(fileEntry);
                        if (length == 0) {
                            fileEntry.flag &= -3;
                        } else if ((fileEntry.flag & 2) == 0) {
                            writeRawFile(fileEntry, bufferedInputStream);
                        } else {
                            throw new IOException("Compressed mode not supported");
                        }
                        if (bufferedInputStream != null) {
                            bufferedInputStream.close();
                        }
                    } catch (IOException e2) {
                        e = e2;
                        try {
                            throw e;
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    }
                } catch (IOException e3) {
                    e = e3;
                    bufferedInputStream = null;
                    throw e;
                } catch (Throwable th3) {
                    th = th3;
                    bufferedInputStream = null;
                    if (bufferedInputStream != null) {
                        bufferedInputStream.close();
                    }
                }
            }
            throw new IOException("file not exists: " + str);
        }
    }

    public void close() {
        if (this.mPackageFile != null) {
            try {
                flush();
                this.mPackageFile.close();
            } catch (IOException e) {
            } finally {
                this.mPackageFile = null;
            }
        }
    }

    public OutputStream createFile(String str, int i) throws IOException {
        OutputStream outputStream = null;
        synchronized (this) {
            if ((this.mMode & 1) <= 0) {
                this.mDirty = true;
                int fileIndex = getFileIndex(str);
                if (fileIndex >= 0) {
                    removeFileEntry(fileIndex);
                }
                FileEntry fileEntry = new FileEntry();
                fileEntry.filename = str;
                fileEntry.nameHash = stringHash(str, HASH_SEED);
                fileEntry.flag = 0;
                fileEntry.packSize = i;
                fileEntry.originSize = 0;
                fileEntry.contentHash = 0;
                fileEntry.availableSize = 0;
                fileEntry.reserved = 0;
                fileEntry.chunkSize = this.mHeader.chunkSize;
                if (insertFileEntry(fileEntry) >= 0) {
                    outputStream = new ZPackOutputStream(this, fileEntry);
                }
            }
        }
        return outputStream;
    }

    public void defrag() throws IOException {
        synchronized (this) {
            if ((this.mMode & 1) <= 0 && !this.mDirty) {
                long j = (long) this.mHeader.headerSize;
                long j2 = (long) this.mHeader.headerSize;
                int i = 0;
                int i2 = 0;
                for (int i3 = 0; i3 < this.mFileEntries.size(); i3++) {
                    FileEntry fileEntry = getFileEntry(i3);
                    if (fileEntry.packSize == 0) {
                        fileEntry.byteOffset = j2;
                    } else if ($assertionsDisabled || ((long) i2) + j == ((long) i) + j2) {
                        if (fileEntry.byteOffset != ((long) i2) + j) {
                            inplaceMoveAhead(j, i2, i);
                            i = (int) (fileEntry.byteOffset - j2);
                            j = fileEntry.byteOffset;
                            i2 = 0;
                        }
                        if (fileEntry.byteOffset != j2) {
                            fileEntry.byteOffset = j2;
                            j2 += (long) fileEntry.packSize;
                            i2 += fileEntry.packSize;
                        } else {
                            fileEntry.byteOffset = j2;
                            j2 += (long) fileEntry.packSize;
                            i2 += fileEntry.packSize;
                        }
                    } else {
                        throw new AssertionError();
                    }
                }
                if (i > 0) {
                    inplaceMoveAhead(j, i2, i);
                }
                writeTables(false);
                this.mPackageFile.setLength(this.mPackageEnd);
            }
        }
    }

    public void flush() throws IOException {
        synchronized (this) {
            if ((this.mMode & 1) <= 0 && this.mDirty) {
                writeTables(false);
                if (this.mHeader.filenameOffset + ((long) this.mHeader.allFilenameSize) > this.mPackageEnd) {
                    this.mPackageEnd = this.mHeader.filenameOffset + ((long) this.mHeader.allFilenameSize);
                }
                this.mDirty = false;
            }
        }
    }

    public FileDescriptor getFD() throws IOException {
        FileDescriptor fd;
        synchronized (this) {
            fd = this.mPackageFile.getFD();
        }
        return fd;
    }

    public int getFileCount() {
        return this.mHeader.fileCount;
    }

    public FileEntry getFileEntry(int i) {
        FileEntry fileEntry;
        synchronized (this) {
            fileEntry = (FileEntry) this.mFileEntries.get(i);
        }
        return fileEntry;
    }

    public FileEntry getFileEntry(String str) {
        FileEntry fileEntry;
        synchronized (this) {
            int fileIndex = getFileIndex(str);
            fileEntry = fileIndex < 0 ? null : getFileEntry(fileIndex);
        }
        return fileEntry;
    }

    public String getPackageFilename() {
        return this.mPackageFilename;
    }

    public boolean hasFile(String str) {
        boolean z;
        synchronized (this) {
            z = getFileIndex(str) >= 0;
        }
        return z;
    }

    public boolean isValid() {
        return this.mPackageFile != null;
    }

    public InputStream openFile(String str) throws IOException {
        InputStream zPackInputStream;
        synchronized (this) {
            int fileIndex = getFileIndex(str);
            if (fileIndex < 0) {
                throw new IOException("file not exists " + str);
            }
            FileEntry fileEntry = getFileEntry(fileIndex);
            if ((fileEntry.flag & 2) == 0) {
                zPackInputStream = new ZPackInputStream(this, fileEntry.byteOffset, fileEntry.availableSize);
            } else {
                throw new IOException("Compressed mode not supported");
            }
        }
        return zPackInputStream;
    }

    public OutputStream openFileToWrite(String str) {
        OutputStream outputStream = null;
        synchronized (this) {
            if ((this.mMode & 1) <= 0) {
                int fileIndex = getFileIndex(str);
                if (fileIndex >= 0) {
                    outputStream = new ZPackOutputStream(this, getFileEntry(fileIndex));
                }
            }
        }
        return outputStream;
    }

    int read(long j) throws IOException {
        int read;
        synchronized (this) {
            if (this.mPackageFile.getFilePointer() != j) {
                this.mPackageFile.seek(j);
            }
            read = this.mPackageFile.read();
        }
        return read;
    }

    int read(long j, byte[] bArr) throws IOException {
        int read;
        synchronized (this) {
            read = read(j, bArr, 0, bArr.length);
        }
        return read;
    }

    int read(long j, byte[] bArr, int i, int i2) throws IOException {
        int read;
        synchronized (this) {
            if (this.mPackageFile.getFilePointer() != j) {
                this.mPackageFile.seek(j);
            }
            read = this.mPackageFile.read(bArr, i, i2);
        }
        return read;
    }

    public void removeFile(String str) {
        synchronized (this) {
            if ((this.mMode & 1) <= 0) {
                int fileIndex = getFileIndex(str);
                if (fileIndex >= 0) {
                    removeFileEntry(fileIndex);
                    this.mDirty = true;
                }
            }
        }
    }

    public boolean setFileAvailableSize(FileEntry fileEntry, int i) {
        synchronized (this) {
            fileEntry.availableSize = i;
            this.mDirty = true;
        }
        return true;
    }

    void write(long j, int i) throws IOException {
        synchronized (this) {
            if (this.mPackageFile.getFilePointer() != j) {
                this.mPackageFile.seek(j);
            }
            this.mPackageFile.write(i);
        }
    }

    void write(long j, byte[] bArr) throws IOException {
        synchronized (this) {
            write(j, bArr, 0, bArr.length);
        }
    }

    void write(long j, byte[] bArr, int i, int i2) throws IOException {
        synchronized (this) {
            if (this.mPackageFile.getFilePointer() != j) {
                this.mPackageFile.seek(j);
            }
            this.mPackageFile.write(bArr, i, i2);
        }
    }
}
