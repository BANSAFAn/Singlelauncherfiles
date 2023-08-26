/*     */ package org.tlauncher.tlauncher.ui.settings;
/*     */ import com.google.inject.Key;
/*     */ import com.google.inject.name.Names;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.File;
/*     */ import java.util.Locale;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.plaf.ScrollBarUI;
/*     */ import javax.swing.plaf.basic.BasicComboPopup;
/*     */ import javax.swing.plaf.basic.ComboPopup;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
/*     */ import org.tlauncher.tlauncher.configuration.enums.BackupFrequency;
/*     */ import org.tlauncher.tlauncher.configuration.enums.BackupSetting;
/*     */ import org.tlauncher.tlauncher.configuration.enums.BackupToolTips;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.console.Console;
/*     */ import org.tlauncher.tlauncher.ui.converter.StringConverter;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorComboBox;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorField;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorFieldChangeListener;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorTextField;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.scenes.SettingsScene;
/*     */ import org.tlauncher.tlauncher.ui.swing.scroll.VersionScrollBarUI;
/*     */ import org.tlauncher.tlauncher.ui.ui.TlauncherBasicComboBoxUI;
/*     */ import org.tlauncher.util.MinecraftUtil;
/*     */ 
/*     */ public class TlauncherSettings extends PageSettings {
/*  45 */   public final TLauncher tlauncher = TLauncher.getInstance(); private static final long serialVersionUID = -555851839208513067L;
/*  46 */   public final Configuration global = this.tlauncher.getConfiguration();
/*  47 */   public final LangConfiguration lang = this.tlauncher.getLang(); private final EditorComboBox<Locale> local; public TlauncherSettings() {
/*  48 */     SpringLayout springLayout = new SpringLayout();
/*  49 */     setLayout(springLayout);
/*     */ 
/*     */     
/*  52 */     EditorComboBox<ConsoleType> consoleConverter = new EditorComboBox((StringConverter)new ConsoleTypeConverter(), (Object[])ConsoleType.values());
/*  53 */     EditorCheckBox doBackup = new EditorCheckBox("ui.yes");
/*  54 */     doBackup.setPreferredSize(new Dimension(70, doBackup.getHeight()));
/*  55 */     doBackup.setSelected(!this.global.getBoolean(BackupSetting.SKIP_USER_BACKUP.toString()));
/*  56 */     JLabel doBackupQuestionLabel = new JLabel(ImageCache.getNativeIcon("qestion-option-panel.png"));
/*  57 */     JLabel backupTitleQuestionLabel = new JLabel(ImageCache.getNativeIcon("qestion-option-panel.png"));
/*     */     
/*  59 */     EditorComboBox<ConnectionQuality> connQuality = new EditorComboBox((StringConverter)new ConnectionQualityConverter(), (Object[])ConnectionQuality.values());
/*     */     
/*  61 */     EditorComboBox<ActionOnLaunch> launchAction = new EditorComboBox((StringConverter)new ActionOnLaunchConverter(), (Object[])ActionOnLaunch.values());
/*     */     
/*  63 */     EditorComboBox<BackupFrequency> backupFrequency = new EditorComboBox((StringConverter)new BackupFrequencyConverter(), (Object[])BackupFrequency.values());
/*  64 */     this.local = new EditorComboBox((StringConverter)new LocaleConverter(), (Object[])this.global.getLocales());
/*  65 */     setTLauncherBasicComboBoxUI((JComboBox<ConsoleType>)consoleConverter);
/*  66 */     setTLauncherBasicComboBoxUI((JComboBox<ConnectionQuality>)connQuality);
/*  67 */     setTLauncherBasicComboBoxUI((JComboBox<ActionOnLaunch>)launchAction);
/*  68 */     setTLauncherBasicComboBoxUI((JComboBox<Locale>)this.local);
/*  69 */     setTLauncherBasicComboBoxUI((JComboBox<BackupFrequency>)backupFrequency);
/*     */     
/*  71 */     SettingElement settingElement = new SettingElement("settings.console.label", (JComponent)consoleConverter, 21);
/*  72 */     springLayout.putConstraint("North", (Component)settingElement, 0, "North", (Component)this);
/*  73 */     springLayout.putConstraint("West", (Component)settingElement, 0, "West", (Component)this);
/*  74 */     springLayout.putConstraint("South", (Component)settingElement, 21, "North", (Component)this);
/*  75 */     springLayout.putConstraint("East", (Component)settingElement, 0, "East", (Component)this);
/*  76 */     add((Component)settingElement);
/*     */     
/*  78 */     SettingElement settingElement_3 = new SettingElement("settings.connection.label", (JComponent)connQuality, 21);
/*  79 */     springLayout.putConstraint("North", (Component)settingElement_3, 15, "South", (Component)settingElement);
/*  80 */     springLayout.putConstraint("West", (Component)settingElement_3, 0, "West", (Component)this);
/*  81 */     springLayout.putConstraint("East", (Component)settingElement_3, 0, "East", (Component)this);
/*  82 */     add((Component)settingElement_3);
/*     */     
/*  84 */     SettingElement settingElement_4 = new SettingElement("settings.launch-action.label", (JComponent)launchAction, 21);
/*  85 */     springLayout.putConstraint("North", (Component)settingElement_4, 15, "South", (Component)settingElement_3);
/*  86 */     springLayout.putConstraint("West", (Component)settingElement_4, 0, "West", (Component)this);
/*  87 */     springLayout.putConstraint("East", (Component)settingElement_4, 0, "East", (Component)this);
/*  88 */     add((Component)settingElement_4);
/*     */     
/*  90 */     SettingElement settingElement_5 = new SettingElement("settings.lang.label", (JComponent)this.local, 21);
/*  91 */     springLayout.putConstraint("North", (Component)settingElement_5, 15, "South", (Component)settingElement_4);
/*  92 */     springLayout.putConstraint("West", (Component)settingElement_5, 0, "West", (Component)settingElement);
/*  93 */     springLayout.putConstraint("East", (Component)settingElement_5, 0, "East", (Component)settingElement);
/*  94 */     add((Component)settingElement_5);
/*     */     
/*  96 */     EditorTextField maxBackupSizeEditorField = new EditorTextField();
/*  97 */     EditorTextField maxTimeForBackupEditorField = new EditorTextField();
/*  98 */     EditorTextField backupRepetitionEditorField = new EditorTextField();
/*  99 */     maxTimeForBackupEditorField.setColumns(5);
/* 100 */     maxBackupSizeEditorField.setColumns(5);
/* 101 */     backupRepetitionEditorField.setText(this.tlauncher.getConfiguration().get(BackupSetting.REPEAT_BACKUP.toString()));
/* 102 */     JPanel backupTitlePanel = new JPanel();
/* 103 */     LocalizableLabel backupTitle = new LocalizableLabel("settings.backup.title");
/* 104 */     backupTitlePanel.setPreferredSize(new Dimension(backupTitlePanel.getWidth(), 27));
/* 105 */     backupTitle.setFont(SettingElement.LABEL_FONT);
/* 106 */     backupTitlePanel.add((Component)backupTitle);
/* 107 */     backupTitlePanel.add(backupTitleQuestionLabel);
/* 108 */     backupTitlePanel.setBackground(SettingsScene.BACKGROUND);
/* 109 */     backupTitleQuestionLabel.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e)
/*     */           {
/* 113 */             String stringPath = MinecraftUtil.buildWorkingPath(new String[] { "versions" }) + File.separator + Localizable.get("version.name") + File.separator + "saves";
/* 114 */             Alert.showHtmlMessage(Localizable.get("settings.backup.title"), 
/* 115 */                 Localizable.get(BackupToolTips.TITLE.toString(), new Object[] {
/* 116 */                     MinecraftUtil.buildWorkingPath(new String[] { "backup/saves" }), this.this$0.tlauncher
/* 117 */                     .getConfiguration().get(BackupSetting.FREE_PARTITION_SIZE.toString()), 
/* 118 */                     MinecraftUtil.buildWorkingPath(new String[] { "saves" }), stringPath, 
/* 119 */                     MinecraftUtil.buildWorkingPath(new String[] { "backup/saves" })
/*     */                   }), 1, 700);
/*     */           }
/*     */         });
/* 123 */     doBackupQuestionLabel.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {
/* 126 */             Alert.showCustomMonolog(Localizable.get("settings.doBackup").replace(":", ""), 
/* 127 */                 Localizable.get("settings.doBackup.tooltip"));
/*     */           }
/*     */         });
/* 130 */     springLayout.putConstraint("North", backupTitlePanel, 5, "South", (Component)settingElement_5);
/* 131 */     springLayout.putConstraint("West", backupTitlePanel, 0, "West", (Component)this);
/* 132 */     springLayout.putConstraint("East", backupTitlePanel, 0, "East", (Component)this);
/*     */     
/* 134 */     add(backupTitlePanel);
/*     */     
/* 136 */     JPanel doBackupPanel = new JPanel();
/* 137 */     EditorTextField doBackupEditorField = new EditorTextField();
/* 138 */     doBackupPanel.setBackground(SettingsScene.BACKGROUND);
/* 139 */     doBackupEditorField.setVisible(false);
/* 140 */     doBackupPanel.add((Component)doBackup);
/* 141 */     doBackupPanel.add((Component)doBackupEditorField);
/*     */     
/* 143 */     doBackup.addActionListener(e -> initBackupCheckbox(doBackup, backupFrequency, maxBackupSizeEditorField, maxTimeForBackupEditorField, backupRepetitionEditorField, doBackupEditorField));
/*     */ 
/*     */ 
/*     */     
/* 147 */     initBackupCheckbox(doBackup, backupFrequency, maxBackupSizeEditorField, maxTimeForBackupEditorField, backupRepetitionEditorField, doBackupEditorField);
/*     */ 
/*     */     
/* 150 */     backupFrequency.addActionListener(e -> {
/*     */           if (backupFrequency.getSelectedValue() != null) {
/*     */             String s = ((BackupFrequency)backupFrequency.getSelectedValue()).toString();
/*     */             if (s.equals(BackupFrequency.EVERYTIME.toString())) {
/*     */               backupRepetitionEditorField.setText("0");
/*     */             } else if (s.equals(BackupFrequency.OFTEN.toString())) {
/*     */               backupRepetitionEditorField.setText("1");
/*     */             } 
/*     */           } 
/*     */         });
/* 160 */     SettingElement settingElement_6 = new SettingElement("settings.doBackup", (JComponent)doBackup, 20, -1, doBackupQuestionLabel);
/*     */     
/* 162 */     springLayout.putConstraint("North", (Component)settingElement_6, 0, "South", backupTitlePanel);
/* 163 */     springLayout.putConstraint("West", (Component)settingElement_6, 0, "West", (Component)this);
/* 164 */     springLayout.putConstraint("East", (Component)settingElement_6, 0, "East", (Component)this);
/* 165 */     add((Component)settingElement_6);
/*     */     
/* 167 */     SettingElement settingElement_7 = new SettingElement("settings.backup.frequency", (JComponent)backupFrequency, 27);
/*     */     
/* 169 */     springLayout.putConstraint("North", (Component)settingElement_7, 5, "South", (Component)settingElement_6);
/* 170 */     springLayout.putConstraint("West", (Component)settingElement_7, 0, "West", (Component)this);
/* 171 */     springLayout.putConstraint("East", (Component)settingElement_7, 0, "East", (Component)this);
/*     */     
/* 173 */     add((Component)settingElement_7);
/*     */     
/* 175 */     JPanel maxBackupSizePanel = getCommonPanel("progress.bar.panel.size", "settings.max.backup.size", BackupToolTips.MAX_BACKUP_SIZE
/* 176 */         .toString());
/*     */     
/* 178 */     SettingElement settingElement_8 = new SettingElement("settings.max.backup.size", (JComponent)maxBackupSizeEditorField, 27, -1, maxBackupSizePanel);
/*     */ 
/*     */     
/* 181 */     springLayout.putConstraint("North", (Component)settingElement_8, 4, "South", (Component)settingElement_7);
/* 182 */     springLayout.putConstraint("West", (Component)settingElement_8, 0, "West", (Component)this);
/* 183 */     springLayout.putConstraint("East", (Component)settingElement_8, 0, "East", (Component)this);
/*     */     
/* 185 */     add((Component)settingElement_8);
/*     */     
/* 187 */     JPanel maxTimeForBackupPanel = getCommonPanel("progress.bar.panel.remaining.time.days", "settings.max.backup.time", BackupToolTips.MAX_BACKUP_SAVE_TIME
/* 188 */         .toString());
/*     */     
/* 190 */     SettingElement settingElement_9 = new SettingElement("settings.max.backup.time", (JComponent)maxTimeForBackupEditorField, 27, -1, maxTimeForBackupPanel);
/*     */ 
/*     */     
/* 193 */     springLayout.putConstraint("North", (Component)settingElement_9, 0, "South", (Component)settingElement_8);
/* 194 */     springLayout.putConstraint("West", (Component)settingElement_9, 0, "West", (Component)this);
/* 195 */     springLayout.putConstraint("East", (Component)settingElement_9, 0, "East", (Component)this);
/*     */     
/* 197 */     add((Component)settingElement_9);
/*     */     
/* 199 */     EditorFieldChangeListener changeListener = new EditorFieldChangeListener()
/*     */       {
/*     */         public void onChange(String oldvalue, String newvalue) {
/* 202 */           Console c = (Console)TLauncher.getInjector().getInstance(Key.get(Console.class, (Annotation)Names.named("console")));
/* 203 */           switch (ConsoleType.get(newvalue)) {
/*     */             case GLOBAL:
/* 205 */               c.show(false);
/*     */               return;
/*     */             case NONE:
/* 208 */               c.hide();
/*     */               return;
/*     */           } 
/* 211 */           throw new IllegalArgumentException("Unknown console type!");
/*     */         }
/*     */       };
/*     */     
/* 215 */     EditorFieldChangeListener conQualityListener = new EditorFieldChangeListener()
/*     */       {
/*     */         public void onChange(String oldValue, String newValue) {
/* 218 */           TlauncherSettings.this.tlauncher.getDownloader().setConfiguration(TlauncherSettings.this.global.getConnectionQuality());
/*     */         }
/*     */       };
/* 221 */     EditorFieldChangeListener localeListener = new EditorFieldChangeListener()
/*     */       {
/*     */         public void onChange(String oldvalue, String newvalue) {
/* 224 */           if (TlauncherSettings.this.tlauncher.getFrame() != null) {
/* 225 */             TlauncherSettings.this.tlauncher.getFrame().updateLocales();
/*     */           }
/*     */         }
/*     */       };
/* 229 */     addHandler(new HandlerSettings("gui.console", (EditorField)consoleConverter, changeListener));
/* 230 */     addHandler(new HandlerSettings("connection", (EditorField)connQuality, conQualityListener));
/* 231 */     addHandler(new HandlerSettings("minecraft.onlaunch", (EditorField)launchAction));
/* 232 */     addHandler(new HandlerSettings("locale", (EditorField)this.local, localeListener));
/* 233 */     addHandler(new HandlerSettings(BackupSetting.MAX_SIZE_FOR_WORLD.toString(), (EditorField)maxBackupSizeEditorField));
/* 234 */     addHandler(new HandlerSettings(BackupSetting.MAX_TIME_FOR_BACKUP.toString(), (EditorField)maxTimeForBackupEditorField));
/* 235 */     addHandler(new HandlerSettings(BackupSetting.REPEAT_BACKUP.toString(), (EditorField)backupFrequency));
/* 236 */     addHandler(new HandlerSettings(BackupSetting.SKIP_USER_BACKUP.toString(), (EditorField)doBackupEditorField));
/*     */   }
/*     */   
/*     */   private JPanel getCommonPanel(String text, final String title, final String toolTipText) {
/* 240 */     JPanel mainPanel = new JPanel();
/* 241 */     mainPanel.setBackground(SettingsScene.BACKGROUND);
/* 242 */     mainPanel.add((Component)new LocalizableLabel(text));
/* 243 */     JLabel questionMark = new JLabel(new ImageIcon(ImageCache.getImage("qestion-option-panel.png")));
/* 244 */     questionMark.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {
/* 247 */             Alert.showCustomMonolog(Localizable.get(title).replace(":", ""), Localizable.get(toolTipText));
/*     */           }
/*     */         });
/* 250 */     mainPanel.add(questionMark);
/* 251 */     return mainPanel;
/*     */   }
/*     */   
/*     */   private static <T> void setTLauncherBasicComboBoxUI(JComboBox<T> comboBox) {
/* 255 */     comboBox.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(149, 149, 149)));
/* 256 */     comboBox.setUI((ComboBoxUI)new TlauncherBasicComboBoxUI()
/*     */         {
/*     */           
/*     */           protected ComboPopup createPopup()
/*     */           {
/* 261 */             BasicComboPopup basic = new BasicComboPopup(this.comboBox)
/*     */               {
/*     */                 protected JScrollPane createScroller() {
/* 264 */                   VersionScrollBarUI barUI = new VersionScrollBarUI()
/*     */                     {
/*     */                       protected Dimension getMinimumThumbSize() {
/* 267 */                         return new Dimension(10, 40);
/*     */                       }
/*     */ 
/*     */                       
/*     */                       public Dimension getMaximumSize(JComponent c) {
/* 272 */                         Dimension dim = super.getMaximumSize(c);
/* 273 */                         dim.setSize(10.0D, dim.getHeight());
/* 274 */                         return dim;
/*     */                       }
/*     */ 
/*     */                       
/*     */                       public Dimension getPreferredSize(JComponent c) {
/* 279 */                         Dimension dim = super.getPreferredSize(c);
/* 280 */                         dim.setSize(13.0D, dim.getHeight());
/* 281 */                         return dim;
/*     */                       }
/*     */                     };
/*     */                   
/* 285 */                   barUI.setGapThubm(5);
/*     */                   
/* 287 */                   JScrollPane scroller = new JScrollPane(this.list, 20, 31);
/*     */                   
/* 289 */                   scroller.getVerticalScrollBar().setUI((ScrollBarUI)barUI);
/* 290 */                   return scroller;
/*     */                 }
/*     */               };
/* 293 */             basic.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.GRAY));
/* 294 */             return basic;
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean chooseChinaLocal() {
/* 300 */     if (Objects.isNull(this.global.getLocale()))
/* 301 */       return false; 
/* 302 */     return (((Locale)this.local.getSelectedValue()).getLanguage().equals((new Locale("zh")).getLanguage()) || this.global
/* 303 */       .getLocale().getLanguage().equals((new Locale("zh")).getLanguage()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void initBackupCheckbox(EditorCheckBox doBackup, EditorComboBox<BackupFrequency> backupFrequency, EditorTextField maxBackupSizeEditorField, EditorTextField maxTimeForBackupEditorField, EditorTextField backupRepetitionEditorField, EditorTextField doBackupEditorField) {
/* 309 */     if (doBackup.isSelected()) {
/* 310 */       maxTimeForBackupEditorField.setEditable(true);
/* 311 */       maxBackupSizeEditorField.setEditable(true);
/* 312 */       backupRepetitionEditorField.setEditable(true);
/* 313 */       backupFrequency.setEnabled(true);
/* 314 */       doBackupEditorField.setText("false");
/*     */     } else {
/* 316 */       maxTimeForBackupEditorField.setEditable(false);
/* 317 */       maxBackupSizeEditorField.setEditable(false);
/* 318 */       backupRepetitionEditorField.setEditable(false);
/* 319 */       backupFrequency.setEnabled(false);
/* 320 */       doBackupEditorField.setText("true");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\settings\TlauncherSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */