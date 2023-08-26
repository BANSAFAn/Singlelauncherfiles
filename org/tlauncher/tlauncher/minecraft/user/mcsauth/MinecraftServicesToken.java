/*    */ package org.tlauncher.tlauncher.minecraft.user.mcsauth;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import org.tlauncher.tlauncher.minecraft.user.preq.Validatable;
/*    */ 
/*    */ public class MinecraftServicesToken
/*    */   implements Validatable
/*    */ {
/*    */   private final Instant createdAt;
/*    */   private String accessToken;
/*    */   private int expiresIn;
/*    */   
/*    */   public MinecraftServicesToken(String accessToken, int expiresIn) {
/* 14 */     this.accessToken = accessToken;
/* 15 */     this.createdAt = Instant.now();
/* 16 */     this.expiresIn = expiresIn;
/*    */   }
/*    */   
/*    */   public MinecraftServicesToken(String accessToken, Instant expiresAt) {
/* 20 */     this.accessToken = accessToken;
/* 21 */     this.createdAt = expiresAt.minusSeconds(3600L);
/* 22 */     this.expiresIn = 3600;
/*    */   }
/*    */   
/*    */   public MinecraftServicesToken() {
/* 26 */     this.createdAt = Instant.now();
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 30 */     if (this == o)
/* 31 */       return true; 
/* 32 */     if (o == null || getClass() != o.getClass())
/* 33 */       return false; 
/* 34 */     MinecraftServicesToken that = (MinecraftServicesToken)o;
/* 35 */     if (this.expiresIn != that.expiresIn)
/* 36 */       return false; 
/* 37 */     return this.accessToken.equals(that.accessToken);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 41 */     int result = this.accessToken.hashCode();
/* 42 */     return 31 * result + this.expiresIn;
/*    */   }
/*    */   
/*    */   public String getAccessToken() {
/* 46 */     return this.accessToken;
/*    */   }
/*    */   
/*    */   public int getExpiresIn() {
/* 50 */     return this.expiresIn;
/*    */   }
/*    */   
/*    */   public Instant calculateExpiryTime() {
/* 54 */     return this.createdAt.plusSeconds(this.expiresIn);
/*    */   }
/*    */   
/*    */   public boolean isExpired() {
/* 58 */     return Instant.now().isAfter(calculateExpiryTime());
/*    */   }
/*    */   
/*    */   public String toString() {
/* 62 */     return "MinecraftServicesToken{accessToken='" + this.accessToken + '\'' + ", expiresIn=" + this.expiresIn + '}';
/*    */   }
/*    */   
/*    */   public void validate() {
/* 66 */     Validatable.notEmpty(this.accessToken, "accessToken");
/* 67 */     Validatable.notNegative(this.expiresIn, "expiresIn");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\mcsauth\MinecraftServicesToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */