/*     */ package org.tlauncher.tlauncher.ui.settings;
/*     */ 
/*     */ import com.google.common.eventbus.Subscribe;
/*     */ import com.google.inject.Inject;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.SwingUtilities;
/*     */ import net.minecraft.launcher.versions.ReleaseType;
/*     */ import org.tlauncher.tlauncher.controller.JavaMinecraftController;
/*     */ import org.tlauncher.tlauncher.entity.minecraft.MinecraftJava;
/*     */ import org.tlauncher.tlauncher.managers.VersionLists;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.converter.MinecraftJavaConverter;
/*     */ import org.tlauncher.tlauncher.ui.converter.StringConverter;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorComboBox;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorField;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorFieldChangeListener;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorFileField;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorResolutionField;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorTextField;
/*     */ import org.tlauncher.tlauncher.ui.explorer.FileChooser;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.modpack.ConfigurationJavaFrame;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.BorderPanel;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MinecraftSettings
/*     */   extends PageSettings
/*     */ {
/*     */   public static final String MINECRAFT_SETTING_RAM = "minecraft.memory.ram2";
/*     */   SettingsMemorySlider slider;
/*     */   @Inject
/*     */   private JavaMinecraftController controller;
/*     */   private EditorComboBox<MinecraftJava.CompleteMinecraftJava> javaComboBox;
/*  54 */   public static final MinecraftJava.CompleteMinecraftJava defaultJava = MinecraftJava.CompleteMinecraftJava.create(Long.valueOf(0L), "settings.default", "", new ArrayList());
/*     */   
/*     */   @Inject
/*     */   private MinecraftJavaConverter converter;
/*     */   
/*     */   public MinecraftSettings() {
/*  60 */     setOpaque(false);
/*     */   }
/*     */   
/*     */   private ExtendedPanel doublePanel(JComponent com1, int gap, JComponent comp2) {
/*  64 */     ExtendedPanel extendedPanel = new ExtendedPanel(new FlowLayout(0, 0, 0));
/*  65 */     extendedPanel.add(com1);
/*  66 */     extendedPanel.add(Box.createHorizontalStrut(gap));
/*  67 */     extendedPanel.add(comp2);
/*  68 */     return extendedPanel;
/*     */   }
/*     */ 
/*     */   
/*     */   private ExtendedPanel createBoxes(List<EditorCheckBox> list) {
/*  73 */     ExtendedPanel extendedPanel = new ExtendedPanel();
/*  74 */     extendedPanel.setLayout(new BoxLayout((Container)extendedPanel, 1));
/*  75 */     for (EditorCheckBox box : list) {
/*  76 */       extendedPanel.add((Component)box);
/*     */     }
/*  78 */     return extendedPanel;
/*     */   }
/*     */ 
/*     */   
/*     */   @Inject
/*     */   public void initGuice() {
/*  84 */     SpringLayout springLayout = new SpringLayout();
/*  85 */     FileChooser fileChooser = (FileChooser)TLauncher.getInjector().getInstance(FileChooser.class);
/*  86 */     fileChooser.setFileSelectionMode(1);
/*  87 */     EditorFileField editorFileField = new EditorFileField("settings.client.gamedir.prompt", fileChooser)
/*     */       {
/*     */         public boolean isValueValid() {
/*     */           try {
/*  91 */             File f = new File(getSettingsValue(), "testChooserFolder");
/*  92 */             FileUtil.createFolder(f);
/*  93 */             FileUtil.deleteDirectory(f);
/*  94 */           } catch (IOException e) {
/*  95 */             Alert.showLocError("settings.client.gamedir.noaccess", e);
/*  96 */             return false;
/*     */           } 
/*  98 */           return super.isValueValid();
/*     */         }
/*     */       };
/*     */     
/* 102 */     EditorResolutionField editorResolutionField = new EditorResolutionField("settings.client.resolution.width", "settings.client.resolution.height", this.global.getDefaultClientWindowSize(), false);
/* 103 */     EditorCheckBox box = new EditorCheckBox("settings.client.resolution.fullscreen");
/*     */     
/* 105 */     List<EditorCheckBox> versions = new ArrayList<>();
/* 106 */     List<HandlerSettings> settings = new ArrayList<>();
/*     */     
/* 108 */     EditorTextField jvmArguments = new EditorTextField("settings.java.args.jvm", true);
/* 109 */     EditorTextField minecraftArguments = new EditorTextField("settings.java.args.minecraft", true);
/* 110 */     ExtendedPanel argPanel1 = new ExtendedPanel();
/* 111 */     argPanel1.setLayout(new BoxLayout((Container)argPanel1, 1));
/*     */     
/* 113 */     UpdaterButton changeJava = new UpdaterButton(UpdaterButton.GRAY_COLOR, "settings.change");
/* 114 */     this.javaComboBox = new EditorComboBox((StringConverter)this.converter, null);
/* 115 */     this.slider = new SettingsMemorySlider();
/*     */     
/* 117 */     EditorFieldChangeListener changeListener = new EditorFieldChangeListener()
/*     */       {
/*     */         public void onChange(String oldValue, String newValue)
/*     */         {
/* 121 */           TLauncher.getInstance().getVersionManager().updateVersionList();
/*     */         }
/*     */       };
/*     */     
/* 125 */     BorderPanel b = new BorderPanel(10, 0);
/* 126 */     b.add((Component)this.javaComboBox, "Center");
/* 127 */     b.add((Component)changeJava, "East");
/* 128 */     b.setPreferredSize(new Dimension(0, 21));
/* 129 */     argPanel1.add((Component)b);
/*     */     
/* 131 */     for (ReleaseType releaseType : ReleaseType.getDefinable()) {
/* 132 */       EditorCheckBox editorCheckBox = new EditorCheckBox("settings.versions." + releaseType);
/* 133 */       HandlerSettings handlerSettings1 = new HandlerSettings("minecraft.versions." + releaseType, (EditorField)editorCheckBox, changeListener);
/*     */       
/* 135 */       addHandler(handlerSettings1);
/* 136 */       settings.add(handlerSettings1);
/* 137 */       versions.add(editorCheckBox);
/*     */     } 
/* 139 */     EditorCheckBox oldRelease = new EditorCheckBox("settings.versions.sub." + ReleaseType.SubType.OLD_RELEASE);
/* 140 */     HandlerSettings handlerSettings = new HandlerSettings("minecraft.versions.sub." + ReleaseType.SubType.OLD_RELEASE, (EditorField)oldRelease, changeListener);
/*     */     
/* 142 */     addHandler(handlerSettings);
/* 143 */     settings.add(handlerSettings);
/* 144 */     versions.add(2, oldRelease);
/*     */     
/* 146 */     SettingElement directorySettings = new SettingElement("settings.client.gamedir.label", (JComponent)editorFileField, 31);
/*     */     
/* 148 */     SettingElement resolution = new SettingElement("settings.client.resolution.label", (JComponent)doublePanel((JComponent)editorResolutionField, 16, (JComponent)box), 21);
/* 149 */     SettingElement versionList = new SettingElement("settings.versions.label", (JComponent)createBoxes(versions), 121);
/* 150 */     SettingElement javaPath = new SettingElement("settings.java.path.label", (JComponent)argPanel1, 0);
/* 151 */     SettingElement memory = new SettingElement("settings.java.memory.label", (JComponent)this.slider, 84, 10, 1);
/*     */     
/* 153 */     ExtendedPanel argPanel = new ExtendedPanel();
/* 154 */     argPanel.setLayout(new BoxLayout((Container)argPanel, 1));
/* 155 */     argPanel.add(Box.createVerticalStrut(9));
/* 156 */     argPanel.add((Component)minecraftArguments);
/* 157 */     SettingElement arguments = new SettingElement("version.manager.editor.field.minecraftArguments", (JComponent)argPanel, 0);
/*     */     
/* 159 */     setLayout(springLayout);
/*     */     
/* 161 */     springLayout.putConstraint("North", (Component)directorySettings, 0, "North", (Component)this);
/* 162 */     springLayout.putConstraint("West", (Component)directorySettings, 0, "West", (Component)this);
/* 163 */     springLayout.putConstraint("South", (Component)directorySettings, 27, "North", (Component)this);
/* 164 */     springLayout.putConstraint("East", (Component)directorySettings, 0, "East", (Component)this);
/*     */     
/* 166 */     add((Component)directorySettings);
/*     */     
/* 168 */     springLayout.putConstraint("North", (Component)resolution, 17, "South", (Component)directorySettings);
/* 169 */     springLayout.putConstraint("West", (Component)resolution, 0, "West", (Component)this);
/* 170 */     springLayout.putConstraint("South", (Component)resolution, 43, "South", (Component)directorySettings);
/* 171 */     springLayout.putConstraint("East", (Component)resolution, 0, "East", (Component)directorySettings);
/* 172 */     add((Component)resolution);
/*     */     
/* 174 */     springLayout.putConstraint("North", (Component)versionList, 6, "South", (Component)resolution);
/* 175 */     springLayout.putConstraint("West", (Component)versionList, 0, "West", (Component)this);
/* 176 */     springLayout.putConstraint("South", (Component)versionList, 138, "South", (Component)resolution);
/* 177 */     springLayout.putConstraint("East", (Component)versionList, 0, "East", (Component)directorySettings);
/* 178 */     add((Component)versionList);
/* 179 */     springLayout.putConstraint("North", (Component)arguments, 2, "South", (Component)versionList);
/* 180 */     springLayout.putConstraint("West", (Component)arguments, 0, "West", (Component)this);
/* 181 */     springLayout.putConstraint("South", (Component)arguments, 32, "North", (Component)arguments);
/* 182 */     springLayout.putConstraint("East", (Component)arguments, 0, "East", (Component)directorySettings);
/* 183 */     add((Component)arguments);
/* 184 */     springLayout.putConstraint("West", (Component)javaPath, 0, "West", (Component)directorySettings);
/* 185 */     springLayout.putConstraint("East", (Component)javaPath, 0, "East", (Component)directorySettings);
/* 186 */     springLayout.putConstraint("North", (Component)javaPath, 12, "South", (Component)arguments);
/* 187 */     springLayout.putConstraint("South", (Component)javaPath, 23, "North", (Component)javaPath);
/*     */     
/* 189 */     add((Component)javaPath);
/*     */     
/* 191 */     springLayout.putConstraint("North", (Component)memory, 18, "South", (Component)javaPath);
/* 192 */     springLayout.putConstraint("South", (Component)memory, 100, "North", (Component)memory);
/* 193 */     springLayout.putConstraint("West", (Component)memory, 0, "West", (Component)directorySettings);
/* 194 */     springLayout.putConstraint("East", (Component)memory, 0, "East", (Component)directorySettings);
/* 195 */     add((Component)memory);
/*     */     
/* 197 */     EditorFieldChangeListener editorFieldChangeListener = new EditorFieldChangeListener()
/*     */       {
/*     */         public void onChange(String oldValue, String newValue)
/*     */         {
/* 201 */           if (!MinecraftSettings.this.tlauncher.isReady()) {
/*     */             return;
/*     */           }
/*     */           try {
/* 205 */             ((VersionLists)MinecraftSettings.this.tlauncher.getManager().getComponent(VersionLists.class)).updateLocal();
/* 206 */           } catch (IOException e) {
/* 207 */             Alert.showLocError("settings.client.gamedir.noaccess", e);
/*     */             
/*     */             return;
/*     */           } 
/* 211 */           MinecraftSettings.this.tlauncher.getVersionManager().asyncRefresh();
/* 212 */           MinecraftSettings.this.tlauncher.getProfileManager().asyncRefresh();
/*     */         }
/*     */       };
/* 215 */     changeJava.addActionListener(e -> {
/*     */           ConfigurationJavaFrame frame = (ConfigurationJavaFrame)TLauncher.getInjector().getInstance(ConfigurationJavaFrame.class);
/*     */           
/*     */           frame.setVisible(true);
/*     */         });
/* 220 */     addHandler(new HandlerSettings("minecraft.gamedir", (EditorField)editorFileField, editorFieldChangeListener));
/* 221 */     addHandler(new HandlerSettings("minecraft.size", (EditorField)editorResolutionField));
/* 222 */     addHandler(new HandlerSettings("minecraft.fullscreen", (EditorField)box));
/* 223 */     addHandler(new HandlerSettings("minecraft.javaargs", (EditorField)jvmArguments));
/* 224 */     addHandler(new HandlerSettings("minecraft.args", (EditorField)minecraftArguments));
/* 225 */     addHandler(new HandlerSettings("minecraft.memory.ram2", this.slider));
/* 226 */     addHandler(new HandlerSettings("minecraft.args", (EditorField)minecraftArguments));
/* 227 */     addHandler(new HandlerSettings("minecraft.java.selected", (EditorField)this.javaComboBox));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/* 233 */     this.controller.notifyListeners();
/* 234 */     this.slider.initMemoryQuestion();
/* 235 */     super.init();
/*     */   }
/*     */   
/*     */   @Subscribe
/*     */   public void applicationEvent(MinecraftJava event) {
/* 240 */     SwingUtilities.invokeLater(() -> {
/*     */           DefaultComboBoxModel<MinecraftJava.CompleteMinecraftJava> model = (DefaultComboBoxModel<MinecraftJava.CompleteMinecraftJava>)this.javaComboBox.getModel();
/*     */           MinecraftJava.CompleteMinecraftJava c = this.controller.getCurrent();
/*     */           model.removeAllElements();
/*     */           model.addElement(defaultJava);
/*     */           event.getJvm().values().forEach(model::addElement);
/*     */           if (Objects.nonNull(c)) {
/*     */             this.javaComboBox.setSelectedValue(c);
/*     */           } else {
/*     */             this.javaComboBox.setSelectedValue(defaultJava);
/*     */           } 
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\settings\MinecraftSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */