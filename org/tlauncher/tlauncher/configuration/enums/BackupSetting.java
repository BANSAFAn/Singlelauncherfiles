/*    */ package org.tlauncher.tlauncher.configuration.enums;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum BackupSetting
/*    */ {
/*  7 */   LAST_MODIFIED_TIME("lastModifiedTime"),
/*    */   
/*  9 */   MAX_TIME_FOR_BACKUP("max.time.for.backups"),
/*    */ 
/*    */ 
/*    */   
/* 13 */   FREE_PARTITION_SIZE("free.partition.size"),
/* 14 */   SKIP_USER_BACKUP("skip.user.backup"),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   MAX_SIZE_FOR_WORLD("max.size.for.world"),
/*    */ 
/*    */ 
/*    */   
/* 23 */   REPEAT_BACKUP("repeat.backup.hours");
/*    */   
/*    */   BackupSetting(String value) {
/* 26 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   private String value;
/*    */   
/*    */   public String toString() {
/* 33 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\configuration\enums\BackupSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */