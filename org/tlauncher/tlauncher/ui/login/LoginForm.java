/*     */ package org.tlauncher.tlauncher.ui.login;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FlowLayout;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Box;
/*     */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*     */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*     */ import org.tlauncher.tlauncher.downloader.Downloader;
/*     */ import org.tlauncher.tlauncher.downloader.DownloaderListener;
/*     */ import org.tlauncher.tlauncher.entity.server.RemoteServer;
/*     */ import org.tlauncher.tlauncher.entity.server.SiteServer;
/*     */ import org.tlauncher.tlauncher.listeners.auth.LoginProcessListener;
/*     */ import org.tlauncher.tlauncher.listeners.auth.ValidateAccountToken;
/*     */ import org.tlauncher.tlauncher.managers.VersionManager;
/*     */ import org.tlauncher.tlauncher.managers.VersionManagerListener;
/*     */ import org.tlauncher.tlauncher.managers.popup.menu.HotServerManager;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Authenticator;
/*     */ import org.tlauncher.tlauncher.minecraft.crash.Crash;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.MainPane;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*     */ import org.tlauncher.tlauncher.ui.block.BlockablePanel;
/*     */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*     */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*     */ import org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.login.buttons.ButtonPanel;
/*     */ import org.tlauncher.tlauncher.ui.login.buttons.PlayButton;
/*     */ import org.tlauncher.tlauncher.ui.scenes.DefaultScene;
/*     */ import org.tlauncher.tlauncher.ui.settings.SettingsPanel;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.async.LoopedThread;
/*     */ 
/*     */ 
/*     */ public class LoginForm
/*     */   extends CenterPanel
/*     */   implements MinecraftListener, AuthenticatorListener, VersionManagerListener, DownloaderListener
/*     */ {
/*  49 */   public static final Dimension LOGIN_SIZE = new Dimension(1050, 70);
/*     */   
/*     */   public static final String LOGIN_BLOCK = "login";
/*     */   
/*     */   public static final String REFRESH_BLOCK = "refresh";
/*     */   
/*     */   public static final String LAUNCH_BLOCK = "launch";
/*     */   
/*     */   public static final String AUTH_BLOCK = "auth";
/*     */   
/*     */   public static final String UPDATER_BLOCK = "update";
/*     */   
/*     */   public static final String DOWNLOADER_BLOCK = "download";
/*     */   
/*     */   public final DefaultScene scene;
/*  64 */   private final List<LoginStateListener> stateListeners = Collections.synchronizedList(new ArrayList<>()); public final MainPane pane; public final VersionComboBox versions; public final PlayButton play; public final BlockablePanel playPanel; public final ButtonPanel buttons; public final AccountComboBox accountComboBox; public final VersionPanel versionPanel; public final AccountPanel accountPanel;
/*  65 */   private final List<LoginProcessListener> processListeners = Collections.synchronizedList(new ArrayList<>());
/*     */   
/*     */   private final SettingsPanel settings;
/*     */   private final StartThread startThread;
/*     */   private final StopThread stopThread;
/*  70 */   private LoginState state = LoginState.STOPPED;
/*     */   private RemoteServer server;
/*     */   
/*     */   public LoginForm(DefaultScene scene) {
/*  74 */     super(noInsets);
/*  75 */     setSize(LOGIN_SIZE);
/*  76 */     setMaximumSize(LOGIN_SIZE);
/*  77 */     setOpaque(true);
/*  78 */     setBackground(new Color(113, 169, 76));
/*     */     
/*  80 */     this.scene = scene;
/*  81 */     this.pane = scene.getMainPane();
/*     */     
/*  83 */     this.settings = scene.settingsForm;
/*     */     
/*  85 */     this.startThread = new StartThread();
/*  86 */     this.stopThread = new StopThread();
/*     */     
/*  88 */     setLayout(new FlowLayout(0, 0, 0));
/*  89 */     add(Box.createHorizontalStrut(19));
/*     */     
/*  91 */     this.play = new PlayButton(this);
/*  92 */     this.accountPanel = new AccountPanel(this, this.global.getBoolean("chooser.type.account"));
/*  93 */     this.buttons = new ButtonPanel(this);
/*  94 */     this.playPanel = new BlockablePanel();
/*  95 */     this.versionPanel = new VersionPanel(this);
/*     */     
/*  97 */     this.accountPanel.setPreferredSize(new Dimension(246, 70));
/*  98 */     add((Component)this.accountPanel);
/*  99 */     add(Box.createHorizontalStrut(20));
/*     */     
/* 101 */     this.versionPanel.setPreferredSize(new Dimension(246, 70));
/* 102 */     add((Component)this.versionPanel);
/* 103 */     add(Box.createHorizontalStrut(20));
/*     */     
/* 105 */     this.playPanel.setLayout(new BorderLayout(0, 0));
/* 106 */     this.playPanel.setPreferredSize(new Dimension(240, 70));
/* 107 */     this.playPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 8, 0));
/* 108 */     this.playPanel.add((Component)this.play);
/* 109 */     add((Component)this.playPanel);
/* 110 */     add(Box.createHorizontalStrut(19));
/*     */     
/* 112 */     this.buttons.setPreferredSize(new Dimension(240, 70));
/* 113 */     add((Component)this.buttons);
/*     */     
/* 115 */     this.versions = this.versionPanel.version;
/* 116 */     this.accountComboBox = this.accountPanel.accountComboBox;
/*     */     
/* 118 */     this.processListeners.add(this.settings);
/* 119 */     this.processListeners.add(this.versionPanel);
/* 120 */     this.processListeners.add(this.versions);
/* 121 */     this.processListeners.add(new ValidateAccountToken());
/*     */     
/* 123 */     this.stateListeners.add(this.play);
/* 124 */     this.tlauncher.getVersionManager().addListener(this);
/* 125 */     this.tlauncher.getDownloader().addListener(this);
/*     */   }
/*     */   
/*     */   private void runProcess() {
/* 129 */     LoginException error = null;
/* 130 */     boolean success = true;
/* 131 */     synchronized (this.processListeners) {
/* 132 */       this.processListeners.forEach(e -> e.preValidate());
/*     */     } 
/* 134 */     synchronized (this.processListeners) {
/* 135 */       for (LoginProcessListener listener : this.processListeners) {
/*     */         try {
/* 137 */           listener.validatePreGameLaunch();
/* 138 */         } catch (LoginException loginError) {
/* 139 */           log(new Object[] { "Catched an error on a listener" });
/* 140 */           error = loginError;
/*     */         } 
/* 142 */         if (error == null)
/*     */           continue; 
/* 144 */         log(new Object[] { error });
/* 145 */         success = false;
/*     */       } 
/*     */       
/* 148 */       if (success) {
/* 149 */         for (LoginProcessListener listener : this.processListeners)
/* 150 */           listener.loginSucceed(); 
/*     */       } else {
/* 152 */         ((HotServerManager)TLauncher.getInjector().getInstance(HotServerManager.class)).enablePopup();
/* 153 */         for (LoginProcessListener listener : this.processListeners) {
/* 154 */           listener.loginFailed();
/*     */         }
/*     */       } 
/*     */     } 
/* 158 */     if (error != null) {
/* 159 */       log(new Object[] { "Login process has ended with an error." });
/*     */       
/*     */       return;
/*     */     } 
/* 163 */     this.global.store();
/*     */     
/* 165 */     log(new Object[] { "Login was OK. Trying to launch now." });
/*     */     
/* 167 */     boolean force = this.versionPanel.forceupdate.isSelected();
/* 168 */     changeState(LoginState.LAUNCHING);
/* 169 */     this.tlauncher.launch(this, this.server, force);
/* 170 */     this.server = null;
/*     */     
/* 172 */     this.versionPanel.forceupdate.setSelected(false);
/*     */   }
/*     */   
/*     */   private void stopProcess() {
/* 176 */     while (!this.tlauncher.isLauncherWorking()) {
/* 177 */       log(new Object[] { "waiting for launcher" });
/* 178 */       U.sleepFor(500L);
/*     */     } 
/*     */     
/* 181 */     changeState(LoginState.STOPPING);
/* 182 */     this.tlauncher.getLauncher().stop();
/*     */   }
/*     */   
/*     */   public void startLauncher() {
/* 186 */     if (Blocker.isBlocked((Blockable)this))
/*     */       return; 
/* 188 */     this.startThread.iterate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startLauncher(RemoteServer server) {
/* 197 */     this.server = server;
/* 198 */     startLauncher();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startLauncher(SiteServer server) {
/* 207 */     if (server == null) {
/* 208 */       log(new Object[] { "version is null" });
/*     */       return;
/*     */     } 
/* 211 */     boolean find = false;
/* 212 */     for (int i = 0; i < this.versions.getModel().getSize(); i++) {
/* 213 */       if (((VersionSyncInfo)this.versions.getModel().getElementAt(i)).getID().equals(server.getVersion())) {
/* 214 */         this.versions.setSelectedValue(this.versions.getModel().getElementAt(i));
/* 215 */         find = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 219 */     if (!find) {
/* 220 */       Alert.showLocMessage("version.do.not.find");
/*     */       return;
/*     */     } 
/* 223 */     this.server = (RemoteServer)server;
/* 224 */     startLauncher();
/*     */   }
/*     */   
/*     */   public void stopLauncher() {
/* 228 */     this.stopThread.iterate();
/*     */   }
/*     */   
/*     */   private void changeState(LoginState state) {
/* 232 */     if (state == null) {
/* 233 */       throw new NullPointerException();
/*     */     }
/* 235 */     if (this.state == state) {
/*     */       return;
/*     */     }
/* 238 */     this.state = state;
/*     */     
/* 240 */     for (LoginStateListener listener : this.stateListeners) {
/* 241 */       listener.loginStateChanged(state);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void block(Object reason) {
/* 247 */     Blocker.block(reason, new Blockable[] { (Blockable)this.settings, (Blockable)this.play, (Blockable)this.versionPanel, (Blockable)this.accountPanel });
/*     */   }
/*     */ 
/*     */   
/*     */   public void unblock(Object reason) {
/* 252 */     Blocker.unblock(reason, new Blockable[] { (Blockable)this.settings, (Blockable)this.play, (Blockable)this.versionPanel, (Blockable)this.accountPanel });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDownloaderStart(Downloader d, int files) {
/* 257 */     Blocker.block((Blockable)this, "download");
/* 258 */     if (this.play.getState() == PlayButton.PlayButtonState.CANCEL) {
/* 259 */       this.play.unblock("downloading finished");
/*     */     }
/*     */   }
/*     */   
/*     */   public void onDownloaderAbort(Downloader d) {
/* 264 */     Blocker.unblock((Blockable)this, "download");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDownloaderProgress(Downloader d, double progress, double speed, double alreadyDownloaded) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDownloaderFileComplete(Downloader d, Downloadable file) {}
/*     */ 
/*     */   
/*     */   public void onDownloaderComplete(Downloader d) {
/* 277 */     Blocker.unblock((Blockable)this, "download");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVersionsRefreshing(VersionManager manager) {
/* 282 */     Blocker.block((Blockable)this, "refresh");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVersionsRefreshingFailed(VersionManager manager) {
/* 287 */     Blocker.unblock((Blockable)this, "refresh");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVersionsRefreshed(VersionManager manager) {
/* 292 */     Blocker.unblock((Blockable)this, "refresh");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAuthPassing(Authenticator auth) {
/* 297 */     Blocker.block((Blockable)this, "auth");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAuthPassingError(Authenticator auth, Exception e) {
/* 302 */     Blocker.unblock((Blockable)this, "auth");
/*     */     
/* 304 */     Throwable cause = e.getCause();
/* 305 */     if (cause != null && e.getCause() instanceof java.io.IOException) {
/*     */       return;
/*     */     }
/* 308 */     throw new LoginException("Cannot auth!");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAuthPassed(Authenticator auth) {
/* 313 */     Blocker.unblock((Blockable)this, "auth");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMinecraftPrepare() {
/* 318 */     Blocker.block((Blockable)this, "launch");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMinecraftAbort() {
/* 323 */     Blocker.unblock((Blockable)this, "launch");
/* 324 */     changeState(LoginState.STOPPED);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMinecraftLaunch() {
/* 329 */     changeState(LoginState.LAUNCHED);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMinecraftClose() {
/* 334 */     Blocker.unblock((Blockable)this, "launch");
/* 335 */     changeState(LoginState.STOPPED);
/*     */     
/* 337 */     this.tlauncher.getVersionManager().startRefresh(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMinecraftError(Throwable e) {
/* 342 */     Blocker.unblock((Blockable)this, "launch");
/* 343 */     changeState(LoginState.STOPPED);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMinecraftKnownError(MinecraftException e) {
/* 348 */     Blocker.unblock((Blockable)this, "launch");
/* 349 */     changeState(LoginState.STOPPED);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMinecraftCrash(Crash crash) {
/* 354 */     Blocker.unblock((Blockable)this, "launch");
/* 355 */     changeState(LoginState.STOPPED);
/*     */   }
/*     */   
/*     */   public void removeLoginProcessListener(LoginProcessListener listener) {
/* 359 */     this.processListeners.remove(listener);
/*     */   }
/*     */   
/*     */   public void addLoginProcessListener(LoginProcessListener listener) {
/* 363 */     this.processListeners.add(listener);
/*     */   }
/*     */   
/*     */   public void addLoginProcessListener(LoginProcessListener listener, int position) {
/* 367 */     this.processListeners.add(position, listener);
/*     */   }
/*     */   
/*     */   public enum LoginState {
/* 371 */     LAUNCHING, STOPPING, STOPPED, LAUNCHED;
/*     */   }
/*     */   
/*     */   public static interface LoginStateListener {
/*     */     void loginStateChanged(LoginForm.LoginState param1LoginState);
/*     */   }
/*     */   
/*     */   class StartThread
/*     */     extends LoopedThread {
/*     */     StartThread() {
/* 381 */       startAndWait();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void iterateOnce() {
/*     */       try {
/* 387 */         LoginForm.this.runProcess();
/* 388 */       } catch (Throwable t) {
/* 389 */         if (t instanceof by.gdev.util.excepiton.NotAllowWriteFileOperation) {
/* 390 */           Alert.showErrorHtml("auth.error.title", 
/* 391 */               Localizable.get("auth.error.can.not.write", new Object[] { t.getMessage() }));
/*     */         } else {
/* 393 */           Alert.showError(t);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   class StopThread extends LoopedThread {
/*     */     StopThread() {
/* 401 */       startAndWait();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void iterateOnce() {
/*     */       try {
/* 407 */         LoginForm.this.stopProcess();
/* 408 */       } catch (Throwable t) {
/* 409 */         Alert.showError(t);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\LoginForm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */