/*     */ package org.tlauncher.tlauncher.ui;
/*     */ 
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.name.Names;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.net.URL;
/*     */ import java.util.Locale;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.UIManager;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.configuration.LangConfiguration;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*     */ import org.tlauncher.tlauncher.rmo.Bootstrapper;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.console.Console;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;
/*     */ import org.tlauncher.tlauncher.ui.login.LoginFormHelper;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ 
/*     */ public class TLauncherFrame
/*     */   extends JFrame
/*     */ {
/*  37 */   public static final float fontSize = OS.WINDOWS.isCurrent() ? 12.0F : 14.0F;
/*     */   
/*  39 */   public static final Color BLUE_COLOR = new Color(60, 170, 232);
/*     */   
/*     */   private final TLauncherFrame instance;
/*     */   
/*     */   private final TLauncher tlauncher;
/*     */   
/*     */   private final Configuration settings;
/*     */   
/*     */   private final LangConfiguration lang;
/*     */   public final MainPane mp;
/*     */   private String brand;
/*     */   
/*     */   public TLauncherFrame(TLauncher t) {
/*  52 */     this.instance = this;
/*     */     
/*  54 */     this.tlauncher = t;
/*     */     
/*  56 */     this.settings = t.getConfiguration();
/*  57 */     this.lang = t.getLang();
/*  58 */     if (!this.settings.getLocale().getLanguage().equals((new Locale("zh")).getLanguage())) {
/*  59 */       SwingUtil.initFont((int)fontSize);
/*     */     }
/*  61 */     SwingUtil.setFavicons(this);
/*     */     
/*  63 */     setUILocale();
/*  64 */     setWindowSize();
/*  65 */     setWindowTitle();
/*     */     
/*  67 */     addWindowListener(new WindowAdapter()
/*     */         {
/*     */           public void windowClosing(WindowEvent e)
/*     */           {
/*  71 */             TLauncherFrame.this.instance.setVisible(false);
/*  72 */             TLauncher.kill();
/*     */           }
/*     */         });
/*     */     
/*  76 */     setDefaultCloseOperation(3);
/*     */ 
/*     */ 
/*     */     
/*  80 */     addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentShown(ComponentEvent e) {
/*  83 */             TLauncherFrame.this.instance.validate();
/*  84 */             TLauncherFrame.this.instance.repaint();
/*  85 */             TLauncherFrame.this.instance.toFront();
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void componentHidden(ComponentEvent e) {}
/*     */         });
/*  93 */     addWindowStateListener(e -> {
/*     */           int newState = getExtendedStateFor(e.getNewState());
/*     */           if (newState == -1) {
/*     */             return;
/*     */           }
/*     */           this.settings.set("gui.window", Integer.valueOf(newState));
/*     */         });
/* 100 */     U.setLoadingStep(Bootstrapper.LoadingStep.PREPARING_MAINPANE);
/* 101 */     this.mp = new MainPane(this);
/* 102 */     add((Component)this.mp);
/* 103 */     U.setLoadingStep(Bootstrapper.LoadingStep.POSTINIT_GUI);
/*     */   }
/*     */   
/*     */   public void afterInitProfile() {
/* 107 */     log(new Object[] { "Packing main frame..." });
/* 108 */     pack();
/*     */     
/* 110 */     log(new Object[] { "Resizing main pane..." });
/* 111 */     this.mp.onResize();
/*     */     
/* 113 */     setWindowTitle();
/*     */     
/* 115 */     U.log(new Object[] { this.tlauncher.getUpdater().isClosed() ? "Updater is already closed. Showing up..." : "Waiting for Updater to close." });
/*     */ 
/*     */     
/* 118 */     while (!this.tlauncher.getUpdater().isClosed()) {
/* 119 */       U.sleepFor(100L);
/*     */     }
/* 121 */     U.log(new Object[] { "Visibility set." });
/*     */     
/* 123 */     setLocationRelativeTo((Component)null);
/*     */     
/* 125 */     requestFocusInWindow();
/*     */   }
/*     */   
/*     */   public TLauncher getLauncher() {
/* 129 */     return this.tlauncher;
/*     */   }
/*     */   
/*     */   public Configuration getConfiguration() {
/* 133 */     return this.settings;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateLocales() {
/*     */     try {
/* 139 */       this.tlauncher.reloadLocale();
/* 140 */     } catch (Exception e) {
/* 141 */       log(new Object[] { "Cannot reload settings!", e });
/*     */       
/*     */       return;
/*     */     } 
/* 145 */     ((Console)TLauncher.getInjector().getInstance(Key.get(Console.class, (Annotation)Names.named("console")))).updateLocale();
/* 146 */     LocalizableMenuItem.updateLocales();
/*     */     
/* 148 */     setWindowTitle();
/* 149 */     setUILocale();
/*     */     
/* 151 */     Localizable.updateContainer(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateTitle() {
/* 157 */     this.brand = "" + TLauncher.getVersion();
/*     */   }
/*     */   
/*     */   private void setWindowTitle() {
/* 161 */     updateTitle();
/* 162 */     if ("BETA".equals(TLauncher.getInnerSettings().get("type"))) {
/* 163 */       setTitle(String.format("TLauncher %s Beta", new Object[] { this.brand }));
/*     */     } else {
/* 165 */       setTitle(String.format("TLauncher %s", new Object[] { this.brand }));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setWindowSize() {
/* 170 */     setResizable(false);
/*     */   }
/*     */   
/*     */   private void setUILocale() {
/* 174 */     UIManager.put("OptionPane.yesButtonText", this.lang.nget("ui.yes"));
/* 175 */     UIManager.put("OptionPane.noButtonText", this.lang.nget("ui.no"));
/* 176 */     UIManager.put("OptionPane.cancelButtonText", this.lang.nget("ui.cancel"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     UIManager.put("FileChooser.acceptAllFileFilterText", this.lang.nget("explorer.extension.all"));
/*     */     
/* 184 */     UIManager.put("FileChooser.lookInLabelText", this.lang.nget("explorer.lookin"));
/* 185 */     UIManager.put("FileChooser.saveInLabelText", this.lang.nget("explorer.lookin"));
/*     */     
/* 187 */     UIManager.put("FileChooser.fileNameLabelText", this.lang.nget("explorer.input.filename"));
/* 188 */     UIManager.put("FileChooser.folderNameLabelText", this.lang.nget("explorer.input.foldername"));
/* 189 */     UIManager.put("FileChooser.filesOfTypeLabelText", this.lang.nget("explorer.input.type"));
/*     */     
/* 191 */     UIManager.put("FileChooser.upFolderToolTipText", this.lang.nget("explorer.button.up.tip"));
/* 192 */     UIManager.put("FileChooser.upFolderAccessibleName", this.lang.nget("explorer.button.up"));
/*     */     
/* 194 */     UIManager.put("FileChooser.newFolderToolTipText", this.lang.nget("explorer.button.newfolder.tip"));
/* 195 */     UIManager.put("FileChooser.newFolderAccessibleName", this.lang.nget("explorer.button.newfolder"));
/* 196 */     UIManager.put("FileChooser.newFolderButtonToolTipText", this.lang.nget("explorer.button.newfolder.tip"));
/* 197 */     UIManager.put("FileChooser.newFolderButtonText", this.lang.nget("explorer.button.newfolder"));
/*     */     
/* 199 */     UIManager.put("FileChooser.other.newFolder", this.lang.nget("explorer.button.newfolder.name"));
/* 200 */     UIManager.put("FileChooser.other.newFolder.subsequent", this.lang.nget("explorer.button.newfolder.name"));
/* 201 */     UIManager.put("FileChooser.win32.newFolder", this.lang.nget("explorer.button.newfolder.name"));
/* 202 */     UIManager.put("FileChooser.win32.newFolder.subsequent", this.lang.nget("explorer.button.newfolder.name"));
/*     */     
/* 204 */     UIManager.put("FileChooser.homeFolderToolTipText", this.lang.nget("explorer.button.home.tip"));
/* 205 */     UIManager.put("FileChooser.homeFolderAccessibleName", this.lang.nget("explorer.button.home"));
/*     */     
/* 207 */     UIManager.put("FileChooser.listViewButtonToolTipText", this.lang.nget("explorer.button.list.tip"));
/* 208 */     UIManager.put("FileChooser.listViewButtonAccessibleName", this.lang.nget("explorer.button.list"));
/*     */     
/* 210 */     UIManager.put("FileChooser.detailsViewButtonToolTipText", this.lang.nget("explorer.button.details.tip"));
/* 211 */     UIManager.put("FileChooser.detailsViewButtonAccessibleName", this.lang.nget("explorer.button.details"));
/*     */     
/* 213 */     UIManager.put("FileChooser.viewMenuButtonToolTipText", this.lang.nget("explorer.button.view.tip"));
/* 214 */     UIManager.put("FileChooser.viewMenuButtonAccessibleName", this.lang.nget("explorer.button.view"));
/*     */     
/* 216 */     UIManager.put("FileChooser.newFolderErrorText", this.lang.nget("explorer.error.newfolder"));
/* 217 */     UIManager.put("FileChooser.newFolderErrorSeparator", ": ");
/*     */     
/* 219 */     UIManager.put("FileChooser.newFolderParentDoesntExistTitleText", this.lang.nget("explorer.error.newfolder-nopath"));
/* 220 */     UIManager.put("FileChooser.newFolderParentDoesntExistText", this.lang.nget("explorer.error.newfolder-nopath"));
/*     */     
/* 222 */     UIManager.put("FileChooser.fileDescriptionText", this.lang.nget("explorer.details.file"));
/* 223 */     UIManager.put("FileChooser.directoryDescriptionText", this.lang.nget("explorer.details.dir"));
/*     */     
/* 225 */     UIManager.put("FileChooser.saveButtonText", this.lang.nget("explorer.button.save"));
/* 226 */     UIManager.put("FileChooser.openButtonText", this.lang.nget("explorer.button.open"));
/*     */     
/* 228 */     UIManager.put("FileChooser.saveDialogTitleText", this.lang.nget("explorer.title.save"));
/* 229 */     UIManager.put("FileChooser.openDialogTitleText", this.lang.nget("explorer.title.open"));
/* 230 */     UIManager.put("FileChooser.cancelButtonText", this.lang.nget("explorer.button.cancel"));
/* 231 */     UIManager.put("FileChooser.updateButtonText", this.lang.nget("explorer.button.update"));
/* 232 */     UIManager.put("FileChooser.helpButtonText", this.lang.nget("explorer.button.help"));
/* 233 */     UIManager.put("FileChooser.directoryOpenButtonText", this.lang.nget("explorer.button.open-dir"));
/*     */     
/* 235 */     UIManager.put("FileChooser.saveButtonToolTipText", this.lang.nget("explorer.title.save.tip"));
/* 236 */     UIManager.put("FileChooser.openButtonToolTipText", this.lang.nget("explorer.title.open.tip"));
/* 237 */     UIManager.put("FileChooser.cancelButtonToolTipText", this.lang.nget("explorer.button.cancel.tip"));
/* 238 */     UIManager.put("FileChooser.updateButtonToolTipText", this.lang.nget("explorer.button.update.tip"));
/* 239 */     UIManager.put("FileChooser.helpButtonToolTipText", this.lang.nget("explorer.title.help.tip"));
/* 240 */     UIManager.put("FileChooser.directoryOpenButtonToolTipText", this.lang.nget("explorer.button.open-dir.tip"));
/*     */     
/* 242 */     UIManager.put("FileChooser.viewMenuLabelText", this.lang.nget("explorer.button.view"));
/* 243 */     UIManager.put("FileChooser.refreshActionLabelText", this.lang.nget("explorer.context.refresh"));
/* 244 */     UIManager.put("FileChooser.newFolderActionLabelText", this.lang.nget("explorer.context.newfolder"));
/*     */     
/* 246 */     UIManager.put("FileChooser.listViewActionLabelText", this.lang.nget("explorer.view.list"));
/* 247 */     UIManager.put("FileChooser.detailsViewActionLabelText", this.lang.nget("explorer.view.details"));
/*     */     
/* 249 */     UIManager.put("FileChooser.filesListAccessibleName", this.lang.nget("explorer.view.list.name"));
/* 250 */     UIManager.put("FileChooser.filesDetailsAccessibleName", this.lang.nget("explorer.view.details.name"));
/*     */     
/* 252 */     UIManager.put("FileChooser.renameErrorTitleText", this.lang.nget("explorer.error.rename.title"));
/* 253 */     UIManager.put("FileChooser.renameErrorText", this.lang.nget("explorer.error.rename") + "\n{0}");
/* 254 */     UIManager.put("FileChooser.renameErrorFileExistsText", this.lang.nget("explorer.error.rename-exists"));
/*     */     
/* 256 */     UIManager.put("FileChooser.readOnly", Boolean.FALSE);
/* 257 */     UIManager.put("TabbedPane.contentOpaque", Boolean.valueOf(false));
/*     */     
/* 259 */     UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
/* 260 */     UIManager.put("TabbedPane.tabInsets", new Insets(0, 8, 6, 8));
/*     */     
/* 262 */     UIManager.put("OptionPane.questionIcon", ImageCache.getIcon("qestion-option-panel.png"));
/* 263 */     UIManager.put("OptionPane.warningIcon", ImageCache.getIcon("warning.png"));
/* 264 */     UIManager.put("OptionPane.informationIcon", ImageCache.getIcon("exclamation-point.png"));
/*     */     
/* 266 */     UIManager.put("TabbedPane.foreground", new Color(60, 170, 232));
/* 267 */     UIManager.put("TabbedPane.font", new Font("Roboto", 1, 16));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(int width, int height) {
/* 273 */     if (getWidth() == width && getHeight() == height) {
/*     */       return;
/*     */     }
/* 276 */     if (getExtendedState() != 0) {
/*     */       return;
/*     */     }
/* 279 */     boolean show = isVisible();
/*     */     
/* 281 */     if (show) {
/* 282 */       setVisible(false);
/*     */     }
/*     */     
/* 285 */     super.setSize(width, height);
/*     */     
/* 287 */     if (show) {
/* 288 */       setVisible(true);
/* 289 */       setLocationRelativeTo((Component)null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSize(Dimension d) {
/* 295 */     setSize(d.width, d.height);
/*     */   }
/*     */   
/*     */   private static int getExtendedStateFor(int state) {
/* 299 */     switch (state) {
/*     */       case 0:
/*     */       case 2:
/*     */       case 4:
/*     */       case 6:
/* 304 */         return state;
/*     */     } 
/* 306 */     return -1;
/*     */   }
/*     */   
/*     */   public static URL getRes(String uri) {
/* 310 */     return TLauncherFrame.class.getResource(uri);
/*     */   }
/*     */   
/*     */   private static void log(Object... o) {
/* 314 */     U.log(new Object[] { "[Frame]", o });
/*     */   }
/*     */ 
/*     */   
/*     */   public void showTips() {
/* 319 */     if (this.mp.defaultScene.loginForm.accountPanel.getTypeAccountShow() == Account.AccountType.FREE && this.mp.defaultScene.loginForm.accountPanel.username
/* 320 */       .getUsername() == null)
/* 321 */       this.mp.defaultScene.loginFormHelper.setState(LoginFormHelper.LoginFormHelperState.SHOWN); 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\TLauncherFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */