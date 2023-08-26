/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ public class ReadAheadInputStream
/*     */   extends InputStream
/*     */ {
/*  46 */   private static final ThreadLocal<byte[]> oneByte = (ThreadLocal)ThreadLocal.withInitial(() -> new byte[1]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ExecutorService newExecutorService() {
/*  54 */     return Executors.newSingleThreadExecutor(ReadAheadInputStream::newThread);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Thread newThread(Runnable r) {
/*  64 */     Thread thread = new Thread(r, "commons-io-read-ahead");
/*  65 */     thread.setDaemon(true);
/*  66 */     return thread;
/*     */   }
/*     */   
/*  69 */   private final ReentrantLock stateChangeLock = new ReentrantLock();
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteBuffer activeBuffer;
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteBuffer readAheadBuffer;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean endOfStream;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean readInProgress;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean readAborted;
/*     */ 
/*     */ 
/*     */   
/*     */   private Throwable readException;
/*     */ 
/*     */   
/*     */   private boolean isClosed;
/*     */ 
/*     */   
/*     */   private boolean isUnderlyingInputStreamBeingClosed;
/*     */ 
/*     */   
/*     */   private boolean isReading;
/*     */ 
/*     */   
/* 105 */   private final AtomicBoolean isWaiting = new AtomicBoolean(false);
/*     */   
/*     */   private final InputStream underlyingInputStream;
/*     */   
/*     */   private final ExecutorService executorService;
/*     */   
/*     */   private final boolean shutdownExecutorService;
/*     */   
/* 113 */   private final Condition asyncReadComplete = this.stateChangeLock.newCondition();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReadAheadInputStream(InputStream inputStream, int bufferSizeInBytes) {
/* 122 */     this(inputStream, bufferSizeInBytes, newExecutorService(), true);
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
/*     */   public ReadAheadInputStream(InputStream inputStream, int bufferSizeInBytes, ExecutorService executorService) {
/* 134 */     this(inputStream, bufferSizeInBytes, executorService, false);
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
/*     */   private ReadAheadInputStream(InputStream inputStream, int bufferSizeInBytes, ExecutorService executorService, boolean shutdownExecutorService) {
/* 147 */     if (bufferSizeInBytes <= 0) {
/* 148 */       throw new IllegalArgumentException("bufferSizeInBytes should be greater than 0, but the value is " + bufferSizeInBytes);
/*     */     }
/*     */     
/* 151 */     this.executorService = Objects.<ExecutorService>requireNonNull(executorService, "executorService");
/* 152 */     this.underlyingInputStream = Objects.<InputStream>requireNonNull(inputStream, "inputStream");
/* 153 */     this.shutdownExecutorService = shutdownExecutorService;
/* 154 */     this.activeBuffer = ByteBuffer.allocate(bufferSizeInBytes);
/* 155 */     this.readAheadBuffer = ByteBuffer.allocate(bufferSizeInBytes);
/* 156 */     this.activeBuffer.flip();
/* 157 */     this.readAheadBuffer.flip();
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/* 162 */     this.stateChangeLock.lock();
/*     */     
/*     */     try {
/* 165 */       return (int)Math.min(2147483647L, this.activeBuffer.remaining() + this.readAheadBuffer.remaining());
/*     */     } finally {
/* 167 */       this.stateChangeLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkReadException() throws IOException {
/* 172 */     if (this.readAborted) {
/* 173 */       if (this.readException instanceof IOException) {
/* 174 */         throw (IOException)this.readException;
/*     */       }
/* 176 */       throw new IOException(this.readException);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 182 */     boolean isSafeToCloseUnderlyingInputStream = false;
/* 183 */     this.stateChangeLock.lock();
/*     */     try {
/* 185 */       if (this.isClosed) {
/*     */         return;
/*     */       }
/* 188 */       this.isClosed = true;
/* 189 */       if (!this.isReading) {
/*     */         
/* 191 */         isSafeToCloseUnderlyingInputStream = true;
/*     */         
/* 193 */         this.isUnderlyingInputStreamBeingClosed = true;
/*     */       } 
/*     */     } finally {
/* 196 */       this.stateChangeLock.unlock();
/*     */     } 
/*     */     
/* 199 */     if (this.shutdownExecutorService) {
/*     */       try {
/* 201 */         this.executorService.shutdownNow();
/* 202 */         this.executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
/* 203 */       } catch (InterruptedException e) {
/* 204 */         InterruptedIOException iio = new InterruptedIOException(e.getMessage());
/* 205 */         iio.initCause(e);
/* 206 */         throw iio;
/*     */       } finally {
/* 208 */         if (isSafeToCloseUnderlyingInputStream) {
/* 209 */           this.underlyingInputStream.close();
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void closeUnderlyingInputStreamIfNecessary() {
/* 216 */     boolean needToCloseUnderlyingInputStream = false;
/* 217 */     this.stateChangeLock.lock();
/*     */     try {
/* 219 */       this.isReading = false;
/* 220 */       if (this.isClosed && !this.isUnderlyingInputStreamBeingClosed)
/*     */       {
/* 222 */         needToCloseUnderlyingInputStream = true;
/*     */       }
/*     */     } finally {
/* 225 */       this.stateChangeLock.unlock();
/*     */     } 
/* 227 */     if (needToCloseUnderlyingInputStream) {
/*     */       try {
/* 229 */         this.underlyingInputStream.close();
/* 230 */       } catch (IOException iOException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isEndOfStream() {
/* 237 */     return (!this.activeBuffer.hasRemaining() && !this.readAheadBuffer.hasRemaining() && this.endOfStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 242 */     if (this.activeBuffer.hasRemaining())
/*     */     {
/* 244 */       return this.activeBuffer.get() & 0xFF;
/*     */     }
/* 246 */     byte[] oneByteArray = oneByte.get();
/* 247 */     return (read(oneByteArray, 0, 1) == -1) ? -1 : (oneByteArray[0] & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int offset, int len) throws IOException {
/* 252 */     if (offset < 0 || len < 0 || len > b.length - offset) {
/* 253 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 255 */     if (len == 0) {
/* 256 */       return 0;
/*     */     }
/*     */     
/* 259 */     if (!this.activeBuffer.hasRemaining()) {
/*     */       
/* 261 */       this.stateChangeLock.lock();
/*     */       try {
/* 263 */         waitForAsyncReadComplete();
/* 264 */         if (!this.readAheadBuffer.hasRemaining()) {
/*     */           
/* 266 */           readAsync();
/* 267 */           waitForAsyncReadComplete();
/* 268 */           if (isEndOfStream()) {
/* 269 */             return -1;
/*     */           }
/*     */         } 
/*     */         
/* 273 */         swapBuffers();
/*     */         
/* 275 */         readAsync();
/*     */       } finally {
/* 277 */         this.stateChangeLock.unlock();
/*     */       } 
/*     */     } 
/* 280 */     len = Math.min(len, this.activeBuffer.remaining());
/* 281 */     this.activeBuffer.get(b, offset, len);
/*     */     
/* 283 */     return len;
/*     */   }
/*     */   
/*     */   private void readAsync() throws IOException {
/*     */     byte[] arr;
/* 288 */     this.stateChangeLock.lock();
/*     */     
/*     */     try {
/* 291 */       arr = this.readAheadBuffer.array();
/* 292 */       if (this.endOfStream || this.readInProgress) {
/*     */         return;
/*     */       }
/* 295 */       checkReadException();
/* 296 */       this.readAheadBuffer.position(0);
/* 297 */       this.readAheadBuffer.flip();
/* 298 */       this.readInProgress = true;
/*     */     } finally {
/* 300 */       this.stateChangeLock.unlock();
/*     */     } 
/* 302 */     this.executorService.execute(() -> {
/*     */           this.stateChangeLock.lock();
/*     */ 
/*     */           
/*     */           try {
/*     */             if (this.isClosed) {
/*     */               this.readInProgress = false;
/*     */ 
/*     */               
/*     */               return;
/*     */             } 
/*     */             
/*     */             this.isReading = true;
/*     */           } finally {
/*     */             this.stateChangeLock.unlock();
/*     */           } 
/*     */           
/*     */           int read = 0;
/*     */           
/*     */           int off = 0;
/*     */           
/*     */           int len = arr.length;
/*     */           
/*     */           Throwable exception = null;
/*     */           
/*     */           try {
/*     */             do {
/*     */               read = this.underlyingInputStream.read(arr, off, len);
/*     */               
/*     */               if (read <= 0) {
/*     */                 break;
/*     */               }
/*     */               
/*     */               off += read;
/*     */               
/*     */               len -= read;
/*     */             } while (len > 0 && !this.isWaiting.get());
/* 339 */           } catch (Throwable ex) {
/*     */             exception = ex;
/*     */             if (ex instanceof Error) {
/*     */               throw (Error)ex;
/*     */             }
/*     */           } finally {
/*     */             this.stateChangeLock.lock();
/*     */             try {
/*     */               this.readAheadBuffer.limit(off);
/*     */               if (read < 0 || exception instanceof java.io.EOFException) {
/*     */                 this.endOfStream = true;
/*     */               } else if (exception != null) {
/*     */                 this.readAborted = true;
/*     */                 this.readException = exception;
/*     */               } 
/*     */               this.readInProgress = false;
/*     */               signalAsyncReadComplete();
/*     */             } finally {
/*     */               this.stateChangeLock.unlock();
/*     */             } 
/*     */             closeUnderlyingInputStreamIfNecessary();
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void signalAsyncReadComplete() {
/* 367 */     this.stateChangeLock.lock();
/*     */     try {
/* 369 */       this.asyncReadComplete.signalAll();
/*     */     } finally {
/* 371 */       this.stateChangeLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public long skip(long n) throws IOException {
/*     */     long skipped;
/* 377 */     if (n <= 0L) {
/* 378 */       return 0L;
/*     */     }
/* 380 */     if (n <= this.activeBuffer.remaining()) {
/*     */       
/* 382 */       this.activeBuffer.position((int)n + this.activeBuffer.position());
/* 383 */       return n;
/*     */     } 
/* 385 */     this.stateChangeLock.lock();
/*     */     
/*     */     try {
/* 388 */       skipped = skipInternal(n);
/*     */     } finally {
/* 390 */       this.stateChangeLock.unlock();
/*     */     } 
/* 392 */     return skipped;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long skipInternal(long n) throws IOException {
/* 403 */     assert this.stateChangeLock.isLocked();
/* 404 */     waitForAsyncReadComplete();
/* 405 */     if (isEndOfStream()) {
/* 406 */       return 0L;
/*     */     }
/* 408 */     if (available() >= n) {
/*     */       
/* 410 */       int i = (int)n;
/*     */       
/* 412 */       i -= this.activeBuffer.remaining();
/* 413 */       assert i > 0;
/* 414 */       this.activeBuffer.position(0);
/* 415 */       this.activeBuffer.flip();
/* 416 */       this.readAheadBuffer.position(i + this.readAheadBuffer.position());
/* 417 */       swapBuffers();
/*     */       
/* 419 */       readAsync();
/* 420 */       return n;
/*     */     } 
/* 422 */     int skippedBytes = available();
/* 423 */     long toSkip = n - skippedBytes;
/* 424 */     this.activeBuffer.position(0);
/* 425 */     this.activeBuffer.flip();
/* 426 */     this.readAheadBuffer.position(0);
/* 427 */     this.readAheadBuffer.flip();
/* 428 */     long skippedFromInputStream = this.underlyingInputStream.skip(toSkip);
/* 429 */     readAsync();
/* 430 */     return skippedBytes + skippedFromInputStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void swapBuffers() {
/* 437 */     ByteBuffer temp = this.activeBuffer;
/* 438 */     this.activeBuffer = this.readAheadBuffer;
/* 439 */     this.readAheadBuffer = temp;
/*     */   }
/*     */   
/*     */   private void waitForAsyncReadComplete() throws IOException {
/* 443 */     this.stateChangeLock.lock();
/*     */     try {
/* 445 */       this.isWaiting.set(true);
/*     */ 
/*     */       
/* 448 */       while (this.readInProgress) {
/* 449 */         this.asyncReadComplete.await();
/*     */       }
/* 451 */     } catch (InterruptedException e) {
/* 452 */       InterruptedIOException iio = new InterruptedIOException(e.getMessage());
/* 453 */       iio.initCause(e);
/* 454 */       throw iio;
/*     */     } finally {
/* 456 */       this.isWaiting.set(false);
/* 457 */       this.stateChangeLock.unlock();
/*     */     } 
/* 459 */     checkReadException();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\ReadAheadInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */