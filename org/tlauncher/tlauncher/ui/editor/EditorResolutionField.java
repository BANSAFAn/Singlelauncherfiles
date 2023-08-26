/*     */ package org.tlauncher.tlauncher.ui.editor;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.BoxLayout;
/*     */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedLabel;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.util.IntegerArray;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EditorResolutionField
/*     */   extends ExtendedPanel
/*     */   implements EditorField
/*     */ {
/*     */   private static final long serialVersionUID = -5565607141889620750L;
/*     */   private EditorIntegerField w;
/*     */   private EditorIntegerField h;
/*     */   private ExtendedLabel x;
/*     */   private final int[] defaults;
/*     */   
/*     */   public EditorResolutionField(String promptW, String promptH, int[] defaults, boolean showDefault) {
/*  28 */     if (defaults == null)
/*  29 */       throw new NullPointerException(); 
/*  30 */     if (defaults.length != 2) {
/*  31 */       throw new IllegalArgumentException("Illegal array size");
/*     */     }
/*  33 */     this.defaults = defaults;
/*  34 */     setLayout(new BoxLayout((Container)this, 0));
/*  35 */     setPreferredSize(new Dimension(161, 21));
/*  36 */     this.w = new EditorIntegerField(promptW);
/*  37 */     this.w.setColumns(4);
/*  38 */     this.w.setHorizontalAlignment(0);
/*  39 */     this.w.setPreferredSize(new Dimension(70, 21));
/*     */     
/*  41 */     this.h = new EditorIntegerField(promptH);
/*  42 */     this.h.setColumns(4);
/*  43 */     this.h.setHorizontalAlignment(0);
/*  44 */     this.h.setPreferredSize(new Dimension(70, 21));
/*     */     
/*  46 */     this.x = new ExtendedLabel("X", 0);
/*  47 */     this.x.setFont(this.x.getFont().deriveFont(1));
/*  48 */     add((Component)this.w);
/*  49 */     add(Box.createHorizontalStrut(6));
/*  50 */     add((Component)this.x);
/*  51 */     add(Box.createHorizontalStrut(6));
/*  52 */     add((Component)this.h);
/*     */   }
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
/*     */   public String getSettingsValue() {
/*  65 */     return this.w.getSettingsValue() + ';' + this.h.getSettingsValue();
/*     */   }
/*     */   
/*     */   int[] getResolution() {
/*     */     try {
/*  70 */       IntegerArray arr = IntegerArray.parseIntegerArray(getSettingsValue());
/*  71 */       return arr.toArray();
/*  72 */     } catch (Exception e) {
/*  73 */       return new int[2];
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValueValid() {
/*  79 */     int[] size = getResolution();
/*     */     
/*  81 */     return (size[0] >= 1 && size[1] >= 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSettingsValue(String value) {
/*     */     String width, height;
/*     */     try {
/*  89 */       IntegerArray arr = IntegerArray.parseIntegerArray(value);
/*  90 */       width = String.valueOf(arr.get(0));
/*  91 */       height = String.valueOf(arr.get(1));
/*  92 */     } catch (Exception e) {
/*  93 */       width = "";
/*  94 */       height = "";
/*     */     } 
/*     */     
/*  97 */     this.w.setText(width);
/*  98 */     this.h.setText(height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBackground(Color bg) {
/* 103 */     if (this.w != null)
/* 104 */       this.w.setBackground(bg); 
/* 105 */     if (this.h != null) {
/* 106 */       this.h.setBackground(bg);
/*     */     }
/*     */   }
/*     */   
/*     */   public void block(Object reason) {
/* 111 */     Blocker.blockComponents(reason, new Component[] { (Component)this.w, (Component)this.h });
/*     */   }
/*     */ 
/*     */   
/*     */   public void unblock(Object reason) {
/* 116 */     Blocker.unblockComponents(Blocker.UNIVERSAL_UNBLOCK, new Component[] { (Component)this.w, (Component)this.h });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\editor\EditorResolutionField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */