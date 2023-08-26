/*    */ package org.tlauncher.tlauncher.ui.converter;
/*    */ 
/*    */ import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter;
/*    */ 
/*    */ public class ConsoleTypeConverter
/*    */   extends LocalizableStringConverter<ConsoleType>
/*    */ {
/*    */   public ConsoleTypeConverter() {
/* 10 */     super("settings.console");
/*    */   }
/*    */ 
/*    */   
/*    */   public ConsoleType fromString(String from) {
/* 15 */     return ConsoleType.get(from);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toValue(ConsoleType from) {
/* 20 */     if (from == null)
/* 21 */       return null; 
/* 22 */     return from.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toPath(ConsoleType from) {
/* 27 */     if (from == null)
/* 28 */       return null; 
/* 29 */     return from.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<ConsoleType> getObjectClass() {
/* 34 */     return ConsoleType.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\converter\ConsoleTypeConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */