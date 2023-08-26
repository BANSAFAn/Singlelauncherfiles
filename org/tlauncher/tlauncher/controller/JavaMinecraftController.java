/*    */ package org.tlauncher.tlauncher.controller;
/*    */ 
/*    */ import com.google.common.eventbus.EventBus;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.Singleton;
/*    */ import java.io.IOException;
/*    */ import java.util.Comparator;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import org.tlauncher.tlauncher.configuration.Configuration;
/*    */ import org.tlauncher.tlauncher.entity.minecraft.MinecraftJava;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.util.FileUtil;
/*    */ import org.tlauncher.util.MinecraftUtil;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Singleton
/*    */ public class JavaMinecraftController
/*    */ {
/* 24 */   private final String filename = "minecraft_tlauncher_java_config.json";
/*    */   public static final String SELECTED_JAVA_KEY = "minecraft.java.selected";
/*    */   @Inject
/*    */   private EventBus eventBus;
/*    */   @Inject
/*    */   private Gson gson;
/*    */   private MinecraftJava minecraftJava;
/*    */   private Configuration con;
/*    */   
/*    */   @Inject
/*    */   private void init() {
/* 35 */     this.minecraftJava = read();
/* 36 */     this.con = TLauncher.getInstance().getConfiguration();
/*    */   }
/*    */   
/*    */   public MinecraftJava.CompleteMinecraftJava getCurrent() {
/* 40 */     return (MinecraftJava.CompleteMinecraftJava)this.minecraftJava.getJvm().get(Long.valueOf(this.con.getLong("minecraft.java.selected")));
/*    */   }
/*    */   
/*    */   public boolean isUserJavaVersion() {
/* 44 */     return Objects.nonNull(getCurrent());
/*    */   }
/*    */   
/*    */   public void add(MinecraftJava.CompleteMinecraftJava completeMinecraftJava) {
/* 48 */     if (Objects.isNull(completeMinecraftJava.getId())) {
/*    */       
/* 50 */       Optional<Long> max = this.minecraftJava.getJvm().values().stream().map(e -> e.getId()).max(Comparator.comparing(Long::valueOf));
/* 51 */       completeMinecraftJava.setId(Long.valueOf(max.isPresent() ? (((Long)max.get()).longValue() + 1L) : 1L));
/*    */     } 
/* 53 */     this.minecraftJava.getJvm().put(completeMinecraftJava.getId(), completeMinecraftJava);
/* 54 */     this.eventBus.post(this.minecraftJava);
/* 55 */     write();
/*    */   }
/*    */   
/*    */   public void remove(MinecraftJava.CompleteMinecraftJava java) {
/* 59 */     this.minecraftJava.getJvm().remove(java.getId());
/* 60 */     this.eventBus.post(this.minecraftJava);
/* 61 */     this.con.set("minecraft.java.selected", Integer.valueOf(0));
/* 62 */     write();
/*    */   }
/*    */   
/*    */   private MinecraftJava read() {
/*    */     try {
/* 67 */       return (MinecraftJava)this.gson.fromJson(FileUtil.readFile(MinecraftUtil.getTLauncherFile("minecraft_tlauncher_java_config.json")), MinecraftJava.class);
/* 68 */     } catch (IOException e) {
/* 69 */       if (!(e instanceof java.io.FileNotFoundException)) {
/* 70 */         write();
/* 71 */         U.log(new Object[] { e });
/*    */       } 
/* 73 */       this.minecraftJava = new MinecraftJava();
/* 74 */       return this.minecraftJava;
/*    */     } 
/*    */   }
/*    */   
/*    */   private void write() {
/*    */     try {
/* 80 */       FileUtil.writeFile(MinecraftUtil.getTLauncherFile("minecraft_tlauncher_java_config.json"), this.gson.toJson(this.minecraftJava));
/* 81 */     } catch (IOException e) {
/* 82 */       U.log(new Object[] { e });
/*    */     } 
/*    */   }
/*    */   
/*    */   public void notifyListeners() {
/* 87 */     this.eventBus.post(this.minecraftJava);
/*    */   }
/*    */   
/*    */   public MinecraftJava.CompleteMinecraftJava getById(Long value) {
/* 91 */     return (MinecraftJava.CompleteMinecraftJava)this.minecraftJava.getJvm().get(value);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\controller\JavaMinecraftController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */