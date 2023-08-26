/*    */ package org.tlauncher.tlauncher.minecraft.user;
/*    */ 
/*    */ public class MicrosoftOAuthCodeRequestException
/*    */   extends MinecraftAuthenticationException {
/*    */   public MicrosoftOAuthCodeRequestException(String message) {
/*  6 */     super(message);
/*    */   }
/*    */   
/*    */   public MicrosoftOAuthCodeRequestException(String message, Throwable cause) {
/* 10 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public String getShortKey() {
/* 14 */     return "microsoft_oauth_code_request";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\MicrosoftOAuthCodeRequestException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */