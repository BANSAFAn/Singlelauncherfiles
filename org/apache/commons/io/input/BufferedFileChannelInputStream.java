/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.Objects;
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
/*     */ 
/*     */ 
/*     */ public final class BufferedFileChannelInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private final ByteBuffer byteBuffer;
/*     */   private final FileChannel fileChannel;
/*  50 */   private static final Class<?> DIRECT_BUFFER_CLASS = getDirectBufferClass();
/*     */   
/*     */   private static Class<?> getDirectBufferClass() {
/*  53 */     Class<?> res = null;
/*     */     try {
/*  55 */       res = Class.forName("sun.nio.ch.DirectBuffer");
/*  56 */     } catch (IllegalAccessError|ClassNotFoundException illegalAccessError) {}
/*     */ 
/*     */     
/*  59 */     return res;
/*     */   }
/*     */   
/*     */   private static boolean isDirectBuffer(Object object) {
/*  63 */     return (DIRECT_BUFFER_CLASS != null && DIRECT_BUFFER_CLASS.isInstance(object));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferedFileChannelInputStream(File file) throws IOException {
/*  73 */     this(file, 8192);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferedFileChannelInputStream(File file, int bufferSizeInBytes) throws IOException {
/*  84 */     this(file.toPath(), bufferSizeInBytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferedFileChannelInputStream(Path path) throws IOException {
/*  94 */     this(path, 8192);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferedFileChannelInputStream(Path path, int bufferSizeInBytes) throws IOException {
/* 105 */     Objects.requireNonNull(path, "path");
/* 106 */     this.fileChannel = FileChannel.open(path, new OpenOption[] { StandardOpenOption.READ });
/* 107 */     this.byteBuffer = ByteBuffer.allocateDirect(bufferSizeInBytes);
/* 108 */     this.byteBuffer.flip();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int available() throws IOException {
/* 113 */     return this.byteBuffer.remaining();
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
/*     */   private void clean(ByteBuffer buffer) {
/* 126 */     if (isDirectBuffer(buffer)) {
/* 127 */       cleanDirectBuffer(buffer);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cleanDirectBuffer(ByteBuffer buffer) {
/* 162 */     String specVer = System.getProperty("java.specification.version");
/* 163 */     if ("1.8".equals(specVer)) {
/*     */       
/*     */       try {
/* 166 */         Class<?> clsCleaner = Class.forName("sun.misc.Cleaner");
/* 167 */         Method cleanerMethod = DIRECT_BUFFER_CLASS.getMethod("cleaner", new Class[0]);
/* 168 */         Object cleaner = cleanerMethod.invoke(buffer, new Object[0]);
/* 169 */         if (cleaner != null) {
/* 170 */           Method cleanMethod = clsCleaner.getMethod("clean", new Class[0]);
/* 171 */           cleanMethod.invoke(cleaner, new Object[0]);
/*     */         } 
/* 173 */       } catch (ReflectiveOperationException e) {
/* 174 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     } else {
/*     */       
/*     */       try {
/* 179 */         Class<?> clsUnsafe = Class.forName("sun.misc.Unsafe");
/* 180 */         Method cleanerMethod = clsUnsafe.getMethod("invokeCleaner", new Class[] { ByteBuffer.class });
/* 181 */         Field unsafeField = clsUnsafe.getDeclaredField("theUnsafe");
/* 182 */         unsafeField.setAccessible(true);
/* 183 */         cleanerMethod.invoke(unsafeField.get((Object)null), new Object[] { buffer });
/* 184 */       } catch (ReflectiveOperationException e) {
/* 185 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close() throws IOException {
/*     */     try {
/* 193 */       this.fileChannel.close();
/*     */     } finally {
/* 195 */       clean(this.byteBuffer);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int read() throws IOException {
/* 201 */     if (!refill()) {
/* 202 */       return -1;
/*     */     }
/* 204 */     return this.byteBuffer.get() & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int read(byte[] b, int offset, int len) throws IOException {
/* 209 */     if (offset < 0 || len < 0 || offset + len < 0 || offset + len > b.length) {
/* 210 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 212 */     if (!refill()) {
/* 213 */       return -1;
/*     */     }
/* 215 */     len = Math.min(len, this.byteBuffer.remaining());
/* 216 */     this.byteBuffer.get(b, offset, len);
/* 217 */     return len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean refill() throws IOException {
/* 226 */     if (!this.byteBuffer.hasRemaining()) {
/* 227 */       this.byteBuffer.clear();
/* 228 */       int nRead = 0;
/* 229 */       while (nRead == 0) {
/* 230 */         nRead = this.fileChannel.read(this.byteBuffer);
/*     */       }
/* 232 */       this.byteBuffer.flip();
/* 233 */       return (nRead >= 0);
/*     */     } 
/* 235 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized long skip(long n) throws IOException {
/* 240 */     if (n <= 0L) {
/* 241 */       return 0L;
/*     */     }
/* 243 */     if (this.byteBuffer.remaining() >= n) {
/*     */       
/* 245 */       this.byteBuffer.position(this.byteBuffer.position() + (int)n);
/* 246 */       return n;
/*     */     } 
/* 248 */     long skippedFromBuffer = this.byteBuffer.remaining();
/* 249 */     long toSkipFromFileChannel = n - skippedFromBuffer;
/*     */     
/* 251 */     this.byteBuffer.position(0);
/* 252 */     this.byteBuffer.flip();
/* 253 */     return skippedFromBuffer + skipFromFileChannel(toSkipFromFileChannel);
/*     */   }
/*     */   
/*     */   private long skipFromFileChannel(long n) throws IOException {
/* 257 */     long currentFilePosition = this.fileChannel.position();
/* 258 */     long size = this.fileChannel.size();
/* 259 */     if (n > size - currentFilePosition) {
/* 260 */       this.fileChannel.position(size);
/* 261 */       return size - currentFilePosition;
/*     */     } 
/* 263 */     this.fileChannel.position(currentFilePosition + n);
/* 264 */     return n;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\BufferedFileChannelInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */