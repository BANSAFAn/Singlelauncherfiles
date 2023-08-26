/*    */ package org.tlauncher.tlauncher.ui.progress;
/*    */ 
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.assistedinject.Assisted;
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Font;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JProgressBar;
/*    */ import javax.swing.SpringLayout;
/*    */ import javax.swing.plaf.ProgressBarUI;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*    */ import org.tlauncher.tlauncher.ui.ui.PreloaderProgressUI;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProgressFrame
/*    */   extends JFrame
/*    */ {
/*    */   private static final int WIDTH = 240;
/*    */   private static final int HEIGHT = 99;
/* 25 */   private Font font = new Font("Verdana", 0, 10);
/*    */   private String version_info;
/*    */   private JProgressBar progressBar;
/* 28 */   public final Color VERSION_BACKGROUND = new Color(40, 134, 187);
/*    */ 
/*    */   
/*    */   @Inject
/*    */   public ProgressFrame(@Assisted("info") String info) {
/* 33 */     getContentPane().setForeground(Color.LIGHT_GRAY);
/* 34 */     setTitle("TLauncher");
/* 35 */     setSize(240, 99);
/* 36 */     setLocationRelativeTo(null);
/* 37 */     setUndecorated(true);
/* 38 */     setResizable(false);
/* 39 */     setBackground(Color.LIGHT_GRAY);
/* 40 */     setDefaultCloseOperation(3);
/*    */ 
/*    */     
/* 43 */     this.version_info = info;
/* 44 */     SpringLayout springLayout = new SpringLayout();
/* 45 */     getContentPane().setLayout(springLayout);
/* 46 */     getContentPane().setPreferredSize(new Dimension(240, 99));
/*    */     
/* 48 */     JLabel version = new JLabel(this.version_info);
/* 49 */     version.setHorizontalAlignment(0);
/* 50 */     version.setForeground(Color.WHITE);
/* 51 */     version.setFont(this.font);
/* 52 */     version.setOpaque(true);
/* 53 */     version.setBackground(this.VERSION_BACKGROUND);
/* 54 */     springLayout.putConstraint("North", version, 0, "North", getContentPane());
/* 55 */     springLayout.putConstraint("West", version, -58, "East", getContentPane());
/* 56 */     springLayout.putConstraint("South", version, 16, "North", getContentPane());
/* 57 */     springLayout.putConstraint("East", version, 0, "East", getContentPane());
/* 58 */     getContentPane().add(version);
/*    */     
/* 60 */     JLabel backgroundImage = new JLabel();
/* 61 */     backgroundImage.setIcon(ImageCache.getNativeIcon("tlauncher.png"));
/* 62 */     springLayout.putConstraint("North", backgroundImage, 0, "North", getContentPane());
/* 63 */     springLayout.putConstraint("West", backgroundImage, 0, "West", getContentPane());
/* 64 */     springLayout.putConstraint("South", backgroundImage, 75, "North", getContentPane());
/* 65 */     springLayout.putConstraint("East", backgroundImage, 244, "West", getContentPane());
/* 66 */     getContentPane().add(backgroundImage);
/*    */ 
/*    */ 
/*    */     
/* 70 */     this.progressBar = new JProgressBar();
/* 71 */     this.progressBar.setIndeterminate(true);
/* 72 */     this.progressBar.setBorder(BorderFactory.createEmptyBorder());
/* 73 */     this.progressBar.setUI((ProgressBarUI)new PreloaderProgressUI(ImageCache.getBufferedImage("bottom-bar.png"), 
/* 74 */           ImageCache.getBufferedImage("up-progress-bar.png")));
/*    */     
/* 76 */     springLayout.putConstraint("North", this.progressBar, 0, "South", backgroundImage);
/* 77 */     springLayout.putConstraint("West", this.progressBar, 0, "West", getContentPane());
/* 78 */     springLayout.putConstraint("South", this.progressBar, 0, "South", getContentPane());
/* 79 */     springLayout.putConstraint("East", this.progressBar, 4, "East", getContentPane());
/* 80 */     getContentPane().add(this.progressBar);
/* 81 */     pack();
/* 82 */     setVisible(true);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\progress\ProgressFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */