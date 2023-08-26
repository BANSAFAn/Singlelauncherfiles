/*     */ package org.tlauncher.tlauncher.ui.modpack;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Point;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPanel;
/*     */ import org.tlauncher.tlauncher.ui.MainPane;
/*     */ import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ 
/*     */ public class TemlateModpackFrame extends JDialog {
/*  19 */   protected int upGap = 92;
/*     */   
/*  21 */   public static final Color BLUE_COLOR = new Color(69, 168, 223);
/*  22 */   public static final Color BLUE_COLOR_UNDER = new Color(2, 161, 221);
/*     */   
/*  24 */   public static final Color COLOR_0_174_239 = new Color(0, 174, 239);
/*     */   
/*     */   public static final int LEFT_BORDER = 29;
/*     */   
/*     */   public static final int RIGHT_BORDER = 27;
/*     */   protected LocalizableLabel label;
/*  30 */   protected final JPanel baseContainer = new JPanel(null);
/*  31 */   private final JPanel paneContainer = new JPanel(new BorderLayout())
/*     */     {
/*     */       public synchronized void addMouseListener(MouseListener l) {}
/*     */     };
/*     */ 
/*     */   
/*     */   public TemlateModpackFrame(JFrame parent, String title, Dimension size) {
/*  38 */     this(parent, title, size, false);
/*     */   }
/*     */   
/*     */   public TemlateModpackFrame(JFrame parent, String title, Dimension size, boolean noTransparentImage) {
/*  42 */     super(parent);
/*  43 */     setUndecorated(true);
/*  44 */     setTitle(Localizable.get(title));
/*  45 */     setLocationRelativeTo((Component)null);
/*  46 */     setResizable(false);
/*  47 */     if (!noTransparentImage) {
/*  48 */       setBackground(new Color(0, 0, 0, 50));
/*  49 */       this.baseContainer.setOpaque(false);
/*     */     } 
/*     */     
/*  52 */     this.baseContainer.add(this.paneContainer);
/*  53 */     add(this.baseContainer);
/*     */     
/*  55 */     JPanel panel = new JPanel(new FlowLayout(0, 0, 0));
/*  56 */     panel.setPreferredSize(new Dimension(size.width, 47));
/*  57 */     panel.setBackground(BLUE_COLOR);
/*     */     
/*  59 */     final ImageUdaterButton close = new ImageUdaterButton(BLUE_COLOR, "close-modpack.png");
/*  60 */     imageUdaterButton.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseEntered(MouseEvent e) {
/*  63 */             close.setBackground(new Color(60, 145, 193));
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/*  68 */             close.setBackground(TemlateModpackFrame.BLUE_COLOR);
/*     */           }
/*     */         });
/*     */     
/*  72 */     this.label = new LocalizableLabel(title);
/*  73 */     SwingUtil.changeFontFamily((JComponent)this.label, FontTL.ROBOTO_REGULAR, 18, ColorUtil.COLOR_248);
/*     */     
/*  75 */     this.label.setBorder(new EmptyBorder(0, 40, 0, 0));
/*     */     
/*  77 */     this.label.setHorizontalTextPosition(0);
/*  78 */     this.label.setHorizontalAlignment(0);
/*  79 */     this.label.setPreferredSize(new Dimension(size.width - 41, 47));
/*  80 */     imageUdaterButton.setPreferredSize(new Dimension(41, 47));
/*     */     
/*  82 */     panel.add((Component)this.label);
/*  83 */     panel.add((Component)imageUdaterButton);
/*     */     
/*  85 */     this.paneContainer.add(panel, "North");
/*     */     
/*  87 */     imageUdaterButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/*  91 */             TemlateModpackFrame.this.setVisible(false);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  96 */     Point point = parent.getContentPane().getLocationOnScreen();
/*  97 */     setBounds(point.x, point.y, MainPane.SIZE.width, MainPane.SIZE.height);
/*  98 */     setCenter(size);
/*  99 */     if (!TLauncher.DEBUG)
/* 100 */       setAlwaysOnTop(true); 
/*     */   }
/*     */   
/*     */   public void addCenter(JComponent comp) {
/* 104 */     this.paneContainer.add(comp, "Center");
/*     */   }
/*     */   
/*     */   public void setCenter(Dimension size) {
/* 108 */     Point point = this.baseContainer.getLocation();
/* 109 */     this.paneContainer.setBounds(point.x + (MainPane.SIZE.width - size.width) / 2, point.y + this.upGap, size.width, size.height);
/*     */     
/* 111 */     this.paneContainer.revalidate();
/* 112 */     this.paneContainer.repaint();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVisible(boolean b) {
/* 117 */     if (b) {
/* 118 */       getParent().setEnabled(false);
/*     */     } else {
/* 120 */       getParent().setEnabled(true);
/*     */     } 
/* 122 */     super.setVisible(b);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\TemlateModpackFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */