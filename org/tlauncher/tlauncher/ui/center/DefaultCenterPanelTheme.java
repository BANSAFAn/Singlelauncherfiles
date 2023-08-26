/*    */ package org.tlauncher.tlauncher.ui.center;
/*    */ 
/*    */ import java.awt.Color;
/*    */ 
/*    */ public class DefaultCenterPanelTheme extends CenterPanelTheme {
/*  6 */   protected final Color backgroundColor = new Color(255, 255, 255, 255);
/*    */   
/*  8 */   protected final Color panelBackgroundColor = new Color(255, 255, 255, 128);
/*    */   
/* 10 */   protected final Color focusColor = new Color(0, 0, 0, 255);
/* 11 */   protected final Color focusLostColor = new Color(128, 128, 128, 255);
/*    */   
/* 13 */   protected final Color successColor = new Color(78, 196, 78, 255);
/* 14 */   protected final Color failureColor = Color.getHSBColor(0.0F, 0.3F, 1.0F);
/*    */   
/* 16 */   protected final Color borderColor = new Color(28, 128, 28, 255);
/* 17 */   protected final Color delPanelColor = this.successColor;
/*    */ 
/*    */   
/*    */   public Color getBackground() {
/* 21 */     return this.backgroundColor;
/*    */   }
/*    */ 
/*    */   
/*    */   public Color getPanelBackground() {
/* 26 */     return this.panelBackgroundColor;
/*    */   }
/*    */ 
/*    */   
/*    */   public Color getFocus() {
/* 31 */     return this.focusColor;
/*    */   }
/*    */ 
/*    */   
/*    */   public Color getFocusLost() {
/* 36 */     return this.focusLostColor;
/*    */   }
/*    */ 
/*    */   
/*    */   public Color getSuccess() {
/* 41 */     return this.successColor;
/*    */   }
/*    */ 
/*    */   
/*    */   public Color getFailure() {
/* 46 */     return this.failureColor;
/*    */   }
/*    */ 
/*    */   
/*    */   public Color getBorder() {
/* 51 */     return this.borderColor;
/*    */   }
/*    */ 
/*    */   
/*    */   public Color getDelPanel() {
/* 56 */     return this.delPanelColor;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\center\DefaultCenterPanelTheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */