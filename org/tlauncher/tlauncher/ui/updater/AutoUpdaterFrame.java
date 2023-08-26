/*     */ package org.tlauncher.tlauncher.ui.updater;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.RenderingHints;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPanel;
/*     */ import org.tlauncher.tlauncher.controller.UpdaterFormController;
/*     */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*     */ import org.tlauncher.tlauncher.downloader.Downloader;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.listener.UpdateUIListener;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.swing.ImagePanel;
/*     */ import org.tlauncher.tlauncher.updater.client.AutoUpdater;
/*     */ import org.tlauncher.tlauncher.updater.client.Update;
/*     */ import org.tlauncher.tlauncher.updater.client.UpdateListener;
/*     */ import org.tlauncher.tlauncher.updater.client.Updater;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ public class AutoUpdaterFrame extends JFrame implements DownloaderListener, UpdaterListener, UpdateListener {
/*     */   private static final long serialVersionUID = -1184260781662212096L;
/*     */   private static final int ANIMATION_TICK = 1;
/*  30 */   private final Color border = new Color(255, 255, 255, 255); private static final double OPACITY_STEP = 0.005D; private final AutoUpdaterFrame instance; private final JPanel titlepan; private final JPanel pan; private final LocalizableLabel label;
/*     */   private final ImagePanel hide;
/*     */   private final ImagePanel skip;
/*     */   private final Object animationLock;
/*     */   private boolean closed;
/*     */   private boolean canSkip;
/*     */   
/*     */   public AutoUpdaterFrame(AutoUpdater updater) {
/*  38 */     this.instance = this;
/*     */     
/*  40 */     this.animationLock = new Object();
/*     */     
/*  42 */     setPreferredSize(new Dimension(350, 60));
/*  43 */     setResizable(false);
/*  44 */     setUndecorated(true);
/*  45 */     setBackground(new Color(0, 0, 0, 0));
/*  46 */     setDefaultCloseOperation(3);
/*     */     
/*  48 */     this.pan = new JPanel()
/*     */       {
/*     */         private static final long serialVersionUID = -8469500310564854471L;
/*  51 */         protected final Insets insets = new Insets(5, 10, 10, 10);
/*  52 */         protected final Color background = new Color(255, 255, 255, 220);
/*     */ 
/*     */         
/*     */         public void paintComponent(Graphics g0) {
/*  56 */           Graphics2D g = (Graphics2D)g0;
/*  57 */           int arc = 16;
/*     */           
/*  59 */           g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */           
/*  61 */           g.setColor(this.background);
/*  62 */           g.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
/*     */           
/*  64 */           g.setColor(AutoUpdaterFrame.this.border);
/*  65 */           for (int x = 1; x < 2; x++) {
/*  66 */             g.drawRoundRect(x - 1, x - 1, getWidth() - 2 * x + 1, getHeight() - 2 * x + 1, arc, arc);
/*     */           }
/*     */           
/*  69 */           Color shadow = U.shiftAlpha(Color.gray, -200);
/*     */           
/*  71 */           for (int i = 2;; i++) {
/*  72 */             shadow = U.shiftAlpha(shadow, -8);
/*     */             
/*  74 */             if (shadow.getAlpha() == 0) {
/*     */               break;
/*     */             }
/*  77 */             g.setColor(shadow);
/*  78 */             g.drawRoundRect(i - 1, i - 1, getWidth() - 2 * i + 1, getHeight() - 2 * i + 1, arc - 2 * i + 1, arc - 2 * i + 1);
/*     */           } 
/*     */ 
/*     */           
/*  82 */           g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
/*     */           
/*  84 */           super.paintComponent(g0);
/*     */         }
/*     */ 
/*     */         
/*     */         public Insets getInsets() {
/*  89 */           return this.insets;
/*     */         }
/*     */       };
/*  92 */     this.pan.setOpaque(false);
/*  93 */     this.pan.setLayout(new BorderLayout());
/*  94 */     add(this.pan);
/*     */     
/*  96 */     this.titlepan = new JPanel();
/*  97 */     this.titlepan.setOpaque(false);
/*  98 */     this.titlepan.setLayout(new FlowLayout(2));
/*     */     
/* 100 */     this.hide = new ImagePanel("hide.png", 0.75F, 0.5F, false)
/*     */       {
/*     */         private static final long serialVersionUID = 513294577418505533L;
/*     */         
/*     */         protected boolean onClick() {
/* 105 */           if (!super.onClick()) {
/* 106 */             return false;
/*     */           }
/* 108 */           AutoUpdaterFrame.this.instance.setExtendedState(1);
/* 109 */           return true;
/*     */         }
/*     */       };
/* 112 */     this.hide.setToolTipText(Localizable.get("autoupdater.buttons.hide"));
/* 113 */     this.titlepan.add((Component)this.hide);
/*     */     
/* 115 */     this.skip = new ImagePanel("skip.png", 0.75F, 0.5F, false)
/*     */       {
/*     */         private static final long serialVersionUID = 513294577418505533L;
/*     */         
/*     */         protected boolean onClick() {
/* 120 */           if (!super.onClick() || !AutoUpdaterFrame.this.canSkip) {
/* 121 */             return false;
/*     */           }
/* 123 */           AutoUpdaterFrame.this.handleClose();
/* 124 */           return true;
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */     
/* 130 */     this.pan.add("East", this.titlepan);
/*     */     
/* 132 */     this.label = new LocalizableLabel("autoupdater.preparing");
/* 133 */     this.label.setOpaque(false);
/* 134 */     this.pan.add("West", (Component)this.label);
/*     */     
/* 136 */     setCanSkip(true);
/*     */     
/* 138 */     updater.getLauncher().getDownloader().addListener(this);
/*     */     
/* 140 */     pack();
/* 141 */     setLocationRelativeTo((Component)null);
/* 142 */     requestFocusInWindow();
/*     */   }
/*     */   
/*     */   private void hideButtons() {
/* 146 */     this.skip.hide();
/*     */   }
/*     */   
/*     */   private void closeFrame() {
/* 150 */     if (this.closed) {
/* 151 */       dispose();
/*     */     } else {
/* 153 */       float opacity = 1.0F;
/*     */       
/* 155 */       synchronized (this.animationLock) {
/*     */         try {
/* 157 */           setOpacity(opacity);
/* 158 */         } catch (Throwable throwable) {}
/*     */ 
/*     */         
/* 161 */         while (opacity > 0.0F) {
/* 162 */           opacity = (float)(opacity - 0.005D);
/* 163 */           if (opacity < 0.0F) {
/* 164 */             opacity = 0.0F;
/*     */           }
/*     */           try {
/* 167 */             setOpacity(opacity);
/* 168 */           } catch (Throwable throwable) {}
/*     */           
/* 170 */           U.sleepFor(1L);
/*     */         } 
/*     */         
/* 173 */         dispose();
/*     */       } 
/*     */     } 
/* 176 */     this.closed = true;
/*     */   }
/*     */   
/*     */   public boolean canSkip() {
/* 180 */     return this.canSkip;
/*     */   }
/*     */   
/*     */   private void setCanSkip(boolean b) {
/* 184 */     this.canSkip = b;
/*     */     
/* 186 */     if (!b)
/* 187 */       hideButtons(); 
/*     */   }
/*     */   
/*     */   private void handleClose() {
/* 191 */     this.label.setText("autoupdater.opening", new Object[] { Double.valueOf(TLauncher.getVersion()) });
/* 192 */     closeFrame();
/*     */   }
/*     */   
/*     */   public boolean isClosed() {
/* 196 */     return this.closed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdateError(Update u, Throwable e) {
/* 201 */     if (this.closed)
/*     */       return; 
/* 203 */     if (!Files.isWritable(FileUtil.getRunningJar().getParentFile().toPath())) {
/* 204 */       if (Alert.showErrorMessage("updater.error.title", "updater.access.denied.message", "updater.button.download.handle", "review.button.close") == 0)
/*     */       {
/* 206 */         OS.openLink(OS.is(new OS[] { OS.WINDOWS }) ? u.getExeLinks().get(0) : u.getJarLinks().get(0));
/*     */       }
/* 208 */       TLauncher.kill();
/* 209 */     } else if (Alert.showLocQuestion("updater.error.title", "updater.download-error", e)) {
/* 210 */       UpdateUIListener.openUpdateLink(u.getlastDownloadedLink());
/*     */     } 
/*     */     
/* 213 */     handleClose();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdateDownloading(Update u) {
/* 218 */     this.label.setText("autoupdater.downloading", new Object[] { Double.valueOf(u.getVersion()) });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdateDownloadError(Update u, Throwable e) {
/* 223 */     onUpdateError(u, e);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdateReady(Update u) {
/* 228 */     setCanSkip(true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onUpdateApplying(Update u) {}
/*     */ 
/*     */   
/*     */   public void onUpdateApplyError(Update u, Throwable e) {
/* 237 */     if (Alert.showLocQuestion("updater.save-error", e)) {
/* 238 */       UpdateUIListener.openUpdateLink(u.getlastDownloadedLink());
/*     */     }
/*     */   }
/*     */   
/*     */   public void onUpdaterRequesting(Updater u) {
/* 243 */     this.label.setText("autoupdater.requesting");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdaterErrored(Updater.SearchFailed failed) {
/* 248 */     handleClose();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdaterSucceeded(Updater.SearchSucceeded succeeded) {
/* 253 */     Update update = succeeded.getResponse();
/* 254 */     if (!FileUtil.checkFreeSpace(FileUtil.getRunningJar(), FileUtil.SIZE_100.longValue()) && update
/* 255 */       .isApplicable()) {
/* 256 */       showSpaceMessage(update);
/* 257 */       update.setFreeSpaceEnough(false);
/*     */     } 
/*     */     
/* 260 */     if (update.isApplicable()) {
/* 261 */       UpdaterFormController controller = new UpdaterFormController(update, TLauncher.getInstance().getConfiguration());
/* 262 */       update.setUserChoose(controller.getResult());
/*     */     } 
/* 264 */     if (update.isApplicable()) {
/* 265 */       update.addListener(this);
/*     */     } else {
/* 267 */       this.label.setText("autoupdater.opening");
/* 268 */       handleClose();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void showSpaceMessage(Update update) {
/* 278 */     Alert.showMessage(Localizable.get("launcher.update.title"), 
/* 279 */         Localizable.get("launcher.update.no.space").replace("disk", FileUtil.getRunningJar().toPath().getRoot().toString()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDownloaderStart(Downloader d, int files) {
/* 284 */     setCanSkip(false);
/*     */   }
/*     */   
/*     */   public void onDownloaderAbort(Downloader d) {}
/*     */   
/*     */   public void onDownloaderProgress(Downloader d, double progress, double speed, double alreadyDownloaded) {}
/*     */   
/*     */   public void onDownloaderFileComplete(Downloader d, Downloadable file) {}
/*     */   
/*     */   public void onDownloaderComplete(Downloader d) {}
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\u\\updater\AutoUpdaterFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */