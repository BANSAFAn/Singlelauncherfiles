/*     */ package org.tlauncher.tlauncher.minecraft.auth;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.gson.annotations.Expose;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.tlauncher.tlauncher.minecraft.user.MicrosoftOAuthToken;
/*     */ import org.tlauncher.tlauncher.minecraft.user.mcsauth.MinecraftServicesToken;
/*     */ 
/*     */ public class Account {
/*     */   private String username;
/*     */   private String userID;
/*     */   private String displayName;
/*     */   private String password;
/*     */   
/*  20 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof Account)) return false;  Account other = (Account)o; if (!other.canEqual(this)) return false;  Object this$username = getUsername(), other$username = other.getUsername(); if ((this$username == null) ? (other$username != null) : !this$username.equals(other$username)) return false;  Object this$userID = getUserID(), other$userID = other.getUserID(); if ((this$userID == null) ? (other$userID != null) : !this$userID.equals(other$userID)) return false;  Object this$displayName = getDisplayName(), other$displayName = other.getDisplayName(); if ((this$displayName == null) ? (other$displayName != null) : !this$displayName.equals(other$displayName)) return false;  Object this$password = getPassword(), other$password = other.getPassword(); if ((this$password == null) ? (other$password != null) : !this$password.equals(other$password)) return false;  Object this$accessToken = getAccessToken(), other$accessToken = other.getAccessToken(); if ((this$accessToken == null) ? (other$accessToken != null) : !this$accessToken.equals(other$accessToken)) return false;  Object this$microsoftOAuthToken = getMicrosoftOAuthToken(), other$microsoftOAuthToken = other.getMicrosoftOAuthToken(); if ((this$microsoftOAuthToken == null) ? (other$microsoftOAuthToken != null) : !this$microsoftOAuthToken.equals(other$microsoftOAuthToken)) return false;  Object this$minecraftServicesToken = getMinecraftServicesToken(), other$minecraftServicesToken = other.getMinecraftServicesToken(); if ((this$minecraftServicesToken == null) ? (other$minecraftServicesToken != null) : !this$minecraftServicesToken.equals(other$minecraftServicesToken)) return false;  Object this$uuid = getUUID(), other$uuid = other.getUUID(); if ((this$uuid == null) ? (other$uuid != null) : !this$uuid.equals(other$uuid)) return false;  Object<Map<String, String>> this$userProperties = (Object<Map<String, String>>)this.userProperties, other$userProperties = (Object<Map<String, String>>)other.userProperties; if ((this$userProperties == null) ? (other$userProperties != null) : !this$userProperties.equals(other$userProperties)) return false;  Object this$type = getType(), other$type = other.getType(); if ((this$type == null) ? (other$type != null) : !this$type.equals(other$type)) return false;  if (isPremiumAccount() != other.isPremiumAccount()) return false;  if (!Arrays.deepEquals((Object[])getProfiles(), (Object[])other.getProfiles())) return false;  Object this$selectedProfile = this.selectedProfile, other$selectedProfile = other.selectedProfile; if ((this$selectedProfile == null) ? (other$selectedProfile != null) : !this$selectedProfile.equals(other$selectedProfile)) return false;  Object this$user = getUser(), other$user = other.getUser(); return !((this$user == null) ? (other$user != null) : !this$user.equals(other$user)); } private String accessToken; private MicrosoftOAuthToken microsoftOAuthToken; private MinecraftServicesToken minecraftServicesToken; private String uuid; private List<Map<String, String>> userProperties; protected boolean canEqual(Object other) { return other instanceof Account; } public int hashCode() { int PRIME = 59; result = 1; Object $username = getUsername(); result = result * 59 + (($username == null) ? 43 : $username.hashCode()); Object $userID = getUserID(); result = result * 59 + (($userID == null) ? 43 : $userID.hashCode()); Object $displayName = getDisplayName(); result = result * 59 + (($displayName == null) ? 43 : $displayName.hashCode()); Object $password = getPassword(); result = result * 59 + (($password == null) ? 43 : $password.hashCode()); Object $accessToken = getAccessToken(); result = result * 59 + (($accessToken == null) ? 43 : $accessToken.hashCode()); Object $microsoftOAuthToken = getMicrosoftOAuthToken(); result = result * 59 + (($microsoftOAuthToken == null) ? 43 : $microsoftOAuthToken.hashCode()); Object $minecraftServicesToken = getMinecraftServicesToken(); result = result * 59 + (($minecraftServicesToken == null) ? 43 : $minecraftServicesToken.hashCode()); Object $uuid = getUUID(); result = result * 59 + (($uuid == null) ? 43 : $uuid.hashCode()); Object<Map<String, String>> $userProperties = (Object<Map<String, String>>)this.userProperties; result = result * 59 + (($userProperties == null) ? 43 : $userProperties.hashCode()); Object $type = getType(); result = result * 59 + (($type == null) ? 43 : $type.hashCode()); result = result * 59 + (isPremiumAccount() ? 79 : 97); result = result * 59 + Arrays.deepHashCode((Object[])getProfiles()); Object $selectedProfile = this.selectedProfile; result = result * 59 + (($selectedProfile == null) ? 43 : $selectedProfile.hashCode()); Object $user = getUser(); return result * 59 + (($user == null) ? 43 : $user.hashCode()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   private AccountType type = AccountType.TLAUNCHER; @Expose
/*     */   private boolean premiumAccount; private GameProfile[] profiles; private GameProfile selectedProfile; private User user;
/*     */   
/*     */   public Account(String username) {
/*  46 */     this();
/*  47 */     setUsername(username);
/*     */   }
/*     */   
/*     */   public Account(Map<String, Object> map) {
/*  51 */     this();
/*  52 */     fillFromMap(map);
/*     */   }
/*     */   
/*     */   public String getUsername() {
/*  56 */     return this.username;
/*     */   }
/*     */   
/*     */   public boolean hasUsername() {
/*  60 */     return (this.username != null);
/*     */   }
/*     */   
/*     */   public void setUsername(String username) {
/*  64 */     this.username = username;
/*     */   }
/*     */   
/*     */   public String getUserID() {
/*  68 */     return this.userID;
/*     */   }
/*     */   
/*     */   public void setUserID(String userID) {
/*  72 */     this.userID = userID;
/*     */   }
/*     */   
/*     */   public String getDisplayName() {
/*  76 */     return (this.displayName == null) ? this.username : this.displayName;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/*  80 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/*  84 */     this.password = password;
/*     */   }
/*     */   
/*     */   public String getAccessToken() {
/*  88 */     if (this.type.equals(AccountType.MICROSOFT) && Objects.nonNull(this.minecraftServicesToken))
/*  89 */       return this.minecraftServicesToken.getAccessToken(); 
/*  90 */     return this.accessToken;
/*     */   }
/*     */   
/*     */   public void setAccessToken(String accessToken) {
/*  94 */     if ("null".equals(accessToken))
/*  95 */       accessToken = null; 
/*  96 */     this.accessToken = accessToken;
/*     */   }
/*     */   
/*     */   public String getUUID() {
/* 100 */     return this.uuid;
/*     */   }
/*     */   
/*     */   public void setUUID(String uuid) {
/* 104 */     this.uuid = uuid;
/*     */   }
/*     */   
/*     */   public GameProfile[] getProfiles() {
/* 108 */     return this.profiles;
/*     */   }
/*     */   
/*     */   public void setProfiles(GameProfile[] p) {
/* 112 */     this.profiles = p;
/*     */   }
/*     */   
/*     */   public GameProfile getProfile() {
/* 116 */     return (this.selectedProfile != null) ? this.selectedProfile : GameProfile.DEFAULT_PROFILE;
/*     */   }
/*     */   
/*     */   public void setProfile(GameProfile p) {
/* 120 */     this.selectedProfile = p;
/*     */   }
/*     */   
/*     */   public void setDisplayName(String displayName) {
/* 124 */     this.displayName = displayName;
/*     */   }
/*     */   
/*     */   public User getUser() {
/* 128 */     return this.user;
/*     */   }
/*     */   
/*     */   public void setUser(User user) {
/* 132 */     this.user = user;
/*     */   }
/*     */   
/*     */   public Map<String, List<String>> getProperties() {
/* 136 */     Map<String, List<String>> map = new HashMap<>();
/* 137 */     List<UserProperty> list = new ArrayList<>();
/*     */     
/* 139 */     if (this.userProperties != null)
/* 140 */       for (Map<String, String> properties : this.userProperties) {
/* 141 */         list.add(new UserProperty(properties.get("name"), properties.get("value")));
/*     */       } 
/* 143 */     if (this.user != null && this.user.getProperties() != null)
/* 144 */       for (Map<String, String> properties : this.user.getProperties()) {
/* 145 */         list.add(new UserProperty(properties.get("name"), properties.get("value")));
/*     */       } 
/* 147 */     for (UserProperty property : list) {
/* 148 */       List<String> values = new ArrayList<>();
/* 149 */       values.add(property.getValue());
/*     */       
/* 151 */       map.put(property.getKey(), values);
/*     */     } 
/*     */     
/* 154 */     return map;
/*     */   }
/*     */   
/*     */   void setProperties(List<Map<String, String>> properties) {
/* 158 */     this.userProperties = properties;
/*     */   }
/*     */   
/*     */   public AccountType getType() {
/* 162 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(AccountType type) {
/* 166 */     if (type == null) {
/* 167 */       throw new NullPointerException();
/*     */     }
/* 169 */     this.type = type;
/*     */   }
/*     */   
/*     */   public boolean isFree() {
/* 173 */     return this.type.equals(AccountType.FREE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Map<String, Object> createMap() {
/* 188 */     Map<String, Object> r = new HashMap<>();
/*     */     
/* 190 */     r.put("username", this.username);
/* 191 */     r.put("userid", this.userID);
/* 192 */     r.put("uuid", UUIDTypeAdapter.toUUID(this.uuid));
/* 193 */     r.put("displayName", this.displayName);
/*     */     
/* 195 */     if (!isFree()) {
/* 196 */       r.put("type", this.type);
/* 197 */       r.put("accessToken", this.accessToken);
/*     */     } 
/*     */     
/* 200 */     if (this.userProperties != null) {
/* 201 */       r.put("userProperties", this.userProperties);
/*     */     }
/* 203 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void fillFromMap(Map<String, Object> map) {
/* 209 */     if (map.containsKey("username")) {
/* 210 */       setUsername(map.get("username").toString());
/*     */     }
/* 212 */     setUserID(map.containsKey("userid") ? map.get("userid").toString() : getUsername());
/* 213 */     setDisplayName(map.containsKey("displayName") ? map.get("displayName").toString() : getUsername());
/*     */     
/* 215 */     setProperties(map.containsKey("userProperties") ? (List<Map<String, String>>)map.get("userProperties") : null);
/*     */     
/* 217 */     setUUID(map.containsKey("uuid") ? UUIDTypeAdapter.toUUID(map.get("uuid").toString()) : null);
/*     */     
/* 219 */     boolean hasAccessToken = map.containsKey("accessToken");
/*     */     
/* 221 */     if (hasAccessToken) {
/* 222 */       setAccessToken(map.get("accessToken").toString());
/*     */     }
/* 224 */     setType(map.containsKey("type") ? (AccountType)Reflect.parseEnum(AccountType.class, map.get("type").toString()) : (hasAccessToken ? AccountType.MOJANG : AccountType.FREE));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Account createFreeAccountByUsername(String username) {
/* 229 */     Account account = new Account();
/* 230 */     account.setUUID(UUIDTypeAdapter.fromUUID(UUID.randomUUID()));
/* 231 */     account.setUsername(username);
/* 232 */     account.setUserID(account.getUsername());
/* 233 */     account.setDisplayName(account.getUsername());
/* 234 */     account.setType(AccountType.FREE);
/* 235 */     return account;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 240 */     return toDebugString();
/*     */   }
/*     */   
/*     */   public String toDebugString() {
/* 244 */     Map<String, Object> map = createMap();
/*     */     
/* 246 */     if (map.containsKey("accessToken")) {
/* 247 */       map.remove("accessToken");
/* 248 */       map.put("accessToken", "(not null)");
/*     */     } 
/*     */     
/* 251 */     return "Account" + map;
/*     */   }
/*     */   
/*     */   public static Account randomAccount() {
/* 255 */     return new Account("empty_manage " + (new Random()).nextLong());
/*     */   }
/*     */   
/*     */   public boolean isPremiumAccount() {
/* 259 */     return this.premiumAccount;
/*     */   }
/*     */   
/*     */   public void setPremiumAccount(boolean premiumAccount) {
/* 263 */     this.premiumAccount = premiumAccount;
/*     */   }
/*     */   
/*     */   public MicrosoftOAuthToken getMicrosoftOAuthToken() {
/* 267 */     return this.microsoftOAuthToken;
/*     */   }
/*     */   
/*     */   public void setMicrosoftOAuthToken(MicrosoftOAuthToken microsoftOAuthToken) {
/* 271 */     this.microsoftOAuthToken = microsoftOAuthToken;
/*     */   }
/*     */   
/*     */   public MinecraftServicesToken getMinecraftServicesToken() {
/* 275 */     return this.minecraftServicesToken;
/*     */   }
/*     */   
/*     */   public void setMinecraftServicesToken(MinecraftServicesToken minecraftServicesToken) {
/* 279 */     this.minecraftServicesToken = minecraftServicesToken;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum AccountType
/*     */   {
/* 288 */     TLAUNCHER, MOJANG, FREE, SPECIAL, MICROSOFT;
/*     */     public static Set<AccountType> NONE_OFFICIAL_ACCOUNTS;
/* 290 */     public static Set<AccountType> OFFICIAL_ACCOUNTS = Sets.newHashSet((Object[])new AccountType[] { MOJANG, MICROSOFT }); static {
/* 291 */       NONE_OFFICIAL_ACCOUNTS = Sets.newHashSet((Object[])new AccountType[] { TLAUNCHER, FREE });
/*     */     }
/*     */   }
/*     */   
/*     */   public String getShortUUID() {
/* 296 */     return UUIDTypeAdapter.toUUID(this.uuid);
/*     */   }
/*     */   
/*     */   public Account() {}
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\auth\Account.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */