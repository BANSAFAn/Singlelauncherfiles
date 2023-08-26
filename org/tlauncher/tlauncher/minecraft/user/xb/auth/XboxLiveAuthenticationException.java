/*    */ package org.tlauncher.tlauncher.minecraft.user.xb.auth;
/*    */ 
/*    */ import org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException;
/*    */ 
/*    */ public class XboxLiveAuthenticationException
/*    */   extends MinecraftAuthenticationException {
/*    */   public XboxLiveAuthenticationException(Throwable cause) {
/*  8 */     super(cause);
/*    */   }
/*    */   
/*    */   public String getShortKey() {
/* 12 */     return "xbox_live_authentication";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\xb\auth\XboxLiveAuthenticationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */