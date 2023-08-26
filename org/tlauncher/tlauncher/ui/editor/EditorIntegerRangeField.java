/*    */ package org.tlauncher.tlauncher.ui.editor;
/*    */ 
/*    */ import org.tlauncher.util.Range;
/*    */ 
/*    */ public class EditorIntegerRangeField
/*    */   extends EditorIntegerField {
/*    */   private final Range<Integer> range;
/*    */   
/*    */   public EditorIntegerRangeField(String placeholder, Range<Integer> range) {
/* 10 */     if (range == null) {
/* 11 */       throw new NullPointerException("range");
/*    */     }
/* 13 */     this.range = range;
/* 14 */     setPlaceholder(placeholder);
/*    */   }
/*    */   
/*    */   public EditorIntegerRangeField(Range<Integer> range) {
/* 18 */     this((String)null, range);
/* 19 */     setPlaceholder("settings.range", new Object[] { range.getMinValue(), range.getMaxValue() });
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValueValid() {
/*    */     try {
/* 25 */       return this.range.fits(Integer.valueOf(Integer.parseInt(getSettingsValue())));
/* 26 */     } catch (Exception e) {
/* 27 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\editor\EditorIntegerRangeField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */