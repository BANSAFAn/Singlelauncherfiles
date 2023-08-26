/*    */ package org.tlauncher.tlauncher.minecraft.user;
/*    */ 
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class InvalidStatusCodeException extends InvalidResponseException {
/*    */   private final int statusCode;
/*    */   
/*    */   public InvalidStatusCodeException(int statusCode, String response) {
/*  9 */     super(response, format(statusCode, response));
/* 10 */     this.statusCode = statusCode;
/*    */   }
/*    */   
/*    */   private static String format(int statusCode, String response) {
/* 14 */     if (StringUtils.isEmpty(response))
/* 15 */       return String.valueOf(statusCode); 
/* 16 */     return statusCode + ": \"" + response + "\"";
/*    */   }
/*    */   
/*    */   public int getStatusCode() {
/* 20 */     return this.statusCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\InvalidStatusCodeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */