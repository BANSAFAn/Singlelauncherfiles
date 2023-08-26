/*    */ package org.tlauncher.tlauncher.updater.bootstrapper.view;
/*    */ 
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JProgressBar;
/*    */ import javax.swing.SwingUtilities;
/*    */ import org.tlauncher.tlauncher.configuration.LangConfiguration;
/*    */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*    */ import org.tlauncher.tlauncher.downloader.Downloader;
/*    */ import org.tlauncher.tlauncher.downloader.DownloaderListener;
/*    */ import org.tlauncher.tlauncher.ui.TLauncherFrame;
/*    */ 
/*    */ public class DownloadingFrameElement extends JFrame implements DownloaderListener {
/*    */   public DownloadingFrameElement(LangConfiguration langConfiguration) {
/* 21 */     this.langConfiguration = langConfiguration;
/* 22 */     this.bar = new JProgressBar();
/* 23 */     JPanel pan = new UpdaterPanelProgressBar();
/* 24 */     pan.setLayout(new BorderLayout());
/* 25 */     pan.setOpaque(false);
/* 26 */     this.label = new JLabel(langConfiguration.get("updater.frame.name"));
/* 27 */     this.label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
/* 28 */     this.label.setFont(this.label.getFont().deriveFont(TLauncherFrame.fontSize));
/* 29 */     pan.add(this.label, "North");
/* 30 */     pan.add(this.bar, "Center");
/* 31 */     pan.add(new JLabel(""), "South");
/* 32 */     setSize(new Dimension(350, 60));
/* 33 */     setResizable(false);
/* 34 */     setUndecorated(true);
/* 35 */     setBackground(new Color(1.0F, 1.0F, 1.0F, 0.0F));
/* 36 */     setDefaultCloseOperation(3);
/* 37 */     setLocationRelativeTo((Component)null);
/* 38 */     setAlwaysOnTop(true);
/* 39 */     add(pan);
/*    */   }
/*    */   
/*    */   private final JProgressBar bar;
/*    */   
/*    */   public void onDownloaderStart(Downloader d, int files) {
/* 45 */     setVisible(true);
/*    */   }
/*    */ 
/*    */   
/*    */   private final JLabel label;
/*    */   private final LangConfiguration langConfiguration;
/*    */   
/*    */   public void onDownloaderAbort(Downloader d) {}
/*    */   
/*    */   public void onDownloaderProgress(Downloader d, double progress, double speed, double alreadyDownloaded) {
/* 55 */     SwingUtilities.invokeLater(() -> {
/*    */           int p = (int)progress;
/*    */           this.label.setText(this.langConfiguration.get("updater.frame.name") + " " + (int)Math.ceil(alreadyDownloaded) + " " + this.langConfiguration.get("updater.frame.speed"));
/*    */           this.bar.setValue(p);
/*    */           this.bar.repaint();
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDownloaderFileComplete(Downloader d, Downloadable file) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDownloaderComplete(Downloader d) {
/* 71 */     setVisible(false);
/* 72 */     dispose();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\bootstrapper\view\DownloadingFrameElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */