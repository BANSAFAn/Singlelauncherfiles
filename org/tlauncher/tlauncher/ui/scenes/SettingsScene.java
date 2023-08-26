/*     */ package org.tlauncher.tlauncher.ui.scenes;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.CardLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.plaf.ButtonUI;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*     */ import org.tlauncher.tlauncher.rmo.Bootstrapper;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.MainPane;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableRadioButton;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.login.LoginForm;
/*     */ import org.tlauncher.tlauncher.ui.server.BackPanel;
/*     */ import org.tlauncher.tlauncher.ui.settings.ConfidentialitySetting;
/*     */ import org.tlauncher.tlauncher.ui.settings.MinecraftSettings;
/*     */ import org.tlauncher.tlauncher.ui.settings.ResetView;
/*     */ import org.tlauncher.tlauncher.ui.settings.SettingsHandlerInterface;
/*     */ import org.tlauncher.tlauncher.ui.settings.TlauncherSettings;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.tlauncher.ui.ui.RadioSettingsUI;
/*     */ import org.tlauncher.util.ColorUtil;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.swing.FontTL;
/*     */ 
/*     */ public class SettingsScene
/*     */   extends PseudoScene
/*     */ {
/*  56 */   public static final Dimension SIZE = new Dimension(620, 529);
/*  57 */   private static final Color SWITCH_FOREGROUND = new Color(60, 170, 232);
/*  58 */   private static final Color LEFT_BUTTONS_BACKGROUND = new Color(88, 159, 42);
/*     */ 
/*     */ 
/*     */   
/*  62 */   public static final Color BACKGROUND = new Color(245, 245, 245);
/*     */   private final MainPane main;
/*     */   private ExtendedPanel base;
/*  65 */   private ButtonGroup buttonGroup = new ButtonGroup();
/*     */   public final TLauncher tlauncher;
/*     */   public final Configuration global;
/*  68 */   public static final Insets BUTTON_INSETS = new Insets(9, 19, 20, 19);
/*  69 */   public static final Insets CENTER_INSETS = new Insets(20, 19, 0, 19);
/*  70 */   public static String minecraft = "minecraft";
/*  71 */   public static String tlaucner = "tlauncher";
/*  72 */   public static String confidentiality = "confidentiality";
/*     */   private List<SettingsHandlerInterface> pages;
/*     */   JButton reset;
/*     */   private TlauncherSettings tlauncherSettings;
/*     */   private ConfidentialitySetting confidentialitySetting;
/*     */   
/*     */   public SettingsScene(MainPane main) {
/*  79 */     super(main);
/*  80 */     this.pages = new ArrayList<>();
/*  81 */     this.main = main;
/*  82 */     this.tlauncher = TLauncher.getInstance();
/*  83 */     this.global = this.tlauncher.getConfiguration();
/*  84 */     ExtendedPanel topPanel = new ExtendedPanel();
/*  85 */     SpringLayout sl_topPanel = new SpringLayout();
/*  86 */     this.base = new ExtendedPanel();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     BackPanel backPanel = new BackPanel("settings.title", new MouseAdapter() { public void mouseClicked(MouseEvent e) { SettingsScene.this.main.openDefaultScene(); } }, ImageCache.getIcon("back-arrow.png"));
/*  93 */     ExtendedPanel switchPanel = new ExtendedPanel();
/*  94 */     ExtendedPanel centerPanel = new ExtendedPanel();
/*  95 */     ExtendedPanel buttonPanel = new ExtendedPanel(new FlowLayout(0, 0, 0));
/*  96 */     MinecraftSettings minecraftSettings = (MinecraftSettings)TLauncher.getInjector().getInstance(MinecraftSettings.class);
/*  97 */     this.tlauncherSettings = new TlauncherSettings();
/*  98 */     this.confidentialitySetting = new ConfidentialitySetting();
/*     */     
/* 100 */     this.pages.add(this.tlauncherSettings);
/* 101 */     this.pages.add(minecraftSettings);
/* 102 */     this.pages.add(this.confidentialitySetting);
/*     */     
/* 104 */     this.base.setSize(SIZE);
/*     */     
/* 106 */     this.base.setOpaque(true);
/* 107 */     this.base.setBackground(BACKGROUND);
/*     */     
/* 109 */     switchPanel.setLayout(new GridLayout(1, 2, 0, 0));
/* 110 */     centerPanel.setLayout(new CardLayout(0, 0));
/* 111 */     this.base.setLayout(new BorderLayout(0, 0));
/*     */     
/* 113 */     topPanel.setPreferredSize(new Dimension(SIZE.width, 49));
/* 114 */     buttonPanel.setPreferredSize(new Dimension(SIZE.width, 55));
/* 115 */     centerPanel.setInsets(CENTER_INSETS);
/* 116 */     buttonPanel.setInsets(BUTTON_INSETS);
/*     */     
/* 118 */     topPanel.setLayout(sl_topPanel);
/*     */     
/* 120 */     sl_topPanel.putConstraint("North", (Component)backPanel, 0, "North", (Component)topPanel);
/* 121 */     sl_topPanel.putConstraint("West", (Component)backPanel, 0, "West", (Component)topPanel);
/* 122 */     sl_topPanel.putConstraint("South", (Component)backPanel, 25, "North", (Component)topPanel);
/* 123 */     sl_topPanel.putConstraint("East", (Component)backPanel, 0, "East", (Component)topPanel);
/* 124 */     topPanel.add((Component)backPanel);
/*     */     
/* 126 */     sl_topPanel.putConstraint("North", (Component)switchPanel, 0, "South", (Component)backPanel);
/* 127 */     sl_topPanel.putConstraint("West", (Component)switchPanel, 0, "West", (Component)topPanel);
/* 128 */     sl_topPanel.putConstraint("South", (Component)switchPanel, 0, "South", (Component)topPanel);
/* 129 */     sl_topPanel.putConstraint("East", (Component)switchPanel, 0, "East", (Component)topPanel);
/* 130 */     topPanel.add((Component)switchPanel);
/*     */     
/* 132 */     centerPanel.add(minecraft, (Component)minecraftSettings);
/* 133 */     centerPanel.add(tlaucner, (Component)this.tlauncherSettings);
/* 134 */     centerPanel.add(confidentiality, (Component)this.confidentialitySetting);
/*     */     
/* 136 */     LocalizableRadioButton button = createRadioButton("settings.tab.minecraft", e -> {
/*     */           CardLayout cl = (CardLayout)centerPanel.getLayout();
/*     */           cl.show((Container)centerPanel, minecraft);
/*     */           repaint();
/*     */         });
/* 141 */     LocalizableRadioButton buttonTl = createRadioButton("settings.tab.tlauncher", e -> {
/*     */           CardLayout cl = (CardLayout)centerPanel.getLayout();
/*     */           cl.show((Container)centerPanel, tlaucner);
/*     */           repaint();
/*     */         });
/* 146 */     LocalizableRadioButton buttonCN = createRadioButton("settings.tab.confidentiality", e -> {
/*     */           CardLayout cl = (CardLayout)centerPanel.getLayout();
/*     */           
/*     */           cl.show((Container)centerPanel, confidentiality);
/*     */           repaint();
/*     */         });
/* 152 */     button.addActionListener(new AbstractAction()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 155 */             if (SettingsScene.this.reset.isVisible()) {
/* 156 */               SettingsScene.this.reset.setVisible(!SettingsScene.this.reset.isVisible());
/*     */             }
/*     */           }
/*     */         });
/* 160 */     buttonTl.addActionListener(new AbstractAction()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 163 */             if (!SettingsScene.this.reset.isVisible()) {
/* 164 */               SettingsScene.this.reset.setVisible(!SettingsScene.this.reset.isVisible());
/*     */             }
/*     */           }
/*     */         });
/* 168 */     buttonCN.addActionListener(new AbstractAction()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 171 */             if (SettingsScene.this.reset.isVisible()) {
/* 172 */               SettingsScene.this.reset.setVisible(!SettingsScene.this.reset.isVisible());
/*     */             }
/*     */           }
/*     */         });
/* 176 */     button.setSelected(true);
/* 177 */     switchPanel.add((Component)button);
/* 178 */     switchPanel.add((Component)buttonTl);
/* 179 */     switchPanel.add((Component)buttonCN);
/*     */     
/* 181 */     fillButtons(buttonPanel);
/*     */     
/* 183 */     this.base.add((Component)centerPanel, "Center");
/* 184 */     this.base.add((Component)topPanel, "North");
/* 185 */     this.base.add((Component)buttonPanel, "South");
/* 186 */     add((Component)this.base);
/*     */     
/* 188 */     SwingUtil.changeFontFamily((Component)backPanel, FontTL.ROBOTO_BOLD);
/* 189 */     addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentShown(ComponentEvent e) {
/* 192 */             for (SettingsHandlerInterface settingsHandlerInterface : SettingsScene.this.pages) {
/* 193 */               settingsHandlerInterface.init();
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onResize() {
/* 201 */     super.onResize();
/* 202 */     this.base.setLocation((int)((getWidth() / 2) - SIZE.getWidth() / 2.0D), 
/* 203 */         (int)(((getHeight() - LoginForm.LOGIN_SIZE.height) / 2) - SIZE.getHeight() / 2.0D));
/*     */   }
/*     */   
/*     */   private LocalizableRadioButton createRadioButton(String name, ActionListener actionListener) {
/* 207 */     LocalizableRadioButton button = new LocalizableRadioButton(name);
/* 208 */     button.setUI((ButtonUI)new RadioSettingsUI(ImageCache.getImage("background-tab-off.png")));
/* 209 */     button.setOpaque(true);
/* 210 */     button.addActionListener(actionListener);
/* 211 */     button.setForeground(SWITCH_FOREGROUND);
/* 212 */     button.setFont(button.getFont().deriveFont(1, 16.0F));
/* 213 */     this.buttonGroup.add((AbstractButton)button);
/* 214 */     return button;
/*     */   }
/*     */   
/*     */   private void fillButtons(ExtendedPanel buttons) {
/* 218 */     Font font = (new JButton()).getFont().deriveFont(1, 13.0F);
/* 219 */     final UpdaterButton saveButton = new UpdaterButton(UpdaterButton.ORANGE_COLOR, "settings.save");
/* 220 */     saveButton.setFont(font);
/* 221 */     saveButton.addActionListener(e -> {
/*     */           for (SettingsHandlerInterface settingsHandlerInterface : this.pages) {
/*     */             if (!settingsHandlerInterface.validateSettings()) {
/*     */               return;
/*     */             }
/*     */           } 
/*     */           
/*     */           boolean restart = restartClient();
/*     */           
/*     */           if (this.tlauncherSettings.chooseChinaLocal() && !restart) {
/*     */             this.main.openDefaultScene();
/*     */             return;
/*     */           } 
/*     */           for (SettingsHandlerInterface settingsHandlerInterface : this.pages) {
/*     */             settingsHandlerInterface.setValues();
/*     */           }
/*     */           this.global.store();
/*     */           if (restart) {
/*     */             try {
/*     */               Bootstrapper.restartLauncher().start();
/* 241 */             } catch (Throwable e1) {
/*     */               U.log(new Object[] { e1 });
/*     */             } 
/*     */             System.exit(0);
/*     */           } 
/*     */           ((ModpackManager)TLauncher.getInjector().getInstance(ModpackManager.class)).resetInfoMod();
/*     */           this.main.openDefaultScene();
/*     */         });
/* 249 */     saveButton.setForeground(Color.WHITE);
/*     */     
/* 251 */     final UpdaterButton defaultButton = new UpdaterButton(UpdaterButton.GREEN_COLOR, "settings.default");
/* 252 */     defaultButton.setFont(font);
/* 253 */     defaultButton.addActionListener(e -> {
/*     */           if (Alert.showLocQuestion("settings.default.warning")) {
/*     */             for (SettingsHandlerInterface settingsHandlerInterface : this.pages) {
/*     */               settingsHandlerInterface.setDefaultSettings();
/*     */             }
/*     */           }
/*     */         });
/* 260 */     defaultButton.setForeground(Color.WHITE);
/*     */     
/* 262 */     int preferredHeight = 26;
/* 263 */     defaultButton.setPreferredSize(new Dimension(178, preferredHeight));
/*     */     
/* 265 */     saveButton.setPreferredSize(new Dimension(141, preferredHeight));
/*     */     
/* 267 */     defaultButton.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseEntered(MouseEvent e) {
/* 270 */             defaultButton.setBackground(SettingsScene.LEFT_BUTTONS_BACKGROUND);
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/* 275 */             defaultButton.setBackground(defaultButton.getBackgroundColor());
/*     */           }
/*     */         });
/* 278 */     saveButton.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseEntered(MouseEvent e) {
/* 281 */             saveButton.setBackground(ColorUtil.COLOR_204);
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/* 286 */             saveButton.setBackground(saveButton.getBackgroundColor());
/*     */           }
/*     */         });
/*     */     
/* 290 */     this.reset = (JButton)new UpdaterButton(new Color(222, 64, 43), new Color(222, 31, 8), Color.WHITE, "settings.reset.button");
/* 291 */     SwingUtil.setFontSize(this.reset, 13.0F, 1);
/* 292 */     this.reset.setPreferredSize(new Dimension(141, preferredHeight));
/* 293 */     this.reset.addActionListener(e -> {
/*     */           ResetView view = new ResetView();
/*     */           Alert.showMessage(Localizable.get("settings.reset.button"), (JPanel)view, new JButton[] { view.getResetAgain() });
/*     */         });
/* 297 */     this.reset.setVisible(false);
/* 298 */     buttons.add(this.reset);
/* 299 */     buttons.add(Box.createHorizontalStrut(100));
/* 300 */     buttons.add((Component)defaultButton);
/* 301 */     buttons.add(Box.createHorizontalStrut(20));
/* 302 */     buttons.add((Component)saveButton);
/*     */   }
/*     */   
/*     */   private boolean restartClient() {
/* 306 */     if (this.tlauncherSettings.chooseChinaLocal()) {
/* 307 */       return Alert.showLocQuestion("tlauncher.restart", "tlauncher.restart.message");
/*     */     }
/* 309 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\scenes\SettingsScene.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */