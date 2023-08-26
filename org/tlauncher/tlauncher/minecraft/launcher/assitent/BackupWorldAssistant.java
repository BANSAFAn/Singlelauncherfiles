/*     */ package org.tlauncher.tlauncher.minecraft.launcher.assitent;
/*     */ 
/*     */ import by.gdev.utils.service.FileMapperService;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Singleton;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.time.Duration;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.ZoneId;
/*     */ import java.time.temporal.ChronoUnit;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.tlauncher.tlauncher.configuration.enums.BackupSetting;
/*     */ import org.tlauncher.tlauncher.entity.BackupWorldList;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.MinecraftUtil;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ @Singleton
/*     */ public class BackupWorldAssistant
/*     */   extends MinecraftLauncherAssistantWrapper
/*     */ {
/*  39 */   private static final Logger log = Logger.getLogger(BackupWorldAssistant.class);
/*     */   
/*     */   @Inject
/*     */   private TLauncher tLauncher;
/*     */   @Inject
/*     */   private FileMapperService fileMapperService;
/*     */   private BackupWorldList backupWorldList;
/*     */   @Inject
/*     */   private Gson gson;
/*     */   
/*     */   @Inject
/*     */   public void init() throws IOException {
/*  51 */     this.backupWorldList = new BackupWorldList();
/*  52 */     File worldJson = new File(MinecraftUtil.getWorkingDirectory(), "worlds.json");
/*  53 */     FileUtil.createFile(worldJson);
/*  54 */     this.backupWorldList = (BackupWorldList)this.fileMapperService.read("worlds.json", BackupWorldList.class);
/*  55 */     if (Objects.isNull(this.backupWorldList))
/*  56 */       this.backupWorldList = new BackupWorldList(); 
/*  57 */     if (log.isDebugEnabled()) {
/*  58 */       log.debug(this.gson.toJson(this.backupWorldList));
/*     */     }
/*     */   }
/*     */   
/*     */   public void constructJavaArguments(MinecraftLauncher launcher) throws MinecraftException {
/*     */     try {
/*  64 */       if (isBackupAvailable()) {
/*  65 */         (this.tLauncher.getFrame()).mp.getProgress().onWorldBackup(" ");
/*  66 */         collectNewWorlds();
/*  67 */         checkIfWorldsAreChanged();
/*  68 */         checkIfModpackWorldsAreChanged();
/*  69 */         backupWorld();
/*  70 */         saveWorlds();
/*     */       } 
/*  72 */       deleteOldBackupOrNotExistedWorlds();
/*  73 */     } catch (IOException e) {
/*  74 */       U.log(new Object[] { e.getMessage() });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isBackupAvailable() {
/*  80 */     boolean res = false;
/*  81 */     if (!this.tLauncher.getConfiguration().getBoolean(BackupSetting.SKIP_USER_BACKUP.toString())) {
/*  82 */       int requestFreeGB = this.tLauncher.getConfiguration().getInteger(BackupSetting.FREE_PARTITION_SIZE.toString());
/*  83 */       res = FileUtil.checkFreeSpace(MinecraftUtil.getWorkingDirectory(), (requestFreeGB * 1024 * 1024));
/*  84 */       if (!res) {
/*  85 */         log.warn("Free space on partition doesn't enough for backup : " + (
/*  86 */             MinecraftUtil.getWorkingDirectory().getFreeSpace() / Math.pow(1024.0D, 3.0D)));
/*     */       }
/*     */     } 
/*  89 */     log.info("backup world is active: " + res);
/*  90 */     return res;
/*     */   }
/*     */   
/*     */   private void saveWorlds() throws IOException {
/*  94 */     log.debug("saved backup world configuration");
/*  95 */     this.fileMapperService.write(this.backupWorldList, "worlds.json");
/*     */   }
/*     */   
/*     */   private void collectNewWorlds() throws IOException {
/*  99 */     Path simpleWorlds = MinecraftUtil.buildWorkingPath(new String[] { "saves" });
/* 100 */     Path versions = MinecraftUtil.buildWorkingPath(new String[] { "versions" });
/* 101 */     Path destForSimpleWorld = MinecraftUtil.buildWorkingPath(new String[] { "backup/saves", "saves" });
/*     */     
/* 103 */     Path destForModPackWorlds = MinecraftUtil.buildWorkingPath(new String[] { "backup/saves", "modpacks_saves" });
/*     */ 
/*     */     
/* 106 */     Files.createDirectories(destForSimpleWorld, (FileAttribute<?>[])new FileAttribute[0]);
/* 107 */     Files.createDirectories(destForModPackWorlds, (FileAttribute<?>[])new FileAttribute[0]);
/*     */     
/* 109 */     addWorldToBackupSet(destForSimpleWorld.toFile(), Files.walk(simpleWorlds, 1, new java.nio.file.FileVisitOption[0]));
/*     */     
/* 111 */     Files.walk(versions, 1, new java.nio.file.FileVisitOption[0]).filter(version -> (new File(version + "/" + "saves")).exists())
/* 112 */       .forEach(version -> {
/*     */           try {
/*     */             Stream<Path> walk2 = Files.walk(Paths.get(version.toString(), new String[] { "saves" }), 1, new java.nio.file.FileVisitOption[0]);
/*     */             addWorldToBackupSet(destForModPackWorlds.toFile(), walk2);
/* 116 */           } catch (IOException e) {
/*     */             U.log(new Object[] { e.getMessage() });
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private void addWorldToBackupSet(File dest, Stream<Path> walk) {
/* 123 */     walk.filter(world -> !world.toFile().getName().equals("saves")).forEach(world -> {
/*     */           try {
/*     */             LocalDateTime time = LocalDateTime.ofInstant(((FileTime)Files.getAttribute(world, BackupSetting.LAST_MODIFIED_TIME.toString(), new java.nio.file.LinkOption[0])).toInstant(), ZoneId.systemDefault());
/*     */             
/*     */             if (this.backupWorldList.getWorlds().stream().anyMatch(())) {
/*     */               return;
/*     */             }
/*     */             
/*     */             File destination = new File(dest, world.getFileName().toString());
/*     */             
/*     */             this.backupWorldList.getWorlds().add(new BackupWorldList.BackupWorld(world.toFile().getName(), world.toString(), destination.getAbsolutePath(), time.withNano(0).toString(), false));
/*     */             log.info("Add new world to backup list: " + world.toFile().getName());
/* 135 */           } catch (IOException e) {
/*     */             log.warn("exception", e);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private void backupWorld() {
/* 142 */     this.backupWorldList.getWorlds().stream().filter(BackupWorldList.BackupWorld::isBackup)
/* 143 */       .filter(b -> Files.isDirectory(Paths.get(b.getSource(), new String[0]), new java.nio.file.LinkOption[0])).forEach(backupWorld -> {
/*     */           log.info("backup world: " + backupWorld.getName());
/*     */           
/*     */           (this.tLauncher.getFrame()).mp.getProgress().onWorldBackup(" " + backupWorld.getName() + " ");
/*     */           
/*     */           try {
/*     */             LocalDateTime time = LocalDateTime.ofInstant(((FileTime)Files.getAttribute(Paths.get(backupWorld.getSource(), new String[0]), BackupSetting.LAST_MODIFIED_TIME.toString(), new java.nio.file.LinkOption[0])).toInstant(), ZoneId.systemDefault());
/*     */             
/*     */             backupWorld.setLastChanged(time.withNano(0).toString());
/*     */             
/*     */             backupWorld.setBackup(false);
/*     */             
/*     */             FileUtil.createFolder(new File(backupWorld.getDestination()));
/*     */             
/*     */             File dest = Paths.get(backupWorld.getDestination(), new String[] { backupWorld.getName().concat("_").concat(backupWorld.getLastChanged().replaceAll(":", "_")).concat(".zip") }).toFile();
/*     */             
/*     */             FileUtil.zipFolder(new File(backupWorld.getSource()), dest);
/*     */             log.info("backup world finished: " + backupWorld.getName());
/* 161 */           } catch (Exception e) {
/*     */             log.warn("exception", e);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void deleteOldBackupOrNotExistedWorlds() throws IOException {
/* 169 */     Files.walk(MinecraftUtil.buildWorkingPath(new String[] { "backup/saves" }, ), new java.nio.file.FileVisitOption[0])
/* 170 */       .filter(backupWorld -> backupWorld.toFile().isFile()).filter(backupWorld -> {
/*     */           try {
/*     */             LocalDateTime time = LocalDateTime.ofInstant(((FileTime)Files.getAttribute(Paths.get(backupWorld.toString(), new String[0]), BackupSetting.LAST_MODIFIED_TIME.toString(), new java.nio.file.LinkOption[0])).toInstant(), ZoneId.systemDefault());
/*     */ 
/*     */             
/*     */             long daysBetween = ChronoUnit.DAYS.between(time, LocalDateTime.now());
/*     */ 
/*     */             
/*     */             return (daysBetween > this.tLauncher.getConfiguration().getInteger(BackupSetting.MAX_TIME_FOR_BACKUP.toString()));
/* 179 */           } catch (IOException e) {
/*     */             U.log(new Object[] { e.getMessage() });
/*     */             return false;
/*     */           } 
/* 183 */         }).forEach(oldWorld -> {
/*     */           log.info("Deleted world: " + oldWorld.toAbsolutePath());
/*     */           
/*     */           FileUtil.deleteFile(oldWorld.toFile());
/*     */         });
/*     */     
/* 189 */     Set<BackupWorldList.BackupWorld> existedWorlds = (Set<BackupWorldList.BackupWorld>)this.backupWorldList.getWorlds().stream().filter(backupWorld -> Paths.get(backupWorld.getSource(), new String[0]).toFile().exists()).collect(Collectors.toSet());
/* 190 */     if (existedWorlds.size() < this.backupWorldList.getWorlds().size()) {
/* 191 */       this.backupWorldList.setWorlds(existedWorlds);
/* 192 */       saveWorlds();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkIfModpackWorldsAreChanged() throws IOException {
/* 198 */     List<Path> versions = (List<Path>)Files.walk(MinecraftUtil.buildWorkingPath(new String[] { "versions" }, ), 1, new java.nio.file.FileVisitOption[0]).filter(x$0 -> Files.isDirectory(x$0, new java.nio.file.LinkOption[0])).collect(Collectors.toList());
/* 199 */     for (Path version : versions) {
/* 200 */       Path versionSaves = Paths.get(version.toString(), new String[] { "saves" });
/* 201 */       if (Files.notExists(versionSaves, new java.nio.file.LinkOption[0]))
/*     */         continue; 
/* 203 */       checkIfWorldsAreChanged(Files.walk(versionSaves, 1, new java.nio.file.FileVisitOption[0]).collect((Collector)Collectors.toList()));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkIfWorldsAreChanged() throws IOException {
/* 208 */     Path source = MinecraftUtil.buildWorkingPath(new String[] { "saves" });
/* 209 */     checkIfWorldsAreChanged(Files.walk(source, 1, new java.nio.file.FileVisitOption[0]).collect((Collector)Collectors.toList()));
/*     */   }
/*     */   
/*     */   private void checkIfWorldsAreChanged(List<Path> files) throws IOException {
/* 213 */     for (Path world : files) {
/* 214 */       FileTime creationTime = (FileTime)Files.getAttribute(world, BackupSetting.LAST_MODIFIED_TIME.toString(), new java.nio.file.LinkOption[0]);
/* 215 */       LocalDateTime convertedFileTime = LocalDateTime.ofInstant(creationTime.toInstant(), ZoneId.systemDefault());
/*     */       
/* 217 */       Optional<BackupWorldList.BackupWorld> backupWorld1 = this.backupWorldList.getWorlds().stream().filter(backupWorld -> backupWorld.getSource().equals(world.toString())).findAny();
/* 218 */       if (!backupWorld1.isPresent())
/*     */         continue; 
/* 220 */       String worldTime = ((BackupWorldList.BackupWorld)backupWorld1.get()).getLastChanged();
/* 221 */       if (Paths.get(((BackupWorldList.BackupWorld)backupWorld1.get()).getDestination(), new String[0]).toFile().listFiles() == null) {
/* 222 */         ((BackupWorldList.BackupWorld)backupWorld1.get()).setBackup(true); continue;
/* 223 */       }  if (!worldTime.equals(convertedFileTime.withNano(0).toString())) {
/* 224 */         if (Paths.get(((BackupWorldList.BackupWorld)backupWorld1.get()).getDestination(), new String[0]).toFile().listFiles() == null) {
/* 225 */           ((BackupWorldList.BackupWorld)backupWorld1.get()).setBackup(true); continue;
/* 226 */         }  if (!isProperBackupWorld(backupWorld1.get())) {
/* 227 */           ((BackupWorldList.BackupWorld)backupWorld1.get()).setBackup(true);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isProperBackupWorld(BackupWorldList.BackupWorld backupWorld) throws IOException {
/* 238 */     long lastBackupSize = Paths.get(backupWorld.getDestination(), new String[] { backupWorld.getName().concat("_").concat(backupWorld.getLastChanged().replaceAll(":", "_").concat(".zip")) }).toFile().length();
/* 239 */     LocalDateTime backupLastChanged = LocalDateTime.parse(backupWorld.getLastChanged());
/*     */     
/* 241 */     long totalHoursPassed = Duration.between(backupLastChanged, LocalDateTime.now()).toHours();
/* 242 */     if (this.tLauncher.getConfiguration().getInteger(BackupSetting.REPEAT_BACKUP.toString()) == 1) {
/* 243 */       return (totalHoursPassed < 24L);
/*     */     }
/* 245 */     if (lastBackupSize / Math.pow(1024.0D, 2.0D) > this.tLauncher.getConfiguration()
/* 246 */       .getInteger(BackupSetting.MAX_SIZE_FOR_WORLD.toString()))
/* 247 */       return true; 
/* 248 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\launcher\assitent\BackupWorldAssistant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */