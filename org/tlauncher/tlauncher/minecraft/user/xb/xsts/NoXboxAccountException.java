/*   */ package org.tlauncher.tlauncher.minecraft.user.xb.xsts;
/*   */ 
/*   */ public class NoXboxAccountException extends XSTSAuthenticationException {
/*   */   public String getShortKey() {
/* 5 */     return super.getShortKey() + ".no_xbox_account";
/*   */   }
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\xb\xsts\NoXboxAccountException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */