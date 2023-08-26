/*    */ package org.tlauncher.tlauncher.configuration.enums;
/*    */ 
/*    */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
/*    */ 
/*    */ public enum ConsoleType {
/*  6 */   GLOBAL, NONE;
/*    */   
/*    */   public static boolean parse(String val) {
/*  9 */     if (val == null)
/* 10 */       return false; 
/* 11 */     for (ConsoleType cur : values()) {
/* 12 */       if (cur.toString().equalsIgnoreCase(val))
/* 13 */         return true; 
/* 14 */     }  return false;
/*    */   }
/*    */   
/*    */   public static ConsoleType get(String val) {
/* 18 */     for (ConsoleType cur : values()) {
/* 19 */       if (cur.toString().equalsIgnoreCase(val))
/* 20 */         return cur; 
/* 21 */     }  throw new NullPointerException("not find console type " + val);
/*    */   }
/*    */   
/*    */   public MinecraftLauncher.ConsoleVisibility getVisibility() {
/* 25 */     return (this == GLOBAL) ? MinecraftLauncher.ConsoleVisibility.ALWAYS : MinecraftLauncher.ConsoleVisibility.ON_CRASH;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 30 */     return super.toString().toLowerCase();
/*    */   }
/*    */   
/*    */   public static ConsoleType getDefault() {
/* 34 */     return NONE;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\configuration\enums\ConsoleType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */