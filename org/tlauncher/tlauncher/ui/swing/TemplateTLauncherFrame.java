/*    */ package org.tlauncher.tlauncher.ui.swing;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JPanel;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.util.SwingUtil;
/*    */ 
/*    */ 
/*    */ public class TemplateTLauncherFrame
/*    */   extends JFrame
/*    */ {
/*    */   protected JPanel contentPane;
/*    */   public static final int MAX_WIDTH_ELEMENT = 500;
/*    */   protected JFrame parent;
/*    */   
/*    */   public TemplateTLauncherFrame(JFrame parent, String title) {
/* 18 */     setBounds(100, 100, 400, 366);
/* 19 */     this.parent = parent;
/* 20 */     setLocationRelativeTo((Component)null);
/* 21 */     setAlwaysOnTop(false);
/* 22 */     setResizable(false);
/* 23 */     SwingUtil.setFavicons(this);
/* 24 */     setTitle(Localizable.get(title));
/* 25 */     parent.setEnabled(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setVisible(boolean b) {
/* 30 */     this.parent.setEnabled(!b);
/* 31 */     super.setVisible(b);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\TemplateTLauncherFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */