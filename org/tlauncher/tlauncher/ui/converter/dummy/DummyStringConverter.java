/*    */ package org.tlauncher.tlauncher.ui.converter.dummy;
/*    */ 
/*    */ public class DummyStringConverter
/*    */   extends DummyConverter<String>
/*    */ {
/*    */   public String fromDummyString(String from) throws RuntimeException {
/*  7 */     return from;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toDummyValue(String value) throws RuntimeException {
/* 12 */     return value;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<String> getObjectClass() {
/* 17 */     return String.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\converter\dummy\DummyStringConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */