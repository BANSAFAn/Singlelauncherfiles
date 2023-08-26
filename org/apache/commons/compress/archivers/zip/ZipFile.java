/*      */ package org.apache.commons.compress.archivers.zip;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.Closeable;
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.SequenceInputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.SeekableByteChannel;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.StandardOpenOption;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.zip.Inflater;
/*      */ import java.util.zip.ZipException;
/*      */ import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
/*      */ import org.apache.commons.compress.compressors.deflate64.Deflate64CompressorInputStream;
/*      */ import org.apache.commons.compress.utils.BoundedArchiveInputStream;
/*      */ import org.apache.commons.compress.utils.BoundedSeekableByteChannelInputStream;
/*      */ import org.apache.commons.compress.utils.CountingInputStream;
/*      */ import org.apache.commons.compress.utils.IOUtils;
/*      */ import org.apache.commons.compress.utils.InputStreamStatistics;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ZipFile
/*      */   implements Closeable
/*      */ {
/*      */   private static final int HASH_SIZE = 509;
/*      */   static final int NIBLET_MASK = 15;
/*      */   static final int BYTE_SHIFT = 8;
/*      */   private static final int POS_0 = 0;
/*      */   private static final int POS_1 = 1;
/*      */   private static final int POS_2 = 2;
/*      */   private static final int POS_3 = 3;
/*  100 */   private static final byte[] ONE_ZERO_BYTE = new byte[1];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  106 */   private final List<ZipArchiveEntry> entries = new LinkedList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  112 */   private final Map<String, LinkedList<ZipArchiveEntry>> nameMap = new HashMap<>(509);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final String encoding;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final ZipEncoding zipEncoding;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final String archiveName;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final SeekableByteChannel archive;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean useUnicodeExtraFields;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private volatile boolean closed = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean isSplitZipArchive;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  155 */   private final byte[] dwordBuf = new byte[8];
/*  156 */   private final byte[] wordBuf = new byte[4];
/*  157 */   private final byte[] cfhBuf = new byte[42];
/*  158 */   private final byte[] shortBuf = new byte[2];
/*  159 */   private final ByteBuffer dwordBbuf = ByteBuffer.wrap(this.dwordBuf);
/*  160 */   private final ByteBuffer wordBbuf = ByteBuffer.wrap(this.wordBuf);
/*  161 */   private final ByteBuffer cfhBbuf = ByteBuffer.wrap(this.cfhBuf);
/*  162 */   private final ByteBuffer shortBbuf = ByteBuffer.wrap(this.shortBuf);
/*      */ 
/*      */   
/*      */   private long centralDirectoryStartDiskNumber;
/*      */   
/*      */   private long centralDirectoryStartRelativeOffset;
/*      */   
/*      */   private long centralDirectoryStartOffset;
/*      */   
/*      */   private static final int CFH_LEN = 42;
/*      */ 
/*      */   
/*      */   public ZipFile(File f) throws IOException {
/*  175 */     this(f, "UTF8");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(Path path) throws IOException {
/*  185 */     this(path, "UTF8");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(String name) throws IOException {
/*  196 */     this((new File(name)).toPath(), "UTF8");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(String name, String encoding) throws IOException {
/*  210 */     this((new File(name)).toPath(), encoding, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(File f, String encoding) throws IOException {
/*  224 */     this(f.toPath(), encoding, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(Path path, String encoding) throws IOException {
/*  237 */     this(path, encoding, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(File f, String encoding, boolean useUnicodeExtraFields) throws IOException {
/*  254 */     this(f.toPath(), encoding, useUnicodeExtraFields, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(Path path, String encoding, boolean useUnicodeExtraFields) throws IOException {
/*  270 */     this(path, encoding, useUnicodeExtraFields, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(File f, String encoding, boolean useUnicodeExtraFields, boolean ignoreLocalFileHeader) throws IOException {
/*  301 */     this(Files.newByteChannel(f.toPath(), EnumSet.of(StandardOpenOption.READ), (FileAttribute<?>[])new FileAttribute[0]), f
/*  302 */         .getAbsolutePath(), encoding, useUnicodeExtraFields, true, ignoreLocalFileHeader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(Path path, String encoding, boolean useUnicodeExtraFields, boolean ignoreLocalFileHeader) throws IOException {
/*  329 */     this(Files.newByteChannel(path, EnumSet.of(StandardOpenOption.READ), (FileAttribute<?>[])new FileAttribute[0]), path
/*  330 */         .toAbsolutePath().toString(), encoding, useUnicodeExtraFields, true, ignoreLocalFileHeader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(SeekableByteChannel channel) throws IOException {
/*  348 */     this(channel, "unknown archive", "UTF8", true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(SeekableByteChannel channel, String encoding) throws IOException {
/*  368 */     this(channel, "unknown archive", encoding, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(SeekableByteChannel channel, String archiveName, String encoding, boolean useUnicodeExtraFields) throws IOException {
/*  392 */     this(channel, archiveName, encoding, useUnicodeExtraFields, false, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(SeekableByteChannel channel, String archiveName, String encoding, boolean useUnicodeExtraFields, boolean ignoreLocalFileHeader) throws IOException {
/*  428 */     this(channel, archiveName, encoding, useUnicodeExtraFields, false, ignoreLocalFileHeader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getEncoding() {
/*  467 */     return this.encoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() throws IOException {
/*  479 */     this.closed = true;
/*      */     
/*  481 */     this.archive.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(ZipFile zipfile) {
/*  490 */     IOUtils.closeQuietly(zipfile);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Enumeration<ZipArchiveEntry> getEntries() {
/*  502 */     return Collections.enumeration(this.entries);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Enumeration<ZipArchiveEntry> getEntriesInPhysicalOrder() {
/*  516 */     ZipArchiveEntry[] allEntries = this.entries.<ZipArchiveEntry>toArray(ZipArchiveEntry.EMPTY_ZIP_ARCHIVE_ENTRY_ARRAY);
/*  517 */     Arrays.sort(allEntries, this.offsetComparator);
/*  518 */     return Collections.enumeration(Arrays.asList(allEntries));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipArchiveEntry getEntry(String name) {
/*  534 */     LinkedList<ZipArchiveEntry> entriesOfThatName = this.nameMap.get(name);
/*  535 */     return (entriesOfThatName != null) ? entriesOfThatName.getFirst() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterable<ZipArchiveEntry> getEntries(String name) {
/*  548 */     List<ZipArchiveEntry> entriesOfThatName = this.nameMap.get(name);
/*  549 */     return (entriesOfThatName != null) ? entriesOfThatName : 
/*  550 */       Collections.<ZipArchiveEntry>emptyList();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterable<ZipArchiveEntry> getEntriesInPhysicalOrder(String name) {
/*  563 */     ZipArchiveEntry[] entriesOfThatName = ZipArchiveEntry.EMPTY_ZIP_ARCHIVE_ENTRY_ARRAY;
/*  564 */     if (this.nameMap.containsKey(name)) {
/*  565 */       entriesOfThatName = (ZipArchiveEntry[])((LinkedList)this.nameMap.get(name)).toArray((Object[])entriesOfThatName);
/*  566 */       Arrays.sort(entriesOfThatName, this.offsetComparator);
/*      */     } 
/*  568 */     return Arrays.asList(entriesOfThatName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canReadEntryData(ZipArchiveEntry ze) {
/*  581 */     return ZipUtil.canHandleEntryData(ze);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InputStream getRawInputStream(ZipArchiveEntry ze) {
/*  595 */     if (!(ze instanceof Entry)) {
/*  596 */       return null;
/*      */     }
/*  598 */     long start = ze.getDataOffset();
/*  599 */     if (start == -1L) {
/*  600 */       return null;
/*      */     }
/*  602 */     return (InputStream)createBoundedInputStream(start, ze.getCompressedSize());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void copyRawEntries(ZipArchiveOutputStream target, ZipArchiveEntryPredicate predicate) throws IOException {
/*  617 */     Enumeration<ZipArchiveEntry> src = getEntriesInPhysicalOrder();
/*  618 */     while (src.hasMoreElements()) {
/*  619 */       ZipArchiveEntry entry = src.nextElement();
/*  620 */       if (predicate.test(entry)) {
/*  621 */         target.addRawArchiveEntry(entry, getRawInputStream(entry));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InputStream getInputStream(ZipArchiveEntry ze) throws IOException {
/*      */     final Inflater inflater;
/*  636 */     if (!(ze instanceof Entry)) {
/*  637 */       return null;
/*      */     }
/*      */     
/*  640 */     ZipUtil.checkRequestedFeatures(ze);
/*  641 */     long start = getDataOffset(ze);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  647 */     InputStream is = new BufferedInputStream((InputStream)createBoundedInputStream(start, ze.getCompressedSize()));
/*  648 */     switch (ZipMethod.getMethodByCode(ze.getMethod())) {
/*      */       case STORED:
/*  650 */         return (InputStream)new StoredStatisticsStream(is);
/*      */       case UNSHRINKING:
/*  652 */         return (InputStream)new UnshrinkingInputStream(is);
/*      */       case IMPLODING:
/*      */         try {
/*  655 */           return new ExplodingInputStream(ze.getGeneralPurposeBit().getSlidingDictionarySize(), ze
/*  656 */               .getGeneralPurposeBit().getNumberOfShannonFanoTrees(), is);
/*  657 */         } catch (IllegalArgumentException ex) {
/*  658 */           throw new IOException("bad IMPLODE data", ex);
/*      */         } 
/*      */       case DEFLATED:
/*  661 */         inflater = new Inflater(true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  667 */         return new InflaterInputStreamWithStatistics(new SequenceInputStream(is, new ByteArrayInputStream(ONE_ZERO_BYTE)), inflater)
/*      */           {
/*      */             public void close() throws IOException
/*      */             {
/*      */               try {
/*  672 */                 super.close();
/*      */               } finally {
/*  674 */                 inflater.end();
/*      */               } 
/*      */             }
/*      */           };
/*      */       case BZIP2:
/*  679 */         return (InputStream)new BZip2CompressorInputStream(is);
/*      */       case ENHANCED_DEFLATED:
/*  681 */         return (InputStream)new Deflate64CompressorInputStream(is);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  696 */     throw new UnsupportedZipFeatureException(ZipMethod.getMethodByCode(ze.getMethod()), ze);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUnixSymlink(ZipArchiveEntry entry) throws IOException {
/*  715 */     if (entry != null && entry.isUnixSymlink()) {
/*  716 */       try (InputStream in = getInputStream(entry)) {
/*  717 */         return this.zipEncoding.decode(IOUtils.toByteArray(in));
/*      */       } 
/*      */     }
/*  720 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void finalize() throws Throwable {
/*      */     try {
/*  731 */       if (!this.closed) {
/*  732 */         System.err.println("Cleaning up unclosed ZipFile for archive " + this.archiveName);
/*      */         
/*  734 */         close();
/*      */       } 
/*      */     } finally {
/*  737 */       super.finalize();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  764 */   private static final long CFH_SIG = ZipLong.getValue(ZipArchiveOutputStream.CFH_SIG);
/*      */   static final int MIN_EOCD_SIZE = 22;
/*      */   private static final int MAX_EOCD_SIZE = 65557;
/*      */   private static final int CFD_LOCATOR_OFFSET = 16;
/*      */   private static final int CFD_DISK_OFFSET = 6;
/*      */   private static final int CFD_LOCATOR_RELATIVE_OFFSET = 8;
/*      */   private static final int ZIP64_EOCDL_LENGTH = 20;
/*      */   private static final int ZIP64_EOCDL_LOCATOR_OFFSET = 8;
/*      */   private static final int ZIP64_EOCD_CFD_LOCATOR_OFFSET = 48;
/*      */   private static final int ZIP64_EOCD_CFD_DISK_OFFSET = 20;
/*      */   private static final int ZIP64_EOCD_CFD_LOCATOR_RELATIVE_OFFSET = 24;
/*      */   private static final long LFH_OFFSET_FOR_FILENAME_LENGTH = 26L;
/*      */   private final Comparator<ZipArchiveEntry> offsetComparator;
/*      */   
/*      */   private Map<ZipArchiveEntry, NameAndComment> populateFromCentralDirectory() throws IOException {
/*  779 */     HashMap<ZipArchiveEntry, NameAndComment> noUTF8Flag = new HashMap<>();
/*      */ 
/*      */     
/*  782 */     positionAtCentralDirectory();
/*  783 */     this.centralDirectoryStartOffset = this.archive.position();
/*      */     
/*  785 */     this.wordBbuf.rewind();
/*  786 */     IOUtils.readFully(this.archive, this.wordBbuf);
/*  787 */     long sig = ZipLong.getValue(this.wordBuf);
/*      */     
/*  789 */     if (sig != CFH_SIG && startsWithLocalFileHeader()) {
/*  790 */       throw new IOException("Central directory is empty, can't expand corrupt archive.");
/*      */     }
/*      */ 
/*      */     
/*  794 */     while (sig == CFH_SIG) {
/*  795 */       readCentralDirectoryEntry(noUTF8Flag);
/*  796 */       this.wordBbuf.rewind();
/*  797 */       IOUtils.readFully(this.archive, this.wordBbuf);
/*  798 */       sig = ZipLong.getValue(this.wordBuf);
/*      */     } 
/*  800 */     return noUTF8Flag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void readCentralDirectoryEntry(Map<ZipArchiveEntry, NameAndComment> noUTF8Flag) throws IOException {
/*  815 */     this.cfhBbuf.rewind();
/*  816 */     IOUtils.readFully(this.archive, this.cfhBbuf);
/*  817 */     int off = 0;
/*  818 */     Entry ze = new Entry();
/*      */     
/*  820 */     int versionMadeBy = ZipShort.getValue(this.cfhBuf, off);
/*  821 */     off += 2;
/*  822 */     ze.setVersionMadeBy(versionMadeBy);
/*  823 */     ze.setPlatform(versionMadeBy >> 8 & 0xF);
/*      */     
/*  825 */     ze.setVersionRequired(ZipShort.getValue(this.cfhBuf, off));
/*  826 */     off += 2;
/*      */     
/*  828 */     GeneralPurposeBit gpFlag = GeneralPurposeBit.parse(this.cfhBuf, off);
/*  829 */     boolean hasUTF8Flag = gpFlag.usesUTF8ForNames();
/*  830 */     ZipEncoding entryEncoding = hasUTF8Flag ? ZipEncodingHelper.UTF8_ZIP_ENCODING : this.zipEncoding;
/*      */     
/*  832 */     if (hasUTF8Flag) {
/*  833 */       ze.setNameSource(ZipArchiveEntry.NameSource.NAME_WITH_EFS_FLAG);
/*      */     }
/*  835 */     ze.setGeneralPurposeBit(gpFlag);
/*  836 */     ze.setRawFlag(ZipShort.getValue(this.cfhBuf, off));
/*      */     
/*  838 */     off += 2;
/*      */ 
/*      */     
/*  841 */     ze.setMethod(ZipShort.getValue(this.cfhBuf, off));
/*  842 */     off += 2;
/*      */     
/*  844 */     long time = ZipUtil.dosToJavaTime(ZipLong.getValue(this.cfhBuf, off));
/*  845 */     ze.setTime(time);
/*  846 */     off += 4;
/*      */     
/*  848 */     ze.setCrc(ZipLong.getValue(this.cfhBuf, off));
/*  849 */     off += 4;
/*      */     
/*  851 */     long size = ZipLong.getValue(this.cfhBuf, off);
/*  852 */     if (size < 0L) {
/*  853 */       throw new IOException("broken archive, entry with negative compressed size");
/*      */     }
/*  855 */     ze.setCompressedSize(size);
/*  856 */     off += 4;
/*      */     
/*  858 */     size = ZipLong.getValue(this.cfhBuf, off);
/*  859 */     if (size < 0L) {
/*  860 */       throw new IOException("broken archive, entry with negative size");
/*      */     }
/*  862 */     ze.setSize(size);
/*  863 */     off += 4;
/*      */     
/*  865 */     int fileNameLen = ZipShort.getValue(this.cfhBuf, off);
/*  866 */     off += 2;
/*  867 */     if (fileNameLen < 0) {
/*  868 */       throw new IOException("broken archive, entry with negative fileNameLen");
/*      */     }
/*      */     
/*  871 */     int extraLen = ZipShort.getValue(this.cfhBuf, off);
/*  872 */     off += 2;
/*  873 */     if (extraLen < 0) {
/*  874 */       throw new IOException("broken archive, entry with negative extraLen");
/*      */     }
/*      */     
/*  877 */     int commentLen = ZipShort.getValue(this.cfhBuf, off);
/*  878 */     off += 2;
/*  879 */     if (commentLen < 0) {
/*  880 */       throw new IOException("broken archive, entry with negative commentLen");
/*      */     }
/*      */     
/*  883 */     ze.setDiskNumberStart(ZipShort.getValue(this.cfhBuf, off));
/*  884 */     off += 2;
/*      */     
/*  886 */     ze.setInternalAttributes(ZipShort.getValue(this.cfhBuf, off));
/*  887 */     off += 2;
/*      */     
/*  889 */     ze.setExternalAttributes(ZipLong.getValue(this.cfhBuf, off));
/*  890 */     off += 4;
/*      */     
/*  892 */     byte[] fileName = IOUtils.readRange(this.archive, fileNameLen);
/*  893 */     if (fileName.length < fileNameLen) {
/*  894 */       throw new EOFException();
/*      */     }
/*  896 */     ze.setName(entryEncoding.decode(fileName), fileName);
/*      */ 
/*      */     
/*  899 */     ze.setLocalHeaderOffset(ZipLong.getValue(this.cfhBuf, off));
/*      */     
/*  901 */     this.entries.add(ze);
/*      */     
/*  903 */     byte[] cdExtraData = IOUtils.readRange(this.archive, extraLen);
/*  904 */     if (cdExtraData.length < extraLen) {
/*  905 */       throw new EOFException();
/*      */     }
/*      */     try {
/*  908 */       ze.setCentralDirectoryExtra(cdExtraData);
/*  909 */     } catch (RuntimeException ex) {
/*  910 */       ZipException z = new ZipException("Invalid extra data in entry " + ze.getName());
/*  911 */       z.initCause(ex);
/*  912 */       throw z;
/*      */     } 
/*      */     
/*  915 */     setSizesAndOffsetFromZip64Extra(ze);
/*  916 */     sanityCheckLFHOffset(ze);
/*      */     
/*  918 */     byte[] comment = IOUtils.readRange(this.archive, commentLen);
/*  919 */     if (comment.length < commentLen) {
/*  920 */       throw new EOFException();
/*      */     }
/*  922 */     ze.setComment(entryEncoding.decode(comment));
/*      */     
/*  924 */     if (!hasUTF8Flag && this.useUnicodeExtraFields) {
/*  925 */       noUTF8Flag.put(ze, new NameAndComment(fileName, comment));
/*      */     }
/*      */     
/*  928 */     ze.setStreamContiguous(true);
/*      */   }
/*      */   
/*      */   private void sanityCheckLFHOffset(ZipArchiveEntry ze) throws IOException {
/*  932 */     if (ze.getDiskNumberStart() < 0L) {
/*  933 */       throw new IOException("broken archive, entry with negative disk number");
/*      */     }
/*  935 */     if (ze.getLocalHeaderOffset() < 0L) {
/*  936 */       throw new IOException("broken archive, entry with negative local file header offset");
/*      */     }
/*  938 */     if (this.isSplitZipArchive) {
/*  939 */       if (ze.getDiskNumberStart() > this.centralDirectoryStartDiskNumber) {
/*  940 */         throw new IOException("local file header for " + ze.getName() + " starts on a later disk than central directory");
/*      */       }
/*  942 */       if (ze.getDiskNumberStart() == this.centralDirectoryStartDiskNumber && ze
/*  943 */         .getLocalHeaderOffset() > this.centralDirectoryStartRelativeOffset) {
/*  944 */         throw new IOException("local file header for " + ze.getName() + " starts after central directory");
/*      */       }
/*  946 */     } else if (ze.getLocalHeaderOffset() > this.centralDirectoryStartOffset) {
/*  947 */       throw new IOException("local file header for " + ze.getName() + " starts after central directory");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setSizesAndOffsetFromZip64Extra(ZipArchiveEntry ze) throws IOException {
/*  966 */     ZipExtraField extra = ze.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
/*  967 */     if (extra != null && !(extra instanceof Zip64ExtendedInformationExtraField)) {
/*  968 */       throw new ZipException("archive contains unparseable zip64 extra field");
/*      */     }
/*  970 */     Zip64ExtendedInformationExtraField z64 = (Zip64ExtendedInformationExtraField)extra;
/*      */     
/*  972 */     if (z64 != null) {
/*  973 */       boolean hasUncompressedSize = (ze.getSize() == 4294967295L);
/*  974 */       boolean hasCompressedSize = (ze.getCompressedSize() == 4294967295L);
/*      */       
/*  976 */       boolean hasRelativeHeaderOffset = (ze.getLocalHeaderOffset() == 4294967295L);
/*  977 */       boolean hasDiskStart = (ze.getDiskNumberStart() == 65535L);
/*  978 */       z64.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, hasDiskStart);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  983 */       if (hasUncompressedSize) {
/*  984 */         long size = z64.getSize().getLongValue();
/*  985 */         if (size < 0L) {
/*  986 */           throw new IOException("broken archive, entry with negative size");
/*      */         }
/*  988 */         ze.setSize(size);
/*  989 */       } else if (hasCompressedSize) {
/*  990 */         z64.setSize(new ZipEightByteInteger(ze.getSize()));
/*      */       } 
/*      */       
/*  993 */       if (hasCompressedSize) {
/*  994 */         long size = z64.getCompressedSize().getLongValue();
/*  995 */         if (size < 0L) {
/*  996 */           throw new IOException("broken archive, entry with negative compressed size");
/*      */         }
/*  998 */         ze.setCompressedSize(size);
/*  999 */       } else if (hasUncompressedSize) {
/* 1000 */         z64.setCompressedSize(new ZipEightByteInteger(ze.getCompressedSize()));
/*      */       } 
/*      */       
/* 1003 */       if (hasRelativeHeaderOffset) {
/* 1004 */         ze.setLocalHeaderOffset(z64.getRelativeHeaderOffset().getLongValue());
/*      */       }
/*      */       
/* 1007 */       if (hasDiskStart) {
/* 1008 */         ze.setDiskNumberStart(z64.getDiskStartNumber().getValue());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void positionAtCentralDirectory() throws IOException {
/* 1164 */     positionAtEndOfCentralDirectoryRecord();
/* 1165 */     boolean found = false;
/*      */     
/* 1167 */     boolean searchedForZip64EOCD = (this.archive.position() > 20L);
/* 1168 */     if (searchedForZip64EOCD) {
/* 1169 */       this.archive.position(this.archive.position() - 20L);
/* 1170 */       this.wordBbuf.rewind();
/* 1171 */       IOUtils.readFully(this.archive, this.wordBbuf);
/* 1172 */       found = Arrays.equals(ZipArchiveOutputStream.ZIP64_EOCD_LOC_SIG, this.wordBuf);
/*      */     } 
/*      */     
/* 1175 */     if (!found) {
/*      */       
/* 1177 */       if (searchedForZip64EOCD) {
/* 1178 */         skipBytes(16);
/*      */       }
/* 1180 */       positionAtCentralDirectory32();
/*      */     } else {
/* 1182 */       positionAtCentralDirectory64();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void positionAtCentralDirectory64() throws IOException {
/* 1197 */     if (this.isSplitZipArchive) {
/* 1198 */       this.wordBbuf.rewind();
/* 1199 */       IOUtils.readFully(this.archive, this.wordBbuf);
/* 1200 */       long diskNumberOfEOCD = ZipLong.getValue(this.wordBuf);
/*      */       
/* 1202 */       this.dwordBbuf.rewind();
/* 1203 */       IOUtils.readFully(this.archive, this.dwordBbuf);
/* 1204 */       long relativeOffsetOfEOCD = ZipEightByteInteger.getLongValue(this.dwordBuf);
/* 1205 */       ((ZipSplitReadOnlySeekableByteChannel)this.archive)
/* 1206 */         .position(diskNumberOfEOCD, relativeOffsetOfEOCD);
/*      */     } else {
/* 1208 */       skipBytes(4);
/*      */       
/* 1210 */       this.dwordBbuf.rewind();
/* 1211 */       IOUtils.readFully(this.archive, this.dwordBbuf);
/* 1212 */       this.archive.position(ZipEightByteInteger.getLongValue(this.dwordBuf));
/*      */     } 
/*      */     
/* 1215 */     this.wordBbuf.rewind();
/* 1216 */     IOUtils.readFully(this.archive, this.wordBbuf);
/* 1217 */     if (!Arrays.equals(this.wordBuf, ZipArchiveOutputStream.ZIP64_EOCD_SIG)) {
/* 1218 */       throw new ZipException("Archive's ZIP64 end of central directory locator is corrupt.");
/*      */     }
/*      */ 
/*      */     
/* 1222 */     if (this.isSplitZipArchive) {
/* 1223 */       skipBytes(16);
/*      */       
/* 1225 */       this.wordBbuf.rewind();
/* 1226 */       IOUtils.readFully(this.archive, this.wordBbuf);
/* 1227 */       this.centralDirectoryStartDiskNumber = ZipLong.getValue(this.wordBuf);
/*      */       
/* 1229 */       skipBytes(24);
/*      */       
/* 1231 */       this.dwordBbuf.rewind();
/* 1232 */       IOUtils.readFully(this.archive, this.dwordBbuf);
/* 1233 */       this.centralDirectoryStartRelativeOffset = ZipEightByteInteger.getLongValue(this.dwordBuf);
/* 1234 */       ((ZipSplitReadOnlySeekableByteChannel)this.archive)
/* 1235 */         .position(this.centralDirectoryStartDiskNumber, this.centralDirectoryStartRelativeOffset);
/*      */     } else {
/* 1237 */       skipBytes(44);
/*      */       
/* 1239 */       this.dwordBbuf.rewind();
/* 1240 */       IOUtils.readFully(this.archive, this.dwordBbuf);
/* 1241 */       this.centralDirectoryStartDiskNumber = 0L;
/* 1242 */       this.centralDirectoryStartRelativeOffset = ZipEightByteInteger.getLongValue(this.dwordBuf);
/* 1243 */       this.archive.position(this.centralDirectoryStartRelativeOffset);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void positionAtCentralDirectory32() throws IOException {
/* 1256 */     if (this.isSplitZipArchive) {
/* 1257 */       skipBytes(6);
/* 1258 */       this.shortBbuf.rewind();
/* 1259 */       IOUtils.readFully(this.archive, this.shortBbuf);
/* 1260 */       this.centralDirectoryStartDiskNumber = ZipShort.getValue(this.shortBuf);
/*      */       
/* 1262 */       skipBytes(8);
/*      */       
/* 1264 */       this.wordBbuf.rewind();
/* 1265 */       IOUtils.readFully(this.archive, this.wordBbuf);
/* 1266 */       this.centralDirectoryStartRelativeOffset = ZipLong.getValue(this.wordBuf);
/* 1267 */       ((ZipSplitReadOnlySeekableByteChannel)this.archive)
/* 1268 */         .position(this.centralDirectoryStartDiskNumber, this.centralDirectoryStartRelativeOffset);
/*      */     } else {
/* 1270 */       skipBytes(16);
/* 1271 */       this.wordBbuf.rewind();
/* 1272 */       IOUtils.readFully(this.archive, this.wordBbuf);
/* 1273 */       this.centralDirectoryStartDiskNumber = 0L;
/* 1274 */       this.centralDirectoryStartRelativeOffset = ZipLong.getValue(this.wordBuf);
/* 1275 */       this.archive.position(this.centralDirectoryStartRelativeOffset);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void positionAtEndOfCentralDirectoryRecord() throws IOException {
/* 1285 */     boolean found = tryToLocateSignature(22L, 65557L, ZipArchiveOutputStream.EOCD_SIG);
/*      */     
/* 1287 */     if (!found) {
/* 1288 */       throw new ZipException("Archive is not a ZIP archive");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean tryToLocateSignature(long minDistanceFromEnd, long maxDistanceFromEnd, byte[] sig) throws IOException {
/* 1300 */     boolean found = false;
/* 1301 */     long off = this.archive.size() - minDistanceFromEnd;
/*      */     
/* 1303 */     long stopSearching = Math.max(0L, this.archive.size() - maxDistanceFromEnd);
/* 1304 */     if (off >= 0L) {
/* 1305 */       for (; off >= stopSearching; off--) {
/* 1306 */         this.archive.position(off);
/*      */         try {
/* 1308 */           this.wordBbuf.rewind();
/* 1309 */           IOUtils.readFully(this.archive, this.wordBbuf);
/* 1310 */           this.wordBbuf.flip();
/* 1311 */         } catch (EOFException ex) {
/*      */           break;
/*      */         } 
/* 1314 */         int curr = this.wordBbuf.get();
/* 1315 */         if (curr == sig[0]) {
/* 1316 */           curr = this.wordBbuf.get();
/* 1317 */           if (curr == sig[1]) {
/* 1318 */             curr = this.wordBbuf.get();
/* 1319 */             if (curr == sig[2]) {
/* 1320 */               curr = this.wordBbuf.get();
/* 1321 */               if (curr == sig[3]) {
/* 1322 */                 found = true;
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/* 1330 */     if (found) {
/* 1331 */       this.archive.position(off);
/*      */     }
/* 1333 */     return found;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void skipBytes(int count) throws IOException {
/* 1341 */     long currentPosition = this.archive.position();
/* 1342 */     long newPosition = currentPosition + count;
/* 1343 */     if (newPosition > this.archive.size()) {
/* 1344 */       throw new EOFException();
/*      */     }
/* 1346 */     this.archive.position(newPosition);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void resolveLocalFileHeaderData(Map<ZipArchiveEntry, NameAndComment> entriesWithoutUTF8Flag) throws IOException {
/* 1374 */     for (ZipArchiveEntry zipArchiveEntry : this.entries) {
/*      */ 
/*      */       
/* 1377 */       Entry ze = (Entry)zipArchiveEntry;
/* 1378 */       int[] lens = setDataOffset(ze);
/* 1379 */       int fileNameLen = lens[0];
/* 1380 */       int extraFieldLen = lens[1];
/* 1381 */       skipBytes(fileNameLen);
/* 1382 */       byte[] localExtraData = IOUtils.readRange(this.archive, extraFieldLen);
/* 1383 */       if (localExtraData.length < extraFieldLen) {
/* 1384 */         throw new EOFException();
/*      */       }
/*      */       try {
/* 1387 */         ze.setExtra(localExtraData);
/* 1388 */       } catch (RuntimeException ex) {
/* 1389 */         ZipException z = new ZipException("Invalid extra data in entry " + ze.getName());
/* 1390 */         z.initCause(ex);
/* 1391 */         throw z;
/*      */       } 
/*      */       
/* 1394 */       if (entriesWithoutUTF8Flag.containsKey(ze)) {
/* 1395 */         NameAndComment nc = entriesWithoutUTF8Flag.get(ze);
/* 1396 */         ZipUtil.setNameAndCommentFromExtraFields(ze, nc.name, nc
/* 1397 */             .comment);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void fillNameMap() {
/* 1403 */     this.entries.forEach(ze -> {
/*      */           String name = ze.getName();
/*      */           LinkedList<ZipArchiveEntry> entriesOfThatName = this.nameMap.computeIfAbsent(name, ());
/*      */           entriesOfThatName.addLast(ze);
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int[] setDataOffset(ZipArchiveEntry ze) throws IOException {
/* 1413 */     long offset = ze.getLocalHeaderOffset();
/* 1414 */     if (this.isSplitZipArchive) {
/* 1415 */       ((ZipSplitReadOnlySeekableByteChannel)this.archive)
/* 1416 */         .position(ze.getDiskNumberStart(), offset + 26L);
/*      */       
/* 1418 */       offset = this.archive.position() - 26L;
/*      */     } else {
/* 1420 */       this.archive.position(offset + 26L);
/*      */     } 
/* 1422 */     this.wordBbuf.rewind();
/* 1423 */     IOUtils.readFully(this.archive, this.wordBbuf);
/* 1424 */     this.wordBbuf.flip();
/* 1425 */     this.wordBbuf.get(this.shortBuf);
/* 1426 */     int fileNameLen = ZipShort.getValue(this.shortBuf);
/* 1427 */     this.wordBbuf.get(this.shortBuf);
/* 1428 */     int extraFieldLen = ZipShort.getValue(this.shortBuf);
/* 1429 */     ze.setDataOffset(offset + 26L + 2L + 2L + fileNameLen + extraFieldLen);
/*      */     
/* 1431 */     if (ze.getDataOffset() + ze.getCompressedSize() > this.centralDirectoryStartOffset) {
/* 1432 */       throw new IOException("data for " + ze.getName() + " overlaps with central directory.");
/*      */     }
/* 1434 */     return new int[] { fileNameLen, extraFieldLen };
/*      */   }
/*      */   
/*      */   private long getDataOffset(ZipArchiveEntry ze) throws IOException {
/* 1438 */     long s = ze.getDataOffset();
/* 1439 */     if (s == -1L) {
/* 1440 */       setDataOffset(ze);
/* 1441 */       return ze.getDataOffset();
/*      */     } 
/* 1443 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean startsWithLocalFileHeader() throws IOException {
/* 1451 */     this.archive.position(0L);
/* 1452 */     this.wordBbuf.rewind();
/* 1453 */     IOUtils.readFully(this.archive, this.wordBbuf);
/* 1454 */     return Arrays.equals(this.wordBuf, ZipArchiveOutputStream.LFH_SIG);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private BoundedArchiveInputStream createBoundedInputStream(long start, long remaining) {
/* 1462 */     if (start < 0L || remaining < 0L || start + remaining < start) {
/* 1463 */       throw new IllegalArgumentException("Corrupted archive, stream boundaries are out of range");
/*      */     }
/*      */     
/* 1466 */     return (this.archive instanceof FileChannel) ? new BoundedFileChannelInputStream(start, remaining) : (BoundedArchiveInputStream)new BoundedSeekableByteChannelInputStream(start, remaining, this.archive);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class BoundedFileChannelInputStream
/*      */     extends BoundedArchiveInputStream
/*      */   {
/*      */     private final FileChannel archive;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     BoundedFileChannelInputStream(long start, long remaining) {
/* 1481 */       super(start, remaining);
/* 1482 */       this.archive = (FileChannel)ZipFile.this.archive;
/*      */     }
/*      */ 
/*      */     
/*      */     protected int read(long pos, ByteBuffer buf) throws IOException {
/* 1487 */       int read = this.archive.read(buf, pos);
/* 1488 */       buf.flip();
/* 1489 */       return read;
/*      */     } }
/*      */   
/*      */   private static final class NameAndComment {
/*      */     private final byte[] name;
/*      */     private final byte[] comment;
/*      */     
/*      */     private NameAndComment(byte[] name, byte[] comment) {
/* 1497 */       this.name = name;
/* 1498 */       this.comment = comment;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ZipFile(SeekableByteChannel channel, String archiveName, String encoding, boolean useUnicodeExtraFields, boolean closeOnError, boolean ignoreLocalFileHeader) throws IOException {
/* 1510 */     this
/*      */       
/* 1512 */       .offsetComparator = Comparator.<ZipArchiveEntry>comparingLong(ZipArchiveEntry::getDiskNumberStart).thenComparingLong(ZipArchiveEntry::getLocalHeaderOffset); this.isSplitZipArchive = channel instanceof ZipSplitReadOnlySeekableByteChannel; this.archiveName = archiveName; this.encoding = encoding; this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding); this.useUnicodeExtraFields = useUnicodeExtraFields; this.archive = channel; boolean success = false; try {
/*      */       Map<ZipArchiveEntry, NameAndComment> entriesWithoutUTF8Flag = populateFromCentralDirectory();
/*      */       if (!ignoreLocalFileHeader)
/*      */         resolveLocalFileHeaderData(entriesWithoutUTF8Flag); 
/*      */       fillNameMap();
/*      */       success = true;
/*      */     } catch (IOException e) {
/*      */       throw new IOException("Error on ZipFile " + archiveName, e);
/*      */     } finally {
/*      */       this.closed = !success;
/*      */       if (!success && closeOnError)
/*      */         IOUtils.closeQuietly(this.archive); 
/* 1524 */     }  } private static class Entry extends ZipArchiveEntry { public int hashCode() { return 3 * super.hashCode() + 
/* 1525 */         (int)getLocalHeaderOffset() + (int)(getLocalHeaderOffset() >> 32L); }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/* 1530 */       if (super.equals(other)) {
/*      */         
/* 1532 */         Entry otherEntry = (Entry)other;
/* 1533 */         return (getLocalHeaderOffset() == otherEntry
/* 1534 */           .getLocalHeaderOffset() && 
/* 1535 */           getDataOffset() == otherEntry
/* 1536 */           .getDataOffset() && 
/* 1537 */           getDiskNumberStart() == otherEntry
/* 1538 */           .getDiskNumberStart());
/*      */       } 
/* 1540 */       return false;
/*      */     } }
/*      */ 
/*      */   
/*      */   private static class StoredStatisticsStream extends CountingInputStream implements InputStreamStatistics {
/*      */     StoredStatisticsStream(InputStream in) {
/* 1546 */       super(in);
/*      */     }
/*      */ 
/*      */     
/*      */     public long getCompressedCount() {
/* 1551 */       return getBytesRead();
/*      */     }
/*      */ 
/*      */     
/*      */     public long getUncompressedCount() {
/* 1556 */       return getCompressedCount();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\zip\ZipFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */