/*    */ package org.tlauncher.tlauncher.ui.loc;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.converter.StringConverter;
/*    */ 
/*    */ public abstract class LocalizableStringConverter<T> implements StringConverter<T> {
/*    */   private final String prefix;
/*    */   
/*    */   public LocalizableStringConverter(String prefix) {
/*  9 */     this.prefix = prefix;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(T from) {
/* 14 */     return Localizable.get(getPath(from));
/*    */   }
/*    */   
/*    */   String getPath(T from) {
/* 18 */     String prefix = getPrefix();
/*    */     
/* 20 */     if (prefix == null || prefix.isEmpty()) {
/* 21 */       return toPath(from);
/*    */     }
/* 23 */     String path = toPath(from);
/* 24 */     return prefix + "." + path;
/*    */   }
/*    */   
/*    */   String getPrefix() {
/* 28 */     return this.prefix;
/*    */   }
/*    */   
/*    */   protected abstract String toPath(T paramT);
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\LocalizableStringConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */