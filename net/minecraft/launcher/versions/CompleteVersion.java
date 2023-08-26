/*     */ package net.minecraft.launcher.versions;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import com.google.gson.TypeAdapterFactory;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import net.minecraft.launcher.updater.VersionList;
/*     */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*     */ import net.minecraft.launcher.versions.json.Argument;
/*     */ import net.minecraft.launcher.versions.json.ArgumentType;
/*     */ import net.minecraft.launcher.versions.json.DateTypeAdapter;
/*     */ import net.minecraft.launcher.versions.json.LowerCaseEnumTypeAdapterFactory;
/*     */ import net.minecraft.launcher.versions.json.RepoTypeAdapter;
/*     */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*     */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*     */ import org.tlauncher.tlauncher.managers.VersionManager;
/*     */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*     */ import org.tlauncher.tlauncher.repository.Repo;
/*     */ import org.tlauncher.util.MinecraftUtil;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.TlauncherUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.gson.serializer.ModpackDTOTypeAdapter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompleteVersion
/*     */   implements Cloneable, Version
/*     */ {
/*     */   String id;
/*     */   String inheritsFrom;
/*     */   Date time;
/*     */   Date releaseTime;
/*     */   ReleaseType type;
/*     */   String jvmArguments;
/*     */   String minecraftArguments;
/*     */   String mainClass;
/*  67 */   Integer minimumLauncherVersion = Integer.valueOf(0);
/*     */   Map<ArgumentType, List<Argument>> arguments;
/*     */   String assets;
/*     */   VersionList list;
/*     */   List<Library> libraries;
/*     */   List<Rule> rules;
/*     */   List<String> deleteEntries;
/*     */   private AssetsMetadata assetIndex;
/*     */   Map<String, MetadataDTO> downloads;
/*     */   private Double complianceLevel;
/*     */   private JavaVersionName javaVersion;
/*     */   private Map<String, LogClient> logging;
/*     */   transient ModifiedVersion modifiedVersion;
/*     */   
/*  81 */   public Map<String, LogClient> getLogging() { return this.logging; } public void setLogging(Map<String, LogClient> logging) {
/*  82 */     this.logging = logging;
/*     */   }
/*     */   public void setModifiedVersion(ModifiedVersion modifiedVersion) {
/*  85 */     this.modifiedVersion = modifiedVersion;
/*     */   }
/*     */   
/*     */   public CompleteVersion() {
/*  89 */     this.modifiedVersion = new ModifiedVersion();
/*     */   }
/*     */   
/*     */   public String getRemoteVersion() {
/*  93 */     return this.modifiedVersion.getRemoteVersion();
/*     */   }
/*     */   
/*     */   public ModifiedVersion getModifiedVersion() {
/*  97 */     return this.modifiedVersion;
/*     */   }
/*     */   
/*     */   public boolean isModpack() {
/* 101 */     return (this.modifiedVersion.getModpack() != null);
/*     */   } public static class AssetsMetadata {
/*     */     private String id; private int totalSize; protected String name;
/* 104 */     public void setId(String id) { this.id = id; } protected long size; protected String path; protected String url; public void setTotalSize(int totalSize) { this.totalSize = totalSize; } public void setName(String name) { this.name = name; } public void setSize(long size) { this.size = size; } public void setPath(String path) { this.path = path; } public void setUrl(String url) { this.url = url; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof AssetsMetadata)) return false;  AssetsMetadata other = (AssetsMetadata)o; if (!other.canEqual(this)) return false;  Object this$id = getId(), other$id = other.getId(); if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;  if (getTotalSize() != other.getTotalSize()) return false;  Object this$name = getName(), other$name = other.getName(); if ((this$name == null) ? (other$name != null) : !this$name.equals(other$name)) return false;  if (getSize() != other.getSize()) return false;  Object this$path = getPath(), other$path = other.getPath(); if ((this$path == null) ? (other$path != null) : !this$path.equals(other$path)) return false;  Object this$url = getUrl(), other$url = other.getUrl(); return !((this$url == null) ? (other$url != null) : !this$url.equals(other$url)); } protected boolean canEqual(Object other) { return other instanceof AssetsMetadata; } public int hashCode() { int PRIME = 59; result = 1; Object $id = getId(); result = result * 59 + (($id == null) ? 43 : $id.hashCode()); result = result * 59 + getTotalSize(); Object $name = getName(); result = result * 59 + (($name == null) ? 43 : $name.hashCode()); long $size = getSize(); result = result * 59 + (int)($size >>> 32L ^ $size); Object $path = getPath(); result = result * 59 + (($path == null) ? 43 : $path.hashCode()); Object $url = getUrl(); return result * 59 + (($url == null) ? 43 : $url.hashCode()); } public String toString() { return "CompleteVersion.AssetsMetadata(id=" + getId() + ", totalSize=" + getTotalSize() + ", name=" + getName() + ", size=" + getSize() + ", path=" + getPath() + ", url=" + getUrl() + ")"; }
/*     */ 
/*     */     
/* 107 */     public String getId() { return this.id; }
/* 108 */     public int getTotalSize() { return this.totalSize; }
/* 109 */     public String getName() { return this.name; }
/* 110 */     public long getSize() { return this.size; }
/* 111 */     public String getPath() { return this.path; } public String getUrl() {
/* 112 */       return this.url;
/*     */     } }
/*     */   
/*     */   public AssetsMetadata getAssetIndex() {
/* 116 */     return this.assetIndex;
/*     */   }
/*     */   
/*     */   public void setAssetIndex(AssetsMetadata assetIndex) {
/* 120 */     this.assetIndex = assetIndex;
/*     */   }
/*     */   
/*     */   public Map<String, MetadataDTO> getDownloads() {
/* 124 */     return this.downloads;
/*     */   }
/*     */   
/*     */   public void setDownloads(Map<String, MetadataDTO> downloads) {
/* 128 */     this.downloads = downloads;
/*     */   }
/*     */   
/*     */   public List<Library> getModsLibraries() {
/* 132 */     return this.modifiedVersion.getModsLibraries();
/*     */   }
/*     */   
/*     */   public void setModsLibraries(List<Library> modsLibraries) {
/* 136 */     this.modifiedVersion.setModsLibraries(modsLibraries);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getID() {
/* 141 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setID(String id) {
/* 146 */     if (id == null || id.isEmpty())
/* 147 */       throw new IllegalArgumentException("ID is NULL or empty"); 
/* 148 */     this.id = id;
/*     */   }
/*     */ 
/*     */   
/*     */   public ReleaseType getReleaseType() {
/* 153 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(ReleaseType type) {
/* 157 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public Repo getSource() {
/* 162 */     return this.modifiedVersion.getSource();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSource(Repo repo) {
/* 167 */     if (repo == null)
/* 168 */       throw new NullPointerException(); 
/* 169 */     this.modifiedVersion.setSource(repo);
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getUpdatedTime() {
/* 174 */     if (this.modifiedVersion.getUpdatedTime() != null)
/* 175 */       return this.modifiedVersion.getUpdatedTime(); 
/* 176 */     return this.time;
/*     */   }
/*     */   
/*     */   public void setUpdatedTime(Date time) {
/* 180 */     if (time == null)
/* 181 */       throw new NullPointerException("Time is NULL!"); 
/* 182 */     this.modifiedVersion.setUpdatedTime(time);
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getReleaseTime() {
/* 187 */     return this.releaseTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public VersionList getVersionList() {
/* 192 */     return this.list;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVersionList(VersionList list) {
/* 197 */     if (list == null) {
/* 198 */       throw new NullPointerException("VersionList cannot be NULL!");
/*     */     }
/* 200 */     this.list = list;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSkinVersion() {
/* 205 */     return this.modifiedVersion.isSkinVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSkinVersion(boolean skinVersion) {
/* 210 */     this.modifiedVersion.setSkinVersion(skinVersion);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUrl() {
/* 215 */     return this.modifiedVersion.getUrl();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUrl(String url) {
/* 220 */     this.modifiedVersion.setUrl(url);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getJar() {
/* 225 */     return this.modifiedVersion.getJar();
/*     */   }
/*     */   
/*     */   public String getInheritsFrom() {
/* 229 */     return this.inheritsFrom;
/*     */   }
/*     */   
/*     */   public String getJVMArguments() {
/* 233 */     return this.jvmArguments;
/*     */   }
/*     */   
/*     */   public String getMinecraftArguments() {
/* 237 */     return this.minecraftArguments;
/*     */   }
/*     */   
/*     */   public void setMinecraftArguments(String args) {
/* 241 */     this.minecraftArguments = args;
/*     */   }
/*     */   
/*     */   public String getMainClass() {
/* 245 */     return this.mainClass;
/*     */   }
/*     */   
/*     */   public void setMainClass(String clazz) {
/* 249 */     this.mainClass = clazz;
/*     */   }
/*     */   
/*     */   public List<Library> getLibraries() {
/* 253 */     return this.libraries;
/*     */   }
/*     */   
/*     */   public List<Rule> getRules() {
/* 257 */     return Collections.unmodifiableList(this.rules);
/*     */   }
/*     */   
/*     */   public List<String> getDeleteEntries() {
/* 261 */     return this.deleteEntries;
/*     */   }
/*     */   
/*     */   public int getMinimumLauncherVersion() {
/* 265 */     return this.minimumLauncherVersion.intValue();
/*     */   }
/*     */   
/*     */   public int getMinimumCustomLauncherVersion() {
/* 269 */     return this.modifiedVersion.getTlauncherVersion().intValue();
/*     */   }
/*     */   
/*     */   public String getAssets() {
/* 273 */     return (this.assets == null) ? "legacy" : this.assets;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 278 */     if (this == o)
/* 279 */       return true; 
/* 280 */     if (o == null)
/* 281 */       return false; 
/* 282 */     if (hashCode() == o.hashCode()) {
/* 283 */       return true;
/*     */     }
/* 285 */     if (!(o instanceof Version)) {
/* 286 */       return false;
/*     */     }
/* 288 */     Version compare = (Version)o;
/* 289 */     if (compare.getID() == null) {
/* 290 */       return false;
/*     */     }
/* 292 */     return compare.getID().equals(this.id);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 297 */     return getClass().getSimpleName() + "{id='" + this.id + "', time=" + this.time + ", release=" + this.releaseTime + ", type=" + this.type + ", class=" + this.mainClass + ", minimumVersion=" + this.minimumLauncherVersion + ", assets='" + this.assets + "', source=" + this.modifiedVersion
/*     */       
/* 299 */       .getSource() + ", list=" + this.list + ", libraries=" + this.libraries + "}";
/*     */   }
/*     */   
/*     */   public File getFile(File base) {
/* 303 */     return new File(base, "versions/" + getID() + "/" + getID() + ".jar");
/*     */   }
/*     */   
/*     */   public boolean appliesToCurrentEnvironment() {
/* 307 */     if (this.rules == null) {
/* 308 */       return true;
/*     */     }
/* 310 */     for (Rule rule : this.rules) {
/* 311 */       Rule.Action action = rule.getAppliedAction();
/*     */       
/* 313 */       if (action == Rule.Action.DISALLOW) {
/* 314 */         return false;
/*     */       }
/*     */     } 
/* 317 */     return true;
/*     */   }
/*     */   
/*     */   public Collection<Library> getRelevantLibraries() {
/* 321 */     List<Library> result = new ArrayList<>();
/*     */     
/* 323 */     for (Library library : this.libraries) {
/* 324 */       if (library.appliesToCurrentEnvironment()) {
/* 325 */         result.add(library);
/*     */       }
/*     */     } 
/* 328 */     return result;
/*     */   }
/*     */   
/*     */   public Collection<File> getClassPath(OS os, File base) {
/* 332 */     Collection<Library> libraries = getRelevantLibraries();
/* 333 */     Collection<File> result = new ArrayList<>();
/*     */     
/* 335 */     for (Library library : libraries) {
/* 336 */       if (library.getNatives() == null) {
/* 337 */         result.add(new File(base, "libraries/" + library.getArtifactPath()));
/*     */       }
/*     */     } 
/* 340 */     result.add(new File(base, "versions/" + getID() + "/" + getID() + ".jar"));
/*     */     
/* 342 */     return result;
/*     */   }
/*     */   
/*     */   public Collection<File> getClassPath(File base) {
/* 346 */     return getClassPath(OS.CURRENT, base);
/*     */   }
/*     */   
/*     */   public Collection<String> getNatives(OS os) {
/* 350 */     Collection<Library> libraries = getRelevantLibraries();
/* 351 */     Collection<String> result = new ArrayList<>();
/*     */     
/* 353 */     for (Library library : libraries) {
/* 354 */       Map<OS, String> natives = library.getNatives();
/*     */       
/* 356 */       if (natives != null && natives.containsKey(os)) {
/* 357 */         result.add("libraries/" + library.getArtifactPath(natives.get(os)));
/*     */       }
/*     */     } 
/* 360 */     return result;
/*     */   }
/*     */   
/*     */   public Collection<String> getNatives() {
/* 364 */     return getNatives(OS.CURRENT);
/*     */   }
/*     */   
/*     */   public Set<String> getRequiredFiles(OS os) {
/* 368 */     Set<String> neededFiles = new HashSet<>();
/*     */     
/* 370 */     for (Library library : getRelevantLibraries()) {
/* 371 */       if (library.getNatives() != null) {
/* 372 */         String natives = library.getNatives().get(os);
/*     */         
/* 374 */         if (natives != null)
/* 375 */           neededFiles.add("libraries/" + library.getArtifactPath(natives)); 
/*     */         continue;
/*     */       } 
/* 378 */       neededFiles.add("libraries/" + library.getArtifactPath());
/*     */     } 
/*     */ 
/*     */     
/* 382 */     return neededFiles;
/*     */   }
/*     */   
/*     */   public Collection<String> getExtractFiles(OS os) {
/* 386 */     Collection<Library> libraries = getRelevantLibraries();
/* 387 */     Collection<String> result = new ArrayList<>();
/*     */     
/* 389 */     for (Library library : libraries) {
/* 390 */       Map<OS, String> natives = library.getNatives();
/*     */       
/* 392 */       if (natives != null && natives.containsKey(os)) {
/* 393 */         result.add("libraries/" + library.getArtifactPath(natives.get(os)));
/*     */       }
/*     */     } 
/* 396 */     return result;
/*     */   }
/*     */   
/*     */   public CompleteVersion resolve(VersionManager vm, boolean useLatest) throws IOException {
/* 400 */     return resolve(vm, useLatest, new ArrayList<>());
/*     */   }
/*     */   
/*     */   protected CompleteVersion resolve(VersionManager vm, boolean useLatest, List<String> inheristance) throws IOException {
/*     */     CompleteVersion result;
/* 405 */     if (vm == null) {
/* 406 */       throw new NullPointerException("version manager");
/*     */     }
/* 408 */     if (this.inheritsFrom == null) {
/* 409 */       return this;
/*     */     }
/* 411 */     log(new Object[] { "Resolving..." });
/*     */     
/* 413 */     if (inheristance.contains(this.id)) {
/* 414 */       throw new IllegalArgumentException(this.id + " should be already resolved.");
/*     */     }
/* 416 */     inheristance.add(this.id);
/*     */     
/* 418 */     log(new Object[] { "Inherits from", this.inheritsFrom });
/*     */     
/* 420 */     VersionSyncInfo parentSyncInfo = vm.getVersionSyncInfo(this.inheritsFrom);
/*     */ 
/*     */     
/*     */     try {
/* 424 */       if (parentSyncInfo == null) {
/* 425 */         return null;
/*     */       }
/* 427 */       result = (CompleteVersion)parentSyncInfo.getCompleteVersion(useLatest).resolve(vm, useLatest, inheristance).clone();
/* 428 */     } catch (CloneNotSupportedException e) {
/* 429 */       throw new RuntimeException(e);
/*     */     } 
/* 431 */     return partCopyInto(result);
/*     */   }
/*     */   
/*     */   private CompleteVersion partCopyInto(CompleteVersion result) {
/* 435 */     result.id = this.id;
/* 436 */     result.modifiedVersion = new ModifiedVersion();
/* 437 */     if (this.modifiedVersion.getJar() != null) {
/* 438 */       result.modifiedVersion.setJar(this.modifiedVersion.getJar());
/*     */     }
/* 440 */     result.inheritsFrom = null;
/*     */     
/* 442 */     if (this.time.getTime() != 0L) {
/* 443 */       result.time = this.time;
/*     */     }
/* 445 */     result.type = this.type;
/*     */     
/* 447 */     if (this.jvmArguments != null) {
/* 448 */       result.jvmArguments = this.jvmArguments;
/*     */     }
/* 450 */     if (this.minecraftArguments != null)
/* 451 */       result.minecraftArguments = this.minecraftArguments; 
/* 452 */     if (Objects.nonNull(result.arguments) || Objects.nonNull(this.arguments)) {
/* 453 */       Map<ArgumentType, List<Argument>> args = new HashMap<>();
/* 454 */       args.put(ArgumentType.GAME, new ArrayList<>());
/* 455 */       args.put(ArgumentType.JVM, new ArrayList<>());
/* 456 */       if (Objects.nonNull(result.arguments))
/* 457 */         result.arguments.forEach((key, value) -> ((List)args.get(key)).addAll(value)); 
/* 458 */       if (Objects.nonNull(this.arguments))
/* 459 */         this.arguments.forEach((key, value) -> ((List)args.get(key)).addAll(value)); 
/* 460 */       result.arguments = args;
/*     */     } 
/* 462 */     if (this.mainClass != null) {
/* 463 */       result.mainClass = this.mainClass;
/*     */     }
/* 465 */     if (this.libraries != null) {
/* 466 */       List<Library> newLibraries = new ArrayList<>(this.libraries);
/* 467 */       if (result.libraries != null) {
/* 468 */         newLibraries.addAll(result.libraries);
/*     */       }
/* 470 */       result.libraries = newLibraries;
/*     */     } 
/*     */     
/* 473 */     if (this.rules != null) {
/* 474 */       if (result.rules != null) {
/* 475 */         result.rules.addAll(this.rules);
/*     */       } else {
/* 477 */         List<Rule> rulesCopy = new ArrayList<>(this.rules.size());
/* 478 */         Collections.copy(rulesCopy, this.rules);
/* 479 */         result.rules = this.rules;
/*     */       } 
/*     */     }
/* 482 */     if (this.deleteEntries != null) {
/* 483 */       if (result.deleteEntries != null) {
/* 484 */         result.deleteEntries.addAll(this.deleteEntries);
/*     */       } else {
/* 486 */         result.deleteEntries = new ArrayList<>(this.deleteEntries);
/*     */       } 
/*     */     }
/* 489 */     if (this.minimumLauncherVersion.intValue() != 0) {
/* 490 */       result.minimumLauncherVersion = this.minimumLauncherVersion;
/*     */     }
/* 492 */     if (this.assets != null && !this.assets.equals("legacy")) {
/* 493 */       result.assets = this.assets;
/*     */     }
/* 495 */     result.setSkinVersion(isSkinVersion());
/*     */     
/* 497 */     if (this.assetIndex != null) {
/* 498 */       result.setAssetIndex(getAssetIndex());
/*     */     }
/* 500 */     if (getDownloads() != null) {
/* 501 */       result.setDownloads(getDownloads());
/*     */     }
/*     */     
/* 504 */     if (this.modifiedVersion.getModsLibraries() != null) {
/* 505 */       List<Library> newLibraries = new ArrayList<>(this.modifiedVersion.getModsLibraries());
/*     */       
/* 507 */       if (result.modifiedVersion.getModsLibraries() != null) {
/* 508 */         newLibraries.addAll(result.modifiedVersion.getModsLibraries());
/*     */       }
/* 510 */       result.modifiedVersion.setModsLibraries(newLibraries);
/*     */     } 
/* 512 */     if (this.modifiedVersion.getModpack() != null) {
/*     */       
/* 514 */       ModpackDTO newModpack = new ModpackDTO();
/*     */       
/* 516 */       this.modifiedVersion.getModpack().copy(newModpack);
/* 517 */       result.modifiedVersion.setModpack(newModpack);
/*     */     } 
/* 519 */     if (result.releaseTime == null) {
/* 520 */       result.releaseTime = this.releaseTime;
/*     */     }
/* 522 */     if (Objects.nonNull(this.modifiedVersion.getAdditionalFiles())) {
/* 523 */       result.modifiedVersion.setAdditionalFiles(new ArrayList<>(this.modifiedVersion.getAdditionalFiles()));
/*     */     }
/* 525 */     result.modifiedVersion.setActivateSkinCapeForUserVersion(this.modifiedVersion.isActivateSkinCapeForUserVersion());
/* 526 */     result.modifiedVersion.setRemoteVersion(getRemoteVersion());
/* 527 */     result.modifiedVersion.setUpdatedTime(getModifiedVersion().getUpdatedTime());
/* 528 */     if (Objects.nonNull(getDownloads()))
/* 529 */       result.setDownloads(new HashMap<>(getDownloads())); 
/* 530 */     result.list = this.list;
/* 531 */     if (Objects.nonNull(getJavaVersion()))
/* 532 */       result.setJavaVersion(getJavaVersion()); 
/* 533 */     if (Objects.nonNull(getLogging()) && Objects.nonNull(getLogging().get("client")))
/* 534 */       result.setLogging(getLogging()); 
/* 535 */     return result;
/*     */   }
/*     */   
/*     */   public CompleteVersion fullCopy(CompleteVersion c) {
/* 539 */     partCopyInto(c);
/* 540 */     c.inheritsFrom = this.inheritsFrom;
/* 541 */     return c;
/*     */   }
/*     */   
/*     */   public Map<ArgumentType, List<Argument>> getArguments() {
/* 545 */     return this.arguments;
/*     */   }
/*     */   
/*     */   public void setArguments(Map<ArgumentType, List<Argument>> arguments) {
/* 549 */     this.arguments = arguments;
/*     */   }
/*     */   
/*     */   private void log(Object... o) {
/* 553 */     U.log(new Object[] { "[Version:" + this.id + "]", o });
/*     */   }
/*     */   
/*     */   public ModpackDTO getModpack() {
/* 557 */     return this.modifiedVersion.getModpack();
/*     */   }
/*     */   
/*     */   public void setModpackDTO(ModpackDTO modpack) {
/* 561 */     this.modifiedVersion.setModpack(modpack);
/*     */   }
/*     */   
/*     */   public List<MetadataDTO> getAdditionalFiles() {
/* 565 */     return this.modifiedVersion.getAdditionalFiles();
/*     */   }
/*     */   
/*     */   public void setAdditionalFiles(List<MetadataDTO> additionalFiles) {
/* 569 */     this.modifiedVersion.setAdditionalFiles(additionalFiles);
/*     */   }
/*     */   
/*     */   public static class CompleteVersionSerializer
/*     */     implements JsonSerializer<CompleteVersion>, JsonDeserializer<CompleteVersion>
/*     */   {
/*     */     private final Gson remoteContext;
/*     */     
/*     */     public CompleteVersionSerializer() {
/* 578 */       GsonBuilder remoteBuilder = new GsonBuilder();
/* 579 */       remoteBuilder.registerTypeAdapter(Date.class, new DateTypeAdapter());
/* 580 */       remoteBuilder.registerTypeAdapter(Argument.class, new Argument.Serializer());
/* 581 */       remoteBuilder.registerTypeAdapter(Library.class, new Library.LibrarySerializer());
/* 582 */       remoteBuilder.registerTypeAdapter(Date.class, new DateTypeAdapter());
/* 583 */       remoteBuilder.registerTypeAdapterFactory((TypeAdapterFactory)new LowerCaseEnumTypeAdapterFactory());
/* 584 */       remoteBuilder.registerTypeAdapter(Repo.class, new RepoTypeAdapter());
/* 585 */       remoteBuilder.registerTypeAdapter(ModpackDTO.class, new ModpackDTOTypeAdapter());
/* 586 */       remoteBuilder.enableComplexMapKeySerialization();
/* 587 */       remoteBuilder.disableHtmlEscaping();
/* 588 */       remoteBuilder.setPrettyPrinting();
/*     */       
/* 590 */       this.remoteContext = remoteBuilder.create();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public CompleteVersion deserialize(JsonElement elem, Type type, JsonDeserializationContext context) throws JsonParseException {
/* 596 */       JsonObject object = elem.getAsJsonObject();
/* 597 */       JsonElement originalId = object.get("original_id");
/*     */       
/* 599 */       if (Objects.nonNull(object.get("userConfigSkinVersion")))
/* 600 */         object.remove("userConfigSkinVersion"); 
/* 601 */       if (originalId != null && originalId.isJsonPrimitive()) {
/* 602 */         String str = originalId.getAsString();
/*     */         
/* 604 */         object.remove("original_id");
/* 605 */         object.addProperty("jar", str);
/*     */       } 
/* 607 */       if (Objects.nonNull(object.get("inheritsFrom")) && Objects.isNull(object.get("jar"))) {
/* 608 */         object.addProperty("jar", object.get("inheritsFrom").getAsString());
/*     */       }
/* 610 */       JsonElement unnecessaryEntries = object.get("unnecessaryEntries");
/*     */       
/* 612 */       if (unnecessaryEntries != null && unnecessaryEntries.isJsonArray()) {
/* 613 */         object.remove("unnecessaryEntries");
/* 614 */         object.add("deleteEntries", unnecessaryEntries);
/*     */       } 
/* 616 */       CompleteVersion version = (CompleteVersion)this.remoteContext.fromJson((JsonElement)object, CompleteVersion.class);
/* 617 */       if (version.id == null) {
/* 618 */         throw new JsonParseException("Version ID is NULL!");
/*     */       }
/* 620 */       if (version.type == null) {
/* 621 */         version.type = ReleaseType.UNKNOWN;
/*     */       }
/* 623 */       JsonElement jar = object.get("jar");
/* 624 */       if (jar == null)
/* 625 */         object.remove("downloadJarLibraries"); 
/* 626 */       if (object.has("userConfigSkinVersion")) {
/* 627 */         object.remove("userConfigSkinVersion");
/*     */       }
/*     */       
/* 630 */       Path modifiedVersionModel = MinecraftUtil.buildWorkingPath(new String[] { "versions", version.getID(), "TLauncherAdditional.json" });
/*     */       
/* 632 */       ModifiedVersion modifiedVersion = (ModifiedVersion)this.remoteContext.fromJson((JsonElement)object, ModifiedVersion.class);
/* 633 */       if (Files.exists(modifiedVersionModel, new java.nio.file.LinkOption[0]) && 
/* 634 */         TlauncherUtil.notHasAnyAdditionalTLauncherField(modifiedVersion)) {
/* 635 */         modifiedVersion = (ModifiedVersion)this.remoteContext.fromJson((JsonElement)getModifiedVersionFromJson(modifiedVersionModel), ModifiedVersion.class);
/*     */       }
/*     */ 
/*     */       
/* 639 */       version.setModifiedVersion(modifiedVersion);
/* 640 */       if (version.modifiedVersion.getSource() == null) {
/* 641 */         version.modifiedVersion.setSource(ClientInstanceRepo.LOCAL_VERSION_REPO);
/*     */       }
/*     */       
/* 644 */       if (version.time == null) {
/* 645 */         version.time = new Date(0L);
/*     */       }
/* 647 */       if (version.assets == null) {
/* 648 */         version.assets = "legacy";
/*     */       }
/* 650 */       if (Objects.nonNull(version.getDownloads()) && version.getDownloads().isEmpty()) {
/* 651 */         version.setDownloads(null);
/*     */       }
/* 653 */       return version;
/*     */     }
/*     */ 
/*     */     
/*     */     private JsonObject getModifiedVersionFromJson(Path absolutePath) {
/* 658 */       try (FileReader fileReader = new FileReader(absolutePath.toFile())) {
/* 659 */         JsonObject object = (new JsonParser()).parse(fileReader).getAsJsonObject();
/* 660 */         return object.getAsJsonObject();
/* 661 */       } catch (IOException e) {
/* 662 */         U.log(new Object[] { "Error while getting modifiedVersion from json" });
/* 663 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonElement serialize(CompleteVersion version0, Type type, JsonSerializationContext context) {
/*     */       CompleteVersion version;
/*     */       try {
/* 672 */         version = (CompleteVersion)version0.clone();
/* 673 */       } catch (CloneNotSupportedException e) {
/* 674 */         U.log(new Object[] { "Cloning of CompleteVersion is not supported O_o", e });
/* 675 */         return this.remoteContext.toJsonTree(version0, type);
/*     */       } 
/* 677 */       version.list = null;
/* 678 */       return this.remoteContext.toJsonTree(version, type);
/*     */     }
/*     */   }
/*     */   
/*     */   public JavaVersionName getJavaVersion() {
/* 683 */     return this.javaVersion;
/*     */   }
/*     */   
/*     */   public void setJavaVersion(JavaVersionName javaVersion) {
/* 687 */     this.javaVersion = javaVersion;
/*     */   }
/*     */   
/*     */   public boolean isActivateSkinCapeForUserVersion() {
/* 691 */     return this.modifiedVersion.isActivateSkinCapeForUserVersion();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\versions\CompleteVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */