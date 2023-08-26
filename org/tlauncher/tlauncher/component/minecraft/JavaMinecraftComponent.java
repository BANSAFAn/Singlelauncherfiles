/*     */ package org.tlauncher.tlauncher.component.minecraft;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Singleton;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.launcher.versions.JavaVersionName;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.controller.JavaMinecraftController;
/*     */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*     */ import org.tlauncher.tlauncher.downloader.DownloadableContainer;
/*     */ import org.tlauncher.tlauncher.downloader.DownloadableContainerHandler;
/*     */ import org.tlauncher.tlauncher.downloader.DownloadableContainerHandlerAdapter;
/*     */ import org.tlauncher.tlauncher.downloader.JVMFileDownloadable;
/*     */ import org.tlauncher.tlauncher.downloader.RetryDownloadException;
/*     */ import org.tlauncher.tlauncher.entity.minecraft.JVMFile;
/*     */ import org.tlauncher.tlauncher.entity.minecraft.JVMManifest;
/*     */ import org.tlauncher.tlauncher.entity.minecraft.JavaVersionDescription;
/*     */ import org.tlauncher.tlauncher.entity.minecraft.MinecraftJava;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.assitent.MinecraftLauncherAssistantWrapper;
/*     */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.MinecraftUtil;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.gson.DownloadUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Singleton
/*     */ public class JavaMinecraftComponent
/*     */   extends MinecraftLauncherAssistantWrapper
/*     */ {
/*  58 */   private static final Logger log = Logger.getLogger(JavaMinecraftComponent.class);
/*     */   
/*     */   @Inject
/*     */   private Gson gson;
/*     */   
/*     */   @Inject
/*     */   private JavaMinecraftController controller;
/*     */   private volatile Map<String, JsonObject> javaConfiguration;
/*  66 */   private final String FILE_META_DELIMITER = "/#//";
/*     */ 
/*     */   
/*     */   public void collectInfo(MinecraftLauncher launcher) throws MinecraftException {
/*  70 */     if (Objects.isNull(this.javaConfiguration) && Objects.nonNull(launcher.getVersion())) {
/*     */       try {
/*  72 */         this.javaConfiguration = (Map<String, JsonObject>)DownloadUtil.loadObjectByKey("java.configuration", (new TypeToken<Map<String, JsonObject>>() {
/*     */             
/*  74 */             },  ).getType(), true);
/*  75 */       } catch (IOException|com.google.gson.JsonSyntaxException e) {
/*  76 */         U.log(new Object[] { e });
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void collectResources(MinecraftLauncher launcher) throws MinecraftException {
/*  82 */     MinecraftJava.CompleteMinecraftJava java = this.controller.getCurrent();
/*     */ 
/*     */     
/*  85 */     JavaVersionName javaVersionName = Objects.isNull(launcher.getVersion().getJavaVersion()) ? JavaVersionName.JAVA_8_LEGACY : launcher.getVersion().getJavaVersion();
/*  86 */     if (Objects.isNull(java)) {
/*  87 */       prepareDefaultJava(launcher, javaVersionName);
/*  88 */       log.info("used default java runtime");
/*     */     } else {
/*  90 */       log.info("used user java runtime");
/*  91 */       launcher.setJavaDir(new File(OS.appendBootstrapperJvm2(java.getPath())));
/*  92 */       launcher.setJava(java);
/*     */     } 
/*  94 */     launcher.setJavaVersion(javaVersionName.getMajorVersion().intValue());
/*  95 */     U.log(new Object[] { String.format("Minecraft requires java version: %s, java path: %s", new Object[] { Integer.valueOf(launcher.getJavaVersion()), launcher
/*  96 */               .getJavaDir() }) });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void prepareDefaultJava(MinecraftLauncher launcher, JavaVersionName javaVersionName) throws MinecraftException {
/* 102 */     boolean macosAndARM = false;
/* 103 */     Configuration c = TLauncher.getInstance().getConfiguration();
/* 104 */     String gpuInfo = c.get("gpu.info.full");
/* 105 */     String osName = OS.CURRENT.name().toLowerCase();
/* 106 */     String arm64 = "-arm64";
/* 107 */     if (OS.CURRENT.equals(OS.OSX) && Objects.nonNull(gpuInfo) && gpuInfo.contains("Apple M1")) {
/* 108 */       macosAndARM = true;
/* 109 */       osName = osName + arm64;
/*     */     } 
/* 111 */     String key = OS.buildJVMKey();
/*     */     
/* 113 */     Path javaPath = Paths.get(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), new String[] { "runtime", javaVersionName
/* 114 */           .getComponent(), osName, javaVersionName.getComponent() });
/* 115 */     Path javaSHA1Path = Paths.get(javaPath.getParent().toString(), new String[] { javaVersionName.getComponent() + ".sha1" });
/* 116 */     Path javaVersionPath = Paths.get(javaPath.getParent().toString(), new String[] { ".version" });
/*     */     
/* 118 */     JavaVersionDescription javaVersionDescription = null;
/*     */ 
/*     */     
/* 121 */     if (Objects.nonNull(this.javaConfiguration)) {
/* 122 */       JsonObject jsonObject = this.javaConfiguration.get(key);
/* 123 */       if (macosAndARM && Objects.nonNull(this.javaConfiguration.get(key + arm64)))
/* 124 */         jsonObject = this.javaConfiguration.get(key + arm64); 
/* 125 */       if (Objects.nonNull(jsonObject)) {
/* 126 */         JsonArray jsonObject2 = jsonObject.getAsJsonArray(javaVersionName.getComponent());
/*     */         
/* 128 */         if (macosAndARM && jsonObject2.size() == 0) {
/* 129 */           jsonObject2 = ((JsonObject)this.javaConfiguration.get(key)).getAsJsonArray(javaVersionName.getComponent());
/*     */         }
/* 131 */         if (Objects.nonNull(jsonObject2) && jsonObject2.size() != 0) {
/* 132 */           javaVersionDescription = (JavaVersionDescription)this.gson.fromJson(jsonObject2.get(0), JavaVersionDescription.class);
/* 133 */           String name = javaVersionDescription.getVersion().getName();
/*     */           try {
/* 135 */             if (Files.exists(javaVersionPath, new java.nio.file.LinkOption[0]) && 
/* 136 */               !name.equals(FileUtil.readFile(javaVersionPath.toFile()))) {
/* 137 */               FileUtil.deleteDirectory(javaPath.getParent().toFile());
/*     */             }
/* 139 */           } catch (IOException e) {
/* 140 */             U.log(new Object[] { e });
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 145 */     if (Files.exists(javaSHA1Path, new java.nio.file.LinkOption[0])) {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 158 */         Optional<MetadataDTO> notProper = Arrays.<String>stream(FileUtil.readFile(javaSHA1Path.toFile()).split(System.lineSeparator())).map(e -> { String[] array = e.split("/#//"); MetadataDTO m = new MetadataDTO(); m.setPath(array[0].trim()); m.setSha1(array[1].trim().split(" ")[0]); return m; }).filter(e -> !Objects.equals(FileUtil.getChecksum(Paths.get(javaPath.toString(), new String[] { e.getPath() }).toFile()), e.getSha1())).findAny();
/* 159 */         if (notProper.isPresent()) {
/* 160 */           uploadJVM(launcher, javaPath, javaSHA1Path, javaVersionPath, javaVersionDescription);
/*     */         }
/* 162 */       } catch (Exception e) {
/* 163 */         uploadJVM(launcher, javaPath, javaSHA1Path, javaVersionPath, javaVersionDescription);
/*     */       } 
/*     */     } else {
/* 166 */       uploadJVM(launcher, javaPath, javaSHA1Path, javaVersionPath, javaVersionDescription);
/*     */     } 
/* 168 */     launcher.setJavaDir(new File(OS.appendBootstrapperJvm1(javaPath.toString())));
/*     */   }
/*     */ 
/*     */   
/*     */   private void uploadJVM(MinecraftLauncher launcher, Path javaPath, Path javaSHA1Path, Path javaVersionPath, JavaVersionDescription javaVersionDescription) throws MinecraftException {
/* 173 */     if (Files.exists(javaPath.getParent(), new java.nio.file.LinkOption[0]))
/* 174 */       FileUtil.deleteDirectory(javaPath.getParent().toFile()); 
/* 175 */     if (Objects.nonNull(javaVersionDescription)) {
/* 176 */       configureDownloadContainer(launcher, javaPath, javaSHA1Path, javaVersionPath, javaVersionDescription);
/* 177 */     } else if (Files.notExists(javaSHA1Path, new java.nio.file.LinkOption[0])) {
/* 178 */       throw new MinecraftException("jvm folder are not valid, update files", "download", new Object[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void configureDownloadContainer(MinecraftLauncher launcher, Path javaPath, Path javaSHA1Path, Path javaVersionPath, JavaVersionDescription javaVersionDescription) throws MinecraftException {
/* 184 */     if (Objects.isNull(javaVersionDescription)) {
/* 185 */       throw new MinecraftException("jvm folder are not valid, update files", "download", new Object[0]);
/*     */     }
/*     */     try {
/* 188 */       JVMManifest jvmManifest = (JVMManifest)this.gson.fromJson(ClientInstanceRepo.EMPTY_REPO
/* 189 */           .getUrl(javaVersionDescription.getManifest().getUrl()), JVMManifest.class);
/*     */       
/* 191 */       DownloadableContainer container = new DownloadableContainer();
/* 192 */       jvmManifest.getFiles().entrySet().stream().filter(e -> ((JVMFile)e.getValue()).getType().equals("file"))
/* 193 */         .forEach(e -> {
/*     */             MetadataDTO m = ((JVMFile)e.getValue()).getDownloads().getRaw();
/*     */             
/*     */             File destination = new File(javaPath.toString(), (String)e.getKey());
/*     */             if (!Objects.equals(FileUtil.getChecksum(destination), m.getSha1()) || destination.length() != m.getSize()) {
/*     */               m.setLocalDestination(destination);
/*     */               JVMFile jvmFile = (JVMFile)e.getValue();
/*     */               jvmFile.setTargetPath((String)e.getKey());
/*     */               JVMFileDownloadable jvm = new JVMFileDownloadable(jvmFile);
/*     */               container.add((Downloadable)jvm);
/*     */             } 
/*     */           });
/* 205 */       container.addHandler((DownloadableContainerHandler)new DownloadJVMFilesHandler(javaPath, jvmManifest, javaSHA1Path, javaVersionPath, javaVersionDescription
/* 206 */             .getVersion().getName()));
/* 207 */       launcher.getDownloader().add(container);
/* 208 */     } catch (IOException e) {
/* 209 */       throw new MinecraftException("jvm folder are not valid, update files", "download", new Object[0]);
/*     */     } 
/*     */   }
/*     */   private class DownloadJVMFilesHandler extends DownloadableContainerHandlerAdapter { private Path javaPath; private JVMManifest jvmManifest;
/*     */     public DownloadJVMFilesHandler(Path javaPath, JVMManifest jvmManifest, Path javaSHA1Path, Path javaVersionPath, String javaVersionName1) {
/* 214 */       this.javaPath = javaPath; this.jvmManifest = jvmManifest; this.javaSHA1Path = javaSHA1Path; this.javaVersionPath = javaVersionPath; this.javaVersionName1 = javaVersionName1;
/*     */     }
/*     */ 
/*     */     
/*     */     private Path javaSHA1Path;
/*     */     
/*     */     private Path javaVersionPath;
/*     */     
/*     */     private String javaVersionName1;
/*     */     
/*     */     public void onFullComplete(DownloadableContainer c) {
/* 225 */       if (c.getList().size() != 0) {
/*     */         
/*     */         try {
/*     */ 
/*     */           
/* 230 */           List<String> list = (List<String>)Files.walk(this.javaPath, new java.nio.file.FileVisitOption[0]).filter(x$0 -> Files.isRegularFile(x$0, new java.nio.file.LinkOption[0])).map(e -> String.format("%s %s %s %s", new Object[] { this.javaPath.relativize(e).toString(), "/#//", FileUtil.getChecksum(e.toFile()), System.currentTimeMillis() + "0000" })).collect(Collectors.toList());
/* 231 */           Files.write(this.javaSHA1Path, (Iterable)list, new OpenOption[] { StandardOpenOption.CREATE });
/* 232 */           Files.write(this.javaVersionPath, this.javaVersionName1.getBytes(), new OpenOption[] { StandardOpenOption.CREATE });
/* 233 */           this.jvmManifest.getFiles().entrySet().stream().filter(e -> ((JVMFile)e.getValue()).getType().equals("directory"))
/* 234 */             .forEach(e -> {
/*     */                 try {
/*     */                   Files.createDirectories(Paths.get(this.javaPath.toString(), new String[] { (String)e.getKey() }), (FileAttribute<?>[])new FileAttribute[0]);
/* 237 */                 } catch (IOException e1) {
/*     */                   U.log(new Object[] { e1 });
/*     */                 } 
/*     */               });
/* 241 */           this.jvmManifest.getFiles().entrySet().stream().filter(e -> ((JVMFile)e.getValue()).getType().equals("link"))
/* 242 */             .forEach(e -> {
/*     */                 Path link = Paths.get(this.javaPath.toString(), new String[] { (String)e.getKey() });
/*     */                 
/*     */                 if (Files.notExists(link, new java.nio.file.LinkOption[0])) {
/*     */                   try {
/*     */                     U.log(new Object[] { String.format("%s %s", new Object[] { Paths.get(((JVMFile)e.getValue()).getTarget(), new String[0]), Paths.get(this.javaPath.toString(), new String[] { (String)e.getKey() }) }) });
/*     */                     Files.createSymbolicLink(link, Paths.get(((JVMFile)e.getValue()).getTarget(), new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
/* 249 */                   } catch (IOException e1) {
/*     */                     U.log(new Object[] { e1 });
/*     */                   } 
/*     */                 }
/*     */               });
/* 254 */         } catch (IOException e) {
/* 255 */           U.log(new Object[] { e });
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete(DownloadableContainer c, Downloadable d) throws RetryDownloadException {
/* 262 */       JVMFileDownloadable d1 = (JVMFileDownloadable)d;
/* 263 */       if (d1.getJvmFile().isExecutable() && !OS.is(new OS[] { OS.WINDOWS }))
/*     */         try {
/* 265 */           Files.setPosixFilePermissions(d.getMetadataDTO().getLocalDestination().toPath(), FileUtil.PERMISSIONS);
/*     */         }
/* 267 */         catch (IOException e) {
/* 268 */           U.log(new Object[] { e });
/*     */         }  
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\component\minecraft\JavaMinecraftComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */