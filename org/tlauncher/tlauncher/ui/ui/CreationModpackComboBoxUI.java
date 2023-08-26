/*     */ package org.tlauncher.tlauncher.ui.ui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.util.Objects;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.plaf.basic.BasicComboBoxUI;
/*     */ import javax.swing.plaf.basic.BasicComboPopup;
/*     */ import javax.swing.plaf.basic.ComboPopup;
/*     */ import org.tlauncher.modpack.domain.client.share.MinecraftVersionDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.VersionMaturity;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
/*     */ import org.tlauncher.util.ColorUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CreationModpackComboBoxUI
/*     */   extends BasicComboBoxUI
/*     */ {
/*  34 */   private static Icon ICON_STAR = (Icon)ImageCache.getIcon("star-true.png");
/*  35 */   private static Icon ICON_STAR_FALSE = (Icon)ImageCache.getIcon("star-false.png");
/*     */ 
/*     */   
/*     */   protected JButton createArrowButton() {
/*  39 */     ImageUdaterButton button = new ImageUdaterButton(Color.WHITE, "gray-combobox-array.png");
/*  40 */     for (ActionListener l : button.getActionListeners()) {
/*  41 */       button.removeActionListener(l);
/*     */     }
/*  43 */     button.setModelPressedColor(ColorUtil.COLOR_195);
/*  44 */     return (JButton)button;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ComboPopup createPopup() {
/*  50 */     BasicComboPopup basic = new BasicComboPopup(this.comboBox)
/*     */       {
/*     */         private static final long serialVersionUID = -1200285237129861017L;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         protected JScrollPane createScroller() {
/*  58 */           ModpackScrollBarUI barUI = new ModpackScrollBarUI()
/*     */             {
/*     */               protected Dimension getMinimumThumbSize() {
/*  61 */                 return new Dimension(10, 40);
/*     */               }
/*     */ 
/*     */               
/*     */               public Dimension getMaximumSize(JComponent c) {
/*  66 */                 Dimension dim = super.getMaximumSize(c);
/*  67 */                 dim.setSize(10.0D, dim.getHeight());
/*  68 */                 return dim;
/*     */               }
/*     */ 
/*     */               
/*     */               public Dimension getPreferredSize(JComponent c) {
/*  73 */                 Dimension dim = super.getPreferredSize(c);
/*  74 */                 dim.setSize(13.0D, dim.getHeight());
/*  75 */                 return dim;
/*     */               }
/*     */             };
/*     */           
/*  79 */           barUI.setGapThubm(5);
/*     */           
/*  81 */           JScrollPane scroller = new JScrollPane(this.list, 20, 31);
/*     */           
/*  83 */           scroller.getVerticalScrollBar().setUI(barUI);
/*  84 */           return scroller;
/*     */         }
/*     */       };
/*  87 */     basic.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(149, 149, 149)));
/*  88 */     return basic;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
/*  94 */     super.paintCurrentValue(g, bounds, hasFocus);
/*  95 */     g.setColor(Color.WHITE);
/*  96 */     g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
/*  97 */     Object ob = this.comboBox.getSelectedItem();
/*  98 */     paintText(g, bounds, Objects.isNull(ob) ? "" : getText(ob));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintText(Graphics g, Rectangle textRect, String text) {
/* 104 */     Graphics2D g2d = (Graphics2D)g;
/* 105 */     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/* 106 */     FontMetrics fm = g2d.getFontMetrics();
/* 107 */     Rectangle2D r = fm.getStringBounds(text, g2d);
/* 108 */     g.setFont(this.comboBox.getFont());
/* 109 */     g.setColor(new Color(25, 25, 25));
/* 110 */     int x = 14;
/* 111 */     int y = (textRect.height - (int)r.getHeight()) / 2 + fm.getAscent();
/* 112 */     g2d.drawString(text, x, y);
/* 113 */     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
/* 114 */     PositionIcon pi = getRenderIcon(this.comboBox.getSelectedItem());
/* 115 */     if (Objects.nonNull(pi)) {
/* 116 */       y = (textRect.height - pi.getIcon().getIconHeight()) / 2;
/* 117 */       x += (int)r.getWidth() + 4;
/* 118 */       pi.getIcon().paintIcon(this.comboBox, g, x, y);
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract String getText(Object paramObject);
/*     */   
/*     */   public static PositionIcon getRenderIcon(Object value) {
/* 125 */     if (Objects.isNull(value) || !(value instanceof MinecraftVersionDTO))
/* 126 */       return null; 
/* 127 */     MinecraftVersionDTO dto = (MinecraftVersionDTO)value;
/* 128 */     if (VersionMaturity.PROMO_RECOMMEDED.equals(dto.getMaturity()))
/* 129 */       return new PositionIcon(ICON_STAR, 2); 
/* 130 */     if (VersionMaturity.PROMO_LATEST.equals(dto.getMaturity())) {
/* 131 */       return new PositionIcon(ICON_STAR_FALSE, 2);
/*     */     }
/* 133 */     return null;
/*     */   } public static class PositionIcon {
/*     */     private Icon icon;
/* 136 */     public void setIcon(Icon icon) { this.icon = icon; } private int iconPosition; public void setIconPosition(int iconPosition) { this.iconPosition = iconPosition; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof PositionIcon)) return false;  PositionIcon other = (PositionIcon)o; if (!other.canEqual(this)) return false;  Object this$icon = getIcon(), other$icon = other.getIcon(); return ((this$icon == null) ? (other$icon != null) : !this$icon.equals(other$icon)) ? false : (!(getIconPosition() != other.getIconPosition())); } protected boolean canEqual(Object other) { return other instanceof PositionIcon; } public int hashCode() { int PRIME = 59; result = 1; Object $icon = getIcon(); result = result * 59 + (($icon == null) ? 43 : $icon.hashCode()); return result * 59 + getIconPosition(); } public String toString() { return "CreationModpackComboBoxUI.PositionIcon(icon=" + getIcon() + ", iconPosition=" + getIconPosition() + ")"; } public PositionIcon(Icon icon, int iconPosition) {
/* 137 */       this.icon = icon; this.iconPosition = iconPosition;
/*     */     }
/* 139 */     public Icon getIcon() { return this.icon; } public int getIconPosition() {
/* 140 */       return this.iconPosition;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\u\\ui\CreationModpackComboBoxUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */