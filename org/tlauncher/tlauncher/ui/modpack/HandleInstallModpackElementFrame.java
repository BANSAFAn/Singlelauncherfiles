/*     */ package org.tlauncher.tlauncher.ui.modpack;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.io.File;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SpringLayout;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.explorer.FileChooser;
/*     */ import org.tlauncher.tlauncher.ui.explorer.filters.FilesAndExtentionFilter;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.swing.GameInstallRadioButton;
/*     */ import org.tlauncher.util.ColorUtil;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.async.AsyncThread;
/*     */ import org.tlauncher.util.swing.FontTL;
/*     */ 
/*     */ public class HandleInstallModpackElementFrame extends TemlateModpackFrame {
/*  33 */   private GameType type = GameType.MOD;
/*  34 */   private static final Dimension DEFAULT_SIZE = new Dimension(572, 470);
/*     */   
/*     */   private File[] files;
/*     */   private LocalizableLabel installToModpack;
/*     */   
/*     */   public HandleInstallModpackElementFrame(JFrame parent, CompleteVersion version) {
/*  40 */     super(parent, "modpack.install.handle.title", DEFAULT_SIZE);
/*  41 */     SpringLayout spring = new SpringLayout();
/*  42 */     JPanel panel = new JPanel(spring);
/*  43 */     ButtonGroup group = new ButtonGroup();
/*  44 */     addCenter(panel);
/*     */     
/*  46 */     FileChooser chooser = (FileChooser)TLauncher.getInjector().getInstance(FileChooser.class);
/*  47 */     chooser.setMultiSelectionEnabled(true);
/*  48 */     chooser.setFileFilter((CommonFilter)new FilesAndExtentionFilter("zip,rar,jar", new String[] { "zip", "rar", "jar" }));
/*     */     
/*  50 */     GameInstallRadioButton gameInstallRadioButton1 = new GameInstallRadioButton("modpack.button.mod");
/*  51 */     GameInstallRadioButton gameInstallRadioButton2 = new GameInstallRadioButton("modpack.button.resourcepack");
/*  52 */     GameInstallRadioButton gameInstallRadioButton3 = new GameInstallRadioButton("modpack.button.map");
/*  53 */     GameInstallRadioButton gameInstallRadioButton4 = new GameInstallRadioButton("modpack.button.shaderpack");
/*     */     
/*  55 */     UpdaterButton chooseFiles = new UpdaterButton(ColorUtil.COLOR_215, "modpack.explorer.files");
/*     */     
/*  57 */     this.installToModpack = new LocalizableLabel();
/*     */     
/*  59 */     LocalizableLabel warningLabel = new LocalizableLabel("modpack.install.handle.warning");
/*  60 */     UpdaterButton updaterButton1 = new UpdaterButton(BLUE_COLOR, BLUE_COLOR_UNDER, "loginform.enter.install");
/*     */     
/*  62 */     this.installToModpack.setHorizontalAlignment(0);
/*  63 */     warningLabel.setHorizontalAlignment(0);
/*     */     
/*  65 */     group.add((AbstractButton)gameInstallRadioButton1);
/*  66 */     group.add((AbstractButton)gameInstallRadioButton2);
/*  67 */     group.add((AbstractButton)gameInstallRadioButton3);
/*  68 */     group.add((AbstractButton)gameInstallRadioButton4);
/*  69 */     SwingUtil.changeFontFamily((JComponent)gameInstallRadioButton1, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
/*  70 */     SwingUtil.changeFontFamily((JComponent)gameInstallRadioButton2, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
/*  71 */     SwingUtil.changeFontFamily((JComponent)gameInstallRadioButton3, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
/*  72 */     SwingUtil.changeFontFamily((JComponent)gameInstallRadioButton4, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
/*     */     
/*  74 */     SwingUtil.changeFontFamily((JComponent)chooseFiles, FontTL.ROBOTO_MEDIUM, 12, ColorUtil.get(96));
/*  75 */     SwingUtil.changeFontFamily((JComponent)this.installToModpack, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
/*  76 */     SwingUtil.changeFontFamily((JComponent)warningLabel, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
/*  77 */     SwingUtil.changeFontFamily((JComponent)updaterButton1, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
/*     */     
/*  79 */     warningLabel.setBackground(ColorUtil.COLOR_244);
/*  80 */     panel.setBackground(Color.WHITE);
/*     */     
/*  82 */     warningLabel.setOpaque(true);
/*     */     
/*  84 */     spring.putConstraint("West", (Component)gameInstallRadioButton1, 0, "West", panel);
/*  85 */     spring.putConstraint("East", (Component)gameInstallRadioButton1, 143, "West", panel);
/*  86 */     spring.putConstraint("North", (Component)gameInstallRadioButton1, 0, "North", panel);
/*  87 */     spring.putConstraint("South", (Component)gameInstallRadioButton1, 58, "North", panel);
/*  88 */     panel.add((Component)gameInstallRadioButton1);
/*     */     
/*  90 */     spring.putConstraint("West", (Component)gameInstallRadioButton2, 143, "West", panel);
/*  91 */     spring.putConstraint("East", (Component)gameInstallRadioButton2, 286, "West", panel);
/*  92 */     spring.putConstraint("North", (Component)gameInstallRadioButton2, 0, "North", panel);
/*  93 */     spring.putConstraint("South", (Component)gameInstallRadioButton2, 58, "North", panel);
/*  94 */     panel.add((Component)gameInstallRadioButton2);
/*     */     
/*  96 */     spring.putConstraint("West", (Component)gameInstallRadioButton3, 286, "West", panel);
/*  97 */     spring.putConstraint("East", (Component)gameInstallRadioButton3, 429, "West", panel);
/*  98 */     spring.putConstraint("North", (Component)gameInstallRadioButton3, 0, "North", panel);
/*  99 */     spring.putConstraint("South", (Component)gameInstallRadioButton3, 58, "North", panel);
/* 100 */     panel.add((Component)gameInstallRadioButton3);
/*     */     
/* 102 */     spring.putConstraint("West", (Component)gameInstallRadioButton4, 429, "West", panel);
/* 103 */     spring.putConstraint("East", (Component)gameInstallRadioButton4, 0, "East", panel);
/* 104 */     spring.putConstraint("North", (Component)gameInstallRadioButton4, 0, "North", panel);
/* 105 */     spring.putConstraint("South", (Component)gameInstallRadioButton4, 58, "North", panel);
/* 106 */     panel.add((Component)gameInstallRadioButton4);
/*     */     
/* 108 */     spring.putConstraint("West", (Component)chooseFiles, 179, "West", panel);
/* 109 */     spring.putConstraint("East", (Component)chooseFiles, -177, "East", panel);
/* 110 */     spring.putConstraint("North", (Component)chooseFiles, 97, "North", panel);
/* 111 */     spring.putConstraint("South", (Component)chooseFiles, 135, "North", panel);
/* 112 */     panel.add((Component)chooseFiles);
/*     */     
/* 114 */     spring.putConstraint("West", (Component)this.installToModpack, 29, "West", panel);
/* 115 */     spring.putConstraint("East", (Component)this.installToModpack, -27, "East", panel);
/* 116 */     spring.putConstraint("North", (Component)this.installToModpack, 156, "North", panel);
/* 117 */     spring.putConstraint("South", (Component)this.installToModpack, 182, "North", panel);
/* 118 */     panel.add((Component)this.installToModpack);
/*     */     
/* 120 */     spring.putConstraint("West", (Component)warningLabel, 0, "West", panel);
/* 121 */     spring.putConstraint("East", (Component)warningLabel, 0, "East", panel);
/* 122 */     spring.putConstraint("North", (Component)warningLabel, 208, "North", panel);
/* 123 */     spring.putConstraint("South", (Component)warningLabel, 326, "North", panel);
/* 124 */     panel.add((Component)warningLabel);
/*     */     
/* 126 */     spring.putConstraint("West", (Component)updaterButton1, 205, "West", panel);
/* 127 */     spring.putConstraint("East", (Component)updaterButton1, 368, "West", panel);
/* 128 */     spring.putConstraint("North", (Component)updaterButton1, -68, "South", panel);
/* 129 */     spring.putConstraint("South", (Component)updaterButton1, -29, "South", panel);
/* 130 */     panel.add((Component)updaterButton1);
/*     */     
/* 132 */     gameInstallRadioButton1.addItemListener(e -> {
/*     */           if (1 == e.getStateChange()) {
/*     */             chooseFiles.setText(Localizable.get("modpack.explorer.files"));
/*     */             
/*     */             this.files = null;
/*     */             this.type = GameType.MOD;
/*     */             updateInfoLabel(version);
/*     */           } 
/*     */         });
/* 141 */     gameInstallRadioButton3.addItemListener(e -> {
/*     */           if (1 == e.getStateChange()) {
/*     */             chooseFiles.setText(Localizable.get("modpack.explorer.files"));
/*     */             this.files = null;
/*     */             this.type = GameType.MAP;
/*     */             updateInfoLabel(version);
/*     */           } 
/*     */         });
/* 149 */     gameInstallRadioButton2.addItemListener(e -> {
/*     */           if (1 == e.getStateChange()) {
/*     */             chooseFiles.setText(Localizable.get("modpack.explorer.files"));
/*     */             
/*     */             this.files = null;
/*     */             this.type = GameType.RESOURCEPACK;
/*     */             updateInfoLabel(version);
/*     */           } 
/*     */         });
/* 158 */     gameInstallRadioButton4.addItemListener(e -> {
/*     */           if (1 == e.getStateChange()) {
/*     */             chooseFiles.setText(Localizable.get("modpack.explorer.files"));
/*     */             this.files = null;
/*     */             this.type = GameType.SHADERPACK;
/*     */             updateInfoLabel(version);
/*     */           } 
/*     */         });
/* 166 */     chooseFiles.addActionListener(e -> {
/*     */           chooseFiles.setBorder(BorderFactory.createEmptyBorder());
/*     */           
/*     */           try {
/*     */             int result = chooser.showDialog(this, Localizable.get("modpack.explorer.files"));
/*     */             if (result == 0) {
/*     */               this.files = chooser.getSelectedFiles();
/*     */               chooseFiles.setText(Localizable.get("explorer.backup.file.chosen"));
/*     */             } 
/* 175 */           } catch (NullPointerException ex) {
/*     */             U.log(new Object[] { ex });
/*     */           } 
/*     */         });
/* 179 */     updaterButton1.addActionListener(e -> {
/*     */           if (this.files == null) {
/*     */             chooseFiles.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             return;
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           AsyncThread.execute(());
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 206 */     gameInstallRadioButton1.setSelected(true);
/*     */   }
/*     */   
/*     */   public static interface HandleListener {
/*     */     void installedSuccess();
/*     */     
/*     */     void processError(Throwable param1Throwable);
/*     */   }
/*     */   
/*     */   private void updateInfoLabel(CompleteVersion version) {
/* 216 */     this.installToModpack.setText(Localizable.get("modpack.install.handle." + this.type.toString().toLowerCase())
/* 217 */         .replace("modpack.name", version.getModpack().getName()));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\HandleInstallModpackElementFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */