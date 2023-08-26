/*    */ package org.tlauncher.tlauncher.minecraft.user;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import java.util.Objects;
/*    */ import org.tlauncher.tlauncher.minecraft.user.preq.Validatable;
/*    */ 
/*    */ public class MicrosoftOAuthToken
/*    */   implements Validatable
/*    */ {
/*    */   private final Instant createdAt;
/*    */   private String accessToken;
/*    */   private String refreshToken;
/*    */   private int expiresIn;
/*    */   
/*    */   public MicrosoftOAuthToken(String accessToken, String refreshToken, int expiresIn) {
/* 16 */     this.accessToken = accessToken;
/* 17 */     this.refreshToken = refreshToken;
/* 18 */     this.createdAt = Instant.now();
/* 19 */     this.expiresIn = expiresIn;
/*    */   }
/*    */   
/*    */   public MicrosoftOAuthToken(String accessToken, String refreshToken, Instant expiresAt) {
/* 23 */     this.accessToken = accessToken;
/* 24 */     this.refreshToken = refreshToken;
/* 25 */     this.createdAt = expiresAt.minusSeconds(3600L);
/* 26 */     this.expiresIn = 3600;
/*    */   }
/*    */   
/*    */   public MicrosoftOAuthToken() {
/* 30 */     this.createdAt = Instant.now();
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 34 */     if (this == o)
/* 35 */       return true; 
/* 36 */     if (o == null || getClass() != o.getClass())
/* 37 */       return false; 
/* 38 */     MicrosoftOAuthToken that = (MicrosoftOAuthToken)o;
/* 39 */     if (this.expiresIn != that.expiresIn)
/* 40 */       return false; 
/* 41 */     if (!Objects.equals(this.accessToken, that.accessToken))
/* 42 */       return false; 
/* 43 */     return Objects.equals(this.refreshToken, that.refreshToken);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 47 */     int result = (this.accessToken != null) ? this.accessToken.hashCode() : 0;
/* 48 */     result = 31 * result + ((this.refreshToken != null) ? this.refreshToken.hashCode() : 0);
/* 49 */     return 31 * result + this.expiresIn;
/*    */   }
/*    */   
/*    */   public String getAccessToken() {
/* 53 */     return this.accessToken;
/*    */   }
/*    */   
/*    */   public String getRefreshToken() {
/* 57 */     return this.refreshToken;
/*    */   }
/*    */   
/*    */   public Instant calculateExpiryTime() {
/* 61 */     return this.createdAt.plusSeconds(this.expiresIn);
/*    */   }
/*    */   
/*    */   public boolean isExpired() {
/* 65 */     return Instant.now().isAfter(calculateExpiryTime());
/*    */   }
/*    */   
/*    */   public String toString() {
/* 69 */     return "MicrosoftOAuthToken{accessToken='" + this.accessToken + '\'' + ", refreshToken='" + this.refreshToken + '\'' + ", expiresIn=" + this.expiresIn + ", createdAt=" + this.createdAt + '}';
/*    */   }
/*    */   
/*    */   public void validate() {
/* 73 */     Validatable.notNull(this.accessToken, "accessToken");
/*    */ 
/*    */     
/* 76 */     Validatable.notNull(this.createdAt, "createdAt");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\MicrosoftOAuthToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */