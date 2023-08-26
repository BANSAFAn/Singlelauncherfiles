/*    */ package org.tlauncher.tlauncher.minecraft.user;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Locale;
/*    */ import java.util.UUID;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.apache.commons.lang3.builder.ToStringBuilder;
/*    */ import org.apache.commons.lang3.builder.ToStringStyle;
/*    */ import org.tlauncher.tlauncher.minecraft.auth.UUIDTypeAdapter;
/*    */ import org.tlauncher.util.StringUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LoginCredentials
/*    */ {
/*    */   private final String username;
/*    */   private final String session;
/*    */   private final String accessToken;
/*    */   private final String playerName;
/*    */   private final String userType;
/*    */   private final String profileName;
/*    */   private final String properties;
/*    */   private final UUID uuid;
/*    */   
/*    */   LoginCredentials(String username, String accessToken, String properties, String playerName, UUID uuid, String userType, String profileName) {
/* 31 */     this.username = StringUtil.requireNotBlank(username, "username");
/* 32 */     this.accessToken = StringUtil.requireNotBlank(accessToken, "accessToken");
/* 33 */     this.properties = StringUtils.isBlank(properties) ? "{}" : properties;
/* 34 */     this.playerName = StringUtil.requireNotBlank(playerName, "playerName");
/* 35 */     this.uuid = uuid;
/* 36 */     this.userType = StringUtil.requireNotBlank(userType, "userType");
/* 37 */     this.profileName = StringUtil.requireNotBlank(profileName, "profileName");
/* 38 */     this.session = String.format(Locale.ROOT, "token:%s:%s", new Object[] { accessToken, UUIDTypeAdapter.fromUUID(uuid) });
/*    */   }
/*    */   
/*    */   public String getUsername() {
/* 42 */     return this.username;
/*    */   }
/*    */   
/*    */   public String getSession() {
/* 46 */     return this.session;
/*    */   }
/*    */   
/*    */   public String getAccessToken() {
/* 50 */     return this.accessToken;
/*    */   }
/*    */   
/*    */   public String getPlayerName() {
/* 54 */     return this.playerName;
/*    */   }
/*    */   
/*    */   public String getUserType() {
/* 58 */     return this.userType;
/*    */   }
/*    */   
/*    */   public String getProfileName() {
/* 62 */     return this.profileName;
/*    */   }
/*    */   
/*    */   public String getProperties() {
/* 66 */     return this.properties;
/*    */   }
/*    */   
/*    */   public UUID getUuid() {
/* 70 */     return this.uuid;
/*    */   }
/*    */   
/*    */   public LinkedHashMap<String, String> map() {
/* 74 */     return new LinkedHashMap<String, String>() {
/*    */       
/*    */       };
/*    */   }
/*    */   
/*    */   public String toString() {
/* 80 */     return (new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE))
/* 81 */       .append("session", this.session)
/* 82 */       .append("accessToken", this.accessToken)
/* 83 */       .append("playerName", this.playerName)
/* 84 */       .append("userType", this.userType)
/* 85 */       .append("profileName", this.profileName)
/* 86 */       .append("properties", this.properties)
/* 87 */       .append("uuid", this.uuid)
/* 88 */       .build();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\LoginCredentials.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */