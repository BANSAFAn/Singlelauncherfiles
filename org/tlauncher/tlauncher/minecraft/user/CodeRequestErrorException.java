/*    */ package org.tlauncher.tlauncher.minecraft.user;
/*    */ 
/*    */ public class CodeRequestErrorException extends MicrosoftOAuthCodeRequestException {
/*    */   public CodeRequestErrorException(String error, String errorDescription) {
/*  5 */     super(format(error, errorDescription));
/*    */   }
/*    */   
/*    */   private static String format(String error, String errorDescription) {
/*  9 */     if (errorDescription != null)
/* 10 */       return error + ": " + errorDescription; 
/* 11 */     return error;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\CodeRequestErrorException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */