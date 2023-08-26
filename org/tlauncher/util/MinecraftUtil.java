/*    */ package org.tlauncher.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.List;
/*    */ import org.tlauncher.tlauncher.configuration.Configuration;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ 
/*    */ public class MinecraftUtil
/*    */ {
/*    */   public static File getWorkingDirectory() {
/* 14 */     if (TLauncher.getInstance() == null) {
/* 15 */       return getDefaultWorkingDirectory();
/*    */     }
/* 17 */     Configuration settings = TLauncher.getInstance().getConfiguration();
/* 18 */     String sdir = settings.get("minecraft.gamedir");
/* 19 */     return getWorkingDirectory(sdir);
/*    */   }
/*    */   public static Path buildWorkingPath(String... path) {
/* 22 */     return Paths.get(getWorkingDirectory().getAbsolutePath(), path);
/*    */   }
/*    */   
/*    */   public static File getWorkingDirectory(String sdir) {
/* 26 */     if (sdir == null) {
/* 27 */       return getDefaultWorkingDirectory();
/*    */     }
/* 29 */     File dir = new File(sdir);
/*    */     
/*    */     try {
/* 32 */       FileUtil.createFolder(dir);
/* 33 */     } catch (IOException e) {
/* 34 */       U.log(new Object[] { "Cannot createScrollWrapper specified Minecraft folder:", dir.getAbsolutePath() });
/* 35 */       return getDefaultWorkingDirectory();
/*    */     } 
/*    */     
/* 38 */     return dir;
/*    */   }
/*    */   
/*    */   public static File getSystemRelatedFile(String path) {
/* 42 */     String applicationData, folder, userHome = System.getProperty("user.home", ".");
/*    */ 
/*    */     
/* 45 */     switch (OS.CURRENT)
/*    */     { case LINUX:
/*    */       case SOLARIS:
/* 48 */         file = new File(userHome, path);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 62 */         return file;case WINDOWS: applicationData = System.getenv("APPDATA"); folder = (applicationData != null) ? applicationData : userHome; file = new File(folder, path); return file;case OSX: file = new File(userHome, "Library/Application Support/" + path); return file; }  File file = new File(userHome, path); return file;
/*    */   }
/*    */   
/*    */   public static File getSystemRelatedDirectory(String path) {
/* 66 */     if (!OS.is(new OS[] { OS.OSX, OS.UNKNOWN })) {
/* 67 */       path = '.' + path;
/*    */     }
/* 69 */     return getSystemRelatedFile(path);
/*    */   }
/*    */   
/*    */   public static File getDefaultWorkingDirectory() {
/* 73 */     return getSystemRelatedDirectory(TLauncher.getFolder());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static File getTLauncherFile(String path) {
/* 81 */     return new File(getSystemRelatedDirectory("tlauncher"), path);
/*    */   }
/*    */   
/*    */   public static boolean isUsernameValid(String username) {
/* 85 */     return (username.length() > 2 && username.length() <= 16 && username.charAt(0) != '-');
/*    */   }
/*    */   public static void configureG1GC(List<String> list) {
/* 88 */     list.add("-XX:+UnlockExperimentalVMOptions");
/* 89 */     list.add("-XX:+UseG1GC");
/* 90 */     list.add("-XX:G1NewSizePercent=20");
/* 91 */     list.add("-XX:G1ReservePercent=20");
/* 92 */     list.add("-XX:MaxGCPauseMillis=50");
/* 93 */     list.add("-XX:G1HeapRegionSize=32M");
/* 94 */     list.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
/* 95 */     list.add("-Dfml.ignorePatchDiscrepancies=true");
/* 96 */     list.add("-Djava.net.preferIPv4Stack=true");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\MinecraftUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */