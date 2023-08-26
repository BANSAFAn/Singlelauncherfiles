/*      */ package org.apache.commons.compress.archivers.zip;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.SeekableByteChannel;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.LinkOption;
/*      */ import java.nio.file.OpenOption;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.StandardOpenOption;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.util.Calendar;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.zip.Deflater;
/*      */ import java.util.zip.ZipException;
/*      */ import org.apache.commons.compress.archivers.ArchiveEntry;
/*      */ import org.apache.commons.compress.archivers.ArchiveOutputStream;
/*      */ import org.apache.commons.compress.utils.ByteUtils;
/*      */ import org.apache.commons.compress.utils.IOUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ZipArchiveOutputStream
/*      */   extends ArchiveOutputStream
/*      */ {
/*      */   static final int BUFFER_SIZE = 512;
/*      */   private static final int LFH_SIG_OFFSET = 0;
/*      */   private static final int LFH_VERSION_NEEDED_OFFSET = 4;
/*      */   private static final int LFH_GPB_OFFSET = 6;
/*      */   private static final int LFH_METHOD_OFFSET = 8;
/*      */   private static final int LFH_TIME_OFFSET = 10;
/*      */   private static final int LFH_CRC_OFFSET = 14;
/*      */   private static final int LFH_COMPRESSED_SIZE_OFFSET = 18;
/*      */   private static final int LFH_ORIGINAL_SIZE_OFFSET = 22;
/*      */   private static final int LFH_FILENAME_LENGTH_OFFSET = 26;
/*      */   private static final int LFH_EXTRA_LENGTH_OFFSET = 28;
/*      */   private static final int LFH_FILENAME_OFFSET = 30;
/*      */   private static final int CFH_SIG_OFFSET = 0;
/*      */   private static final int CFH_VERSION_MADE_BY_OFFSET = 4;
/*      */   private static final int CFH_VERSION_NEEDED_OFFSET = 6;
/*      */   private static final int CFH_GPB_OFFSET = 8;
/*      */   private static final int CFH_METHOD_OFFSET = 10;
/*      */   private static final int CFH_TIME_OFFSET = 12;
/*      */   private static final int CFH_CRC_OFFSET = 16;
/*      */   private static final int CFH_COMPRESSED_SIZE_OFFSET = 20;
/*      */   private static final int CFH_ORIGINAL_SIZE_OFFSET = 24;
/*      */   private static final int CFH_FILENAME_LENGTH_OFFSET = 28;
/*      */   private static final int CFH_EXTRA_LENGTH_OFFSET = 30;
/*      */   private static final int CFH_COMMENT_LENGTH_OFFSET = 32;
/*      */   private static final int CFH_DISK_NUMBER_OFFSET = 34;
/*      */   private static final int CFH_INTERNAL_ATTRIBUTES_OFFSET = 36;
/*      */   private static final int CFH_EXTERNAL_ATTRIBUTES_OFFSET = 38;
/*      */   private static final int CFH_LFH_OFFSET = 42;
/*      */   private static final int CFH_FILENAME_OFFSET = 46;
/*      */   protected boolean finished;
/*      */   public static final int DEFLATED = 8;
/*      */   public static final int DEFAULT_COMPRESSION = -1;
/*      */   public static final int STORED = 0;
/*      */   static final String DEFAULT_ENCODING = "UTF8";
/*      */   @Deprecated
/*      */   public static final int EFS_FLAG = 2048;
/*      */   private CurrentEntry entry;
/*  158 */   private String comment = "";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  163 */   private int level = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasCompressionLevelChanged;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  174 */   private int method = 8;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  179 */   private final List<ZipArchiveEntry> entries = new LinkedList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final StreamCompressor streamCompressor;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long cdOffset;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long cdLength;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long cdDiskNumberStart;
/*      */ 
/*      */ 
/*      */   
/*      */   private long eocdLength;
/*      */ 
/*      */ 
/*      */   
/*  207 */   private static final byte[] ZERO = new byte[] { 0, 0 };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  212 */   private static final byte[] LZERO = new byte[] { 0, 0, 0, 0 };
/*      */   
/*  214 */   private static final byte[] ONE = ZipLong.getBytes(1L);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  219 */   private final Map<ZipArchiveEntry, EntryMetaData> metaData = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  229 */   private String encoding = "UTF8";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  238 */   private ZipEncoding zipEncoding = ZipEncodingHelper.getZipEncoding("UTF8");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Deflater def;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final SeekableByteChannel channel;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final OutputStream outputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useUTF8Flag = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean fallbackToUTF8;
/*      */ 
/*      */ 
/*      */   
/*  267 */   private UnicodeExtraFieldPolicy createUnicodeExtraFields = UnicodeExtraFieldPolicy.NEVER;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasUsedZip64;
/*      */ 
/*      */ 
/*      */   
/*  276 */   private Zip64Mode zip64Mode = Zip64Mode.AsNeeded;
/*      */   
/*  278 */   private final byte[] copyBuffer = new byte[32768];
/*  279 */   private final Calendar calendarInstance = Calendar.getInstance();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean isSplitZip;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  290 */   private final Map<Integer, Integer> numberOfCDInDiskData = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipArchiveOutputStream(OutputStream out) {
/*  297 */     this.outputStream = out;
/*  298 */     this.channel = null;
/*  299 */     this.def = new Deflater(this.level, true);
/*  300 */     this.streamCompressor = StreamCompressor.create(out, this.def);
/*  301 */     this.isSplitZip = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipArchiveOutputStream(File file) throws IOException {
/*  311 */     this(file.toPath(), new OpenOption[0]);
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
/*      */   public ZipArchiveOutputStream(Path file, OpenOption... options) throws IOException {
/*  323 */     this.def = new Deflater(this.level, true);
/*  324 */     OutputStream outputStream = null;
/*  325 */     SeekableByteChannel channel = null;
/*  326 */     StreamCompressor streamCompressor = null;
/*      */     try {
/*  328 */       channel = Files.newByteChannel(file, 
/*  329 */           EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.TRUNCATE_EXISTING), (FileAttribute<?>[])new FileAttribute[0]);
/*      */ 
/*      */ 
/*      */       
/*  333 */       streamCompressor = StreamCompressor.create(channel, this.def);
/*  334 */     } catch (IOException e) {
/*  335 */       IOUtils.closeQuietly(channel);
/*  336 */       channel = null;
/*  337 */       outputStream = Files.newOutputStream(file, options);
/*  338 */       streamCompressor = StreamCompressor.create(outputStream, this.def);
/*      */     } 
/*  340 */     this.outputStream = outputStream;
/*  341 */     this.channel = channel;
/*  342 */     this.streamCompressor = streamCompressor;
/*  343 */     this.isSplitZip = false;
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
/*      */   public ZipArchiveOutputStream(File file, long zipSplitSize) throws IOException {
/*  368 */     this(file.toPath(), zipSplitSize);
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
/*      */   public ZipArchiveOutputStream(Path path, long zipSplitSize) throws IOException {
/*  388 */     this.def = new Deflater(this.level, true);
/*  389 */     this.outputStream = new ZipSplitOutputStream(path, zipSplitSize);
/*  390 */     this.streamCompressor = StreamCompressor.create(this.outputStream, this.def);
/*  391 */     this.channel = null;
/*  392 */     this.isSplitZip = true;
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
/*      */   public ZipArchiveOutputStream(SeekableByteChannel channel) {
/*  407 */     this.channel = channel;
/*  408 */     this.def = new Deflater(this.level, true);
/*  409 */     this.streamCompressor = StreamCompressor.create(channel, this.def);
/*  410 */     this.outputStream = null;
/*  411 */     this.isSplitZip = false;
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
/*      */   public boolean isSeekable() {
/*  424 */     return (this.channel != null);
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
/*      */   public void setEncoding(String encoding) {
/*  437 */     this.encoding = encoding;
/*  438 */     this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
/*  439 */     if (this.useUTF8Flag && !ZipEncodingHelper.isUTF8(encoding)) {
/*  440 */       this.useUTF8Flag = false;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getEncoding() {
/*  450 */     return this.encoding;
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
/*      */   public void setUseLanguageEncodingFlag(boolean b) {
/*  463 */     this.useUTF8Flag = (b && ZipEncodingHelper.isUTF8(this.encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCreateUnicodeExtraFields(UnicodeExtraFieldPolicy b) {
/*  474 */     this.createUnicodeExtraFields = b;
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
/*      */   public void setFallbackToUTF8(boolean b) {
/*  488 */     this.fallbackToUTF8 = b;
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
/*      */   public void setUseZip64(Zip64Mode mode) {
/*  537 */     this.zip64Mode = mode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getBytesWritten() {
/*  547 */     return this.streamCompressor.getTotalBytesWritten();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void finish() throws IOException {
/*  558 */     if (this.finished) {
/*  559 */       throw new IOException("This archive has already been finished");
/*      */     }
/*      */     
/*  562 */     if (this.entry != null) {
/*  563 */       throw new IOException("This archive contains unclosed entries.");
/*      */     }
/*      */     
/*  566 */     long cdOverallOffset = this.streamCompressor.getTotalBytesWritten();
/*  567 */     this.cdOffset = cdOverallOffset;
/*  568 */     if (this.isSplitZip) {
/*      */ 
/*      */       
/*  571 */       ZipSplitOutputStream zipSplitOutputStream = (ZipSplitOutputStream)this.outputStream;
/*  572 */       this.cdOffset = zipSplitOutputStream.getCurrentSplitSegmentBytesWritten();
/*  573 */       this.cdDiskNumberStart = zipSplitOutputStream.getCurrentSplitSegmentIndex();
/*      */     } 
/*  575 */     writeCentralDirectoryInChunks();
/*      */     
/*  577 */     this.cdLength = this.streamCompressor.getTotalBytesWritten() - cdOverallOffset;
/*      */ 
/*      */     
/*  580 */     ByteBuffer commentData = this.zipEncoding.encode(this.comment);
/*  581 */     long commentLength = commentData.limit() - commentData.position();
/*  582 */     this.eocdLength = 22L + commentLength;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  592 */     writeZip64CentralDirectory();
/*  593 */     writeCentralDirectoryEnd();
/*  594 */     this.metaData.clear();
/*  595 */     this.entries.clear();
/*  596 */     this.streamCompressor.close();
/*  597 */     if (this.isSplitZip)
/*      */     {
/*  599 */       this.outputStream.close();
/*      */     }
/*  601 */     this.finished = true;
/*      */   }
/*      */   
/*      */   private void writeCentralDirectoryInChunks() throws IOException {
/*  605 */     int NUM_PER_WRITE = 1000;
/*  606 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(70000);
/*  607 */     int count = 0;
/*  608 */     for (ZipArchiveEntry ze : this.entries) {
/*  609 */       byteArrayOutputStream.write(createCentralFileHeader(ze));
/*  610 */       if (++count > 1000) {
/*  611 */         writeCounted(byteArrayOutputStream.toByteArray());
/*  612 */         byteArrayOutputStream.reset();
/*  613 */         count = 0;
/*      */       } 
/*      */     } 
/*  616 */     writeCounted(byteArrayOutputStream.toByteArray());
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
/*      */   public void closeArchiveEntry() throws IOException {
/*  628 */     preClose();
/*      */     
/*  630 */     flushDeflater();
/*      */     
/*  632 */     long bytesWritten = this.streamCompressor.getTotalBytesWritten() - this.entry.dataStart;
/*  633 */     long realCrc = this.streamCompressor.getCrc32();
/*  634 */     this.entry.bytesRead = this.streamCompressor.getBytesRead();
/*  635 */     Zip64Mode effectiveMode = getEffectiveZip64Mode(this.entry.entry);
/*  636 */     boolean actuallyNeedsZip64 = handleSizesAndCrc(bytesWritten, realCrc, effectiveMode);
/*  637 */     closeEntry(actuallyNeedsZip64, false);
/*  638 */     this.streamCompressor.reset();
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
/*      */   private void closeCopiedEntry(boolean phased) throws IOException {
/*  652 */     preClose();
/*  653 */     this.entry.bytesRead = this.entry.entry.getSize();
/*  654 */     Zip64Mode effectiveMode = getEffectiveZip64Mode(this.entry.entry);
/*  655 */     boolean actuallyNeedsZip64 = checkIfNeedsZip64(effectiveMode);
/*  656 */     closeEntry(actuallyNeedsZip64, phased);
/*      */   }
/*      */   
/*      */   private void closeEntry(boolean actuallyNeedsZip64, boolean phased) throws IOException {
/*  660 */     if (!phased && this.channel != null) {
/*  661 */       rewriteSizesAndCrc(actuallyNeedsZip64);
/*      */     }
/*      */     
/*  664 */     if (!phased) {
/*  665 */       writeDataDescriptor(this.entry.entry);
/*      */     }
/*  667 */     this.entry = null;
/*      */   }
/*      */   
/*      */   private void preClose() throws IOException {
/*  671 */     if (this.finished) {
/*  672 */       throw new IOException("Stream has already been finished");
/*      */     }
/*      */     
/*  675 */     if (this.entry == null) {
/*  676 */       throw new IOException("No current entry to close");
/*      */     }
/*      */     
/*  679 */     if (!this.entry.hasWritten) {
/*  680 */       write(ByteUtils.EMPTY_BYTE_ARRAY, 0, 0);
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
/*      */   public void addRawArchiveEntry(ZipArchiveEntry entry, InputStream rawStream) throws IOException {
/*  699 */     ZipArchiveEntry ae = new ZipArchiveEntry(entry);
/*  700 */     if (hasZip64Extra(ae))
/*      */     {
/*      */ 
/*      */       
/*  704 */       ae.removeExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
/*      */     }
/*      */ 
/*      */     
/*  708 */     boolean is2PhaseSource = (ae.getCrc() != -1L && ae.getSize() != -1L && ae.getCompressedSize() != -1L);
/*  709 */     putArchiveEntry(ae, is2PhaseSource);
/*  710 */     copyFromZipInputStream(rawStream);
/*  711 */     closeCopiedEntry(is2PhaseSource);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void flushDeflater() throws IOException {
/*  718 */     if (this.entry.entry.getMethod() == 8) {
/*  719 */       this.streamCompressor.flushDeflater();
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
/*      */   private boolean handleSizesAndCrc(long bytesWritten, long crc, Zip64Mode effectiveMode) throws ZipException {
/*  732 */     if (this.entry.entry.getMethod() == 8) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  737 */       this.entry.entry.setSize(this.entry.bytesRead);
/*  738 */       this.entry.entry.setCompressedSize(bytesWritten);
/*  739 */       this.entry.entry.setCrc(crc);
/*      */     }
/*  741 */     else if (this.channel == null) {
/*  742 */       if (this.entry.entry.getCrc() != crc) {
/*  743 */         throw new ZipException("Bad CRC checksum for entry " + this.entry
/*  744 */             .entry.getName() + ": " + 
/*  745 */             Long.toHexString(this.entry.entry.getCrc()) + " instead of " + 
/*      */             
/*  747 */             Long.toHexString(crc));
/*      */       }
/*      */       
/*  750 */       if (this.entry.entry.getSize() != bytesWritten) {
/*  751 */         throw new ZipException("Bad size for entry " + this.entry
/*  752 */             .entry.getName() + ": " + this.entry
/*  753 */             .entry.getSize() + " instead of " + bytesWritten);
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  758 */       this.entry.entry.setSize(bytesWritten);
/*  759 */       this.entry.entry.setCompressedSize(bytesWritten);
/*  760 */       this.entry.entry.setCrc(crc);
/*      */     } 
/*      */     
/*  763 */     return checkIfNeedsZip64(effectiveMode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkIfNeedsZip64(Zip64Mode effectiveMode) throws ZipException {
/*  773 */     boolean actuallyNeedsZip64 = isZip64Required(this.entry.entry, effectiveMode);
/*  774 */     if (actuallyNeedsZip64 && effectiveMode == Zip64Mode.Never) {
/*  775 */       throw new Zip64RequiredException(Zip64RequiredException.getEntryTooBigMessage(this.entry.entry));
/*      */     }
/*  777 */     return actuallyNeedsZip64;
/*      */   }
/*      */   
/*      */   private boolean isZip64Required(ZipArchiveEntry entry1, Zip64Mode requestedMode) {
/*  781 */     return (requestedMode == Zip64Mode.Always || requestedMode == Zip64Mode.AlwaysWithCompatibility || 
/*  782 */       isTooLargeForZip32(entry1));
/*      */   }
/*      */   
/*      */   private boolean isTooLargeForZip32(ZipArchiveEntry zipArchiveEntry) {
/*  786 */     return (zipArchiveEntry.getSize() >= 4294967295L || zipArchiveEntry.getCompressedSize() >= 4294967295L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void rewriteSizesAndCrc(boolean actuallyNeedsZip64) throws IOException {
/*  796 */     long save = this.channel.position();
/*      */     
/*  798 */     this.channel.position(this.entry.localDataStart);
/*  799 */     writeOut(ZipLong.getBytes(this.entry.entry.getCrc()));
/*  800 */     if (!hasZip64Extra(this.entry.entry) || !actuallyNeedsZip64) {
/*  801 */       writeOut(ZipLong.getBytes(this.entry.entry.getCompressedSize()));
/*  802 */       writeOut(ZipLong.getBytes(this.entry.entry.getSize()));
/*      */     } else {
/*  804 */       writeOut(ZipLong.ZIP64_MAGIC.getBytes());
/*  805 */       writeOut(ZipLong.ZIP64_MAGIC.getBytes());
/*      */     } 
/*      */     
/*  808 */     if (hasZip64Extra(this.entry.entry)) {
/*  809 */       ByteBuffer name = getName(this.entry.entry);
/*  810 */       int nameLen = name.limit() - name.position();
/*      */       
/*  812 */       this.channel.position(this.entry.localDataStart + 12L + 4L + nameLen + 4L);
/*      */ 
/*      */ 
/*      */       
/*  816 */       writeOut(ZipEightByteInteger.getBytes(this.entry.entry.getSize()));
/*  817 */       writeOut(ZipEightByteInteger.getBytes(this.entry.entry.getCompressedSize()));
/*      */       
/*  819 */       if (!actuallyNeedsZip64) {
/*      */ 
/*      */         
/*  822 */         this.channel.position(this.entry.localDataStart - 10L);
/*  823 */         writeOut(ZipShort.getBytes(versionNeededToExtract(this.entry.entry.getMethod(), false, false)));
/*      */ 
/*      */ 
/*      */         
/*  827 */         this.entry.entry.removeExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
/*      */         
/*  829 */         this.entry.entry.setExtra();
/*      */ 
/*      */ 
/*      */         
/*  833 */         if (this.entry.causedUseOfZip64) {
/*  834 */           this.hasUsedZip64 = false;
/*      */         }
/*      */       } 
/*      */     } 
/*  838 */     this.channel.position(save);
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
/*      */   public void putArchiveEntry(ArchiveEntry archiveEntry) throws IOException {
/*  850 */     putArchiveEntry(archiveEntry, false);
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
/*      */   private void putArchiveEntry(ArchiveEntry archiveEntry, boolean phased) throws IOException {
/*  866 */     if (this.finished) {
/*  867 */       throw new IOException("Stream has already been finished");
/*      */     }
/*      */     
/*  870 */     if (this.entry != null) {
/*  871 */       closeArchiveEntry();
/*      */     }
/*      */     
/*  874 */     this.entry = new CurrentEntry((ZipArchiveEntry)archiveEntry);
/*  875 */     this.entries.add(this.entry.entry);
/*      */     
/*  877 */     setDefaults(this.entry.entry);
/*      */     
/*  879 */     Zip64Mode effectiveMode = getEffectiveZip64Mode(this.entry.entry);
/*  880 */     validateSizeInformation(effectiveMode);
/*      */     
/*  882 */     if (shouldAddZip64Extra(this.entry.entry, effectiveMode)) {
/*      */       ZipEightByteInteger size, compressedSize;
/*  884 */       Zip64ExtendedInformationExtraField z64 = getZip64Extra(this.entry.entry);
/*      */ 
/*      */ 
/*      */       
/*  888 */       if (phased) {
/*      */         
/*  890 */         size = new ZipEightByteInteger(this.entry.entry.getSize());
/*  891 */         compressedSize = new ZipEightByteInteger(this.entry.entry.getCompressedSize());
/*  892 */       } else if (this.entry.entry.getMethod() == 0 && this.entry
/*  893 */         .entry.getSize() != -1L) {
/*      */         
/*  895 */         compressedSize = size = new ZipEightByteInteger(this.entry.entry.getSize());
/*      */       }
/*      */       else {
/*      */         
/*  899 */         compressedSize = size = ZipEightByteInteger.ZERO;
/*      */       } 
/*  901 */       z64.setSize(size);
/*  902 */       z64.setCompressedSize(compressedSize);
/*  903 */       this.entry.entry.setExtra();
/*      */     } 
/*      */     
/*  906 */     if (this.entry.entry.getMethod() == 8 && this.hasCompressionLevelChanged) {
/*  907 */       this.def.setLevel(this.level);
/*  908 */       this.hasCompressionLevelChanged = false;
/*      */     } 
/*  910 */     writeLocalFileHeader((ZipArchiveEntry)archiveEntry, phased);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setDefaults(ZipArchiveEntry entry) {
/*  918 */     if (entry.getMethod() == -1) {
/*  919 */       entry.setMethod(this.method);
/*      */     }
/*      */     
/*  922 */     if (entry.getTime() == -1L) {
/*  923 */       entry.setTime(System.currentTimeMillis());
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
/*      */   private void validateSizeInformation(Zip64Mode effectiveMode) throws ZipException {
/*  936 */     if (this.entry.entry.getMethod() == 0 && this.channel == null) {
/*  937 */       if (this.entry.entry.getSize() == -1L) {
/*  938 */         throw new ZipException("Uncompressed size is required for STORED method when not writing to a file");
/*      */       }
/*      */ 
/*      */       
/*  942 */       if (this.entry.entry.getCrc() == -1L) {
/*  943 */         throw new ZipException("CRC checksum is required for STORED method when not writing to a file");
/*      */       }
/*      */       
/*  946 */       this.entry.entry.setCompressedSize(this.entry.entry.getSize());
/*      */     } 
/*      */     
/*  949 */     if ((this.entry.entry.getSize() >= 4294967295L || this.entry
/*  950 */       .entry.getCompressedSize() >= 4294967295L) && effectiveMode == Zip64Mode.Never)
/*      */     {
/*  952 */       throw new Zip64RequiredException(
/*  953 */           Zip64RequiredException.getEntryTooBigMessage(this.entry.entry));
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
/*      */   private boolean shouldAddZip64Extra(ZipArchiveEntry entry, Zip64Mode mode) {
/*  972 */     return (mode == Zip64Mode.Always || mode == Zip64Mode.AlwaysWithCompatibility || entry
/*      */       
/*  974 */       .getSize() >= 4294967295L || entry
/*  975 */       .getCompressedSize() >= 4294967295L || (entry
/*  976 */       .getSize() == -1L && this.channel != null && mode != Zip64Mode.Never));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setComment(String comment) {
/*  985 */     this.comment = comment;
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
/*      */   public void setLevel(int level) {
/*  997 */     if (level < -1 || level > 9)
/*      */     {
/*  999 */       throw new IllegalArgumentException("Invalid compression level: " + level);
/*      */     }
/*      */     
/* 1002 */     if (this.level == level) {
/*      */       return;
/*      */     }
/* 1005 */     this.hasCompressionLevelChanged = true;
/* 1006 */     this.level = level;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMethod(int method) {
/* 1016 */     this.method = method;
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
/*      */   public boolean canWriteEntryData(ArchiveEntry ae) {
/* 1028 */     if (ae instanceof ZipArchiveEntry) {
/* 1029 */       ZipArchiveEntry zae = (ZipArchiveEntry)ae;
/* 1030 */       return (zae.getMethod() != ZipMethod.IMPLODING.getCode() && zae
/* 1031 */         .getMethod() != ZipMethod.UNSHRINKING.getCode() && 
/* 1032 */         ZipUtil.canHandleEntryData(zae));
/*      */     } 
/* 1034 */     return false;
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
/*      */   public void writePreamble(byte[] preamble) throws IOException {
/* 1046 */     writePreamble(preamble, 0, preamble.length);
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
/*      */   public void writePreamble(byte[] preamble, int offset, int length) throws IOException {
/* 1060 */     if (this.entry != null) {
/* 1061 */       throw new IllegalStateException("Preamble must be written before creating an entry");
/*      */     }
/* 1063 */     this.streamCompressor.writeCounted(preamble, offset, length);
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
/*      */   public void write(byte[] b, int offset, int length) throws IOException {
/* 1075 */     if (this.entry == null) {
/* 1076 */       throw new IllegalStateException("No current entry");
/*      */     }
/* 1078 */     ZipUtil.checkRequestedFeatures(this.entry.entry);
/* 1079 */     long writtenThisTime = this.streamCompressor.write(b, offset, length, this.entry.entry.getMethod());
/* 1080 */     count(writtenThisTime);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeCounted(byte[] data) throws IOException {
/* 1089 */     this.streamCompressor.writeCounted(data);
/*      */   }
/*      */   
/*      */   private void copyFromZipInputStream(InputStream src) throws IOException {
/* 1093 */     if (this.entry == null) {
/* 1094 */       throw new IllegalStateException("No current entry");
/*      */     }
/* 1096 */     ZipUtil.checkRequestedFeatures(this.entry.entry);
/* 1097 */     this.entry.hasWritten = true;
/*      */     int length;
/* 1099 */     while ((length = src.read(this.copyBuffer)) >= 0) {
/*      */       
/* 1101 */       this.streamCompressor.writeCounted(this.copyBuffer, 0, length);
/* 1102 */       count(length);
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
/*      */   public void close() throws IOException {
/*      */     try {
/* 1118 */       if (!this.finished) {
/* 1119 */         finish();
/*      */       }
/*      */     } finally {
/* 1122 */       destroy();
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
/*      */   public void flush() throws IOException {
/* 1134 */     if (this.outputStream != null) {
/* 1135 */       this.outputStream.flush();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1145 */   static final byte[] LFH_SIG = ZipLong.LFH_SIG.getBytes();
/*      */ 
/*      */ 
/*      */   
/* 1149 */   static final byte[] DD_SIG = ZipLong.DD_SIG.getBytes();
/*      */ 
/*      */ 
/*      */   
/* 1153 */   static final byte[] CFH_SIG = ZipLong.CFH_SIG.getBytes();
/*      */ 
/*      */ 
/*      */   
/* 1157 */   static final byte[] EOCD_SIG = ZipLong.getBytes(101010256L);
/*      */ 
/*      */ 
/*      */   
/* 1161 */   static final byte[] ZIP64_EOCD_SIG = ZipLong.getBytes(101075792L);
/*      */ 
/*      */ 
/*      */   
/* 1165 */   static final byte[] ZIP64_EOCD_LOC_SIG = ZipLong.getBytes(117853008L);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void deflate() throws IOException {
/* 1172 */     this.streamCompressor.deflate();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeLocalFileHeader(ZipArchiveEntry ze) throws IOException {
/* 1181 */     writeLocalFileHeader(ze, false);
/*      */   }
/*      */   
/*      */   private void writeLocalFileHeader(ZipArchiveEntry ze, boolean phased) throws IOException {
/* 1185 */     boolean encodable = this.zipEncoding.canEncode(ze.getName());
/* 1186 */     ByteBuffer name = getName(ze);
/*      */     
/* 1188 */     if (this.createUnicodeExtraFields != UnicodeExtraFieldPolicy.NEVER) {
/* 1189 */       addUnicodeExtraFields(ze, encodable, name);
/*      */     }
/*      */     
/* 1192 */     long localHeaderStart = this.streamCompressor.getTotalBytesWritten();
/* 1193 */     if (this.isSplitZip) {
/*      */ 
/*      */       
/* 1196 */       ZipSplitOutputStream splitOutputStream = (ZipSplitOutputStream)this.outputStream;
/* 1197 */       ze.setDiskNumberStart(splitOutputStream.getCurrentSplitSegmentIndex());
/* 1198 */       localHeaderStart = splitOutputStream.getCurrentSplitSegmentBytesWritten();
/*      */     } 
/*      */     
/* 1201 */     byte[] localHeader = createLocalFileHeader(ze, name, encodable, phased, localHeaderStart);
/* 1202 */     this.metaData.put(ze, new EntryMetaData(localHeaderStart, usesDataDescriptor(ze.getMethod(), phased)));
/* 1203 */     this.entry.localDataStart = localHeaderStart + 14L;
/* 1204 */     writeCounted(localHeader);
/* 1205 */     this.entry.dataStart = this.streamCompressor.getTotalBytesWritten();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private byte[] createLocalFileHeader(ZipArchiveEntry ze, ByteBuffer name, boolean encodable, boolean phased, long archiveOffset) {
/* 1211 */     ZipExtraField oldEx = ze.getExtraField(ResourceAlignmentExtraField.ID);
/* 1212 */     if (oldEx != null) {
/* 1213 */       ze.removeExtraField(ResourceAlignmentExtraField.ID);
/*      */     }
/* 1215 */     ResourceAlignmentExtraField oldAlignmentEx = (oldEx instanceof ResourceAlignmentExtraField) ? (ResourceAlignmentExtraField)oldEx : null;
/*      */ 
/*      */     
/* 1218 */     int alignment = ze.getAlignment();
/* 1219 */     if (alignment <= 0 && oldAlignmentEx != null) {
/* 1220 */       alignment = oldAlignmentEx.getAlignment();
/*      */     }
/*      */     
/* 1223 */     if (alignment > 1 || (oldAlignmentEx != null && !oldAlignmentEx.allowMethodChange())) {
/*      */ 
/*      */       
/* 1226 */       int oldLength = 30 + name.limit() - name.position() + (ze.getLocalFileDataExtra()).length;
/*      */       
/* 1228 */       int padding = (int)(-archiveOffset - oldLength - 4L - 2L & (alignment - 1));
/*      */ 
/*      */       
/* 1231 */       ze.addExtraField(new ResourceAlignmentExtraField(alignment, (oldAlignmentEx != null && oldAlignmentEx
/* 1232 */             .allowMethodChange()), padding));
/*      */     } 
/*      */     
/* 1235 */     byte[] extra = ze.getLocalFileDataExtra();
/* 1236 */     int nameLen = name.limit() - name.position();
/* 1237 */     int len = 30 + nameLen + extra.length;
/* 1238 */     byte[] buf = new byte[len];
/*      */     
/* 1240 */     System.arraycopy(LFH_SIG, 0, buf, 0, 4);
/*      */ 
/*      */     
/* 1243 */     int zipMethod = ze.getMethod();
/* 1244 */     boolean dataDescriptor = usesDataDescriptor(zipMethod, phased);
/*      */     
/* 1246 */     ZipShort.putShort(versionNeededToExtract(zipMethod, hasZip64Extra(ze), dataDescriptor), buf, 4);
/*      */     
/* 1248 */     GeneralPurposeBit generalPurposeBit = getGeneralPurposeBits((!encodable && this.fallbackToUTF8), dataDescriptor);
/* 1249 */     generalPurposeBit.encode(buf, 6);
/*      */ 
/*      */     
/* 1252 */     ZipShort.putShort(zipMethod, buf, 8);
/*      */     
/* 1254 */     ZipUtil.toDosTime(this.calendarInstance, ze.getTime(), buf, 10);
/*      */ 
/*      */     
/* 1257 */     if (phased || (zipMethod != 8 && this.channel == null)) {
/* 1258 */       ZipLong.putLong(ze.getCrc(), buf, 14);
/*      */     } else {
/* 1260 */       System.arraycopy(LZERO, 0, buf, 14, 4);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1265 */     if (hasZip64Extra(this.entry.entry)) {
/*      */ 
/*      */ 
/*      */       
/* 1269 */       ZipLong.ZIP64_MAGIC.putLong(buf, 18);
/* 1270 */       ZipLong.ZIP64_MAGIC.putLong(buf, 22);
/* 1271 */     } else if (phased) {
/* 1272 */       ZipLong.putLong(ze.getCompressedSize(), buf, 18);
/* 1273 */       ZipLong.putLong(ze.getSize(), buf, 22);
/* 1274 */     } else if (zipMethod == 8 || this.channel != null) {
/* 1275 */       System.arraycopy(LZERO, 0, buf, 18, 4);
/* 1276 */       System.arraycopy(LZERO, 0, buf, 22, 4);
/*      */     } else {
/* 1278 */       ZipLong.putLong(ze.getSize(), buf, 18);
/* 1279 */       ZipLong.putLong(ze.getSize(), buf, 22);
/*      */     } 
/*      */     
/* 1282 */     ZipShort.putShort(nameLen, buf, 26);
/*      */ 
/*      */     
/* 1285 */     ZipShort.putShort(extra.length, buf, 28);
/*      */ 
/*      */     
/* 1288 */     System.arraycopy(name.array(), name.arrayOffset(), buf, 30, nameLen);
/*      */ 
/*      */     
/* 1291 */     System.arraycopy(extra, 0, buf, 30 + nameLen, extra.length);
/*      */     
/* 1293 */     return buf;
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
/*      */   private void addUnicodeExtraFields(ZipArchiveEntry ze, boolean encodable, ByteBuffer name) throws IOException {
/* 1305 */     if (this.createUnicodeExtraFields == UnicodeExtraFieldPolicy.ALWAYS || !encodable)
/*      */     {
/* 1307 */       ze.addExtraField(new UnicodePathExtraField(ze.getName(), name
/* 1308 */             .array(), name
/* 1309 */             .arrayOffset(), name
/* 1310 */             .limit() - name
/* 1311 */             .position()));
/*      */     }
/*      */     
/* 1314 */     String comm = ze.getComment();
/* 1315 */     if (comm != null && !comm.isEmpty()) {
/*      */       
/* 1317 */       boolean commentEncodable = this.zipEncoding.canEncode(comm);
/*      */       
/* 1319 */       if (this.createUnicodeExtraFields == UnicodeExtraFieldPolicy.ALWAYS || !commentEncodable) {
/*      */         
/* 1321 */         ByteBuffer commentB = getEntryEncoding(ze).encode(comm);
/* 1322 */         ze.addExtraField(new UnicodeCommentExtraField(comm, commentB
/* 1323 */               .array(), commentB
/* 1324 */               .arrayOffset(), commentB
/* 1325 */               .limit() - commentB
/* 1326 */               .position()));
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
/*      */   protected void writeDataDescriptor(ZipArchiveEntry ze) throws IOException {
/* 1338 */     if (!usesDataDescriptor(ze.getMethod(), false)) {
/*      */       return;
/*      */     }
/* 1341 */     writeCounted(DD_SIG);
/* 1342 */     writeCounted(ZipLong.getBytes(ze.getCrc()));
/* 1343 */     if (!hasZip64Extra(ze)) {
/* 1344 */       writeCounted(ZipLong.getBytes(ze.getCompressedSize()));
/* 1345 */       writeCounted(ZipLong.getBytes(ze.getSize()));
/*      */     } else {
/* 1347 */       writeCounted(ZipEightByteInteger.getBytes(ze.getCompressedSize()));
/* 1348 */       writeCounted(ZipEightByteInteger.getBytes(ze.getSize()));
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
/*      */   protected void writeCentralFileHeader(ZipArchiveEntry ze) throws IOException {
/* 1361 */     byte[] centralFileHeader = createCentralFileHeader(ze);
/* 1362 */     writeCounted(centralFileHeader);
/*      */   }
/*      */ 
/*      */   
/*      */   private byte[] createCentralFileHeader(ZipArchiveEntry ze) throws IOException {
/* 1367 */     EntryMetaData entryMetaData = this.metaData.get(ze);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1372 */     boolean needsZip64Extra = (hasZip64Extra(ze) || ze.getCompressedSize() >= 4294967295L || ze.getSize() >= 4294967295L || entryMetaData.offset >= 4294967295L || ze.getDiskNumberStart() >= 65535L || this.zip64Mode == Zip64Mode.Always || this.zip64Mode == Zip64Mode.AlwaysWithCompatibility);
/*      */ 
/*      */ 
/*      */     
/* 1376 */     if (needsZip64Extra && this.zip64Mode == Zip64Mode.Never)
/*      */     {
/*      */ 
/*      */       
/* 1380 */       throw new Zip64RequiredException("Archive's size exceeds the limit of 4GByte.");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1385 */     handleZip64Extra(ze, entryMetaData.offset, needsZip64Extra);
/*      */     
/* 1387 */     return createCentralFileHeader(ze, getName(ze), entryMetaData, needsZip64Extra);
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
/*      */   private byte[] createCentralFileHeader(ZipArchiveEntry ze, ByteBuffer name, EntryMetaData entryMetaData, boolean needsZip64Extra) throws IOException {
/* 1400 */     if (this.isSplitZip) {
/*      */ 
/*      */       
/* 1403 */       int currentSplitSegment = ((ZipSplitOutputStream)this.outputStream).getCurrentSplitSegmentIndex();
/* 1404 */       if (this.numberOfCDInDiskData.get(Integer.valueOf(currentSplitSegment)) == null) {
/* 1405 */         this.numberOfCDInDiskData.put(Integer.valueOf(currentSplitSegment), Integer.valueOf(1));
/*      */       } else {
/* 1407 */         int originalNumberOfCD = ((Integer)this.numberOfCDInDiskData.get(Integer.valueOf(currentSplitSegment))).intValue();
/* 1408 */         this.numberOfCDInDiskData.put(Integer.valueOf(currentSplitSegment), Integer.valueOf(originalNumberOfCD + 1));
/*      */       } 
/*      */     } 
/*      */     
/* 1412 */     byte[] extra = ze.getCentralDirectoryExtra();
/* 1413 */     int extraLength = extra.length;
/*      */ 
/*      */     
/* 1416 */     String comm = ze.getComment();
/* 1417 */     if (comm == null) {
/* 1418 */       comm = "";
/*      */     }
/*      */     
/* 1421 */     ByteBuffer commentB = getEntryEncoding(ze).encode(comm);
/* 1422 */     int nameLen = name.limit() - name.position();
/* 1423 */     int commentLen = commentB.limit() - commentB.position();
/* 1424 */     int len = 46 + nameLen + extraLength + commentLen;
/* 1425 */     byte[] buf = new byte[len];
/*      */     
/* 1427 */     System.arraycopy(CFH_SIG, 0, buf, 0, 4);
/*      */ 
/*      */ 
/*      */     
/* 1431 */     ZipShort.putShort(ze.getPlatform() << 8 | (!this.hasUsedZip64 ? 20 : 45), buf, 4);
/*      */ 
/*      */     
/* 1434 */     int zipMethod = ze.getMethod();
/* 1435 */     boolean encodable = this.zipEncoding.canEncode(ze.getName());
/* 1436 */     ZipShort.putShort(versionNeededToExtract(zipMethod, needsZip64Extra, entryMetaData.usesDataDescriptor), buf, 6);
/*      */     
/* 1438 */     getGeneralPurposeBits((!encodable && this.fallbackToUTF8), entryMetaData.usesDataDescriptor).encode(buf, 8);
/*      */ 
/*      */     
/* 1441 */     ZipShort.putShort(zipMethod, buf, 10);
/*      */ 
/*      */ 
/*      */     
/* 1445 */     ZipUtil.toDosTime(this.calendarInstance, ze.getTime(), buf, 12);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1450 */     ZipLong.putLong(ze.getCrc(), buf, 16);
/* 1451 */     if (ze.getCompressedSize() >= 4294967295L || ze
/* 1452 */       .getSize() >= 4294967295L || this.zip64Mode == Zip64Mode.Always || this.zip64Mode == Zip64Mode.AlwaysWithCompatibility) {
/*      */ 
/*      */       
/* 1455 */       ZipLong.ZIP64_MAGIC.putLong(buf, 20);
/* 1456 */       ZipLong.ZIP64_MAGIC.putLong(buf, 24);
/*      */     } else {
/* 1458 */       ZipLong.putLong(ze.getCompressedSize(), buf, 20);
/* 1459 */       ZipLong.putLong(ze.getSize(), buf, 24);
/*      */     } 
/*      */     
/* 1462 */     ZipShort.putShort(nameLen, buf, 28);
/*      */ 
/*      */     
/* 1465 */     ZipShort.putShort(extraLength, buf, 30);
/*      */     
/* 1467 */     ZipShort.putShort(commentLen, buf, 32);
/*      */ 
/*      */     
/* 1470 */     if (this.isSplitZip) {
/* 1471 */       if (ze.getDiskNumberStart() >= 65535L || this.zip64Mode == Zip64Mode.Always) {
/* 1472 */         ZipShort.putShort(65535, buf, 34);
/*      */       } else {
/* 1474 */         ZipShort.putShort((int)ze.getDiskNumberStart(), buf, 34);
/*      */       } 
/*      */     } else {
/* 1477 */       System.arraycopy(ZERO, 0, buf, 34, 2);
/*      */     } 
/*      */ 
/*      */     
/* 1481 */     ZipShort.putShort(ze.getInternalAttributes(), buf, 36);
/*      */ 
/*      */     
/* 1484 */     ZipLong.putLong(ze.getExternalAttributes(), buf, 38);
/*      */ 
/*      */     
/* 1487 */     if (entryMetaData.offset >= 4294967295L || this.zip64Mode == Zip64Mode.Always) {
/* 1488 */       ZipLong.putLong(4294967295L, buf, 42);
/*      */     } else {
/* 1490 */       ZipLong.putLong(Math.min(entryMetaData.offset, 4294967295L), buf, 42);
/*      */     } 
/*      */ 
/*      */     
/* 1494 */     System.arraycopy(name.array(), name.arrayOffset(), buf, 46, nameLen);
/*      */     
/* 1496 */     int extraStart = 46 + nameLen;
/* 1497 */     System.arraycopy(extra, 0, buf, extraStart, extraLength);
/*      */     
/* 1499 */     int commentStart = extraStart + extraLength;
/*      */ 
/*      */     
/* 1502 */     System.arraycopy(commentB.array(), commentB.arrayOffset(), buf, commentStart, commentLen);
/* 1503 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void handleZip64Extra(ZipArchiveEntry ze, long lfhOffset, boolean needsZip64Extra) {
/* 1512 */     if (needsZip64Extra) {
/* 1513 */       Zip64ExtendedInformationExtraField z64 = getZip64Extra(ze);
/* 1514 */       if (ze.getCompressedSize() >= 4294967295L || ze
/* 1515 */         .getSize() >= 4294967295L || this.zip64Mode == Zip64Mode.Always || this.zip64Mode == Zip64Mode.AlwaysWithCompatibility) {
/*      */ 
/*      */         
/* 1518 */         z64.setCompressedSize(new ZipEightByteInteger(ze.getCompressedSize()));
/* 1519 */         z64.setSize(new ZipEightByteInteger(ze.getSize()));
/*      */       } else {
/*      */         
/* 1522 */         z64.setCompressedSize(null);
/* 1523 */         z64.setSize(null);
/*      */       } 
/*      */       
/* 1526 */       boolean needsToEncodeLfhOffset = (lfhOffset >= 4294967295L || this.zip64Mode == Zip64Mode.Always);
/*      */ 
/*      */       
/* 1529 */       boolean needsToEncodeDiskNumberStart = (ze.getDiskNumberStart() >= 65535L || this.zip64Mode == Zip64Mode.Always);
/*      */       
/* 1531 */       if (needsToEncodeLfhOffset || needsToEncodeDiskNumberStart) {
/* 1532 */         z64.setRelativeHeaderOffset(new ZipEightByteInteger(lfhOffset));
/*      */       }
/* 1534 */       if (needsToEncodeDiskNumberStart) {
/* 1535 */         z64.setDiskStartNumber(new ZipLong(ze.getDiskNumberStart()));
/*      */       }
/* 1537 */       ze.setExtra();
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
/*      */   protected void writeCentralDirectoryEnd() throws IOException {
/* 1549 */     if (!this.hasUsedZip64 && this.isSplitZip) {
/* 1550 */       ((ZipSplitOutputStream)this.outputStream).prepareToWriteUnsplittableContent(this.eocdLength);
/*      */     }
/*      */     
/* 1553 */     validateIfZip64IsNeededInEOCD();
/*      */     
/* 1555 */     writeCounted(EOCD_SIG);
/*      */ 
/*      */     
/* 1558 */     int numberOfThisDisk = 0;
/* 1559 */     if (this.isSplitZip) {
/* 1560 */       numberOfThisDisk = ((ZipSplitOutputStream)this.outputStream).getCurrentSplitSegmentIndex();
/*      */     }
/* 1562 */     writeCounted(ZipShort.getBytes(numberOfThisDisk));
/*      */ 
/*      */     
/* 1565 */     writeCounted(ZipShort.getBytes((int)this.cdDiskNumberStart));
/*      */ 
/*      */     
/* 1568 */     int numberOfEntries = this.entries.size();
/*      */ 
/*      */ 
/*      */     
/* 1572 */     int numOfEntriesOnThisDisk = this.isSplitZip ? ((this.numberOfCDInDiskData.get(Integer.valueOf(numberOfThisDisk)) == null) ? 0 : ((Integer)this.numberOfCDInDiskData.get(Integer.valueOf(numberOfThisDisk))).intValue()) : numberOfEntries;
/*      */ 
/*      */     
/* 1575 */     byte[] numOfEntriesOnThisDiskData = ZipShort.getBytes(Math.min(numOfEntriesOnThisDisk, 65535));
/* 1576 */     writeCounted(numOfEntriesOnThisDiskData);
/*      */ 
/*      */     
/* 1579 */     byte[] num = ZipShort.getBytes(Math.min(numberOfEntries, 65535));
/*      */     
/* 1581 */     writeCounted(num);
/*      */ 
/*      */     
/* 1584 */     writeCounted(ZipLong.getBytes(Math.min(this.cdLength, 4294967295L)));
/* 1585 */     writeCounted(ZipLong.getBytes(Math.min(this.cdOffset, 4294967295L)));
/*      */ 
/*      */     
/* 1588 */     ByteBuffer data = this.zipEncoding.encode(this.comment);
/* 1589 */     int dataLen = data.limit() - data.position();
/* 1590 */     writeCounted(ZipShort.getBytes(dataLen));
/* 1591 */     this.streamCompressor.writeCounted(data.array(), data.arrayOffset(), dataLen);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void validateIfZip64IsNeededInEOCD() throws Zip64RequiredException {
/* 1601 */     if (this.zip64Mode != Zip64Mode.Never) {
/*      */       return;
/*      */     }
/*      */     
/* 1605 */     int numberOfThisDisk = 0;
/* 1606 */     if (this.isSplitZip) {
/* 1607 */       numberOfThisDisk = ((ZipSplitOutputStream)this.outputStream).getCurrentSplitSegmentIndex();
/*      */     }
/* 1609 */     if (numberOfThisDisk >= 65535) {
/* 1610 */       throw new Zip64RequiredException("Number of the disk of End Of Central Directory exceeds the limit of 65535.");
/*      */     }
/*      */ 
/*      */     
/* 1614 */     if (this.cdDiskNumberStart >= 65535L) {
/* 1615 */       throw new Zip64RequiredException("Number of the disk with the start of Central Directory exceeds the limit of 65535.");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1620 */     int numOfEntriesOnThisDisk = (this.numberOfCDInDiskData.get(Integer.valueOf(numberOfThisDisk)) == null) ? 0 : ((Integer)this.numberOfCDInDiskData.get(Integer.valueOf(numberOfThisDisk))).intValue();
/* 1621 */     if (numOfEntriesOnThisDisk >= 65535) {
/* 1622 */       throw new Zip64RequiredException("Number of entries on this disk exceeds the limit of 65535.");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1627 */     if (this.entries.size() >= 65535) {
/* 1628 */       throw new Zip64RequiredException("Archive contains more than 65535 entries.");
/*      */     }
/*      */ 
/*      */     
/* 1632 */     if (this.cdLength >= 4294967295L) {
/* 1633 */       throw new Zip64RequiredException("The size of the entire central directory exceeds the limit of 4GByte.");
/*      */     }
/*      */ 
/*      */     
/* 1637 */     if (this.cdOffset >= 4294967295L) {
/* 1638 */       throw new Zip64RequiredException("Archive's size exceeds the limit of 4GByte.");
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
/*      */   protected void writeZip64CentralDirectory() throws IOException {
/* 1650 */     if (this.zip64Mode == Zip64Mode.Never) {
/*      */       return;
/*      */     }
/*      */     
/* 1654 */     if (!this.hasUsedZip64 && shouldUseZip64EOCD())
/*      */     {
/* 1656 */       this.hasUsedZip64 = true;
/*      */     }
/*      */     
/* 1659 */     if (!this.hasUsedZip64) {
/*      */       return;
/*      */     }
/*      */     
/* 1663 */     long offset = this.streamCompressor.getTotalBytesWritten();
/* 1664 */     long diskNumberStart = 0L;
/* 1665 */     if (this.isSplitZip) {
/*      */ 
/*      */       
/* 1668 */       ZipSplitOutputStream zipSplitOutputStream = (ZipSplitOutputStream)this.outputStream;
/* 1669 */       offset = zipSplitOutputStream.getCurrentSplitSegmentBytesWritten();
/* 1670 */       diskNumberStart = zipSplitOutputStream.getCurrentSplitSegmentIndex();
/*      */     } 
/*      */ 
/*      */     
/* 1674 */     writeOut(ZIP64_EOCD_SIG);
/*      */ 
/*      */     
/* 1677 */     writeOut(
/* 1678 */         ZipEightByteInteger.getBytes(44L));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1689 */     writeOut(ZipShort.getBytes(45));
/* 1690 */     writeOut(ZipShort.getBytes(45));
/*      */ 
/*      */     
/* 1693 */     int numberOfThisDisk = 0;
/* 1694 */     if (this.isSplitZip) {
/* 1695 */       numberOfThisDisk = ((ZipSplitOutputStream)this.outputStream).getCurrentSplitSegmentIndex();
/*      */     }
/* 1697 */     writeOut(ZipLong.getBytes(numberOfThisDisk));
/*      */ 
/*      */     
/* 1700 */     writeOut(ZipLong.getBytes(this.cdDiskNumberStart));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1705 */     int numOfEntriesOnThisDisk = this.isSplitZip ? ((this.numberOfCDInDiskData.get(Integer.valueOf(numberOfThisDisk)) == null) ? 0 : ((Integer)this.numberOfCDInDiskData.get(Integer.valueOf(numberOfThisDisk))).intValue()) : this.entries.size();
/* 1706 */     byte[] numOfEntriesOnThisDiskData = ZipEightByteInteger.getBytes(numOfEntriesOnThisDisk);
/* 1707 */     writeOut(numOfEntriesOnThisDiskData);
/*      */ 
/*      */     
/* 1710 */     byte[] num = ZipEightByteInteger.getBytes(this.entries.size());
/* 1711 */     writeOut(num);
/*      */ 
/*      */     
/* 1714 */     writeOut(ZipEightByteInteger.getBytes(this.cdLength));
/* 1715 */     writeOut(ZipEightByteInteger.getBytes(this.cdOffset));
/*      */ 
/*      */ 
/*      */     
/* 1719 */     if (this.isSplitZip) {
/*      */ 
/*      */       
/* 1722 */       int zip64EOCDLOCLength = 20;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1727 */       long unsplittableContentSize = 20L + this.eocdLength;
/* 1728 */       ((ZipSplitOutputStream)this.outputStream).prepareToWriteUnsplittableContent(unsplittableContentSize);
/*      */     } 
/*      */ 
/*      */     
/* 1732 */     writeOut(ZIP64_EOCD_LOC_SIG);
/*      */ 
/*      */     
/* 1735 */     writeOut(ZipLong.getBytes(diskNumberStart));
/*      */     
/* 1737 */     writeOut(ZipEightByteInteger.getBytes(offset));
/*      */     
/* 1739 */     if (this.isSplitZip) {
/*      */ 
/*      */       
/* 1742 */       int totalNumberOfDisks = ((ZipSplitOutputStream)this.outputStream).getCurrentSplitSegmentIndex() + 1;
/* 1743 */       writeOut(ZipLong.getBytes(totalNumberOfDisks));
/*      */     } else {
/* 1745 */       writeOut(ONE);
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
/*      */   private boolean shouldUseZip64EOCD() {
/* 1757 */     int numberOfThisDisk = 0;
/* 1758 */     if (this.isSplitZip) {
/* 1759 */       numberOfThisDisk = ((ZipSplitOutputStream)this.outputStream).getCurrentSplitSegmentIndex();
/*      */     }
/* 1761 */     int numOfEntriesOnThisDisk = (this.numberOfCDInDiskData.get(Integer.valueOf(numberOfThisDisk)) == null) ? 0 : ((Integer)this.numberOfCDInDiskData.get(Integer.valueOf(numberOfThisDisk))).intValue();
/* 1762 */     return (numberOfThisDisk >= 65535 || this.cdDiskNumberStart >= 65535L || numOfEntriesOnThisDisk >= 65535 || this.entries
/*      */ 
/*      */       
/* 1765 */       .size() >= 65535 || this.cdLength >= 4294967295L || this.cdOffset >= 4294967295L);
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
/*      */   protected final void writeOut(byte[] data) throws IOException {
/* 1777 */     this.streamCompressor.writeOut(data, 0, data.length);
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
/*      */   protected final void writeOut(byte[] data, int offset, int length) throws IOException {
/* 1790 */     this.streamCompressor.writeOut(data, offset, length);
/*      */   }
/*      */ 
/*      */   
/*      */   private GeneralPurposeBit getGeneralPurposeBits(boolean utfFallback, boolean usesDataDescriptor) {
/* 1795 */     GeneralPurposeBit b = new GeneralPurposeBit();
/* 1796 */     b.useUTF8ForNames((this.useUTF8Flag || utfFallback));
/* 1797 */     if (usesDataDescriptor) {
/* 1798 */       b.useDataDescriptor(true);
/*      */     }
/* 1800 */     return b;
/*      */   }
/*      */   
/*      */   private int versionNeededToExtract(int zipMethod, boolean zip64, boolean usedDataDescriptor) {
/* 1804 */     if (zip64) {
/* 1805 */       return 45;
/*      */     }
/* 1807 */     if (usedDataDescriptor) {
/* 1808 */       return 20;
/*      */     }
/* 1810 */     return versionNeededToExtractMethod(zipMethod);
/*      */   }
/*      */   
/*      */   private boolean usesDataDescriptor(int zipMethod, boolean phased) {
/* 1814 */     return (!phased && zipMethod == 8 && this.channel == null);
/*      */   }
/*      */   
/*      */   private int versionNeededToExtractMethod(int zipMethod) {
/* 1818 */     return (zipMethod == 8) ? 20 : 10;
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
/*      */   public ArchiveEntry createArchiveEntry(File inputFile, String entryName) throws IOException {
/* 1835 */     if (this.finished) {
/* 1836 */       throw new IOException("Stream has already been finished");
/*      */     }
/* 1838 */     return new ZipArchiveEntry(inputFile, entryName);
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
/*      */   public ArchiveEntry createArchiveEntry(Path inputPath, String entryName, LinkOption... options) throws IOException {
/* 1861 */     if (this.finished) {
/* 1862 */       throw new IOException("Stream has already been finished");
/*      */     }
/* 1864 */     return new ZipArchiveEntry(inputPath, entryName, new LinkOption[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Zip64ExtendedInformationExtraField getZip64Extra(ZipArchiveEntry ze) {
/* 1875 */     if (this.entry != null) {
/* 1876 */       this.entry.causedUseOfZip64 = !this.hasUsedZip64;
/*      */     }
/* 1878 */     this.hasUsedZip64 = true;
/* 1879 */     ZipExtraField extra = ze.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
/* 1880 */     Zip64ExtendedInformationExtraField z64 = (extra instanceof Zip64ExtendedInformationExtraField) ? (Zip64ExtendedInformationExtraField)extra : null;
/*      */     
/* 1882 */     if (z64 == null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1889 */       z64 = new Zip64ExtendedInformationExtraField();
/*      */     }
/*      */ 
/*      */     
/* 1893 */     ze.addAsFirstExtraField(z64);
/*      */     
/* 1895 */     return z64;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasZip64Extra(ZipArchiveEntry ze) {
/* 1905 */     return ze.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID) instanceof Zip64ExtendedInformationExtraField;
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
/*      */   private Zip64Mode getEffectiveZip64Mode(ZipArchiveEntry ze) {
/* 1918 */     if (this.zip64Mode != Zip64Mode.AsNeeded || this.channel != null || ze
/*      */       
/* 1920 */       .getMethod() != 8 || ze
/* 1921 */       .getSize() != -1L) {
/* 1922 */       return this.zip64Mode;
/*      */     }
/* 1924 */     return Zip64Mode.Never;
/*      */   }
/*      */   
/*      */   private ZipEncoding getEntryEncoding(ZipArchiveEntry ze) {
/* 1928 */     boolean encodable = this.zipEncoding.canEncode(ze.getName());
/* 1929 */     return (!encodable && this.fallbackToUTF8) ? ZipEncodingHelper.UTF8_ZIP_ENCODING : this.zipEncoding;
/*      */   }
/*      */ 
/*      */   
/*      */   private ByteBuffer getName(ZipArchiveEntry ze) throws IOException {
/* 1934 */     return getEntryEncoding(ze).encode(ze.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void destroy() throws IOException {
/*      */     try {
/* 1946 */       if (this.channel != null) {
/* 1947 */         this.channel.close();
/*      */       }
/*      */     } finally {
/* 1950 */       if (this.outputStream != null) {
/* 1951 */         this.outputStream.close();
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
/*      */   public static final class UnicodeExtraFieldPolicy
/*      */   {
/* 1964 */     public static final UnicodeExtraFieldPolicy ALWAYS = new UnicodeExtraFieldPolicy("always");
/*      */ 
/*      */ 
/*      */     
/* 1968 */     public static final UnicodeExtraFieldPolicy NEVER = new UnicodeExtraFieldPolicy("never");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1973 */     public static final UnicodeExtraFieldPolicy NOT_ENCODEABLE = new UnicodeExtraFieldPolicy("not encodeable");
/*      */     
/*      */     private final String name;
/*      */     
/*      */     private UnicodeExtraFieldPolicy(String n) {
/* 1978 */       this.name = n;
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1982 */       return this.name;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class CurrentEntry {
/*      */     private final ZipArchiveEntry entry;
/*      */     private long localDataStart;
/*      */     private long dataStart;
/*      */     
/*      */     private CurrentEntry(ZipArchiveEntry entry) {
/* 1992 */       this.entry = entry;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private long bytesRead;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean causedUseOfZip64;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean hasWritten;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class EntryMetaData
/*      */   {
/*      */     private final long offset;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final boolean usesDataDescriptor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private EntryMetaData(long offset, boolean usesDataDescriptor) {
/* 2031 */       this.offset = offset;
/* 2032 */       this.usesDataDescriptor = usesDataDescriptor;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\zip\ZipArchiveOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */