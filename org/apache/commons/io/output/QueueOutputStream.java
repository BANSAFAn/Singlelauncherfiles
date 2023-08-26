/*    */ package org.apache.commons.io.output;
/*    */ 
/*    */ import java.io.InterruptedIOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import java.util.concurrent.LinkedBlockingQueue;
/*    */ import org.apache.commons.io.input.QueueInputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class QueueOutputStream
/*    */   extends OutputStream
/*    */ {
/*    */   private final BlockingQueue<Integer> blockingQueue;
/*    */   
/*    */   public QueueOutputStream() {
/* 63 */     this(new LinkedBlockingQueue<>());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public QueueOutputStream(BlockingQueue<Integer> blockingQueue) {
/* 72 */     this.blockingQueue = Objects.<BlockingQueue<Integer>>requireNonNull(blockingQueue, "blockingQueue");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public QueueInputStream newQueueInputStream() {
/* 82 */     return new QueueInputStream(this.blockingQueue);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(int b) throws InterruptedIOException {
/*    */     try {
/* 93 */       this.blockingQueue.put(Integer.valueOf(0xFF & b));
/* 94 */     } catch (InterruptedException e) {
/* 95 */       Thread.currentThread().interrupt();
/* 96 */       InterruptedIOException interruptedIoException = new InterruptedIOException();
/* 97 */       interruptedIoException.initCause(e);
/* 98 */       throw interruptedIoException;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\output\QueueOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */