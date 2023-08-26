/*    */ package org.tlauncher.tlauncher.ui.swing.extended;
/*    */ 
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Component;
/*    */ import java.awt.LayoutManager;
/*    */ 
/*    */ public class BorderPanel extends ExtendedPanel {
/*    */   private static final long serialVersionUID = -7641580330557833990L;
/*    */   
/*    */   private BorderPanel(BorderLayout layout, boolean isDoubleBuffered) {
/* 11 */     super(isDoubleBuffered);
/*    */     
/* 13 */     if (layout == null) {
/* 14 */       layout = new BorderLayout();
/*    */     }
/* 16 */     setLayout(layout);
/*    */   }
/*    */   
/*    */   public BorderPanel() {
/* 20 */     this((BorderLayout)null, true);
/*    */   }
/*    */   
/*    */   public BorderPanel(int hgap, int vgap) {
/* 24 */     this();
/*    */     
/* 26 */     setHgap(hgap);
/* 27 */     setVgap(vgap);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BorderLayout getLayout() {
/* 33 */     return (BorderLayout)super.getLayout();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLayout(LayoutManager mgr) {
/* 38 */     if (mgr instanceof BorderLayout)
/* 39 */       super.setLayout(mgr); 
/*    */   }
/*    */   
/*    */   public int getHgap() {
/* 43 */     return getLayout().getHgap();
/*    */   }
/*    */   
/*    */   public void setHgap(int hgap) {
/* 47 */     getLayout().setHgap(hgap);
/*    */   }
/*    */   
/*    */   public int getVgap() {
/* 51 */     return getLayout().getVgap();
/*    */   }
/*    */   
/*    */   public void setVgap(int vgap) {
/* 55 */     getLayout().setVgap(vgap);
/*    */   }
/*    */   
/*    */   public void setNorth(Component comp) {
/* 59 */     add(comp, "North");
/*    */   }
/*    */   
/*    */   public void setEast(Component comp) {
/* 63 */     add(comp, "East");
/*    */   }
/*    */   
/*    */   public void setSouth(Component comp) {
/* 67 */     add(comp, "South");
/*    */   }
/*    */   
/*    */   public void setWest(Component comp) {
/* 71 */     add(comp, "West");
/*    */   }
/*    */   
/*    */   public void setCenter(Component comp) {
/* 75 */     add(comp, "Center");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\extended\BorderPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */