/*    */ package org.tlauncher.tlauncher.configuration.enums;
/*    */ 
/*    */ public enum ActionOnLaunch {
/*  4 */   HIDE, EXIT, NOTHING;
/*    */   
/*    */   public static boolean parse(String val) {
/*  7 */     if (val == null)
/*  8 */       return false; 
/*  9 */     for (ActionOnLaunch cur : values()) {
/* 10 */       if (cur.toString().equalsIgnoreCase(val))
/* 11 */         return true; 
/* 12 */     }  return false;
/*    */   }
/*    */   
/*    */   public static ActionOnLaunch get(String val) {
/* 16 */     for (ActionOnLaunch cur : values()) {
/* 17 */       if (cur.toString().equalsIgnoreCase(val))
/* 18 */         return cur; 
/* 19 */     }  return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 24 */     return super.toString().toLowerCase();
/*    */   }
/*    */   
/*    */   public static ActionOnLaunch getDefault() {
/* 28 */     return HIDE;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\configuration\enums\ActionOnLaunch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */