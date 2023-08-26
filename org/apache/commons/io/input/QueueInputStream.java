/*    */ package org.apache.commons.io.input;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import java.util.concurrent.LinkedBlockingQueue;
/*    */ import org.apache.commons.io.output.QueueOutputStream;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class QueueInputStream
/*    */   extends InputStream
/*    */ {
/*    */   private final BlockingQueue<Integer> blockingQueue;
/*    */   
/*    */   public QueueInputStream() {
/* 65 */     this(new LinkedBlockingQueue<>());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public QueueInputStream(BlockingQueue<Integer> blockingQueue) {
/* 74 */     this.blockingQueue = Objects.<BlockingQueue<Integer>>requireNonNull(blockingQueue, "blockingQueue");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public QueueOutputStream newQueueOutputStream() {
/* 84 */     return new QueueOutputStream(this.blockingQueue);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int read() {
/* 94 */     Integer value = this.blockingQueue.poll();
/* 95 */     return (value == null) ? -1 : (0xFF & value.intValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\QueueInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */