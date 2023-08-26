/*     */ package org.tlauncher.tlauncher.updater.client;
/*     */ import com.google.gson.annotations.Expose;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import net.minecraft.launcher.process.JavaProcessLauncher;
/*     */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*     */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*     */ import org.tlauncher.tlauncher.downloader.DownloadableHandler;
/*     */ import org.tlauncher.tlauncher.downloader.Downloader;
/*     */ import org.tlauncher.tlauncher.downloader.mods.DownloadableHandlerAdapter;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ 
/*     */ public class Update {
/*     */   @Expose(serialize = false, deserialize = false)
/*  28 */   private final List<UpdateListener> listeners = Collections.synchronizedList(new ArrayList<>());
/*     */   
/*     */   private double version;
/*     */   private double requiredAtLeastFor;
/*     */   protected boolean mandatory;
/*  33 */   private List<String> jarLinks = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  39 */   private final List<String> triedToDownload = new ArrayList<>();
/*  40 */   private List<String> exeLinks = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private AtomicBoolean stateDownloading;
/*     */ 
/*     */   
/*  47 */   protected Map<String, String> description = new HashMap<>(); private int updaterView; private boolean updaterLaterInstall; private List<Offer> offers; private Offer selectedOffer; private int offerDelay; private int offerEmptyCheckboxDelay; private Map<String, List<Banner>> banners;
/*     */   private List<String> rootAccessExe;
/*     */   private Double aboveMandatoryVersion;
/*     */   
/*  51 */   public Offer getSelectedOffer() { return this.selectedOffer; } public void setSelectedOffer(Offer selectedOffer) {
/*  52 */     this.selectedOffer = selectedOffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private Set<Double> mandatoryUpdatedVersions = new HashSet<>();
/*     */   
/*     */   public void setDescription(Map<String, String> description) {
/*  62 */     this.description = description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Expose(serialize = false, deserialize = false)
/*     */   private boolean userChoose = true;
/*     */ 
/*     */ 
/*     */   
/*     */   @Expose(serialize = false, deserialize = false)
/*     */   private boolean freeSpaceEnough = true;
/*     */ 
/*     */   
/*     */   @Expose(serialize = false, deserialize = false)
/*  78 */   protected State state = State.NONE;
/*     */ 
/*     */   
/*     */   @Expose(serialize = false, deserialize = false)
/*  82 */   protected Downloader downloader = getDefaultDownloader();
/*     */ 
/*     */   
/*     */   @Expose(serialize = false, deserialize = false)
/*     */   private Downloadable download;
/*     */ 
/*     */   
/*     */   public Optional<Offer> getOfferByLang(String lang) {
/*  90 */     return this.offers.stream().filter(e -> e.getTopText().containsKey(lang)).findAny();
/*     */   }
/*     */   
/*     */   public Update(double version, Map<String, String> description, List<String> jarList, List<String> exeList) {
/*  94 */     this.version = version;
/*     */     
/*  96 */     if (description != null) {
/*  97 */       this.description.putAll(description);
/*     */     }
/*  99 */     if (exeList != null)
/* 100 */       this.exeLinks.addAll(exeList); 
/* 101 */     if (jarList != null)
/* 102 */       this.jarLinks.addAll(jarList); 
/*     */   }
/*     */   
/*     */   public double getVersion() {
/* 106 */     return this.version;
/*     */   }
/*     */   
/*     */   public void setVersion(double version) {
/* 110 */     this.version = version;
/*     */   }
/*     */   
/*     */   public double getRequiredAtLeastFor() {
/* 114 */     return this.requiredAtLeastFor;
/*     */   }
/*     */   
/*     */   public void setRequiredAtLeastFor(double version) {
/* 118 */     this.requiredAtLeastFor = version;
/*     */   }
/*     */   
/*     */   public Map<String, String> getDescriptionMap() {
/* 122 */     return this.description;
/*     */   }
/*     */   
/*     */   public State getState() {
/* 126 */     return this.state;
/*     */   }
/*     */   
/*     */   protected void setState(State newState) {
/* 130 */     if (newState.ordinal() <= this.state.ordinal() && this.state.ordinal() != (State.values()).length - 1) {
/* 131 */       throw new IllegalStateException("tried to change from " + this.state + " to " + newState);
/*     */     }
/* 133 */     this.state = newState;
/* 134 */     log(new Object[] { "Set state:", newState });
/*     */   }
/*     */   
/*     */   public Downloader getDownloader() {
/* 138 */     return this.downloader;
/*     */   }
/*     */   
/*     */   public void setDownloader(Downloader downloader) {
/* 142 */     this.downloader = downloader;
/*     */   }
/*     */   
/*     */   public boolean isApplicable() {
/* 146 */     if (!this.freeSpaceEnough) {
/* 147 */       return false;
/*     */     }
/* 149 */     if (!this.userChoose)
/* 150 */       return false; 
/* 151 */     return (TLauncher.getVersion() < this.version);
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/* 155 */     if (!this.userChoose)
/* 156 */       return false; 
/* 157 */     return (this.requiredAtLeastFor != 0.0D && TLauncher.getVersion() <= this.requiredAtLeastFor);
/*     */   }
/*     */   
/*     */   public String getDescription(String key) {
/* 161 */     return (this.description == null) ? null : this.description.get(key);
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 165 */     return getDescription(TLauncher.getInstance().getConfiguration().getLocale().toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void download0(PackageType packageType, boolean async) {
/* 173 */     setState(State.DOWNLOADING);
/*     */     
/* 175 */     File destination = new File(FileUtil.getRunningJar().getAbsolutePath() + ".update");
/* 176 */     destination.deleteOnExit();
/* 177 */     log(new Object[] { "dest", destination });
/* 178 */     onUpdateDownloading();
/*     */     
/* 180 */     individualUpdate(packageType, async, destination);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void individualUpdate(PackageType packageType, boolean async, File destination) {
/* 190 */     String pathServer = getLink(packageType);
/* 191 */     MetadataDTO metadataDTO = new MetadataDTO();
/* 192 */     metadataDTO.setUrl(pathServer);
/* 193 */     metadataDTO.setLocalDestination(destination);
/* 194 */     log(new Object[] { "url:", pathServer });
/*     */     
/* 196 */     this.download = new Downloadable(ClientInstanceRepo.EMPTY_REPO, metadataDTO);
/*     */     
/* 198 */     if (this.triedToDownload.size() == calculateListSize(packageType)) {
/* 199 */       this.download.addHandler((DownloadableHandler)new DownloadableHandlerAdapter()
/*     */           {
/*     */             public void onAbort(Downloadable d) {
/* 202 */               Update.this.onUpdateDownloadError(d.getError());
/*     */             }
/*     */ 
/*     */             
/*     */             public void onComplete(Downloadable d) {
/* 207 */               Update.this.onUpdateReady();
/*     */             }
/*     */ 
/*     */             
/*     */             public void onError(Downloadable d, Throwable e) {
/* 212 */               Update.this.onUpdateDownloadError(e);
/*     */             }
/*     */           });
/*     */     } else {
/*     */       
/* 217 */       this.download.addHandler((DownloadableHandler)new DownloadableHandlerAdapter()
/*     */           {
/*     */             public void onAbort(Downloadable d) {
/* 220 */               Update.this.onUpdateDownloadError(d.getError());
/*     */             }
/*     */ 
/*     */             
/*     */             public void onComplete(Downloadable d) {
/* 225 */               Update.this.onUpdateReady();
/*     */             }
/*     */ 
/*     */             
/*     */             public void onError(Downloadable d, Throwable e) {
/* 230 */               Update.this.log(new Object[] { e });
/* 231 */               Update.this.stateDownloading.set(false);
/*     */             }
/*     */           });
/*     */     } 
/*     */     
/* 236 */     this.downloader.add(this.download);
/* 237 */     this.stateDownloading = new AtomicBoolean(true);
/* 238 */     if (async) {
/* 239 */       this.downloader.startDownload();
/*     */     } else {
/* 241 */       this.downloader.startDownloadAndWait();
/* 242 */     }  if (!this.stateDownloading.get() && this.triedToDownload.size() != calculateListSize(packageType)) {
/* 243 */       individualUpdate(packageType, async, destination);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getLink(PackageType packageType) {
/* 253 */     switch (packageType) {
/*     */       case EXE:
/* 255 */         return findLink(this.exeLinks);
/*     */       case JAR:
/* 257 */         return findLink(this.jarLinks);
/*     */     } 
/* 259 */     throw new NullPointerException("incorrect PackageType");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int calculateListSize(PackageType packageType) {
/* 267 */     switch (packageType) {
/*     */       case EXE:
/* 269 */         return this.exeLinks.size();
/*     */       case JAR:
/* 271 */         return this.jarLinks.size();
/*     */     } 
/* 273 */     throw new NullPointerException("incorrect PackageType");
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
/*     */   private String findLink(List<String> list) {
/* 285 */     Random r = new Random();
/*     */     while (true) {
/* 287 */       String link = list.get(r.nextInt(list.size()));
/* 288 */       if (!this.triedToDownload.contains(link)) {
/* 289 */         this.triedToDownload.add(link);
/* 290 */         return link;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void download(PackageType type, boolean async) {
/*     */     try {
/* 297 */       download0(type, async);
/* 298 */     } catch (Throwable t) {
/* 299 */       onUpdateError(t);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void download(boolean async) {
/* 304 */     download(PackageType.CURRENT, async);
/*     */   }
/*     */   
/*     */   public void download() {
/* 308 */     download(false);
/*     */   }
/*     */   
/*     */   public void asyncDownload() {
/* 312 */     download(true);
/*     */   }
/*     */   
/*     */   private void apply0() throws Throwable {
/* 316 */     setState(State.APPLYING);
/*     */     
/* 318 */     JavaProcessLauncher javaProcessLauncher = Bootstrapper.restartLauncher();
/* 319 */     File replace = FileUtil.getRunningJar(), replaceWith = this.download.getMetadataDTO().getLocalDestination();
/* 320 */     ProcessBuilder builder = javaProcessLauncher.createProcess();
/* 321 */     onUpdateApplying();
/*     */     
/* 323 */     InputStream in = new FileInputStream(replaceWith);
/* 324 */     OutputStream out = new FileOutputStream(replace);
/*     */     
/* 326 */     byte[] buffer = new byte[2048];
/*     */     
/* 328 */     int read = in.read(buffer);
/*     */     
/* 330 */     while (read > 0) {
/* 331 */       out.write(buffer, 0, read);
/* 332 */       read = in.read(buffer);
/*     */     } 
/*     */     
/*     */     try {
/* 336 */       in.close();
/* 337 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/*     */     try {
/* 341 */       out.close();
/* 342 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/*     */     try {
/* 346 */       builder.start();
/* 347 */     } catch (Throwable t) {
/* 348 */       log(new Object[] { t });
/*     */     } 
/*     */     
/* 351 */     TLauncher.kill();
/*     */   }
/*     */ 
/*     */   
/*     */   public void apply() {
/*     */     try {
/* 357 */       File file = FileUtil.getRunningJar();
/* 358 */       FileUtils.copyFile(file, new File(file.getParentFile(), "Old-" + file.getName()));
/* 359 */       TlauncherUtil.clearTimeLabel();
/* 360 */       apply0();
/* 361 */     } catch (Throwable t) {
/* 362 */       onUpdateApplyError(t);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addListener(UpdateListener l) {
/* 367 */     this.listeners.add(l);
/*     */   }
/*     */   
/*     */   public void removeListener(UpdateListener l) {
/* 371 */     this.listeners.remove(l);
/*     */   }
/*     */   
/*     */   private void onUpdateError(Throwable e) {
/* 375 */     setState(State.ERRORED);
/*     */     
/* 377 */     synchronized (this.listeners) {
/* 378 */       for (UpdateListener l : this.listeners)
/* 379 */         l.onUpdateError(this, e); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void onUpdateDownloading() {
/* 384 */     synchronized (this.listeners) {
/* 385 */       for (UpdateListener l : this.listeners)
/* 386 */         l.onUpdateDownloading(this); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void onUpdateDownloadError(Throwable e) {
/* 391 */     setState(State.ERRORED);
/*     */     
/* 393 */     synchronized (this.listeners) {
/* 394 */       for (UpdateListener l : this.listeners)
/* 395 */         l.onUpdateDownloadError(this, e); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void onUpdateReady() {
/* 400 */     setState(State.READY);
/*     */     
/* 402 */     synchronized (this.listeners) {
/* 403 */       for (UpdateListener l : this.listeners)
/* 404 */         l.onUpdateReady(this); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void onUpdateApplying() {
/* 409 */     synchronized (this.listeners) {
/* 410 */       for (UpdateListener l : this.listeners)
/* 411 */         l.onUpdateApplying(this); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void onUpdateApplyError(Throwable e) {
/* 416 */     setState(State.ERRORED);
/*     */     
/* 418 */     synchronized (this.listeners) {
/* 419 */       for (UpdateListener l : this.listeners)
/* 420 */         l.onUpdateApplyError(this, e); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Downloader getDefaultDownloader() {
/* 425 */     return TLauncher.getInstance().getDownloader();
/*     */   }
/*     */   
/*     */   protected void log(Object... o) {
/* 429 */     U.log(new Object[] { "[Update:" + this.version + "]", o });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getJarLinks() {
/* 440 */     return this.jarLinks;
/*     */   }
/*     */   public boolean isMandatory() {
/* 443 */     if (this.mandatory)
/* 444 */       return true; 
/* 445 */     Double v1 = Double.valueOf(TLauncher.getVersion());
/* 446 */     if (Objects.nonNull(this.aboveMandatoryVersion) && this.aboveMandatoryVersion.compareTo(v1) > 0)
/* 447 */       return true; 
/* 448 */     return (Objects.nonNull(this.mandatoryUpdatedVersions) && this.mandatoryUpdatedVersions.contains(v1));
/*     */   }
/*     */   
/*     */   public void setMandatory(boolean mandatory) {
/* 452 */     this.mandatory = mandatory;
/*     */   }
/*     */   
/*     */   public boolean isUserChoose() {
/* 456 */     return this.userChoose;
/*     */   }
/*     */   
/*     */   public void setUserChoose(boolean userChoose) {
/* 460 */     this.userChoose = userChoose;
/*     */   }
/*     */   
/*     */   public String getlastDownloadedLink() {
/* 464 */     if (this.triedToDownload.size() > 0) {
/* 465 */       return this.triedToDownload.get(this.triedToDownload.size() - 1);
/*     */     }
/* 467 */     return "";
/*     */   }
/*     */   
/*     */   public boolean isFreeSpaceEnough() {
/* 471 */     return this.freeSpaceEnough;
/*     */   }
/*     */   
/*     */   public void setFreeSpaceEnough(boolean freeSpaceEnough) {
/* 475 */     this.freeSpaceEnough = freeSpaceEnough;
/*     */   }
/*     */   
/*     */   public void setJarLinks(List<String> jarLinks) {
/* 479 */     this.jarLinks = jarLinks;
/*     */   }
/*     */   
/*     */   public List<String> getExeLinks() {
/* 483 */     return this.exeLinks;
/*     */   }
/*     */   
/*     */   public void setExeLinks(List<String> exeLinks) {
/* 487 */     this.exeLinks = exeLinks;
/*     */   }
/*     */   
/*     */   public List<Offer> getOffers() {
/* 491 */     return this.offers;
/*     */   }
/*     */   
/*     */   public void setOffers(List<Offer> offers) {
/* 495 */     this.offers = offers;
/*     */   }
/*     */   
/*     */   public Map<String, List<Banner>> getBanners() {
/* 499 */     return this.banners;
/*     */   }
/*     */   
/*     */   public int getUpdaterView() {
/* 503 */     return this.updaterView;
/*     */   }
/*     */   
/*     */   public void setBanners(Map<String, List<Banner>> banners) {
/* 507 */     this.banners = banners;
/*     */   }
/*     */   
/*     */   public void setUpdaterView(int updaterView) {
/* 511 */     this.updaterView = updaterView;
/*     */   }
/*     */   
/*     */   public boolean isUpdaterLaterInstall() {
/* 515 */     return this.updaterLaterInstall;
/*     */   }
/*     */   
/*     */   public void setUpdaterLaterInstall(boolean updaterLaterInstall) {
/* 519 */     this.updaterLaterInstall = updaterLaterInstall;
/*     */   }
/*     */   
/*     */   public int getOfferDelay() {
/* 523 */     return this.offerDelay;
/*     */   }
/*     */   
/*     */   public void setOfferDelay(int offerDelay) {
/* 527 */     this.offerDelay = offerDelay;
/*     */   }
/*     */   
/*     */   public int getOfferEmptyCheckboxDelay() {
/* 531 */     return this.offerEmptyCheckboxDelay;
/*     */   }
/*     */   
/*     */   public void setOfferEmptyCheckboxDelay(int offerEmptyCheckboxDelay) {
/* 535 */     this.offerEmptyCheckboxDelay = offerEmptyCheckboxDelay;
/*     */   }
/*     */   
/*     */   public List<String> getRootAccessExe() {
/* 539 */     return this.rootAccessExe;
/*     */   }
/*     */   
/*     */   public void setRootAccessExe(List<String> rootAccessExe) {
/* 543 */     this.rootAccessExe = rootAccessExe;
/*     */   }
/*     */   
/*     */   public Double getAboveMandatoryVersion() {
/* 547 */     return this.aboveMandatoryVersion;
/*     */   }
/*     */   
/*     */   public void setAboveMandatoryVersion(Double aboveMandatoryVersion) {
/* 551 */     this.aboveMandatoryVersion = aboveMandatoryVersion;
/*     */   }
/*     */   
/*     */   public Set<Double> getMandatoryUpdatedVersions() {
/* 555 */     return this.mandatoryUpdatedVersions;
/*     */   }
/*     */   
/*     */   public void setMandatoryUpdatedVersions(Set<Double> mandatoryUpdatedVersions) {
/* 559 */     this.mandatoryUpdatedVersions = mandatoryUpdatedVersions;
/*     */   }
/*     */   public Update() {}
/*     */   
/* 563 */   public enum State { NONE, DOWNLOADING, READY, APPLYING, ERRORED; }
/*     */ 
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\client\Update.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */