/*    */ package org.tlauncher.tlauncher.modpack;
/*    */ 
/*    */ import com.google.inject.Inject;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.CopyOption;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.StandardCopyOption;
/*    */ import net.minecraft.launcher.versions.Version;
/*    */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*    */ import org.tlauncher.tlauncher.minecraft.crash.Crash;
/*    */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
/*    */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.util.FileUtil;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModpackGameListener
/*    */   implements MinecraftListener
/*    */ {
/*    */   @Inject
/*    */   private ModpackManager modpackManager;
/*    */   @Inject
/*    */   private TLauncher tLauncher;
/*    */   
/*    */   public void onMinecraftPrepare() {}
/*    */   
/*    */   public void onMinecraftAbort() {}
/*    */   
/*    */   public void onMinecraftLaunch() {
/* 37 */     U.log(new Object[] { "copied to modpack servers.dat" });
/* 38 */     copy(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMinecraftClose() {
/* 43 */     U.log(new Object[] { "copy from modpack servers.dat " });
/* 44 */     copy(false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onMinecraftError(Throwable e) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onMinecraftKnownError(MinecraftException e) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onMinecraftCrash(Crash crash) {}
/*    */ 
/*    */ 
/*    */   
/*    */   private void copy(boolean toModpack) {
/* 63 */     String servers = "servers.dat";
/* 64 */     Path modpackServers = ModpackUtil.getPathByVersion((Version)this.tLauncher.getLauncher().getVersion(), new String[] { servers });
/* 65 */     Path baseServers = FileUtil.getRelative(servers);
/*    */     try {
/* 67 */       if (toModpack) {
/* 68 */         Files.copy(baseServers, modpackServers, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*    */       } else {
/* 70 */         Files.copy(modpackServers, baseServers, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*    */       } 
/* 72 */     } catch (IOException e) {
/* 73 */       U.log(new Object[] { e });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\modpack\ModpackGameListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */