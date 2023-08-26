/*     */ package org.tlauncher.tlauncher.ui.ui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.Objects;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ScrollBarUI;
/*     */ import javax.swing.plaf.basic.BasicComboBoxUI;
/*     */ import javax.swing.plaf.basic.BasicComboPopup;
/*     */ import javax.swing.plaf.basic.ComboPopup;
/*     */ import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.swing.scroll.VersionScrollBarUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TlauncherBasicComboBoxUI
/*     */   extends BasicComboBoxUI
/*     */ {
/*     */   public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
/*     */     Component c;
/*  35 */     ListCellRenderer<Object> renderer = this.comboBox.getRenderer();
/*     */ 
/*     */     
/*  38 */     if (hasFocus && !isPopupVisible(this.comboBox)) {
/*  39 */       c = renderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, true, false);
/*     */     } else {
/*  41 */       c = renderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, false, false);
/*  42 */       if (Objects.isNull(c))
/*     */         return; 
/*  44 */       c.setBackground(UIManager.getColor("ComboBox.background"));
/*     */     } 
/*  46 */     c.setFont(this.comboBox.getFont());
/*  47 */     if (hasFocus && !isPopupVisible(this.comboBox)) {
/*  48 */       c.setForeground(Color.BLACK);
/*  49 */       c.setBackground(Color.WHITE);
/*     */     }
/*  51 */     else if (this.comboBox.isEnabled()) {
/*  52 */       c.setForeground(this.comboBox.getForeground());
/*  53 */       c.setBackground(this.comboBox.getBackground());
/*     */     } else {
/*  55 */       c.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
/*  56 */       c.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  61 */     boolean shouldValidate = false;
/*  62 */     if (c instanceof javax.swing.JPanel) {
/*  63 */       shouldValidate = true;
/*     */     }
/*     */     
/*  66 */     int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
/*  67 */     if (this.padding != null) {
/*  68 */       x = bounds.x + this.padding.left;
/*  69 */       y = bounds.y + this.padding.top;
/*  70 */       w = bounds.width - this.padding.left + this.padding.right;
/*  71 */       h = bounds.height - this.padding.top + this.padding.bottom;
/*     */     } 
/*  73 */     this.currentValuePane.paintComponent(g, c, this.comboBox, x, y, w, h, shouldValidate);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected JButton createArrowButton() {
/*  79 */     ImageUdaterButton imageUdaterButton = new ImageUdaterButton(Color.white, "black-arrow.png");
/*  80 */     for (ActionListener l : imageUdaterButton.getActionListeners()) {
/*  81 */       imageUdaterButton.removeActionListener(l);
/*     */     }
/*  83 */     return (JButton)imageUdaterButton;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ComboPopup createPopup() {
/*  89 */     BasicComboPopup basic = new BasicComboPopup(this.comboBox)
/*     */       {
/*     */         private static final long serialVersionUID = -5424584744514133918L;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         protected JScrollPane createScroller() {
/*  97 */           VersionScrollBarUI barUI = new VersionScrollBarUI()
/*     */             {
/*     */               protected Dimension getMinimumThumbSize() {
/* 100 */                 return new Dimension(10, 40);
/*     */               }
/*     */ 
/*     */               
/*     */               public Dimension getMaximumSize(JComponent c) {
/* 105 */                 Dimension dim = super.getMaximumSize(c);
/* 106 */                 dim.setSize(10.0D, dim.getHeight());
/* 107 */                 return dim;
/*     */               }
/*     */ 
/*     */               
/*     */               public Dimension getPreferredSize(JComponent c) {
/* 112 */                 Dimension dim = super.getPreferredSize(c);
/* 113 */                 dim.setSize(13.0D, dim.getHeight());
/* 114 */                 return dim;
/*     */               }
/*     */             };
/*     */           
/* 118 */           barUI.setGapThubm(5);
/*     */           
/* 120 */           JScrollPane scroller = new JScrollPane(this.list, 20, 31);
/*     */           
/* 122 */           scroller.getVerticalScrollBar().setUI((ScrollBarUI)barUI);
/* 123 */           return scroller;
/*     */         }
/*     */       };
/* 126 */     basic.setBorder(BorderFactory.createEmptyBorder());
/* 127 */     return basic;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\u\\ui\TlauncherBasicComboBoxUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */