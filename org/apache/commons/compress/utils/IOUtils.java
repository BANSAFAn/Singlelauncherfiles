/*     */ package org.apache.commons.compress.utils;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Closeable;
/*     */ import java.io.EOFException;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class IOUtils
/*     */ {
/*     */   private static final int COPY_BUF_SIZE = 8024;
/*     */   private static final int SKIP_BUF_SIZE = 4096;
/*  47 */   public static final LinkOption[] EMPTY_LINK_OPTIONS = new LinkOption[0];
/*     */ 
/*     */ 
/*     */   
/*  51 */   private static final byte[] SKIP_BUF = new byte[4096];
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
/*     */   public static long copy(InputStream input, OutputStream output) throws IOException {
/*  70 */     return copy(input, output, 8024);
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
/*     */   public static long copy(InputStream input, OutputStream output, int buffersize) throws IOException {
/*  89 */     if (buffersize < 1) {
/*  90 */       throw new IllegalArgumentException("buffersize must be bigger than 0");
/*     */     }
/*  92 */     byte[] buffer = new byte[buffersize];
/*  93 */     int n = 0;
/*  94 */     long count = 0L;
/*  95 */     while (-1 != (n = input.read(buffer))) {
/*  96 */       if (output != null) {
/*  97 */         output.write(buffer, 0, n);
/*     */       }
/*  99 */       count += n;
/*     */     } 
/* 101 */     return count;
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
/*     */   public static long skip(InputStream input, long numToSkip) throws IOException {
/* 121 */     long available = numToSkip;
/* 122 */     while (numToSkip > 0L) {
/* 123 */       long skipped = input.skip(numToSkip);
/* 124 */       if (skipped == 0L) {
/*     */         break;
/*     */       }
/* 127 */       numToSkip -= skipped;
/*     */     } 
/*     */     
/* 130 */     while (numToSkip > 0L) {
/* 131 */       int read = readFully(input, SKIP_BUF, 0, 
/* 132 */           (int)Math.min(numToSkip, 4096L));
/* 133 */       if (read < 1) {
/*     */         break;
/*     */       }
/* 136 */       numToSkip -= read;
/*     */     } 
/* 138 */     return available - numToSkip;
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
/*     */   public static int read(File file, byte[] array) throws IOException {
/* 155 */     try (InputStream inputStream = Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0])) {
/* 156 */       return readFully(inputStream, array, 0, array.length);
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
/*     */   public static int readFully(InputStream input, byte[] array) throws IOException {
/* 173 */     return readFully(input, array, 0, array.length);
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
/*     */   public static int readFully(InputStream input, byte[] array, int offset, int len) throws IOException {
/* 194 */     if (len < 0 || offset < 0 || len + offset > array.length || len + offset < 0) {
/* 195 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 197 */     int count = 0, x = 0;
/* 198 */     while (count != len) {
/* 199 */       x = input.read(array, offset + count, len - count);
/* 200 */       if (x == -1) {
/*     */         break;
/*     */       }
/* 203 */       count += x;
/*     */     } 
/* 205 */     return count;
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
/*     */   public static void readFully(ReadableByteChannel channel, ByteBuffer byteBuffer) throws IOException {
/* 223 */     int expectedLength = byteBuffer.remaining();
/* 224 */     int read = 0;
/* 225 */     while (read < expectedLength) {
/* 226 */       int readNow = channel.read(byteBuffer);
/* 227 */       if (readNow <= 0) {
/*     */         break;
/*     */       }
/* 230 */       read += readNow;
/*     */     } 
/* 232 */     if (read < expectedLength) {
/* 233 */       throw new EOFException();
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
/*     */   public static byte[] toByteArray(InputStream input) throws IOException {
/* 256 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/* 257 */     copy(input, output);
/* 258 */     return output.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeQuietly(Closeable c) {
/* 267 */     if (c != null) {
/*     */       try {
/* 269 */         c.close();
/* 270 */       } catch (IOException iOException) {}
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
/*     */   public static void copy(File sourceFile, OutputStream outputStream) throws IOException {
/* 283 */     Files.copy(sourceFile.toPath(), outputStream);
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
/*     */   public static long copyRange(InputStream input, long len, OutputStream output) throws IOException {
/* 303 */     return copyRange(input, len, output, 8024);
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
/*     */   public static long copyRange(InputStream input, long len, OutputStream output, int buffersize) throws IOException {
/* 326 */     if (buffersize < 1) {
/* 327 */       throw new IllegalArgumentException("buffersize must be bigger than 0");
/*     */     }
/* 329 */     byte[] buffer = new byte[(int)Math.min(buffersize, len)];
/* 330 */     int n = 0;
/* 331 */     long count = 0L;
/* 332 */     while (count < len && -1 != (n = input.read(buffer, 0, (int)Math.min(len - count, buffer.length)))) {
/* 333 */       if (output != null) {
/* 334 */         output.write(buffer, 0, n);
/*     */       }
/* 336 */       count += n;
/*     */     } 
/* 338 */     return count;
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
/*     */   public static byte[] readRange(InputStream input, int len) throws IOException {
/* 353 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/* 354 */     copyRange(input, len, output);
/* 355 */     return output.toByteArray();
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
/*     */   public static byte[] readRange(ReadableByteChannel input, int len) throws IOException {
/* 370 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/* 371 */     ByteBuffer b = ByteBuffer.allocate(Math.min(len, 8024));
/* 372 */     int read = 0;
/* 373 */     while (read < len) {
/*     */       
/* 375 */       b.limit(Math.min(len - read, b.capacity()));
/* 376 */       int readNow = input.read(b);
/* 377 */       if (readNow <= 0) {
/*     */         break;
/*     */       }
/* 380 */       output.write(b.array(), 0, readNow);
/* 381 */       b.rewind();
/* 382 */       read += readNow;
/*     */     } 
/* 384 */     return output.toByteArray();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compres\\utils\IOUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */