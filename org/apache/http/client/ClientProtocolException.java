/*    */ package org.apache.http.client;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ 
/*    */ @Immutable
/*    */ public class ClientProtocolException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = -5596590843227115865L;
/*    */   
/*    */   public ClientProtocolException() {}
/*    */   
/*    */   public ClientProtocolException(String s) {
/* 48 */     super(s);
/*    */   }
/*    */   
/*    */   public ClientProtocolException(Throwable cause) {
/* 52 */     initCause(cause);
/*    */   }
/*    */   
/*    */   public ClientProtocolException(String message, Throwable cause) {
/* 56 */     super(message);
/* 57 */     initCause(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\ClientProtocolException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */