/*    */ package org.tlauncher.tlauncher.ui.modpack;
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JLayeredPane;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JSlider;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*    */ import org.tlauncher.util.SwingUtil;
/*    */ import org.tlauncher.util.swing.FontTL;
/*    */ 
/*    */ public class SliderModpackPanel extends ExtendedPanel {
/* 15 */   private final Color color179 = new Color(179, 179, 179);
/* 16 */   private final Color downBackground = new Color(244, 252, 255);
/* 17 */   private final int downHeight = 40;
/* 18 */   private final Color color233 = new Color(233, 233, 233);
/*    */   
/*    */   JSlider slider;
/*    */   
/*    */   public SliderModpackPanel(Dimension dimension) {
/* 23 */     setPreferredSize(dimension);
/*    */     
/* 25 */     this.slider = new JSlider();
/*    */     
/* 27 */     this.slider.setOpaque(false);
/* 28 */     this.slider.setUI((SliderUI)new ModpackSliderUI(this.slider));
/* 29 */     this.slider.setMinimum(512);
/* 30 */     this.slider.setMaximum(OS.Arch.MAX_MEMORY);
/* 31 */     this.slider.setMajorTickSpacing((OS.Arch.MAX_MEMORY - 512) / 20);
/* 32 */     this.slider.setSnapToTicks(true);
/* 33 */     this.slider.setPaintLabels(false);
/*    */     
/* 35 */     this.slider.setValue(this.slider.getMaximum() * 2 / 3);
/*    */     
/* 37 */     JLayeredPane layeredPane = new JLayeredPane();
/* 38 */     JPanel downPanel = new JPanel(new FlowLayout(0));
/* 39 */     downPanel.add(Box.createVerticalStrut(215));
/*    */     
/* 41 */     LocalizableLabel localizableLabel1 = new LocalizableLabel("modpack.config.recommended");
/* 42 */     localizableLabel1.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, this.color233));
/* 43 */     localizableLabel1.setHorizontalAlignment(0);
/* 44 */     localizableLabel1.setPreferredSize(new Dimension(131, 40));
/* 45 */     LocalizableLabel localizableLabel2 = new LocalizableLabel("modpack.config.high.level");
/* 46 */     localizableLabel2.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, this.color233));
/* 47 */     localizableLabel2.setHorizontalAlignment(0);
/*    */     
/* 49 */     localizableLabel2.setPreferredSize(new Dimension(171, 20));
/*    */     
/* 51 */     SwingUtil.changeFontFamily(this.slider, FontTL.ROBOTO_BOLD, 14, Color.BLACK);
/* 52 */     SwingUtil.changeFontFamily((JComponent)localizableLabel2, FontTL.ROBOTO_REGULAR, 12, this.color179);
/* 53 */     SwingUtil.changeFontFamily((JComponent)localizableLabel1, FontTL.ROBOTO_REGULAR, 12, this.color179);
/*    */     
/* 55 */     downPanel.setBackground(this.downBackground);
/*    */     
/* 57 */     layeredPane.setPreferredSize(dimension);
/* 58 */     layeredPane.add(this.slider, new Integer(2));
/* 59 */     layeredPane.add((Component)localizableLabel2, new Integer(1));
/* 60 */     layeredPane.add((Component)localizableLabel1, new Integer(1));
/* 61 */     layeredPane.add(downPanel, new Integer(0));
/*    */     
/* 63 */     localizableLabel1.setBounds(225, 27, 130, 39);
/* 64 */     localizableLabel2.setBounds(356, 27, 171, 39);
/* 65 */     int border = 8;
/* 66 */     downPanel.setBounds(border + 3, dimension.height - 40 - 12, dimension.width - border * 2 - 3, 40);
/*    */ 
/*    */     
/* 69 */     this.slider.setBounds(0, 0, dimension.width, dimension.height - 20);
/* 70 */     add(layeredPane);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getValue() {
/* 75 */     return this.slider.getValue();
/*    */   }
/*    */   
/*    */   public void setValue(int value) {
/* 79 */     this.slider.setValue(value);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\SliderModpackPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */