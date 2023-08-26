/*    */ package org.tlauncher.tlauncher.minecraft.user;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ import org.tlauncher.tlauncher.minecraft.user.mcsauth.MinecraftServicesToken;
/*    */ import org.tlauncher.tlauncher.minecraft.user.preq.MinecraftOAuthProfile;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MinecraftUser
/*    */   extends User
/*    */ {
/*    */   public static final String TYPE = "minecraft";
/*    */   private MinecraftOAuthProfile profile;
/*    */   private MicrosoftOAuthToken microsoftToken;
/*    */   private MinecraftServicesToken minecraftToken;
/*    */   
/*    */   public MinecraftUser(MinecraftOAuthProfile profile, MicrosoftOAuthToken microsoftToken, MinecraftServicesToken minecraftToken) {
/* 20 */     setPayload(profile, microsoftToken, minecraftToken);
/*    */   }
/*    */   
/*    */   public String getUsername() {
/* 24 */     return this.profile.getName();
/*    */   }
/*    */   
/*    */   public String getDisplayName() {
/* 28 */     return this.profile.getName();
/*    */   }
/*    */   
/*    */   public UUID getUUID() {
/* 32 */     return this.profile.getId();
/*    */   }
/*    */   
/*    */   public MicrosoftOAuthToken getMicrosoftToken() {
/* 36 */     return this.microsoftToken;
/*    */   }
/*    */   
/*    */   void setMicrosoftToken(MicrosoftOAuthToken microsoftToken) {
/* 40 */     this.microsoftToken = Objects.<MicrosoftOAuthToken>requireNonNull(microsoftToken, "microsoftToken");
/*    */   }
/*    */   
/*    */   public MinecraftServicesToken getMinecraftToken() {
/* 44 */     return this.minecraftToken;
/*    */   }
/*    */   
/*    */   void setMinecraftToken(MinecraftServicesToken minecraftToken) {
/* 48 */     this.minecraftToken = Objects.<MinecraftServicesToken>requireNonNull(minecraftToken, "minecraftToken");
/*    */   }
/*    */   
/*    */   void setPayload(MinecraftOAuthProfile profile, MicrosoftOAuthToken microsoftToken, MinecraftServicesToken minecraftToken) {
/* 52 */     setProfile(profile);
/* 53 */     setMicrosoftToken(microsoftToken);
/* 54 */     setMinecraftToken(minecraftToken);
/*    */   }
/*    */   
/*    */   void setProfile(MinecraftOAuthProfile profile) {
/* 58 */     this.profile = Objects.<MinecraftOAuthProfile>requireNonNull(profile, "profile");
/*    */   }
/*    */   
/*    */   public String getType() {
/* 62 */     return "minecraft";
/*    */   }
/*    */   
/*    */   protected boolean equals(User user) {
/* 66 */     return (user != null && this.profile.getId().equals(((MinecraftUser)user).profile.getId()));
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 70 */     int result = this.profile.hashCode();
/* 71 */     result = 31 * result + this.microsoftToken.hashCode();
/* 72 */     return 31 * result + this.minecraftToken.hashCode();
/*    */   }
/*    */   
/*    */   public LoginCredentials getLoginCredentials() {
/* 76 */     return new LoginCredentials(this.profile
/* 77 */         .getName(), this.minecraftToken
/* 78 */         .getAccessToken(), "{}", this.profile
/*    */         
/* 80 */         .getName(), this.profile
/* 81 */         .getId(), "mojang", this.profile
/*    */         
/* 83 */         .getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\MinecraftUser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */