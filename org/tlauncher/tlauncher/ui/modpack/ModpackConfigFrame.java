/*     */ package org.tlauncher.tlauncher.ui.modpack;
/*     */ 
/*     */ import by.gdev.util.DesktopUtil;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ import javax.swing.plaf.ComboBoxUI;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import org.tlauncher.modpack.domain.client.ForgeVersionDTO;
/*     */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*     */ import org.tlauncher.modpack.domain.client.GameVersionDTO;
/*     */ import org.tlauncher.modpack.domain.client.ModDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.modpack.domain.client.share.MinecraftVersionDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.NameIdDTO;
/*     */ import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.controller.ModpackConfig;
/*     */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*     */ import org.tlauncher.tlauncher.modpack.ModpackUtil;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterFullButton;
/*     */ import org.tlauncher.tlauncher.ui.swing.renderer.CreationModpackForgeComboboxRenderer;
/*     */ import org.tlauncher.tlauncher.ui.ui.CreationModpackForgeComboboxUI;
/*     */ import org.tlauncher.util.ColorUtil;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.swing.FontTL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModpackConfigFrame
/*     */   extends TemlateModpackFrame
/*     */ {
/*     */   private ModpackConfig controller;
/*     */   private JCheckBox box;
/*     */   private JTextField modpackName;
/*     */   private JComboBox<MinecraftVersionDTO> minecraftVersion;
/*     */   private SliderModpackPanel slider;
/*     */   private JButton save;
/*     */   private JButton open;
/*     */   private JButton remove;
/*     */   private EditorCheckBox skinCheckBox;
/*  74 */   private static final Dimension maxSize = new Dimension(572, 451);
/*  75 */   private static final Dimension minSize = new Dimension(572, 350);
/*     */   
/*     */   private ModpackManager manager;
/*     */   
/*     */   private final JPanel panel;
/*     */   private final JLabel selectedMemory;
/*     */   private JLabel question;
/*     */   private Configuration c;
/*  83 */   private Color colorButton = new Color(0, 174, 239);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModpackConfigFrame(JFrame parent, CompleteVersion version) {
/*  90 */     super(parent, "modpack.config.title", minSize);
/*  91 */     this.manager = (ModpackManager)TLauncher.getInjector().getInstance(ModpackManager.class);
/*  92 */     this.controller = (ModpackConfig)TLauncher.getInjector().getInstance(ModpackConfig.class);
/*  93 */     this.question = new JLabel(ImageCache.getNativeIcon("qestion-option-panel.png"));
/*  94 */     this.c = TLauncher.getInstance().getConfiguration();
/*  95 */     this.question.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mousePressed(MouseEvent e) {
/*  98 */             ModpackConfigFrame.this.setVisible(false);
/*  99 */             Alert.showLocMessage(ModpackConfigFrame.this.c.get("memory.problem.message"));
/* 100 */             ModpackConfigFrame.this.setVisible(true);
/*     */           }
/*     */         });
/* 103 */     LocalizableLabel localizableLabel1 = new LocalizableLabel("modpack.config.memory.title");
/* 104 */     LocalizableLabel localizableLabel2 = new LocalizableLabel("modpack.config.system.label");
/*     */     
/* 106 */     Border empty = BorderFactory.createLineBorder(ColorUtil.COLOR_149, 1);
/* 107 */     CompoundBorder border = new CompoundBorder(empty, BorderFactory.createEmptyBorder(0, 15, 0, 0));
/*     */     
/* 109 */     LocalizableLabel localizableLabel3 = new LocalizableLabel("modpack.creation.name");
/*     */     
/* 111 */     JLabel forgeVersionLabel = new JLabel(Localizable.get("version.name.v1") + ":");
/* 112 */     LocalizableLabel localizableLabel4 = new LocalizableLabel("version.manager.editor.field.type");
/* 113 */     LocalizableLabel localizableLabel5 = new LocalizableLabel("modpack.config.game.version");
/*     */     
/* 115 */     localizableLabel4.setVerticalAlignment(0);
/* 116 */     localizableLabel5.setVerticalAlignment(0);
/*     */     
/* 118 */     this.selectedMemory = (JLabel)new LocalizableLabel("settings.java.memory.label");
/* 119 */     ModpackVersionDTO modpackVersionDTO = (ModpackVersionDTO)version.getModpack().getVersion();
/*     */     
/* 121 */     GameVersionDTO gameVersionDTO = modpackVersionDTO.getGameVersionDTO();
/* 122 */     this.minecraftVersion = new JComboBox<>();
/* 123 */     this.minecraftVersion.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorUtil.COLOR_149));
/* 124 */     this.minecraftVersion.setRenderer((ListCellRenderer<? super MinecraftVersionDTO>)new CreationModpackForgeComboboxRenderer());
/* 125 */     this.minecraftVersion.setUI((ComboBoxUI)new CreationModpackForgeComboboxUI());
/*     */     
/* 127 */     this.box = (JCheckBox)new EditorCheckBox("modpack.config.memory.box");
/* 128 */     this.box.setIconTextGap(14);
/* 129 */     this.skinCheckBox = new EditorCheckBox("modpack.config.skin.use");
/* 130 */     this.skinCheckBox.setIconTextGap(14);
/* 131 */     if (ModpackUtil.useSkinMod(version)) {
/* 132 */       this.skinCheckBox.setSelected(true);
/*     */     }
/* 134 */     else if (gameVersionDTO == null) {
/* 135 */       this.skinCheckBox.setSelected(false);
/* 136 */       this.skinCheckBox.addItemListener(e -> {
/*     */             if (e.getStateChange() == 1) {
/*     */               setVisible(false);
/*     */               
/*     */               Alert.showLocMessage("modpack.internet.update");
/*     */               
/*     */               this.skinCheckBox.setSelected(false);
/*     */               setVisible(true);
/*     */             } 
/*     */           });
/*     */     } 
/* 147 */     this.modpackName = new JTextField(version.getID());
/* 148 */     this.modpackName.setBorder(border);
/* 149 */     this.modpackName.setForeground(ColorUtil.COLOR_25);
/*     */ 
/*     */     
/* 152 */     JLabel gameVersionValue = new JLabel(Objects.nonNull(gameVersionDTO) ? gameVersionDTO.getName() : modpackVersionDTO.getGameVersion());
/* 153 */     NameIdDTO mvt = modpackVersionDTO.findFirstMinecraftVersionType();
/*     */     
/* 155 */     LocalizableLabel localizableLabel6 = new LocalizableLabel("modpack.version." + (Objects.nonNull(mvt) ? mvt.getName() : "forge"));
/*     */     
/* 157 */     localizableLabel6.setBorder(border);
/* 158 */     gameVersionValue.setBorder(border);
/*     */     
/* 160 */     this.slider = new SliderModpackPanel(new Dimension(534, 80));
/* 161 */     if (version.getModpack().isModpackMemory()) {
/* 162 */       this.slider.setValue(version.getModpack().getMemory());
/*     */     } else {
/* 164 */       this.slider.setValue(TLauncher.getInstance().getConfiguration().getInteger("minecraft.memory.ram2"));
/*     */     } 
/*     */     
/* 167 */     this.save = (JButton)new UpdaterFullButton(this.colorButton, BLUE_COLOR_UNDER, "settings.save", "save-modpack.png");
/* 168 */     this.save.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));
/* 169 */     this.save.setIconTextGap(19);
/* 170 */     this.open = (JButton)new UpdaterFullButton(this.colorButton, BLUE_COLOR_UNDER, "modpack.open.folder", "open-modpack.png");
/* 171 */     this.open.setIconTextGap(10);
/* 172 */     this.remove = (JButton)new UpdaterFullButton(new Color(208, 43, 43), new Color(180, 39, 39), "modpack.popup.delete", "modpack-dustbin.png");
/*     */     
/* 174 */     this.remove.setBorder(BorderFactory.createEmptyBorder(0, 19, 0, 0));
/* 175 */     this.remove.setIconTextGap(15);
/*     */     
/* 177 */     SpringLayout spring = new SpringLayout();
/* 178 */     this.panel = new JPanel(spring);
/* 179 */     this.panel.setBackground(Color.WHITE);
/*     */     
/* 181 */     this.panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 21, 0));
/* 182 */     addCenter(this.panel);
/*     */     
/* 184 */     SwingUtil.changeFontFamily((JComponent)localizableLabel3, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_149);
/* 185 */     SwingUtil.changeFontFamily((JComponent)localizableLabel5, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_149);
/* 186 */     SwingUtil.changeFontFamily((JComponent)localizableLabel4, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_149);
/* 187 */     SwingUtil.changeFontFamily(gameVersionValue, FontTL.ROBOTO_REGULAR, 16, ColorUtil.COLOR_149);
/* 188 */     SwingUtil.changeFontFamily((JComponent)localizableLabel6, FontTL.ROBOTO_REGULAR, 16, ColorUtil.COLOR_149);
/* 189 */     SwingUtil.changeFontFamily(this.modpackName, FontTL.ROBOTO_REGULAR, 16, ColorUtil.COLOR_25);
/* 190 */     SwingUtil.changeFontFamily(forgeVersionLabel, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_149);
/* 191 */     SwingUtil.changeFontFamily(this.minecraftVersion, FontTL.ROBOTO_REGULAR, 16, ColorUtil.COLOR_149);
/* 192 */     SwingUtil.changeFontFamily(this.box, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
/* 193 */     SwingUtil.changeFontFamily((JComponent)this.skinCheckBox, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
/* 194 */     SwingUtil.changeFontFamily((JComponent)localizableLabel1, FontTL.ROBOTO_BOLD, 14, Color.BLACK);
/* 195 */     SwingUtil.changeFontFamily((JComponent)localizableLabel2, FontTL.ROBOTO_BOLD, 14, Color.BLACK);
/* 196 */     SwingUtil.changeFontFamily(this.selectedMemory, FontTL.ROBOTO_BOLD, 14, Color.BLACK);
/*     */     
/* 198 */     SwingUtil.changeFontFamily(this.save, FontTL.ROBOTO_REGULAR, 12, Color.WHITE);
/* 199 */     SwingUtil.changeFontFamily(this.open, FontTL.ROBOTO_REGULAR, 12, Color.WHITE);
/* 200 */     SwingUtil.changeFontFamily(this.remove, FontTL.ROBOTO_REGULAR, 12, Color.WHITE);
/*     */     
/* 202 */     spring.putConstraint("West", (Component)localizableLabel3, 30, "West", this.panel);
/* 203 */     spring.putConstraint("East", (Component)localizableLabel3, 267, "West", this.panel);
/* 204 */     spring.putConstraint("North", (Component)localizableLabel3, -3, "North", this.panel);
/* 205 */     spring.putConstraint("South", (Component)localizableLabel3, 15, "North", this.panel);
/* 206 */     this.panel.add((Component)localizableLabel3);
/*     */     
/* 208 */     spring.putConstraint("West", this.modpackName, 29, "West", this.panel);
/* 209 */     spring.putConstraint("East", this.modpackName, -27, "East", this.panel);
/* 210 */     spring.putConstraint("North", this.modpackName, 19, "North", this.panel);
/* 211 */     spring.putConstraint("South", this.modpackName, 63, "North", this.panel);
/* 212 */     this.panel.add(this.modpackName);
/*     */     
/* 214 */     spring.putConstraint("West", (Component)localizableLabel4, 32, "West", this.panel);
/* 215 */     spring.putConstraint("East", (Component)localizableLabel4, 139, "West", this.panel);
/* 216 */     spring.putConstraint("North", (Component)localizableLabel4, 10, "South", this.modpackName);
/* 217 */     spring.putConstraint("South", (Component)localizableLabel4, 28, "South", this.modpackName);
/* 218 */     this.panel.add((Component)localizableLabel4);
/*     */     
/* 220 */     spring.putConstraint("West", (Component)localizableLabel5, 162, "West", this.panel);
/* 221 */     spring.putConstraint("East", (Component)localizableLabel5, 289, "West", this.panel);
/* 222 */     spring.putConstraint("North", (Component)localizableLabel5, 10, "South", this.modpackName);
/* 223 */     spring.putConstraint("South", (Component)localizableLabel5, 28, "South", this.modpackName);
/* 224 */     this.panel.add((Component)localizableLabel5);
/*     */     
/* 226 */     spring.putConstraint("West", forgeVersionLabel, 332, "West", this.panel);
/* 227 */     spring.putConstraint("East", forgeVersionLabel, -27, "East", this.panel);
/* 228 */     spring.putConstraint("North", forgeVersionLabel, 10, "South", this.modpackName);
/* 229 */     spring.putConstraint("South", forgeVersionLabel, 28, "South", this.modpackName);
/* 230 */     this.panel.add(forgeVersionLabel);
/*     */     
/* 232 */     spring.putConstraint("West", (Component)localizableLabel6, 29, "West", this.panel);
/* 233 */     spring.putConstraint("East", (Component)localizableLabel6, 139, "West", this.panel);
/* 234 */     spring.putConstraint("North", (Component)localizableLabel6, 4, "South", (Component)localizableLabel5);
/* 235 */     spring.putConstraint("South", (Component)localizableLabel6, 48, "South", (Component)localizableLabel5);
/* 236 */     this.panel.add((Component)localizableLabel6);
/*     */     
/* 238 */     spring.putConstraint("West", gameVersionValue, 159, "West", this.panel);
/* 239 */     spring.putConstraint("East", gameVersionValue, 309, "West", this.panel);
/* 240 */     spring.putConstraint("North", gameVersionValue, 4, "South", (Component)localizableLabel5);
/* 241 */     spring.putConstraint("South", gameVersionValue, 48, "South", (Component)localizableLabel5);
/* 242 */     this.panel.add(gameVersionValue);
/*     */     
/* 244 */     spring.putConstraint("West", this.minecraftVersion, 329, "West", this.panel);
/* 245 */     spring.putConstraint("East", this.minecraftVersion, -27, "East", this.panel);
/* 246 */     spring.putConstraint("North", this.minecraftVersion, 4, "South", forgeVersionLabel);
/* 247 */     spring.putConstraint("South", this.minecraftVersion, 48, "South", forgeVersionLabel);
/* 248 */     this.panel.add(this.minecraftVersion);
/*     */     
/* 250 */     spring.putConstraint("West", (Component)localizableLabel2, 29, "West", this.panel);
/* 251 */     spring.putConstraint("East", (Component)localizableLabel2, 169, "East", this.panel);
/* 252 */     spring.putConstraint("North", (Component)localizableLabel2, 15, "South", this.minecraftVersion);
/* 253 */     spring.putConstraint("South", (Component)localizableLabel2, 33, "South", this.minecraftVersion);
/* 254 */     this.panel.add((Component)localizableLabel2);
/*     */     
/* 256 */     spring.putConstraint("West", (Component)this.skinCheckBox, 179, "West", this.panel);
/* 257 */     spring.putConstraint("East", (Component)this.skinCheckBox, -27, "East", this.panel);
/* 258 */     spring.putConstraint("North", (Component)this.skinCheckBox, 15, "South", this.minecraftVersion);
/* 259 */     spring.putConstraint("South", (Component)this.skinCheckBox, 33, "South", this.minecraftVersion);
/* 260 */     this.panel.add((Component)this.skinCheckBox);
/*     */     
/* 262 */     spring.putConstraint("West", (Component)localizableLabel1, 29, "West", this.panel);
/* 263 */     spring.putConstraint("East", (Component)localizableLabel1, 169, "East", this.panel);
/* 264 */     spring.putConstraint("North", (Component)localizableLabel1, 10, "South", (Component)this.skinCheckBox);
/* 265 */     spring.putConstraint("South", (Component)localizableLabel1, 33, "South", (Component)this.skinCheckBox);
/* 266 */     this.panel.add((Component)localizableLabel1);
/*     */     
/* 268 */     spring.putConstraint("West", this.box, 179, "West", this.panel);
/* 269 */     spring.putConstraint("East", this.box, -27, "East", this.panel);
/* 270 */     spring.putConstraint("North", this.box, 10, "South", (Component)this.skinCheckBox);
/* 271 */     spring.putConstraint("South", this.box, 33, "South", (Component)this.skinCheckBox);
/* 272 */     this.panel.add(this.box);
/*     */     
/* 274 */     spring.putConstraint("West", this.save, 29, "West", this.panel);
/* 275 */     spring.putConstraint("East", this.save, 168, "West", this.panel);
/* 276 */     spring.putConstraint("North", this.save, -43, "South", this.panel);
/* 277 */     spring.putConstraint("South", this.save, 0, "South", this.panel);
/* 278 */     this.panel.add(this.save);
/* 279 */     spring.putConstraint("West", this.open, 185, "West", this.panel);
/* 280 */     spring.putConstraint("East", this.open, 390, "West", this.panel);
/* 281 */     spring.putConstraint("North", this.open, -43, "South", this.panel);
/* 282 */     spring.putConstraint("South", this.open, 0, "South", this.panel);
/* 283 */     this.panel.add(this.open);
/* 284 */     spring.putConstraint("West", this.remove, 406, "West", this.panel);
/* 285 */     spring.putConstraint("East", this.remove, -27, "East", this.panel);
/* 286 */     spring.putConstraint("North", this.remove, -43, "South", this.panel);
/* 287 */     spring.putConstraint("South", this.remove, 0, "South", this.panel);
/* 288 */     this.panel.add(this.remove);
/*     */     
/* 290 */     this.box.addItemListener(e -> updateState(spring));
/* 291 */     this.box.setSelected(!version.getModpack().isModpackMemory());
/* 292 */     this.save.addActionListener(e -> {
/*     */           if (!this.box.isSelected()) {
/*     */             version.getModpack().setMemory(this.slider.getValue());
/*     */           }
/*     */           
/*     */           version.getModpack().setModpackMemory(!this.box.isSelected());
/*     */           
/*     */           this.controller.save(version, this.modpackName.getText(), this.skinCheckBox.isSelected(), ((MinecraftVersionDTO)this.minecraftVersion.getSelectedItem()).createFromCurrent());
/*     */           
/*     */           setVisible(false);
/*     */         });
/* 303 */     this.open.addActionListener(e -> {
/*     */           this.controller.open(version);
/*     */           
/*     */           setVisible(false);
/*     */         });
/* 308 */     this.remove.addActionListener(e -> {
/*     */           this.controller.remove(version);
/*     */           setVisible(false);
/*     */         });
/* 312 */     updateState(spring);
/* 313 */     addedVersions(modpackVersionDTO);
/*     */   }
/*     */   
/*     */   private void addedVersions(ModpackVersionDTO mv) {
/* 317 */     CompletableFuture.runAsync(() -> DesktopUtil.uncheckCall(()))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 327 */       .exceptionally(e -> {
/*     */           if (Objects.isNull(mv.getForgeVersionDTO())) {
/*     */             ForgeVersionDTO f = new ForgeVersionDTO();
/*     */             
/*     */             f.setName(mv.getForgeVersion());
/*     */             
/*     */             this.minecraftVersion.setModel(new DefaultComboBoxModel<>((MinecraftVersionDTO[])Lists.newArrayList((Object[])new ForgeVersionDTO[] { f }).toArray((Object[])new MinecraftVersionDTO[0])));
/*     */           } else {
/*     */             this.minecraftVersion.setModel(new DefaultComboBoxModel<>((MinecraftVersionDTO[])Lists.newArrayList((Object[])new ForgeVersionDTO[] { mv.getForgeVersionDTO() }).toArray((Object[])new MinecraftVersionDTO[0])));
/*     */           } 
/*     */           return null;
/*     */         });
/* 339 */     CompletableFuture.runAsync(() -> DesktopUtil.uncheckCall(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateState(SpringLayout spring) {
/* 346 */     if (this.box.isSelected()) {
/* 347 */       this.panel.remove(this.selectedMemory);
/* 348 */       this.panel.remove((Component)this.slider);
/* 349 */       this.panel.remove(this.question);
/* 350 */       setCenter(minSize);
/*     */     } else {
/* 352 */       spring.putConstraint("West", (Component)this.slider, 12, "West", this.panel);
/* 353 */       spring.putConstraint("East", (Component)this.slider, -13, "East", this.panel);
/* 354 */       spring.putConstraint("North", (Component)this.slider, -130, "South", this.panel);
/* 355 */       spring.putConstraint("South", (Component)this.slider, -60, "South", this.panel);
/* 356 */       this.panel.add((Component)this.slider);
/*     */       
/* 358 */       spring.putConstraint("West", this.selectedMemory, 29, "West", this.panel);
/* 359 */       spring.putConstraint("East", this.selectedMemory, 29 + 
/* 360 */           SwingUtil.getWidthText(this.selectedMemory, this.selectedMemory.getText()) + 5, "West", this.panel);
/*     */       
/* 362 */       spring.putConstraint("North", this.selectedMemory, -150, "South", this.panel);
/* 363 */       spring.putConstraint("South", this.selectedMemory, -132, "South", this.panel);
/* 364 */       this.panel.add(this.selectedMemory);
/* 365 */       spring.putConstraint("West", this.question, 2, "East", this.selectedMemory);
/* 366 */       spring.putConstraint("East", this.question, 25, "East", this.selectedMemory);
/* 367 */       spring.putConstraint("North", this.question, -151, "South", this.panel);
/* 368 */       spring.putConstraint("South", this.question, -131, "South", this.panel);
/* 369 */       this.panel.add(this.question);
/* 370 */       setCenter(maxSize);
/* 371 */       if (!this.c.isExist("memory.problem.message")) {
/* 372 */         this.question.setVisible(false);
/*     */       }
/*     */     } 
/* 375 */     this.panel.revalidate();
/* 376 */     this.panel.repaint();
/*     */   }
/*     */   
/*     */   private void updateSkinBoxButton(GameVersionDTO gameVersionDTO, ModpackVersionDTO mv) {
/* 380 */     if (this.skinCheckBox.isSelected())
/*     */       return; 
/* 382 */     SwingUtilities.invokeLater(() -> this.skinCheckBox.setEnabled(false));
/* 383 */     GameEntityDTO g = new GameEntityDTO();
/* 384 */     g.setId(ModDTO.TL_SKIN_CAPE_ID);
/*     */     try {
/* 386 */       this.manager.getInstallingGameEntity(GameType.MOD, g, null, gameVersionDTO, mv.findFirstMinecraftVersionType());
/* 387 */       SwingUtilities.invokeLater(() -> this.skinCheckBox.setEnabled(true));
/* 388 */     } catch (IOException e) {
/* 389 */       U.log(new Object[] { "not found tl skin cape for", gameVersionDTO.getName(), mv.findFirstMinecraftVersionType() });
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\ModpackConfigFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */