/*    */ package org.tlauncher.tlauncher.minecraft.user.preq;
/*    */ 
/*    */ import org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException;
/*    */ 
/*    */ public class MinecraftProfileRequestException
/*    */   extends MinecraftAuthenticationException {
/*    */   public MinecraftProfileRequestException(Throwable cause) {
/*  8 */     super(cause);
/*    */   }
/*    */   
/*    */   public String getShortKey() {
/* 12 */     return "minecraft_profile_request";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\preq\MinecraftProfileRequestException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */