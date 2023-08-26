/*     */ package org.tlauncher.tlauncher.ui.swing.progress;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.nio.file.Paths;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*     */ import org.tlauncher.tlauncher.downloader.Downloader;
/*     */ import org.tlauncher.tlauncher.downloader.DownloaderListener;
/*     */ import org.tlauncher.tlauncher.listeners.auth.LoginProcessListener;
/*     */ import org.tlauncher.tlauncher.minecraft.crash.Crash;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftExtendedListener;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
/*     */ import org.tlauncher.tlauncher.ui.login.LoginException;
/*     */ import org.tlauncher.tlauncher.ui.progress.login.LauncherProgress;
/*     */ import org.tlauncher.tlauncher.ui.progress.login.LauncherProgressListener;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.util.MinecraftUtil;
/*     */ 
/*     */ public class ProgressBarPanel extends JLayeredPane implements LocalizableComponent, DownloaderListener, MinecraftExtendedListener, LauncherProgressListener, LoginProcessListener {
/*  30 */   public static final Dimension SIZE = new Dimension(1050, 24); private static final long serialVersionUID = 4005627356304541408L;
/*     */   private final JLabel percentLabel;
/*     */   private final JLabel centerLabel;
/*     */   private final JLabel speedLabel;
/*     */   private final JLabel fileLabel;
/*     */   private final ExtendedPanel upperPanel;
/*     */   private JProgressBar bar;
/*     */   private boolean downloadingStart;
/*     */   
/*     */   public ProgressBarPanel(Icon speed, Icon file, LauncherProgress bar) {
/*  40 */     this.bar = (JProgressBar)bar;
/*  41 */     setVisible(false);
/*  42 */     this.upperPanel = new ExtendedPanel(new BorderLayout(0, 0));
/*  43 */     this.speedLabel = createLabel(speed);
/*  44 */     this.speedLabel.setHorizontalTextPosition(4);
/*  45 */     this.fileLabel = createLabel(file);
/*  46 */     this.fileLabel.setHorizontalTextPosition(4);
/*  47 */     this.percentLabel = createLabel((Icon)null);
/*  48 */     this.centerLabel = createLabel((Icon)null);
/*     */     
/*  50 */     this.centerLabel.setHorizontalAlignment(0);
/*     */     
/*  52 */     this.percentLabel.setFont(this.percentLabel.getFont().deriveFont(1));
/*  53 */     this.upperPanel.setPreferredSize(SIZE);
/*  54 */     this.upperPanel.setInsets(0, 20, 0, 20);
/*     */     
/*  56 */     setPreferredSize(SIZE);
/*     */     
/*  58 */     ExtendedPanel leftPanel = new ExtendedPanel();
/*  59 */     leftPanel.setLayout(new BoxLayout((Container)leftPanel, 0));
/*  60 */     leftPanel.add(this.speedLabel);
/*  61 */     leftPanel.add(Box.createHorizontalStrut(20));
/*  62 */     leftPanel.add(this.fileLabel);
/*  63 */     this.upperPanel.add(this.centerLabel, "Center");
/*  64 */     this.upperPanel.add(this.percentLabel, "East");
/*  65 */     this.upperPanel.add((Component)leftPanel, "West");
/*  66 */     add((Component)bar, 1);
/*  67 */     add((Component)this.upperPanel, 0);
/*     */     
/*  69 */     bar.setBounds(0, 0, SIZE.width, SIZE.height);
/*  70 */     this.upperPanel.setBounds(0, 0, SIZE.width, SIZE.height);
/*     */   }
/*     */   
/*     */   private JLabel createLabel(Icon icon) {
/*  74 */     JLabel jLabel = new JLabel();
/*  75 */     jLabel.setFont(jLabel.getFont().deriveFont(0, 12.0F));
/*  76 */     if (icon != null) {
/*  77 */       jLabel.setIcon(icon);
/*     */     }
/*  79 */     jLabel.setHorizontalTextPosition(4);
/*  80 */     return jLabel;
/*     */   }
/*     */   
/*     */   private void startProgress() {
/*  84 */     clean();
/*  85 */     this.centerLabel.setText(Localizable.get("progress.bar.panel.init"));
/*  86 */     this.downloadingStart = true;
/*  87 */     setVisible(true);
/*     */   }
/*     */   
/*     */   private void stopProgress() {
/*  91 */     clean();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateLocale() {}
/*     */ 
/*     */   
/*     */   public void onDownloaderStart(Downloader d, int files) {
/* 100 */     startProgress();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDownloaderAbort(Downloader d) {
/* 105 */     stopProgress();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDownloaderProgress(Downloader d, double progress, double speed, double remainedData) {
/* 111 */     updateStateDownloading(d, progress, speed, remainedData);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDownloaderFileComplete(Downloader d, Downloadable file) {
/* 116 */     updateStateDownloading(d, d.getProgress(), d.getSpeed(), d.getRemainedData());
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateStateDownloading(Downloader d, double progress, double speed, double remainedData) {
/* 121 */     int remainingTime = (int)(remainedData / speed);
/* 122 */     this.bar.setValue((int)progress);
/* 123 */     StringBuilder b = new StringBuilder();
/* 124 */     b.append(Localizable.get("progress.bar.panel.file.remained"))
/* 125 */       .append(" ")
/* 126 */       .append(d.getRemaining())
/* 127 */       .append(" ")
/* 128 */       .append(Localizable.get("progress.bar.panel.file"))
/* 129 */       .append(" ")
/* 130 */       .append((int)Math.ceil(remainedData))
/* 131 */       .append(" ")
/* 132 */       .append(Localizable.get("progress.bar.panel.size"))
/* 133 */       .append(", ")
/* 134 */       .append(Localizable.get("progress.bar.panel.remaining.time"))
/* 135 */       .append(" ");
/* 136 */     if (remainingTime < 60) {
/* 137 */       b.append(Localizable.get("progress.bar.panel.remaining.time.less.than.minute"));
/*     */     } else {
/* 139 */       b.append((int)Math.ceil(remainingTime / 60.0D))
/* 140 */         .append(" ")
/* 141 */         .append(Localizable.get("progress.bar.panel.remaining.time.minutes"));
/* 142 */     }  this.fileLabel.setText(b.toString());
/* 143 */     b.setLength(0);
/* 144 */     b.append(Localizable.get("progress.bar.panel.speed"));
/*     */     
/* 146 */     if (speed < 1.0D) {
/* 147 */       speed *= 1024.0D;
/* 148 */       b.append(" ")
/* 149 */         .append(String.format("%.2f", new Object[] { Double.valueOf(speed)
/* 150 */             })).append(" ")
/* 151 */         .append(Localizable.get("progress.bar.panel.speed.unity.KB"));
/*     */     } else {
/* 153 */       b.append(" ")
/* 154 */         .append(String.format("%.2f", new Object[] { Double.valueOf(speed)
/* 155 */             })).append(" ")
/* 156 */         .append(Localizable.get("progress.bar.panel.speed.unity.MB"));
/*     */     } 
/* 158 */     this.speedLabel.setText(b.toString());
/* 159 */     this.percentLabel.setText((int)progress + "%");
/* 160 */     if (this.downloadingStart) {
/* 161 */       this.centerLabel.setVisible(false);
/* 162 */       this.fileLabel.setVisible(true);
/* 163 */       this.speedLabel.setVisible(true);
/* 164 */       this.downloadingStart = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDownloaderComplete(Downloader d) {
/* 170 */     stopProgress();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMinecraftPrepare() {
/* 175 */     startProgress();
/* 176 */     this.centerLabel.setVisible(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMinecraftAbort() {
/* 181 */     stopProgress();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftClose() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftError(Throwable e) {
/* 191 */     stopProgress();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftKnownError(MinecraftException e) {
/* 197 */     stopProgress();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftCrash(Crash crash) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftCollecting() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftComparingAssets() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftDownloading() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftReconstructingAssets() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftUnpackingNatives() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftDeletingEntries() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftConstructing() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftLaunch() {
/* 241 */     stopProgress();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftPostLaunch() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onWorldBackup(String backupWorld) {
/* 251 */     this.centerLabel.setText(Localizable.get("settings.backup.process", new Object[] { backupWorld }).concat(" " + 
/* 252 */           String.valueOf(Paths.get(MinecraftUtil.getWorkingDirectory().toString(), new String[] { "backup/saves" }))));
/*     */   }
/*     */   
/*     */   private void clean() {
/* 256 */     this.speedLabel.setVisible(false);
/* 257 */     this.fileLabel.setVisible(false);
/* 258 */     this.speedLabel.setText("");
/* 259 */     this.fileLabel.setText("");
/*     */     
/* 261 */     this.percentLabel.setText("");
/* 262 */     this.bar.setValue(0);
/* 263 */     setVisible(false);
/*     */   }
/*     */   
/*     */   public JProgressBar getBar() {
/* 267 */     return this.bar;
/*     */   }
/*     */   
/*     */   public void setBar(JProgressBar bar) {
/* 271 */     this.bar = bar;
/*     */   }
/*     */ 
/*     */   
/*     */   public void repaintPanel() {
/* 276 */     this.upperPanel.repaint();
/*     */   }
/*     */ 
/*     */   
/*     */   public void preValidate() throws LoginException {
/* 281 */     SwingUtilities.invokeLater(() -> {
/*     */           clean();
/*     */           this.centerLabel.setText(Localizable.get("progress.bar.panel.validate.account.token"));
/*     */           this.downloadingStart = true;
/*     */           setVisible(true);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void loginFailed() {
/* 291 */     SwingUtilities.invokeLater(() -> stopProgress());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\progress\ProgressBarPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */