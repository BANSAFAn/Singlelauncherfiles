/*     */ package net.minecraft.launcher.updater;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import net.minecraft.launcher.versions.Library;
/*     */ import net.minecraft.launcher.versions.Version;
/*     */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*     */ import org.tlauncher.tlauncher.managers.VersionManager;
/*     */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*     */ import org.tlauncher.tlauncher.repository.Repo;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.OS;
/*     */ 
/*     */ 
/*     */ public class VersionSyncInfo
/*     */ {
/*     */   Version localVersion;
/*     */   Version remoteVersion;
/*     */   
/*     */   public VersionSyncInfo(Version localVersion, Version remoteVersion) {
/*  26 */     if (localVersion == null && remoteVersion == null) {
/*  27 */       throw new NullPointerException("Cannot createScrollWrapper sync info from NULLs!");
/*     */     }
/*     */     
/*  30 */     this.localVersion = localVersion;
/*  31 */     this.remoteVersion = remoteVersion;
/*     */     
/*  33 */     if (localVersion != null && remoteVersion != null) {
/*  34 */       localVersion.setVersionList(remoteVersion.getVersionList());
/*     */     }
/*  36 */     if (getID() == null)
/*  37 */       throw new NullPointerException("Cannot createScrollWrapper sync info from versions that have NULL IDs"); 
/*     */   }
/*     */   private CompleteVersion completeLocal; private CompleteVersion completeRemote; private String id;
/*     */   
/*     */   public VersionSyncInfo(VersionSyncInfo info) {
/*  42 */     this(info.getLocal(), info.getRemote());
/*     */   }
/*     */   
/*     */   private VersionSyncInfo() {
/*  46 */     this.localVersion = null;
/*  47 */     this.remoteVersion = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  52 */     if (this == o) {
/*  53 */       return true;
/*     */     }
/*  55 */     if (getID() == null || !(o instanceof VersionSyncInfo)) {
/*  56 */       return false;
/*     */     }
/*  58 */     VersionSyncInfo v = (VersionSyncInfo)o;
/*  59 */     return getID().equals(v.getID());
/*     */   }
/*     */   
/*     */   public Version getLocal() {
/*  63 */     return this.localVersion;
/*     */   }
/*     */   
/*     */   public void setLocal(Version version) {
/*  67 */     this.localVersion = version;
/*     */     
/*  69 */     if (version instanceof CompleteVersion)
/*  70 */       this.completeLocal = (CompleteVersion)version; 
/*     */   }
/*     */   
/*     */   public Version getRemote() {
/*  74 */     return this.remoteVersion;
/*     */   }
/*     */   
/*     */   public void setRemote(Version version) {
/*  78 */     this.remoteVersion = version;
/*     */     
/*  80 */     if (version instanceof CompleteVersion)
/*  81 */       this.completeRemote = (CompleteVersion)version; 
/*     */   }
/*     */   
/*     */   public String getID() {
/*  85 */     if (this.id != null) {
/*  86 */       return this.id;
/*     */     }
/*  88 */     if (this.localVersion != null) {
/*  89 */       return this.localVersion.getID();
/*     */     }
/*  91 */     if (this.remoteVersion != null) {
/*  92 */       return this.remoteVersion.getID();
/*     */     }
/*  94 */     return null;
/*     */   }
/*     */   
/*     */   public void setID(String id) {
/*  98 */     if (id != null && id.isEmpty()) {
/*  99 */       throw new IllegalArgumentException("ID cannot be empty!");
/*     */     }
/* 101 */     this.id = id;
/*     */   }
/*     */   
/*     */   public Version getLatestVersion() {
/* 105 */     if (this.remoteVersion != null) {
/* 106 */       return this.remoteVersion;
/*     */     }
/* 108 */     return this.localVersion;
/*     */   }
/*     */   
/*     */   public Version getAvailableVersion() {
/* 112 */     if (this.localVersion != null) {
/* 113 */       return this.localVersion;
/*     */     }
/* 115 */     return this.remoteVersion;
/*     */   }
/*     */   
/*     */   public boolean isInstalled() {
/* 119 */     return (this.localVersion != null);
/*     */   }
/*     */   
/*     */   public boolean hasRemote() {
/* 123 */     return (this.remoteVersion != null);
/*     */   }
/*     */   
/*     */   public boolean isUpToDate() {
/* 127 */     if (this.localVersion == null)
/* 128 */       return false; 
/* 129 */     if (this.remoteVersion == null) {
/* 130 */       return true;
/*     */     }
/* 132 */     return (this.localVersion.getUpdatedTime().compareTo(this.remoteVersion
/* 133 */         .getUpdatedTime()) >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 138 */     return getClass().getSimpleName() + "{id='" + getID() + "',\nlocal=" + this.localVersion + ",\nremote=" + this.remoteVersion + ", isInstalled=" + 
/*     */       
/* 140 */       isInstalled() + ", hasRemote=" + 
/* 141 */       hasRemote() + ", isUpToDate=" + isUpToDate() + "}";
/*     */   }
/*     */ 
/*     */   
/*     */   public CompleteVersion resolveCompleteVersion(VersionManager manager, boolean latest) throws IOException {
/*     */     Version version;
/* 147 */     if (latest) {
/* 148 */       version = getLatestVersion();
/* 149 */     } else if (isInstalled()) {
/* 150 */       version = getLocal();
/*     */     } else {
/* 152 */       version = getRemote();
/*     */     } 
/* 154 */     if (version.equals(this.localVersion) && this.completeLocal != null && this.completeLocal.getInheritsFrom() == null)
/* 155 */       return this.completeLocal; 
/* 156 */     if (version.equals(this.remoteVersion) && this.completeRemote != null && this.completeRemote.getInheritsFrom() == null) {
/* 157 */       return this.completeRemote;
/*     */     }
/* 159 */     CompleteVersion complete = version.getVersionList().getCompleteVersion(version).resolve(manager, latest);
/*     */     
/* 161 */     if (version == this.localVersion) {
/* 162 */       this.completeLocal = complete;
/* 163 */     } else if (version == this.remoteVersion) {
/* 164 */       this.completeRemote = complete;
/*     */     } 
/* 166 */     return complete;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CompleteVersion getCompleteVersion(boolean latest) throws IOException {
/*     */     Version version;
/* 173 */     if (latest) {
/* 174 */       version = getLatestVersion();
/* 175 */     } else if (isInstalled()) {
/* 176 */       version = getLocal();
/*     */     } else {
/* 178 */       version = getRemote();
/*     */     } 
/* 180 */     if (version.equals(this.localVersion) && this.completeLocal != null) {
/* 181 */       return this.completeLocal;
/*     */     }
/* 183 */     if (version.equals(this.remoteVersion) && this.completeRemote != null) {
/* 184 */       return this.completeRemote;
/*     */     }
/* 186 */     CompleteVersion complete = version.getVersionList().getCompleteVersion(version);
/*     */     
/* 188 */     if (version == this.localVersion) {
/* 189 */       this.completeLocal = complete;
/* 190 */     } else if (version == this.remoteVersion) {
/* 191 */       this.completeRemote = complete;
/*     */     } 
/* 193 */     return complete;
/*     */   }
/*     */   
/*     */   public CompleteVersion getLatestCompleteVersion() throws IOException {
/* 197 */     return getCompleteVersion(true);
/*     */   }
/*     */   
/*     */   public CompleteVersion getLocalCompleteVersion() {
/* 201 */     return this.completeLocal;
/*     */   }
/*     */   
/*     */   private Set<Downloadable> getRequiredDownloadables(OS os, File targetDirectory, boolean force, boolean tlauncher) throws IOException {
/* 205 */     Set<Downloadable> neededFiles = new HashSet<>();
/*     */     
/* 207 */     CompleteVersion version = getCompleteVersion(force);
/* 208 */     version = TLauncher.getInstance().getTLauncherManager().addFilesForDownloading(version, tlauncher);
/* 209 */     Repo source = hasRemote() ? this.remoteVersion.getSource() : ClientInstanceRepo.OFFICIAL_VERSION_REPO;
/*     */     
/* 211 */     if (!source.isSelectable()) {
/* 212 */       return neededFiles;
/*     */     }
/* 214 */     for (Library library : version.getRelevantLibraries()) {
/* 215 */       File local = analizeFolderLibrary(os, targetDirectory, library);
/* 216 */       if (Objects.nonNull(local))
/* 217 */         neededFiles.add(library.getDownloadable(source, local, os)); 
/*     */     } 
/* 219 */     if (Objects.nonNull(version.getModsLibraries()))
/* 220 */       for (Library library : version.getModsLibraries()) {
/* 221 */         File local = analizeFolderLibrary(os, targetDirectory, library);
/* 222 */         if (Objects.nonNull(local)) {
/* 223 */           neededFiles.add(library.getDownloadable(source, local, os));
/*     */         }
/*     */       }  
/* 226 */     return neededFiles;
/*     */   }
/*     */   
/*     */   private File analizeFolderLibrary(OS os, File targetDirectory, Library library) {
/* 230 */     String file = null;
/* 231 */     if (library.getNatives() != null) {
/* 232 */       String natives = (String)library.getNatives().get(os);
/* 233 */       if (natives != null) {
/* 234 */         file = library.getArtifactPath(natives);
/*     */       }
/*     */     } else {
/* 237 */       file = library.getArtifactPath();
/*     */     } 
/*     */     
/* 240 */     if (file == null) {
/* 241 */       return null;
/*     */     }
/* 243 */     File local = new File(targetDirectory, "libraries/" + file);
/*     */     
/* 245 */     if (local.isFile()) {
/* 246 */       if (Objects.isNull(library.getChecksum()) || Objects.nonNull(library.getDeleteEntriesList()))
/* 247 */         return null; 
/* 248 */       if (library.getChecksum().equals(FileUtil.getChecksum(local)))
/* 249 */         return null; 
/*     */     } 
/* 251 */     return local;
/*     */   }
/*     */   
/*     */   public Set<Downloadable> getRequiredDownloadables(File targetDirectory, boolean force, boolean tlauncher) throws IOException {
/* 255 */     return getRequiredDownloadables(OS.CURRENT, targetDirectory, force, tlauncher);
/*     */   }
/*     */   
/*     */   public static VersionSyncInfo createEmpty() {
/* 259 */     return new VersionSyncInfo();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launche\\updater\VersionSyncInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */