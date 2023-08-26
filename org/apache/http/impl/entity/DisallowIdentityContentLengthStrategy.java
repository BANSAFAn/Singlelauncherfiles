/*    */ package org.apache.http.impl.entity;
/*    */ 
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpMessage;
/*    */ import org.apache.http.ProtocolException;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.entity.ContentLengthStrategy;
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
/*    */ public class DisallowIdentityContentLengthStrategy
/*    */   implements ContentLengthStrategy
/*    */ {
/* 45 */   public static final DisallowIdentityContentLengthStrategy INSTANCE = new DisallowIdentityContentLengthStrategy(new LaxContentLengthStrategy(0));
/*    */ 
/*    */   
/*    */   private final ContentLengthStrategy contentLengthStrategy;
/*    */ 
/*    */   
/*    */   public DisallowIdentityContentLengthStrategy(ContentLengthStrategy contentLengthStrategy) {
/* 52 */     this.contentLengthStrategy = contentLengthStrategy;
/*    */   }
/*    */ 
/*    */   
/*    */   public long determineLength(HttpMessage message) throws HttpException {
/* 57 */     long result = this.contentLengthStrategy.determineLength(message);
/* 58 */     if (result == -1L) {
/* 59 */       throw new ProtocolException("Identity transfer encoding cannot be used");
/*    */     }
/* 61 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\entity\DisallowIdentityContentLengthStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */