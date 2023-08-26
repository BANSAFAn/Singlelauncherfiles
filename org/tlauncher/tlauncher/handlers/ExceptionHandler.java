/*    */ package org.tlauncher.tlauncher.handlers;
/*    */ 
/*    */ import org.apache.log4j.Logger;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ import org.tlauncher.util.Reflect;
/*    */ import org.tlauncher.util.TlauncherUtil;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
/*    */   private static ExceptionHandler instance;
/*    */   private static long gcLastCall;
/* 13 */   private static final Logger LOGGER = Logger.getLogger("main");
/*    */   
/*    */   public static ExceptionHandler getInstance() {
/* 16 */     if (instance == null)
/* 17 */       instance = new ExceptionHandler(); 
/* 18 */     return instance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void uncaughtException(Thread t, Throwable e) {
/* 26 */     OutOfMemoryError asOOM = (OutOfMemoryError)Reflect.cast(e, OutOfMemoryError.class);
/* 27 */     if (asOOM != null && reduceMemory(asOOM)) {
/*    */       return;
/*    */     }
/* 30 */     if (scanTrace(e)) {
/*    */       try {
/* 32 */         Alert.showError("Exception in thread " + t.getName(), e);
/* 33 */       } catch (Exception w) {
/* 34 */         w.printStackTrace();
/*    */       } 
/*    */     } else {
/* 37 */       LOGGER.warn("exception", e);
/* 38 */       U.log(new Object[] { e });
/*    */     } 
/*    */     
/* 41 */     if (e instanceof UnsatisfiedLinkError && e.getMessage().contains("jfxwebkit.dll")) {
/* 42 */       U.log(new Object[] { "should use new jvm" });
/* 43 */       TLauncher.getInstance().getConfiguration().set("not.work.jfxwebkit.dll", Boolean.valueOf(true));
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 50 */     if (TLauncher.getInstance().getConfiguration().getBoolean("gui.statistics.checkbox")) {
/* 51 */       String type = TLauncher.getInnerSettings().get("type");
/* 52 */       if ("BETA".equals(type)) {
/* 53 */         TlauncherUtil.sendLog(type);
/*    */       } else {
/* 55 */         TlauncherUtil.sendLog(e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   public static boolean reduceMemory(OutOfMemoryError e) {
/* 60 */     if (e == null) {
/* 61 */       return false;
/*    */     }
/* 63 */     U.log(new Object[] { "OutOfMemory error has occurred, solving..." });
/* 64 */     long currentTime = System.currentTimeMillis(), diff = Math.abs(currentTime - gcLastCall);
/*    */     
/* 66 */     if (diff > 5000L) {
/* 67 */       gcLastCall = currentTime;
/*    */       
/* 69 */       U.gc();
/*    */       
/* 71 */       return true;
/*    */     } 
/*    */     
/* 74 */     U.log(new Object[] { "GC is unable to reduce memory usage" });
/* 75 */     return false;
/*    */   }
/*    */   
/*    */   private static boolean scanTrace(Throwable e) {
/* 79 */     StackTraceElement[] elements = e.getStackTrace();
/*    */     
/* 81 */     for (StackTraceElement element : elements) {
/* 82 */       if (element.getClassName().startsWith("org.tlauncher"))
/* 83 */         return true; 
/*    */     } 
/* 85 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\handlers\ExceptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */