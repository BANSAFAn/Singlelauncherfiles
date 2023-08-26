/*    */ package org.tlauncher.tlauncher.ui.scenes;
/*    */ 
/*    */ import javax.swing.JComponent;
/*    */ import org.tlauncher.tlauncher.ui.MainPane;
/*    */ import org.tlauncher.tlauncher.ui.swing.AnimatedVisibility;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane;
/*    */ 
/*    */ public abstract class PseudoScene
/*    */   extends ExtendedLayeredPane implements AnimatedVisibility {
/*    */   private static final long serialVersionUID = -1L;
/*    */   private final MainPane main;
/*    */   private boolean shown = true;
/*    */   
/*    */   PseudoScene(MainPane main) {
/* 15 */     super((JComponent)main);
/*    */     
/* 17 */     this.main = main;
/* 18 */     setSize(main.getWidth(), main.getHeight());
/*    */   }
/*    */   
/*    */   public MainPane getMainPane() {
/* 22 */     return this.main;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setShown(boolean shown) {
/* 27 */     setShown(shown, true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setShown(boolean shown, boolean animate) {
/* 32 */     if (this.shown == shown) {
/*    */       return;
/*    */     }
/* 35 */     this.shown = shown;
/* 36 */     setVisible(shown);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\scenes\PseudoScene.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */