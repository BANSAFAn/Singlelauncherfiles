/*    */ package org.tlauncher.tlauncher.ui.converter;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.tlauncher.tlauncher.configuration.Configuration;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ 
/*    */ public class LocaleConverter
/*    */   implements StringConverter<Locale>
/*    */ {
/*    */   public String toString(Locale from) {
/* 11 */     if (from == null) {
/* 12 */       return null;
/*    */     }
/*    */     
/* 15 */     if (from.equals(Locale.ENGLISH)) {
/* 16 */       TLauncher.getInstance(); return TLauncher.getInnerSettings().get("converter.value" + from.toString());
/* 17 */     }  TLauncher.getInstance(); return TLauncher.getInnerSettings().get("converter.value." + from.toString());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Locale fromString(String from) {
/* 24 */     return Configuration.getLocaleOf(from);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toValue(Locale from) {
/* 29 */     if (from == null)
/* 30 */       return null; 
/* 31 */     return from.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<Locale> getObjectClass() {
/* 36 */     return Locale.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\converter\LocaleConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */