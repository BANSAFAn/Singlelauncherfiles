/*      */ package org.apache.commons.compress.archivers.zip;
/*      */ 
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PushbackInputStream;
/*      */ import java.math.BigInteger;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.util.Arrays;
/*      */ import java.util.Objects;
/*      */ import java.util.zip.CRC32;
/*      */ import java.util.zip.DataFormatException;
/*      */ import java.util.zip.Inflater;
/*      */ import java.util.zip.ZipException;
/*      */ import org.apache.commons.compress.archivers.ArchiveEntry;
/*      */ import org.apache.commons.compress.archivers.ArchiveInputStream;
/*      */ import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
/*      */ import org.apache.commons.compress.compressors.deflate64.Deflate64CompressorInputStream;
/*      */ import org.apache.commons.compress.utils.ArchiveUtils;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ZipArchiveInputStream
/*      */   extends ArchiveInputStream
/*      */   implements InputStreamStatistics
/*      */ {
/*      */   private final ZipEncoding zipEncoding;
/*      */   final String encoding;
/*      */   private final boolean useUnicodeExtraFields;
/*      */   private final InputStream inputStream;
/*   97 */   private final Inflater inf = new Inflater(true);
/*      */ 
/*      */   
/*  100 */   private final ByteBuffer buf = ByteBuffer.allocate(512);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CurrentEntry current;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean closed;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hitCentralDirectory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ByteArrayInputStream lastStoredEntry;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean allowStoredEntriesWithDataDescriptor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long uncompressedCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean skipSplitSig;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int LFH_LEN = 30;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int CFH_LEN = 46;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final long TWO_EXP_32 = 4294967296L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  176 */   private final byte[] lfhBuf = new byte[30];
/*  177 */   private final byte[] skipBuf = new byte[1024];
/*  178 */   private final byte[] shortBuf = new byte[2];
/*  179 */   private final byte[] wordBuf = new byte[4];
/*  180 */   private final byte[] twoDwordBuf = new byte[16];
/*      */ 
/*      */   
/*      */   private int entriesRead;
/*      */   
/*      */   private static final String USE_ZIPFILE_INSTEAD_OF_STREAM_DISCLAIMER = " while reading a stored entry using data descriptor. Either the archive is broken or it can not be read using ZipArchiveInputStream and you must use ZipFile. A common cause for this is a ZIP archive containing a ZIP archive. See http://commons.apache.org/proper/commons-compress/zip.html#ZipArchiveInputStream_vs_ZipFile";
/*      */ 
/*      */   
/*      */   public ZipArchiveInputStream(InputStream inputStream) {
/*  189 */     this(inputStream, "UTF8");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipArchiveInputStream(InputStream inputStream, String encoding) {
/*  200 */     this(inputStream, encoding, true);
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
/*      */   public ZipArchiveInputStream(InputStream inputStream, String encoding, boolean useUnicodeExtraFields) {
/*  212 */     this(inputStream, encoding, useUnicodeExtraFields, false);
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
/*      */   public ZipArchiveInputStream(InputStream inputStream, String encoding, boolean useUnicodeExtraFields, boolean allowStoredEntriesWithDataDescriptor) {
/*  230 */     this(inputStream, encoding, useUnicodeExtraFields, allowStoredEntriesWithDataDescriptor, false);
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
/*      */   public ZipArchiveInputStream(InputStream inputStream, String encoding, boolean useUnicodeExtraFields, boolean allowStoredEntriesWithDataDescriptor, boolean skipSplitSig) {
/*  252 */     this.encoding = encoding;
/*  253 */     this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
/*  254 */     this.useUnicodeExtraFields = useUnicodeExtraFields;
/*  255 */     this.inputStream = new PushbackInputStream(inputStream, this.buf.capacity());
/*  256 */     this.allowStoredEntriesWithDataDescriptor = allowStoredEntriesWithDataDescriptor;
/*  257 */     this.skipSplitSig = skipSplitSig;
/*      */     
/*  259 */     this.buf.limit(0);
/*      */   }
/*      */   
/*      */   public ZipArchiveEntry getNextZipEntry() throws IOException {
/*  263 */     this.uncompressedCount = 0L;
/*      */     
/*  265 */     boolean firstEntry = true;
/*  266 */     if (this.closed || this.hitCentralDirectory) {
/*  267 */       return null;
/*      */     }
/*  269 */     if (this.current != null) {
/*  270 */       closeEntry();
/*  271 */       firstEntry = false;
/*      */     } 
/*      */     
/*  274 */     long currentHeaderOffset = getBytesRead();
/*      */     try {
/*  276 */       if (firstEntry) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  281 */         readFirstLocalFileHeader();
/*      */       } else {
/*  283 */         readFully(this.lfhBuf);
/*      */       } 
/*  285 */     } catch (EOFException e) {
/*  286 */       return null;
/*      */     } 
/*      */     
/*  289 */     ZipLong sig = new ZipLong(this.lfhBuf);
/*  290 */     if (!sig.equals(ZipLong.LFH_SIG)) {
/*  291 */       if (sig.equals(ZipLong.CFH_SIG) || sig.equals(ZipLong.AED_SIG) || isApkSigningBlock(this.lfhBuf)) {
/*  292 */         this.hitCentralDirectory = true;
/*  293 */         skipRemainderOfArchive();
/*  294 */         return null;
/*      */       } 
/*  296 */       throw new ZipException(String.format("Unexpected record signature: 0x%x", new Object[] { Long.valueOf(sig.getValue()) }));
/*      */     } 
/*      */     
/*  299 */     int off = 4;
/*  300 */     this.current = new CurrentEntry();
/*      */     
/*  302 */     int versionMadeBy = ZipShort.getValue(this.lfhBuf, off);
/*  303 */     off += 2;
/*  304 */     this.current.entry.setPlatform(versionMadeBy >> 8 & 0xF);
/*      */     
/*  306 */     GeneralPurposeBit gpFlag = GeneralPurposeBit.parse(this.lfhBuf, off);
/*  307 */     boolean hasUTF8Flag = gpFlag.usesUTF8ForNames();
/*  308 */     ZipEncoding entryEncoding = hasUTF8Flag ? ZipEncodingHelper.UTF8_ZIP_ENCODING : this.zipEncoding;
/*  309 */     this.current.hasDataDescriptor = gpFlag.usesDataDescriptor();
/*  310 */     this.current.entry.setGeneralPurposeBit(gpFlag);
/*      */     
/*  312 */     off += 2;
/*      */     
/*  314 */     this.current.entry.setMethod(ZipShort.getValue(this.lfhBuf, off));
/*  315 */     off += 2;
/*      */     
/*  317 */     long time = ZipUtil.dosToJavaTime(ZipLong.getValue(this.lfhBuf, off));
/*  318 */     this.current.entry.setTime(time);
/*  319 */     off += 4;
/*      */     
/*  321 */     ZipLong size = null, cSize = null;
/*  322 */     if (!this.current.hasDataDescriptor) {
/*  323 */       this.current.entry.setCrc(ZipLong.getValue(this.lfhBuf, off));
/*  324 */       off += 4;
/*      */       
/*  326 */       cSize = new ZipLong(this.lfhBuf, off);
/*  327 */       off += 4;
/*      */       
/*  329 */       size = new ZipLong(this.lfhBuf, off);
/*  330 */       off += 4;
/*      */     } else {
/*  332 */       off += 12;
/*      */     } 
/*      */     
/*  335 */     int fileNameLen = ZipShort.getValue(this.lfhBuf, off);
/*      */     
/*  337 */     off += 2;
/*      */     
/*  339 */     int extraLen = ZipShort.getValue(this.lfhBuf, off);
/*  340 */     off += 2;
/*      */     
/*  342 */     byte[] fileName = readRange(fileNameLen);
/*  343 */     this.current.entry.setName(entryEncoding.decode(fileName), fileName);
/*  344 */     if (hasUTF8Flag) {
/*  345 */       this.current.entry.setNameSource(ZipArchiveEntry.NameSource.NAME_WITH_EFS_FLAG);
/*      */     }
/*      */     
/*  348 */     byte[] extraData = readRange(extraLen);
/*      */     try {
/*  350 */       this.current.entry.setExtra(extraData);
/*  351 */     } catch (RuntimeException ex) {
/*  352 */       ZipException z = new ZipException("Invalid extra data in entry " + this.current.entry.getName());
/*  353 */       z.initCause(ex);
/*  354 */       throw z;
/*      */     } 
/*      */     
/*  357 */     if (!hasUTF8Flag && this.useUnicodeExtraFields) {
/*  358 */       ZipUtil.setNameAndCommentFromExtraFields(this.current.entry, fileName, null);
/*      */     }
/*      */     
/*  361 */     processZip64Extra(size, cSize);
/*      */     
/*  363 */     this.current.entry.setLocalHeaderOffset(currentHeaderOffset);
/*  364 */     this.current.entry.setDataOffset(getBytesRead());
/*  365 */     this.current.entry.setStreamContiguous(true);
/*      */     
/*  367 */     ZipMethod m = ZipMethod.getMethodByCode(this.current.entry.getMethod());
/*  368 */     if (this.current.entry.getCompressedSize() != -1L) {
/*  369 */       if (ZipUtil.canHandleEntryData(this.current.entry) && m != ZipMethod.STORED && m != ZipMethod.DEFLATED) {
/*  370 */         InputStream bis = new BoundedInputStream(this.inputStream, this.current.entry.getCompressedSize());
/*  371 */         switch (m) {
/*      */           case UNSHRINKING:
/*  373 */             this.current.inputStream = (InputStream)new UnshrinkingInputStream(bis);
/*      */             break;
/*      */           case IMPLODING:
/*      */             try {
/*  377 */               this.current.inputStream = new ExplodingInputStream(this.current
/*  378 */                   .entry.getGeneralPurposeBit().getSlidingDictionarySize(), this.current
/*  379 */                   .entry.getGeneralPurposeBit().getNumberOfShannonFanoTrees(), bis);
/*      */             }
/*  381 */             catch (IllegalArgumentException ex) {
/*  382 */               throw new IOException("bad IMPLODE data", ex);
/*      */             } 
/*      */             break;
/*      */           case BZIP2:
/*  386 */             this.current.inputStream = (InputStream)new BZip2CompressorInputStream(bis);
/*      */             break;
/*      */           case ENHANCED_DEFLATED:
/*  389 */             this.current.inputStream = (InputStream)new Deflate64CompressorInputStream(bis);
/*      */             break;
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       } 
/*  398 */     } else if (m == ZipMethod.ENHANCED_DEFLATED) {
/*  399 */       this.current.inputStream = (InputStream)new Deflate64CompressorInputStream(this.inputStream);
/*      */     } 
/*      */     
/*  402 */     this.entriesRead++;
/*  403 */     return this.current.entry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void readFirstLocalFileHeader() throws IOException {
/*  412 */     readFully(this.lfhBuf);
/*  413 */     ZipLong sig = new ZipLong(this.lfhBuf);
/*      */     
/*  415 */     if (!this.skipSplitSig && sig.equals(ZipLong.DD_SIG)) {
/*  416 */       throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.SPLITTING);
/*      */     }
/*      */ 
/*      */     
/*  420 */     if (sig.equals(ZipLong.SINGLE_SEGMENT_SPLIT_MARKER) || sig.equals(ZipLong.DD_SIG)) {
/*      */       
/*  422 */       byte[] missedLfhBytes = new byte[4];
/*  423 */       readFully(missedLfhBytes);
/*  424 */       System.arraycopy(this.lfhBuf, 4, this.lfhBuf, 0, 26);
/*  425 */       System.arraycopy(missedLfhBytes, 0, this.lfhBuf, 26, 4);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processZip64Extra(ZipLong size, ZipLong cSize) throws ZipException {
/*  436 */     ZipExtraField extra = this.current.entry.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
/*  437 */     if (extra != null && !(extra instanceof Zip64ExtendedInformationExtraField)) {
/*  438 */       throw new ZipException("archive contains unparseable zip64 extra field");
/*      */     }
/*  440 */     Zip64ExtendedInformationExtraField z64 = (Zip64ExtendedInformationExtraField)extra;
/*      */     
/*  442 */     this.current.usesZip64 = (z64 != null);
/*  443 */     if (!this.current.hasDataDescriptor) {
/*  444 */       if (z64 != null && (ZipLong.ZIP64_MAGIC
/*  445 */         .equals(cSize) || ZipLong.ZIP64_MAGIC.equals(size))) {
/*  446 */         if (z64.getCompressedSize() == null || z64.getSize() == null)
/*      */         {
/*  448 */           throw new ZipException("archive contains corrupted zip64 extra field");
/*      */         }
/*  450 */         long s = z64.getCompressedSize().getLongValue();
/*  451 */         if (s < 0L) {
/*  452 */           throw new ZipException("broken archive, entry with negative compressed size");
/*      */         }
/*  454 */         this.current.entry.setCompressedSize(s);
/*  455 */         s = z64.getSize().getLongValue();
/*  456 */         if (s < 0L) {
/*  457 */           throw new ZipException("broken archive, entry with negative size");
/*      */         }
/*  459 */         this.current.entry.setSize(s);
/*  460 */       } else if (cSize != null && size != null) {
/*  461 */         if (cSize.getValue() < 0L) {
/*  462 */           throw new ZipException("broken archive, entry with negative compressed size");
/*      */         }
/*  464 */         this.current.entry.setCompressedSize(cSize.getValue());
/*  465 */         if (size.getValue() < 0L) {
/*  466 */           throw new ZipException("broken archive, entry with negative size");
/*      */         }
/*  468 */         this.current.entry.setSize(size.getValue());
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public ArchiveEntry getNextEntry() throws IOException {
/*  475 */     return getNextZipEntry();
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
/*      */   public boolean canReadEntryData(ArchiveEntry ae) {
/*  487 */     if (ae instanceof ZipArchiveEntry) {
/*  488 */       ZipArchiveEntry ze = (ZipArchiveEntry)ae;
/*  489 */       return (ZipUtil.canHandleEntryData(ze) && 
/*  490 */         supportsDataDescriptorFor(ze) && 
/*  491 */         supportsCompressedSizeFor(ze));
/*      */     } 
/*  493 */     return false;
/*      */   }
/*      */   
/*      */   public int read(byte[] buffer, int offset, int length) throws IOException {
/*      */     int read;
/*  498 */     if (length == 0) {
/*  499 */       return 0;
/*      */     }
/*  501 */     if (this.closed) {
/*  502 */       throw new IOException("The stream is closed");
/*      */     }
/*      */     
/*  505 */     if (this.current == null) {
/*  506 */       return -1;
/*      */     }
/*      */ 
/*      */     
/*  510 */     if (offset > buffer.length || length < 0 || offset < 0 || buffer.length - offset < length) {
/*  511 */       throw new ArrayIndexOutOfBoundsException();
/*      */     }
/*      */     
/*  514 */     ZipUtil.checkRequestedFeatures(this.current.entry);
/*  515 */     if (!supportsDataDescriptorFor(this.current.entry)) {
/*  516 */       throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.DATA_DESCRIPTOR, this.current
/*  517 */           .entry);
/*      */     }
/*  519 */     if (!supportsCompressedSizeFor(this.current.entry)) {
/*  520 */       throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.UNKNOWN_COMPRESSED_SIZE, this.current
/*  521 */           .entry);
/*      */     }
/*      */ 
/*      */     
/*  525 */     if (this.current.entry.getMethod() == 0) {
/*  526 */       read = readStored(buffer, offset, length);
/*  527 */     } else if (this.current.entry.getMethod() == 8) {
/*  528 */       read = readDeflated(buffer, offset, length);
/*  529 */     } else if (this.current.entry.getMethod() == ZipMethod.UNSHRINKING.getCode() || this.current
/*  530 */       .entry.getMethod() == ZipMethod.IMPLODING.getCode() || this.current
/*  531 */       .entry.getMethod() == ZipMethod.ENHANCED_DEFLATED.getCode() || this.current
/*  532 */       .entry.getMethod() == ZipMethod.BZIP2.getCode()) {
/*  533 */       read = this.current.inputStream.read(buffer, offset, length);
/*      */     } else {
/*  535 */       throw new UnsupportedZipFeatureException(ZipMethod.getMethodByCode(this.current.entry.getMethod()), this.current
/*  536 */           .entry);
/*      */     } 
/*      */     
/*  539 */     if (read >= 0) {
/*  540 */       this.current.crc.update(buffer, offset, read);
/*  541 */       this.uncompressedCount += read;
/*      */     } 
/*      */     
/*  544 */     return read;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getCompressedCount() {
/*  553 */     int method = this.current.entry.getMethod();
/*  554 */     if (method == 0) {
/*  555 */       return this.current.bytesRead;
/*      */     }
/*  557 */     if (method == 8) {
/*  558 */       return getBytesInflated();
/*      */     }
/*  560 */     if (method == ZipMethod.UNSHRINKING.getCode() || method == ZipMethod.IMPLODING
/*  561 */       .getCode() || method == ZipMethod.ENHANCED_DEFLATED
/*  562 */       .getCode() || method == ZipMethod.BZIP2
/*  563 */       .getCode()) {
/*  564 */       return ((InputStreamStatistics)this.current.checkInputStream()).getCompressedCount();
/*      */     }
/*  566 */     return -1L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getUncompressedCount() {
/*  574 */     return this.uncompressedCount;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readStored(byte[] buffer, int offset, int length) throws IOException {
/*  582 */     if (this.current.hasDataDescriptor) {
/*  583 */       if (this.lastStoredEntry == null) {
/*  584 */         readStoredEntry();
/*      */       }
/*  586 */       return this.lastStoredEntry.read(buffer, offset, length);
/*      */     } 
/*      */     
/*  589 */     long csize = this.current.entry.getSize();
/*  590 */     if (this.current.bytesRead >= csize) {
/*  591 */       return -1;
/*      */     }
/*      */     
/*  594 */     if (this.buf.position() >= this.buf.limit()) {
/*  595 */       this.buf.position(0);
/*  596 */       int l = this.inputStream.read(this.buf.array());
/*  597 */       if (l == -1) {
/*  598 */         this.buf.limit(0);
/*  599 */         throw new IOException("Truncated ZIP file");
/*      */       } 
/*  601 */       this.buf.limit(l);
/*      */       
/*  603 */       count(l);
/*  604 */       CurrentEntry currentEntry1 = this.current; currentEntry1.bytesReadFromStream = currentEntry1.bytesReadFromStream + l;
/*      */     } 
/*      */     
/*  607 */     int toRead = Math.min(this.buf.remaining(), length);
/*  608 */     if (csize - this.current.bytesRead < toRead)
/*      */     {
/*  610 */       toRead = (int)(csize - this.current.bytesRead);
/*      */     }
/*  612 */     this.buf.get(buffer, offset, toRead);
/*  613 */     CurrentEntry currentEntry = this.current; currentEntry.bytesRead = currentEntry.bytesRead + toRead;
/*  614 */     return toRead;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readDeflated(byte[] buffer, int offset, int length) throws IOException {
/*  621 */     int read = readFromInflater(buffer, offset, length);
/*  622 */     if (read <= 0) {
/*  623 */       if (this.inf.finished()) {
/*  624 */         return -1;
/*      */       }
/*  626 */       if (this.inf.needsDictionary()) {
/*  627 */         throw new ZipException("This archive needs a preset dictionary which is not supported by Commons Compress.");
/*      */       }
/*      */ 
/*      */       
/*  631 */       if (read == -1) {
/*  632 */         throw new IOException("Truncated ZIP file");
/*      */       }
/*      */     } 
/*  635 */     return read;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readFromInflater(byte[] buffer, int offset, int length) throws IOException {
/*  643 */     int read = 0;
/*      */     do {
/*  645 */       if (this.inf.needsInput()) {
/*  646 */         int l = fill();
/*  647 */         if (l > 0)
/*  648 */         { CurrentEntry currentEntry = this.current; currentEntry.bytesReadFromStream = currentEntry.bytesReadFromStream + this.buf.limit(); }
/*  649 */         else { if (l == -1) {
/*  650 */             return -1;
/*      */           }
/*      */           break; }
/*      */       
/*      */       } 
/*      */       try {
/*  656 */         read = this.inf.inflate(buffer, offset, length);
/*  657 */       } catch (DataFormatException e) {
/*  658 */         throw (IOException)(new ZipException(e.getMessage())).initCause(e);
/*      */       } 
/*  660 */     } while (read == 0 && this.inf.needsInput());
/*  661 */     return read;
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() throws IOException {
/*  666 */     if (!this.closed) {
/*  667 */       this.closed = true;
/*      */       try {
/*  669 */         this.inputStream.close();
/*      */       } finally {
/*  671 */         this.inf.end();
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
/*      */   public long skip(long value) throws IOException {
/*  693 */     if (value >= 0L) {
/*  694 */       long skipped = 0L;
/*  695 */       while (skipped < value) {
/*  696 */         long rem = value - skipped;
/*  697 */         int x = read(this.skipBuf, 0, (int)((this.skipBuf.length > rem) ? rem : this.skipBuf.length));
/*  698 */         if (x == -1) {
/*  699 */           return skipped;
/*      */         }
/*  701 */         skipped += x;
/*      */       } 
/*  703 */       return skipped;
/*      */     } 
/*  705 */     throw new IllegalArgumentException();
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
/*      */   public static boolean matches(byte[] signature, int length) {
/*  718 */     if (length < ZipArchiveOutputStream.LFH_SIG.length) {
/*  719 */       return false;
/*      */     }
/*      */     
/*  722 */     return (checksig(signature, ZipArchiveOutputStream.LFH_SIG) || 
/*  723 */       checksig(signature, ZipArchiveOutputStream.EOCD_SIG) || 
/*  724 */       checksig(signature, ZipArchiveOutputStream.DD_SIG) || 
/*  725 */       checksig(signature, ZipLong.SINGLE_SEGMENT_SPLIT_MARKER.getBytes()));
/*      */   }
/*      */   
/*      */   private static boolean checksig(byte[] signature, byte[] expected) {
/*  729 */     for (int i = 0; i < expected.length; i++) {
/*  730 */       if (signature[i] != expected[i]) {
/*  731 */         return false;
/*      */       }
/*      */     } 
/*  734 */     return true;
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
/*      */   private void closeEntry() throws IOException {
/*  756 */     if (this.closed) {
/*  757 */       throw new IOException("The stream is closed");
/*      */     }
/*  759 */     if (this.current == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  764 */     if (currentEntryHasOutstandingBytes()) {
/*  765 */       drainCurrentEntryData();
/*      */     } else {
/*      */       
/*  768 */       skip(Long.MAX_VALUE);
/*      */ 
/*      */       
/*  771 */       long inB = (this.current.entry.getMethod() == 8) ? getBytesInflated() : this.current.bytesRead;
/*      */ 
/*      */ 
/*      */       
/*  775 */       int diff = (int)(this.current.bytesReadFromStream - inB);
/*      */ 
/*      */       
/*  778 */       if (diff > 0) {
/*  779 */         pushback(this.buf.array(), this.buf.limit() - diff, diff);
/*  780 */         CurrentEntry currentEntry = this.current; currentEntry.bytesReadFromStream = currentEntry.bytesReadFromStream - diff;
/*      */       } 
/*      */ 
/*      */       
/*  784 */       if (currentEntryHasOutstandingBytes()) {
/*  785 */         drainCurrentEntryData();
/*      */       }
/*      */     } 
/*      */     
/*  789 */     if (this.lastStoredEntry == null && this.current.hasDataDescriptor) {
/*  790 */       readDataDescriptor();
/*      */     }
/*      */     
/*  793 */     this.inf.reset();
/*  794 */     this.buf.clear().flip();
/*  795 */     this.current = null;
/*  796 */     this.lastStoredEntry = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean currentEntryHasOutstandingBytes() {
/*  807 */     return (this.current.bytesReadFromStream <= this.current.entry.getCompressedSize() && 
/*  808 */       !this.current.hasDataDescriptor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void drainCurrentEntryData() throws IOException {
/*  816 */     long remaining = this.current.entry.getCompressedSize() - this.current.bytesReadFromStream;
/*  817 */     while (remaining > 0L) {
/*  818 */       long n = this.inputStream.read(this.buf.array(), 0, (int)Math.min(this.buf.capacity(), remaining));
/*  819 */       if (n < 0L) {
/*  820 */         throw new EOFException("Truncated ZIP entry: " + 
/*  821 */             ArchiveUtils.sanitize(this.current.entry.getName()));
/*      */       }
/*  823 */       count(n);
/*  824 */       remaining -= n;
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
/*      */   private long getBytesInflated() {
/*  844 */     long inB = this.inf.getBytesRead();
/*  845 */     if (this.current.bytesReadFromStream >= 4294967296L) {
/*  846 */       while (inB + 4294967296L <= this.current.bytesReadFromStream) {
/*  847 */         inB += 4294967296L;
/*      */       }
/*      */     }
/*  850 */     return inB;
/*      */   }
/*      */   
/*      */   private int fill() throws IOException {
/*  854 */     if (this.closed) {
/*  855 */       throw new IOException("The stream is closed");
/*      */     }
/*  857 */     int length = this.inputStream.read(this.buf.array());
/*  858 */     if (length > 0) {
/*  859 */       this.buf.limit(length);
/*  860 */       count(this.buf.limit());
/*  861 */       this.inf.setInput(this.buf.array(), 0, this.buf.limit());
/*      */     } 
/*  863 */     return length;
/*      */   }
/*      */   
/*      */   private void readFully(byte[] b) throws IOException {
/*  867 */     readFully(b, 0);
/*      */   }
/*      */   
/*      */   private void readFully(byte[] b, int off) throws IOException {
/*  871 */     int len = b.length - off;
/*  872 */     int count = IOUtils.readFully(this.inputStream, b, off, len);
/*  873 */     count(count);
/*  874 */     if (count < len) {
/*  875 */       throw new EOFException();
/*      */     }
/*      */   }
/*      */   
/*      */   private byte[] readRange(int len) throws IOException {
/*  880 */     byte[] ret = IOUtils.readRange(this.inputStream, len);
/*  881 */     count(ret.length);
/*  882 */     if (ret.length < len) {
/*  883 */       throw new EOFException();
/*      */     }
/*  885 */     return ret;
/*      */   }
/*      */   
/*      */   private void readDataDescriptor() throws IOException {
/*  889 */     readFully(this.wordBuf);
/*  890 */     ZipLong val = new ZipLong(this.wordBuf);
/*  891 */     if (ZipLong.DD_SIG.equals(val)) {
/*      */       
/*  893 */       readFully(this.wordBuf);
/*  894 */       val = new ZipLong(this.wordBuf);
/*      */     } 
/*  896 */     this.current.entry.setCrc(val.getValue());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  909 */     readFully(this.twoDwordBuf);
/*  910 */     ZipLong potentialSig = new ZipLong(this.twoDwordBuf, 8);
/*  911 */     if (potentialSig.equals(ZipLong.CFH_SIG) || potentialSig.equals(ZipLong.LFH_SIG)) {
/*  912 */       pushback(this.twoDwordBuf, 8, 8);
/*  913 */       long size = ZipLong.getValue(this.twoDwordBuf);
/*  914 */       if (size < 0L) {
/*  915 */         throw new ZipException("broken archive, entry with negative compressed size");
/*      */       }
/*  917 */       this.current.entry.setCompressedSize(size);
/*  918 */       size = ZipLong.getValue(this.twoDwordBuf, 4);
/*  919 */       if (size < 0L) {
/*  920 */         throw new ZipException("broken archive, entry with negative size");
/*      */       }
/*  922 */       this.current.entry.setSize(size);
/*      */     } else {
/*  924 */       long size = ZipEightByteInteger.getLongValue(this.twoDwordBuf);
/*  925 */       if (size < 0L) {
/*  926 */         throw new ZipException("broken archive, entry with negative compressed size");
/*      */       }
/*  928 */       this.current.entry.setCompressedSize(size);
/*  929 */       size = ZipEightByteInteger.getLongValue(this.twoDwordBuf, 8);
/*  930 */       if (size < 0L) {
/*  931 */         throw new ZipException("broken archive, entry with negative size");
/*      */       }
/*  933 */       this.current.entry.setSize(size);
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
/*      */   private boolean supportsDataDescriptorFor(ZipArchiveEntry entry) {
/*  945 */     return (!entry.getGeneralPurposeBit().usesDataDescriptor() || (this.allowStoredEntriesWithDataDescriptor && entry
/*  946 */       .getMethod() == 0) || entry
/*  947 */       .getMethod() == 8 || entry
/*  948 */       .getMethod() == ZipMethod.ENHANCED_DEFLATED.getCode());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean supportsCompressedSizeFor(ZipArchiveEntry entry) {
/*  956 */     return (entry.getCompressedSize() != -1L || entry
/*  957 */       .getMethod() == 8 || entry
/*  958 */       .getMethod() == ZipMethod.ENHANCED_DEFLATED.getCode() || (entry
/*  959 */       .getGeneralPurposeBit().usesDataDescriptor() && this.allowStoredEntriesWithDataDescriptor && entry
/*      */       
/*  961 */       .getMethod() == 0));
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
/*      */   private void readStoredEntry() throws IOException {
/*  988 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  989 */     int off = 0;
/*  990 */     boolean done = false;
/*      */ 
/*      */     
/*  993 */     int ddLen = this.current.usesZip64 ? 20 : 12;
/*      */     
/*  995 */     while (!done) {
/*  996 */       int r = this.inputStream.read(this.buf.array(), off, 512 - off);
/*  997 */       if (r <= 0)
/*      */       {
/*      */         
/* 1000 */         throw new IOException("Truncated ZIP file");
/*      */       }
/* 1002 */       if (r + off < 4) {
/*      */         
/* 1004 */         off += r;
/*      */         
/*      */         continue;
/*      */       } 
/* 1008 */       done = bufferContainsSignature(bos, off, r, ddLen);
/* 1009 */       if (!done) {
/* 1010 */         off = cacheBytesRead(bos, off, r, ddLen);
/*      */       }
/*      */     } 
/* 1013 */     if (this.current.entry.getCompressedSize() != this.current.entry.getSize()) {
/* 1014 */       throw new ZipException("compressed and uncompressed size don't match while reading a stored entry using data descriptor. Either the archive is broken or it can not be read using ZipArchiveInputStream and you must use ZipFile. A common cause for this is a ZIP archive containing a ZIP archive. See http://commons.apache.org/proper/commons-compress/zip.html#ZipArchiveInputStream_vs_ZipFile");
/*      */     }
/*      */     
/* 1017 */     byte[] b = bos.toByteArray();
/* 1018 */     if (b.length != this.current.entry.getSize()) {
/* 1019 */       throw new ZipException("actual and claimed size don't match while reading a stored entry using data descriptor. Either the archive is broken or it can not be read using ZipArchiveInputStream and you must use ZipFile. A common cause for this is a ZIP archive containing a ZIP archive. See http://commons.apache.org/proper/commons-compress/zip.html#ZipArchiveInputStream_vs_ZipFile");
/*      */     }
/*      */     
/* 1022 */     this.lastStoredEntry = new ByteArrayInputStream(b);
/*      */   }
/*      */   
/* 1025 */   private static final byte[] LFH = ZipLong.LFH_SIG.getBytes();
/* 1026 */   private static final byte[] CFH = ZipLong.CFH_SIG.getBytes();
/* 1027 */   private static final byte[] DD = ZipLong.DD_SIG.getBytes();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean bufferContainsSignature(ByteArrayOutputStream bos, int offset, int lastRead, int expectedDDLen) throws IOException {
/* 1040 */     boolean done = false;
/* 1041 */     for (int i = 0; !done && i < offset + lastRead - 4; i++) {
/* 1042 */       if (this.buf.array()[i] == LFH[0] && this.buf.array()[i + 1] == LFH[1]) {
/* 1043 */         int expectDDPos = i;
/* 1044 */         if ((i >= expectedDDLen && this.buf
/* 1045 */           .array()[i + 2] == LFH[2] && this.buf.array()[i + 3] == LFH[3]) || (this.buf
/* 1046 */           .array()[i + 2] == CFH[2] && this.buf.array()[i + 3] == CFH[3])) {
/*      */           
/* 1048 */           expectDDPos = i - expectedDDLen;
/* 1049 */           done = true;
/*      */         }
/* 1051 */         else if (this.buf.array()[i + 2] == DD[2] && this.buf.array()[i + 3] == DD[3]) {
/*      */           
/* 1053 */           done = true;
/*      */         } 
/* 1055 */         if (done) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1060 */           pushback(this.buf.array(), expectDDPos, offset + lastRead - expectDDPos);
/* 1061 */           bos.write(this.buf.array(), 0, expectDDPos);
/* 1062 */           readDataDescriptor();
/*      */         } 
/*      */       } 
/*      */     } 
/* 1066 */     return done;
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
/*      */   private int cacheBytesRead(ByteArrayOutputStream bos, int offset, int lastRead, int expecteDDLen) {
/* 1079 */     int cacheable = offset + lastRead - expecteDDLen - 3;
/* 1080 */     if (cacheable > 0) {
/* 1081 */       bos.write(this.buf.array(), 0, cacheable);
/* 1082 */       System.arraycopy(this.buf.array(), cacheable, this.buf.array(), 0, expecteDDLen + 3);
/* 1083 */       offset = expecteDDLen + 3;
/*      */     } else {
/* 1085 */       offset += lastRead;
/*      */     } 
/* 1087 */     return offset;
/*      */   }
/*      */   
/*      */   private void pushback(byte[] buf, int offset, int length) throws IOException {
/* 1091 */     ((PushbackInputStream)this.inputStream).unread(buf, offset, length);
/* 1092 */     pushedBackBytes(length);
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
/*      */   private void skipRemainderOfArchive() throws IOException {
/* 1120 */     if (this.entriesRead > 0) {
/* 1121 */       realSkip(this.entriesRead * 46L - 30L);
/* 1122 */       boolean foundEocd = findEocdRecord();
/* 1123 */       if (foundEocd) {
/* 1124 */         realSkip(16L);
/* 1125 */         readFully(this.shortBuf);
/*      */         
/* 1127 */         int commentLen = ZipShort.getValue(this.shortBuf);
/* 1128 */         if (commentLen >= 0) {
/* 1129 */           realSkip(commentLen);
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1134 */     throw new IOException("Truncated ZIP file");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean findEocdRecord() throws IOException {
/* 1142 */     int currentByte = -1;
/* 1143 */     boolean skipReadCall = false;
/* 1144 */     while (skipReadCall || (currentByte = readOneByte()) > -1) {
/* 1145 */       skipReadCall = false;
/* 1146 */       if (!isFirstByteOfEocdSig(currentByte)) {
/*      */         continue;
/*      */       }
/* 1149 */       currentByte = readOneByte();
/* 1150 */       if (currentByte != ZipArchiveOutputStream.EOCD_SIG[1]) {
/* 1151 */         if (currentByte == -1) {
/*      */           break;
/*      */         }
/* 1154 */         skipReadCall = isFirstByteOfEocdSig(currentByte);
/*      */         continue;
/*      */       } 
/* 1157 */       currentByte = readOneByte();
/* 1158 */       if (currentByte != ZipArchiveOutputStream.EOCD_SIG[2]) {
/* 1159 */         if (currentByte == -1) {
/*      */           break;
/*      */         }
/* 1162 */         skipReadCall = isFirstByteOfEocdSig(currentByte);
/*      */         continue;
/*      */       } 
/* 1165 */       currentByte = readOneByte();
/* 1166 */       if (currentByte == -1) {
/*      */         break;
/*      */       }
/* 1169 */       if (currentByte == ZipArchiveOutputStream.EOCD_SIG[3]) {
/* 1170 */         return true;
/*      */       }
/* 1172 */       skipReadCall = isFirstByteOfEocdSig(currentByte);
/*      */     } 
/* 1174 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void realSkip(long value) throws IOException {
/* 1185 */     if (value >= 0L) {
/* 1186 */       long skipped = 0L;
/* 1187 */       while (skipped < value) {
/* 1188 */         long rem = value - skipped;
/* 1189 */         int x = this.inputStream.read(this.skipBuf, 0, (int)((this.skipBuf.length > rem) ? rem : this.skipBuf.length));
/* 1190 */         if (x == -1) {
/*      */           return;
/*      */         }
/* 1193 */         count(x);
/* 1194 */         skipped += x;
/*      */       } 
/*      */       return;
/*      */     } 
/* 1198 */     throw new IllegalArgumentException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readOneByte() throws IOException {
/* 1208 */     int b = this.inputStream.read();
/* 1209 */     if (b != -1) {
/* 1210 */       count(1);
/*      */     }
/* 1212 */     return b;
/*      */   }
/*      */   
/*      */   private boolean isFirstByteOfEocdSig(int b) {
/* 1216 */     return (b == ZipArchiveOutputStream.EOCD_SIG[0]);
/*      */   }
/*      */   
/* 1219 */   private static final byte[] APK_SIGNING_BLOCK_MAGIC = new byte[] { 65, 80, 75, 32, 83, 105, 103, 32, 66, 108, 111, 99, 107, 32, 52, 50 };
/*      */ 
/*      */   
/* 1222 */   private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isApkSigningBlock(byte[] suspectLocalFileHeader) throws IOException {
/* 1240 */     BigInteger len = ZipEightByteInteger.getValue(suspectLocalFileHeader);
/*      */ 
/*      */     
/* 1243 */     BigInteger toSkip = len.add(BigInteger.valueOf((8 - suspectLocalFileHeader.length) - APK_SIGNING_BLOCK_MAGIC.length));
/*      */     
/* 1245 */     byte[] magic = new byte[APK_SIGNING_BLOCK_MAGIC.length];
/*      */     
/*      */     try {
/* 1248 */       if (toSkip.signum() < 0) {
/*      */         
/* 1250 */         int off = suspectLocalFileHeader.length + toSkip.intValue();
/*      */         
/* 1252 */         if (off < 8) {
/* 1253 */           return false;
/*      */         }
/* 1255 */         int bytesInBuffer = Math.abs(toSkip.intValue());
/* 1256 */         System.arraycopy(suspectLocalFileHeader, off, magic, 0, Math.min(bytesInBuffer, magic.length));
/* 1257 */         if (bytesInBuffer < magic.length) {
/* 1258 */           readFully(magic, bytesInBuffer);
/*      */         }
/*      */       } else {
/* 1261 */         while (toSkip.compareTo(LONG_MAX) > 0) {
/* 1262 */           realSkip(Long.MAX_VALUE);
/* 1263 */           toSkip = toSkip.add(LONG_MAX.negate());
/*      */         } 
/* 1265 */         realSkip(toSkip.longValue());
/* 1266 */         readFully(magic);
/*      */       } 
/* 1268 */     } catch (EOFException ex) {
/*      */       
/* 1270 */       return false;
/*      */     } 
/* 1272 */     return Arrays.equals(magic, APK_SIGNING_BLOCK_MAGIC);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class CurrentEntry
/*      */   {
/* 1284 */     private final ZipArchiveEntry entry = new ZipArchiveEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean hasDataDescriptor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean usesZip64;
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
/*      */     
/*      */     private long bytesReadFromStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1314 */     private final CRC32 crc = new CRC32();
/*      */ 
/*      */ 
/*      */     
/*      */     private InputStream inputStream;
/*      */ 
/*      */ 
/*      */     
/*      */     private <T extends InputStream> T checkInputStream() {
/* 1323 */       return (T)Objects.<InputStream>requireNonNull(this.inputStream, "inputStream");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private CurrentEntry() {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class BoundedInputStream
/*      */     extends InputStream
/*      */   {
/*      */     private final InputStream in;
/*      */ 
/*      */     
/*      */     private final long max;
/*      */ 
/*      */     
/*      */     private long pos;
/*      */ 
/*      */ 
/*      */     
/*      */     public BoundedInputStream(InputStream in, long size) {
/* 1349 */       this.max = size;
/* 1350 */       this.in = in;
/*      */     }
/*      */ 
/*      */     
/*      */     public int read() throws IOException {
/* 1355 */       if (this.max >= 0L && this.pos >= this.max) {
/* 1356 */         return -1;
/*      */       }
/* 1358 */       int result = this.in.read();
/* 1359 */       this.pos++;
/* 1360 */       ZipArchiveInputStream.this.count(1);
/* 1361 */       ZipArchiveInputStream.this.current.bytesReadFromStream++;
/* 1362 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public int read(byte[] b) throws IOException {
/* 1367 */       return read(b, 0, b.length);
/*      */     }
/*      */ 
/*      */     
/*      */     public int read(byte[] b, int off, int len) throws IOException {
/* 1372 */       if (len == 0) {
/* 1373 */         return 0;
/*      */       }
/* 1375 */       if (this.max >= 0L && this.pos >= this.max) {
/* 1376 */         return -1;
/*      */       }
/* 1378 */       long maxRead = (this.max >= 0L) ? Math.min(len, this.max - this.pos) : len;
/* 1379 */       int bytesRead = this.in.read(b, off, (int)maxRead);
/*      */       
/* 1381 */       if (bytesRead == -1) {
/* 1382 */         return -1;
/*      */       }
/*      */       
/* 1385 */       this.pos += bytesRead;
/* 1386 */       ZipArchiveInputStream.this.count(bytesRead);
/* 1387 */       ZipArchiveInputStream.CurrentEntry currentEntry = ZipArchiveInputStream.this.current; currentEntry.bytesReadFromStream = currentEntry.bytesReadFromStream + bytesRead;
/* 1388 */       return bytesRead;
/*      */     }
/*      */ 
/*      */     
/*      */     public long skip(long n) throws IOException {
/* 1393 */       long toSkip = (this.max >= 0L) ? Math.min(n, this.max - this.pos) : n;
/* 1394 */       long skippedBytes = IOUtils.skip(this.in, toSkip);
/* 1395 */       this.pos += skippedBytes;
/* 1396 */       return skippedBytes;
/*      */     }
/*      */ 
/*      */     
/*      */     public int available() throws IOException {
/* 1401 */       if (this.max >= 0L && this.pos >= this.max) {
/* 1402 */         return 0;
/*      */       }
/* 1404 */       return this.in.available();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\zip\ZipArchiveInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */