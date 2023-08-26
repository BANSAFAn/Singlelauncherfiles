/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.commons.io.function.IOConsumer;
/*     */ import org.apache.commons.io.function.IOFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThresholdingOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private static final IOFunction<ThresholdingOutputStream, OutputStream> NOOP_OS_GETTER = os -> NullOutputStream.NULL_OUTPUT_STREAM;
/*     */   private final int threshold;
/*     */   private final IOConsumer<ThresholdingOutputStream> thresholdConsumer;
/*     */   private final IOFunction<ThresholdingOutputStream, OutputStream> outputStreamGetter;
/*     */   private long written;
/*     */   private boolean thresholdExceeded;
/*     */   
/*     */   public ThresholdingOutputStream(int threshold) {
/*  76 */     this(threshold, IOConsumer.noop(), NOOP_OS_GETTER);
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
/*     */   public ThresholdingOutputStream(int threshold, IOConsumer<ThresholdingOutputStream> thresholdConsumer, IOFunction<ThresholdingOutputStream, OutputStream> outputStreamGetter) {
/*  89 */     this.threshold = threshold;
/*  90 */     this.thresholdConsumer = (thresholdConsumer == null) ? IOConsumer.noop() : thresholdConsumer;
/*  91 */     this.outputStreamGetter = (outputStreamGetter == null) ? NOOP_OS_GETTER : outputStreamGetter;
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
/*     */   protected void checkThreshold(int count) throws IOException {
/* 103 */     if (!this.thresholdExceeded && this.written + count > this.threshold) {
/* 104 */       this.thresholdExceeded = true;
/* 105 */       thresholdReached();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 117 */       flush();
/* 118 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/* 121 */     getStream().close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 132 */     getStream().flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getByteCount() {
/* 141 */     return this.written;
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
/*     */   protected OutputStream getStream() throws IOException {
/* 153 */     return (OutputStream)this.outputStreamGetter.apply(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getThreshold() {
/* 162 */     return this.threshold;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isThresholdExceeded() {
/* 171 */     return (this.written > this.threshold);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void resetByteCount() {
/* 179 */     this.thresholdExceeded = false;
/* 180 */     this.written = 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setByteCount(long count) {
/* 191 */     this.written = count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void thresholdReached() throws IOException {
/* 201 */     this.thresholdConsumer.accept(this);
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
/*     */   public void write(byte[] b) throws IOException {
/* 214 */     checkThreshold(b.length);
/* 215 */     getStream().write(b);
/* 216 */     this.written += b.length;
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
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/* 231 */     checkThreshold(len);
/* 232 */     getStream().write(b, off, len);
/* 233 */     this.written += len;
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
/*     */   public void write(int b) throws IOException {
/* 246 */     checkThreshold(1);
/* 247 */     getStream().write(b);
/* 248 */     this.written++;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\output\ThresholdingOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */