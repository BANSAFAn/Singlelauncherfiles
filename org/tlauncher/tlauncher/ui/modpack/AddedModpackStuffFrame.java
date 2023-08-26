/*    */ package org.tlauncher.tlauncher.ui.modpack;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.SpringLayout;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.tlauncher.ui.swing.HtmlTextPane;
/*    */ import org.tlauncher.util.ColorUtil;
/*    */ import org.tlauncher.util.SwingUtil;
/*    */ import org.tlauncher.util.swing.FontTL;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AddedModpackStuffFrame
/*    */   extends TemlateModpackFrame
/*    */ {
/*    */   protected JPanel panel;
/*    */   protected SpringLayout spring;
/*    */   protected HtmlTextPane message;
/*    */   private static final long serialVersionUID = 8694630846172369187L;
/* 29 */   private static final Dimension DEFAULT_SIZE = new Dimension(572, 310);
/*    */   
/*    */   public AddedModpackStuffFrame(JFrame parent, String title, String message1) {
/* 32 */     super(parent, title, DEFAULT_SIZE);
/* 33 */     this.spring = new SpringLayout();
/* 34 */     this.panel = new JPanel(this.spring);
/* 35 */     this.panel.setBackground(Color.WHITE);
/* 36 */     addCenter(this.panel);
/*    */     
/* 38 */     this.message = HtmlTextPane.get(String.format("<div><center>%s</center></div>", new Object[] { Localizable.get(message1) }));
/* 39 */     this.message.setOpaque(false);
/* 40 */     BorderFactory.createEmptyBorder(0, 21, 0, 0);
/*    */     
/* 42 */     SwingUtil.changeFontFamily((JComponent)this.message, FontTL.ROBOTO_REGULAR, 15, ColorUtil.COLOR_25);
/*    */     
/* 44 */     this.spring.putConstraint("West", (Component)this.message, 29, "West", this.panel);
/* 45 */     this.spring.putConstraint("East", (Component)this.message, -27, "East", this.panel);
/* 46 */     this.spring.putConstraint("North", (Component)this.message, 40, "North", this.panel);
/* 47 */     this.spring.putConstraint("South", (Component)this.message, 140, "North", this.panel);
/* 48 */     this.panel.add((Component)this.message);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\AddedModpackStuffFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */