/*     */ package org.tlauncher.tlauncher.ui.editor;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.plaf.ComboBoxUI;
/*     */ import javax.swing.plaf.ScrollBarUI;
/*     */ import javax.swing.plaf.basic.BasicComboPopup;
/*     */ import javax.swing.plaf.basic.ComboPopup;
/*     */ import org.tlauncher.tlauncher.ui.converter.StringConverter;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedComboBox;
/*     */ import org.tlauncher.tlauncher.ui.swing.scroll.VersionScrollBarUI;
/*     */ import org.tlauncher.tlauncher.ui.ui.TlauncherBasicComboBoxUI;
/*     */ 
/*     */ public class EditorComboBox<T> extends ExtendedComboBox<T> implements EditorField {
/*     */   private static final long serialVersionUID = -2320340434786516374L;
/*     */   
/*     */   public EditorComboBox(StringConverter<T> converter, T[] values, boolean allowNull) {
/*  22 */     super(converter);
/*     */     
/*  24 */     this.allowNull = allowNull;
/*     */     
/*  26 */     if (values == null) {
/*     */       return;
/*     */     }
/*  29 */     for (T value : values)
/*  30 */       addItem(value); 
/*     */   }
/*     */   private final boolean allowNull;
/*     */   public EditorComboBox(StringConverter<T> converter, T[] values) {
/*  34 */     this(converter, values, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSettingsValue() {
/*  39 */     T value = (T)getSelectedValue();
/*  40 */     return convert(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSettingsValue(String string) {
/*  45 */     T value = (T)convert(string);
/*     */     
/*  47 */     if (!this.allowNull && string == null) {
/*  48 */       boolean hasNull = false;
/*     */       
/*  50 */       for (int i = 0; i < getItemCount(); i++) {
/*  51 */         if (getItemAt(i) == null)
/*  52 */           hasNull = true; 
/*     */       } 
/*  54 */       if (!hasNull) {
/*     */         return;
/*     */       }
/*     */     } 
/*  58 */     setSelectedValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValueValid() {
/*  63 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void block(Object reason) {
/*  68 */     setEnabled(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void unblock(Object reason) {
/*  73 */     setEnabled(true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  78 */     TlauncherBasicComboBoxUI list = new TlauncherBasicComboBoxUI()
/*     */       {
/*     */         protected ComboPopup createPopup()
/*     */         {
/*  82 */           BasicComboPopup basic = new BasicComboPopup(this.comboBox)
/*     */             {
/*     */               protected JScrollPane createScroller() {
/*  85 */                 VersionScrollBarUI barUI = new VersionScrollBarUI()
/*     */                   {
/*     */                     protected Dimension getMinimumThumbSize() {
/*  88 */                       return new Dimension(10, 40);
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public Dimension getMaximumSize(JComponent c) {
/*  93 */                       Dimension dim = super.getMaximumSize(c);
/*  94 */                       dim.setSize(10.0D, dim.getHeight());
/*  95 */                       return dim;
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public Dimension getPreferredSize(JComponent c) {
/* 100 */                       Dimension dim = super.getPreferredSize(c);
/* 101 */                       dim.setSize(10.0D, dim.getHeight());
/* 102 */                       return dim;
/*     */                     }
/*     */                   };
/*     */                 
/* 106 */                 barUI.setGapThubm(5);
/*     */                 
/* 108 */                 JScrollPane scroller = new JScrollPane(this.list, 20, 31);
/*     */                 
/* 110 */                 scroller.getVerticalScrollBar().setUI((ScrollBarUI)barUI);
/* 111 */                 return scroller;
/*     */               }
/*     */             };
/* 114 */           basic.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK));
/* 115 */           return basic;
/*     */         }
/*     */       };
/* 118 */     setUI((ComboBoxUI)list);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\editor\EditorComboBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */