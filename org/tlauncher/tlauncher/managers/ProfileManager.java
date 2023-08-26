/*     */ package org.tlauncher.tlauncher.managers;
/*     */ 
/*     */ import by.gdev.utils.service.FileMapperService;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.name.Names;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.attribute.FileOwnerAttributeView;
/*     */ import java.nio.file.attribute.UserPrincipal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.tlauncher.tlauncher.component.RefreshableComponent;
/*     */ import org.tlauncher.tlauncher.entity.profile.ClientProfile;
/*     */ import org.tlauncher.tlauncher.exceptions.RequiredTLAccountException;
/*     */ import org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.browser.BrowserHolder;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.MinecraftUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.advertising.AdvertisingStatusObservable;
/*     */ import org.tlauncher.util.advertising.AdvertisingStatusObserver;
/*     */ 
/*     */ 
/*     */ public class ProfileManager
/*     */   extends RefreshableComponent
/*     */ {
/*     */   private static final String DEFAULT_PROFILE_FILENAME = "TlauncherProfiles.json";
/*     */   private static final String LAUNCHER_PROFILE_FILENAME = "launcher_profiles.json";
/*  41 */   private final List<ProfileManagerListener> listeners = Collections.synchronizedList(new ArrayList<>());
/*     */   
/*     */   private final FileMapperService fileMapperService;
/*     */   private volatile ClientProfile clientProfile;
/*     */   
/*     */   public ProfileManager(ComponentManager manager) throws Exception {
/*  47 */     super(manager);
/*  48 */     this
/*  49 */       .fileMapperService = (FileMapperService)TLauncher.getInjector().getInstance(Key.get(FileMapperService.class, (Annotation)Names.named("profileFileMapperService")));
/*  50 */     addListener(new ProfileManagerAdapter()
/*     */         {
/*     */           private boolean init;
/*     */           
/*     */           public void fireRefreshed(ClientProfile clientProfile) {
/*  55 */             if (this.init) {
/*     */               return;
/*     */             }
/*  58 */             this.init = true;
/*  59 */             TLauncher.getInstance().getTLauncherManager().asyncRefresh();
/*  60 */             AdvertisingStatusObservable adStatus = new AdvertisingStatusObservable(clientProfile, ProfileManager.this);
/*     */             
/*  62 */             if (BrowserHolder.getInstance().getBrowser() != null) {
/*  63 */               adStatus.addListeners((AdvertisingStatusObserver)BrowserHolder.getInstance().getBrowser());
/*     */             }
/*  65 */             adStatus.run();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean refresh() {
/*  72 */     loadProfiles();
/*  73 */     fireProfileRefreshed();
/*     */     try {
/*  75 */       saveProfiles();
/*  76 */     } catch (IOException e) {
/*  77 */       return false;
/*     */     } 
/*  79 */     return true;
/*     */   }
/*     */   
/*     */   public void fireProfileRefreshed() {
/*  83 */     for (ProfileManagerListener listener : this.listeners)
/*  84 */       listener.fireRefreshed(this.clientProfile); 
/*     */   }
/*     */   
/*     */   public UUID getClientToken() {
/*  88 */     return this.clientProfile.getClientToken();
/*     */   }
/*     */   
/*     */   public void addListener(ProfileManagerListener listener) {
/*  92 */     if (listener == null) {
/*  93 */       throw new NullPointerException();
/*     */     }
/*  95 */     if (!this.listeners.contains(listener))
/*  96 */       this.listeners.add(listener); 
/*     */   }
/*     */   
/*     */   public boolean isNotPremium() {
/* 100 */     return this.clientProfile.getAccounts().values().stream().noneMatch(Account::isPremiumAccount);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Account account) throws IOException {
/* 105 */     Account ac = (Account)this.clientProfile.getAccounts().get(account.getShortUUID());
/* 106 */     if (Objects.nonNull(ac)) {
/* 107 */       this.clientProfile.getAccounts().remove(account.getShortUUID());
/* 108 */       saveProfiles();
/* 109 */       fireProfileChanged();
/* 110 */       return true;
/*     */     } 
/*     */     
/* 113 */     fireProfileChanged();
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void save(Account account) throws IOException {
/* 119 */     U.log(new Object[] { "saved account " + account.toString() });
/* 120 */     this.clientProfile.getAccounts().put(account.getShortUUID(), account);
/* 121 */     this.clientProfile.setSelectedAccountUUID(account.getShortUUID());
/* 122 */     saveProfiles();
/* 123 */     fireProfileChanged();
/*     */   }
/*     */   
/*     */   public Account getSelectedAccount() {
/* 127 */     if (Objects.isNull(this.clientProfile.getSelectedAccountUUID())) {
/* 128 */       return null;
/*     */     }
/* 130 */     return (Account)this.clientProfile.getAccounts().get(this.clientProfile.getSelectedAccountUUID());
/*     */   }
/*     */   
/*     */   public boolean hasSelectedAccount() {
/* 134 */     return Objects.nonNull(getSelectedAccount());
/*     */   }
/*     */   
/*     */   public void selectAccount(Account account) {
/*     */     try {
/* 139 */       if (!account.getShortUUID().equals(this.clientProfile.getSelectedAccountUUID())) {
/* 140 */         this.clientProfile.setSelectedAccountUUID(account.getShortUUID());
/* 141 */         saveProfiles();
/*     */       } 
/*     */     } catch (Throwable $ex) {
/*     */       throw $ex;
/*     */     } 
/*     */   } public void updateFreeAccountField(String username) { try {
/*     */       Account ac;
/* 148 */       Optional<Account> op = this.clientProfile.getAccounts().values().stream().filter(e -> Objects.equals(e.getShortUUID(), this.clientProfile.getFreeAccountUUID())).findFirst();
/*     */       
/* 150 */       if (op.isPresent() && !Objects.equals(((Account)op.get()).getUsername(), this.clientProfile.getFreeAccountUUID())) {
/* 151 */         ac = op.get();
/* 152 */         ac.setUsername(username);
/* 153 */         ac.setUserID(username);
/* 154 */         ac.setDisplayName(username);
/*     */       } else {
/* 156 */         ac = Account.createFreeAccountByUsername(username);
/* 157 */         this.clientProfile.setFreeAccountUUID(ac.getShortUUID());
/*     */       } 
/* 159 */       if (StringUtils.isBlank(username)) {
/* 160 */         remove(ac);
/*     */       } else {
/* 162 */         save(ac);
/*     */       } 
/* 164 */       TLauncher.getInstance().getConfiguration().set("login.account", null);
/*     */     } catch (Throwable $ex) {
/*     */       throw $ex;
/*     */     }  } private File getProfileFile() {
/* 168 */     return new File(MinecraftUtil.getWorkingDirectory(), "TlauncherProfiles.json");
/*     */   }
/*     */   
/*     */   private void fireProfileChanged() {
/* 172 */     for (ProfileManagerListener listener : this.listeners)
/* 173 */       listener.fireClientProfileChanged(this.clientProfile); 
/*     */   }
/*     */   
/*     */   private void loadProfiles() {
/* 177 */     log(new Object[] { "Refreshing profiles from:", getProfileFile() });
/*     */     try {
/* 179 */       this.clientProfile = (ClientProfile)this.fileMapperService.read("TlauncherProfiles.json", ClientProfile.class);
/* 180 */       if (Objects.isNull(this.clientProfile))
/* 181 */         this.clientProfile = new ClientProfile(); 
/* 182 */       setOldField();
/*     */       
/* 184 */       removedNotValidAccounts();
/* 185 */     } catch (Exception e) {
/* 186 */       log(new Object[] { "Cannot read from", "TlauncherProfiles.json", e });
/* 187 */       this.clientProfile = new ClientProfile();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void removedNotValidAccounts() {
/* 193 */     ((List)this.clientProfile.getAccounts().entrySet().stream()
/* 194 */       .filter(e -> (Objects.isNull(((Account)e.getValue()).getShortUUID()) || StringUtils.isBlank(((Account)e.getValue()).getUsername()) || ((String)e.getKey()).contains("-") || Objects.isNull(e.getKey()) || !((String)e.getKey()).equals(((Account)e.getValue()).getShortUUID())))
/*     */ 
/*     */       
/* 197 */       .collect(Collectors.toList())).forEach(e -> {
/*     */           U.log(new Object[] { "removed account with null uuid or username " + ((Account)e.getValue()).toString() });
/*     */           this.clientProfile.getAccounts().remove(e.getKey());
/*     */         });
/* 201 */     if (StringUtils.contains(this.clientProfile.getFreeAccountUUID(), "-")) {
/* 202 */       this.clientProfile.setFreeAccountUUID(null);
/*     */     }
/* 204 */     if (StringUtils.contains(this.clientProfile.getSelectedAccountUUID(), "-")) {
/* 205 */       this.clientProfile.setSelectedAccountUUID(null);
/*     */     }
/*     */     
/* 208 */     createStandardOfficialProfileFile();
/*     */   }
/*     */ 
/*     */   
/*     */   private void setOldField() {
/* 213 */     if (Objects.nonNull(this.clientProfile.getAuthenticationDatabase())) {
/* 214 */       this.clientProfile.setAccounts(new HashMap<>());
/* 215 */       for (Account c : this.clientProfile.getAuthenticationDatabase().getAccounts()) {
/* 216 */         this.clientProfile.getAccounts().put(c.getShortUUID(), c);
/*     */       }
/* 218 */       this.clientProfile.setAuthenticationDatabase(null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void createStandardOfficialProfileFile() {
/* 223 */     File launcherProfile = new File(MinecraftUtil.getWorkingDirectory(), "launcher_profiles.json");
/* 224 */     if (Files.notExists(launcherProfile.toPath(), new java.nio.file.LinkOption[0])) {
/*     */       try {
/* 226 */         FileUtil.writeFile(launcherProfile, "{\n\"clientToken\": \"" + this.clientProfile
/* 227 */             .getClientToken() + "\"\n,\"profiles\": {}}");
/* 228 */       } catch (IOException e) {
/* 229 */         log(new Object[] { e });
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void saveProfiles() throws IOException {
/* 235 */     U.log(new Object[] { "save profiles" });
/* 236 */     this.fileMapperService.write(this.clientProfile, "TlauncherProfiles.json");
/*     */   }
/*     */   
/*     */   public void print() {
/* 240 */     U.log(new Object[] { "accounts" });
/* 241 */     this.clientProfile.getAccounts().entrySet().forEach(e -> U.log(new Object[] { "" + (String)e.getKey() + " " + ((Account)e.getValue()).getUsername() + " " + ((Account)e.getValue()).getShortUUID() + " " + ((Account)e.getValue()).getType() }));
/*     */ 
/*     */ 
/*     */     
/* 245 */     U.log(new Object[] { "selected " + this.clientProfile.getSelectedAccountUUID() });
/* 246 */     U.log(new Object[] { "selected free" + this.clientProfile.getFreeAccountUUID() });
/*     */   }
/*     */ 
/*     */   
/*     */   public void print1() {
/* 251 */     File f = getProfileFile();
/* 252 */     FileOwnerAttributeView ownerInfo = Files.<FileOwnerAttributeView>getFileAttributeView(f.toPath(), FileOwnerAttributeView.class, new java.nio.file.LinkOption[0]);
/*     */     
/*     */     try {
/* 255 */       UserPrincipal fileOwner = ownerInfo.getOwner();
/* 256 */       U.log(new Object[] { "File Owned by: " + fileOwner.getName() });
/* 257 */       U.log(new Object[] { String.format("read %s, write %s, execute %s", new Object[] { Boolean.valueOf(f.canRead()), Boolean.valueOf(f.canWrite()), Boolean.valueOf(f.canExecute()) }) });
/* 258 */     } catch (IOException e) {
/* 259 */       U.log(new Object[] { e });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Account findUniqueTlauncherAccount() throws SelectedAnyOneTLAccountException, RequiredTLAccountException {
/* 265 */     Account ac = getSelectedAccount();
/* 266 */     if (Objects.nonNull(ac) && Account.AccountType.TLAUNCHER.equals(ac.getType())) {
/* 267 */       return ac;
/*     */     }
/*     */     
/* 270 */     List<Account> list = (List<Account>)this.clientProfile.getAccounts().values().stream().filter(a -> Account.AccountType.TLAUNCHER.equals(a.getType())).collect(Collectors.toList());
/* 271 */     if (list.size() > 1)
/* 272 */       throw new SelectedAnyOneTLAccountException(); 
/* 273 */     if (list.isEmpty())
/* 274 */       throw new RequiredTLAccountException(); 
/* 275 */     return list.get(0);
/*     */   }
/*     */   
/*     */   public List<Account> findAllTLAccount() {
/* 279 */     return (List<Account>)this.clientProfile.getAccounts().values().stream().filter(a -> Account.AccountType.TLAUNCHER.equals(a.getType()))
/* 280 */       .collect(Collectors.toList());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\managers\ProfileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */