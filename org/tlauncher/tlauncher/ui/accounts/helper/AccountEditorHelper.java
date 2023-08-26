/*     */ package org.tlauncher.tlauncher.ui.accounts.helper;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import javax.swing.JComponent;
/*     */ import org.tlauncher.tlauncher.ui.MainPane;
/*     */ import org.tlauncher.tlauncher.ui.accounts.AccountEditor;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.scenes.AccountEditorScene;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AccountEditorHelper
/*     */   extends ExtendedLayeredPane
/*     */ {
/*     */   static final int MARGIN = 5;
/*     */   static final byte LEFT = 0;
/*     */   static final byte UP = 1;
/*     */   static final byte RIGHT = 2;
/*     */   static final byte DOWN = 3;
/*     */   private final MainPane pane;
/*     */   private final HelperTip[] tips;
/*     */   
/*     */   public AccountEditorHelper(AccountEditorScene scene) {
/*  29 */     super((JComponent)scene);
/*     */     
/*  31 */     this.pane = scene.getMainPane();
/*  32 */     AccountEditor editor = scene.editor;
/*  33 */     this.tips = new HelperTip[] { new HelperTip("account", (Component)editor, (Component)editor, (byte)1, new HelperState[] { HelperState.PREMIUM, HelperState.FREE }), new HelperTip("username", (Component)editor.username, (Component)editor, (byte)0, new HelperState[] { HelperState.PREMIUM, HelperState.FREE }), new HelperTip("password", (Component)editor.password, (Component)editor, (byte)0, new HelperState[] { HelperState.PREMIUM }), new HelperTip("button", (Component)editor.save, (Component)editor, (byte)0, new HelperState[] { HelperState.PREMIUM, HelperState.FREE }) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  51 */     add((Component[])this.tips);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setState(HelperState state) {
/*  57 */     if (state == null) {
/*  58 */       throw new NullPointerException();
/*     */     }
/*     */ 
/*     */     
/*  62 */     for (HelperState st : HelperState.values()) {
/*  63 */       st.item.setEnabled(!st.equals(state));
/*     */     }
/*  65 */     if (state == HelperState.NONE) {
/*  66 */       for (HelperTip step : this.tips) {
/*  67 */         step.setVisible(false);
/*     */       }
/*     */       return;
/*     */     } 
/*  71 */     for (HelperTip step : this.tips) {
/*     */       int x, y;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  77 */       LocalizableLabel l = step.label;
/*  78 */       l.setText("auth.helper." + state.toString() + "." + step.name);
/*     */       
/*  80 */       Component c = step.component;
/*  81 */       int cWidth = c.getWidth(), cHeight = c.getHeight();
/*  82 */       Point cp = this.pane.getLocationOf(c);
/*     */       
/*  84 */       Component p = step.parent;
/*  85 */       int pWidth = p.getWidth(), pHeight = p.getHeight();
/*  86 */       Point pp = this.pane.getLocationOf(p);
/*     */       
/*  88 */       FontMetrics fm = l.getFontMetrics(l.getFont());
/*  89 */       Insets i = step.getInsets();
/*     */       
/*  91 */       int height = i.top + i.bottom + fm.getHeight();
/*  92 */       int width = i.left + i.right + fm.stringWidth(l.getText());
/*     */ 
/*     */ 
/*     */       
/*  96 */       switch (step.alignment) {
/*     */         case 0:
/*  98 */           x = pp.x - 5 - width;
/*  99 */           y = cp.y + cHeight / 2 - height / 2;
/*     */           break;
/*     */         case 1:
/* 102 */           x = cp.x + cWidth / 2 - width / 2;
/* 103 */           y = pp.y - 5 - height;
/*     */           break;
/*     */         case 2:
/* 106 */           x = pp.x + pWidth + 5;
/* 107 */           y = cp.y + cHeight / 2 - height / 2;
/*     */           break;
/*     */         case 3:
/* 110 */           x = cp.x + cWidth / 2 - width / 2;
/* 111 */           y = pp.y + pHeight + 5;
/*     */           break;
/*     */         default:
/* 114 */           throw new IllegalArgumentException("Unknown alignment");
/*     */       } 
/*     */       
/* 117 */       if (x < 0) {
/* 118 */         x = 0;
/* 119 */       } else if (x + width > getWidth()) {
/* 120 */         x = getWidth() - width;
/*     */       } 
/* 122 */       if (y < 0) {
/* 123 */         y = 0;
/* 124 */       } else if (y + height > getHeight()) {
/* 125 */         y = getHeight() - height;
/*     */       } 
/* 127 */       step.setVisible(true);
/* 128 */       step.setBounds(x, y, width, height);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onResize() {
/* 136 */     super.onResize();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\accounts\helper\AccountEditorHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */