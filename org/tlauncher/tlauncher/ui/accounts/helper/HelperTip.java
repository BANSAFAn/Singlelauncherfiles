/*    */ package org.tlauncher.tlauncher.ui.accounts.helper;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*    */ import org.tlauncher.tlauncher.ui.center.CenterPanelTheme;
/*    */ import org.tlauncher.tlauncher.ui.center.LoginHelperTheme;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ class HelperTip
/*    */   extends CenterPanel
/*    */ {
/*    */   public final String name;
/*    */   public final LocalizableLabel label;
/*    */   public final Component component;
/*    */   public final Component parent;
/*    */   public final byte alignment;
/*    */   public final HelperState[] states;
/* 19 */   private static final LoginHelperTheme HelperTipTheme = new LoginHelperTheme();
/*    */ 
/*    */   
/*    */   HelperTip(String name, Component component, Component parent, byte alignment, HelperState... states) {
/* 23 */     super((CenterPanelTheme)HelperTipTheme, smallSquareInsets);
/* 24 */     if (name == null) {
/* 25 */       throw new NullPointerException("Name is NULL");
/*    */     }
/* 27 */     if (name.isEmpty()) {
/* 28 */       throw new IllegalArgumentException("Name is empty");
/*    */     }
/* 30 */     if (component == null) {
/* 31 */       throw new NullPointerException("Component is NULL");
/*    */     }
/* 33 */     if (parent == null) {
/* 34 */       throw new NullPointerException("Parent is NULL");
/*    */     }
/* 36 */     if (alignment > 3) {
/* 37 */       throw new IllegalArgumentException("Unknown alignment");
/*    */     }
/* 39 */     if (states == null) {
/* 40 */       throw new NullPointerException("State array is NULL");
/*    */     }
/* 42 */     this.name = name;
/* 43 */     this.component = component;
/* 44 */     this.parent = parent;
/* 45 */     this.alignment = alignment;
/* 46 */     this.label = new LocalizableLabel();
/*    */     
/* 48 */     this.states = states;
/*    */     
/* 50 */     add((Component)this.label);
/* 51 */     setBackground(U.shiftAlpha(getTheme().getBackground(), 255));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\accounts\helper\HelperTip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */