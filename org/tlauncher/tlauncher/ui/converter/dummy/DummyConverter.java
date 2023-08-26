/*    */ package org.tlauncher.tlauncher.ui.converter.dummy;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.converter.StringConverter;
/*    */ 
/*    */ public abstract class DummyConverter<T>
/*    */   implements StringConverter<T> {
/*    */   private static DummyConverter<Object>[] converters;
/*    */   
/*    */   public static DummyConverter<Object>[] getConverters() {
/* 10 */     if (converters == null) {
/* 11 */       converters = (DummyConverter<Object>[])new DummyConverter[] { new DummyStringConverter(), new DummyIntegerConverter(), new DummyDoubleConverter(), new DummyLongConverter(), new DummyDateConverter() };
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 18 */     return converters;
/*    */   }
/*    */ 
/*    */   
/*    */   public T fromString(String from) {
/* 23 */     return fromDummyString(from);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(T from) {
/* 28 */     return toValue(from);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toValue(T from) {
/* 33 */     return toDummyValue(from);
/*    */   }
/*    */   
/*    */   public abstract T fromDummyString(String paramString) throws RuntimeException;
/*    */   
/*    */   public abstract String toDummyValue(T paramT) throws RuntimeException;
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\converter\dummy\DummyConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */