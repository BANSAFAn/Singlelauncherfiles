/*     */ package org.tlauncher.tlauncher.ui.modpack;
/*     */ import java.awt.CardLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.io.File;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.DefaultListModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.SpringLayout;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import org.tlauncher.exceptions.ParseModPackException;
/*     */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
/*     */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.explorer.FileChooser;
/*     */ import org.tlauncher.tlauncher.ui.explorer.filters.CommonFilter;
/*     */ import org.tlauncher.tlauncher.ui.explorer.filters.FilesAndExtentionFilter;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableButton;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
/*     */ import org.tlauncher.tlauncher.ui.swing.GameInstallRadioButton;
/*     */ import org.tlauncher.tlauncher.ui.swing.GameRadioTextButton;
/*     */ import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
/*     */ import org.tlauncher.tlauncher.ui.swing.renderer.UserCategoryListRenderer;
/*     */ import org.tlauncher.tlauncher.ui.ui.ModpackComboBoxUI;
/*     */ import org.tlauncher.tlauncher.ui.ui.ModpackScrollBarUI;
/*     */ import org.tlauncher.util.ColorUtil;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.swing.FontTL;
/*     */ 
/*     */ public class ModpackBackupFrame extends TemlateModpackFrame {
/*  53 */   public static final SimpleDateFormat format = new SimpleDateFormat("dd-MM-YYYY-HH_mm_ss"); private static final long serialVersionUID = 6901167695599672717L;
/*     */   private static final String RESTORER = "RESTORER";
/*     */   private static final String BACKUP = "BACKUP";
/*     */   private static final String RECOVER_FOLDER = "backup/modpacks";
/*  57 */   private static final Dimension DEFAULT_SIZE = new Dimension(572, 470);
/*     */   private final ModpackComboBox localModpack;
/*  59 */   private ButtonGroup group = new ButtonGroup();
/*     */   FileChooser explorerRecovery;
/*  61 */   private Color listColor = new Color(237, 249, 255);
/*     */   
/*     */   private JPanel subEntityPanel;
/*     */   final JList<GameEntityDTO> entitiesList;
/*     */   JComboBox<CompleteVersion> modpackBox;
/*     */   final JComboBox<GameType> modpackElementType;
/*  67 */   private CompleteVersion selectedVersion = null;
/*     */   
/*     */   public ModpackBackupFrame(JFrame parent, ModpackComboBox localModpack) {
/*  70 */     super(parent, "modpack.backup.title", DEFAULT_SIZE);
/*  71 */     this.localModpack = localModpack;
/*     */     
/*  73 */     SpringLayout springRecoverer = new SpringLayout();
/*  74 */     SpringLayout springBackup = new SpringLayout();
/*  75 */     SpringLayout springPanel = new SpringLayout();
/*  76 */     CardLayout cardLayout = new CardLayout();
/*  77 */     JPanel panel = new JPanel(springPanel);
/*  78 */     JPanel cardPanel = new JPanel(cardLayout);
/*     */     
/*  80 */     ModpackManager manager = (ModpackManager)TLauncher.getInjector().getInstance(ModpackManager.class);
/*  81 */     JPanel restoredPanel = new JPanel(springRecoverer);
/*  82 */     JPanel backupModpackPanel = new JPanel(springBackup);
/*  83 */     backupModpackPanel.setBackground(Color.WHITE);
/*     */     
/*  85 */     addCenter(panel);
/*     */     try {
/*  87 */       FileUtil.createFolder(FileUtil.getRelative("backup/modpacks").toFile());
/*  88 */     } catch (IOException e1) {
/*  89 */       U.log(new Object[] { e1 });
/*     */     } 
/*     */     
/*  92 */     this.explorerRecovery = (FileChooser)TLauncher.getInjector().getInstance(FileChooser.class);
/*  93 */     this.explorerRecovery.setCurrentDirectory(FileUtil.getRelative("backup/modpacks").toFile());
/*  94 */     this.explorerRecovery.setMultiSelectionEnabled(false);
/*  95 */     this.explorerRecovery.setFileFilter((CommonFilter)new FilesAndExtentionFilter("zip, rar", new String[] { "zip", "rar" }));
/*     */     
/*  97 */     this.modpackBox = new JComboBox<>();
/*     */     
/*  99 */     this.entitiesList = new JList<>();
/*     */     
/* 101 */     GameInstallRadioButton gameInstallRadioButton1 = new GameInstallRadioButton("modpack.backup.button.restore");
/* 102 */     GameInstallRadioButton gameInstallRadioButton2 = new GameInstallRadioButton("modpack.backup.button.backup");
/* 103 */     final UpdaterButton doButton = new UpdaterButton(BLUE_COLOR, ColorUtil.BLUE_MODPACK_BUTTON_UP, "modpack.backup.button.do");
/*     */     
/* 105 */     UpdaterButton chooseFile = new UpdaterButton(ColorUtil.COLOR_215, "explorer.title");
/*     */     
/* 107 */     final LocalizableLabel description = new LocalizableLabel("modpack.backup.message.info");
/* 108 */     LocalizableLabel informationBold = new LocalizableLabel("modpack.backup.info.label.0");
/* 109 */     UpdaterButton updaterButton1 = new UpdaterButton(BLUE_COLOR, ColorUtil.BLUE_MODPACK_BUTTON_UP, "modpack.backup.down.button.restore");
/*     */     
/* 111 */     ModpackScrollBarUI barUI = new ModpackScrollBarUI()
/*     */       {
/*     */         protected Dimension getMinimumThumbSize() {
/* 114 */           return new Dimension(10, 40);
/*     */         }
/*     */ 
/*     */         
/*     */         public Dimension getMaximumSize(JComponent c) {
/* 119 */           Dimension dim = super.getMaximumSize(c);
/* 120 */           dim.setSize(10.0D, dim.getHeight());
/* 121 */           return dim;
/*     */         }
/*     */ 
/*     */         
/*     */         public Dimension getPreferredSize(JComponent c) {
/* 126 */           Dimension dim = super.getPreferredSize(c);
/* 127 */           dim.setSize(10.0D, dim.getHeight());
/* 128 */           return dim;
/*     */         }
/*     */       };
/*     */     
/* 132 */     barUI.setGapThubm(5);
/* 133 */     CardLayout cardLayout1 = new CardLayout();
/* 134 */     this.subEntityPanel = new JPanel(cardLayout1);
/* 135 */     this.subEntityPanel.setBackground(this.listColor);
/*     */     
/* 137 */     JScrollPane scroller = new JScrollPane(this.entitiesList, 20, 31);
/*     */     
/* 139 */     this.subEntityPanel.add(scroller, ModpackScene.NOT_EMPTY);
/* 140 */     this.subEntityPanel.add((Component)new EmptyView(GameType.MOD, "modpack.table.empty."), ModpackScene.EMPTY + GameType.MOD
/* 141 */         .toString());
/* 142 */     this.subEntityPanel.add((Component)new EmptyView(GameType.RESOURCEPACK, "modpack.table.empty."), ModpackScene.EMPTY + GameType.RESOURCEPACK);
/*     */     
/* 144 */     this.subEntityPanel.add((Component)new EmptyView(GameType.MAP, "modpack.table.empty."), ModpackScene.EMPTY + GameType.MAP);
/*     */     
/* 146 */     this.subEntityPanel.add((Component)new EmptyView(GameType.SHADERPACK, "modpack.table.empty."), ModpackScene.EMPTY + GameType.SHADERPACK);
/*     */ 
/*     */     
/* 149 */     this.subEntityPanel.add((Component)new EmptyView(GameType.MAP, "modpack.backup.all.elements."), "" + GameType.MODPACK + GameType.MAP);
/*     */     
/* 151 */     this.subEntityPanel.add((Component)new EmptyView(GameType.MOD, "modpack.backup.all.elements."), "" + GameType.MODPACK + GameType.MOD);
/*     */     
/* 153 */     this.subEntityPanel.add((Component)new EmptyView(GameType.RESOURCEPACK, "modpack.backup.all.elements."), "" + GameType.MODPACK + GameType.RESOURCEPACK);
/*     */     
/* 155 */     this.subEntityPanel.add((Component)new EmptyView(GameType.SHADERPACK, "modpack.backup.all.elements."), "" + GameType.MODPACK + GameType.SHADERPACK);
/*     */ 
/*     */     
/* 158 */     scroller.getVerticalScrollBar().setUI((ScrollBarUI)barUI);
/* 159 */     scroller.getVerticalScrollBar().setBackground(this.listColor);
/*     */     
/* 161 */     this.entitiesList.setSelectionModel(new DefaultListSelectionModel() {
/*     */           private static final long serialVersionUID = -4763682115998962110L;
/* 163 */           private int i0 = -1;
/* 164 */           private int i1 = -1;
/*     */           
/*     */           public void setSelectionInterval(int index0, int index1) {
/* 167 */             if (this.i0 == index0 && this.i1 == index1) {
/* 168 */               if (getValueIsAdjusting()) {
/* 169 */                 setValueIsAdjusting(false);
/* 170 */                 setSelection(index0, index1);
/*     */               } 
/*     */             } else {
/* 173 */               this.i0 = index0;
/* 174 */               this.i1 = index1;
/* 175 */               setValueIsAdjusting(false);
/* 176 */               setSelection(index0, index1);
/*     */             } 
/*     */           }
/*     */           
/*     */           private void setSelection(int index0, int index1) {
/* 181 */             ModpackVersionDTO v = (ModpackVersionDTO)ModpackBackupFrame.this.selectedVersion.getModpack().getVersion();
/* 182 */             if (isSelectedIndex(index0)) {
/* 183 */               v.getByType((GameType)ModpackBackupFrame.this.modpackElementType.getSelectedItem())
/* 184 */                 .remove(ModpackBackupFrame.this.entitiesList.getModel().getElementAt(index0));
/* 185 */               removeSelectionInterval(index0, index1);
/*     */             } else {
/* 187 */               GameEntityDTO en = ModpackBackupFrame.this.entitiesList.getModel().getElementAt(index0);
/*     */ 
/*     */               
/* 190 */               List<GameEntityDTO> list = v.getByType((GameType)ModpackBackupFrame.this.modpackElementType.getSelectedItem());
/* 191 */               list.add(en);
/* 192 */               addSelectionInterval(index0, index1);
/*     */             } 
/*     */           }
/*     */         });
/* 196 */     this.entitiesList.setCellRenderer(new DefaultListCellRenderer()
/*     */         {
/*     */           private static final long serialVersionUID = -4696236449762079444L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
/* 205 */             GameEntityDTO entity = (GameEntityDTO)value;
/* 206 */             JLabel label = new JLabel(entity.getName());
/* 207 */             SwingUtil.changeFontFamily(label, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
/* 208 */             label.setHorizontalTextPosition(4);
/* 209 */             label.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
/* 210 */             if (isSelected) {
/* 211 */               label.setIcon((Icon)ImageCache.getIcon("settings-check-box-on.png"));
/* 212 */               label.setIconTextGap(15);
/*     */             } else {
/* 214 */               label.setIconTextGap(14);
/* 215 */               label.setIcon((Icon)ImageCache.getIcon("settings-check-box-off.png"));
/*     */             } 
/* 217 */             label.setPreferredSize(new Dimension(0, 30));
/* 218 */             label.setOpaque(false);
/* 219 */             return label;
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 224 */     this.modpackElementType = new JComboBox<>(new GameType[] { GameType.MOD, GameType.RESOURCEPACK, GameType.MAP, GameType.SHADERPACK });
/*     */     
/* 226 */     this.modpackBox.setRenderer((ListCellRenderer<? super CompleteVersion>)new UserCategoryListRenderer()
/*     */         {
/*     */           private static final long serialVersionUID = 3495501578727016309L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
/* 236 */             return createElement(index, isSelected, ((CompleteVersion)value).getID());
/*     */           }
/*     */         });
/* 239 */     this.modpackElementType.setRenderer((ListCellRenderer<? super GameType>)new UserCategoryListRenderer()
/*     */         {
/*     */           private static final long serialVersionUID = -6697788712085514932L;
/*     */ 
/*     */           
/*     */           public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
/* 245 */             return createElement(index, isSelected, 
/* 246 */                 Localizable.get("modpack.button." + value.toString()));
/*     */           }
/*     */         });
/*     */     
/* 250 */     this.modpackElementType.setUI((ComboBoxUI)new ModpackComboBoxUI()
/*     */         {
/*     */           public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
/* 253 */             paintBackground(g, bounds);
/* 254 */             paintText(g, bounds, 
/* 255 */                 Localizable.get("modpack.button." + ModpackBackupFrame.this.modpackElementType.getSelectedItem().toString()));
/*     */           }
/*     */         });
/* 258 */     this.modpackBox.setUI((ComboBoxUI)new ModpackComboBoxUI()
/*     */         {
/*     */           public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus)
/*     */           {
/* 262 */             paintBackground(g, bounds);
/* 263 */             paintText(g, bounds, ((CompleteVersion)ModpackBackupFrame.this.modpackBox.getSelectedItem()).getID());
/*     */           }
/*     */         });
/* 266 */     this.modpackBox.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ModpackComboxRenderer.LINE));
/* 267 */     this.modpackElementType.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ModpackComboxRenderer.LINE));
/*     */     
/* 269 */     gameInstallRadioButton1.setActionCommand("RESTORER");
/* 270 */     gameInstallRadioButton2.setActionCommand("BACKUP");
/*     */     
/* 272 */     description.setHorizontalAlignment(0);
/* 273 */     description.setVerticalAlignment(1);
/* 274 */     informationBold.setHorizontalAlignment(0);
/*     */     
/* 276 */     this.group.add((AbstractButton)gameInstallRadioButton1);
/* 277 */     this.group.add((AbstractButton)gameInstallRadioButton2);
/*     */     
/* 279 */     SwingUtil.changeFontFamily((JComponent)gameInstallRadioButton1, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
/* 280 */     SwingUtil.changeFontFamily((JComponent)gameInstallRadioButton2, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
/*     */     
/* 282 */     SwingUtil.changeFontFamily((JComponent)chooseFile, FontTL.ROBOTO_REGULAR, 12, Color.BLACK);
/*     */     
/* 284 */     SwingUtil.changeFontFamily((JComponent)description, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
/*     */     
/* 286 */     SwingUtil.changeFontFamily((JComponent)doButton, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
/* 287 */     SwingUtil.changeFontFamily((JComponent)updaterButton1, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
/*     */     
/* 289 */     SwingUtil.changeFontFamily((JComponent)informationBold, FontTL.ROBOTO_BOLD, 14, Color.BLACK);
/* 290 */     SwingUtil.changeFontFamily(this.modpackBox, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
/* 291 */     SwingUtil.changeFontFamily(this.modpackElementType, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
/* 292 */     SwingUtil.changeFontFamily(this.entitiesList, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
/*     */     
/* 294 */     description.setBackground(ColorUtil.COLOR_244);
/* 295 */     informationBold.setBackground(ColorUtil.COLOR_244);
/* 296 */     restoredPanel.setBackground(Color.WHITE);
/* 297 */     this.entitiesList.setBackground(this.listColor);
/*     */     
/* 299 */     description.setOpaque(true);
/* 300 */     informationBold.setOpaque(true);
/*     */     
/* 302 */     springPanel.putConstraint("West", (Component)gameInstallRadioButton1, 0, "West", panel);
/* 303 */     springPanel.putConstraint("East", (Component)gameInstallRadioButton1, 286, "West", panel);
/* 304 */     springPanel.putConstraint("North", (Component)gameInstallRadioButton1, 0, "North", panel);
/* 305 */     springPanel.putConstraint("South", (Component)gameInstallRadioButton1, 58, "North", panel);
/* 306 */     panel.add((Component)gameInstallRadioButton1);
/*     */     
/* 308 */     springPanel.putConstraint("West", (Component)gameInstallRadioButton2, 286, "West", panel);
/* 309 */     springPanel.putConstraint("East", (Component)gameInstallRadioButton2, 572, "West", panel);
/* 310 */     springPanel.putConstraint("North", (Component)gameInstallRadioButton2, 0, "North", panel);
/* 311 */     springPanel.putConstraint("South", (Component)gameInstallRadioButton2, 58, "North", panel);
/* 312 */     panel.add((Component)gameInstallRadioButton2);
/*     */     
/* 314 */     springPanel.putConstraint("West", cardPanel, 0, "West", panel);
/* 315 */     springPanel.putConstraint("East", cardPanel, 0, "East", panel);
/* 316 */     springPanel.putConstraint("North", cardPanel, 58, "North", panel);
/* 317 */     springPanel.putConstraint("South", cardPanel, 0, "South", panel);
/* 318 */     panel.add(cardPanel);
/*     */     
/* 320 */     cardPanel.add("RESTORER", restoredPanel);
/* 321 */     cardPanel.add("BACKUP", backupModpackPanel);
/*     */ 
/*     */     
/* 324 */     springRecoverer.putConstraint("West", (Component)chooseFile, 179, "West", restoredPanel);
/*     */     
/* 326 */     springRecoverer.putConstraint("East", (Component)chooseFile, -177, "East", restoredPanel);
/*     */     
/* 328 */     springRecoverer.putConstraint("North", (Component)chooseFile, 39, "North", restoredPanel);
/* 329 */     springRecoverer.putConstraint("South", (Component)chooseFile, 77, "North", restoredPanel);
/* 330 */     restoredPanel.add((Component)chooseFile);
/*     */     
/* 332 */     springRecoverer.putConstraint("West", (Component)informationBold, 0, "West", restoredPanel);
/* 333 */     springRecoverer.putConstraint("East", (Component)informationBold, 0, "East", restoredPanel);
/* 334 */     springRecoverer.putConstraint("North", (Component)informationBold, 113, "North", restoredPanel);
/* 335 */     springRecoverer.putConstraint("South", (Component)informationBold, 148, "North", restoredPanel);
/* 336 */     restoredPanel.add((Component)informationBold);
/*     */     
/* 338 */     springRecoverer.putConstraint("West", (Component)description, 0, "West", restoredPanel);
/* 339 */     springRecoverer.putConstraint("East", (Component)description, 0, "East", restoredPanel);
/* 340 */     springRecoverer.putConstraint("North", (Component)description, 145, "North", restoredPanel);
/* 341 */     springRecoverer.putConstraint("South", (Component)description, 276, "North", restoredPanel);
/* 342 */     restoredPanel.add((Component)description);
/*     */     
/* 344 */     springRecoverer.putConstraint("West", (Component)updaterButton1, 205, "West", restoredPanel);
/*     */     
/* 346 */     springRecoverer.putConstraint("East", (Component)updaterButton1, 368, "West", restoredPanel);
/*     */     
/* 348 */     springRecoverer.putConstraint("North", (Component)updaterButton1, -68, "South", restoredPanel);
/* 349 */     springRecoverer.putConstraint("South", (Component)updaterButton1, -29, "South", restoredPanel);
/* 350 */     restoredPanel.add((Component)updaterButton1);
/*     */ 
/*     */ 
/*     */     
/* 354 */     springBackup.putConstraint("West", this.modpackBox, 129, "West", backupModpackPanel);
/*     */     
/* 356 */     springBackup.putConstraint("East", this.modpackBox, -127, "East", backupModpackPanel);
/*     */     
/* 358 */     springBackup.putConstraint("North", this.modpackBox, 21, "North", backupModpackPanel);
/* 359 */     springBackup.putConstraint("South", this.modpackBox, 61, "North", backupModpackPanel);
/* 360 */     backupModpackPanel.add(this.modpackBox);
/*     */     
/* 362 */     springBackup.putConstraint("West", this.modpackElementType, 129, "West", backupModpackPanel);
/*     */     
/* 364 */     springBackup.putConstraint("East", this.modpackElementType, -127, "East", backupModpackPanel);
/*     */     
/* 366 */     springBackup.putConstraint("North", this.modpackElementType, 81, "North", backupModpackPanel);
/* 367 */     springBackup.putConstraint("South", this.modpackElementType, 121, "North", backupModpackPanel);
/*     */     
/* 369 */     backupModpackPanel.add(this.modpackElementType);
/*     */     
/* 371 */     springBackup.putConstraint("West", this.subEntityPanel, 129, "West", backupModpackPanel);
/*     */     
/* 373 */     springBackup.putConstraint("East", this.subEntityPanel, -127, "East", backupModpackPanel);
/*     */     
/* 375 */     springBackup.putConstraint("North", this.subEntityPanel, 121, "North", backupModpackPanel);
/* 376 */     springBackup.putConstraint("South", this.subEntityPanel, 271, "North", backupModpackPanel);
/* 377 */     backupModpackPanel.add(this.subEntityPanel);
/*     */     
/* 379 */     springBackup.putConstraint("West", (Component)doButton, 175, "West", backupModpackPanel);
/*     */     
/* 381 */     springBackup.putConstraint("East", (Component)doButton, 398, "West", backupModpackPanel);
/*     */     
/* 383 */     springBackup.putConstraint("North", (Component)doButton, -62, "South", backupModpackPanel);
/* 384 */     springBackup.putConstraint("South", (Component)doButton, -20, "South", backupModpackPanel);
/* 385 */     backupModpackPanel.add((Component)doButton);
/*     */     
/* 387 */     gameInstallRadioButton1.addActionListener(e -> cardLayout.show(cardPanel, e.getActionCommand()));
/* 388 */     gameInstallRadioButton2.addActionListener(e -> {
/*     */           if (localModpack.getItemCount() < 2) {
/*     */             setVisible(false);
/*     */             
/*     */             Alert.showLocMessage("modpack.backup.init");
/*     */             
/*     */             setVisible(true);
/*     */             restoredBackup.setSelected(true);
/*     */             return;
/*     */           } 
/*     */           CompleteVersion defaultVersion = new CompleteVersion();
/*     */           defaultVersion.setID(Localizable.get("modpack.backup.modpack.box"));
/*     */           this.modpackBox.removeAllItems();
/*     */           this.modpackBox.addItem(defaultVersion);
/*     */           for (int i = 1; i < localModpack.getItemCount(); i++) {
/*     */             this.modpackBox.addItem(localModpack.getItemAt(i));
/*     */           }
/*     */           this.modpackBox.setSelectedIndex(0);
/*     */           cardLayout.show(cardPanel, e.getActionCommand());
/*     */         });
/* 408 */     this.modpackBox.addItemListener(e -> {
/*     */           if (1 == e.getStateChange()) {
/*     */             if (this.modpackBox.getSelectedIndex() != 0) {
/*     */               this.selectedVersion = ((CompleteVersion)this.modpackBox.getSelectedItem()).fullCopy(new CompleteVersion());
/*     */             } else {
/*     */               this.selectedVersion = null;
/*     */             } 
/*     */             
/*     */             prepareEntityList();
/*     */           } 
/*     */         });
/*     */     
/* 420 */     this.modpackElementType.addItemListener(e -> {
/*     */           if (1 == e.getStateChange()) {
/*     */             prepareEntityList();
/*     */           }
/*     */         });
/*     */     
/* 426 */     doButton.addActionListener(e -> {
/*     */           FileChooser f = (FileChooser)TLauncher.getInjector().getInstance(FileChooser.class);
/*     */           
/*     */           String name = (this.selectedVersion == null) ? ("modpacks-" + format.format(new Date())) : (this.selectedVersion.getID() + " " + format.format(new Date()));
/*     */           
/*     */           name = name + ".zip";
/*     */           
/*     */           f.setFileFilter((CommonFilter)new FilesAndExtentionFilter("zip format", new String[] { "zip" }));
/*     */           f.setDialogTitle(Localizable.get("console.save.popup"));
/*     */           f.setSelectedFile(new File(FileUtil.getRelative("backup/modpacks").toFile(), name));
/*     */           f.setMultiSelectionEnabled(false);
/*     */           setAlwaysOnTop(false);
/*     */           int res = f.showSaveDialog(this);
/*     */           if (res != 0) {
/*     */             return;
/*     */           }
/*     */           setAlwaysOnTop(true);
/*     */           doButton.setText("modpack.install.process");
/*     */           doButton.setEnabled(false);
/*     */           List<CompleteVersion> list = new ArrayList<>();
/*     */           if (this.selectedVersion == null) {
/*     */             for (int i = 1; i < this.modpackBox.getItemCount(); i++) {
/*     */               list.add(((CompleteVersion)this.modpackBox.getItemAt(i)).fullCopy(new CompleteVersion()));
/*     */             }
/*     */           } else {
/*     */             list.add(this.selectedVersion);
/*     */           } 
/*     */           manager.backupModPack(list, f.getSelectedFile(), new HandleListener()
/*     */               {
/*     */                 public void processError(Exception e)
/*     */                 {
/* 457 */                   doButton.setEnabled(true);
/* 458 */                   doButton.setText("modpack.backup.button.do");
/* 459 */                   ModpackBackupFrame.this.showWarning("modpack.backup.files.error");
/*     */                 }
/*     */ 
/*     */ 
/*     */                 
/*     */                 public void installedSuccess(List modpackNames) {}
/*     */ 
/*     */ 
/*     */                 
/*     */                 public void operationSuccess() {
/* 469 */                   doButton.setEnabled(true);
/* 470 */                   doButton.setText("modpack.backup.button.do");
/* 471 */                   ModpackBackupFrame.this.setVisible(false);
/* 472 */                   Alert.showLocMessageWithoutTitle("modpack.backup.files.do");
/* 473 */                   ModpackBackupFrame.this.setVisible(true);
/*     */                 }
/*     */               });
/*     */         });
/*     */     
/* 478 */     chooseFile.addActionListener(e -> {
/*     */           setAlwaysOnTop(false);
/*     */           if (this.explorerRecovery.showDialog(this) == 0) {
/*     */             chooseFile.setText("explorer.backup.file.chosen");
/*     */             File f = this.explorerRecovery.getSelectedFile();
/*     */             U.log(new Object[] { "selected file " + f.toString() });
/*     */             try {
/*     */               List<String> list = manager.analizeArchiver(f);
/*     */               description.setText(buildDescription(list));
/* 487 */             } catch (ParseModPackException e1) {
/*     */               showWarning("modpack.install.files.error");
/*     */               U.log(new Object[] { e1 });
/*     */             } 
/*     */           } 
/*     */           setAlwaysOnTop(true);
/*     */         });
/* 494 */     updaterButton1.addActionListener(e -> {
/*     */           File f = this.explorerRecovery.getSelectedFile();
/*     */           if (f == null || f.isDirectory()) {
/*     */             showWarning("explorer.error.choose.file");
/*     */             return;
/*     */           } 
/*     */           try {
/*     */             restoreButton.setEnabled(false);
/*     */             restoreButton.setText("modpack.install.process");
/*     */             manager.installModPack(f, new HandleListener()
/*     */                 {
/*     */                   public void processError(Exception e)
/*     */                   {
/* 507 */                     restoreButton.setEnabled(true);
/* 508 */                     ModpackBackupFrame.this.showWarning("modpack.install.files.error");
/*     */                   }
/*     */ 
/*     */ 
/*     */                   
/*     */                   public void operationSuccess() {}
/*     */ 
/*     */ 
/*     */                   
/*     */                   public void installedSuccess(List modpackNames) {
/* 518 */                     restoreButton.setText("modpack.backup.down.button.restore");
/* 519 */                     restoreButton.setEnabled(true);
/* 520 */                     description.setText(ModpackBackupFrame.this.buildDescription(modpackNames));
/* 521 */                     ModpackBackupFrame.this.setVisible(false);
/* 522 */                     Alert.showLocMessage("modpack.install.files.installed");
/*     */                   }
/*     */                 });
/*     */           }
/* 526 */           catch (Exception e1) {
/*     */             U.log(new Object[] { e1 });
/*     */             
/*     */             showWarning("modpack.install.files.error");
/*     */           } 
/*     */         });
/* 532 */     gameInstallRadioButton1.setSelected(true);
/* 533 */     cardLayout.show(cardPanel, "RESTORER");
/*     */   }
/*     */   
/*     */   private void showWarning(String value) {
/* 537 */     setVisible(false);
/* 538 */     Alert.showLocWarning(value);
/* 539 */     setVisible(true);
/*     */   }
/*     */   
/*     */   private void prepareEntityList() {
/* 543 */     DefaultListModel<GameEntityDTO> listModel = new DefaultListModel<>();
/* 544 */     this.entitiesList.setModel(listModel);
/* 545 */     if (this.selectedVersion != null) {
/* 546 */       ModpackVersionDTO version = (ModpackVersionDTO)this.selectedVersion.getModpack().getVersion();
/* 547 */       for (GameEntityDTO en : version.getByType((GameType)this.modpackElementType.getSelectedItem())) {
/* 548 */         listModel.addElement(en);
/*     */       }
/* 550 */       this.entitiesList.setEnabled(true);
/* 551 */       if (this.entitiesList.getModel().getSize() == 0) {
/* 552 */         ((CardLayout)this.subEntityPanel.getLayout()).show(this.subEntityPanel, "" + ModpackScene.EMPTY + this.modpackElementType
/* 553 */             .getSelectedItem());
/*     */       } else {
/* 555 */         ((CardLayout)this.subEntityPanel.getLayout()).show(this.subEntityPanel, ModpackScene.NOT_EMPTY);
/*     */       }
/*     */     
/*     */     }
/* 559 */     else if (containsAnyElement((GameType)this.modpackElementType.getModel().getSelectedItem())) {
/* 560 */       ((CardLayout)this.subEntityPanel.getLayout()).show(this.subEntityPanel, "" + GameType.MODPACK + this.modpackElementType
/* 561 */           .getSelectedItem());
/*     */     } else {
/* 563 */       ((CardLayout)this.subEntityPanel.getLayout()).show(this.subEntityPanel, ModpackScene.EMPTY + this.modpackElementType
/* 564 */           .getSelectedItem());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 569 */     int[] array = new int[listModel.getSize()];
/* 570 */     for (int i = 0; i < listModel.getSize(); i++) {
/* 571 */       array[i] = i;
/*     */     }
/* 573 */     this.entitiesList.setSelectedIndices(array);
/*     */   }
/*     */   
/*     */   private String buildDescription(List<String> list) {
/* 577 */     StringBuilder builder = new StringBuilder();
/* 578 */     builder.append("<html>");
/* 579 */     builder.append("<p style='text-align: center; margin-top:5'>");
/* 580 */     for (String aList : list) {
/* 581 */       builder.append(aList).append("<br>");
/*     */     }
/* 583 */     builder.append("</p></html>");
/* 584 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean containsAnyElement(GameType type) {
/* 596 */     for (ModpackDTO modpackDTO : this.localModpack.getModpacks()) {
/* 597 */       if (((ModpackVersionDTO)modpackDTO.getVersion()).getByType(type).size() > 0) {
/* 598 */         return true;
/*     */       }
/*     */     } 
/* 601 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private class EmptyView
/*     */     extends ExtendedPanel
/*     */   {
/*     */     private static final long serialVersionUID = 9216616637184943829L;
/*     */     
/*     */     public EmptyView(GameType gameType, String nameLoc) {
/* 611 */       setLayout(new BorderLayout());
/* 612 */       LocalizableLabel localizableLabel = new LocalizableLabel(nameLoc + gameType);
/* 613 */       localizableLabel.setHorizontalAlignment(0);
/* 614 */       localizableLabel.setAlignmentY(0.0F);
/* 615 */       SwingUtil.changeFontFamily((JComponent)localizableLabel, FontTL.ROBOTO_BOLD, 14);
/* 616 */       localizableLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
/* 617 */       add((Component)localizableLabel, "Center");
/* 618 */       setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, ColorUtil.COLOR_149));
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface HandleListener {
/*     */     void operationSuccess();
/*     */     
/*     */     void processError(Exception param1Exception);
/*     */     
/*     */     void installedSuccess(List<String> param1List);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\ModpackBackupFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */