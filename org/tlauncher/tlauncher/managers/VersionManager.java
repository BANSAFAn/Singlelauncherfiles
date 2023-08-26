/*     */ package org.tlauncher.tlauncher.managers;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.launcher.updater.LatestVersionSyncInfo;
/*     */ import net.minecraft.launcher.updater.LocalVersionList;
/*     */ import net.minecraft.launcher.updater.RemoteVersionList;
/*     */ import net.minecraft.launcher.updater.VersionFilter;
/*     */ import net.minecraft.launcher.updater.VersionList;
/*     */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import net.minecraft.launcher.versions.ReleaseType;
/*     */ import net.minecraft.launcher.versions.Version;
/*     */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*     */ import org.tlauncher.tlauncher.component.ComponentDependence;
/*     */ import org.tlauncher.tlauncher.component.InterruptibleComponent;
/*     */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*     */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*     */ import org.tlauncher.tlauncher.repository.Repo;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.Time;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.async.AsyncObject;
/*     */ import org.tlauncher.util.async.AsyncObjectContainer;
/*     */ import org.tlauncher.util.async.AsyncObjectGotErrorException;
/*     */ import org.tlauncher.util.async.AsyncThread;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ComponentDependence({AssetsManager.class, VersionLists.class, TLauncherManager.class})
/*     */ public class VersionManager
/*     */   extends InterruptibleComponent
/*     */ {
/*     */   private final LocalVersionList localList;
/*     */   private final RemoteVersionList[] remoteLists;
/*     */   private final List<VersionManagerListener> listeners;
/*     */   private final Object versionFlushLock;
/*     */   private Map<ReleaseType, Version> latestVersions;
/*     */   private boolean hadRemote;
/*     */   private boolean localRefresh;
/*  56 */   private final List<VersionSyncInfo> currentSyncVersions = new ArrayList<>();
/*     */   
/*     */   public VersionManager(ComponentManager manager) throws Exception {
/*  59 */     super(manager);
/*     */     
/*  61 */     VersionLists list = manager.<VersionLists>getComponent(VersionLists.class);
/*     */     
/*  63 */     this.localList = list.getLocal();
/*  64 */     this.remoteLists = list.getRemoteLists();
/*     */     
/*  66 */     this.latestVersions = new LinkedHashMap<>();
/*     */     
/*  68 */     this.listeners = Collections.synchronizedList(new ArrayList<>());
/*  69 */     this.versionFlushLock = new Object();
/*     */   }
/*     */   
/*     */   public void addListener(VersionManagerListener listener) {
/*  73 */     if (listener == null) {
/*  74 */       throw new NullPointerException();
/*     */     }
/*  76 */     this.listeners.add(listener);
/*     */   }
/*     */   
/*     */   public LocalVersionList getLocalList() {
/*  80 */     synchronized (this.versionFlushLock) {
/*  81 */       return this.localList;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Map<ReleaseType, Version> getLatestVersions() {
/*  86 */     synchronized (this.versionFlushLock) {
/*  87 */       return Collections.unmodifiableMap(this.latestVersions);
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean refresh(int refreshID, boolean local) {
/*  92 */     this.refreshList[refreshID] = true;
/*  93 */     this.localRefresh = local;
/*  94 */     int i = local | (!this.manager.getLauncher().getConfiguration().getBoolean("minecraft.versions.sub.remote") ? 1 : 0);
/*  95 */     this.hadRemote |= (i == 0) ? 1 : 0;
/*     */     
/*  97 */     if (i != 0) {
/*  98 */       log(new Object[] { "Refreshing versions locally..." });
/*     */     } else {
/* 100 */       log(new Object[] { "Refreshing versions remotely..." });
/*     */       
/* 102 */       this.latestVersions.clear();
/*     */       
/* 104 */       synchronized (this.listeners) {
/* 105 */         for (VersionManagerListener listener : this.listeners) {
/* 106 */           listener.onVersionsRefreshing(this);
/*     */         }
/*     */       } 
/*     */     } 
/* 110 */     Object lock = new Object();
/* 111 */     Time.start(lock);
/*     */     
/* 113 */     Map<AsyncObject<VersionList.RawVersionList>, VersionList.RawVersionList> result = null;
/* 114 */     Throwable e = null;
/*     */     
/*     */     try {
/* 117 */       result = refreshVersions(i);
/* 118 */     } catch (Throwable e0) {
/* 119 */       e = e0;
/*     */     } 
/*     */     
/* 122 */     if (isCancelled(refreshID)) {
/* 123 */       log(new Object[] { "Version refresh has been cancelled (" + Time.stop(lock) + " ms)" });
/* 124 */       return false;
/*     */     } 
/*     */     
/* 127 */     if (e != null) {
/* 128 */       synchronized (this.listeners) {
/* 129 */         for (VersionManagerListener listener : this.listeners) {
/* 130 */           listener.onVersionsRefreshingFailed(this);
/*     */         }
/*     */       } 
/* 133 */       log(new Object[] { "Cannot refresh versions (" + Time.stop(lock) + " ms)", e });
/* 134 */       return true;
/*     */     } 
/*     */     
/* 137 */     if (result != null) {
/* 138 */       synchronized (this.versionFlushLock) {
/* 139 */         for (AsyncObject<VersionList.RawVersionList> object : result.keySet()) {
/* 140 */           VersionList.RawVersionList rawList = result.get(object);
/*     */           
/* 142 */           if (rawList == null) {
/*     */             continue;
/*     */           }
/* 145 */           AsyncRawVersionListObject listObject = (AsyncRawVersionListObject)object;
/* 146 */           RemoteVersionList versionList = listObject.getVersionList();
/*     */           
/* 148 */           versionList.refreshVersions(rawList);
/* 149 */           this.latestVersions.putAll(versionList.getLatestVersions());
/*     */         } 
/*     */       } 
/*     */     }
/* 153 */     this.latestVersions = U.sortMap(this.latestVersions, (Object[])ReleaseType.values());
/* 154 */     List<VersionSyncInfo> l1 = getVersions0();
/* 155 */     this.currentSyncVersions.clear();
/* 156 */     this.currentSyncVersions.addAll(l1);
/* 157 */     log(new Object[] { "Versions has been refreshed (" + Time.stop(lock) + " ms)" });
/*     */     
/* 159 */     this.refreshList[refreshID] = false;
/*     */     
/* 161 */     synchronized (this.listeners) {
/* 162 */       for (VersionManagerListener listener : this.listeners) {
/* 163 */         listener.onVersionsRefreshed(this);
/*     */       }
/*     */     } 
/* 166 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean refresh(int queueID) {
/* 171 */     return refresh(queueID, false);
/*     */   }
/*     */   
/*     */   public void startRefresh(boolean local) {
/* 175 */     refresh(nextID(), local);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void stopRefresh() {
/* 180 */     super.stopRefresh();
/* 181 */     startRefresh(true);
/*     */   }
/*     */   
/*     */   public void asyncRefresh(boolean local) {
/* 185 */     AsyncThread.execute(() -> startRefresh(local));
/*     */   }
/*     */ 
/*     */   
/*     */   public void asyncRefresh() {
/* 190 */     asyncRefresh(false);
/*     */   }
/*     */   
/*     */   private Map<AsyncObject<VersionList.RawVersionList>, VersionList.RawVersionList> refreshVersions(boolean local) {
/* 194 */     this.localList.refreshVersions();
/*     */     
/* 196 */     if (local) {
/* 197 */       return null;
/*     */     }
/* 199 */     AsyncObjectContainer<VersionList.RawVersionList> container = new AsyncObjectContainer();
/*     */     
/* 201 */     for (RemoteVersionList remoteList : this.remoteLists) {
/* 202 */       container.add(new AsyncRawVersionListObject(remoteList));
/*     */     }
/* 204 */     return container.execute();
/*     */   }
/*     */   
/*     */   public void updateVersionList() {
/* 208 */     if (!this.hadRemote) {
/* 209 */       asyncRefresh();
/*     */     } else {
/* 211 */       for (VersionManagerListener listener : this.listeners)
/* 212 */         listener.onVersionsRefreshed(this); 
/*     */     } 
/*     */   }
/*     */   public VersionSyncInfo getVersionSyncInfo(Version version) {
/* 216 */     return getVersionSyncInfo(version.getID());
/*     */   }
/*     */   
/*     */   public VersionSyncInfo getVersionSyncInfo(String name) {
/* 220 */     if (name == null) {
/* 221 */       throw new NullPointerException("Cannot get sync info of NULL!");
/*     */     }
/* 223 */     if (name.startsWith("latest-")) {
/* 224 */       String realID = name.substring(7);
/* 225 */       name = null;
/*     */       
/* 227 */       for (Map.Entry<ReleaseType, Version> entry : this.latestVersions.entrySet()) {
/* 228 */         if (((ReleaseType)entry.getKey()).toString().equals(realID)) {
/* 229 */           name = ((Version)entry.getValue()).getID();
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 234 */       if (name == null) {
/* 235 */         return null;
/*     */       }
/*     */     } 
/* 238 */     Version localVersion = this.localList.getVersion(name);
/*     */     
/* 240 */     if (localVersion instanceof CompleteVersion && ((CompleteVersion)localVersion).getInheritsFrom() != null) {
/*     */       
/*     */       try {
/* 243 */         CompleteVersion completeVersion = ((CompleteVersion)localVersion).resolve(this, false);
/* 244 */       } catch (IOException ioE) {
/* 245 */         U.log(new Object[] { ioE });
/* 246 */         localVersion = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 251 */     Version remoteVersion = null; RemoteVersionList[] arrayOfRemoteVersionList; int i;
/*     */     byte b;
/* 253 */     for (arrayOfRemoteVersionList = this.remoteLists, i = arrayOfRemoteVersionList.length, b = 0; b < i; ) { RemoteVersionList list = arrayOfRemoteVersionList[b];
/* 254 */       Version currentVersion = list.getVersion(name);
/*     */       
/* 256 */       if (currentVersion == null) {
/*     */         b++; continue;
/*     */       } 
/* 259 */       remoteVersion = currentVersion; }
/*     */ 
/*     */ 
/*     */     
/* 263 */     return (localVersion == null && remoteVersion == null) ? null : new VersionSyncInfo(localVersion, remoteVersion);
/*     */   }
/*     */   
/*     */   public LatestVersionSyncInfo getLatestVersionSyncInfo(Version version) {
/* 267 */     if (version == null) {
/* 268 */       throw new NullPointerException("Cannot get latest sync info of NULL!");
/*     */     }
/* 270 */     VersionSyncInfo syncInfo = getVersionSyncInfo(version);
/*     */     
/* 272 */     return new LatestVersionSyncInfo(version.getReleaseType(), syncInfo);
/*     */   }
/*     */   
/*     */   public List<VersionSyncInfo> getVersions(VersionFilter filter, boolean includeLatest) {
/* 276 */     return (List<VersionSyncInfo>)this.currentSyncVersions.stream().filter(v -> 
/* 277 */         !(!includeLatest && v instanceof LatestVersionSyncInfo))
/*     */ 
/*     */       
/* 280 */       .filter(v -> Objects.isNull(filter) ? true : filter.satisfies(v.getAvailableVersion()))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 285 */       .collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public List<VersionSyncInfo> getVersions(boolean includeLatest) {
/* 289 */     return getVersions(
/* 290 */         (TLauncher.getInstance() == null) ? null : TLauncher.getInstance().getConfiguration().getVersionFilter(), includeLatest);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized List<VersionSyncInfo> getVersions() {
/* 295 */     return getVersions(true);
/*     */   }
/*     */   
/*     */   private synchronized List<VersionSyncInfo> getVersions0() {
/* 299 */     List<VersionSyncInfo> plainResult = new ArrayList<>();
/* 300 */     List<VersionSyncInfo> result = new ArrayList<>();
/* 301 */     Map<String, VersionSyncInfo> lookup = new HashMap<>();
/*     */     
/* 303 */     for (Version version : this.latestVersions.values()) {
/* 304 */       LatestVersionSyncInfo syncInfo = getLatestVersionSyncInfo(version);
/*     */       
/* 306 */       if (!result.contains(syncInfo))
/* 307 */         result.add(syncInfo); 
/*     */     } 
/* 309 */     for (Version v : Lists.newArrayList(this.localList.getVersions())) {
/* 310 */       VersionSyncInfo syncInfo = getVersionSyncInfo(v);
/* 311 */       if (syncInfo != null) {
/* 312 */         lookup.put(v.getID(), syncInfo);
/* 313 */         plainResult.add(syncInfo);
/*     */       } 
/*     */     } 
/*     */     
/* 317 */     for (RemoteVersionList remoteList : this.remoteLists) {
/*     */       
/* 319 */       if (Objects.isNull(remoteList))
/* 320 */         U.log(new Object[] { "remote list is null" }); 
/* 321 */       remoteList.getVersions().stream().filter(version -> !lookup.containsKey(version.getID()))
/* 322 */         .forEach(version -> {
/*     */             VersionSyncInfo syncInfo = getVersionSyncInfo(version);
/*     */             lookup.put(version.getID(), syncInfo);
/*     */             plainResult.add(syncInfo);
/*     */           });
/*     */     } 
/* 328 */     plainResult.sort((a, b) -> {
/*     */           Date aDate = a.getLatestVersion().getReleaseTime();
/*     */           
/*     */           Date bDate = b.getLatestVersion().getReleaseTime();
/* 332 */           return (aDate == null || bDate == null) ? 1 : bDate.compareTo(aDate);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 338 */     result.addAll(plainResult);
/*     */     
/* 340 */     return result;
/*     */   }
/*     */   
/*     */   public List<VersionSyncInfo> getInstalledVersions(VersionFilter filter) {
/* 344 */     List<VersionSyncInfo> result = new ArrayList<>();
/*     */     
/* 346 */     for (Version version : this.localList.getVersions()) {
/* 347 */       result.add(getVersionSyncInfo(version));
/*     */     }
/* 349 */     return result;
/*     */   }
/*     */   
/*     */   public List<VersionSyncInfo> getInstalledVersions() {
/* 353 */     return getInstalledVersions(
/* 354 */         (TLauncher.getInstance() == null) ? null : TLauncher.getInstance().getConfiguration().getVersionFilter());
/*     */   }
/*     */ 
/*     */   
/*     */   public VersionSyncInfoContainer downloadVersion(VersionSyncInfo syncInfo, boolean tlauncher, boolean force) throws IOException {
/* 359 */     VersionSyncInfoContainer container = new VersionSyncInfoContainer(syncInfo);
/* 360 */     CompleteVersion completeVersion = syncInfo.getCompleteVersion(force);
/* 361 */     File baseDirectory = this.localList.getBaseDirectory();
/*     */     
/* 363 */     Set<Downloadable> required = syncInfo.getRequiredDownloadables(baseDirectory, force, tlauncher);
/* 364 */     container.addAll(required);
/* 365 */     log(new Object[] { "Required for version " + syncInfo.getID() + ':', required });
/*     */     
/* 367 */     String originalId = completeVersion.getJar();
/*     */     
/* 369 */     Repo repo = ClientInstanceRepo.OFFICIAL_VERSION_REPO;
/* 370 */     String id = completeVersion.getID(), jarFile = "versions/", saveFile = "versions/";
/*     */     
/* 372 */     if (originalId == null) {
/* 373 */       if (Objects.nonNull(syncInfo.getRemote()))
/* 374 */         repo = syncInfo.getRemote().getSource(); 
/* 375 */       jarFile = jarFile + id + "/" + id + ".jar";
/* 376 */       saveFile = jarFile;
/*     */     } else {
/*     */       
/* 379 */       repo = ClientInstanceRepo.OFFICIAL_VERSION_REPO;
/* 380 */       jarFile = jarFile + originalId + "/" + originalId + ".jar";
/* 381 */       saveFile = saveFile + id + "/" + id + ".jar";
/*     */     } 
/* 383 */     File versionFile = new File(baseDirectory, saveFile);
/*     */ 
/*     */     
/* 386 */     if (Objects.nonNull(completeVersion.getDownloads()) && 
/* 387 */       Objects.nonNull(completeVersion.getDownloads().get("client"))) {
/* 388 */       String key = "client_tl_manager";
/*     */       
/* 390 */       CompleteVersion completeVersion1 = TLauncher.getInstance().getTLauncherManager().addFilesForDownloading(completeVersion, false);
/* 391 */       boolean containsKey = Objects.nonNull(completeVersion1.getDownloads().get(key));
/* 392 */       boolean tlAccount = TLauncher.getInstance().getTLauncherManager().applyTLauncherAccountLib(completeVersion);
/* 393 */       addedMinecraftClient(force, container, (MetadataDTO)completeVersion.getDownloads().get("client"), baseDirectory, versionFile, (!containsKey || !tlAccount), completeVersion
/* 394 */           .getModifiedVersion().isSkipHashsumValidation());
/* 395 */       if (containsKey)
/* 396 */         addedMinecraftClient(force, container, (MetadataDTO)completeVersion1.getDownloads().get(key), baseDirectory, versionFile, (containsKey && tlAccount), completeVersion
/* 397 */             .getModifiedVersion().isSkipHashsumValidation()); 
/* 398 */     } else if (!Files.exists((new File(baseDirectory, saveFile)).toPath(), new java.nio.file.LinkOption[0])) {
/* 399 */       String cacheRelatedFolder = "libraries/v1/" + jarFile;
/* 400 */       MetadataDTO client = new MetadataDTO();
/* 401 */       client.setPath(cacheRelatedFolder);
/* 402 */       client.setUrl(jarFile);
/* 403 */       client.setLocalDestination(new File(baseDirectory, cacheRelatedFolder));
/* 404 */       Downloadable d = new Downloadable(repo, client, force);
/* 405 */       d.addAdditionalDestination(new File(baseDirectory, saveFile));
/* 406 */       log(new Object[] { "Jar for " + syncInfo.getID() + ':', d });
/* 407 */       container.add(d);
/*     */     } 
/* 409 */     return container;
/*     */   }
/*     */   
/*     */   public boolean isLocalRefresh() {
/* 413 */     return this.localRefresh;
/*     */   }
/*     */ 
/*     */   
/*     */   private VersionSyncInfoContainer addedMinecraftClient(boolean force, VersionSyncInfoContainer container, MetadataDTO client, File baseDirectory, File versionFile, boolean additionalCopy, boolean skipHashumValidation) throws IOException {
/* 418 */     Repo repo = ClientInstanceRepo.EMPTY_REPO;
/* 419 */     String cacheRelatedFolder = "libraries" + (new URL(client.getUrl())).getPath();
/* 420 */     File cacheFile = new File(baseDirectory, cacheRelatedFolder);
/* 421 */     client.setLocalDestination(cacheFile);
/*     */     
/* 423 */     if (versionFile.exists() && (FileUtil.getChecksum(versionFile).equals(client.getSha1()) || skipHashumValidation))
/* 424 */       return container; 
/* 425 */     if (cacheFile.exists() && (FileUtil.getChecksum(cacheFile).equals(client.getSha1()) || skipHashumValidation)) {
/* 426 */       FileUtil.copyFile(cacheFile.getAbsoluteFile(), versionFile, true);
/* 427 */       return container;
/*     */     } 
/* 429 */     Downloadable d = new Downloadable(repo, client, force);
/* 430 */     container.add(d);
/* 431 */     if (additionalCopy)
/* 432 */       d.addAdditionalDestination(versionFile); 
/* 433 */     return container;
/*     */   }
/*     */   
/*     */   class AsyncRawVersionListObject extends AsyncObject<VersionList.RawVersionList> {
/*     */     private final RemoteVersionList remoteList;
/*     */     
/*     */     AsyncRawVersionListObject(RemoteVersionList remoteList) {
/* 440 */       this.remoteList = remoteList;
/*     */     }
/*     */     
/*     */     RemoteVersionList getVersionList() {
/* 444 */       return this.remoteList;
/*     */     }
/*     */ 
/*     */     
/*     */     protected VersionList.RawVersionList execute() throws AsyncObjectGotErrorException {
/*     */       try {
/* 450 */         return this.remoteList.getRawList();
/* 451 */       } catch (Exception e) {
/* 452 */         VersionManager.this.log(new Object[] { "Error refreshing version list:", e });
/* 453 */         throw new AsyncObjectGotErrorException(this, e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\managers\VersionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */