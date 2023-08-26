/*    */ package org.tlauncher.util.statistics;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
/*    */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListenerAdapter;
/*    */ 
/*    */ 
/*    */ public class GameRunningListener
/*    */   extends MinecraftListenerAdapter
/*    */ {
/*    */   private MinecraftLauncher game;
/*    */   
/*    */   public GameRunningListener(MinecraftLauncher game) {
/* 15 */     this.game = game;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMinecraftLaunch() {
/* 20 */     Map<String, Object> map = new HashMap<>();
/* 21 */     map.put("version", this.game.getVersion().getID());
/* 22 */     StatisticsUtil.startSending("save/run/version", null, map);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\statistics\GameRunningListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */