/*    */ package org.tlauncher.tlauncher.configuration.enums;
/*    */ 
/*    */ 
/*    */ public enum ConnectionQuality
/*    */ {
/*  6 */   GOOD(2, 5, 3, 30000), NORMAL(3, 6, 2, 45000), BAD(3, 6, 1, 60000);
/*    */   
/*    */   private final int[] configuration;
/*    */   private final int timeout;
/*    */   
/*    */   ConnectionQuality(int minTries, int maxTries, int maxThreads, int timeout) {
/* 12 */     this.minTries = minTries;
/* 13 */     this.maxTries = maxTries;
/* 14 */     this.maxThreads = maxThreads;
/*    */     
/* 16 */     this.timeout = timeout;
/*    */     
/* 18 */     this.configuration = new int[] { minTries, maxTries, maxThreads };
/*    */   }
/*    */   private final int maxThreads; private final int maxTries; private final int minTries;
/*    */   public static boolean parse(String val) {
/* 22 */     if (val == null)
/* 23 */       return false; 
/* 24 */     for (ConnectionQuality cur : values()) {
/* 25 */       if (cur.toString().equalsIgnoreCase(val))
/* 26 */         return true; 
/* 27 */     }  return false;
/*    */   }
/*    */   
/*    */   public static ConnectionQuality get(String val) {
/* 31 */     for (ConnectionQuality cur : values()) {
/* 32 */       if (cur.toString().equalsIgnoreCase(val))
/* 33 */         return cur; 
/* 34 */     }  return null;
/*    */   }
/*    */   
/*    */   public int[] getConfiguration() {
/* 38 */     return this.configuration;
/*    */   }
/*    */   
/*    */   public int getMinTries() {
/* 42 */     return this.minTries;
/*    */   }
/*    */   
/*    */   public int getMaxTries() {
/* 46 */     return this.maxTries;
/*    */   }
/*    */   
/*    */   public int getMaxThreads() {
/* 50 */     return this.maxThreads;
/*    */   }
/*    */   
/*    */   public int getTries(boolean fast) {
/* 54 */     return fast ? this.minTries : this.maxTries;
/*    */   }
/*    */   
/*    */   public int getTimeout() {
/* 58 */     return this.timeout;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 63 */     return super.toString().toLowerCase();
/*    */   }
/*    */   
/*    */   public static ConnectionQuality getDefault() {
/* 67 */     return GOOD;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\configuration\enums\ConnectionQuality.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */