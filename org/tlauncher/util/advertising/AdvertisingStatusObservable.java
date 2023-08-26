/*     */ package org.tlauncher.util.advertising;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.swing.SwingUtilities;
/*     */ import net.minecraft.launcher.Http;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.tlauncher.skin.domain.AdvertisingDTO;
/*     */ import org.tlauncher.skin.domain.responce.AdvertisingResponseDTO;
/*     */ import org.tlauncher.tlauncher.entity.profile.ClientProfile;
/*     */ import org.tlauncher.tlauncher.managers.ProfileManager;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AdvertisingStatusObservable
/*     */   implements Runnable
/*     */ {
/*     */   private final ClientProfile clientProfile;
/*     */   private final ProfileManager profileManager;
/*     */   private List<AdvertisingStatusObserver> listeners;
/*     */   
/*     */   public AdvertisingStatusObservable(ClientProfile clientProfile, ProfileManager profileManager) {
/*  35 */     this.listeners = new ArrayList<>(); this.clientProfile = clientProfile; this.profileManager = profileManager; } public void setListeners(List<AdvertisingStatusObserver> listeners) { this.listeners = listeners; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof AdvertisingStatusObservable)) return false;  AdvertisingStatusObservable other = (AdvertisingStatusObservable)o; if (!other.canEqual(this)) return false;  Object this$clientProfile = getClientProfile(), other$clientProfile = other.getClientProfile(); if ((this$clientProfile == null) ? (other$clientProfile != null) : !this$clientProfile.equals(other$clientProfile)) return false;  Object this$profileManager = getProfileManager(), other$profileManager = other.getProfileManager(); if ((this$profileManager == null) ? (other$profileManager != null) : !this$profileManager.equals(other$profileManager)) return false;  Object<AdvertisingStatusObserver> this$listeners = (Object<AdvertisingStatusObserver>)getListeners(), other$listeners = (Object<AdvertisingStatusObserver>)other.getListeners(); return !((this$listeners == null) ? (other$listeners != null) : !this$listeners.equals(other$listeners)); } protected boolean canEqual(Object other) { return other instanceof AdvertisingStatusObservable; } public List<AdvertisingStatusObserver> getListeners() { return this.listeners; }
/*     */   public int hashCode() { int PRIME = 59; result = 1; Object $clientProfile = getClientProfile(); result = result * 59 + (($clientProfile == null) ? 43 : $clientProfile.hashCode()); Object $profileManager = getProfileManager(); result = result * 59 + (($profileManager == null) ? 43 : $profileManager.hashCode()); Object<AdvertisingStatusObserver> $listeners = (Object<AdvertisingStatusObserver>)getListeners(); return result * 59 + (($listeners == null) ? 43 : $listeners.hashCode()); }
/*     */   public String toString() { return "AdvertisingStatusObservable(clientProfile=" + getClientProfile() + ", profileManager=" + getProfileManager() + ", listeners=" + getListeners() + ")"; }
/*     */   public ClientProfile getClientProfile() { return this.clientProfile; }
/*  39 */   public ProfileManager getProfileManager() { return this.profileManager; } public void run() { log("started to get ad information");
/*  40 */     AdvertisingDTO dto = new AdvertisingDTO();
/*     */     try {
/*  42 */       String url = TLauncher.getInnerSettings().get("skin.server.advertising");
/*  43 */       for (Iterator<Account> iterator = this.clientProfile.getAccounts().values().iterator(); iterator.hasNext(); ) { Account acc = iterator.next();
/*  44 */         if (acc.getType() == Account.AccountType.TLAUNCHER) {
/*  45 */           U.debug(new Object[] { acc });
/*  46 */           JsonObject send = new JsonObject();
/*  47 */           send.add("clientToken", (JsonElement)new JsonPrimitive(this.clientProfile.getClientToken().toString()));
/*  48 */           send.add("accessToken", (JsonElement)new JsonPrimitive(
/*  49 */                 (acc.getAccessToken() == null) ? "null" : acc.getAccessToken()));
/*     */           
/*  51 */           String res = Http.performPost(new URL(url), send.toString(), "application/json");
/*  52 */           AdvertisingResponseDTO advertisingResponseDTO = (AdvertisingResponseDTO)(new Gson()).fromJson(res, AdvertisingResponseDTO.class);
/*     */           
/*  54 */           if (StringUtils.isNotBlank(advertisingResponseDTO.getError())) {
/*  55 */             if ("Invalid token.".equals(advertisingResponseDTO.getErrorMessage())) {
/*  56 */               SwingUtilities.invokeLater(() -> {
/*     */                     Alert.showWarning("", Localizable.getByKeys("token.not.valid", new Object[] { "account.config", "crash.opengl.help" }));
/*     */                     
/*     */                     try {
/*     */                       this.profileManager.remove(acc);
/*  61 */                     } catch (IOException e) {
/*     */                       U.log(new Object[] { e });
/*     */                     } 
/*     */                     U.log(new Object[] { "removed not valid token" });
/*     */                     log(advertisingResponseDTO.getError() + " " + advertisingResponseDTO.getErrorMessage());
/*     */                   });
/*     */             }
/*     */             continue;
/*     */           } 
/*  70 */           acc.setAccessToken(advertisingResponseDTO.getAccessToken());
/*  71 */           if (advertisingResponseDTO.getUser().isAccountStatus()) {
/*  72 */             acc.setPremiumAccount(true);
/*  73 */             dto = advertisingResponseDTO.getAdvertising();
/*     */ 
/*     */             
/*     */             break;
/*     */           } 
/*     */         }  }
/*     */     
/*  80 */     } catch (IOException|com.google.gson.JsonSyntaxException e) {
/*  81 */       log(e);
/*     */     } 
/*  83 */     log("finished to get add information");
/*  84 */     notifyObserver(dto); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addListeners(AdvertisingStatusObserver advertisingStatusListerner) {
/*  89 */     this.listeners.add(advertisingStatusListerner);
/*     */   }
/*     */   
/*     */   public void removeListener(AdvertisingStatusObserver advertisingStatusListerner) {
/*  93 */     this.listeners.remove(advertisingStatusListerner);
/*     */   }
/*     */   
/*     */   public void notifyObserver(AdvertisingDTO advertisingDTO) {
/*  97 */     for (AdvertisingStatusObserver advertisingStatusObserver : this.listeners) {
/*  98 */       advertisingStatusObserver.advertisingReceived(advertisingDTO);
/*     */     }
/*     */   }
/*     */   
/*     */   private void log(Object o) {
/* 103 */     U.log(new Object[] { "[AdvertisingStatusObserver] ", o });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\advertising\AdvertisingStatusObservable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */