/*    */ package org.tlauncher.tlauncher.ui.swing.extended;
/*    */ 
/*    */ import javax.swing.Icon;
/*    */ 
/*    */ public class HTMLLabel extends ExtendedLabel {
/*    */   private static final long serialVersionUID = -509864367525835474L;
/*    */   
/*    */   public HTMLLabel(String text, Icon icon, int horizontalAlignment) {
/*  9 */     super(text, icon, horizontalAlignment);
/*    */   }
/*    */   
/*    */   public HTMLLabel(String text, int horizontalAlignment) {
/* 13 */     super(text, horizontalAlignment);
/*    */   }
/*    */   
/*    */   public HTMLLabel(String text) {
/* 17 */     super(text);
/*    */   }
/*    */   
/*    */   public HTMLLabel(Icon image, int horizontalAlignment) {
/* 21 */     super(image, horizontalAlignment);
/*    */   }
/*    */   
/*    */   public HTMLLabel(Icon image) {
/* 25 */     super(image);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public HTMLLabel() {}
/*    */ 
/*    */   
/*    */   public void setText(String text) {
/* 34 */     super.setText("<html>" + (
/*    */         
/* 36 */         (text == null) ? null : text
/* 37 */         .replace("\n", "<br/>")) + "</html>");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\extended\HTMLLabel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */