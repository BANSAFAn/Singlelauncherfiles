/*    */ package org.tlauncher.tlauncher.ui.text;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InvalidateTextField
/*    */   extends CheckableTextField
/*    */ {
/*    */   private static final long serialVersionUID = -4076362911409776688L;
/*    */   
/*    */   protected InvalidateTextField(CenterPanel panel, String placeholder, String value) {
/* 17 */     super(panel, placeholder, value);
/*    */   }
/*    */   
/*    */   public InvalidateTextField(CenterPanel panel) {
/* 21 */     this(panel, null, null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String check(String text) {
/* 26 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\text\InvalidateTextField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */