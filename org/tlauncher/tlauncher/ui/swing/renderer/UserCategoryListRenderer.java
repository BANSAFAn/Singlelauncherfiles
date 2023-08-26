/*    */ package org.tlauncher.tlauncher.ui.swing.renderer;
/*    */ 
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Rectangle;
/*    */ import java.util.Locale;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JList;
/*    */ import javax.swing.JPanel;
/*    */ import org.tlauncher.modpack.domain.client.share.GameEntitySort;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.util.SwingUtil;
/*    */ import org.tlauncher.util.swing.FontTL;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UserCategoryListRenderer
/*    */   extends ModpackComboxRenderer
/*    */ {
/*    */   public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
/* 30 */     if (value == null) {
/* 31 */       return null;
/*    */     }
/*    */     
/* 34 */     String text = Localizable.get("modpack." + ((GameEntitySort)value).toString().toLowerCase(Locale.ROOT));
/* 35 */     return createElement(index, isSelected, text);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Component createElement(final int index, final boolean isSelected, String text) {
/* 40 */     JLabel label = new JLabel(text)
/*    */       {
/*    */         protected void paintComponent(Graphics g) {
/* 43 */           super.paintComponent(g);
/* 44 */           if (isSelected && index > -1) {
/* 45 */             Rectangle r = g.getClipBounds();
/* 46 */             r.height--;
/* 47 */             g.setColor(new Color(121, 201, 245));
/* 48 */             g.drawLine(UserCategoryListRenderer.this.GUP_LEFT, r.height - getFontMetrics(getFont()).getDescent(), 
/* 49 */                 getFontMetrics(getFont()).stringWidth(getText()) + UserCategoryListRenderer.this.GUP_LEFT, r.height - 
/* 50 */                 getFontMetrics(getFont()).getDescent());
/*    */             
/* 52 */             g.setColor(new Color(121, 211, 247));
/* 53 */             g.drawLine(UserCategoryListRenderer.this.GUP_LEFT, r.height - getFontMetrics(getFont()).getDescent(), 
/* 54 */                 getFontMetrics(getFont()).stringWidth(getText()) + UserCategoryListRenderer.this.GUP_LEFT, r.height - 
/* 55 */                 getFontMetrics(getFont()).getDescent());
/*    */           } 
/*    */         }
/*    */       };
/*    */     
/* 60 */     label.setForeground(Color.WHITE);
/* 61 */     SwingUtil.changeFontFamily(label, FontTL.ROBOTO_REGULAR, 14);
/* 62 */     label.setBorder(BorderFactory.createEmptyBorder(0, this.GUP_LEFT, 0, 0));
/*    */     
/* 64 */     JPanel p = new JPanel(new BorderLayout());
/* 65 */     p.setPreferredSize(new Dimension(172, 30));
/*    */     
/* 67 */     p.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, LINE));
/* 68 */     p.setBackground(this.backgroundBox);
/* 69 */     p.add(label, "Center");
/* 70 */     return p;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\renderer\UserCategoryListRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */