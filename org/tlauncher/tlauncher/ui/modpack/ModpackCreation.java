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
/*     */ import java.nio.file.InvalidPathException;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.List;
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
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.plaf.ComboBoxUI;
/*     */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*     */ import org.tlauncher.modpack.domain.client.GameVersionDTO;
/*     */ import org.tlauncher.modpack.domain.client.ModDTO;
/*     */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.modpack.domain.client.share.MinecraftVersionDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.NameIdDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.VersionMaturity;
/*     */ import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
/*     */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableTextField;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterFullButton;
/*     */ import org.tlauncher.tlauncher.ui.swing.renderer.CreationMinecraftTypeComboboxRenderer;
/*     */ import org.tlauncher.tlauncher.ui.swing.renderer.CreationModpackForgeComboboxRenderer;
/*     */ import org.tlauncher.tlauncher.ui.swing.renderer.CreationModpackGameVersionComboboxRenderer;
/*     */ import org.tlauncher.tlauncher.ui.ui.CreationMinecraftTypeComboboxUI;
/*     */ import org.tlauncher.tlauncher.ui.ui.CreationModpackForgeComboboxUI;
/*     */ import org.tlauncher.tlauncher.ui.ui.CreationModpackGameVersionComboboxUI;
/*     */ import org.tlauncher.util.ColorUtil;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.swing.FontTL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModpackCreation
/*     */   extends TemlateModpackFrame
/*     */ {
/*     */   private final LocalizableTextField versionName;
/*     */   private final JComboBox<GameVersionDTO> gameVersions;
/*     */   private final JComboBox<MinecraftVersionDTO> versionNameValue;
/*     */   private JComboBox<NameIdDTO> minecraftVersionTypes;
/*     */   private JButton create;
/*     */   private JCheckBox box;
/*     */   private JButton cancel;
/*     */   private EditorCheckBox tlskinCapeModBox;
/*  77 */   private static final Dimension maxSize = new Dimension(572, 479);
/*  78 */   private static final Dimension minSize = new Dimension(572, 374);
/*     */   private JLabel question;
/*     */   private Configuration c;
/*     */   private ModpackManager modpackManager;
/*     */   
/*     */   public ModpackCreation(JFrame parent) {
/*  84 */     super(parent, "modpack.creation.modpack", new Dimension(572, 479));
/*  85 */     this.modpackManager = (ModpackManager)TLauncher.getInjector().getInstance(ModpackManager.class);
/*  86 */     this.question = new JLabel(ImageCache.getNativeIcon("qestion-option-panel.png"));
/*  87 */     this.c = TLauncher.getInstance().getConfiguration();
/*  88 */     this.question.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mousePressed(MouseEvent e) {
/*  91 */             ModpackCreation.this.setVisible(false);
/*  92 */             Alert.showLocMessage(ModpackCreation.this.c.get("memory.problem.message"));
/*  93 */             ModpackCreation.this.setVisible(true);
/*     */           }
/*     */         });
/*  96 */     SpringLayout spring = new SpringLayout();
/*  97 */     JPanel panel = new JPanel(spring);
/*  98 */     panel.setBorder(new EmptyBorder(0, 0, 33, 0));
/*  99 */     addCenter(panel);
/* 100 */     panel.setBackground(Color.WHITE);
/*     */     
/* 102 */     JLabel nameLabel = new JLabel(Localizable.get("modpack.creation.name"));
/* 103 */     nameLabel.setForeground(ColorUtil.COLOR_149);
/* 104 */     LocalizableLabel localizableLabel1 = new LocalizableLabel("settings.java.memory.label");
/*     */     
/* 106 */     JLabel type = new JLabel(Localizable.get("version.manager.editor.field.type"));
/*     */     
/* 108 */     LocalizableLabel localizableLabel2 = new LocalizableLabel("modpack.config.memory.title");
/* 109 */     LocalizableLabel localizableLabel3 = new LocalizableLabel("modpack.config.system.label");
/* 110 */     this.box = (JCheckBox)new EditorCheckBox("modpack.config.memory.box");
/* 111 */     this.box.setIconTextGap(14);
/*     */     
/* 113 */     this.tlskinCapeModBox = new EditorCheckBox("modpack.config.skin.use");
/* 114 */     this.tlskinCapeModBox.setIconTextGap(14);
/* 115 */     this.tlskinCapeModBox.setSelected(true);
/*     */     
/* 117 */     JLabel versionGameLabel = new JLabel(Localizable.get("modpack.table.pack.element.version") + ":");
/* 118 */     JLabel versionLabel = new JLabel(Localizable.get("version.name.v1") + ":");
/*     */     
/* 120 */     this.create = (JButton)new UpdaterFullButton(COLOR_0_174_239, ColorUtil.BLUE_MODPACK_BUTTON_UP, "modpack.create.button", "modpack-creation.button.png");
/*     */     
/* 122 */     this.create.setEnabled(false);
/* 123 */     this.create.setBorder(new EmptyBorder(0, 29, 0, 0));
/*     */     
/* 125 */     this.create.setIconTextGap(42);
/*     */     
/* 127 */     this.cancel = (JButton)new UpdaterFullButton(new Color(208, 43, 43), new Color(180, 39, 39), "loginform.enter.cancel", "modpack-cancel-button.png");
/*     */     
/* 129 */     this.cancel.setBorder(new EmptyBorder(0, 29, 0, 0));
/* 130 */     this.cancel.setIconTextGap(42);
/*     */     
/* 132 */     this.gameVersions = new JComboBox<>();
/*     */     
/* 134 */     this.gameVersions.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorUtil.COLOR_149));
/* 135 */     this.gameVersions.setRenderer((ListCellRenderer<? super GameVersionDTO>)new CreationModpackGameVersionComboboxRenderer());
/* 136 */     this.gameVersions.setUI((ComboBoxUI)new CreationModpackGameVersionComboboxUI());
/*     */     
/* 138 */     this.minecraftVersionTypes = new JComboBox<>((NameIdDTO[])this.modpackManager.getMinecraftVersionTypes().toArray((Object[])new NameIdDTO[0]));
/* 139 */     this.minecraftVersionTypes.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorUtil.COLOR_149));
/* 140 */     this.minecraftVersionTypes.setRenderer((ListCellRenderer<? super NameIdDTO>)new CreationMinecraftTypeComboboxRenderer());
/* 141 */     this.minecraftVersionTypes.setUI((ComboBoxUI)new CreationMinecraftTypeComboboxUI());
/*     */     
/* 143 */     this.versionNameValue = new JComboBox<>();
/* 144 */     this.versionNameValue.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorUtil.COLOR_149));
/* 145 */     this.versionNameValue.setRenderer((ListCellRenderer<? super MinecraftVersionDTO>)new CreationModpackForgeComboboxRenderer());
/* 146 */     this.versionNameValue.setUI((ComboBoxUI)new CreationModpackForgeComboboxUI());
/*     */     
/* 148 */     this.versionName = new LocalizableTextField("modpack.creation.input.name")
/*     */       {
/*     */         protected void onFocusLost()
/*     */         {
/* 152 */           super.onFocusLost();
/* 153 */           if (getValue() == null) {
/* 154 */             ModpackCreation.this.versionName.setForeground(ColorUtil.COLOR_202);
/*     */           }
/*     */         }
/*     */         
/*     */         protected void onFocusGained() {
/* 159 */           super.onFocusGained();
/*     */           
/* 161 */           ModpackCreation.this.versionName.setForeground(ColorUtil.COLOR_25);
/*     */         }
/*     */       };
/*     */     
/* 165 */     this.versionName.setHorizontalAlignment(0);
/*     */     
/* 167 */     this.versionName.setBorder(BorderFactory.createLineBorder(ColorUtil.COLOR_149, 1));
/*     */     
/* 169 */     SliderModpackPanel slider = new SliderModpackPanel(new Dimension(534, 80));
/*     */     
/* 171 */     SwingUtil.changeFontFamily(type, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_149);
/* 172 */     SwingUtil.changeFontFamily(nameLabel, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_149);
/* 173 */     SwingUtil.changeFontFamily(versionLabel, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_149);
/* 174 */     SwingUtil.changeFontFamily(versionGameLabel, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_149);
/* 175 */     SwingUtil.changeFontFamily((JComponent)localizableLabel2, FontTL.ROBOTO_BOLD, 14, Color.BLACK);
/* 176 */     SwingUtil.changeFontFamily((JComponent)localizableLabel1, FontTL.ROBOTO_BOLD, 14, Color.BLACK);
/* 177 */     SwingUtil.changeFontFamily((JComponent)localizableLabel3, FontTL.ROBOTO_BOLD, 14, Color.BLACK);
/* 178 */     SwingUtil.changeFontFamily((JComponent)this.tlskinCapeModBox, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
/* 179 */     SwingUtil.changeFontFamily(this.create, FontTL.ROBOTO_BOLD, 12, Color.WHITE);
/* 180 */     SwingUtil.changeFontFamily(this.box, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
/* 181 */     SwingUtil.changeFontFamily(this.minecraftVersionTypes, FontTL.ROBOTO_REGULAR, 14, Color.BLACK);
/* 182 */     SwingUtil.changeFontFamily(this.gameVersions, FontTL.ROBOTO_BOLD, 14, ColorUtil.COLOR_25);
/* 183 */     SwingUtil.changeFontFamily(this.versionNameValue, FontTL.ROBOTO_BOLD, 14, ColorUtil.COLOR_25);
/* 184 */     SwingUtil.changeFontFamily(this.cancel, FontTL.ROBOTO_BOLD, 12, Color.WHITE);
/* 185 */     SwingUtil.changeFontFamily((JComponent)this.versionName, FontTL.ROBOTO_BOLD, 18, ColorUtil.COLOR_202);
/*     */     
/* 187 */     int height = 18;
/* 188 */     spring.putConstraint("West", nameLabel, 253, "West", panel);
/* 189 */     spring.putConstraint("East", nameLabel, 353, "West", panel);
/* 190 */     spring.putConstraint("North", nameLabel, 23, "North", panel);
/* 191 */     spring.putConstraint("South", nameLabel, 23 + height, "North", panel);
/* 192 */     panel.add(nameLabel);
/*     */     
/* 194 */     spring.putConstraint("West", (Component)this.versionName, 29, "West", panel);
/* 195 */     spring.putConstraint("East", (Component)this.versionName, -27, "East", panel);
/* 196 */     spring.putConstraint("North", (Component)this.versionName, 2, "South", nameLabel);
/* 197 */     spring.putConstraint("South", (Component)this.versionName, 46, "South", nameLabel);
/* 198 */     panel.add((Component)this.versionName);
/*     */     
/* 200 */     spring.putConstraint("West", type, 32, "West", panel);
/* 201 */     spring.putConstraint("East", type, 139, "West", panel);
/* 202 */     spring.putConstraint("North", type, 18, "South", (Component)this.versionName);
/* 203 */     spring.putConstraint("South", type, 18 + height, "South", (Component)this.versionName);
/* 204 */     panel.add(type);
/*     */     
/* 206 */     spring.putConstraint("West", versionGameLabel, 162, "West", panel);
/* 207 */     spring.putConstraint("East", versionGameLabel, 289, "West", panel);
/* 208 */     spring.putConstraint("North", versionGameLabel, 18, "South", (Component)this.versionName);
/* 209 */     spring.putConstraint("South", versionGameLabel, 18 + height, "South", (Component)this.versionName);
/* 210 */     panel.add(versionGameLabel);
/*     */     
/* 212 */     spring.putConstraint("West", versionLabel, 332, "West", panel);
/* 213 */     spring.putConstraint("East", versionLabel, 545, "West", panel);
/* 214 */     spring.putConstraint("North", versionLabel, 18, "South", (Component)this.versionName);
/* 215 */     spring.putConstraint("South", versionLabel, 18 + height, "South", (Component)this.versionName);
/* 216 */     panel.add(versionLabel);
/*     */     
/* 218 */     spring.putConstraint("West", this.minecraftVersionTypes, 29, "West", panel);
/* 219 */     spring.putConstraint("East", this.minecraftVersionTypes, 139, "West", panel);
/* 220 */     spring.putConstraint("North", this.minecraftVersionTypes, 5, "South", versionLabel);
/* 221 */     spring.putConstraint("South", this.minecraftVersionTypes, 49, "South", versionLabel);
/* 222 */     panel.add(this.minecraftVersionTypes);
/*     */     
/* 224 */     spring.putConstraint("West", this.gameVersions, 159, "West", panel);
/* 225 */     spring.putConstraint("East", this.gameVersions, 309, "West", panel);
/* 226 */     spring.putConstraint("North", this.gameVersions, 5, "South", versionLabel);
/* 227 */     spring.putConstraint("South", this.gameVersions, 49, "South", versionLabel);
/* 228 */     panel.add(this.gameVersions);
/*     */     
/* 230 */     spring.putConstraint("West", this.versionNameValue, 329, "West", panel);
/* 231 */     spring.putConstraint("East", this.versionNameValue, 545, "West", panel);
/* 232 */     spring.putConstraint("North", this.versionNameValue, 5, "South", versionLabel);
/* 233 */     spring.putConstraint("South", this.versionNameValue, 49, "South", versionLabel);
/* 234 */     panel.add(this.versionNameValue);
/*     */     
/* 236 */     spring.putConstraint("West", (Component)localizableLabel2, 29, "West", panel);
/* 237 */     spring.putConstraint("East", (Component)localizableLabel2, 229, "West", panel);
/* 238 */     spring.putConstraint("North", (Component)localizableLabel2, 15, "South", this.versionNameValue);
/* 239 */     spring.putConstraint("South", (Component)localizableLabel2, 32, "South", this.versionNameValue);
/* 240 */     panel.add((Component)localizableLabel2);
/*     */     
/* 242 */     spring.putConstraint("West", this.box, 179, "West", panel);
/* 243 */     spring.putConstraint("East", this.box, 529, "West", panel);
/* 244 */     spring.putConstraint("North", this.box, 15, "South", this.versionNameValue);
/* 245 */     spring.putConstraint("South", this.box, 32, "South", this.versionNameValue);
/* 246 */     panel.add(this.box);
/*     */     
/* 248 */     spring.putConstraint("West", (Component)localizableLabel3, 29, "West", panel);
/* 249 */     spring.putConstraint("East", (Component)localizableLabel3, 229, "West", this.versionNameValue);
/*     */     
/* 251 */     spring.putConstraint("North", (Component)localizableLabel3, 10, "South", (Component)localizableLabel2);
/* 252 */     spring.putConstraint("South", (Component)localizableLabel3, 27, "South", (Component)localizableLabel2);
/* 253 */     panel.add((Component)localizableLabel3);
/*     */     
/* 255 */     spring.putConstraint("West", (Component)this.tlskinCapeModBox, 179, "West", panel);
/* 256 */     spring.putConstraint("East", (Component)this.tlskinCapeModBox, 529, "West", panel);
/* 257 */     spring.putConstraint("North", (Component)this.tlskinCapeModBox, 10, "South", (Component)localizableLabel2);
/* 258 */     spring.putConstraint("South", (Component)this.tlskinCapeModBox, 27, "South", (Component)localizableLabel2);
/* 259 */     panel.add((Component)this.tlskinCapeModBox);
/*     */     
/* 261 */     spring.putConstraint("West", this.create, 29, "West", panel);
/* 262 */     spring.putConstraint("East", this.create, 267, "West", panel);
/* 263 */     spring.putConstraint("North", this.create, -43, "South", panel);
/* 264 */     spring.putConstraint("South", this.create, 0, "South", panel);
/* 265 */     panel.add(this.create);
/*     */     
/* 267 */     spring.putConstraint("West", this.cancel, 307, "West", panel);
/* 268 */     spring.putConstraint("East", this.cancel, 545, "West", panel);
/* 269 */     spring.putConstraint("North", this.cancel, -43, "South", panel);
/* 270 */     spring.putConstraint("South", this.cancel, 0, "South", panel);
/* 271 */     panel.add(this.cancel);
/*     */     
/* 273 */     this.create.addActionListener(e -> {
/*     */           String name = this.versionName.getValue();
/*     */           try {
/*     */             Paths.get(name, new String[0]);
/* 277 */           } catch (InvalidPathException|NullPointerException ex) {
/*     */             this.versionName.setBorder(BorderFactory.createLineBorder(new Color(255, 62, 62), 1));
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/*     */           if (!this.modpackManager.checkNameVersion(Lists.newArrayList((Object[])new String[] { name }))) {
/*     */             setVisible(false);
/*     */             Alert.showLocWarning("modpack.config.memory.message");
/*     */             setVisible(true);
/*     */             return;
/*     */           } 
/*     */           ModpackDTO modpackDTO = new ModpackDTO();
/*     */           modpackDTO.setId(Long.valueOf(-U.n()));
/*     */           ModpackVersionDTO v = new ModpackVersionDTO();
/*     */           v.setId(Long.valueOf(-U.n() - 1L));
/*     */           modpackDTO.setName(name);
/*     */           GameVersionDTO g = (GameVersionDTO)this.gameVersions.getSelectedItem();
/*     */           v.setGameVersionDTO(g);
/*     */           v.setName("1.0");
/*     */           v.setMinecraftVersionTypes(Lists.newArrayList((Object[])new NameIdDTO[] { (NameIdDTO)this.minecraftVersionTypes.getSelectedItem() }));
/*     */           v.setMinecraftVersionName(((MinecraftVersionDTO)this.versionNameValue.getSelectedItem()).createFromCurrent());
/*     */           modpackDTO.setVersion((VersionDTO)v);
/*     */           if (!this.box.isSelected()) {
/*     */             modpackDTO.setModpackMemory(true);
/*     */             modpackDTO.setMemory(slider.getValue());
/*     */           } 
/*     */           this.modpackManager.createModpack(name, modpackDTO, this.tlskinCapeModBox.isSelected());
/*     */           setVisible(false);
/*     */         });
/* 307 */     this.box.addItemListener(e -> {
/*     */           if (e.getStateChange() == 2) {
/*     */             setCenter(maxSize);
/*     */             
/*     */             spring.putConstraint("West", (Component)slider, 12, "West", panel);
/*     */             
/*     */             spring.putConstraint("East", (Component)slider, -13, "East", panel);
/*     */             
/*     */             spring.putConstraint("North", (Component)slider, 259, "North", panel);
/*     */             spring.putConstraint("South", (Component)slider, 339, "South", panel);
/*     */             panel.add((Component)slider);
/*     */             spring.putConstraint("West", selectedMemory, 29, "West", panel);
/*     */             spring.putConstraint("East", selectedMemory, 29 + SwingUtil.getWidthText(selectedMemory, selectedMemory.getText()) + 5, "West", panel);
/*     */             spring.putConstraint("North", selectedMemory, 245, "North", panel);
/*     */             spring.putConstraint("South", selectedMemory, 263, "North", panel);
/*     */             panel.add(selectedMemory);
/*     */             spring.putConstraint("West", this.question, 2, "East", selectedMemory);
/*     */             spring.putConstraint("East", this.question, 25, "East", selectedMemory);
/*     */             spring.putConstraint("North", this.question, -153, "South", panel);
/*     */             spring.putConstraint("South", this.question, -133, "South", panel);
/*     */             panel.add(this.question);
/*     */             setCenter(maxSize);
/*     */             if (!this.c.isExist("memory.problem.message")) {
/*     */               this.question.setVisible(false);
/*     */             }
/*     */           } else {
/*     */             panel.remove(selectedMemory);
/*     */             panel.remove((Component)slider);
/*     */             panel.remove(this.question);
/*     */             setCenter(minSize);
/*     */           } 
/*     */         });
/* 339 */     this.cancel.addActionListener(e -> setVisible(false));
/* 340 */     this.box.setSelected(true);
/* 341 */     this.minecraftVersionTypes.addItemListener(item -> {
/*     */           if (item.getStateChange() == 1) {
/*     */             getGameVersionsFields((NameIdDTO)item.getItem());
/*     */           }
/*     */         });
/* 346 */     this.gameVersions.addItemListener(e -> {
/*     */           if (e.getStateChange() == 1) {
/*     */             CompletableFuture.runAsync(()).exceptionally(());
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 360 */     getGameVersionsFields((NameIdDTO)this.minecraftVersionTypes.getSelectedItem());
/*     */   }
/*     */   
/*     */   private void getGameVersionsFields(NameIdDTO nameIdDTO) {
/* 364 */     this.create.setEnabled(false);
/* 365 */     setStatusTlSkinCapeModBox(false);
/* 366 */     CompletableFuture.runAsync(() -> DesktopUtil.uncheckCall(()))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 375 */       .exceptionally(e -> {
/*     */           U.log(new Object[] { e });
/*     */           return null;
/*     */         });
/*     */   }
/*     */   
/*     */   private void updateVersions(GameVersionDTO gameVersionDTO) throws IOException {
/* 382 */     NameIdDTO minecraftVersionType = (NameIdDTO)this.minecraftVersionTypes.getSelectedItem();
/*     */     
/* 384 */     List<MinecraftVersionDTO> list = this.modpackManager.getVersionsByGameVersionAndMinecraftVersionType(gameVersionDTO.getId(), minecraftVersionType);
/* 385 */     SwingUtilities.invokeLater(() -> {
/*     */           this.versionNameValue.setModel(new DefaultComboBoxModel<>((MinecraftVersionDTO[])list.toArray((Object[])new MinecraftVersionDTO[0])));
/*     */           list.stream().filter(()).findFirst().ifPresent(());
/*     */           this.create.setEnabled(true);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateSkinBoxButton(GameVersionDTO gameVersionDTO) {
/* 397 */     GameEntityDTO g = new GameEntityDTO();
/* 398 */     g.setId(ModDTO.TL_SKIN_CAPE_ID);
/* 399 */     NameIdDTO minecraftVersionType = (NameIdDTO)this.minecraftVersionTypes.getSelectedItem();
/*     */     try {
/* 401 */       this.modpackManager.getInstallingGameEntity(GameType.MOD, g, null, gameVersionDTO, (NameIdDTO)this.minecraftVersionTypes
/* 402 */           .getSelectedItem());
/* 403 */       setStatusTlSkinCapeModBox(true);
/* 404 */     } catch (IOException e) {
/* 405 */       U.log(new Object[] { "not found tl skin cape for", gameVersionDTO.getName(), minecraftVersionType.getName() });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setStatusTlSkinCapeModBox(boolean flag) {
/* 410 */     SwingUtilities.invokeLater(() -> {
/*     */           this.tlskinCapeModBox.setEnabled(flag);
/*     */           this.tlskinCapeModBox.setSelected(flag);
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\ModpackCreation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */