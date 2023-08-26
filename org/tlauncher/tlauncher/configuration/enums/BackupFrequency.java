/*    */ package org.tlauncher.tlauncher.configuration.enums;
/*    */ 
/*    */ public enum BackupFrequency
/*    */ {
/*  5 */   EVERYTIME("settings.backup.frequency.always"), OFTEN("settings.backup.frequency.once");
/*    */   
/*    */   private final String value;
/*    */   
/*    */   BackupFrequency(String value) {
/* 10 */     this.value = value;
/*    */   }
/*    */   
/*    */   public static BackupFrequency get(String val) {
/* 14 */     if (val.equals("0") || val.equals(EVERYTIME.toString()))
/* 15 */       return EVERYTIME; 
/* 16 */     if (val.equals("1") || val.equals(OFTEN.toString()))
/* 17 */       return OFTEN; 
/* 18 */     return null;
/*    */   }
/*    */   
/*    */   public static String convert(BackupFrequency from) {
/* 22 */     if (from.equals(EVERYTIME))
/* 23 */       return "0"; 
/* 24 */     if (from.equals(OFTEN))
/* 25 */       return "1"; 
/* 26 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 31 */     return this.value;
/*    */   }
/*    */   
/*    */   public static BackupFrequency getDefault() {
/* 35 */     return OFTEN;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\configuration\enums\BackupFrequency.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */