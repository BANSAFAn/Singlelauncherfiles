/*     */ package net.minecraft.launcher.updater;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import com.google.gson.TypeAdapterFactory;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import net.minecraft.launcher.versions.ModifiedVersion;
/*     */ import net.minecraft.launcher.versions.PartialVersion;
/*     */ import net.minecraft.launcher.versions.ReleaseType;
/*     */ import net.minecraft.launcher.versions.Version;
/*     */ import net.minecraft.launcher.versions.json.DateTypeAdapter;
/*     */ import net.minecraft.launcher.versions.json.LowerCaseEnumTypeAdapterFactory;
/*     */ import org.tlauncher.serialization.version.ModifiedVersionSerializer;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.Time;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ public abstract class VersionList {
/*  29 */   protected final List<Version> versions = Collections.synchronizedList(new ArrayList<>());
/*  30 */   protected final Map<String, Version> byName = new Hashtable<>();
/*  31 */   protected final Map<ReleaseType, Version> latest = new Hashtable<>();
/*     */   VersionList() {
/*  33 */     GsonBuilder builder = new GsonBuilder();
/*  34 */     builder.registerTypeAdapterFactory((TypeAdapterFactory)new LowerCaseEnumTypeAdapterFactory());
/*  35 */     builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
/*  36 */     builder.registerTypeAdapter(CompleteVersion.class, new CompleteVersion.CompleteVersionSerializer());
/*  37 */     builder.registerTypeAdapter(ModifiedVersion.class, new ModifiedVersionSerializer());
/*  38 */     builder.enableComplexMapKeySerialization();
/*  39 */     builder.setPrettyPrinting();
/*  40 */     builder.disableHtmlEscaping();
/*     */     
/*  42 */     this.gson = builder.create();
/*     */   }
/*     */   public final Gson gson;
/*     */   public List<Version> getVersions() {
/*  46 */     return this.versions;
/*     */   }
/*     */   
/*     */   public Map<ReleaseType, Version> getLatestVersions() {
/*  50 */     return this.latest;
/*     */   }
/*     */   
/*     */   public Version getVersion(String name) {
/*  54 */     if (name == null || name.isEmpty()) {
/*  55 */       throw new IllegalArgumentException("Name cannot be NULL or empty");
/*     */     }
/*  57 */     return this.byName.get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompleteVersion getCompleteVersion(Version version) throws JsonSyntaxException, IOException {
/*  62 */     if (version instanceof CompleteVersion) {
/*  63 */       return (CompleteVersion)version;
/*     */     }
/*  65 */     if (version == null) {
/*  66 */       throw new NullPointerException("Version cannot be NULL!");
/*     */     }
/*  68 */     CompleteVersion complete = (CompleteVersion)this.gson.fromJson(getUrl("versions/" + version.getID() + "/" + version.getID() + ".json"), CompleteVersion.class);
/*     */     
/*  70 */     complete.setID(version.getID());
/*  71 */     complete.setVersionList(this);
/*  72 */     complete.setSkinVersion(version.isSkinVersion());
/*  73 */     Collections.replaceAll(this.versions, version, complete);
/*     */     
/*  75 */     return complete;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompleteVersion getCompleteVersion(String name) throws JsonSyntaxException, IOException {
/*  80 */     Version version = getVersion(name);
/*  81 */     if (version == null) {
/*  82 */       return null;
/*     */     }
/*  84 */     return getCompleteVersion(version);
/*     */   }
/*     */   
/*     */   public Version getLatestVersion(ReleaseType type) {
/*  88 */     if (type == null) {
/*  89 */       throw new NullPointerException();
/*     */     }
/*  91 */     return this.latest.get(type);
/*     */   }
/*     */   
/*     */   public RawVersionList getRawList() throws IOException {
/*  95 */     Object lock = new Object();
/*  96 */     Time.start(lock);
/*  97 */     RawVersionList list = (RawVersionList)this.gson.fromJson(getUrl("versions/versions.json"), RawVersionList.class);
/*     */     
/*  99 */     for (PartialVersion version : list.versions) {
/* 100 */       version.setVersionList(this);
/*     */     }
/* 102 */     log(new Object[] { "Got in", Long.valueOf(Time.stop(lock)), "ms" });
/*     */     
/* 104 */     return list;
/*     */   }
/*     */   
/*     */   public void refreshVersions(RawVersionList versionList) {
/* 108 */     clearCache();
/*     */     
/* 110 */     for (Version version : versionList.getVersions()) {
/*     */       
/* 112 */       if (version == null || version.getID() == null) {
/*     */         continue;
/*     */       }
/* 115 */       this.versions.add(version);
/* 116 */       this.byName.put(version.getID(), version);
/*     */     } 
/*     */     
/* 119 */     for (Map.Entry<ReleaseType, String> en : versionList.latest.entrySet()) {
/* 120 */       ReleaseType releaseType = en.getKey();
/*     */       
/* 122 */       if (releaseType == null) {
/* 123 */         log(new Object[] { "Unknown release type for latest version entry:", en });
/*     */         
/*     */         continue;
/*     */       } 
/* 127 */       Version version = getVersion(en.getValue());
/*     */       
/* 129 */       if (version == null) {
/* 130 */         throw new NullPointerException("Cannot find version for latest version entry: " + en);
/*     */       }
/*     */       
/* 133 */       this.latest.put(releaseType, version);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void refreshVersions() throws IOException {
/* 138 */     refreshVersions(getRawList());
/*     */   }
/*     */   
/*     */   protected CompleteVersion addVersion(CompleteVersion version) {
/* 142 */     if (version.getID() == null) {
/* 143 */       throw new IllegalArgumentException("Cannot add blank version");
/*     */     }
/* 145 */     if (getVersion(version.getID()) != null) {
/* 146 */       log(new Object[] { "Version '" + version.getID() + "' is already tracked" });
/* 147 */       return version;
/*     */     } 
/* 149 */     this.versions.add(version);
/* 150 */     this.byName.put(version.getID(), version);
/*     */     
/* 152 */     return version;
/*     */   }
/*     */   
/*     */   public JsonElement serializeVersion(CompleteVersion version) {
/* 156 */     if (version == null)
/* 157 */       throw new NullPointerException("CompleteVersion cannot be NULL!"); 
/* 158 */     return this.gson.toJsonTree(version);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract boolean hasAllFiles(CompleteVersion paramCompleteVersion, OS paramOS);
/*     */ 
/*     */   
/*     */   protected abstract String getUrl(String paramString) throws IOException;
/*     */ 
/*     */   
/*     */   protected void clearCache() {
/* 169 */     this.byName.clear();
/* 170 */     this.versions.clear();
/* 171 */     this.latest.clear();
/*     */   }
/*     */   
/*     */   void log(Object... obj) {
/* 175 */     U.log(new Object[] { "[" + getClass().getSimpleName() + "]", obj });
/*     */   }
/*     */   
/*     */   public static class RawVersionList {
/* 179 */     List<PartialVersion> versions = new ArrayList<>();
/* 180 */     Map<ReleaseType, String> latest = new EnumMap<>(ReleaseType.class);
/*     */     
/*     */     public List<PartialVersion> getVersions() {
/* 183 */       return this.versions;
/*     */     }
/*     */     
/*     */     public Map<ReleaseType, String> getLatestVersions() {
/* 187 */       return this.latest;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launche\\updater\VersionList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */