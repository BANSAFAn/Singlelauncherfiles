/*    */ package org.tlauncher.tlauncher.ui.converter;
/*    */ 
/*    */ import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter;
/*    */ 
/*    */ public class ConnectionQualityConverter
/*    */   extends LocalizableStringConverter<ConnectionQuality>
/*    */ {
/*    */   public ConnectionQualityConverter() {
/* 10 */     super("settings.connection");
/*    */   }
/*    */ 
/*    */   
/*    */   public ConnectionQuality fromString(String from) {
/* 15 */     return ConnectionQuality.get(from);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toValue(ConnectionQuality from) {
/* 20 */     return from.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toPath(ConnectionQuality from) {
/* 25 */     return from.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<ConnectionQuality> getObjectClass() {
/* 30 */     return ConnectionQuality.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\converter\ConnectionQualityConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */