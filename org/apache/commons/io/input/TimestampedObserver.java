/*    */ package org.apache.commons.io.input;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.time.Duration;
/*    */ import java.time.Instant;
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
/*    */ public class TimestampedObserver
/*    */   extends ObservableInputStream.Observer
/*    */ {
/*    */   private volatile Instant closeInstant;
/* 46 */   private final Instant openInstant = Instant.now();
/*    */ 
/*    */   
/*    */   public void closed() throws IOException {
/* 50 */     this.closeInstant = Instant.now();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Instant getCloseInstant() {
/* 59 */     return this.closeInstant;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Duration getOpenToCloseDuration() {
/* 68 */     return Duration.between(this.openInstant, this.closeInstant);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Duration getOpenToNowDuration() {
/* 77 */     return Duration.between(this.openInstant, Instant.now());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Instant getOpenInstant() {
/* 86 */     return this.openInstant;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 91 */     return "TimestampedObserver [openInstant=" + this.openInstant + ", closeInstant=" + this.closeInstant + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\TimestampedObserver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */