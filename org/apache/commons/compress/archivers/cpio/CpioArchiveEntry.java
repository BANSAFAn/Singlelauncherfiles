/*      */ package org.apache.commons.compress.archivers.cpio;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.LinkOption;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.attribute.FileTime;
/*      */ import java.util.Date;
/*      */ import java.util.Objects;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import org.apache.commons.compress.archivers.ArchiveEntry;
/*      */ import org.apache.commons.compress.utils.ExactMath;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CpioArchiveEntry
/*      */   implements CpioConstants, ArchiveEntry
/*      */ {
/*      */   private final short fileFormat;
/*      */   private final int headerSize;
/*      */   private final int alignmentBoundary;
/*      */   private long chksum;
/*      */   private long filesize;
/*      */   private long gid;
/*      */   private long inode;
/*      */   private long maj;
/*      */   private long min;
/*      */   private long mode;
/*      */   private long mtime;
/*      */   private String name;
/*      */   private long nlink;
/*      */   private long rmaj;
/*      */   private long rmin;
/*      */   private long uid;
/*      */   
/*      */   public CpioArchiveEntry(short format) {
/*  214 */     switch (format) {
/*      */       case 1:
/*  216 */         this.headerSize = 110;
/*  217 */         this.alignmentBoundary = 4;
/*      */         break;
/*      */       case 2:
/*  220 */         this.headerSize = 110;
/*  221 */         this.alignmentBoundary = 4;
/*      */         break;
/*      */       case 4:
/*  224 */         this.headerSize = 76;
/*  225 */         this.alignmentBoundary = 0;
/*      */         break;
/*      */       case 8:
/*  228 */         this.headerSize = 26;
/*  229 */         this.alignmentBoundary = 2;
/*      */         break;
/*      */       default:
/*  232 */         throw new IllegalArgumentException("Unknown header type " + format);
/*      */     } 
/*  234 */     this.fileFormat = format;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CpioArchiveEntry(String name) {
/*  245 */     this((short)1, name);
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
/*      */   public CpioArchiveEntry(short format, String name) {
/*  267 */     this(format);
/*  268 */     this.name = name;
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
/*      */   public CpioArchiveEntry(String name, long size) {
/*  281 */     this(name);
/*  282 */     setSize(size);
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
/*      */   public CpioArchiveEntry(short format, String name, long size) {
/*  307 */     this(format, name);
/*  308 */     setSize(size);
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
/*      */   public CpioArchiveEntry(File inputFile, String entryName) {
/*  322 */     this((short)1, inputFile, entryName);
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
/*      */   public CpioArchiveEntry(Path inputPath, String entryName, LinkOption... options) throws IOException {
/*  339 */     this((short)1, inputPath, entryName, options);
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
/*      */   public CpioArchiveEntry(short format, File inputFile, String entryName) {
/*  365 */     this(format, entryName, inputFile.isFile() ? inputFile.length() : 0L);
/*  366 */     if (inputFile.isDirectory()) {
/*  367 */       setMode(16384L);
/*  368 */     } else if (inputFile.isFile()) {
/*  369 */       setMode(32768L);
/*      */     } else {
/*  371 */       throw new IllegalArgumentException("Cannot determine type of file " + inputFile
/*  372 */           .getName());
/*      */     } 
/*      */     
/*  375 */     setTime(inputFile.lastModified() / 1000L);
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
/*      */   public CpioArchiveEntry(short format, Path inputPath, String entryName, LinkOption... options) throws IOException {
/*  403 */     this(format, entryName, Files.isRegularFile(inputPath, options) ? Files.size(inputPath) : 0L);
/*  404 */     if (Files.isDirectory(inputPath, options)) {
/*  405 */       setMode(16384L);
/*  406 */     } else if (Files.isRegularFile(inputPath, options)) {
/*  407 */       setMode(32768L);
/*      */     } else {
/*  409 */       throw new IllegalArgumentException("Cannot determine type of file " + inputPath);
/*      */     } 
/*      */     
/*  412 */     setTime(Files.getLastModifiedTime(inputPath, options));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkNewFormat() {
/*  419 */     if ((this.fileFormat & 0x3) == 0) {
/*  420 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkOldFormat() {
/*  428 */     if ((this.fileFormat & 0xC) == 0) {
/*  429 */       throw new UnsupportedOperationException();
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
/*      */   public long getChksum() {
/*  441 */     checkNewFormat();
/*  442 */     return this.chksum & 0xFFFFFFFFL;
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
/*      */   public long getDevice() {
/*  454 */     checkOldFormat();
/*  455 */     return this.min;
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
/*      */   public long getDeviceMaj() {
/*  467 */     checkNewFormat();
/*  468 */     return this.maj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getDeviceMin() {
/*  478 */     checkNewFormat();
/*  479 */     return this.min;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getSize() {
/*  490 */     return this.filesize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getFormat() {
/*  499 */     return this.fileFormat;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getGID() {
/*  508 */     return this.gid;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getHeaderSize() {
/*  517 */     return this.headerSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAlignmentBoundary() {
/*  526 */     return this.alignmentBoundary;
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
/*      */   @Deprecated
/*      */   public int getHeaderPadCount() {
/*  539 */     return getHeaderPadCount((Charset)null);
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
/*      */   public int getHeaderPadCount(Charset charset) {
/*  551 */     if (this.name == null) {
/*  552 */       return 0;
/*      */     }
/*  554 */     if (charset == null) {
/*  555 */       return getHeaderPadCount(this.name.length());
/*      */     }
/*  557 */     return getHeaderPadCount((this.name.getBytes(charset)).length);
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
/*      */   public int getHeaderPadCount(long nameSize) {
/*  571 */     if (this.alignmentBoundary == 0) {
/*  572 */       return 0;
/*      */     }
/*  574 */     int size = this.headerSize + 1;
/*  575 */     if (this.name != null) {
/*  576 */       size = ExactMath.add(size, nameSize);
/*      */     }
/*  578 */     int remain = size % this.alignmentBoundary;
/*  579 */     if (remain > 0) {
/*  580 */       return this.alignmentBoundary - remain;
/*      */     }
/*  582 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDataPadCount() {
/*  591 */     if (this.alignmentBoundary == 0) {
/*  592 */       return 0;
/*      */     }
/*  594 */     long size = this.filesize;
/*  595 */     int remain = (int)(size % this.alignmentBoundary);
/*  596 */     if (remain > 0) {
/*  597 */       return this.alignmentBoundary - remain;
/*      */     }
/*  599 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getInode() {
/*  608 */     return this.inode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getMode() {
/*  617 */     return (this.mode == 0L && !"TRAILER!!!".equals(this.name)) ? 32768L : this.mode;
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
/*      */   public String getName() {
/*  629 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getNumberOfLinks() {
/*  638 */     return (this.nlink == 0L) ? (isDirectory() ? 2L : 1L) : this.nlink;
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
/*      */   public long getRemoteDevice() {
/*  650 */     checkOldFormat();
/*  651 */     return this.rmin;
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
/*      */   public long getRemoteDeviceMaj() {
/*  663 */     checkNewFormat();
/*  664 */     return this.rmaj;
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
/*      */   public long getRemoteDeviceMin() {
/*  676 */     checkNewFormat();
/*  677 */     return this.rmin;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getTime() {
/*  686 */     return this.mtime;
/*      */   }
/*      */ 
/*      */   
/*      */   public Date getLastModifiedDate() {
/*  691 */     return new Date(1000L * getTime());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getUID() {
/*  700 */     return this.uid;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBlockDevice() {
/*  709 */     return (CpioUtil.fileType(this.mode) == 24576L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCharacterDevice() {
/*  718 */     return (CpioUtil.fileType(this.mode) == 8192L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDirectory() {
/*  728 */     return (CpioUtil.fileType(this.mode) == 16384L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNetwork() {
/*  737 */     return (CpioUtil.fileType(this.mode) == 36864L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPipe() {
/*  746 */     return (CpioUtil.fileType(this.mode) == 4096L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRegularFile() {
/*  755 */     return (CpioUtil.fileType(this.mode) == 32768L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSocket() {
/*  764 */     return (CpioUtil.fileType(this.mode) == 49152L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSymbolicLink() {
/*  773 */     return (CpioUtil.fileType(this.mode) == 40960L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setChksum(long chksum) {
/*  784 */     checkNewFormat();
/*  785 */     this.chksum = chksum & 0xFFFFFFFFL;
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
/*      */   public void setDevice(long device) {
/*  798 */     checkOldFormat();
/*  799 */     this.min = device;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDeviceMaj(long maj) {
/*  809 */     checkNewFormat();
/*  810 */     this.maj = maj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDeviceMin(long min) {
/*  820 */     checkNewFormat();
/*  821 */     this.min = min;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSize(long size) {
/*  831 */     if (size < 0L || size > 4294967295L) {
/*  832 */       throw new IllegalArgumentException("Invalid entry size <" + size + ">");
/*      */     }
/*  834 */     this.filesize = size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGID(long gid) {
/*  844 */     this.gid = gid;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInode(long inode) {
/*  854 */     this.inode = inode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMode(long mode) {
/*  864 */     long maskedMode = mode & 0xF000L;
/*  865 */     switch ((int)maskedMode) {
/*      */       case 4096:
/*      */       case 8192:
/*      */       case 16384:
/*      */       case 24576:
/*      */       case 32768:
/*      */       case 36864:
/*      */       case 40960:
/*      */       case 49152:
/*      */         break;
/*      */       default:
/*  876 */         throw new IllegalArgumentException("Unknown mode. Full: " + 
/*      */             
/*  878 */             Long.toHexString(mode) + " Masked: " + 
/*  879 */             Long.toHexString(maskedMode));
/*      */     } 
/*      */     
/*  882 */     this.mode = mode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setName(String name) {
/*  892 */     this.name = name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNumberOfLinks(long nlink) {
/*  902 */     this.nlink = nlink;
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
/*      */   public void setRemoteDevice(long device) {
/*  915 */     checkOldFormat();
/*  916 */     this.rmin = device;
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
/*      */   public void setRemoteDeviceMaj(long rmaj) {
/*  929 */     checkNewFormat();
/*  930 */     this.rmaj = rmaj;
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
/*      */   public void setRemoteDeviceMin(long rmin) {
/*  943 */     checkNewFormat();
/*  944 */     this.rmin = rmin;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTime(long time) {
/*  954 */     this.mtime = time;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTime(FileTime time) {
/*  964 */     this.mtime = time.to(TimeUnit.SECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUID(long uid) {
/*  974 */     this.uid = uid;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  982 */     return Objects.hash(new Object[] { this.name });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object obj) {
/*  990 */     if (this == obj) {
/*  991 */       return true;
/*      */     }
/*  993 */     if (obj == null || getClass() != obj.getClass()) {
/*  994 */       return false;
/*      */     }
/*  996 */     CpioArchiveEntry other = (CpioArchiveEntry)obj;
/*  997 */     if (this.name == null) {
/*  998 */       return (other.name == null);
/*      */     }
/* 1000 */     return this.name.equals(other.name);
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\cpio\CpioArchiveEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */