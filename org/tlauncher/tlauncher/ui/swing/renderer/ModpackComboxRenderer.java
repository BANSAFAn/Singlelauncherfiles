/*    */ package org.tlauncher.tlauncher.ui.swing.renderer;
/*    */ 
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Rectangle;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.DefaultListCellRenderer;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JList;
/*    */ import javax.swing.JPanel;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.util.ColorUtil;
/*    */ import org.tlauncher.util.SwingUtil;
/*    */ import org.tlauncher.util.swing.FontTL;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModpackComboxRenderer
/*    */   extends DefaultListCellRenderer
/*    */ {
/* 26 */   public static final Color LINE = new Color(67, 187, 255);
/* 27 */   protected int GUP_LEFT = 18;
/* 28 */   protected Color backgroundBox = ColorUtil.BACKGROUND_COMBO_BOX_POPUP;
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getListCellRendererComponent(JList<?> list, Object value, final int index, final boolean isSelected, boolean cellHasFocus) {
/* 33 */     if (value == null) {
/* 34 */       return null;
/*    */     }
/* 36 */     JPanel p = new JPanel(new BorderLayout());
/* 37 */     p.setPreferredSize(new Dimension(172, 30));
/*    */     
/* 39 */     p.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, LINE));
/* 40 */     p.setBackground(this.backgroundBox);
/*    */     
/* 42 */     JLabel label = new JLabel(((CompleteVersion)value).getID())
/*    */       {
/*    */         protected void paintComponent(Graphics g) {
/* 45 */           super.paintComponent(g);
/* 46 */           if (isSelected && index > -1) {
/* 47 */             Rectangle r = getBounds();
/* 48 */             r.height--;
/* 49 */             g.setColor(new Color(121, 201, 245));
/* 50 */             g.drawLine(ModpackComboxRenderer.this.GUP_LEFT, r.height - getFontMetrics(getFont()).getDescent(), 
/* 51 */                 getFontMetrics(getFont()).stringWidth(getText()) + ModpackComboxRenderer.this.GUP_LEFT, r.height - 
/* 52 */                 getFontMetrics(getFont()).getDescent());
/*    */             
/* 54 */             g.setColor(new Color(121, 211, 247));
/* 55 */             g.drawLine(ModpackComboxRenderer.this.GUP_LEFT, r.height - getFontMetrics(getFont()).getDescent(), 
/* 56 */                 getFontMetrics(getFont()).stringWidth(getText()) + ModpackComboxRenderer.this.GUP_LEFT, r.height - 
/* 57 */                 getFontMetrics(getFont()).getDescent());
/*    */           } 
/*    */         }
/*    */       };
/*    */ 
/*    */     
/* 63 */     label.setForeground(Color.WHITE);
/* 64 */     label.setBorder(BorderFactory.createEmptyBorder(0, this.GUP_LEFT, 0, 0));
/* 65 */     SwingUtil.changeFontFamily(label, FontTL.ROBOTO_REGULAR, 14);
/* 66 */     p.add(label, "Center");
/*    */     
/* 68 */     if (index > 0) {
/*    */       
/* 70 */       label = new JLabel(ImageCache.getNativeIcon("config-modpack.png"));
/* 71 */       label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));
/* 72 */       SwingUtil.changeFontFamily(label, FontTL.ROBOTO_REGULAR, 14);
/* 73 */       p.add(label, "East");
/*    */     } 
/*    */     
/* 76 */     if (index == 0 && list.getModel().getSize() < 2) {
/* 77 */       label.setText(Localizable.get("modpack.local.box.default.list"));
/*    */     }
/* 79 */     return p;
/*    */   }
/*    */   
/*    */   public void setBackground(Color color) {
/* 83 */     this.backgroundBox = color;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\renderer\ModpackComboxRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */