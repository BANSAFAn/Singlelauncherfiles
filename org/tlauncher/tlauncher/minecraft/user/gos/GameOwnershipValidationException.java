/*    */ package org.tlauncher.tlauncher.minecraft.user.gos;
/*    */ 
/*    */ import org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException;
/*    */ 
/*    */ public class GameOwnershipValidationException
/*    */   extends MinecraftAuthenticationException {
/*    */   private final boolean isKnownNotToOwn;
/*    */   
/*    */   public GameOwnershipValidationException(Throwable cause) {
/* 10 */     super(cause);
/* 11 */     this.isKnownNotToOwn = false;
/*    */   }
/*    */   
/*    */   public GameOwnershipValidationException(String message) {
/* 15 */     super(message);
/* 16 */     this.isKnownNotToOwn = true;
/*    */   }
/*    */   
/*    */   public boolean isKnownNotToOwn() {
/* 20 */     return this.isKnownNotToOwn;
/*    */   }
/*    */   
/*    */   public String getShortKey() {
/* 24 */     return "game_ownership";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\gos\GameOwnershipValidationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */