/*    */ package org.tlauncher.tlauncher.ui.swing.extended;
/*    */ 
/*    */ import javax.swing.JTabbedPane;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import org.tlauncher.tlauncher.ui.swing.util.Orientation;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TabbedPane
/*    */   extends JTabbedPane
/*    */ {
/*    */   public TabbedPane(Orientation tabLocation, TabLayout layout) {
/* 13 */     setTabLocation((tabLocation == null) ? Orientation.TOP : tabLocation);
/* 14 */     setTabLayout((layout == null) ? TabLayout.SCROLL : layout);
/*    */   }
/*    */   
/*    */   public TabbedPane() {
/* 18 */     this((Orientation)null, (TabLayout)null);
/*    */   }
/*    */   
/*    */   public ExtendedUI getExtendedUI() {
/* 22 */     ComponentUI ui = getUI();
/*    */     
/* 24 */     if (ui instanceof ExtendedUI) {
/* 25 */       return (ExtendedUI)ui;
/*    */     }
/* 27 */     return null;
/*    */   }
/*    */   
/*    */   public void setTabLocation(Orientation direction) {
/* 31 */     if (direction == null) {
/* 32 */       throw new NullPointerException();
/*    */     }
/* 34 */     setTabPlacement(direction.getSwingAlias());
/*    */   }
/*    */   
/*    */   public TabLayout getTabLayout() {
/* 38 */     return TabLayout.fromSwingConstant(getTabLayoutPolicy());
/*    */   }
/*    */   
/*    */   public void setTabLayout(TabLayout layout) {
/* 42 */     if (layout == null) {
/* 43 */       throw new NullPointerException();
/*    */     }
/* 45 */     setTabLayoutPolicy(layout.getSwingAlias());
/*    */   }
/*    */   
/*    */   public enum TabLayout {
/* 49 */     WRAP(0), SCROLL(1);
/*    */     
/*    */     private final int swingAlias;
/*    */     
/*    */     TabLayout(int swingAlias) {
/* 54 */       this.swingAlias = swingAlias;
/*    */     }
/*    */     
/*    */     public int getSwingAlias() {
/* 58 */       return this.swingAlias;
/*    */     }
/*    */     
/*    */     public static TabLayout fromSwingConstant(int orientation) {
/* 62 */       for (TabLayout current : values()) {
/* 63 */         if (orientation == current.getSwingAlias())
/* 64 */           return current; 
/* 65 */       }  return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\extended\TabbedPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */