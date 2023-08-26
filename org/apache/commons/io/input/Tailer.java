/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.charset.Charset;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Tailer
/*     */   implements Runnable
/*     */ {
/*     */   private static final int DEFAULT_DELAY_MILLIS = 1000;
/*     */   private static final String RAF_MODE = "r";
/* 133 */   private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] inbuf;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final File file;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Charset charset;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final long delayMillis;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean end;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final TailerListener listener;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean reOpen;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean run = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tailer(File file, TailerListener listener) {
/* 181 */     this(file, listener, 1000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tailer(File file, TailerListener listener, long delayMillis) {
/* 191 */     this(file, listener, delayMillis, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tailer(File file, TailerListener listener, long delayMillis, boolean end) {
/* 202 */     this(file, listener, delayMillis, end, 8192);
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
/*     */   public Tailer(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen) {
/* 215 */     this(file, listener, delayMillis, end, reOpen, 8192);
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
/*     */   public Tailer(File file, TailerListener listener, long delayMillis, boolean end, int bufSize) {
/* 228 */     this(file, listener, delayMillis, end, false, bufSize);
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
/*     */   public Tailer(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
/* 242 */     this(file, DEFAULT_CHARSET, listener, delayMillis, end, reOpen, bufSize);
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
/*     */   public Tailer(File file, Charset charset, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
/* 258 */     this.file = file;
/* 259 */     this.delayMillis = delayMillis;
/* 260 */     this.end = end;
/*     */     
/* 262 */     this.inbuf = IOUtils.byteArray(bufSize);
/*     */ 
/*     */     
/* 265 */     this.listener = listener;
/* 266 */     listener.init(this);
/* 267 */     this.reOpen = reOpen;
/* 268 */     this.charset = charset;
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
/*     */   public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, int bufSize) {
/* 283 */     return create(file, listener, delayMillis, end, false, bufSize);
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
/*     */   public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
/* 300 */     return create(file, DEFAULT_CHARSET, listener, delayMillis, end, reOpen, bufSize);
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
/*     */   public static Tailer create(File file, Charset charset, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
/* 318 */     Tailer tailer = new Tailer(file, charset, listener, delayMillis, end, reOpen, bufSize);
/* 319 */     Thread thread = new Thread(tailer);
/* 320 */     thread.setDaemon(true);
/* 321 */     thread.start();
/* 322 */     return tailer;
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
/*     */   public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end) {
/* 336 */     return create(file, listener, delayMillis, end, 8192);
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
/*     */   public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen) {
/* 351 */     return create(file, listener, delayMillis, end, reOpen, 8192);
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
/*     */   public static Tailer create(File file, TailerListener listener, long delayMillis) {
/* 363 */     return create(file, listener, delayMillis, false);
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
/*     */   public static Tailer create(File file, TailerListener listener) {
/* 375 */     return create(file, listener, 1000L, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 384 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean getRun() {
/* 394 */     return this.run;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDelay() {
/* 403 */     return this.delayMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 411 */     RandomAccessFile reader = null;
/*     */     try {
/* 413 */       long last = 0L;
/* 414 */       long position = 0L;
/*     */       
/* 416 */       while (getRun() && reader == null) {
/*     */         try {
/* 418 */           reader = new RandomAccessFile(this.file, "r");
/* 419 */         } catch (FileNotFoundException e) {
/* 420 */           this.listener.fileNotFound();
/*     */         } 
/* 422 */         if (reader == null) {
/* 423 */           Thread.sleep(this.delayMillis);
/*     */           continue;
/*     */         } 
/* 426 */         position = this.end ? this.file.length() : 0L;
/* 427 */         last = FileUtils.lastModified(this.file);
/* 428 */         reader.seek(position);
/*     */       } 
/*     */       
/* 431 */       while (getRun()) {
/* 432 */         boolean newer = FileUtils.isFileNewer(this.file, last);
/*     */         
/* 434 */         long length = this.file.length();
/* 435 */         if (length < position) {
/*     */           
/* 437 */           this.listener.fileRotated();
/*     */ 
/*     */           
/* 440 */           try (RandomAccessFile save = reader) {
/* 441 */             reader = new RandomAccessFile(this.file, "r");
/*     */ 
/*     */             
/*     */             try {
/* 445 */               readLines(save);
/* 446 */             } catch (IOException ioe) {
/* 447 */               this.listener.handle(ioe);
/*     */             } 
/* 449 */             position = 0L;
/* 450 */           } catch (FileNotFoundException e) {
/*     */             
/* 452 */             this.listener.fileNotFound();
/* 453 */             Thread.sleep(this.delayMillis);
/*     */           } 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 459 */         if (length > position) {
/*     */           
/* 461 */           position = readLines(reader);
/* 462 */           last = FileUtils.lastModified(this.file);
/* 463 */         } else if (newer) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 468 */           position = 0L;
/* 469 */           reader.seek(position);
/*     */ 
/*     */           
/* 472 */           position = readLines(reader);
/* 473 */           last = FileUtils.lastModified(this.file);
/*     */         } 
/* 475 */         if (this.reOpen && reader != null) {
/* 476 */           reader.close();
/*     */         }
/* 478 */         Thread.sleep(this.delayMillis);
/* 479 */         if (getRun() && this.reOpen) {
/* 480 */           reader = new RandomAccessFile(this.file, "r");
/* 481 */           reader.seek(position);
/*     */         } 
/*     */       } 
/* 484 */     } catch (InterruptedException e) {
/* 485 */       Thread.currentThread().interrupt();
/* 486 */       this.listener.handle(e);
/* 487 */     } catch (Exception e) {
/* 488 */       this.listener.handle(e);
/*     */     } finally {
/*     */       try {
/* 491 */         if (reader != null) {
/* 492 */           reader.close();
/*     */         }
/* 494 */       } catch (IOException e) {
/* 495 */         this.listener.handle(e);
/*     */       } 
/* 497 */       stop();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 505 */     this.run = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long readLines(RandomAccessFile reader) throws IOException {
/* 516 */     try (ByteArrayOutputStream lineBuf = new ByteArrayOutputStream(64)) {
/* 517 */       long pos = reader.getFilePointer();
/* 518 */       long rePos = pos;
/*     */       
/* 520 */       boolean seenCR = false; int num;
/* 521 */       while (getRun() && (num = reader.read(this.inbuf)) != -1) {
/* 522 */         for (int i = 0; i < num; i++) {
/* 523 */           byte ch = this.inbuf[i];
/* 524 */           switch (ch) {
/*     */             case 10:
/* 526 */               seenCR = false;
/* 527 */               this.listener.handle(new String(lineBuf.toByteArray(), this.charset));
/* 528 */               lineBuf.reset();
/* 529 */               rePos = pos + i + 1L;
/*     */               break;
/*     */             case 13:
/* 532 */               if (seenCR) {
/* 533 */                 lineBuf.write(13);
/*     */               }
/* 535 */               seenCR = true;
/*     */               break;
/*     */             default:
/* 538 */               if (seenCR) {
/* 539 */                 seenCR = false;
/* 540 */                 this.listener.handle(new String(lineBuf.toByteArray(), this.charset));
/* 541 */                 lineBuf.reset();
/* 542 */                 rePos = pos + i + 1L;
/*     */               } 
/* 544 */               lineBuf.write(ch); break;
/*     */           } 
/*     */         } 
/* 547 */         pos = reader.getFilePointer();
/*     */       } 
/*     */       
/* 550 */       reader.seek(rePos);
/*     */       
/* 552 */       if (this.listener instanceof TailerListenerAdapter) {
/* 553 */         ((TailerListenerAdapter)this.listener).endOfFileReached();
/*     */       }
/*     */       
/* 556 */       return rePos;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\Tailer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */