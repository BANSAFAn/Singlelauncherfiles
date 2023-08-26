/*     */ package org.tlauncher.tlauncher.ui;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Point;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.plaf.ProgressBarUI;
/*     */ import org.tlauncher.tlauncher.downloader.DownloaderListener;
/*     */ import org.tlauncher.tlauncher.listeners.auth.LoginProcessListener;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.browser.BrowserHolder;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.login.LoginForm;
/*     */ import org.tlauncher.tlauncher.ui.progress.login.LauncherProgress;
/*     */ import org.tlauncher.tlauncher.ui.scenes.AccountEditorScene;
/*     */ import org.tlauncher.tlauncher.ui.scenes.AdditionalHostServerScene;
/*     */ import org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene;
/*     */ import org.tlauncher.tlauncher.ui.scenes.DefaultScene;
/*     */ import org.tlauncher.tlauncher.ui.scenes.MicrosoftAuthScene;
/*     */ import org.tlauncher.tlauncher.ui.scenes.ModpackEnitityScene;
/*     */ import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
/*     */ import org.tlauncher.tlauncher.ui.scenes.PseudoScene;
/*     */ import org.tlauncher.tlauncher.ui.scenes.SettingsScene;
/*     */ import org.tlauncher.tlauncher.ui.scenes.VersionManagerScene;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane;
/*     */ import org.tlauncher.tlauncher.ui.swing.progress.ProgressBarPanel;
/*     */ import org.tlauncher.tlauncher.ui.ui.FancyProgressBar;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ 
/*     */ 
/*     */ public class MainPane
/*     */   extends ExtendedLayeredPane
/*     */ {
/*     */   private static final long serialVersionUID = 1577003831121606643L;
/*  37 */   public static final Dimension SIZE = new Dimension(1050, 655);
/*     */   
/*     */   public final BrowserHolder browser;
/*     */   public final DefaultScene defaultScene;
/*     */   public final VersionManagerScene versionManager;
/*     */   public final SettingsScene settingsScene;
/*     */   public final ModpackScene modpackScene;
/*     */   public final CompleteSubEntityScene completeSubEntityScene;
/*     */   public final ModpackEnitityScene modpackEnitityScene;
/*     */   public final AdditionalHostServerScene additionalHostServerScene;
/*     */   public final SideNotifier notifier;
/*     */   public final AccountEditorScene accountEditor;
/*     */   public final MicrosoftAuthScene microsoftAuthScene;
/*     */   private final TLauncherFrame rootFrame;
/*     */   private PseudoScene scene;
/*     */   private final ProgressBarPanel barPanel;
/*     */   
/*     */   MainPane(TLauncherFrame frame) {
/*  55 */     super(null);
/*  56 */     setPreferredSize(SIZE);
/*  57 */     setMaximumSize(SIZE);
/*  58 */     this.rootFrame = frame;
/*     */     
/*  60 */     this.browser = BrowserHolder.getInstance();
/*  61 */     this.browser.setPane(this);
/*  62 */     add((Component)this.browser);
/*     */     
/*  64 */     this.notifier = new SideNotifier();
/*  65 */     this.notifier.setLocation(10, 10);
/*  66 */     this.notifier.setSize(32, 32);
/*  67 */     add((Component)this.notifier);
/*     */     
/*  69 */     this.defaultScene = new DefaultScene(this);
/*  70 */     add((Component)this.defaultScene);
/*     */     
/*  72 */     this.versionManager = new VersionManagerScene(this);
/*  73 */     add((Component)this.versionManager);
/*     */     
/*  75 */     this.modpackScene = new ModpackScene(this);
/*  76 */     add((Component)this.modpackScene);
/*     */     
/*  78 */     this.completeSubEntityScene = new CompleteSubEntityScene(this);
/*  79 */     add((Component)this.completeSubEntityScene);
/*     */     
/*  81 */     this.modpackEnitityScene = new ModpackEnitityScene(this);
/*  82 */     add((Component)this.modpackEnitityScene);
/*     */     
/*  84 */     this.additionalHostServerScene = new AdditionalHostServerScene(this);
/*  85 */     add((Component)this.additionalHostServerScene);
/*  86 */     this.microsoftAuthScene = new MicrosoftAuthScene(this);
/*  87 */     add((Component)this.microsoftAuthScene);
/*     */     
/*  89 */     LauncherProgress bar = new LauncherProgress();
/*  90 */     bar.setBorder(BorderFactory.createEmptyBorder());
/*  91 */     bar.setUI((ProgressBarUI)new FancyProgressBar(ImageCache.getBufferedImage("login-progress-bar.png")));
/*  92 */     bar.setOpaque(false);
/*  93 */     this
/*  94 */       .barPanel = new ProgressBarPanel(ImageCache.getNativeIcon("speed-icon.png"), ImageCache.getNativeIcon("files-icon.png"), bar);
/*     */     
/*  96 */     this.defaultScene.loginForm.addLoginProcessListener((LoginProcessListener)this.barPanel);
/*  97 */     TLauncher.getInstance().getDownloader().addListener((DownloaderListener)this.barPanel);
/*  98 */     add((Component)this.barPanel);
/*     */     
/* 100 */     this.accountEditor = new AccountEditorScene(this);
/* 101 */     add((Component)this.accountEditor);
/*     */     
/* 103 */     this.settingsScene = new SettingsScene(this);
/* 104 */     add((Component)this.settingsScene);
/*     */     
/* 106 */     setScene((PseudoScene)this.defaultScene, false);
/*     */     
/* 108 */     addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentResized(ComponentEvent e) {
/* 111 */             MainPane.this.onResize();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public PseudoScene getScene() {
/* 117 */     return this.scene;
/*     */   }
/*     */   
/*     */   public void setScene(PseudoScene scene) {
/* 121 */     setScene(scene, true);
/*     */   }
/*     */   
/*     */   private void setScene(PseudoScene newscene, boolean animate) {
/* 125 */     if (newscene == null) {
/* 126 */       throw new NullPointerException();
/*     */     }
/* 128 */     if (newscene.equals(this.scene)) {
/*     */       return;
/*     */     }
/* 131 */     for (Component comp : getComponents()) {
/* 132 */       if (newscene != this.additionalHostServerScene)
/* 133 */       { if (!comp.equals(newscene) && comp instanceof PseudoScene && comp != this.defaultScene) {
/* 134 */           ((PseudoScene)comp).setShown(false, animate);
/*     */         } }
/* 136 */       else if (!comp.equals(newscene) && comp instanceof PseudoScene)
/* 137 */       { ((PseudoScene)comp).setShown(false, animate); } 
/* 138 */     }  this.scene = newscene;
/* 139 */     this.scene.setShown(true);
/*     */     
/* 141 */     this.browser.setBrowserContentShown("scene", this.scene.equals(this.defaultScene));
/*     */   }
/*     */   
/*     */   public void openDefaultScene() {
/* 145 */     setScene((PseudoScene)this.defaultScene);
/*     */   }
/*     */   
/*     */   public TLauncherFrame getRootFrame() {
/* 149 */     return this.rootFrame;
/*     */   }
/*     */   
/*     */   public ProgressBarPanel getProgress() {
/* 153 */     return this.barPanel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onResize() {
/* 158 */     this.browser.onResize();
/*     */     
/* 160 */     this.barPanel.setBounds(0, getHeight() - ProgressBarPanel.SIZE.height - LoginForm.LOGIN_SIZE.height, ProgressBarPanel.SIZE.width, ProgressBarPanel.SIZE.height);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Point getLocationOf(Component comp) {
/* 166 */     return SwingUtil.getRelativeLocation((Component)this, comp);
/*     */   }
/*     */   
/*     */   public void openAccountEditor() {
/* 170 */     setScene((PseudoScene)this.accountEditor);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\MainPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */