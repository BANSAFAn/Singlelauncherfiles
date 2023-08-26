/*    */ package org.tlauncher.tlauncher.ui.swing.extended;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.Action;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.JButton;
/*    */ import org.tlauncher.tlauncher.ui.TLauncherFrame;
/*    */ 
/*    */ public class ExtendedButton extends JButton {
/* 13 */   public static final Color ORANGE_COLOR = new Color(235, 132, 46); private static final long serialVersionUID = -2009736184875993130L;
/* 14 */   public static final Color GREEN_COLOR = new Color(107, 202, 45);
/* 15 */   public static final Color DARK_GREEN_COLOR = new Color(113, 169, 76);
/* 16 */   public static final Color GRAY_COLOR = new Color(176, 177, 173);
/*    */ 
/*    */   
/*    */   protected ExtendedButton() {
/* 20 */     init();
/*    */   }
/*    */   
/*    */   public ExtendedButton(Icon icon) {
/* 24 */     super(icon);
/* 25 */     init();
/*    */   }
/*    */   
/*    */   protected ExtendedButton(String text) {
/* 29 */     super(text);
/* 30 */     init();
/*    */   }
/*    */   
/*    */   public ExtendedButton(Action a) {
/* 34 */     super(a);
/* 35 */     init();
/*    */   }
/*    */   
/*    */   public ExtendedButton(String text, Icon icon) {
/* 39 */     super(text, icon);
/* 40 */     init();
/*    */   }
/*    */   
/*    */   private void init() {
/* 44 */     setFont(getFont().deriveFont(TLauncherFrame.fontSize));
/* 45 */     setOpaque(false);
/* 46 */     addActionListener(new ActionListener()
/*    */         {
/*    */           public void actionPerformed(ActionEvent e) {
/* 49 */             Component parent = findRootParent(ExtendedButton.this.getParent());
/*    */             
/* 51 */             if (parent == null) {
/*    */               return;
/*    */             }
/* 54 */             parent.requestFocusInWindow();
/*    */           }
/*    */           
/*    */           private Component findRootParent(Component comp) {
/* 58 */             if (comp == null)
/* 59 */               return null; 
/* 60 */             if (comp.getParent() == null) {
/* 61 */               return comp;
/*    */             }
/* 63 */             return findRootParent(comp.getParent());
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\extended\ExtendedButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */