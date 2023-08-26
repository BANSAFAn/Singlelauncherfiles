/*     */ package org.tlauncher.tlauncher.managers;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.InvalidPathException;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import net.minecraft.launcher.versions.Library;
/*     */ import net.minecraft.launcher.versions.Version;
/*     */ import net.minecraft.launcher.versions.json.Argument;
/*     */ import net.minecraft.launcher.versions.json.ArgumentType;
/*     */ import org.apache.commons.io.FilenameUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*     */ import org.tlauncher.tlauncher.component.RefreshableComponent;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.entity.TLauncherLib;
/*     */ import org.tlauncher.tlauncher.entity.TLauncherVersionChanger;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*     */ import org.tlauncher.tlauncher.minecraft.crash.Crash;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener;
/*     */ import org.tlauncher.tlauncher.modpack.ModpackUtil;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.MinecraftUtil;
/*     */ import org.tlauncher.util.gson.DownloadUtil;
/*     */ 
/*     */ public class TLauncherManager
/*     */   extends RefreshableComponent
/*     */   implements MinecraftListener
/*     */ {
/*  50 */   private static final Logger log = LoggerFactory.getLogger(TLauncherManager.class);
/*     */   
/*     */   public static final String CLIENT_TL_MANAGER = "_tl_manager";
/*     */   
/*     */   private TLauncherVersionChanger tLauncherVersionChanger;
/*  55 */   private final Gson gson = new Gson();
/*     */   
/*     */   public TLauncherManager(ComponentManager manager) throws Exception {
/*  58 */     super(manager);
/*     */   }
/*     */   
/*     */   public boolean useTLauncherAccount(Version version) {
/*  62 */     if (Objects.nonNull(this.tLauncherVersionChanger) && 
/*  63 */       this.tLauncherVersionChanger.getTlauncherSkinCapeVersion().contains(version.getID())) {
/*  64 */       return true;
/*     */     }
/*  66 */     return (version.isSkinVersion() || version.isActivateSkinCapeForUserVersion());
/*     */   }
/*     */   
/*     */   private List<TLauncherLib> findAddedLibraries(CompleteVersion complete) {
/*  70 */     String id = this.tLauncherVersionChanger.getVersionIDForUserSkinCapeVersion(complete);
/*  71 */     ArrayList<TLauncherLib> libList = new ArrayList<>();
/*  72 */     for (TLauncherLib tlauncherLib : this.tLauncherVersionChanger.getLibraries()) {
/*  73 */       if (tlauncherLib.isSupport(id)) {
/*  74 */         libList.add(tlauncherLib); continue;
/*     */       } 
/*  76 */       for (Library library : complete.getLibraries()) {
/*  77 */         if (tlauncherLib.isApply(library, complete))
/*  78 */           libList.add(tlauncherLib); 
/*     */       } 
/*     */     } 
/*  81 */     return libList;
/*     */   }
/*     */   
/*     */   public boolean applyTLauncherAccountLib(CompleteVersion original) {
/*  85 */     Configuration settings = TLauncher.getInstance().getConfiguration();
/*  86 */     Account ac = TLauncher.getInstance().getProfileManager().getSelectedAccount();
/*  87 */     if (Account.AccountType.OFFICIAL_ACCOUNTS.contains(ac.getType()) || 
/*  88 */       !settings.getBoolean("skin.status.checkbox.state")) {
/*  89 */       return false;
/*     */     }
/*  91 */     return useTLauncherAccount((Version)original);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompleteVersion addFilesForDownloading(CompleteVersion original, boolean customTexture) {
/*  98 */     if (Objects.isNull(this.tLauncherVersionChanger))
/*  99 */       return original; 
/* 100 */     log(new Object[] { "add required libraries:", original.getID() });
/* 101 */     CompleteVersion complete = original.fullCopy(new CompleteVersion());
/* 102 */     for (TLauncherLib lib : findAddedLibraries(original)) {
/* 103 */       complete.getLibraries().addAll(lib.getRequires());
/* 104 */       complete.getLibraries().add(lib);
/* 105 */       addMinecraftClientFiles(complete, lib, "_tl_manager", false);
/*     */     } 
/*     */     
/* 108 */     for (TLauncherLib l : this.tLauncherVersionChanger.getAddedMods(complete)) {
/* 109 */       complete.getLibraries().addAll(l.getRequires());
/* 110 */       complete.getLibraries().add(l);
/*     */     } 
/* 112 */     return complete;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addMinecraftClientFiles(CompleteVersion complete, TLauncherLib lib, String postfix, boolean showLog) {
/* 119 */     if (Objects.nonNull(lib.getDownloads()) && Objects.nonNull(complete.getDownloads())) {
/* 120 */       for (Map.Entry<String, MetadataDTO> e : (Iterable<Map.Entry<String, MetadataDTO>>)lib.getDownloads().entrySet()) {
/* 121 */         complete.getDownloads().put((String)e.getKey() + postfix, e.getValue());
/* 122 */         if (showLog) {
/* 123 */           log(new Object[] { String.format("new client will be put: %s", new Object[] { ((MetadataDTO)e.getValue()).getUrl() }) });
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean refresh() {
/*     */     try {
/* 131 */       this.tLauncherVersionChanger = (TLauncherVersionChanger)DownloadUtil.loadObjectByKey("skin.config.library", TLauncherVersionChanger.class);
/*     */     }
/* 133 */     catch (Throwable e) {
/* 134 */       log(new Object[] { "Failed to refresh TLancher manager", e });
/* 135 */       return false;
/*     */     } 
/* 137 */     return true;
/*     */   }
/*     */   
/*     */   public CompleteVersion createUpdatedVersion(CompleteVersion original, boolean showLog) {
/* 141 */     CompleteVersion complete = original.fullCopy(new CompleteVersion());
/* 142 */     transferVersionArgumentToModern(complete);
/* 143 */     if (Objects.isNull(this.tLauncherVersionChanger))
/* 144 */       return complete; 
/* 145 */     List<TLauncherLib> libList = findAddedLibraries(original);
/* 146 */     boolean tlLibraryType = applyTLauncherAccountLib(complete);
/* 147 */     for (TLauncherLib lib : libList) {
/* 148 */       if (!lib.isProperAccountTypeLib(tlLibraryType))
/*     */         continue; 
/* 150 */       boolean added = false;
/* 151 */       List<Library> list = complete.getLibraries();
/* 152 */       for (int i = 0; i < complete.getLibraries().size(); i++) {
/* 153 */         Library current = complete.getLibraries().get(i);
/* 154 */         if (lib.isApply(current, complete)) {
/* 155 */           if (showLog)
/* 156 */             log.info("library will be replaced: {} -> {}", current.getName(), lib.getName()); 
/* 157 */           complete.getLibraries().remove(i);
/* 158 */           complete.getLibraries().add(i, lib);
/* 159 */           added = true;
/* 160 */           setAdditionalFields(complete, lib);
/* 161 */           List<Library> requiredLibraries = lib.getRequires();
/* 162 */           addedRequiredLibraries(list, requiredLibraries, showLog);
/*     */           break;
/*     */         } 
/*     */       } 
/* 166 */       if (!added) {
/* 167 */         addedRequiredLibraries(complete.getLibraries(), lib.getRequires(), showLog);
/* 168 */         complete.getLibraries().add(0, lib);
/* 169 */         if (showLog)
/* 170 */           log(new Object[] { "library will be added:", lib.getName() }); 
/* 171 */         setAdditionalFields(complete, lib);
/*     */       } 
/* 173 */       addMinecraftClientFiles(complete, lib, "", showLog);
/*     */     } 
/*     */     
/* 176 */     return complete;
/*     */   }
/*     */   
/*     */   private void transferVersionArgumentToModern(CompleteVersion completeVersion) {
/* 180 */     String s = completeVersion.getMinecraftArguments();
/* 181 */     if (Objects.isNull(s))
/*     */       return; 
/* 183 */     String[] array = s.replace("  ", " ").split(" ");
/* 184 */     List<Argument> list = new ArrayList<>();
/* 185 */     Arrays.<String>stream(array).forEach(e -> list.add(new Argument(new String[] { e }, null)));
/* 186 */     list.add(new Argument(new String[] { "--width" }, null));
/* 187 */     list.add(new Argument(new String[] { "${resolution_width}" }, null));
/* 188 */     list.add(new Argument(new String[] { "--height" }, null));
/* 189 */     list.add(new Argument(new String[] { "${resolution_height}" }, null));
/* 190 */     Map<ArgumentType, List<Argument>> map = new HashMap<>();
/* 191 */     map.put(ArgumentType.GAME, list);
/* 192 */     List<Argument> jvm = new ArrayList<>();
/* 193 */     if (Objects.nonNull(completeVersion.getJVMArguments())) {
/* 194 */       array = completeVersion.getJVMArguments().replace("  ", " ").split(" ");
/* 195 */       Arrays.<String>stream(array).forEach(e -> jvm.add(new Argument(new String[] { e }, null)));
/*     */     } 
/* 197 */     jvm.add(new Argument(new String[] { "-Djava.library.path=${natives_directory}" }, null));
/* 198 */     jvm.add(new Argument(new String[] { "-cp" }, null));
/* 199 */     jvm.add(new Argument(new String[] { "${classpath}" }, null));
/* 200 */     map.put(ArgumentType.JVM, jvm);
/* 201 */     completeVersion.setArguments(map);
/*     */   }
/*     */   
/*     */   private void addedRequiredLibraries(List<Library> list, List<Library> requiredLibraries, boolean showLog) {
/* 205 */     if (Objects.isNull(requiredLibraries)) {
/*     */       return;
/*     */     }
/* 208 */     for (Library r : requiredLibraries) {
/* 209 */       boolean addedRequiredLib = false;
/* 210 */       for (int j = 0; j < list.size(); j++) {
/* 211 */         Library l = list.get(j);
/* 212 */         if (r.getPlainName().equals(l.getPlainName())) {
/* 213 */           if (showLog)
/* 214 */             log(new Object[] { "library will be replaced as required:", r.getName() }); 
/* 215 */           list.remove(j);
/* 216 */           list.add(j, r);
/* 217 */           addedRequiredLib = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 221 */       if (!addedRequiredLib) {
/* 222 */         list.add(0, r);
/* 223 */         if (showLog)
/* 224 */           log(new Object[] { "library will be added as required:", r.getName() }); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setAdditionalFields(CompleteVersion complete, TLauncherLib lib) {
/* 230 */     Map<ArgumentType, List<Argument>> arguments = lib.getArguments();
/* 231 */     if (Objects.nonNull(arguments)) {
/* 232 */       for (Map.Entry<ArgumentType, List<Argument>> arg : arguments.entrySet()) {
/* 233 */         ArgumentType type = arg.getKey();
/* 234 */         List<Argument> libArguments = arg.getValue();
/* 235 */         List<Argument> versionArguments = (List<Argument>)complete.getArguments().get(type);
/* 236 */         for (Argument a : libArguments) {
/* 237 */           boolean added = false;
/* 238 */           for (int i = 0; i < versionArguments.size(); i++) {
/* 239 */             if (Arrays.equals((Object[])a.getValues(), (Object[])((Argument)versionArguments.get(i)).getValues())) {
/* 240 */               versionArguments.remove(i);
/* 241 */               versionArguments.add(i, a);
/* 242 */               added = true;
/*     */               break;
/*     */             } 
/*     */           } 
/* 246 */           if (!added) {
/* 247 */             versionArguments.add(a);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 253 */     if (StringUtils.isNotBlank(lib.getMainClass())) {
/* 254 */       complete.setMainClass(lib.getMainClass());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMinecraftPrepare() {
/* 260 */     cleanMods();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftAbort() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftLaunch() {
/* 272 */     File optifineTempFile = new File(MinecraftUtil.getWorkingDirectory(), TLauncher.getInnerSettings().get("skin.config.temp.optifine.file.new"));
/* 273 */     Path mods = Paths.get(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), new String[] { "mods" });
/* 274 */     CompleteVersion completeVersion = TLauncher.getInstance().getLauncher().getVersion();
/* 275 */     if (completeVersion.isModpack()) {
/* 276 */       mods = ModpackUtil.getPathByVersion((Version)completeVersion, new String[] { "mods" });
/*     */     }
/* 278 */     if (!Files.exists(mods, new java.nio.file.LinkOption[0]))
/*     */       try {
/* 280 */         Files.createDirectory(mods, (FileAttribute<?>[])new FileAttribute[0]);
/* 281 */       } catch (IOException e1) {
/* 282 */         log(new Object[] { e1 });
/*     */       }  
/* 284 */     List<String> filesMods = readMods();
/*     */     
/* 286 */     if (completeVersion.getID().startsWith("ForgeOptiFine"))
/*     */     {
/* 288 */       for (Library library : completeVersion.getLibraries()) {
/*     */         
/* 290 */         if (library.getName().contains("optifine:OptiFine")) {
/* 291 */           Path out = copyMods(library);
/* 292 */           filesMods.add(out.toString());
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 298 */     if (completeVersion.getModsLibraries() != null) {
/* 299 */       List<Library> libraries = completeVersion.getModsLibraries();
/* 300 */       copyListMods(filesMods, libraries);
/*     */     } 
/* 302 */     if (Objects.nonNull(this.tLauncherVersionChanger)) {
/* 303 */       List<TLauncherLib> libraries = this.tLauncherVersionChanger.getAddedMods(completeVersion, 
/* 304 */           applyTLauncherAccountLib(completeVersion));
/* 305 */       copyListMods(filesMods, (List)libraries);
/*     */     } 
/*     */     try {
/* 308 */       String result = this.gson.toJson(filesMods, (new TypeToken<ArrayList<String>>() {  }
/* 309 */           ).getType());
/* 310 */       FileUtil.writeFile(optifineTempFile, result);
/* 311 */     } catch (IOException e) {
/* 312 */       log(new Object[] { e });
/*     */     } 
/* 314 */     printModsFiles("mods after", mods);
/*     */   }
/*     */   
/*     */   private void copyListMods(List<String> filesMods, List<? extends Library> libraries) {
/* 318 */     for (Library lib : libraries)
/* 319 */       filesMods.add(copyMods(lib).toString()); 
/*     */   }
/*     */   
/*     */   private Path copyMods(Library library) {
/* 323 */     Path in = Paths.get(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), new String[] { "libraries", library
/* 324 */           .getArtifactPath() });
/* 325 */     Path out = Paths.get(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), new String[] { "mods", 
/* 326 */           FilenameUtils.getName(library.getArtifactPath()) });
/*     */     
/*     */     try {
/* 329 */       Files.copy(in, out, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 330 */     } catch (IOException e) {
/* 331 */       log(new Object[] { e });
/*     */     } 
/* 333 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftClose() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftError(Throwable e) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftKnownError(MinecraftException e) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftCrash(Crash crash) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void cleanMods() {
/* 356 */     Path mods1 = Paths.get(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), new String[] { "mods" });
/* 357 */     printModsFiles("before clearLibrary", mods1);
/* 358 */     Path old = FileUtil.getRelativeConfig("skin.config.temp.optifine.file");
/* 359 */     Path mods = FileUtil.getRelativeConfig("skin.config.temp.optifine.file.new");
/*     */     
/* 361 */     if (Files.exists(old, new java.nio.file.LinkOption[0])) {
/*     */       try {
/* 363 */         log(new Object[] { "clear old library" });
/* 364 */         Files.delete(Paths.get(FileUtil.readFile(old.toFile()), new String[0]));
/* 365 */         Files.delete(old);
/* 366 */       } catch (IOException exception) {
/* 367 */         log(new Object[] { exception });
/* 368 */       } catch (InvalidPathException ex) {
/*     */         try {
/* 370 */           Files.delete(old);
/* 371 */         } catch (IOException e) {
/* 372 */           log(new Object[] { e });
/*     */         } 
/*     */       } 
/*     */     }
/* 376 */     cleanMods(mods);
/* 377 */     printModsFiles("after clearLibrary", mods1);
/*     */   }
/*     */   
/*     */   private void cleanMods(Path mods) {
/* 381 */     if (Files.exists(mods, new java.nio.file.LinkOption[0])) {
/* 382 */       List<String> list = readMods();
/* 383 */       if (!list.isEmpty()) {
/* 384 */         Iterator<String> it = list.iterator();
/* 385 */         while (it.hasNext()) {
/* 386 */           String file = it.next();
/*     */           
/*     */           try {
/* 389 */             Files.delete(Paths.get(file, new String[0]));
/* 390 */             it.remove();
/* 391 */           } catch (NoSuchFileException|InvalidPathException e) {
/* 392 */             it.remove();
/* 393 */             log(new Object[] { e });
/* 394 */           } catch (IOException exception) {
/* 395 */             log(new Object[] { exception.getMessage() });
/*     */           } 
/*     */         } 
/*     */       } 
/* 399 */       writeStateMod(list);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeStateMod(List<String> list) {
/*     */     try {
/* 405 */       String result = this.gson.toJson(list, (new TypeToken<ArrayList<String>>() {  }
/* 406 */           ).getType());
/* 407 */       FileUtil.writeFile(FileUtil.getRelativeConfigFile("skin.config.temp.optifine.file.new"), result);
/* 408 */       log(new Object[] { "written: ", result });
/* 409 */     } catch (IOException e) {
/* 410 */       log(new Object[] { e });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void printModsFiles(String state, Path mods) {
/* 415 */     File[] files = mods.toFile().listFiles(File::isFile);
/* 416 */     if (Objects.nonNull(files))
/* 417 */       log(new Object[] { state, files }); 
/*     */   }
/*     */   
/*     */   private List<String> readMods() {
/* 421 */     Path mods = FileUtil.getRelativeConfig("skin.config.temp.optifine.file.new");
/* 422 */     List<String> list = null;
/* 423 */     if (Files.exists(mods, new java.nio.file.LinkOption[0])) {
/*     */       
/*     */       try {
/* 426 */         list = (List<String>)this.gson.fromJson(FileUtil.readFile(mods.toFile(), "utf-8"), (new TypeToken<ArrayList<String>>() {  }
/* 427 */             ).getType());
/* 428 */       } catch (IOException|com.google.gson.JsonSyntaxException e) {
/* 429 */         log(new Object[] { e });
/*     */       } 
/*     */     }
/* 432 */     return (list == null) ? new ArrayList<>() : list;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\managers\TLauncherManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */