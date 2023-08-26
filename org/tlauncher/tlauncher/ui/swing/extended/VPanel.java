/*    */ package org.tlauncher.tlauncher.ui.swing.extended;
/*    */ 
/*    */ import java.awt.LayoutManager;
/*    */ import javax.swing.BoxLayout;
/*    */ import org.tlauncher.util.Reflect;
/*    */ 
/*    */ 
/*    */ public class VPanel
/*    */   extends ExtendedPanel
/*    */ {
/*    */   private VPanel(boolean isDoubleBuffered) {
/* 12 */     super(isDoubleBuffered);
/* 13 */     setLayout(new BoxLayout(this, 3));
/*    */   }
/*    */   
/*    */   public VPanel() {
/* 17 */     this(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public BoxLayout getLayout() {
/* 22 */     return (BoxLayout)super.getLayout();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLayout(LayoutManager mgr) {
/* 27 */     if (!(mgr instanceof BoxLayout)) {
/*    */       return;
/*    */     }
/* 30 */     int axis = ((BoxLayout)Reflect.cast(mgr, BoxLayout.class)).getAxis();
/*    */     
/* 32 */     if (axis == 3 || axis == 1) {
/* 33 */       super.setLayout(mgr);
/*    */     } else {
/* 35 */       throw new IllegalArgumentException("Illegal BoxLayout axis!");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\extended\VPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */