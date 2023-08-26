/*     */ package org.tlauncher.tlauncher.ui.settings;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import net.minecraft.launcher.versions.ReleaseType;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
/*     */ import org.tlauncher.tlauncher.listeners.auth.LoginProcessListener;
/*     */ import org.tlauncher.tlauncher.managers.VersionLists;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*     */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*     */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*     */ import org.tlauncher.tlauncher.ui.converter.ActionOnLaunchConverter;
/*     */ import org.tlauncher.tlauncher.ui.converter.ConnectionQualityConverter;
/*     */ import org.tlauncher.tlauncher.ui.converter.ConsoleTypeConverter;
/*     */ import org.tlauncher.tlauncher.ui.converter.LocaleConverter;
/*     */ import org.tlauncher.tlauncher.ui.converter.StringConverter;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorComboBox;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorFieldChangeListener;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorFieldHandler;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorFieldListener;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorFileField;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorGroupHandler;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorHandler;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorPair;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorResolutionField;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorTextField;
/*     */ import org.tlauncher.tlauncher.ui.editor.TabbedEditorPanel;
/*     */ import org.tlauncher.tlauncher.ui.explorer.FileChooser;
/*     */ import org.tlauncher.tlauncher.ui.explorer.filters.CommonFilter;
/*     */ import org.tlauncher.tlauncher.ui.explorer.filters.FolderFilter;
/*     */ import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.login.LoginException;
/*     */ import org.tlauncher.tlauncher.ui.scenes.DefaultScene;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.TabbedPane;
/*     */ 
/*     */ public class SettingsPanel
/*     */   extends TabbedEditorPanel
/*     */   implements LoginProcessListener, LocalizableComponent {
/*     */   private static final long serialVersionUID = -6596485925333261093L;
/*     */   private final DefaultScene scene;
/*     */   private final TabbedEditorPanel.EditorPanelTab minecraftTab;
/*     */   public final EditorFieldHandler directory;
/*     */   public final EditorFieldHandler resolution;
/*     */   public final EditorFieldHandler fullscreen;
/*     */   public final EditorFieldHandler javaArgs;
/*     */   public final EditorFieldHandler mcArgs;
/*     */   public final EditorFieldHandler memory;
/*     */   public final EditorFieldHandler statistics;
/*     */   public final EditorGroupHandler versionHandler;
/*     */   private final TabbedEditorPanel.EditorPanelTab tlauncherTab;
/*     */   public final EditorFieldHandler console;
/*     */   public final EditorFieldHandler connQuality;
/*     */   public final EditorFieldHandler launchAction;
/*     */   public final EditorFieldHandler locale;
/*     */   private final TabbedEditorPanel.EditorPanelTab confidentialityTab;
/*     */   private final ExtendedPanel minecraftButtons;
/*     */   private final ExtendedPanel tlauncherTabButtons;
/*     */   private final ExtendedPanel confidentialityTabButtons;
/*     */   private final JPopupMenu popup;
/*     */   private final LocalizableMenuItem infoItem;
/*     */   private final LocalizableMenuItem defaultItem;
/*     */   private EditorHandler selectedHandler;
/*     */   
/*     */   public SettingsPanel(DefaultScene sc) {
/*  92 */     super(tipTheme, new Insets(5, 10, 10, 10));
/*     */     
/*  94 */     this.confidentialityTabButtons = createButton();
/*  95 */     this.tlauncherTabButtons = createButton();
/*  96 */     this.minecraftButtons = createButton();
/*  97 */     if (this.tabPane.getExtendedUI() != null) {
/*  98 */       this.tabPane.getExtendedUI().setTheme(settingsTheme);
/*     */     }
/* 100 */     this.scene = sc;
/*     */     
/* 102 */     FocusListener warning = new FocusListener()
/*     */       {
/*     */         public void focusGained(FocusEvent e) {
/* 105 */           SettingsPanel.this.setMessage("settings.warning");
/*     */         }
/*     */ 
/*     */         
/*     */         public void focusLost(FocusEvent e) {
/* 110 */           SettingsPanel.this.setMessage(null); }
/*     */       };
/* 112 */     FocusListener restart = new FocusListener()
/*     */       {
/*     */         public void focusGained(FocusEvent e) {
/* 115 */           SettingsPanel.this.setMessage("settings.restart");
/*     */         }
/*     */ 
/*     */         
/*     */         public void focusLost(FocusEvent e) {
/* 120 */           SettingsPanel.this.setMessage(null);
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */     
/* 126 */     this.minecraftTab = new TabbedEditorPanel.EditorPanelTab(this, "settings.tab.minecraft");
/* 127 */     this.minecraftTab.setInsets(new Insets(20, 20, 0, 20));
/* 128 */     FileChooser chooser = (FileChooser)TLauncher.getInjector().getInstance(FileChooser.class);
/* 129 */     chooser.setFileFilter((CommonFilter)new FolderFilter());
/* 130 */     this.directory = new EditorFieldHandler("minecraft.gamedir", (JComponent)new EditorFileField("settings.client.gamedir.prompt", chooser), warning);
/*     */     
/* 132 */     this.directory.addListener((EditorFieldListener)new EditorFieldChangeListener()
/*     */         {
/*     */           public void onChange(String oldValue, String newValue)
/*     */           {
/* 136 */             if (!SettingsPanel.this.tlauncher.isReady()) {
/*     */               return;
/*     */             }
/*     */             try {
/* 140 */               ((VersionLists)SettingsPanel.this.tlauncher.getManager().getComponent(VersionLists.class)).updateLocal();
/* 141 */             } catch (IOException e) {
/* 142 */               Alert.showLocError("settings.client.gamedir.noaccess", e);
/*     */               
/*     */               return;
/*     */             } 
/* 146 */             SettingsPanel.this.tlauncher.getVersionManager().asyncRefresh();
/* 147 */             SettingsPanel.this.tlauncher.getProfileManager().asyncRefresh();
/*     */           }
/*     */         });
/* 150 */     this.minecraftTab.add(new EditorPair("settings.client.gamedir.label", new EditorHandler[] { (EditorHandler)this.directory }));
/*     */     
/* 152 */     this
/*     */       
/* 154 */       .resolution = new EditorFieldHandler("minecraft.size", (JComponent)new EditorResolutionField("settings.client.resolution.width", "settings.client.resolution.height", this.global.getDefaultClientWindowSize(), false), restart);
/*     */     
/* 156 */     this.fullscreen = new EditorFieldHandler("minecraft.fullscreen", (JComponent)new EditorCheckBox("settings.client.resolution.fullscreen"));
/*     */     
/* 158 */     this.minecraftTab.add(new EditorPair("settings.client.resolution.label", new EditorHandler[] { (EditorHandler)this.resolution }));
/*     */     
/* 160 */     List<ReleaseType> releaseTypes = ReleaseType.getDefinable();
/* 161 */     List<EditorFieldHandler> versions = new ArrayList<>(releaseTypes.size());
/*     */     
/* 163 */     for (ReleaseType releaseType : ReleaseType.getDefinable()) {
/* 164 */       versions.add(new EditorFieldHandler("minecraft.versions." + releaseType, (JComponent)new EditorCheckBox("settings.versions." + releaseType)));
/*     */     }
/*     */     
/* 167 */     versions.add(new EditorFieldHandler("minecraft.versions.sub." + ReleaseType.SubType.OLD_RELEASE, (JComponent)new EditorCheckBox("settings.versions.sub." + ReleaseType.SubType.OLD_RELEASE)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 176 */     this.versionHandler = new EditorGroupHandler(versions);
/*     */     
/* 178 */     this.minecraftTab.add(new EditorPair("settings.versions.label", versions));
/* 179 */     this.minecraftTab.nextPane();
/*     */     
/* 181 */     this.javaArgs = new EditorFieldHandler("minecraft.javaargs", (JComponent)new EditorTextField("settings.java.args.jvm", true), warning);
/*     */     
/* 183 */     this.mcArgs = new EditorFieldHandler("minecraft.args", (JComponent)new EditorTextField("settings.java.args.minecraft", true), warning);
/*     */ 
/*     */     
/* 186 */     this.minecraftTab.add(new EditorPair("settings.java.args.label", new EditorHandler[] { (EditorHandler)this.javaArgs, (EditorHandler)this.mcArgs }));
/*     */     
/* 188 */     this.minecraftTab.nextPane();
/*     */     
/* 190 */     this.memory = new EditorFieldHandler("minecraft.memory.ram2", (JComponent)new SettingsMemorySlider(), warning);
/*     */     
/* 192 */     this.minecraftTab.add(new EditorPair("settings.java.memory.label", new EditorHandler[] { (EditorHandler)this.memory }));
/*     */     
/* 194 */     add(this.minecraftTab);
/*     */ 
/*     */ 
/*     */     
/* 198 */     this.tlauncherTab = new TabbedEditorPanel.EditorPanelTab(this, "settings.tab.tlauncher");
/* 199 */     this.tlauncherTab.setInsets(CenterPanel.ISETS_20);
/*     */     
/* 201 */     this
/* 202 */       .console = new EditorFieldHandler("gui.console", (JComponent)new EditorComboBox((StringConverter)new ConsoleTypeConverter(), (Object[])ConsoleType.values()));
/* 203 */     new EditorFieldChangeListener()
/*     */       {
/*     */         public void onChange(String oldValue, String newValue) {}
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     this.tlauncherTab.add(new EditorPair("settings.console.label", new EditorHandler[] { (EditorHandler)this.console }));
/*     */     
/* 213 */     this
/* 214 */       .connQuality = new EditorFieldHandler("connection", (JComponent)new EditorComboBox((StringConverter)new ConnectionQualityConverter(), (Object[])ConnectionQuality.values()));
/* 215 */     this.connQuality.addListener((EditorFieldListener)new EditorFieldChangeListener()
/*     */         {
/*     */           public void onChange(String oldValue, String newValue) {
/* 218 */             SettingsPanel.this.tlauncher.getDownloader().setConfiguration(SettingsPanel.this.global.getConnectionQuality());
/*     */           }
/*     */         });
/* 221 */     this.tlauncherTab.add(new EditorPair("settings.connection.label", new EditorHandler[] { (EditorHandler)this.connQuality }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     this
/* 230 */       .launchAction = new EditorFieldHandler("minecraft.onlaunch", (JComponent)new EditorComboBox((StringConverter)new ActionOnLaunchConverter(), (Object[])ActionOnLaunch.values()));
/* 231 */     this.tlauncherTab.add(new EditorPair("settings.launch-action.label", new EditorHandler[] { (EditorHandler)this.launchAction }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 257 */     this.tlauncherTab.nextPane();
/*     */     
/* 259 */     this
/* 260 */       .locale = new EditorFieldHandler("locale", (JComponent)new EditorComboBox((StringConverter)new LocaleConverter(), (Object[])this.global.getLocales()));
/* 261 */     this.locale.addListener((EditorFieldListener)new EditorFieldChangeListener()
/*     */         {
/*     */           public void onChange(String oldvalue, String newvalue) {
/* 264 */             if (SettingsPanel.this.tlauncher.getFrame() != null)
/* 265 */               SettingsPanel.this.tlauncher.getFrame().updateLocales(); 
/*     */           }
/*     */         });
/* 268 */     this.tlauncherTab.add(new EditorPair("settings.lang.label", new EditorHandler[] { (EditorHandler)this.locale }));
/*     */     
/* 270 */     add(this.tlauncherTab);
/*     */     
/* 272 */     this.confidentialityTab = new TabbedEditorPanel.EditorPanelTab(this, "settings.tab.tlauncher");
/* 273 */     this.confidentialityTab.setInsets(CenterPanel.ISETS_20);
/*     */     
/* 275 */     this.statistics = new EditorFieldHandler("gui.statistics.checkbox", (JComponent)new EditorCheckBox("statistics.settings.checkbox.name"));
/*     */ 
/*     */     
/* 278 */     this.confidentialityTab.add(new EditorPair("statistics.settings.title", new EditorHandler[] { (EditorHandler)this.statistics }));
/* 279 */     this.confidentialityTab.nextPane();
/*     */     
/* 281 */     add(this.confidentialityTab);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 290 */     this.tlauncherTab.addVerticalGap(150);
/* 291 */     this.tlauncherTab.addButtons(this.tlauncherTabButtons);
/* 292 */     this.minecraftTab.addButtons(this.minecraftButtons);
/* 293 */     this.confidentialityTab.addVerticalGap(150);
/* 294 */     this.confidentialityTab.addButtons(this.confidentialityTabButtons);
/*     */     
/* 296 */     this.tabPane.addChangeListener(new ChangeListener() {
/* 297 */           private final String aboutBlock = "abouttab";
/*     */ 
/*     */           
/*     */           public void stateChanged(ChangeEvent e) {
/* 301 */             if (SettingsPanel.this.tabPane.getSelectedComponent() instanceof TabbedEditorPanel.EditorScrollPane && 
/* 302 */               !((TabbedEditorPanel.EditorScrollPane)SettingsPanel.this.tabPane.getSelectedComponent()).getTab().getSavingEnabled()) {
/* 303 */               Blocker.blockComponents("abouttab", new Component[] { (Component)SettingsPanel.access$600(this.this$0) });
/* 304 */               Blocker.blockComponents("abouttab", new Component[] { (Component)SettingsPanel.access$700(this.this$0) });
/* 305 */               Blocker.blockComponents("abouttab", new Component[] { (Component)SettingsPanel.access$800(this.this$0) });
/*     */             } else {
/* 307 */               Blocker.unblockComponents("abouttab", new Component[] { (Component)SettingsPanel.access$600(this.this$0) });
/* 308 */               Blocker.unblockComponents("abouttab", new Component[] { (Component)SettingsPanel.access$700(this.this$0) });
/* 309 */               Blocker.unblockComponents("abouttab", new Component[] { (Component)SettingsPanel.access$800(this.this$0) });
/*     */             } 
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 317 */     this.popup = new JPopupMenu();
/*     */     
/* 319 */     this.infoItem = new LocalizableMenuItem("settings.popup.info");
/* 320 */     this.infoItem.setEnabled(false);
/* 321 */     this.popup.add((JMenuItem)this.infoItem);
/*     */     
/* 323 */     this.defaultItem = new LocalizableMenuItem("settings.popup.default");
/* 324 */     this.defaultItem.addActionListener(e -> {
/*     */           if (this.selectedHandler == null) {
/*     */             return;
/*     */           }
/*     */           resetValue(this.selectedHandler);
/*     */         });
/* 330 */     this.popup.add((JMenuItem)this.defaultItem);
/*     */     
/* 332 */     for (EditorHandler handler : this.handlers) {
/* 333 */       Component handlerComponent = handler.getComponent();
/*     */       
/* 335 */       handlerComponent.addMouseListener(new MouseAdapter()
/*     */           {
/*     */             public void mouseClicked(MouseEvent e) {
/* 338 */               if (e.getButton() != 3)
/*     */                 return; 
/* 340 */               SettingsPanel.this.callPopup(e, handler);
/*     */             }
/*     */           });
/*     */     } 
/*     */     
/* 345 */     updateValues();
/* 346 */     updateLocale();
/*     */   }
/*     */   
/*     */   void updateValues() {
/* 350 */     boolean globalUnSaveable = !this.global.isSaveable();
/* 351 */     for (EditorHandler handler : this.handlers) {
/* 352 */       String path = handler.getPath(), value = this.global.get(path);
/*     */       
/* 354 */       handler.updateValue(value);
/* 355 */       setValid(handler, true);
/*     */       
/* 357 */       if (globalUnSaveable || !this.global.isSaveable(path))
/* 358 */         Blocker.block((Blockable)handler, "unsaveable"); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean saveValues() {
/* 363 */     if (!checkValues()) {
/* 364 */       return false;
/*     */     }
/* 366 */     for (EditorHandler handler : this.handlers) {
/* 367 */       String path = handler.getPath(), value = handler.getValue();
/*     */       
/* 369 */       this.global.set(path, value, false);
/*     */       
/* 371 */       handler.onChange(value);
/*     */     } 
/*     */     
/* 374 */     this.global.store();
/*     */     
/* 376 */     return true;
/*     */   }
/*     */   
/*     */   void resetValues() {
/* 380 */     for (EditorHandler handler : this.handlers)
/* 381 */       resetValue(handler); 
/*     */   }
/*     */   
/*     */   void resetValue(EditorHandler handler) {
/* 385 */     String path = handler.getPath();
/*     */     
/* 387 */     if (!this.global.isSaveable(path)) {
/*     */       return;
/*     */     }
/* 390 */     String value = this.global.getDefault(path);
/*     */     
/* 392 */     log(new Object[] { "Resetting:", handler.getClass().getSimpleName(), path, value });
/*     */     
/* 394 */     handler.setValue(value);
/*     */     
/* 396 */     log(new Object[] { "Reset!" });
/*     */   }
/*     */   
/*     */   boolean canReset(EditorHandler handler) {
/* 400 */     String key = handler.getPath();
/*     */     
/* 402 */     return (this.global.isSaveable(key) && this.global.getDefault(handler.getPath()) != null);
/*     */   }
/*     */   
/*     */   void callPopup(MouseEvent e, EditorHandler handler) {
/* 406 */     if (this.popup.isShowing()) {
/* 407 */       this.popup.setVisible(false);
/*     */     }
/* 409 */     defocus();
/*     */     
/* 411 */     int x = e.getX(), y = e.getY();
/* 412 */     this.selectedHandler = handler;
/*     */     
/* 414 */     updateResetMenu();
/* 415 */     this.infoItem.setVariables(new Object[] { handler.getPath() });
/* 416 */     this.popup.show((JComponent)e.getSource(), x, y);
/*     */   }
/*     */ 
/*     */   
/*     */   public void block(Object reason) {
/* 421 */     Blocker.blockComponents((Container)this.minecraftTab, reason);
/* 422 */     updateResetMenu();
/*     */   }
/*     */ 
/*     */   
/*     */   public void unblock(Object reason) {
/* 427 */     Blocker.unblockComponents((Container)this.minecraftTab, reason);
/* 428 */     updateResetMenu();
/*     */   }
/*     */   
/*     */   private void updateResetMenu() {
/* 432 */     if (this.selectedHandler != null) {
/* 433 */       this.defaultItem.setEnabled(!Blocker.isBlocked((Blockable)this.selectedHandler));
/*     */     }
/*     */   }
/*     */   
/*     */   public void validatePreGameLaunch() throws LoginException {
/* 438 */     if (checkValues()) {
/*     */       return;
/*     */     }
/* 441 */     this.scene.setSidePanel(DefaultScene.SidePanel.SETTINGS);
/* 442 */     throw new LoginException("Invalid settings!");
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateLocale() {}
/*     */ 
/*     */   
/*     */   private ExtendedPanel createButton() {
/* 450 */     UpdaterButton updaterButton1 = new UpdaterButton(UpdaterButton.ORANGE_COLOR, "settings.save");
/* 451 */     updaterButton1.setFont(updaterButton1.getFont().deriveFont(1));
/* 452 */     updaterButton1.addActionListener(e -> saveValues());
/*     */     
/* 454 */     UpdaterButton updaterButton2 = new UpdaterButton(UpdaterButton.ORANGE_COLOR, "settings.default");
/* 455 */     updaterButton2.addActionListener(e -> {
/*     */           if (Alert.showLocQuestion("settings.default.warning"))
/*     */             resetValues(); 
/*     */         });
/* 459 */     ImageUdaterButton homeButton = new ImageUdaterButton(ImageUdaterButton.GREEN_COLOR, "home.png");
/* 460 */     homeButton.addActionListener(e -> {
/*     */           updateValues();
/*     */           
/*     */           this.scene.setSidePanel(null);
/*     */         });
/* 465 */     Dimension size = homeButton.getPreferredSize();
/* 466 */     if (size != null) {
/* 467 */       homeButton.setPreferredSize(new Dimension(size.width * 2, size.height));
/*     */     }
/* 469 */     ExtendedPanel buttonPanel = new ExtendedPanel();
/* 470 */     buttonPanel.setLayout(new GridBagLayout());
/* 471 */     GridBagConstraints c = new GridBagConstraints();
/* 472 */     c.fill = 2;
/* 473 */     c.gridy = 0;
/* 474 */     c.gridx = 0;
/* 475 */     c.weightx = 1.0D;
/* 476 */     c.insets = new Insets(0, 0, 0, 10);
/* 477 */     buttonPanel.add((Component)updaterButton1, c);
/* 478 */     c.gridx++;
/* 479 */     buttonPanel.add((Component)updaterButton2, c);
/* 480 */     c.gridx++;
/* 481 */     buttonPanel.add(Box.createHorizontalStrut(20), c);
/* 482 */     c.gridx++;
/* 483 */     buttonPanel.add((Component)homeButton, c);
/*     */     
/* 485 */     return buttonPanel;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\settings\SettingsPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */