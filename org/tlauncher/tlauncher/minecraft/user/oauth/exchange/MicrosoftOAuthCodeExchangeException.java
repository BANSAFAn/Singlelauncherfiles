/*    */ package org.tlauncher.tlauncher.minecraft.user.oauth.exchange;
/*    */ 
/*    */ import org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException;
/*    */ 
/*    */ public class MicrosoftOAuthCodeExchangeException
/*    */   extends MinecraftAuthenticationException {
/*    */   public MicrosoftOAuthCodeExchangeException(Throwable cause) {
/*  8 */     super(cause);
/*    */   }
/*    */   
/*    */   public String getShortKey() {
/* 12 */     return "microsoft_oauth_code_exchange";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\oauth\exchange\MicrosoftOAuthCodeExchangeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */