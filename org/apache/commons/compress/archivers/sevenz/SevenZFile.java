/*      */ package org.apache.commons.compress.archivers.sevenz;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.Closeable;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.FilterInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.channels.Channels;
/*      */ import java.nio.channels.SeekableByteChannel;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.StandardOpenOption;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.zip.CRC32;
/*      */ import java.util.zip.CheckedInputStream;
/*      */ import org.apache.commons.compress.MemoryLimitException;
/*      */ import org.apache.commons.compress.utils.BoundedInputStream;
/*      */ import org.apache.commons.compress.utils.ByteUtils;
/*      */ import org.apache.commons.compress.utils.CRC32VerifyingInputStream;
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
/*      */ public class SevenZFile
/*      */   implements Closeable
/*      */ {
/*      */   static final int SIGNATURE_HEADER_SIZE = 32;
/*      */   private static final String DEFAULT_FILE_NAME = "unknown archive";
/*      */   private final String fileName;
/*      */   private SeekableByteChannel channel;
/*      */   private final Archive archive;
/*   99 */   private int currentEntryIndex = -1;
/*  100 */   private int currentFolderIndex = -1;
/*      */   
/*      */   private InputStream currentFolderInputStream;
/*      */   
/*      */   private byte[] password;
/*      */   private final SevenZFileOptions options;
/*      */   private long compressedBytesReadFromCurrentEntry;
/*      */   private long uncompressedBytesReadFromCurrentEntry;
/*  108 */   private final ArrayList<InputStream> deferredBlockStreams = new ArrayList<>();
/*      */ 
/*      */   
/*  111 */   static final byte[] sevenZSignature = new byte[] { 55, 122, -68, -81, 39, 28 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SevenZFile(File fileName, char[] password) throws IOException {
/*  124 */     this(fileName, password, SevenZFileOptions.DEFAULT);
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
/*      */   public SevenZFile(File fileName, char[] password, SevenZFileOptions options) throws IOException {
/*  137 */     this(Files.newByteChannel(fileName.toPath(), EnumSet.of(StandardOpenOption.READ), (FileAttribute<?>[])new FileAttribute[0]), fileName
/*  138 */         .getAbsolutePath(), utf16Decode(password), true, options);
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
/*      */   @Deprecated
/*      */   public SevenZFile(File fileName, byte[] password) throws IOException {
/*  153 */     this(Files.newByteChannel(fileName.toPath(), EnumSet.of(StandardOpenOption.READ), (FileAttribute<?>[])new FileAttribute[0]), fileName
/*  154 */         .getAbsolutePath(), password, true, SevenZFileOptions.DEFAULT);
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
/*      */   public SevenZFile(SeekableByteChannel channel) throws IOException {
/*  169 */     this(channel, SevenZFileOptions.DEFAULT);
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
/*      */   public SevenZFile(SeekableByteChannel channel, SevenZFileOptions options) throws IOException {
/*  185 */     this(channel, "unknown archive", null, options);
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
/*      */   public SevenZFile(SeekableByteChannel channel, char[] password) throws IOException {
/*  202 */     this(channel, password, SevenZFileOptions.DEFAULT);
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
/*      */   public SevenZFile(SeekableByteChannel channel, char[] password, SevenZFileOptions options) throws IOException {
/*  220 */     this(channel, "unknown archive", password, options);
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
/*      */   public SevenZFile(SeekableByteChannel channel, String fileName, char[] password) throws IOException {
/*  238 */     this(channel, fileName, password, SevenZFileOptions.DEFAULT);
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
/*      */   public SevenZFile(SeekableByteChannel channel, String fileName, char[] password, SevenZFileOptions options) throws IOException {
/*  257 */     this(channel, fileName, utf16Decode(password), false, options);
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
/*      */   public SevenZFile(SeekableByteChannel channel, String fileName) throws IOException {
/*  274 */     this(channel, fileName, SevenZFileOptions.DEFAULT);
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
/*      */   public SevenZFile(SeekableByteChannel channel, String fileName, SevenZFileOptions options) throws IOException {
/*  292 */     this(channel, fileName, null, false, options);
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
/*      */   @Deprecated
/*      */   public SevenZFile(SeekableByteChannel channel, byte[] password) throws IOException {
/*  313 */     this(channel, "unknown archive", password);
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
/*      */   @Deprecated
/*      */   public SevenZFile(SeekableByteChannel channel, String fileName, byte[] password) throws IOException {
/*  335 */     this(channel, fileName, password, false, SevenZFileOptions.DEFAULT);
/*      */   }
/*      */ 
/*      */   
/*      */   private SevenZFile(SeekableByteChannel channel, String filename, byte[] password, boolean closeOnError, SevenZFileOptions options) throws IOException {
/*  340 */     boolean succeeded = false;
/*  341 */     this.channel = channel;
/*  342 */     this.fileName = filename;
/*  343 */     this.options = options;
/*      */     try {
/*  345 */       this.archive = readHeaders(password);
/*  346 */       if (password != null) {
/*  347 */         this.password = Arrays.copyOf(password, password.length);
/*      */       } else {
/*  349 */         this.password = null;
/*      */       } 
/*  351 */       succeeded = true;
/*      */     } finally {
/*  353 */       if (!succeeded && closeOnError) {
/*  354 */         this.channel.close();
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
/*      */   public SevenZFile(File fileName) throws IOException {
/*  366 */     this(fileName, SevenZFileOptions.DEFAULT);
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
/*      */   public SevenZFile(File fileName, SevenZFileOptions options) throws IOException {
/*  378 */     this(fileName, (char[])null, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() throws IOException {
/*  387 */     if (this.channel != null) {
/*      */       try {
/*  389 */         this.channel.close();
/*      */       } finally {
/*  391 */         this.channel = null;
/*  392 */         if (this.password != null) {
/*  393 */           Arrays.fill(this.password, (byte)0);
/*      */         }
/*  395 */         this.password = null;
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
/*      */   public SevenZArchiveEntry getNextEntry() throws IOException {
/*  408 */     if (this.currentEntryIndex >= this.archive.files.length - 1) {
/*  409 */       return null;
/*      */     }
/*  411 */     this.currentEntryIndex++;
/*  412 */     SevenZArchiveEntry entry = this.archive.files[this.currentEntryIndex];
/*  413 */     if (entry.getName() == null && this.options.getUseDefaultNameForUnnamedEntries()) {
/*  414 */       entry.setName(getDefaultName());
/*      */     }
/*  416 */     buildDecodingStream(this.currentEntryIndex, false);
/*  417 */     this.uncompressedBytesReadFromCurrentEntry = this.compressedBytesReadFromCurrentEntry = 0L;
/*  418 */     return entry;
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
/*      */   public Iterable<SevenZArchiveEntry> getEntries() {
/*  435 */     return new ArrayList<>(Arrays.asList(this.archive.files));
/*      */   }
/*      */ 
/*      */   
/*      */   private Archive readHeaders(byte[] password) throws IOException {
/*  440 */     ByteBuffer buf = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN);
/*  441 */     readFully(buf);
/*  442 */     byte[] signature = new byte[6];
/*  443 */     buf.get(signature);
/*  444 */     if (!Arrays.equals(signature, sevenZSignature)) {
/*  445 */       throw new IOException("Bad 7z signature");
/*      */     }
/*      */     
/*  448 */     byte archiveVersionMajor = buf.get();
/*  449 */     byte archiveVersionMinor = buf.get();
/*  450 */     if (archiveVersionMajor != 0) {
/*  451 */       throw new IOException(String.format("Unsupported 7z version (%d,%d)", new Object[] {
/*  452 */               Byte.valueOf(archiveVersionMajor), Byte.valueOf(archiveVersionMinor)
/*      */             }));
/*      */     }
/*  455 */     boolean headerLooksValid = false;
/*  456 */     long startHeaderCrc = 0xFFFFFFFFL & buf.getInt();
/*  457 */     if (startHeaderCrc == 0L) {
/*      */       
/*  459 */       long currentPosition = this.channel.position();
/*  460 */       ByteBuffer peekBuf = ByteBuffer.allocate(20);
/*  461 */       readFully(peekBuf);
/*  462 */       this.channel.position(currentPosition);
/*      */       
/*  464 */       while (peekBuf.hasRemaining()) {
/*  465 */         if (peekBuf.get() != 0) {
/*  466 */           headerLooksValid = true;
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } else {
/*  471 */       headerLooksValid = true;
/*      */     } 
/*      */     
/*  474 */     if (headerLooksValid) {
/*  475 */       return initializeArchive(readStartHeader(startHeaderCrc), password, true);
/*      */     }
/*      */     
/*  478 */     if (this.options.getTryToRecoverBrokenArchives()) {
/*  479 */       return tryToLocateEndHeader(password);
/*      */     }
/*  481 */     throw new IOException("archive seems to be invalid.\nYou may want to retry and enable the tryToRecoverBrokenArchives if the archive could be a multi volume archive that has been closed prematurely.");
/*      */   }
/*      */ 
/*      */   
/*      */   private Archive tryToLocateEndHeader(byte[] password) throws IOException {
/*      */     long minPos;
/*  487 */     ByteBuffer nidBuf = ByteBuffer.allocate(1);
/*  488 */     long searchLimit = 1048576L;
/*      */     
/*  490 */     long previousDataSize = this.channel.position() + 20L;
/*      */ 
/*      */     
/*  493 */     if (this.channel.position() + 1048576L > this.channel.size()) {
/*  494 */       minPos = this.channel.position();
/*      */     } else {
/*  496 */       minPos = this.channel.size() - 1048576L;
/*      */     } 
/*  498 */     long pos = this.channel.size() - 1L;
/*      */     
/*  500 */     while (pos > minPos) {
/*  501 */       pos--;
/*  502 */       this.channel.position(pos);
/*  503 */       nidBuf.rewind();
/*  504 */       if (this.channel.read(nidBuf) < 1) {
/*  505 */         throw new EOFException();
/*      */       }
/*  507 */       int nid = nidBuf.array()[0];
/*      */       
/*  509 */       if (nid == 23 || nid == 1) {
/*      */         
/*      */         try {
/*  512 */           StartHeader startHeader = new StartHeader();
/*  513 */           startHeader.nextHeaderOffset = pos - previousDataSize;
/*  514 */           startHeader.nextHeaderSize = this.channel.size() - pos;
/*  515 */           Archive result = initializeArchive(startHeader, password, false);
/*      */           
/*  517 */           if (result.packSizes.length > 0 && result.files.length > 0) {
/*  518 */             return result;
/*      */           }
/*  520 */         } catch (Exception exception) {}
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  525 */     throw new IOException("Start header corrupt and unable to guess end header");
/*      */   }
/*      */   
/*      */   private Archive initializeArchive(StartHeader startHeader, byte[] password, boolean verifyCrc) throws IOException {
/*  529 */     assertFitsIntoNonNegativeInt("nextHeaderSize", startHeader.nextHeaderSize);
/*  530 */     int nextHeaderSizeInt = (int)startHeader.nextHeaderSize;
/*  531 */     this.channel.position(32L + startHeader.nextHeaderOffset);
/*  532 */     if (verifyCrc) {
/*  533 */       long position = this.channel.position();
/*  534 */       CheckedInputStream cis = new CheckedInputStream(Channels.newInputStream(this.channel), new CRC32());
/*  535 */       if (cis.skip(nextHeaderSizeInt) != nextHeaderSizeInt) {
/*  536 */         throw new IOException("Problem computing NextHeader CRC-32");
/*      */       }
/*  538 */       if (startHeader.nextHeaderCrc != cis.getChecksum().getValue()) {
/*  539 */         throw new IOException("NextHeader CRC-32 mismatch");
/*      */       }
/*  541 */       this.channel.position(position);
/*      */     } 
/*  543 */     Archive archive = new Archive();
/*  544 */     ByteBuffer buf = ByteBuffer.allocate(nextHeaderSizeInt).order(ByteOrder.LITTLE_ENDIAN);
/*  545 */     readFully(buf);
/*  546 */     int nid = getUnsignedByte(buf);
/*  547 */     if (nid == 23) {
/*  548 */       buf = readEncodedHeader(buf, archive, password);
/*      */       
/*  550 */       archive = new Archive();
/*  551 */       nid = getUnsignedByte(buf);
/*      */     } 
/*  553 */     if (nid != 1) {
/*  554 */       throw new IOException("Broken or unsupported archive: no Header");
/*      */     }
/*  556 */     readHeader(buf, archive);
/*  557 */     archive.subStreamsInfo = null;
/*  558 */     return archive;
/*      */   }
/*      */   
/*      */   private StartHeader readStartHeader(long startHeaderCrc) throws IOException {
/*  562 */     StartHeader startHeader = new StartHeader();
/*      */ 
/*      */     
/*  565 */     try (DataInputStream dataInputStream = new DataInputStream((InputStream)new CRC32VerifyingInputStream(new BoundedSeekableByteChannelInputStream(this.channel, 20L), 20L, startHeaderCrc))) {
/*      */       
/*  567 */       startHeader.nextHeaderOffset = Long.reverseBytes(dataInputStream.readLong());
/*  568 */       if (startHeader.nextHeaderOffset < 0L || startHeader.nextHeaderOffset + 32L > this.channel
/*  569 */         .size()) {
/*  570 */         throw new IOException("nextHeaderOffset is out of bounds");
/*      */       }
/*      */       
/*  573 */       startHeader.nextHeaderSize = Long.reverseBytes(dataInputStream.readLong());
/*  574 */       long nextHeaderEnd = startHeader.nextHeaderOffset + startHeader.nextHeaderSize;
/*  575 */       if (nextHeaderEnd < startHeader.nextHeaderOffset || nextHeaderEnd + 32L > this.channel
/*  576 */         .size()) {
/*  577 */         throw new IOException("nextHeaderSize is out of bounds");
/*      */       }
/*      */       
/*  580 */       startHeader.nextHeaderCrc = 0xFFFFFFFFL & Integer.reverseBytes(dataInputStream.readInt());
/*      */       
/*  582 */       return startHeader;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readHeader(ByteBuffer header, Archive archive) throws IOException {
/*  587 */     int pos = header.position();
/*  588 */     ArchiveStatistics stats = sanityCheckAndCollectStatistics(header);
/*  589 */     stats.assertValidity(this.options.getMaxMemoryLimitInKb());
/*  590 */     header.position(pos);
/*      */     
/*  592 */     int nid = getUnsignedByte(header);
/*      */     
/*  594 */     if (nid == 2) {
/*  595 */       readArchiveProperties(header);
/*  596 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */     
/*  599 */     if (nid == 3) {
/*  600 */       throw new IOException("Additional streams unsupported");
/*      */     }
/*      */ 
/*      */     
/*  604 */     if (nid == 4) {
/*  605 */       readStreamsInfo(header, archive);
/*  606 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */     
/*  609 */     if (nid == 5) {
/*  610 */       readFilesInfo(header, archive);
/*  611 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private ArchiveStatistics sanityCheckAndCollectStatistics(ByteBuffer header) throws IOException {
/*  617 */     ArchiveStatistics stats = new ArchiveStatistics();
/*      */     
/*  619 */     int nid = getUnsignedByte(header);
/*      */     
/*  621 */     if (nid == 2) {
/*  622 */       sanityCheckArchiveProperties(header);
/*  623 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */     
/*  626 */     if (nid == 3) {
/*  627 */       throw new IOException("Additional streams unsupported");
/*      */     }
/*      */ 
/*      */     
/*  631 */     if (nid == 4) {
/*  632 */       sanityCheckStreamsInfo(header, stats);
/*  633 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */     
/*  636 */     if (nid == 5) {
/*  637 */       sanityCheckFilesInfo(header, stats);
/*  638 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */     
/*  641 */     if (nid != 0) {
/*  642 */       throw new IOException("Badly terminated header, found " + nid);
/*      */     }
/*      */     
/*  645 */     return stats;
/*      */   }
/*      */ 
/*      */   
/*      */   private void readArchiveProperties(ByteBuffer input) throws IOException {
/*  650 */     int nid = getUnsignedByte(input);
/*  651 */     while (nid != 0) {
/*  652 */       long propertySize = readUint64(input);
/*  653 */       byte[] property = new byte[(int)propertySize];
/*  654 */       get(input, property);
/*  655 */       nid = getUnsignedByte(input);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void sanityCheckArchiveProperties(ByteBuffer header) throws IOException {
/*  661 */     int nid = getUnsignedByte(header);
/*  662 */     while (nid != 0) {
/*      */       
/*  664 */       int propertySize = assertFitsIntoNonNegativeInt("propertySize", readUint64(header));
/*  665 */       if (skipBytesFully(header, propertySize) < propertySize) {
/*  666 */         throw new IOException("invalid property size");
/*      */       }
/*  668 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */   }
/*      */   
/*      */   private ByteBuffer readEncodedHeader(ByteBuffer header, Archive archive, byte[] password) throws IOException {
/*      */     CRC32VerifyingInputStream cRC32VerifyingInputStream;
/*  674 */     int pos = header.position();
/*  675 */     ArchiveStatistics stats = new ArchiveStatistics();
/*  676 */     sanityCheckStreamsInfo(header, stats);
/*  677 */     stats.assertValidity(this.options.getMaxMemoryLimitInKb());
/*  678 */     header.position(pos);
/*      */     
/*  680 */     readStreamsInfo(header, archive);
/*      */     
/*  682 */     if (archive.folders == null || archive.folders.length == 0) {
/*  683 */       throw new IOException("no folders, can't read encoded header");
/*      */     }
/*  685 */     if (archive.packSizes == null || archive.packSizes.length == 0) {
/*  686 */       throw new IOException("no packed streams, can't read encoded header");
/*      */     }
/*      */ 
/*      */     
/*  690 */     Folder folder = archive.folders[0];
/*  691 */     int firstPackStreamIndex = 0;
/*  692 */     long folderOffset = 32L + archive.packPos + 0L;
/*      */ 
/*      */     
/*  695 */     this.channel.position(folderOffset);
/*  696 */     InputStream inputStreamStack = new BoundedSeekableByteChannelInputStream(this.channel, archive.packSizes[0]);
/*      */     
/*  698 */     for (Coder coder : folder.getOrderedCoders()) {
/*  699 */       if (coder.numInStreams != 1L || coder.numOutStreams != 1L) {
/*  700 */         throw new IOException("Multi input/output stream coders are not yet supported");
/*      */       }
/*  702 */       inputStreamStack = Coders.addDecoder(this.fileName, inputStreamStack, folder
/*  703 */           .getUnpackSizeForCoder(coder), coder, password, this.options.getMaxMemoryLimitInKb());
/*      */     } 
/*  705 */     if (folder.hasCrc)
/*      */     {
/*  707 */       cRC32VerifyingInputStream = new CRC32VerifyingInputStream(inputStreamStack, folder.getUnpackSize(), folder.crc);
/*      */     }
/*  709 */     int unpackSize = assertFitsIntoNonNegativeInt("unpackSize", folder.getUnpackSize());
/*  710 */     byte[] nextHeader = IOUtils.readRange((InputStream)cRC32VerifyingInputStream, unpackSize);
/*  711 */     if (nextHeader.length < unpackSize) {
/*  712 */       throw new IOException("premature end of stream");
/*      */     }
/*  714 */     cRC32VerifyingInputStream.close();
/*  715 */     return ByteBuffer.wrap(nextHeader).order(ByteOrder.LITTLE_ENDIAN);
/*      */   }
/*      */ 
/*      */   
/*      */   private void sanityCheckStreamsInfo(ByteBuffer header, ArchiveStatistics stats) throws IOException {
/*  720 */     int nid = getUnsignedByte(header);
/*      */     
/*  722 */     if (nid == 6) {
/*  723 */       sanityCheckPackInfo(header, stats);
/*  724 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */     
/*  727 */     if (nid == 7) {
/*  728 */       sanityCheckUnpackInfo(header, stats);
/*  729 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */     
/*  732 */     if (nid == 8) {
/*  733 */       sanityCheckSubStreamsInfo(header, stats);
/*  734 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */     
/*  737 */     if (nid != 0) {
/*  738 */       throw new IOException("Badly terminated StreamsInfo");
/*      */     }
/*      */   }
/*      */   
/*      */   private void readStreamsInfo(ByteBuffer header, Archive archive) throws IOException {
/*  743 */     int nid = getUnsignedByte(header);
/*      */     
/*  745 */     if (nid == 6) {
/*  746 */       readPackInfo(header, archive);
/*  747 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */     
/*  750 */     if (nid == 7) {
/*  751 */       readUnpackInfo(header, archive);
/*  752 */       nid = getUnsignedByte(header);
/*      */     } else {
/*      */       
/*  755 */       archive.folders = Folder.EMPTY_FOLDER_ARRAY;
/*      */     } 
/*      */     
/*  758 */     if (nid == 8) {
/*  759 */       readSubStreamsInfo(header, archive);
/*  760 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void sanityCheckPackInfo(ByteBuffer header, ArchiveStatistics stats) throws IOException {
/*  765 */     long packPos = readUint64(header);
/*  766 */     if (packPos < 0L || 32L + packPos > this.channel.size() || 32L + packPos < 0L)
/*      */     {
/*  768 */       throw new IOException("packPos (" + packPos + ") is out of range");
/*      */     }
/*  770 */     long numPackStreams = readUint64(header);
/*  771 */     stats.numberOfPackedStreams = assertFitsIntoNonNegativeInt("numPackStreams", numPackStreams);
/*  772 */     int nid = getUnsignedByte(header);
/*  773 */     if (nid == 9) {
/*  774 */       long totalPackSizes = 0L;
/*  775 */       for (int i = 0; i < stats.numberOfPackedStreams; i++) {
/*  776 */         long packSize = readUint64(header);
/*  777 */         totalPackSizes += packSize;
/*  778 */         long endOfPackStreams = 32L + packPos + totalPackSizes;
/*  779 */         if (packSize < 0L || endOfPackStreams > this.channel
/*  780 */           .size() || endOfPackStreams < packPos)
/*      */         {
/*  782 */           throw new IOException("packSize (" + packSize + ") is out of range");
/*      */         }
/*      */       } 
/*  785 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */     
/*  788 */     if (nid == 10) {
/*      */       
/*  790 */       int crcsDefined = readAllOrBits(header, stats.numberOfPackedStreams).cardinality();
/*  791 */       if (skipBytesFully(header, (4 * crcsDefined)) < (4 * crcsDefined)) {
/*  792 */         throw new IOException("invalid number of CRCs in PackInfo");
/*      */       }
/*  794 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */     
/*  797 */     if (nid != 0) {
/*  798 */       throw new IOException("Badly terminated PackInfo (" + nid + ")");
/*      */     }
/*      */   }
/*      */   
/*      */   private void readPackInfo(ByteBuffer header, Archive archive) throws IOException {
/*  803 */     archive.packPos = readUint64(header);
/*  804 */     int numPackStreamsInt = (int)readUint64(header);
/*  805 */     int nid = getUnsignedByte(header);
/*  806 */     if (nid == 9) {
/*  807 */       archive.packSizes = new long[numPackStreamsInt];
/*  808 */       for (int i = 0; i < archive.packSizes.length; i++) {
/*  809 */         archive.packSizes[i] = readUint64(header);
/*      */       }
/*  811 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */     
/*  814 */     if (nid == 10) {
/*  815 */       archive.packCrcsDefined = readAllOrBits(header, numPackStreamsInt);
/*  816 */       archive.packCrcs = new long[numPackStreamsInt];
/*  817 */       for (int i = 0; i < numPackStreamsInt; i++) {
/*  818 */         if (archive.packCrcsDefined.get(i)) {
/*  819 */           archive.packCrcs[i] = 0xFFFFFFFFL & getInt(header);
/*      */         }
/*      */       } 
/*      */       
/*  823 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void sanityCheckUnpackInfo(ByteBuffer header, ArchiveStatistics stats) throws IOException {
/*  829 */     int nid = getUnsignedByte(header);
/*  830 */     if (nid != 11) {
/*  831 */       throw new IOException("Expected kFolder, got " + nid);
/*      */     }
/*  833 */     long numFolders = readUint64(header);
/*  834 */     stats.numberOfFolders = assertFitsIntoNonNegativeInt("numFolders", numFolders);
/*  835 */     int external = getUnsignedByte(header);
/*  836 */     if (external != 0) {
/*  837 */       throw new IOException("External unsupported");
/*      */     }
/*      */     
/*  840 */     List<Integer> numberOfOutputStreamsPerFolder = new LinkedList<>();
/*  841 */     for (int i = 0; i < stats.numberOfFolders; i++) {
/*  842 */       numberOfOutputStreamsPerFolder.add(Integer.valueOf(sanityCheckFolder(header, stats)));
/*      */     }
/*      */     
/*  845 */     long totalNumberOfBindPairs = stats.numberOfOutStreams - stats.numberOfFolders;
/*  846 */     long packedStreamsRequiredByFolders = stats.numberOfInStreams - totalNumberOfBindPairs;
/*  847 */     if (packedStreamsRequiredByFolders < stats.numberOfPackedStreams) {
/*  848 */       throw new IOException("archive doesn't contain enough packed streams");
/*      */     }
/*      */     
/*  851 */     nid = getUnsignedByte(header);
/*  852 */     if (nid != 12) {
/*  853 */       throw new IOException("Expected kCodersUnpackSize, got " + nid);
/*      */     }
/*      */     
/*  856 */     for (Iterator<Integer> iterator = numberOfOutputStreamsPerFolder.iterator(); iterator.hasNext(); ) { int numberOfOutputStreams = ((Integer)iterator.next()).intValue();
/*  857 */       for (int j = 0; j < numberOfOutputStreams; j++) {
/*  858 */         long unpackSize = readUint64(header);
/*  859 */         if (unpackSize < 0L) {
/*  860 */           throw new IllegalArgumentException("negative unpackSize");
/*      */         }
/*      */       }  }
/*      */ 
/*      */     
/*  865 */     nid = getUnsignedByte(header);
/*  866 */     if (nid == 10) {
/*  867 */       stats.folderHasCrc = readAllOrBits(header, stats.numberOfFolders);
/*  868 */       int crcsDefined = stats.folderHasCrc.cardinality();
/*  869 */       if (skipBytesFully(header, (4 * crcsDefined)) < (4 * crcsDefined)) {
/*  870 */         throw new IOException("invalid number of CRCs in UnpackInfo");
/*      */       }
/*  872 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */     
/*  875 */     if (nid != 0) {
/*  876 */       throw new IOException("Badly terminated UnpackInfo");
/*      */     }
/*      */   }
/*      */   
/*      */   private void readUnpackInfo(ByteBuffer header, Archive archive) throws IOException {
/*  881 */     int nid = getUnsignedByte(header);
/*  882 */     int numFoldersInt = (int)readUint64(header);
/*  883 */     Folder[] folders = new Folder[numFoldersInt];
/*  884 */     archive.folders = folders;
/*  885 */     getUnsignedByte(header);
/*  886 */     for (int i = 0; i < numFoldersInt; i++) {
/*  887 */       folders[i] = readFolder(header);
/*      */     }
/*      */     
/*  890 */     nid = getUnsignedByte(header);
/*  891 */     for (Folder folder : folders) {
/*  892 */       assertFitsIntoNonNegativeInt("totalOutputStreams", folder.totalOutputStreams);
/*  893 */       folder.unpackSizes = new long[(int)folder.totalOutputStreams];
/*  894 */       for (int j = 0; j < folder.totalOutputStreams; j++) {
/*  895 */         folder.unpackSizes[j] = readUint64(header);
/*      */       }
/*      */     } 
/*      */     
/*  899 */     nid = getUnsignedByte(header);
/*  900 */     if (nid == 10) {
/*  901 */       BitSet crcsDefined = readAllOrBits(header, numFoldersInt);
/*  902 */       for (int j = 0; j < numFoldersInt; j++) {
/*  903 */         if (crcsDefined.get(j)) {
/*  904 */           (folders[j]).hasCrc = true;
/*  905 */           (folders[j]).crc = 0xFFFFFFFFL & getInt(header);
/*      */         } else {
/*  907 */           (folders[j]).hasCrc = false;
/*      */         } 
/*      */       } 
/*      */       
/*  911 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void sanityCheckSubStreamsInfo(ByteBuffer header, ArchiveStatistics stats) throws IOException {
/*  917 */     int nid = getUnsignedByte(header);
/*  918 */     List<Integer> numUnpackSubStreamsPerFolder = new LinkedList<>();
/*  919 */     if (nid == 13) {
/*  920 */       for (int i = 0; i < stats.numberOfFolders; i++) {
/*  921 */         numUnpackSubStreamsPerFolder.add(Integer.valueOf(assertFitsIntoNonNegativeInt("numStreams", readUint64(header))));
/*      */       }
/*  923 */       stats.numberOfUnpackSubStreams = numUnpackSubStreamsPerFolder.stream().mapToLong(Integer::longValue).sum();
/*  924 */       nid = getUnsignedByte(header);
/*      */     } else {
/*  926 */       stats.numberOfUnpackSubStreams = stats.numberOfFolders;
/*      */     } 
/*      */     
/*  929 */     assertFitsIntoNonNegativeInt("totalUnpackStreams", stats.numberOfUnpackSubStreams);
/*      */     
/*  931 */     if (nid == 9) {
/*  932 */       for (Iterator<Integer> iterator = numUnpackSubStreamsPerFolder.iterator(); iterator.hasNext(); ) { int numUnpackSubStreams = ((Integer)iterator.next()).intValue();
/*  933 */         if (numUnpackSubStreams == 0) {
/*      */           continue;
/*      */         }
/*  936 */         for (int i = 0; i < numUnpackSubStreams - 1; i++) {
/*  937 */           long size = readUint64(header);
/*  938 */           if (size < 0L) {
/*  939 */             throw new IOException("negative unpackSize");
/*      */           }
/*      */         }  }
/*      */       
/*  943 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */     
/*  946 */     int numDigests = 0;
/*  947 */     if (numUnpackSubStreamsPerFolder.isEmpty()) {
/*      */       
/*  949 */       numDigests = (stats.folderHasCrc == null) ? stats.numberOfFolders : (stats.numberOfFolders - stats.folderHasCrc.cardinality());
/*      */     } else {
/*  951 */       int folderIdx = 0;
/*  952 */       for (Iterator<Integer> iterator = numUnpackSubStreamsPerFolder.iterator(); iterator.hasNext(); ) { int numUnpackSubStreams = ((Integer)iterator.next()).intValue();
/*  953 */         if (numUnpackSubStreams != 1 || stats.folderHasCrc == null || 
/*  954 */           !stats.folderHasCrc.get(folderIdx++)) {
/*  955 */           numDigests += numUnpackSubStreams;
/*      */         } }
/*      */     
/*      */     } 
/*      */     
/*  960 */     if (nid == 10) {
/*  961 */       assertFitsIntoNonNegativeInt("numDigests", numDigests);
/*      */       
/*  963 */       int missingCrcs = readAllOrBits(header, numDigests).cardinality();
/*  964 */       if (skipBytesFully(header, (4 * missingCrcs)) < (4 * missingCrcs)) {
/*  965 */         throw new IOException("invalid number of missing CRCs in SubStreamInfo");
/*      */       }
/*  967 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */     
/*  970 */     if (nid != 0) {
/*  971 */       throw new IOException("Badly terminated SubStreamsInfo");
/*      */     }
/*      */   }
/*      */   
/*      */   private void readSubStreamsInfo(ByteBuffer header, Archive archive) throws IOException {
/*  976 */     for (Folder folder : archive.folders) {
/*  977 */       folder.numUnpackSubStreams = 1;
/*      */     }
/*  979 */     long unpackStreamsCount = archive.folders.length;
/*      */     
/*  981 */     int nid = getUnsignedByte(header);
/*  982 */     if (nid == 13) {
/*  983 */       unpackStreamsCount = 0L;
/*  984 */       for (Folder folder : archive.folders) {
/*  985 */         long numStreams = readUint64(header);
/*  986 */         folder.numUnpackSubStreams = (int)numStreams;
/*  987 */         unpackStreamsCount += numStreams;
/*      */       } 
/*  989 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */     
/*  992 */     int totalUnpackStreams = (int)unpackStreamsCount;
/*  993 */     SubStreamsInfo subStreamsInfo = new SubStreamsInfo();
/*  994 */     subStreamsInfo.unpackSizes = new long[totalUnpackStreams];
/*  995 */     subStreamsInfo.hasCrc = new BitSet(totalUnpackStreams);
/*  996 */     subStreamsInfo.crcs = new long[totalUnpackStreams];
/*      */     
/*  998 */     int nextUnpackStream = 0;
/*  999 */     for (Folder folder : archive.folders) {
/* 1000 */       if (folder.numUnpackSubStreams != 0) {
/*      */ 
/*      */         
/* 1003 */         long sum = 0L;
/* 1004 */         if (nid == 9) {
/* 1005 */           for (int i = 0; i < folder.numUnpackSubStreams - 1; i++) {
/* 1006 */             long size = readUint64(header);
/* 1007 */             subStreamsInfo.unpackSizes[nextUnpackStream++] = size;
/* 1008 */             sum += size;
/*      */           } 
/*      */         }
/* 1011 */         if (sum > folder.getUnpackSize()) {
/* 1012 */           throw new IOException("sum of unpack sizes of folder exceeds total unpack size");
/*      */         }
/* 1014 */         subStreamsInfo.unpackSizes[nextUnpackStream++] = folder.getUnpackSize() - sum;
/*      */       } 
/* 1016 */     }  if (nid == 9) {
/* 1017 */       nid = getUnsignedByte(header);
/*      */     }
/*      */     
/* 1020 */     int numDigests = 0;
/* 1021 */     for (Folder folder : archive.folders) {
/* 1022 */       if (folder.numUnpackSubStreams != 1 || !folder.hasCrc) {
/* 1023 */         numDigests += folder.numUnpackSubStreams;
/*      */       }
/*      */     } 
/*      */     
/* 1027 */     if (nid == 10) {
/* 1028 */       BitSet hasMissingCrc = readAllOrBits(header, numDigests);
/* 1029 */       long[] missingCrcs = new long[numDigests];
/* 1030 */       for (int i = 0; i < numDigests; i++) {
/* 1031 */         if (hasMissingCrc.get(i)) {
/* 1032 */           missingCrcs[i] = 0xFFFFFFFFL & getInt(header);
/*      */         }
/*      */       } 
/* 1035 */       int nextCrc = 0;
/* 1036 */       int nextMissingCrc = 0;
/* 1037 */       for (Folder folder : archive.folders) {
/* 1038 */         if (folder.numUnpackSubStreams == 1 && folder.hasCrc) {
/* 1039 */           subStreamsInfo.hasCrc.set(nextCrc, true);
/* 1040 */           subStreamsInfo.crcs[nextCrc] = folder.crc;
/* 1041 */           nextCrc++;
/*      */         } else {
/* 1043 */           for (int j = 0; j < folder.numUnpackSubStreams; j++) {
/* 1044 */             subStreamsInfo.hasCrc.set(nextCrc, hasMissingCrc.get(nextMissingCrc));
/* 1045 */             subStreamsInfo.crcs[nextCrc] = missingCrcs[nextMissingCrc];
/* 1046 */             nextCrc++;
/* 1047 */             nextMissingCrc++;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1052 */       nid = getUnsignedByte(header);
/*      */     } 
/*      */     
/* 1055 */     archive.subStreamsInfo = subStreamsInfo;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int sanityCheckFolder(ByteBuffer header, ArchiveStatistics stats) throws IOException {
/* 1061 */     int numCoders = assertFitsIntoNonNegativeInt("numCoders", readUint64(header));
/* 1062 */     if (numCoders == 0) {
/* 1063 */       throw new IOException("Folder without coders");
/*      */     }
/* 1065 */     stats.numberOfCoders = stats.numberOfCoders + numCoders;
/*      */     
/* 1067 */     long totalOutStreams = 0L;
/* 1068 */     long totalInStreams = 0L;
/* 1069 */     for (int i = 0; i < numCoders; i++) {
/* 1070 */       int bits = getUnsignedByte(header);
/* 1071 */       int idSize = bits & 0xF;
/* 1072 */       get(header, new byte[idSize]);
/*      */       
/* 1074 */       boolean isSimple = ((bits & 0x10) == 0);
/* 1075 */       boolean hasAttributes = ((bits & 0x20) != 0);
/* 1076 */       boolean moreAlternativeMethods = ((bits & 0x80) != 0);
/* 1077 */       if (moreAlternativeMethods) {
/* 1078 */         throw new IOException("Alternative methods are unsupported, please report. The reference implementation doesn't support them either.");
/*      */       }
/*      */ 
/*      */       
/* 1082 */       if (isSimple) {
/* 1083 */         totalInStreams++;
/* 1084 */         totalOutStreams++;
/*      */       } else {
/* 1086 */         totalInStreams += 
/* 1087 */           assertFitsIntoNonNegativeInt("numInStreams", readUint64(header));
/* 1088 */         totalOutStreams += 
/* 1089 */           assertFitsIntoNonNegativeInt("numOutStreams", readUint64(header));
/*      */       } 
/*      */       
/* 1092 */       if (hasAttributes) {
/*      */         
/* 1094 */         int propertiesSize = assertFitsIntoNonNegativeInt("propertiesSize", readUint64(header));
/* 1095 */         if (skipBytesFully(header, propertiesSize) < propertiesSize) {
/* 1096 */           throw new IOException("invalid propertiesSize in folder");
/*      */         }
/*      */       } 
/*      */     } 
/* 1100 */     assertFitsIntoNonNegativeInt("totalInStreams", totalInStreams);
/* 1101 */     assertFitsIntoNonNegativeInt("totalOutStreams", totalOutStreams);
/* 1102 */     stats.numberOfOutStreams = stats.numberOfOutStreams + totalOutStreams;
/* 1103 */     stats.numberOfInStreams = stats.numberOfInStreams + totalInStreams;
/*      */     
/* 1105 */     if (totalOutStreams == 0L) {
/* 1106 */       throw new IOException("Total output streams can't be 0");
/*      */     }
/*      */ 
/*      */     
/* 1110 */     int numBindPairs = assertFitsIntoNonNegativeInt("numBindPairs", totalOutStreams - 1L);
/* 1111 */     if (totalInStreams < numBindPairs) {
/* 1112 */       throw new IOException("Total input streams can't be less than the number of bind pairs");
/*      */     }
/* 1114 */     BitSet inStreamsBound = new BitSet((int)totalInStreams);
/* 1115 */     for (int j = 0; j < numBindPairs; j++) {
/* 1116 */       int inIndex = assertFitsIntoNonNegativeInt("inIndex", readUint64(header));
/* 1117 */       if (totalInStreams <= inIndex) {
/* 1118 */         throw new IOException("inIndex is bigger than number of inStreams");
/*      */       }
/* 1120 */       inStreamsBound.set(inIndex);
/* 1121 */       int outIndex = assertFitsIntoNonNegativeInt("outIndex", readUint64(header));
/* 1122 */       if (totalOutStreams <= outIndex) {
/* 1123 */         throw new IOException("outIndex is bigger than number of outStreams");
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1128 */     int numPackedStreams = assertFitsIntoNonNegativeInt("numPackedStreams", totalInStreams - numBindPairs);
/*      */     
/* 1130 */     if (numPackedStreams == 1) {
/* 1131 */       if (inStreamsBound.nextClearBit(0) == -1) {
/* 1132 */         throw new IOException("Couldn't find stream's bind pair index");
/*      */       }
/*      */     } else {
/* 1135 */       for (int k = 0; k < numPackedStreams; k++) {
/*      */         
/* 1137 */         int packedStreamIndex = assertFitsIntoNonNegativeInt("packedStreamIndex", readUint64(header));
/* 1138 */         if (packedStreamIndex >= totalInStreams) {
/* 1139 */           throw new IOException("packedStreamIndex is bigger than number of totalInStreams");
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1144 */     return (int)totalOutStreams;
/*      */   }
/*      */   
/*      */   private Folder readFolder(ByteBuffer header) throws IOException {
/* 1148 */     Folder folder = new Folder();
/*      */     
/* 1150 */     long numCoders = readUint64(header);
/* 1151 */     Coder[] coders = new Coder[(int)numCoders];
/* 1152 */     long totalInStreams = 0L;
/* 1153 */     long totalOutStreams = 0L;
/* 1154 */     for (int i = 0; i < coders.length; i++) {
/* 1155 */       coders[i] = new Coder();
/* 1156 */       int bits = getUnsignedByte(header);
/* 1157 */       int idSize = bits & 0xF;
/* 1158 */       boolean isSimple = ((bits & 0x10) == 0);
/* 1159 */       boolean hasAttributes = ((bits & 0x20) != 0);
/* 1160 */       boolean moreAlternativeMethods = ((bits & 0x80) != 0);
/*      */       
/* 1162 */       (coders[i]).decompressionMethodId = new byte[idSize];
/* 1163 */       get(header, (coders[i]).decompressionMethodId);
/* 1164 */       if (isSimple) {
/* 1165 */         (coders[i]).numInStreams = 1L;
/* 1166 */         (coders[i]).numOutStreams = 1L;
/*      */       } else {
/* 1168 */         (coders[i]).numInStreams = readUint64(header);
/* 1169 */         (coders[i]).numOutStreams = readUint64(header);
/*      */       } 
/* 1171 */       totalInStreams += (coders[i]).numInStreams;
/* 1172 */       totalOutStreams += (coders[i]).numOutStreams;
/* 1173 */       if (hasAttributes) {
/* 1174 */         long propertiesSize = readUint64(header);
/* 1175 */         (coders[i]).properties = new byte[(int)propertiesSize];
/* 1176 */         get(header, (coders[i]).properties);
/*      */       } 
/*      */       
/* 1179 */       if (moreAlternativeMethods) {
/* 1180 */         throw new IOException("Alternative methods are unsupported, please report. The reference implementation doesn't support them either.");
/*      */       }
/*      */     } 
/*      */     
/* 1184 */     folder.coders = coders;
/* 1185 */     folder.totalInputStreams = totalInStreams;
/* 1186 */     folder.totalOutputStreams = totalOutStreams;
/*      */     
/* 1188 */     long numBindPairs = totalOutStreams - 1L;
/* 1189 */     BindPair[] bindPairs = new BindPair[(int)numBindPairs];
/* 1190 */     for (int j = 0; j < bindPairs.length; j++) {
/* 1191 */       bindPairs[j] = new BindPair();
/* 1192 */       (bindPairs[j]).inIndex = readUint64(header);
/* 1193 */       (bindPairs[j]).outIndex = readUint64(header);
/*      */     } 
/* 1195 */     folder.bindPairs = bindPairs;
/*      */     
/* 1197 */     long numPackedStreams = totalInStreams - numBindPairs;
/* 1198 */     long[] packedStreams = new long[(int)numPackedStreams];
/* 1199 */     if (numPackedStreams == 1L) {
/*      */       int k;
/* 1201 */       for (k = 0; k < (int)totalInStreams && 
/* 1202 */         folder.findBindPairForInStream(k) >= 0; k++);
/*      */ 
/*      */ 
/*      */       
/* 1206 */       packedStreams[0] = k;
/*      */     } else {
/* 1208 */       for (int k = 0; k < (int)numPackedStreams; k++) {
/* 1209 */         packedStreams[k] = readUint64(header);
/*      */       }
/*      */     } 
/* 1212 */     folder.packedStreams = packedStreams;
/*      */     
/* 1214 */     return folder;
/*      */   }
/*      */   private BitSet readAllOrBits(ByteBuffer header, int size) throws IOException {
/*      */     BitSet bits;
/* 1218 */     int areAllDefined = getUnsignedByte(header);
/*      */     
/* 1220 */     if (areAllDefined != 0) {
/* 1221 */       bits = new BitSet(size);
/* 1222 */       for (int i = 0; i < size; i++) {
/* 1223 */         bits.set(i, true);
/*      */       }
/*      */     } else {
/* 1226 */       bits = readBits(header, size);
/*      */     } 
/* 1228 */     return bits;
/*      */   }
/*      */   
/*      */   private BitSet readBits(ByteBuffer header, int size) throws IOException {
/* 1232 */     BitSet bits = new BitSet(size);
/* 1233 */     int mask = 0;
/* 1234 */     int cache = 0;
/* 1235 */     for (int i = 0; i < size; i++) {
/* 1236 */       if (mask == 0) {
/* 1237 */         mask = 128;
/* 1238 */         cache = getUnsignedByte(header);
/*      */       } 
/* 1240 */       bits.set(i, ((cache & mask) != 0));
/* 1241 */       mask >>>= 1;
/*      */     } 
/* 1243 */     return bits;
/*      */   }
/*      */   
/*      */   private void sanityCheckFilesInfo(ByteBuffer header, ArchiveStatistics stats) throws IOException {
/* 1247 */     stats.numberOfEntries = assertFitsIntoNonNegativeInt("numFiles", readUint64(header));
/*      */     
/* 1249 */     int emptyStreams = -1;
/*      */     while (true) {
/* 1251 */       int external, timesDefined, attributesDefined, namesLength, j, filesSeen, i, propertyType = getUnsignedByte(header);
/* 1252 */       if (propertyType == 0) {
/*      */         break;
/*      */       }
/* 1255 */       long size = readUint64(header);
/* 1256 */       switch (propertyType) {
/*      */         case 14:
/* 1258 */           emptyStreams = readBits(header, stats.numberOfEntries).cardinality();
/*      */           continue;
/*      */         
/*      */         case 15:
/* 1262 */           if (emptyStreams == -1) {
/* 1263 */             throw new IOException("Header format error: kEmptyStream must appear before kEmptyFile");
/*      */           }
/* 1265 */           readBits(header, emptyStreams);
/*      */           continue;
/*      */         
/*      */         case 16:
/* 1269 */           if (emptyStreams == -1) {
/* 1270 */             throw new IOException("Header format error: kEmptyStream must appear before kAnti");
/*      */           }
/* 1272 */           readBits(header, emptyStreams);
/*      */           continue;
/*      */         
/*      */         case 17:
/* 1276 */           external = getUnsignedByte(header);
/* 1277 */           if (external != 0) {
/* 1278 */             throw new IOException("Not implemented");
/*      */           }
/*      */           
/* 1281 */           namesLength = assertFitsIntoNonNegativeInt("file names length", size - 1L);
/* 1282 */           if ((namesLength & 0x1) != 0) {
/* 1283 */             throw new IOException("File names length invalid");
/*      */           }
/*      */           
/* 1286 */           filesSeen = 0;
/* 1287 */           for (i = 0; i < namesLength; i += 2) {
/* 1288 */             char c = getChar(header);
/* 1289 */             if (c == '\000') {
/* 1290 */               filesSeen++;
/*      */             }
/*      */           } 
/* 1293 */           if (filesSeen != stats.numberOfEntries) {
/* 1294 */             throw new IOException("Invalid number of file names (" + filesSeen + " instead of " + stats
/* 1295 */                 .numberOfEntries + ")");
/*      */           }
/*      */           continue;
/*      */ 
/*      */         
/*      */         case 18:
/* 1301 */           timesDefined = readAllOrBits(header, stats.numberOfEntries).cardinality();
/* 1302 */           j = getUnsignedByte(header);
/* 1303 */           if (j != 0) {
/* 1304 */             throw new IOException("Not implemented");
/*      */           }
/* 1306 */           if (skipBytesFully(header, (8 * timesDefined)) < (8 * timesDefined)) {
/* 1307 */             throw new IOException("invalid creation dates size");
/*      */           }
/*      */           continue;
/*      */ 
/*      */         
/*      */         case 19:
/* 1313 */           timesDefined = readAllOrBits(header, stats.numberOfEntries).cardinality();
/* 1314 */           j = getUnsignedByte(header);
/* 1315 */           if (j != 0) {
/* 1316 */             throw new IOException("Not implemented");
/*      */           }
/* 1318 */           if (skipBytesFully(header, (8 * timesDefined)) < (8 * timesDefined)) {
/* 1319 */             throw new IOException("invalid access dates size");
/*      */           }
/*      */           continue;
/*      */ 
/*      */         
/*      */         case 20:
/* 1325 */           timesDefined = readAllOrBits(header, stats.numberOfEntries).cardinality();
/* 1326 */           j = getUnsignedByte(header);
/* 1327 */           if (j != 0) {
/* 1328 */             throw new IOException("Not implemented");
/*      */           }
/* 1330 */           if (skipBytesFully(header, (8 * timesDefined)) < (8 * timesDefined)) {
/* 1331 */             throw new IOException("invalid modification dates size");
/*      */           }
/*      */           continue;
/*      */ 
/*      */         
/*      */         case 21:
/* 1337 */           attributesDefined = readAllOrBits(header, stats.numberOfEntries).cardinality();
/* 1338 */           j = getUnsignedByte(header);
/* 1339 */           if (j != 0) {
/* 1340 */             throw new IOException("Not implemented");
/*      */           }
/* 1342 */           if (skipBytesFully(header, (4 * attributesDefined)) < (4 * attributesDefined)) {
/* 1343 */             throw new IOException("invalid windows attributes size");
/*      */           }
/*      */           continue;
/*      */         
/*      */         case 24:
/* 1348 */           throw new IOException("kStartPos is unsupported, please report");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 25:
/* 1354 */           if (skipBytesFully(header, size) < size) {
/* 1355 */             throw new IOException("Incomplete kDummy property");
/*      */           }
/*      */           continue;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1362 */       if (skipBytesFully(header, size) < size) {
/* 1363 */         throw new IOException("Incomplete property of type " + propertyType);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1369 */     stats.numberOfEntriesWithStream = stats.numberOfEntries - Math.max(emptyStreams, 0);
/*      */   }
/*      */   
/*      */   private void readFilesInfo(ByteBuffer header, Archive archive) throws IOException {
/* 1373 */     int numFilesInt = (int)readUint64(header);
/* 1374 */     Map<Integer, SevenZArchiveEntry> fileMap = new LinkedHashMap<>();
/* 1375 */     BitSet isEmptyStream = null;
/* 1376 */     BitSet isEmptyFile = null;
/* 1377 */     BitSet isAnti = null; while (true) {
/*      */       byte[] names; BitSet timesDefined, attributesDefined;
/* 1379 */       int namesLength, j, nextFile, nextName, k, propertyType = getUnsignedByte(header);
/* 1380 */       if (propertyType == 0) {
/*      */         break;
/*      */       }
/* 1383 */       long size = readUint64(header);
/* 1384 */       switch (propertyType) {
/*      */         case 14:
/* 1386 */           isEmptyStream = readBits(header, numFilesInt);
/*      */           continue;
/*      */         
/*      */         case 15:
/* 1390 */           isEmptyFile = readBits(header, isEmptyStream.cardinality());
/*      */           continue;
/*      */         
/*      */         case 16:
/* 1394 */           isAnti = readBits(header, isEmptyStream.cardinality());
/*      */           continue;
/*      */         
/*      */         case 17:
/* 1398 */           getUnsignedByte(header);
/* 1399 */           names = new byte[(int)(size - 1L)];
/* 1400 */           namesLength = names.length;
/* 1401 */           get(header, names);
/* 1402 */           nextFile = 0;
/* 1403 */           nextName = 0;
/* 1404 */           for (k = 0; k < namesLength; k += 2) {
/* 1405 */             if (names[k] == 0 && names[k + 1] == 0) {
/* 1406 */               checkEntryIsInitialized(fileMap, nextFile);
/* 1407 */               ((SevenZArchiveEntry)fileMap.get(Integer.valueOf(nextFile))).setName(new String(names, nextName, k - nextName, StandardCharsets.UTF_16LE));
/* 1408 */               nextName = k + 2;
/* 1409 */               nextFile++;
/*      */             } 
/*      */           } 
/* 1412 */           if (nextName != namesLength || nextFile != numFilesInt) {
/* 1413 */             throw new IOException("Error parsing file names");
/*      */           }
/*      */           continue;
/*      */         
/*      */         case 18:
/* 1418 */           timesDefined = readAllOrBits(header, numFilesInt);
/* 1419 */           getUnsignedByte(header);
/* 1420 */           for (j = 0; j < numFilesInt; j++) {
/* 1421 */             checkEntryIsInitialized(fileMap, j);
/* 1422 */             SevenZArchiveEntry entryAtIndex = fileMap.get(Integer.valueOf(j));
/* 1423 */             entryAtIndex.setHasCreationDate(timesDefined.get(j));
/* 1424 */             if (entryAtIndex.getHasCreationDate()) {
/* 1425 */               entryAtIndex.setCreationDate(getLong(header));
/*      */             }
/*      */           } 
/*      */           continue;
/*      */         
/*      */         case 19:
/* 1431 */           timesDefined = readAllOrBits(header, numFilesInt);
/* 1432 */           getUnsignedByte(header);
/* 1433 */           for (j = 0; j < numFilesInt; j++) {
/* 1434 */             checkEntryIsInitialized(fileMap, j);
/* 1435 */             SevenZArchiveEntry entryAtIndex = fileMap.get(Integer.valueOf(j));
/* 1436 */             entryAtIndex.setHasAccessDate(timesDefined.get(j));
/* 1437 */             if (entryAtIndex.getHasAccessDate()) {
/* 1438 */               entryAtIndex.setAccessDate(getLong(header));
/*      */             }
/*      */           } 
/*      */           continue;
/*      */         
/*      */         case 20:
/* 1444 */           timesDefined = readAllOrBits(header, numFilesInt);
/* 1445 */           getUnsignedByte(header);
/* 1446 */           for (j = 0; j < numFilesInt; j++) {
/* 1447 */             checkEntryIsInitialized(fileMap, j);
/* 1448 */             SevenZArchiveEntry entryAtIndex = fileMap.get(Integer.valueOf(j));
/* 1449 */             entryAtIndex.setHasLastModifiedDate(timesDefined.get(j));
/* 1450 */             if (entryAtIndex.getHasLastModifiedDate()) {
/* 1451 */               entryAtIndex.setLastModifiedDate(getLong(header));
/*      */             }
/*      */           } 
/*      */           continue;
/*      */         
/*      */         case 21:
/* 1457 */           attributesDefined = readAllOrBits(header, numFilesInt);
/* 1458 */           getUnsignedByte(header);
/* 1459 */           for (j = 0; j < numFilesInt; j++) {
/* 1460 */             checkEntryIsInitialized(fileMap, j);
/* 1461 */             SevenZArchiveEntry entryAtIndex = fileMap.get(Integer.valueOf(j));
/* 1462 */             entryAtIndex.setHasWindowsAttributes(attributesDefined.get(j));
/* 1463 */             if (entryAtIndex.getHasWindowsAttributes()) {
/* 1464 */               entryAtIndex.setWindowsAttributes(getInt(header));
/*      */             }
/*      */           } 
/*      */           continue;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 25:
/* 1473 */           skipBytesFully(header, size);
/*      */           continue;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1479 */       skipBytesFully(header, size);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1484 */     int nonEmptyFileCounter = 0;
/* 1485 */     int emptyFileCounter = 0;
/* 1486 */     for (int i = 0; i < numFilesInt; i++) {
/* 1487 */       SevenZArchiveEntry entryAtIndex = fileMap.get(Integer.valueOf(i));
/* 1488 */       if (entryAtIndex != null) {
/*      */ 
/*      */         
/* 1491 */         entryAtIndex.setHasStream((isEmptyStream == null || !isEmptyStream.get(i)));
/* 1492 */         if (entryAtIndex.hasStream()) {
/* 1493 */           if (archive.subStreamsInfo == null) {
/* 1494 */             throw new IOException("Archive contains file with streams but no subStreamsInfo");
/*      */           }
/* 1496 */           entryAtIndex.setDirectory(false);
/* 1497 */           entryAtIndex.setAntiItem(false);
/* 1498 */           entryAtIndex.setHasCrc(archive.subStreamsInfo.hasCrc.get(nonEmptyFileCounter));
/* 1499 */           entryAtIndex.setCrcValue(archive.subStreamsInfo.crcs[nonEmptyFileCounter]);
/* 1500 */           entryAtIndex.setSize(archive.subStreamsInfo.unpackSizes[nonEmptyFileCounter]);
/* 1501 */           if (entryAtIndex.getSize() < 0L) {
/* 1502 */             throw new IOException("broken archive, entry with negative size");
/*      */           }
/* 1504 */           nonEmptyFileCounter++;
/*      */         } else {
/* 1506 */           entryAtIndex.setDirectory((isEmptyFile == null || !isEmptyFile.get(emptyFileCounter)));
/* 1507 */           entryAtIndex.setAntiItem((isAnti != null && isAnti.get(emptyFileCounter)));
/* 1508 */           entryAtIndex.setHasCrc(false);
/* 1509 */           entryAtIndex.setSize(0L);
/* 1510 */           emptyFileCounter++;
/*      */         } 
/*      */       } 
/* 1513 */     }  archive.files = (SevenZArchiveEntry[])fileMap.values().stream().filter(Objects::nonNull).toArray(x$0 -> new SevenZArchiveEntry[x$0]);
/* 1514 */     calculateStreamMap(archive);
/*      */   }
/*      */   
/*      */   private void checkEntryIsInitialized(Map<Integer, SevenZArchiveEntry> archiveEntries, int index) {
/* 1518 */     if (archiveEntries.get(Integer.valueOf(index)) == null) {
/* 1519 */       archiveEntries.put(Integer.valueOf(index), new SevenZArchiveEntry());
/*      */     }
/*      */   }
/*      */   
/*      */   private void calculateStreamMap(Archive archive) throws IOException {
/* 1524 */     StreamMap streamMap = new StreamMap();
/*      */     
/* 1526 */     int nextFolderPackStreamIndex = 0;
/* 1527 */     int numFolders = (archive.folders != null) ? archive.folders.length : 0;
/* 1528 */     streamMap.folderFirstPackStreamIndex = new int[numFolders];
/* 1529 */     for (int i = 0; i < numFolders; i++) {
/* 1530 */       streamMap.folderFirstPackStreamIndex[i] = nextFolderPackStreamIndex;
/* 1531 */       nextFolderPackStreamIndex += (archive.folders[i]).packedStreams.length;
/*      */     } 
/*      */     
/* 1534 */     long nextPackStreamOffset = 0L;
/* 1535 */     int numPackSizes = archive.packSizes.length;
/* 1536 */     streamMap.packStreamOffsets = new long[numPackSizes];
/* 1537 */     for (int j = 0; j < numPackSizes; j++) {
/* 1538 */       streamMap.packStreamOffsets[j] = nextPackStreamOffset;
/* 1539 */       nextPackStreamOffset += archive.packSizes[j];
/*      */     } 
/*      */     
/* 1542 */     streamMap.folderFirstFileIndex = new int[numFolders];
/* 1543 */     streamMap.fileFolderIndex = new int[archive.files.length];
/* 1544 */     int nextFolderIndex = 0;
/* 1545 */     int nextFolderUnpackStreamIndex = 0;
/* 1546 */     for (int k = 0; k < archive.files.length; k++) {
/* 1547 */       if (!archive.files[k].hasStream() && nextFolderUnpackStreamIndex == 0) {
/* 1548 */         streamMap.fileFolderIndex[k] = -1;
/*      */       } else {
/*      */         
/* 1551 */         if (nextFolderUnpackStreamIndex == 0) {
/* 1552 */           for (; nextFolderIndex < archive.folders.length; nextFolderIndex++) {
/* 1553 */             streamMap.folderFirstFileIndex[nextFolderIndex] = k;
/* 1554 */             if ((archive.folders[nextFolderIndex]).numUnpackSubStreams > 0) {
/*      */               break;
/*      */             }
/*      */           } 
/* 1558 */           if (nextFolderIndex >= archive.folders.length) {
/* 1559 */             throw new IOException("Too few folders in archive");
/*      */           }
/*      */         } 
/* 1562 */         streamMap.fileFolderIndex[k] = nextFolderIndex;
/* 1563 */         if (archive.files[k].hasStream()) {
/*      */ 
/*      */           
/* 1566 */           nextFolderUnpackStreamIndex++;
/* 1567 */           if (nextFolderUnpackStreamIndex >= (archive.folders[nextFolderIndex]).numUnpackSubStreams) {
/* 1568 */             nextFolderIndex++;
/* 1569 */             nextFolderUnpackStreamIndex = 0;
/*      */           } 
/*      */         } 
/*      */       } 
/* 1573 */     }  archive.streamMap = streamMap;
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
/*      */   private void buildDecodingStream(int entryIndex, boolean isRandomAccess) throws IOException {
/*      */     CRC32VerifyingInputStream cRC32VerifyingInputStream;
/* 1589 */     if (this.archive.streamMap == null) {
/* 1590 */       throw new IOException("Archive doesn't contain stream information to read entries");
/*      */     }
/* 1592 */     int folderIndex = this.archive.streamMap.fileFolderIndex[entryIndex];
/* 1593 */     if (folderIndex < 0) {
/* 1594 */       this.deferredBlockStreams.clear();
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1599 */     SevenZArchiveEntry file = this.archive.files[entryIndex];
/* 1600 */     boolean isInSameFolder = false;
/* 1601 */     if (this.currentFolderIndex == folderIndex) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1607 */       if (entryIndex > 0) {
/* 1608 */         file.setContentMethods(this.archive.files[entryIndex - 1].getContentMethods());
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1614 */       if (isRandomAccess && file.getContentMethods() == null) {
/* 1615 */         int folderFirstFileIndex = this.archive.streamMap.folderFirstFileIndex[folderIndex];
/* 1616 */         SevenZArchiveEntry folderFirstFile = this.archive.files[folderFirstFileIndex];
/* 1617 */         file.setContentMethods(folderFirstFile.getContentMethods());
/*      */       } 
/* 1619 */       isInSameFolder = true;
/*      */     } else {
/* 1621 */       this.currentFolderIndex = folderIndex;
/*      */       
/* 1623 */       reopenFolderInputStream(folderIndex, file);
/*      */     } 
/*      */     
/* 1626 */     boolean haveSkippedEntries = false;
/* 1627 */     if (isRandomAccess)
/*      */     {
/* 1629 */       haveSkippedEntries = skipEntriesWhenNeeded(entryIndex, isInSameFolder, folderIndex);
/*      */     }
/*      */     
/* 1632 */     if (isRandomAccess && this.currentEntryIndex == entryIndex && !haveSkippedEntries) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1641 */     BoundedInputStream boundedInputStream = new BoundedInputStream(this.currentFolderInputStream, file.getSize());
/* 1642 */     if (file.getHasCrc()) {
/* 1643 */       cRC32VerifyingInputStream = new CRC32VerifyingInputStream((InputStream)boundedInputStream, file.getSize(), file.getCrcValue());
/*      */     }
/*      */     
/* 1646 */     this.deferredBlockStreams.add(cRC32VerifyingInputStream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void reopenFolderInputStream(int folderIndex, SevenZArchiveEntry file) throws IOException {
/* 1657 */     this.deferredBlockStreams.clear();
/* 1658 */     if (this.currentFolderInputStream != null) {
/* 1659 */       this.currentFolderInputStream.close();
/* 1660 */       this.currentFolderInputStream = null;
/*      */     } 
/* 1662 */     Folder folder = this.archive.folders[folderIndex];
/* 1663 */     int firstPackStreamIndex = this.archive.streamMap.folderFirstPackStreamIndex[folderIndex];
/* 1664 */     long folderOffset = 32L + this.archive.packPos + this.archive.streamMap.packStreamOffsets[firstPackStreamIndex];
/*      */ 
/*      */     
/* 1667 */     this.currentFolderInputStream = buildDecoderStack(folder, folderOffset, firstPackStreamIndex, file);
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
/*      */   private boolean skipEntriesWhenNeeded(int entryIndex, boolean isInSameFolder, int folderIndex) throws IOException {
/* 1694 */     SevenZArchiveEntry file = this.archive.files[entryIndex];
/*      */ 
/*      */     
/* 1697 */     if (this.currentEntryIndex == entryIndex && !hasCurrentEntryBeenRead()) {
/* 1698 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1706 */     int filesToSkipStartIndex = this.archive.streamMap.folderFirstFileIndex[this.currentFolderIndex];
/* 1707 */     if (isInSameFolder) {
/* 1708 */       if (this.currentEntryIndex < entryIndex) {
/*      */         
/* 1710 */         filesToSkipStartIndex = this.currentEntryIndex + 1;
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1715 */         reopenFolderInputStream(folderIndex, file);
/*      */       } 
/*      */     }
/*      */     
/* 1719 */     for (int i = filesToSkipStartIndex; i < entryIndex; i++) {
/* 1720 */       CRC32VerifyingInputStream cRC32VerifyingInputStream; SevenZArchiveEntry fileToSkip = this.archive.files[i];
/* 1721 */       BoundedInputStream boundedInputStream = new BoundedInputStream(this.currentFolderInputStream, fileToSkip.getSize());
/* 1722 */       if (fileToSkip.getHasCrc()) {
/* 1723 */         cRC32VerifyingInputStream = new CRC32VerifyingInputStream((InputStream)boundedInputStream, fileToSkip.getSize(), fileToSkip.getCrcValue());
/*      */       }
/* 1725 */       this.deferredBlockStreams.add(cRC32VerifyingInputStream);
/*      */ 
/*      */       
/* 1728 */       fileToSkip.setContentMethods(file.getContentMethods());
/*      */     } 
/* 1730 */     return true;
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
/*      */   private boolean hasCurrentEntryBeenRead() {
/* 1742 */     boolean hasCurrentEntryBeenRead = false;
/* 1743 */     if (!this.deferredBlockStreams.isEmpty()) {
/* 1744 */       InputStream currentEntryInputStream = this.deferredBlockStreams.get(this.deferredBlockStreams.size() - 1);
/*      */ 
/*      */       
/* 1747 */       if (currentEntryInputStream instanceof CRC32VerifyingInputStream) {
/* 1748 */         hasCurrentEntryBeenRead = (((CRC32VerifyingInputStream)currentEntryInputStream).getBytesRemaining() != this.archive.files[this.currentEntryIndex].getSize());
/*      */       }
/*      */       
/* 1751 */       if (currentEntryInputStream instanceof BoundedInputStream) {
/* 1752 */         hasCurrentEntryBeenRead = (((BoundedInputStream)currentEntryInputStream).getBytesRemaining() != this.archive.files[this.currentEntryIndex].getSize());
/*      */       }
/*      */     } 
/* 1755 */     return hasCurrentEntryBeenRead;
/*      */   }
/*      */ 
/*      */   
/*      */   private InputStream buildDecoderStack(Folder folder, long folderOffset, int firstPackStreamIndex, SevenZArchiveEntry entry) throws IOException {
/* 1760 */     this.channel.position(folderOffset);
/* 1761 */     InputStream inputStreamStack = new FilterInputStream(new BufferedInputStream(new BoundedSeekableByteChannelInputStream(this.channel, this.archive.packSizes[firstPackStreamIndex])))
/*      */       {
/*      */         
/*      */         public int read() throws IOException
/*      */         {
/* 1766 */           int r = this.in.read();
/* 1767 */           if (r >= 0) {
/* 1768 */             count(1);
/*      */           }
/* 1770 */           return r;
/*      */         }
/*      */         
/*      */         public int read(byte[] b) throws IOException {
/* 1774 */           return read(b, 0, b.length);
/*      */         }
/*      */         
/*      */         public int read(byte[] b, int off, int len) throws IOException {
/* 1778 */           if (len == 0) {
/* 1779 */             return 0;
/*      */           }
/* 1781 */           int r = this.in.read(b, off, len);
/* 1782 */           if (r >= 0) {
/* 1783 */             count(r);
/*      */           }
/* 1785 */           return r;
/*      */         }
/*      */         private void count(int c) {
/* 1788 */           SevenZFile.this.compressedBytesReadFromCurrentEntry = SevenZFile.this.compressedBytesReadFromCurrentEntry + c;
/*      */         }
/*      */       };
/* 1791 */     LinkedList<SevenZMethodConfiguration> methods = new LinkedList<>();
/* 1792 */     for (Coder coder : folder.getOrderedCoders()) {
/* 1793 */       if (coder.numInStreams != 1L || coder.numOutStreams != 1L) {
/* 1794 */         throw new IOException("Multi input/output stream coders are not yet supported");
/*      */       }
/* 1796 */       SevenZMethod method = SevenZMethod.byId(coder.decompressionMethodId);
/* 1797 */       inputStreamStack = Coders.addDecoder(this.fileName, inputStreamStack, folder
/* 1798 */           .getUnpackSizeForCoder(coder), coder, this.password, this.options.getMaxMemoryLimitInKb());
/* 1799 */       methods.addFirst(new SevenZMethodConfiguration(method, 
/* 1800 */             Coders.findByMethod(method).getOptionsFromCoder(coder, inputStreamStack)));
/*      */     } 
/* 1802 */     entry.setContentMethods(methods);
/* 1803 */     if (folder.hasCrc) {
/* 1804 */       return (InputStream)new CRC32VerifyingInputStream(inputStreamStack, folder
/* 1805 */           .getUnpackSize(), folder.crc);
/*      */     }
/* 1807 */     return inputStreamStack;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int read() throws IOException {
/* 1818 */     int b = getCurrentStream().read();
/* 1819 */     if (b >= 0) {
/* 1820 */       this.uncompressedBytesReadFromCurrentEntry++;
/*      */     }
/* 1822 */     return b;
/*      */   }
/*      */   
/*      */   private InputStream getCurrentStream() throws IOException {
/* 1826 */     if (this.archive.files[this.currentEntryIndex].getSize() == 0L) {
/* 1827 */       return new ByteArrayInputStream(ByteUtils.EMPTY_BYTE_ARRAY);
/*      */     }
/* 1829 */     if (this.deferredBlockStreams.isEmpty()) {
/* 1830 */       throw new IllegalStateException("No current 7z entry (call getNextEntry() first).");
/*      */     }
/*      */     
/* 1833 */     while (this.deferredBlockStreams.size() > 1) {
/*      */ 
/*      */ 
/*      */       
/* 1837 */       try (InputStream stream = (InputStream)this.deferredBlockStreams.remove(0)) {
/* 1838 */         IOUtils.skip(stream, Long.MAX_VALUE);
/*      */       } 
/* 1840 */       this.compressedBytesReadFromCurrentEntry = 0L;
/*      */     } 
/*      */     
/* 1843 */     return this.deferredBlockStreams.get(0);
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
/*      */   public InputStream getInputStream(SevenZArchiveEntry entry) throws IOException {
/* 1859 */     int entryIndex = -1;
/* 1860 */     for (int i = 0; i < this.archive.files.length; i++) {
/* 1861 */       if (entry == this.archive.files[i]) {
/* 1862 */         entryIndex = i;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/* 1867 */     if (entryIndex < 0) {
/* 1868 */       throw new IllegalArgumentException("Can not find " + entry.getName() + " in " + this.fileName);
/*      */     }
/*      */     
/* 1871 */     buildDecodingStream(entryIndex, true);
/* 1872 */     this.currentEntryIndex = entryIndex;
/* 1873 */     this.currentFolderIndex = this.archive.streamMap.fileFolderIndex[entryIndex];
/* 1874 */     return getCurrentStream();
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
/*      */   public int read(byte[] b) throws IOException {
/* 1886 */     return read(b, 0, b.length);
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
/*      */   public int read(byte[] b, int off, int len) throws IOException {
/* 1900 */     if (len == 0) {
/* 1901 */       return 0;
/*      */     }
/* 1903 */     int cnt = getCurrentStream().read(b, off, len);
/* 1904 */     if (cnt > 0) {
/* 1905 */       this.uncompressedBytesReadFromCurrentEntry += cnt;
/*      */     }
/* 1907 */     return cnt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InputStreamStatistics getStatisticsForCurrentEntry() {
/* 1917 */     return new InputStreamStatistics()
/*      */       {
/*      */         public long getCompressedCount() {
/* 1920 */           return SevenZFile.this.compressedBytesReadFromCurrentEntry;
/*      */         }
/*      */         
/*      */         public long getUncompressedCount() {
/* 1924 */           return SevenZFile.this.uncompressedBytesReadFromCurrentEntry;
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   private static long readUint64(ByteBuffer in) throws IOException {
/* 1931 */     long firstByte = getUnsignedByte(in);
/* 1932 */     int mask = 128;
/* 1933 */     long value = 0L;
/* 1934 */     for (int i = 0; i < 8; i++) {
/* 1935 */       if ((firstByte & mask) == 0L) {
/* 1936 */         return value | (firstByte & (mask - 1)) << 8 * i;
/*      */       }
/* 1938 */       long nextByte = getUnsignedByte(in);
/* 1939 */       value |= nextByte << 8 * i;
/* 1940 */       mask >>>= 1;
/*      */     } 
/* 1942 */     return value;
/*      */   }
/*      */   
/*      */   private static char getChar(ByteBuffer buf) throws IOException {
/* 1946 */     if (buf.remaining() < 2) {
/* 1947 */       throw new EOFException();
/*      */     }
/* 1949 */     return buf.getChar();
/*      */   }
/*      */   
/*      */   private static int getInt(ByteBuffer buf) throws IOException {
/* 1953 */     if (buf.remaining() < 4) {
/* 1954 */       throw new EOFException();
/*      */     }
/* 1956 */     return buf.getInt();
/*      */   }
/*      */   
/*      */   private static long getLong(ByteBuffer buf) throws IOException {
/* 1960 */     if (buf.remaining() < 8) {
/* 1961 */       throw new EOFException();
/*      */     }
/* 1963 */     return buf.getLong();
/*      */   }
/*      */   
/*      */   private static void get(ByteBuffer buf, byte[] to) throws IOException {
/* 1967 */     if (buf.remaining() < to.length) {
/* 1968 */       throw new EOFException();
/*      */     }
/* 1970 */     buf.get(to);
/*      */   }
/*      */   
/*      */   private static int getUnsignedByte(ByteBuffer buf) throws IOException {
/* 1974 */     if (!buf.hasRemaining()) {
/* 1975 */       throw new EOFException();
/*      */     }
/* 1977 */     return buf.get() & 0xFF;
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
/*      */   public static boolean matches(byte[] signature, int length) {
/* 1991 */     if (length < sevenZSignature.length) {
/* 1992 */       return false;
/*      */     }
/*      */     
/* 1995 */     for (int i = 0; i < sevenZSignature.length; i++) {
/* 1996 */       if (signature[i] != sevenZSignature[i]) {
/* 1997 */         return false;
/*      */       }
/*      */     } 
/* 2000 */     return true;
/*      */   }
/*      */   
/*      */   private static long skipBytesFully(ByteBuffer input, long bytesToSkip) {
/* 2004 */     if (bytesToSkip < 1L) {
/* 2005 */       return 0L;
/*      */     }
/* 2007 */     int current = input.position();
/* 2008 */     int maxSkip = input.remaining();
/* 2009 */     if (maxSkip < bytesToSkip) {
/* 2010 */       bytesToSkip = maxSkip;
/*      */     }
/* 2012 */     input.position(current + (int)bytesToSkip);
/* 2013 */     return bytesToSkip;
/*      */   }
/*      */   
/*      */   private void readFully(ByteBuffer buf) throws IOException {
/* 2017 */     buf.rewind();
/* 2018 */     IOUtils.readFully(this.channel, buf);
/* 2019 */     buf.flip();
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 2024 */     return this.archive.toString();
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
/*      */   public String getDefaultName() {
/* 2045 */     if ("unknown archive".equals(this.fileName) || this.fileName == null) {
/* 2046 */       return null;
/*      */     }
/*      */     
/* 2049 */     String lastSegment = (new File(this.fileName)).getName();
/* 2050 */     int dotPos = lastSegment.lastIndexOf(".");
/* 2051 */     if (dotPos > 0) {
/* 2052 */       return lastSegment.substring(0, dotPos);
/*      */     }
/* 2054 */     return lastSegment + "~";
/*      */   }
/*      */   
/*      */   private static byte[] utf16Decode(char[] chars) {
/* 2058 */     if (chars == null) {
/* 2059 */       return null;
/*      */     }
/* 2061 */     ByteBuffer encoded = StandardCharsets.UTF_16LE.encode(CharBuffer.wrap(chars));
/* 2062 */     if (encoded.hasArray()) {
/* 2063 */       return encoded.array();
/*      */     }
/* 2065 */     byte[] e = new byte[encoded.remaining()];
/* 2066 */     encoded.get(e);
/* 2067 */     return e;
/*      */   }
/*      */   
/*      */   private static int assertFitsIntoNonNegativeInt(String what, long value) throws IOException {
/* 2071 */     if (value > 2147483647L || value < 0L) {
/* 2072 */       throw new IOException("Cannot handle " + what + " " + value);
/*      */     }
/* 2074 */     return (int)value;
/*      */   }
/*      */   
/*      */   private static class ArchiveStatistics { private int numberOfPackedStreams;
/*      */     private long numberOfCoders;
/*      */     private long numberOfOutStreams;
/*      */     private long numberOfInStreams;
/*      */     private long numberOfUnpackSubStreams;
/*      */     private int numberOfFolders;
/*      */     private BitSet folderHasCrc;
/*      */     private int numberOfEntries;
/*      */     private int numberOfEntriesWithStream;
/*      */     
/*      */     private ArchiveStatistics() {}
/*      */     
/*      */     public String toString() {
/* 2090 */       return "Archive with " + this.numberOfEntries + " entries in " + this.numberOfFolders + " folders. Estimated size " + (
/* 2091 */         estimateSize() / 1024L) + " kB.";
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
/*      */     long estimateSize() {
/* 2103 */       long lowerBound = 16L * this.numberOfPackedStreams + (this.numberOfPackedStreams / 8) + this.numberOfFolders * folderSize() + this.numberOfCoders * coderSize() + (this.numberOfOutStreams - this.numberOfFolders) * bindPairSize() + 8L * (this.numberOfInStreams - this.numberOfOutStreams + this.numberOfFolders) + 8L * this.numberOfOutStreams + this.numberOfEntries * entrySize() + streamMapSize();
/*      */       
/* 2105 */       return 2L * lowerBound;
/*      */     }
/*      */     
/*      */     void assertValidity(int maxMemoryLimitInKb) throws IOException {
/* 2109 */       if (this.numberOfEntriesWithStream > 0 && this.numberOfFolders == 0) {
/* 2110 */         throw new IOException("archive with entries but no folders");
/*      */       }
/* 2112 */       if (this.numberOfEntriesWithStream > this.numberOfUnpackSubStreams) {
/* 2113 */         throw new IOException("archive doesn't contain enough substreams for entries");
/*      */       }
/*      */       
/* 2116 */       long memoryNeededInKb = estimateSize() / 1024L;
/* 2117 */       if (maxMemoryLimitInKb < memoryNeededInKb) {
/* 2118 */         throw new MemoryLimitException(memoryNeededInKb, maxMemoryLimitInKb);
/*      */       }
/*      */     }
/*      */     
/*      */     private long folderSize() {
/* 2123 */       return 30L;
/*      */     }
/*      */     
/*      */     private long coderSize() {
/* 2127 */       return 22L;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private long bindPairSize() {
/* 2134 */       return 16L;
/*      */     }
/*      */     
/*      */     private long entrySize() {
/* 2138 */       return 100L;
/*      */     }
/*      */     
/*      */     private long streamMapSize() {
/* 2142 */       return (8 * this.numberOfFolders + 8 * this.numberOfPackedStreams + 4 * this.numberOfEntries);
/*      */     } }
/*      */ 
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\sevenz\SevenZFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */