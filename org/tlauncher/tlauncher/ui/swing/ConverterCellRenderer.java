/*    */ package org.tlauncher.tlauncher.ui.swing;
/*    */ 
/*    */ import javax.swing.ListCellRenderer;
/*    */ import org.tlauncher.tlauncher.ui.converter.StringConverter;
/*    */ 
/*    */ public abstract class ConverterCellRenderer<T>
/*    */   implements ListCellRenderer<T> {
/*    */   protected final StringConverter<T> converter;
/*    */   
/*    */   ConverterCellRenderer(StringConverter<T> converter) {
/* 11 */     if (converter == null) {
/* 12 */       throw new NullPointerException();
/*    */     }
/* 14 */     this.converter = converter;
/*    */   }
/*    */   
/*    */   public StringConverter<T> getConverter() {
/* 18 */     return this.converter;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\ConverterCellRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */