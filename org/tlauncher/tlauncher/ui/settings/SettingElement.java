/*    */ package org.tlauncher.tlauncher.ui.settings;
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.JComponent;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*    */ 
/*    */ public class SettingElement extends ExtendedPanel {
/* 10 */   public static int FIRST_PART = 162;
/* 11 */   public static int SECOND_PART = 420;
/* 12 */   public static final Font LABEL_FONT = (new JLabel()).getFont().deriveFont(1, 12.0F);
/*    */   
/*    */   public SettingElement(String name, JComponent panel, int height) {
/* 15 */     setLayout(new BoxLayout((Container)this, 0));
/* 16 */     LocalizableLabel label = new LocalizableLabel(name);
/*    */     
/* 18 */     label.setHorizontalAlignment(2);
/* 19 */     label.setVerticalAlignment(0);
/* 20 */     label.setVerticalAlignment(0);
/* 21 */     label.setVerticalTextPosition(0);
/* 22 */     label.setFont(LABEL_FONT);
/* 23 */     ExtendedPanel p = new ExtendedPanel(new BorderLayout(0, 0));
/* 24 */     p.setPreferredSize(new Dimension(FIRST_PART, height));
/* 25 */     p.add((Component)label);
/* 26 */     panel.setPreferredSize(new Dimension(SECOND_PART, height));
/* 27 */     add((Component)p);
/* 28 */     add(panel);
/*    */   }
/*    */   public SettingElement(String name, JComponent panel, int height, int labelUpGap, int labelVerticalPosition) {
/* 31 */     setLayout(new BoxLayout((Container)this, 0));
/* 32 */     LocalizableLabel label = new LocalizableLabel(name);
/* 33 */     label.setHorizontalAlignment(2);
/* 34 */     label.setVerticalAlignment(labelVerticalPosition);
/* 35 */     label.setVerticalTextPosition(0);
/* 36 */     label.setFont(LABEL_FONT);
/* 37 */     ExtendedPanel p = new ExtendedPanel(new BorderLayout(0, 0));
/* 38 */     p.setInsets(labelUpGap, 0, 0, 0);
/* 39 */     p.setPreferredSize(new Dimension(FIRST_PART, height));
/* 40 */     p.add((Component)label);
/* 41 */     panel.setPreferredSize(new Dimension(SECOND_PART, height));
/* 42 */     add((Component)p);
/* 43 */     add(panel);
/*    */   }
/*    */   public SettingElement(String name, JComponent panel, int height, int labelUpGap) {
/* 46 */     this(name, panel, height, labelUpGap, 0);
/*    */   }
/*    */   
/*    */   public SettingElement(String name, JComponent elem, int height, int labelUpGap, JComponent thirdElement) {
/* 50 */     setLayout(new FlowLayout(0, 0, 0));
/* 51 */     LocalizableLabel label = new LocalizableLabel(name);
/* 52 */     label.setHorizontalAlignment(2);
/* 53 */     label.setVerticalAlignment(0);
/* 54 */     label.setVerticalAlignment(0);
/* 55 */     label.setVerticalTextPosition(0);
/* 56 */     label.setFont(LABEL_FONT);
/* 57 */     ExtendedPanel p = new ExtendedPanel(new BorderLayout(0, 0));
/* 58 */     p.setInsets(labelUpGap, 0, 0, 0);
/* 59 */     p.setPreferredSize(new Dimension(FIRST_PART, height));
/* 60 */     p.add((Component)label);
/* 61 */     add((Component)p);
/* 62 */     add(elem);
/* 63 */     elem.setPreferredSize(new Dimension((elem.getPreferredSize()).width, height));
/* 64 */     add(thirdElement);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\settings\SettingElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */