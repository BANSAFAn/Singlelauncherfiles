/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.commons.io.Charsets;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.io.StandardLineSeparator;
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
/*     */ public class ReversedLinesFileReader
/*     */   implements Closeable
/*     */ {
/*     */   private static final String EMPTY_STRING = "";
/*     */   private static final int DEFAULT_BLOCK_SIZE = 8192;
/*     */   private final int blockSize;
/*     */   private final Charset charset;
/*     */   private final SeekableByteChannel channel;
/*     */   private final long totalByteLength;
/*     */   private final long totalBlockCount;
/*     */   private final byte[][] newLineSequences;
/*     */   private final int avoidNewlineSplitBufferSize;
/*     */   private final int byteDecrement;
/*     */   private FilePart currentFilePart;
/*     */   private boolean trailingNewlineOfFileSkipped;
/*     */   
/*     */   private class FilePart
/*     */   {
/*     */     private final long no;
/*     */     private final byte[] data;
/*     */     private byte[] leftOver;
/*     */     private int currentLastBytePos;
/*     */     
/*     */     private FilePart(long no, int length, byte[] leftOverOfLastFilePart) throws IOException {
/*  65 */       this.no = no;
/*  66 */       int dataLength = length + ((leftOverOfLastFilePart != null) ? leftOverOfLastFilePart.length : 0);
/*  67 */       this.data = new byte[dataLength];
/*  68 */       long off = (no - 1L) * ReversedLinesFileReader.this.blockSize;
/*     */ 
/*     */       
/*  71 */       if (no > 0L) {
/*  72 */         ReversedLinesFileReader.this.channel.position(off);
/*  73 */         int countRead = ReversedLinesFileReader.this.channel.read(ByteBuffer.wrap(this.data, 0, length));
/*  74 */         if (countRead != length) {
/*  75 */           throw new IllegalStateException("Count of requested bytes and actually read bytes don't match");
/*     */         }
/*     */       } 
/*     */       
/*  79 */       if (leftOverOfLastFilePart != null) {
/*  80 */         System.arraycopy(leftOverOfLastFilePart, 0, this.data, length, leftOverOfLastFilePart.length);
/*     */       }
/*  82 */       this.currentLastBytePos = this.data.length - 1;
/*  83 */       this.leftOver = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void createLeftOver() {
/*  90 */       int lineLengthBytes = this.currentLastBytePos + 1;
/*  91 */       if (lineLengthBytes > 0) {
/*     */         
/*  93 */         this.leftOver = IOUtils.byteArray(lineLengthBytes);
/*  94 */         System.arraycopy(this.data, 0, this.leftOver, 0, lineLengthBytes);
/*     */       } else {
/*  96 */         this.leftOver = null;
/*     */       } 
/*  98 */       this.currentLastBytePos = -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int getNewLineMatchByteCount(byte[] data, int i) {
/* 109 */       for (byte[] newLineSequence : ReversedLinesFileReader.this.newLineSequences) {
/* 110 */         int k; boolean match = true;
/* 111 */         for (int j = newLineSequence.length - 1; j >= 0; j--) {
/* 112 */           int m = i + j - newLineSequence.length - 1;
/* 113 */           k = match & ((m >= 0 && data[m] == newLineSequence[j]) ? 1 : 0);
/*     */         } 
/* 115 */         if (k != 0) {
/* 116 */           return newLineSequence.length;
/*     */         }
/*     */       } 
/* 119 */       return 0;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String readLine() {
/* 129 */       String line = null;
/*     */ 
/*     */       
/* 132 */       boolean isLastFilePart = (this.no == 1L);
/*     */       
/* 134 */       int i = this.currentLastBytePos;
/* 135 */       while (i > -1) {
/*     */         
/* 137 */         if (!isLastFilePart && i < ReversedLinesFileReader.this.avoidNewlineSplitBufferSize) {
/*     */ 
/*     */           
/* 140 */           createLeftOver();
/*     */           
/*     */           break;
/*     */         } 
/*     */         int newLineMatchByteCount;
/* 145 */         if ((newLineMatchByteCount = getNewLineMatchByteCount(this.data, i)) > 0) {
/* 146 */           int lineStart = i + 1;
/* 147 */           int lineLengthBytes = this.currentLastBytePos - lineStart + 1;
/*     */           
/* 149 */           if (lineLengthBytes < 0) {
/* 150 */             throw new IllegalStateException("Unexpected negative line length=" + lineLengthBytes);
/*     */           }
/* 152 */           byte[] lineData = IOUtils.byteArray(lineLengthBytes);
/* 153 */           System.arraycopy(this.data, lineStart, lineData, 0, lineLengthBytes);
/*     */           
/* 155 */           line = new String(lineData, ReversedLinesFileReader.this.charset);
/*     */           
/* 157 */           this.currentLastBytePos = i - newLineMatchByteCount;
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 162 */         i -= ReversedLinesFileReader.this.byteDecrement;
/*     */ 
/*     */         
/* 165 */         if (i < 0) {
/* 166 */           createLeftOver();
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/* 172 */       if (isLastFilePart && this.leftOver != null) {
/*     */         
/* 174 */         line = new String(this.leftOver, ReversedLinesFileReader.this.charset);
/* 175 */         this.leftOver = null;
/*     */       } 
/*     */       
/* 178 */       return line;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private FilePart rollOver() throws IOException {
/* 189 */       if (this.currentLastBytePos > -1) {
/* 190 */         throw new IllegalStateException("Current currentLastCharPos unexpectedly positive... last readLine() should have returned something! currentLastCharPos=" + this.currentLastBytePos);
/*     */       }
/*     */ 
/*     */       
/* 194 */       if (this.no > 1L) {
/* 195 */         return new FilePart(this.no - 1L, ReversedLinesFileReader.this.blockSize, this.leftOver);
/*     */       }
/*     */       
/* 198 */       if (this.leftOver != null) {
/* 199 */         throw new IllegalStateException("Unexpected leftover of the last block: leftOverOfThisFilePart=" + new String(this.leftOver, ReversedLinesFileReader.this
/* 200 */               .charset));
/*     */       }
/* 202 */       return null;
/*     */     }
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
/*     */   @Deprecated
/*     */   public ReversedLinesFileReader(File file) throws IOException {
/* 230 */     this(file, 8192, Charset.defaultCharset());
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
/*     */   public ReversedLinesFileReader(File file, Charset charset) throws IOException {
/* 243 */     this(file.toPath(), charset);
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
/*     */   public ReversedLinesFileReader(File file, int blockSize, Charset charset) throws IOException {
/* 258 */     this(file.toPath(), blockSize, charset);
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
/*     */   public ReversedLinesFileReader(File file, int blockSize, String charsetName) throws IOException {
/* 277 */     this(file.toPath(), blockSize, charsetName);
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
/*     */   public ReversedLinesFileReader(Path file, Charset charset) throws IOException {
/* 290 */     this(file, 8192, charset);
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
/*     */   public ReversedLinesFileReader(Path file, int blockSize, Charset charset) throws IOException {
/* 305 */     this.blockSize = blockSize;
/* 306 */     this.charset = Charsets.toCharset(charset);
/*     */ 
/*     */     
/* 309 */     CharsetEncoder charsetEncoder = this.charset.newEncoder();
/* 310 */     float maxBytesPerChar = charsetEncoder.maxBytesPerChar();
/* 311 */     if (maxBytesPerChar == 1.0F)
/*     */     
/* 313 */     { this.byteDecrement = 1; }
/* 314 */     else if (this.charset == StandardCharsets.UTF_8)
/*     */     
/*     */     { 
/*     */       
/* 318 */       this.byteDecrement = 1; }
/* 319 */     else if (this.charset == Charset.forName("Shift_JIS") || this.charset == 
/*     */       
/* 321 */       Charset.forName("windows-31j") || this.charset == 
/* 322 */       Charset.forName("x-windows-949") || this.charset == 
/* 323 */       Charset.forName("gbk") || this.charset == 
/* 324 */       Charset.forName("x-windows-950"))
/* 325 */     { this.byteDecrement = 1; }
/* 326 */     else if (this.charset == StandardCharsets.UTF_16BE || this.charset == StandardCharsets.UTF_16LE)
/*     */     
/*     */     { 
/*     */       
/* 330 */       this.byteDecrement = 2; }
/* 331 */     else { if (this.charset == StandardCharsets.UTF_16) {
/* 332 */         throw new UnsupportedEncodingException("For UTF-16, you need to specify the byte order (use UTF-16BE or UTF-16LE)");
/*     */       }
/*     */       
/* 335 */       throw new UnsupportedEncodingException("Encoding " + charset + " is not supported yet (feel free to submit a patch)"); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 341 */     this
/*     */ 
/*     */       
/* 344 */       .newLineSequences = new byte[][] { StandardLineSeparator.CRLF.getBytes(this.charset), StandardLineSeparator.LF.getBytes(this.charset), StandardLineSeparator.CR.getBytes(this.charset) };
/*     */ 
/*     */     
/* 347 */     this.avoidNewlineSplitBufferSize = (this.newLineSequences[0]).length;
/*     */ 
/*     */     
/* 350 */     this.channel = Files.newByteChannel(file, new OpenOption[] { StandardOpenOption.READ });
/* 351 */     this.totalByteLength = this.channel.size();
/* 352 */     int lastBlockLength = (int)(this.totalByteLength % blockSize);
/* 353 */     if (lastBlockLength > 0) {
/* 354 */       this.totalBlockCount = this.totalByteLength / blockSize + 1L;
/*     */     } else {
/* 356 */       this.totalBlockCount = this.totalByteLength / blockSize;
/* 357 */       if (this.totalByteLength > 0L) {
/* 358 */         lastBlockLength = blockSize;
/*     */       }
/*     */     } 
/* 361 */     this.currentFilePart = new FilePart(this.totalBlockCount, lastBlockLength, null);
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
/*     */   public ReversedLinesFileReader(Path file, int blockSize, String charsetName) throws IOException {
/* 382 */     this(file, blockSize, Charsets.toCharset(charsetName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 392 */     this.channel.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String readLine() throws IOException {
/* 403 */     String line = this.currentFilePart.readLine();
/* 404 */     while (line == null) {
/* 405 */       this.currentFilePart = this.currentFilePart.rollOver();
/* 406 */       if (this.currentFilePart == null) {
/*     */         break;
/*     */       }
/*     */       
/* 410 */       line = this.currentFilePart.readLine();
/*     */     } 
/*     */ 
/*     */     
/* 414 */     if ("".equals(line) && !this.trailingNewlineOfFileSkipped) {
/* 415 */       this.trailingNewlineOfFileSkipped = true;
/* 416 */       line = readLine();
/*     */     } 
/*     */     
/* 419 */     return line;
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
/*     */   public List<String> readLines(int lineCount) throws IOException {
/* 438 */     if (lineCount < 0) {
/* 439 */       throw new IllegalArgumentException("lineCount < 0");
/*     */     }
/* 441 */     ArrayList<String> arrayList = new ArrayList<>(lineCount);
/* 442 */     for (int i = 0; i < lineCount; i++) {
/* 443 */       String line = readLine();
/* 444 */       if (line == null) {
/* 445 */         return arrayList;
/*     */       }
/* 447 */       arrayList.add(line);
/*     */     } 
/* 449 */     return arrayList;
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
/*     */   public String toString(int lineCount) throws IOException {
/* 465 */     List<String> lines = readLines(lineCount);
/* 466 */     Collections.reverse(lines);
/* 467 */     return lines.isEmpty() ? "" : (String.join(System.lineSeparator(), (Iterable)lines) + System.lineSeparator());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\ReversedLinesFileReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */