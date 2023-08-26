/*     */ package org.tlauncher.tlauncher.ui.ui;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.event.PopupMenuEvent;
/*     */ import javax.swing.event.PopupMenuListener;
/*     */ import javax.swing.plaf.basic.BasicComboPopup;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
/*     */ import org.tlauncher.util.ColorUtil;
/*     */ 
/*     */ public class ModpackComboBoxUI extends BasicComboBoxUI {
/*  22 */   protected int GUP_LEFT = 20;
/*     */   
/*     */   protected boolean centerText = false;
/*     */   
/*     */   protected JButton createArrowButton() {
/*  27 */     final ImageUdaterButton button = new ImageUdaterButton(ColorUtil.BACKGROUND_COMBO_BOX_POPUP, "white-arrow-down.png");
/*  28 */     for (ActionListener l : button.getActionListeners()) {
/*  29 */       button.removeActionListener(l);
/*     */     }
/*  31 */     button.setBackground(ColorUtil.BACKGROUND_COMBO_BOX_POPUP);
/*  32 */     this.comboBox.addPopupMenuListener(new PopupMenuListener()
/*     */         {
/*     */           
/*     */           public void popupMenuWillBecomeVisible(PopupMenuEvent e)
/*     */           {
/*  37 */             button.setImage(ImageCache.getBufferedImage("white-arrow-up.png"));
/*     */           }
/*     */ 
/*     */           
/*     */           public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
/*  42 */             button.setImage(ImageCache.getBufferedImage("white-arrow-down.png"));
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void popupMenuCanceled(PopupMenuEvent e) {}
/*     */         });
/*  52 */     return (JButton)button;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ComboPopup createPopup() {
/*  58 */     BasicComboPopup basic = new BasicComboPopup(this.comboBox)
/*     */       {
/*     */         private static final long serialVersionUID = -3111049881837377991L;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         protected JScrollPane createScroller() {
/*  66 */           ModpackScrollBarUI barUI = new ModpackScrollBarUI()
/*     */             {
/*     */               protected Dimension getMinimumThumbSize() {
/*  69 */                 return new Dimension(10, 40);
/*     */               }
/*     */ 
/*     */               
/*     */               public Dimension getMaximumSize(JComponent c) {
/*  74 */                 Dimension dim = super.getMaximumSize(c);
/*  75 */                 dim.setSize(10.0D, dim.getHeight());
/*  76 */                 return dim;
/*     */               }
/*     */ 
/*     */               
/*     */               public Dimension getPreferredSize(JComponent c) {
/*  81 */                 Dimension dim = super.getPreferredSize(c);
/*  82 */                 dim.setSize(13.0D, dim.getHeight());
/*  83 */                 return dim;
/*     */               }
/*     */             };
/*     */ 
/*     */           
/*  88 */           barUI.setGapThubm(5);
/*     */           
/*  90 */           JScrollPane scroller = new JScrollPane(this.list, 20, 31);
/*     */           
/*  92 */           scroller.getVerticalScrollBar().setUI(barUI);
/*  93 */           scroller.setBackground(ColorUtil.BACKGROUND_COMBO_BOX_POPUP);
/*  94 */           return scroller;
/*     */         }
/*     */       };
/*  97 */     basic.setMaximumSize(new Dimension(172, 149));
/*  98 */     basic.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, ModpackComboxRenderer.LINE));
/*  99 */     return basic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
/* 106 */     paintBackground(g, bounds);
/* 107 */     if (Objects.nonNull(this.comboBox.getSelectedItem()))
/* 108 */       paintText(g, bounds, ((CompleteVersion)this.comboBox.getSelectedItem()).getID()); 
/*     */   }
/*     */   
/*     */   protected void paintBackground(Graphics g, Rectangle bounds) {
/* 112 */     g.setColor(ColorUtil.BACKGROUND_COMBO_BOX_POPUP);
/* 113 */     g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintText(Graphics g, Rectangle textRect, String text) {
/* 118 */     Graphics2D g2d = (Graphics2D)g;
/* 119 */     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/* 120 */     g2d.setFont(this.comboBox.getFont());
/*     */     
/* 122 */     FontMetrics fm = g2d.getFontMetrics();
/* 123 */     Rectangle2D r = fm.getStringBounds(text, g2d);
/* 124 */     g.setColor(Color.WHITE);
/* 125 */     int x = this.GUP_LEFT;
/* 126 */     int y = (textRect.height - (int)r.getHeight()) / 2 + fm.getAscent();
/* 127 */     g2d.drawString(text, x, y);
/* 128 */     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\u\\ui\ModpackComboBoxUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */