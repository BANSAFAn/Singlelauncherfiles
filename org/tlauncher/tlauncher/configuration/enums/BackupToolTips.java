/*    */ package org.tlauncher.tlauncher.configuration.enums;
/*    */ 
/*    */ public enum BackupToolTips {
/*  4 */   TITLE("settings.backup.title.tooltip"),
/*    */   
/*  6 */   DO_BACKUPS("settings.doBackup.tooltip"),
/*    */   
/*  8 */   MAX_BACKUP_SIZE("settings.max.backup.size.tooltip"),
/*    */   
/* 10 */   MAX_BACKUP_SAVE_TIME("settings.max.backup.time.tooltip");
/*    */   
/*    */   private String value;
/*    */   
/*    */   BackupToolTips(String value) {
/* 15 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 20 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\configuration\enums\BackupToolTips.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */