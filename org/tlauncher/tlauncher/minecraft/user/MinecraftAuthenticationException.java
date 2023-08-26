/*    */ package org.tlauncher.tlauncher.minecraft.user;
/*    */ 
/*    */ public abstract class MinecraftAuthenticationException
/*    */   extends Exception {
/*    */   public MinecraftAuthenticationException() {}
/*    */   
/*    */   public MinecraftAuthenticationException(String message) {
/*  8 */     super(message);
/*    */   }
/*    */   
/*    */   public MinecraftAuthenticationException(String message, Throwable cause) {
/* 12 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public MinecraftAuthenticationException(Throwable cause) {
/* 16 */     super(cause);
/*    */   }
/*    */   
/*    */   public abstract String getShortKey();
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\MinecraftAuthenticationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */