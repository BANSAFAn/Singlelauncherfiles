/*    */ package org.tlauncher.tlauncher.ui.converter;
/*    */ 
/*    */ import org.tlauncher.tlauncher.configuration.enums.BackupFrequency;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter;
/*    */ 
/*    */ public class BackupFrequencyConverter
/*    */   extends LocalizableStringConverter<BackupFrequency>
/*    */ {
/*    */   public BackupFrequencyConverter() {
/* 10 */     super("");
/*    */   }
/*    */ 
/*    */   
/*    */   public BackupFrequency fromString(String from) {
/* 15 */     return BackupFrequency.get(from);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toValue(BackupFrequency from) {
/* 20 */     if (from == null)
/* 21 */       return null; 
/* 22 */     return BackupFrequency.convert(from);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toPath(BackupFrequency from) {
/* 27 */     if (from == null)
/* 28 */       return null; 
/* 29 */     return from.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<BackupFrequency> getObjectClass() {
/* 34 */     return BackupFrequency.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\converter\BackupFrequencyConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */