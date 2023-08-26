/*     */ package net.minecraft.launcher.updater;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import net.minecraft.launcher.versions.LogClient;
/*     */ import net.minecraft.launcher.versions.ModifiedVersion;
/*     */ import net.minecraft.launcher.versions.Version;
/*     */ import net.minecraft.launcher.versions.json.Argument;
/*     */ import net.minecraft.launcher.versions.json.ArgumentType;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.tlauncher.modpack.domain.client.share.ForgeStringComparator;
/*     */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.MinecraftUtil;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ public class LocalVersionList
/*     */   extends StreamVersionList {
/*  37 */   private static final Logger log = Logger.getLogger(LocalVersionList.class);
/*     */   
/*     */   private File baseDirectory;
/*     */   
/*     */   private File baseVersionsDir;
/*     */   
/*     */   public LocalVersionList(File baseDirectory) throws IOException {
/*  44 */     setBaseDirectory(baseDirectory);
/*     */   }
/*     */   
/*     */   public File getBaseDirectory() {
/*  48 */     return this.baseDirectory;
/*     */   }
/*     */   
/*     */   public void setBaseDirectory(File directory) throws IOException {
/*  52 */     if (directory == null) {
/*  53 */       throw new IllegalArgumentException("Base directory is NULL!");
/*     */     }
/*  55 */     FileUtil.createFolder(directory);
/*  56 */     log(new Object[] { "Base directory:", directory.getAbsolutePath() });
/*     */     
/*  58 */     this.baseDirectory = directory;
/*  59 */     this.baseVersionsDir = new File(this.baseDirectory, "versions");
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void refreshVersions() {
/*  64 */     clearCache();
/*     */     
/*  66 */     File[] files = this.baseVersionsDir.listFiles();
/*  67 */     if (files == null) {
/*     */       return;
/*     */     }
/*  70 */     for (File directory : files) {
/*  71 */       String id = directory.getName();
/*  72 */       File jsonFile = new File(directory, id + ".json");
/*     */       
/*  74 */       if (directory.isDirectory() && jsonFile.isFile())
/*     */         
/*     */         try {
/*     */           
/*  78 */           CompleteVersion version = (CompleteVersion)this.gson.fromJson(getUrl("versions/" + id + "/" + id + ".json"), CompleteVersion.class);
/*     */ 
/*     */           
/*  81 */           if (version == null)
/*  82 */           { log(new Object[] { "JSON descriptor of version \"" + id + "\" in NULL, it won't be added in list as local." });
/*     */              }
/*     */           
/*  85 */           else if (!"1.17".equals(version.getAssets()) || !Objects.isNull(version.getJavaVersion()))
/*     */           
/*     */           { 
/*  88 */             version.setID(id);
/*  89 */             version.setSource(ClientInstanceRepo.LOCAL_VERSION_REPO);
/*  90 */             version.setVersionList(this);
/*     */             
/*  92 */             if (version.getID().equalsIgnoreCase(version.getInheritsFrom())) {
/*  93 */               version = renameVersion(version, findNewVersionName(version.getID(), 0));
/*     */             }
/*     */             
/*  96 */             checkDoubleArgument(version);
/*     */             
/*  98 */             fixedlog4j(version);
/*  99 */             addVersion(version); } 
/* 100 */         } catch (Throwable ex) {
/* 101 */           log(new Object[] { "Error occurred while parsing local version", id, ex });
/*     */           try {
/* 103 */             SimpleDateFormat s = new SimpleDateFormat("dd_HH_mm_ss");
/* 104 */             Files.move(directory.toPath(), Paths.get(directory.toString() + "_error_version_" + s.format(new Date()), new String[0]), new java.nio.file.CopyOption[0]);
/* 105 */           } catch (IOException e) {
/* 106 */             U.log(new Object[] { e });
/*     */           } 
/*     */         }  
/*     */     } 
/*     */   }
/*     */   private void fixedlog4j(CompleteVersion version) throws IOException {
/*     */     String id;
/* 113 */     Map<String, LogClient> map = version.getLogging();
/* 114 */     ForgeStringComparator comparator = new ForgeStringComparator();
/*     */ 
/*     */     
/* 117 */     if (Objects.nonNull(version.getJar())) {
/* 118 */       id = version.getJar();
/*     */     } else {
/*     */       
/* 121 */       id = version.getAssets().replaceAll("pre-", "").replaceAll("-af", "").replaceAll("-aprilfools", "").replaceAll("14w30c", "1.7.10").replaceAll("14w31a", "1.7.10").replaceAll("14w25a", "1.7.10");
/* 122 */       if (id.equals("legacy")) {
/* 123 */         if ((version.getID().split(" ")).length == 2) {
/* 124 */           id = version.getID().split(" ")[1];
/*     */         } else {
/* 126 */           id = "1.7";
/*     */         } 
/*     */       }
/*     */     } 
/*     */     try {
/* 131 */       if (comparator.compare("1.6.9", id) == 1 && (Objects.isNull(map) || Objects.isNull(map.get("client")))) {
/* 132 */         LogClient l; map = new HashMap<>();
/*     */         
/* 134 */         String message = "set logging %s version type %s, version id %s";
/* 135 */         if (comparator.compare("1.11.9", id) == -1) {
/* 136 */           log.info(String.format(message, new Object[] { "1.7.10.json", id, version.getID() }));
/* 137 */           l = (LogClient)this.gson.fromJson(new InputStreamReader(FileUtil.getResourceAppStream("/fix_log4j/1.7.10.json")), LogClient.class);
/*     */         } else {
/*     */           
/* 140 */           log.info(String.format(message, new Object[] { "1.12.json", id, version.getID() }));
/* 141 */           l = (LogClient)this.gson.fromJson(new InputStreamReader(FileUtil.getResourceAppStream("/fix_log4j/1.12.json")), LogClient.class);
/*     */         } 
/*     */         
/* 144 */         map.put("client", l);
/* 145 */         version.setLogging(map);
/* 146 */         Path versionPath = MinecraftUtil.buildWorkingPath(new String[] { "versions", version.getID(), version
/* 147 */               .getID() + ".json" });
/* 148 */         FileUtil.writeFile(versionPath.toFile(), this.gson.toJson((JsonElement)serializeVersion(version).getAsJsonObject()));
/* 149 */         saveVersion(version);
/*     */       } 
/* 151 */     } catch (NumberFormatException e) {
/* 152 */       log.warn("can't patch fix log4j" + id);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkDoubleArgument(CompleteVersion version) throws IOException {
/* 157 */     Map<ArgumentType, List<Argument>> map = version.getArguments();
/* 158 */     if (Objects.nonNull(map)) {
/* 159 */       List<Argument> list = map.get(ArgumentType.GAME);
/* 160 */       if (Objects.isNull(list))
/*     */         return; 
/* 162 */       if (list.size() > 30) {
/* 163 */         Set<String> set = new HashSet<>();
/* 164 */         list.removeIf(e -> {
/*     */               String value = Arrays.toString((Object[])e.getValues());
/*     */               if (set.contains(value)) {
/*     */                 U.log(new Object[] { "removed arg " + e });
/*     */                 return true;
/*     */               } 
/*     */               set.add(value);
/*     */               return false;
/*     */             });
/* 173 */         saveVersion(version);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private String findNewVersionName(String name, int i) {
/* 179 */     String newName = name + " fixed " + i;
/* 180 */     File f = new File(this.baseVersionsDir, newName);
/* 181 */     if (f.exists()) {
/* 182 */       return findNewVersionName(name, ++i);
/*     */     }
/* 184 */     return newName;
/*     */   }
/*     */   
/*     */   public synchronized void saveVersion(CompleteVersion version) throws IOException {
/* 188 */     fixedlog4j(version);
/* 189 */     Path versionPath = MinecraftUtil.buildWorkingPath(new String[] { "versions", version.getID(), version
/* 190 */           .getID() + ".json" });
/* 191 */     Path modifiedVersionPath = MinecraftUtil.buildWorkingPath(new String[] { "versions", version.getID(), "TLauncherAdditional.json" });
/*     */     
/* 193 */     ModifiedVersion mv = version.getModifiedVersion();
/* 194 */     if (Objects.nonNull(version.getRemoteVersion())) {
/* 195 */       FileUtil.writeFile(versionPath.toFile(), version.getRemoteVersion());
/* 196 */     } else if (Files.notExists(versionPath, new java.nio.file.LinkOption[0]) || !ClientInstanceRepo.LOCAL_VERSION_REPO.equals(mv.getSource())) {
/* 197 */       FileUtil.writeFile(versionPath.toFile(), this.gson.toJson((JsonElement)serializeVersion(version).getAsJsonObject()));
/*     */     } 
/* 199 */     mv.setSource(ClientInstanceRepo.LOCAL_VERSION_REPO);
/* 200 */     FileUtil.writeFile(modifiedVersionPath.toFile(), this.gson.toJson(mv));
/*     */   }
/*     */   
/*     */   public synchronized void deleteVersion(String id, boolean deleteLibraries) throws IOException {
/* 204 */     CompleteVersion version = getCompleteVersion(id);
/*     */     
/* 206 */     if (version == null) {
/* 207 */       throw new IllegalArgumentException("Version is not installed! id = " + id);
/*     */     }
/* 209 */     File dir = new File(this.baseVersionsDir, id + '/');
/*     */     
/* 211 */     if (!dir.isDirectory()) {
/* 212 */       throw new IOException("Cannot find directory: " + dir.getAbsolutePath());
/*     */     }
/* 214 */     FileUtil.deleteDirectory(dir);
/*     */     
/* 216 */     if (!deleteLibraries) {
/*     */       return;
/*     */     }
/* 219 */     for (File library : version.getClassPath(this.baseDirectory)) {
/* 220 */       FileUtil.deleteFile(library);
/*     */     }
/* 222 */     for (String nativeLib : version.getNatives()) {
/* 223 */       FileUtil.deleteFile(new File(this.baseDirectory, nativeLib));
/*     */     }
/*     */   }
/*     */   
/*     */   protected InputStream getInputStream(String uri) throws IOException {
/* 228 */     return new FileInputStream(new File(this.baseDirectory, uri));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAllFiles(CompleteVersion version, OS os) {
/* 233 */     Set<String> files = version.getRequiredFiles(os);
/*     */     
/* 235 */     for (String file : files) {
/* 236 */       File required = new File(this.baseDirectory, file);
/* 237 */       if (!required.isFile() || required.length() == 0L) {
/* 238 */         return false;
/*     */       }
/*     */     } 
/* 241 */     return true;
/*     */   }
/*     */   
/*     */   public synchronized CompleteVersion renameVersion(CompleteVersion version, String name) throws IOException {
/* 245 */     CompleteVersion newVersion = version.fullCopy(new CompleteVersion());
/* 246 */     newVersion.setID(name);
/* 247 */     if (Objects.nonNull(version.getModpack()))
/* 248 */       newVersion.getModpack().setName(name); 
/* 249 */     File newFolder = new File(this.baseVersionsDir.toString(), newVersion.getID());
/* 250 */     if (newFolder.exists()) {
/* 251 */       throw new IOException("folder exists " + newFolder);
/*     */     }
/* 253 */     String oldName = version.getID();
/* 254 */     File oldJar = new File(this.baseVersionsDir.toString(), version.getID() + "/" + version.getID() + ".jar");
/* 255 */     File newJar = new File(this.baseVersionsDir.toString(), version.getID() + "/" + newVersion.getID() + ".jar");
/*     */     
/* 257 */     if (oldJar.exists() && !oldJar.renameTo(newJar)) {
/* 258 */       throw new IOException("can't rename from " + oldJar + "to " + newJar);
/*     */     }
/* 260 */     File oldFolder = new File(this.baseVersionsDir.toString(), oldName);
/* 261 */     if (!oldFolder.renameTo(newFolder)) {
/* 262 */       throw new IOException("can't rename from " + version.getID() + "to " + newVersion.getID());
/*     */     }
/* 264 */     FileUtil.deleteFile(new File(this.baseVersionsDir
/* 265 */           .toString(), newVersion.getID() + "/" + version.getID() + ".json"));
/* 266 */     saveVersion(newVersion);
/*     */     
/* 268 */     return newVersion;
/*     */   }
/*     */   
/*     */   public synchronized void refreshLocalVersion(CompleteVersion version) throws IOException {
/* 272 */     saveVersion(version);
/* 273 */     version.setSource(ClientInstanceRepo.LOCAL_VERSION_REPO);
/* 274 */     version.setVersionList(this);
/* 275 */     this.byName.put(version.getID(), version);
/*     */     
/* 277 */     for (int i = 0; i < this.versions.size(); i++) {
/* 278 */       if (((Version)this.versions.get(i)).getID().equalsIgnoreCase(version.getID())) {
/* 279 */         this.versions.remove(i);
/* 280 */         this.versions.add(i, version);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launche\\updater\LocalVersionList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */