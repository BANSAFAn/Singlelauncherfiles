/*    */ package org.tlauncher.tlauncher.minecraft.auth;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ import org.tlauncher.tlauncher.exceptions.auth.AuthenticatorException;
/*    */ 
/*    */ public class FreeAuthentication
/*    */   extends Authenticator {
/*    */   public FreeAuthentication(Account account) {
/* 10 */     super(account);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void pass() throws AuthenticatorException {
/* 15 */     if (Objects.isNull(this.account.getUUID())) {
/* 16 */       this.account.setUUID(UUIDTypeAdapter.fromUUID(UUID.randomUUID()));
/* 17 */       this.account.setUserID(this.account.getUUID());
/* 18 */       this.account.setDisplayName(this.account.getUsername());
/*    */     } else {
/* 20 */       this.account.setDisplayName(this.account.getUsername());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\auth\FreeAuthentication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */