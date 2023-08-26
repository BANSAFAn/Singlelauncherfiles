/*    */ package org.apache.http.impl.execchain;
/*    */ 
/*    */ import java.io.InterruptedIOException;
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ @Immutable
/*    */ public class RequestAbortedException
/*    */   extends InterruptedIOException
/*    */ {
/*    */   private static final long serialVersionUID = 4973849966012490112L;
/*    */   
/*    */   public RequestAbortedException(String message) {
/* 45 */     super(message);
/*    */   }
/*    */   
/*    */   public RequestAbortedException(String message, Throwable cause) {
/* 49 */     super(message);
/* 50 */     if (cause != null)
/* 51 */       initCause(cause); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\execchain\RequestAbortedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */