/*     */ package org.tlauncher.tlauncher.minecraft.auth;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.launcher.Http;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.tlauncher.tlauncher.entity.auth.AuthenticationRequest;
/*     */ import org.tlauncher.tlauncher.entity.auth.AuthenticationResponse;
/*     */ import org.tlauncher.tlauncher.entity.auth.RefreshRequest;
/*     */ import org.tlauncher.tlauncher.entity.auth.RefreshResponse;
/*     */ import org.tlauncher.tlauncher.entity.auth.Request;
/*     */ import org.tlauncher.tlauncher.entity.auth.Response;
/*     */ import org.tlauncher.tlauncher.exceptions.auth.AuthenticatorException;
/*     */ import org.tlauncher.tlauncher.exceptions.auth.BlockedUserException;
/*     */ import org.tlauncher.tlauncher.exceptions.auth.InvalidCredentialsException;
/*     */ import org.tlauncher.tlauncher.exceptions.auth.NotCorrectTokenOrIdException;
/*     */ import org.tlauncher.tlauncher.exceptions.auth.UserMigratedException;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardAuthenticator
/*     */   extends Authenticator
/*     */ {
/*  32 */   protected final Gson gson = (new GsonBuilder()).registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
/*     */   private final URL AUTHENTICATE_URL;
/*     */   private final URL REFRESH_URL;
/*     */   
/*     */   StandardAuthenticator(Account account, String authUrl, String refreshUrl) {
/*  37 */     super(account);
/*     */     
/*  39 */     this.AUTHENTICATE_URL = Http.constantURL(authUrl);
/*  40 */     this.REFRESH_URL = Http.constantURL(refreshUrl);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pass() throws AuthenticatorException {
/*  45 */     if (this.account.isFree()) {
/*  46 */       throw new IllegalArgumentException("invalid account type");
/*     */     }
/*  48 */     if (this.account.getPassword() == null && this.account.getAccessToken() == null) {
/*  49 */       throw new AuthenticatorException(new NullPointerException("password/accessToken"));
/*     */     }
/*  51 */     log(new Object[] { "Staring to authenticate:", this.account });
/*  52 */     log(new Object[] { "hasUsername:", this.account.getUsername() });
/*  53 */     log(new Object[] { "hasPassword:", Boolean.valueOf((this.account.getPassword() != null)) });
/*  54 */     log(new Object[] { "hasAccessToken:", Boolean.valueOf((this.account.getAccessToken() != null)) });
/*     */     
/*  56 */     if (this.account.getPassword() == null) {
/*  57 */       validateToken();
/*     */     } else {
/*  59 */       passwordLogin();
/*     */     } 
/*  61 */     log(new Object[] { "Log in successful!" });
/*     */     
/*  63 */     log(new Object[] { "hasUUID:", Boolean.valueOf((this.account.getUUID() != null)) });
/*  64 */     log(new Object[] { "hasAccessToken:", Boolean.valueOf((this.account.getAccessToken() != null)) });
/*  65 */     log(new Object[] { "hasProfiles:", Boolean.valueOf((this.account.getProfiles() != null)) });
/*  66 */     log(new Object[] { "hasProfile:", Boolean.valueOf((this.account.getProfiles() != null)) });
/*  67 */     log(new Object[] { "hasProperties:", Boolean.valueOf((this.account.getProperties() != null)) });
/*     */   }
/*     */   
/*     */   private void passwordLogin() throws AuthenticatorException {
/*  71 */     log(new Object[] { "Loggining in with password" });
/*     */     
/*  73 */     AuthenticationRequest request = new AuthenticationRequest(this);
/*     */     try {
/*  75 */       AuthenticationResponse response = makeRequest(this.AUTHENTICATE_URL, (Request)request, AuthenticationResponse.class);
/*     */       
/*  77 */       this.account.setPassword(null);
/*  78 */       this.account.setUserID((response.getUserId() != null) ? response.getUserId() : this.account.getUsername());
/*  79 */       this.account.setAccessToken(response.getAccessToken());
/*  80 */       this.account.setProfiles(response.getAvailableProfiles());
/*  81 */       this.account.setProfile(response.getSelectedProfile());
/*  82 */       this.account.setUser(response.getUser());
/*  83 */       this.account.setUUID(request.getClientToken());
/*     */       
/*  85 */       if (response.getSelectedProfile() != null) {
/*  86 */         this.account.setUUID(response.getSelectedProfile().getId());
/*  87 */         this.account.setDisplayName(response.getSelectedProfile().getName());
/*     */       } 
/*  89 */     } catch (InvalidCredentialsException e) {
/*  90 */       throw new AuthenticatorException("Invalid user or password", "restore.on.site." + this.account
/*  91 */           .getType().toString().toLowerCase(Locale.ROOT));
/*     */     } 
/*  93 */     if (Account.AccountType.MOJANG.equals(this.account.getType()))
/*  94 */       Alert.showLocWarning(null, Localizable.get("auth.warn.default.auth"), null); 
/*     */   }
/*     */   
/*     */   private void validateToken() throws AuthenticatorException {
/*  98 */     log(new Object[] { "Loggining in with token" });
/*     */     
/* 100 */     RefreshRequest request = new RefreshRequest(this);
/*     */     try {
/* 102 */       RefreshResponse response = makeRequest(this.REFRESH_URL, (Request)request, RefreshResponse.class);
/* 103 */       this.account.setAccessToken(response.getAccessToken());
/* 104 */       if (StringUtils.isNotBlank(response.getAccessToken())) {
/* 105 */         if (this instanceof TlauncherAuthenticator) {
/* 106 */           this.account.setType(Account.AccountType.TLAUNCHER);
/*     */         } else {
/* 108 */           this.account.setType(Account.AccountType.MOJANG);
/*     */         } 
/*     */       } else {
/* 111 */         this.account.setType(Account.AccountType.FREE);
/*     */       } 
/* 113 */       this.account.setUser(response.getUser());
/*     */       
/* 115 */       this.account.setProfile(response.getSelectedProfile());
/* 116 */       this.account.setUser(response.getUser());
/* 117 */     } catch (InvalidCredentialsException e) {
/* 118 */       throw new NotCorrectTokenOrIdException();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private <T extends Response> T makeRequest(URL url, Request input, Class<T> classOfT) throws AuthenticatorException {
/*     */     Response response;
/* 125 */     if (url == null) {
/* 126 */       throw new NullPointerException("url");
/*     */     }
/*     */     
/*     */     try {
/*     */       String jsonResult;
/* 131 */       if (input == null) {
/* 132 */         jsonResult = Http.performGet(url);
/*     */       } else {
/* 134 */         jsonResult = Http.performPost(url, this.gson.toJson(input), "application/json");
/*     */       } 
/*     */       try {
/* 137 */         response = (Response)this.gson.fromJson(jsonResult, classOfT);
/* 138 */       } catch (RuntimeException rE) {
/* 139 */         throw new AuthenticatorException("Error parsing response: \"" + jsonResult + "\"", "unparseable", rE);
/*     */       }
/*     */     
/* 142 */     } catch (IOException e) {
/* 143 */       if (e.getMessage().contains("Server returned HTTP response code: 403")) {
/* 144 */         throw new InvalidCredentialsException();
/*     */       }
/* 146 */       throw new AuthenticatorException("Error making request, uncaught IOException", "unreachable", e);
/*     */     } 
/*     */     
/* 149 */     if (response == null) {
/* 150 */       return null;
/*     */     }
/* 152 */     if (StringUtils.isBlank(response.getError()))
/* 153 */       return (T)response; 
/* 154 */     throw getException(response);
/*     */   }
/*     */   
/*     */   protected AuthenticatorException getException(Response result) {
/* 158 */     if ("UserMigratedException".equals(result.getCause())) {
/* 159 */       return (AuthenticatorException)new UserMigratedException();
/*     */     }
/* 161 */     if ("ForbiddenOperationException".equals(result.getError()))
/* 162 */       return (AuthenticatorException)new InvalidCredentialsException(); 
/* 163 */     if (Objects.nonNull(result.getErrorMessage()) && result.getErrorMessage().contains("User is blocked")) {
/* 164 */       return (AuthenticatorException)new BlockedUserException(result.getCause(), result.getErrorMessage());
/*     */     }
/* 166 */     return new AuthenticatorException(result, "internal");
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\auth\StandardAuthenticator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */