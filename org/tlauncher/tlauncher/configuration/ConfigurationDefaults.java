/*    */ package org.tlauncher.tlauncher.configuration;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.launcher.versions.ReleaseType;
/*    */ import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
/*    */ import org.tlauncher.tlauncher.configuration.enums.BackupSetting;
/*    */ import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
/*    */ import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
/*    */ import org.tlauncher.tlauncher.updater.client.Notices;
/*    */ import org.tlauncher.util.IntegerArray;
/*    */ import org.tlauncher.util.MinecraftUtil;
/*    */ import org.tlauncher.util.OS;
/*    */ 
/*    */ 
/*    */ class ConfigurationDefaults
/*    */ {
/*    */   private static final int version = 3;
/*    */   private final Map<String, Object> d;
/*    */   
/*    */   ConfigurationDefaults(InnerConfiguration inner) {
/* 24 */     this.d = new HashMap<>();
/*    */     
/* 26 */     this.d.put("settings.version", Integer.valueOf(3));
/*    */     
/* 28 */     this.d.put("minecraft.gamedir", MinecraftUtil.getDefaultWorkingDirectory().getAbsolutePath());
/* 29 */     this.d.put("minecraft.size", new IntegerArray(new int[] { 925, 530 }));
/* 30 */     this.d.put("minecraft.fullscreen", Boolean.valueOf(false));
/*    */     
/* 32 */     for (ReleaseType type : ReleaseType.getDefault()) {
/* 33 */       this.d.put("minecraft.versions." + type.name().toLowerCase(), Boolean.valueOf(true));
/*    */     }
/* 35 */     for (ReleaseType.SubType type : ReleaseType.SubType.getDefault()) {
/* 36 */       this.d.put("minecraft.versions.sub." + type.name().toLowerCase(), Boolean.valueOf(true));
/*    */     }
/* 38 */     for (Notices.NoticeType type : Notices.NoticeType.values()) {
/* 39 */       if (type.isAdvert())
/* 40 */         this.d.put("gui.notice." + type.name().toLowerCase(), Boolean.valueOf(true)); 
/*    */     } 
/* 42 */     this.d.put("minecraft.memory.ram2", Integer.valueOf(OS.Arch.PREFERRED_MEMORY));
/*    */     
/* 44 */     this.d.put("minecraft.onlaunch", ActionOnLaunch.getDefault());
/*    */     
/* 46 */     this.d.put("gui.console", ConsoleType.getDefault());
/* 47 */     this.d.put("gui.console.width", Integer.valueOf(720));
/* 48 */     this.d.put("gui.console.height", Integer.valueOf(500));
/* 49 */     this.d.put("gui.console.x", Integer.valueOf(30));
/* 50 */     this.d.put("gui.console.y", Integer.valueOf(30));
/*    */     
/* 52 */     this.d.put("connection", ConnectionQuality.getDefault());
/* 53 */     this.d.put("client", UUID.randomUUID());
/* 54 */     this.d.put("gui.statistics.checkbox", Boolean.valueOf(false));
/* 55 */     this.d.put("gui.settings.guard.checkbox", Boolean.valueOf(true));
/* 56 */     this.d.put("gui.settings.servers.recommendation", Boolean.valueOf(true));
/* 57 */     this.d.put("gui.settings.servers.recommendation", Boolean.valueOf(true));
/* 58 */     this.d.put(BackupSetting.FREE_PARTITION_SIZE.toString(), Integer.valueOf(inner.getInteger(BackupSetting.FREE_PARTITION_SIZE.toString())));
/* 59 */     this.d.put(BackupSetting.SKIP_USER_BACKUP.toString(), Boolean.valueOf(inner.getBoolean(BackupSetting.SKIP_USER_BACKUP.toString())));
/* 60 */     this.d.put(BackupSetting.MAX_TIME_FOR_BACKUP.toString(), Integer.valueOf(inner.getInteger(BackupSetting.MAX_TIME_FOR_BACKUP.toString())));
/* 61 */     this.d.put(BackupSetting.REPEAT_BACKUP.toString(), Integer.valueOf(inner.getInteger(BackupSetting.REPEAT_BACKUP.toString())));
/* 62 */     this.d.put(BackupSetting.MAX_SIZE_FOR_WORLD.toString(), Integer.valueOf(inner.getInteger(BackupSetting.MAX_SIZE_FOR_WORLD.toString())));
/*    */   }
/*    */   
/*    */   public static int getVersion() {
/* 66 */     return 3;
/*    */   }
/*    */   
/*    */   public Map<String, Object> getMap() {
/* 70 */     return Collections.unmodifiableMap(this.d);
/*    */   }
/*    */   
/*    */   public Object get(String key) {
/* 74 */     return this.d.get(key);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\configuration\ConfigurationDefaults.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */