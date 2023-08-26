/*     */ package org.apache.commons.compress.archivers.zip;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.compress.utils.FileNameUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ZipSplitOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private OutputStream outputStream;
/*     */   private Path zipFile;
/*     */   private final long splitSize;
/*     */   private int currentSplitSegmentIndex;
/*     */   private long currentSplitSegmentBytesWritten;
/*     */   private boolean finished;
/*  42 */   private final byte[] singleByte = new byte[1];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long ZIP_SEGMENT_MIN_SIZE = 65536L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long ZIP_SEGMENT_MAX_SIZE = 4294967295L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipSplitOutputStream(File zipFile, long splitSize) throws IllegalArgumentException, IOException {
/*  63 */     this(zipFile.toPath(), splitSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipSplitOutputStream(Path zipFile, long splitSize) throws IllegalArgumentException, IOException {
/*  75 */     if (splitSize < 65536L || splitSize > 4294967295L) {
/*  76 */       throw new IllegalArgumentException("zip split segment size should between 64K and 4,294,967,295");
/*     */     }
/*  78 */     this.zipFile = zipFile;
/*  79 */     this.splitSize = splitSize;
/*  80 */     this.outputStream = Files.newOutputStream(zipFile, new java.nio.file.OpenOption[0]);
/*     */     
/*  82 */     writeZipSplitSignature();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepareToWriteUnsplittableContent(long unsplittableContentSize) throws IllegalArgumentException, IOException {
/*  97 */     if (unsplittableContentSize > this.splitSize) {
/*  98 */       throw new IllegalArgumentException("The unsplittable content size is bigger than the split segment size");
/*     */     }
/*     */     
/* 101 */     long bytesRemainingInThisSegment = this.splitSize - this.currentSplitSegmentBytesWritten;
/* 102 */     if (bytesRemainingInThisSegment < unsplittableContentSize) {
/* 103 */       openNewSplitSegment();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int i) throws IOException {
/* 109 */     this.singleByte[0] = (byte)(i & 0xFF);
/* 110 */     write(this.singleByte);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/* 115 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/* 129 */     if (len <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 133 */     if (this.currentSplitSegmentBytesWritten >= this.splitSize) {
/* 134 */       openNewSplitSegment();
/* 135 */       write(b, off, len);
/* 136 */     } else if (this.currentSplitSegmentBytesWritten + len > this.splitSize) {
/* 137 */       int bytesToWriteForThisSegment = (int)this.splitSize - (int)this.currentSplitSegmentBytesWritten;
/* 138 */       write(b, off, bytesToWriteForThisSegment);
/* 139 */       openNewSplitSegment();
/* 140 */       write(b, off + bytesToWriteForThisSegment, len - bytesToWriteForThisSegment);
/*     */     } else {
/* 142 */       this.outputStream.write(b, off, len);
/* 143 */       this.currentSplitSegmentBytesWritten += len;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 149 */     if (!this.finished) {
/* 150 */       finish();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void finish() throws IOException {
/* 160 */     if (this.finished) {
/* 161 */       throw new IOException("This archive has already been finished");
/*     */     }
/*     */     
/* 164 */     String zipFileBaseName = FileNameUtils.getBaseName(this.zipFile);
/* 165 */     this.outputStream.close();
/* 166 */     Files.move(this.zipFile, this.zipFile.resolveSibling(zipFileBaseName + ".zip"), new CopyOption[] { StandardCopyOption.ATOMIC_MOVE });
/* 167 */     this.finished = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void openNewSplitSegment() throws IOException {
/* 177 */     if (this.currentSplitSegmentIndex == 0) {
/* 178 */       this.outputStream.close();
/* 179 */       Path path = createNewSplitSegmentFile(Integer.valueOf(1));
/* 180 */       Files.move(this.zipFile, path, new CopyOption[] { StandardCopyOption.ATOMIC_MOVE });
/*     */     } 
/*     */     
/* 183 */     Path newFile = createNewSplitSegmentFile(null);
/*     */     
/* 185 */     this.outputStream.close();
/* 186 */     this.outputStream = Files.newOutputStream(newFile, new java.nio.file.OpenOption[0]);
/* 187 */     this.currentSplitSegmentBytesWritten = 0L;
/* 188 */     this.zipFile = newFile;
/* 189 */     this.currentSplitSegmentIndex++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeZipSplitSignature() throws IOException {
/* 199 */     this.outputStream.write(ZipArchiveOutputStream.DD_SIG);
/* 200 */     this.currentSplitSegmentBytesWritten += ZipArchiveOutputStream.DD_SIG.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Path createNewSplitSegmentFile(Integer zipSplitSegmentSuffixIndex) throws IOException {
/* 226 */     int newZipSplitSegmentSuffixIndex = (zipSplitSegmentSuffixIndex == null) ? (this.currentSplitSegmentIndex + 2) : zipSplitSegmentSuffixIndex.intValue();
/* 227 */     String baseName = FileNameUtils.getBaseName(this.zipFile);
/* 228 */     String extension = ".z";
/* 229 */     if (newZipSplitSegmentSuffixIndex <= 9) {
/* 230 */       extension = extension + "0" + newZipSplitSegmentSuffixIndex;
/*     */     } else {
/* 232 */       extension = extension + newZipSplitSegmentSuffixIndex;
/*     */     } 
/*     */     
/* 235 */     Path parent = this.zipFile.getParent();
/* 236 */     String dir = Objects.nonNull(parent) ? parent.toAbsolutePath().toString() : ".";
/* 237 */     Path newFile = this.zipFile.getFileSystem().getPath(dir, new String[] { baseName + extension });
/*     */     
/* 239 */     if (Files.exists(newFile, new java.nio.file.LinkOption[0])) {
/* 240 */       throw new IOException("split zip segment " + baseName + extension + " already exists");
/*     */     }
/* 242 */     return newFile;
/*     */   }
/*     */   
/*     */   public int getCurrentSplitSegmentIndex() {
/* 246 */     return this.currentSplitSegmentIndex;
/*     */   }
/*     */   
/*     */   public long getCurrentSplitSegmentBytesWritten() {
/* 250 */     return this.currentSplitSegmentBytesWritten;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\zip\ZipSplitOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */