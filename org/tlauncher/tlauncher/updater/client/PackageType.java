/*    */ package org.tlauncher.tlauncher.updater.client;
/*    */ import org.tlauncher.util.FileUtil;
/*    */ 
/*    */ public enum PackageType {
/*    */   public static final PackageType CURRENT;
/*  6 */   EXE, JAR;
/*    */   static {
/*  8 */     CURRENT = FileUtil.getRunningJar().toString().endsWith(".exe") ? EXE : JAR;
/*    */   }
/*    */   public String toLowerCase() {
/* 11 */     return name().toLowerCase();
/*    */   }
/*    */   
/*    */   public static boolean isCurrent(PackageType pt) {
/* 15 */     return (pt == CURRENT);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\client\PackageType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */