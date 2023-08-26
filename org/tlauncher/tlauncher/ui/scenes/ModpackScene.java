/*      */ package org.tlauncher.tlauncher.ui.scenes;
/*      */ 
/*      */ import by.gdev.util.DesktopUtil;
/*      */ import com.google.inject.Injector;
/*      */ import com.google.inject.Key;
/*      */ import com.google.inject.name.Names;
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.CardLayout;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FlowLayout;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.GridLayout;
/*      */ import java.awt.MouseInfo;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.AdjustmentListener;
/*      */ import java.awt.event.ComponentAdapter;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.ItemEvent;
/*      */ import java.awt.event.ItemListener;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseMotionAdapter;
/*      */ import java.io.IOException;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import javax.swing.BorderFactory;
/*      */ import javax.swing.Box;
/*      */ import javax.swing.BoxLayout;
/*      */ import javax.swing.ButtonGroup;
/*      */ import javax.swing.ComboBoxModel;
/*      */ import javax.swing.DefaultComboBoxModel;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JComboBox;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JFrame;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JLayeredPane;
/*      */ import javax.swing.JMenuItem;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.JRadioButton;
/*      */ import javax.swing.ListCellRenderer;
/*      */ import javax.swing.SpringLayout;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.Timer;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.border.EmptyBorder;
/*      */ import javax.swing.event.DocumentListener;
/*      */ import javax.swing.event.PopupMenuEvent;
/*      */ import javax.swing.event.PopupMenuListener;
/*      */ import javax.swing.plaf.ComboBoxUI;
/*      */ import javax.swing.plaf.ScrollBarUI;
/*      */ import net.minecraft.launcher.versions.CompleteVersion;
/*      */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*      */ import org.tlauncher.modpack.domain.client.GameVersionDTO;
/*      */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*      */ import org.tlauncher.modpack.domain.client.SubModpackDTO;
/*      */ import org.tlauncher.modpack.domain.client.share.CategoryDTO;
/*      */ import org.tlauncher.modpack.domain.client.share.GameEntitySort;
/*      */ import org.tlauncher.modpack.domain.client.share.GameType;
/*      */ import org.tlauncher.modpack.domain.client.share.NameIdDTO;
/*      */ import org.tlauncher.modpack.domain.client.site.CommonPage;
/*      */ import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
/*      */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*      */ import org.tlauncher.tlauncher.exceptions.RequiredTLAccountException;
/*      */ import org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException;
/*      */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*      */ import org.tlauncher.tlauncher.minecraft.crash.Crash;
/*      */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
/*      */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener;
/*      */ import org.tlauncher.tlauncher.modpack.ModpackUtil;
/*      */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*      */ import org.tlauncher.tlauncher.ui.MainPane;
/*      */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*      */ import org.tlauncher.tlauncher.ui.button.StateModpackElementButton;
/*      */ import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
/*      */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*      */ import org.tlauncher.tlauncher.ui.listener.mods.DeferredDocumentListener;
/*      */ import org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener;
/*      */ import org.tlauncher.tlauncher.ui.listener.mods.GameEntityPanel;
/*      */ import org.tlauncher.tlauncher.ui.listener.mods.ModpackBoxListener;
/*      */ import org.tlauncher.tlauncher.ui.listener.mods.ModpackSearchListener;
/*      */ import org.tlauncher.tlauncher.ui.listener.mods.RightPanelAdjustmentListener;
/*      */ import org.tlauncher.tlauncher.ui.listener.mods.UpdateFavoriteValueListener;
/*      */ import org.tlauncher.tlauncher.ui.listener.mods.UpdateGameListener;
/*      */ import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
/*      */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*      */ import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
/*      */ import org.tlauncher.tlauncher.ui.loc.LocalizableHTMLLabel;
/*      */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*      */ import org.tlauncher.tlauncher.ui.loc.LocalizableTextField;
/*      */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*      */ import org.tlauncher.tlauncher.ui.loc.UpdaterFullButton;
/*      */ import org.tlauncher.tlauncher.ui.loc.modpack.ShadowButton;
/*      */ import org.tlauncher.tlauncher.ui.login.LoginForm;
/*      */ import org.tlauncher.tlauncher.ui.menu.ModpackPopup;
/*      */ import org.tlauncher.tlauncher.ui.model.CategoryComboBoxModel;
/*      */ import org.tlauncher.tlauncher.ui.modpack.ModpackConfigFrame;
/*      */ import org.tlauncher.tlauncher.ui.modpack.ModpackCreation;
/*      */ import org.tlauncher.tlauncher.ui.modpack.filter.BaseModpackFilter;
/*      */ import org.tlauncher.tlauncher.ui.modpack.filter.Filter;
/*      */ import org.tlauncher.tlauncher.ui.modpack.filter.NameFilter;
/*      */ import org.tlauncher.tlauncher.ui.modpack.right.panel.GameEntityRightPanel;
/*      */ import org.tlauncher.tlauncher.ui.server.BackPanel;
/*      */ import org.tlauncher.tlauncher.ui.swing.GameRadioTextButton;
/*      */ import org.tlauncher.tlauncher.ui.swing.ImageButton;
/*      */ import org.tlauncher.tlauncher.ui.swing.ScrollPane;
/*      */ import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
/*      */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedButton;
/*      */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*      */ import org.tlauncher.tlauncher.ui.swing.renderer.CategoryListRenderer;
/*      */ import org.tlauncher.tlauncher.ui.swing.renderer.CreationMinecraftTypeComboboxRenderer;
/*      */ import org.tlauncher.tlauncher.ui.swing.renderer.CreationModpackGameVersionComboboxRenderer;
/*      */ import org.tlauncher.tlauncher.ui.swing.renderer.ModpackComboxRenderer;
/*      */ import org.tlauncher.tlauncher.ui.swing.renderer.UserCategoryListRenderer;
/*      */ import org.tlauncher.tlauncher.ui.ui.CategoryComboBoxUI;
/*      */ import org.tlauncher.tlauncher.ui.ui.CreationMinecraftTypeComboboxUI;
/*      */ import org.tlauncher.tlauncher.ui.ui.CreationModpackGameVersionComboboxUI;
/*      */ import org.tlauncher.tlauncher.ui.ui.ModpackComboBoxUI;
/*      */ import org.tlauncher.tlauncher.ui.ui.ModpackScrollBarUI;
/*      */ import org.tlauncher.util.ColorUtil;
/*      */ import org.tlauncher.util.OS;
/*      */ import org.tlauncher.util.SwingUtil;
/*      */ import org.tlauncher.util.U;
/*      */ import org.tlauncher.util.swing.FontTL;
/*      */ 
/*      */ public class ModpackScene extends PseudoScene implements MinecraftListener, UpdateFavoriteValueListener {
/*  146 */   public static final Dimension SIZE = new Dimension(MainPane.SIZE.width, 585);
/*  147 */   public static final Color UP_BACKGROUND_PANEL_COLOR = new Color(60, 170, 252);
/*  148 */   public static final Color BACKGROUND_RIGHT_UNDER_PANEL = new Color(241, 241, 241);
/*      */   public static final int WIDTH_SEARCH_PANEL = 215;
/*  150 */   public static int LEFT_WIDTH = 234; public static int RIGHT_WIDTH = SIZE.width - LEFT_WIDTH;
/*  151 */   public static String EMPTY = "EMPTY"; public static String NOT_EMPTY = "NOT_EMPTY"; public static String PREPARING = "PREPARING";
/*  152 */   protected final ModpackComboBox localmodpacks = new ModpackComboBox();
/*      */   private final JComboBox<NameIdDTO> minecraftVersionTypes;
/*      */   private final JComboBox<GameVersionDTO> gameVersions;
/*  155 */   private final ExtendedPanel panel = new ExtendedPanel();
/*      */   private final JPanel layoutCenterPanel;
/*      */   private final GameEntityRightPanel modpacksPanel;
/*      */   private final GameEntityRightPanel modsPanel;
/*      */   private final GameEntityRightPanel resourcePackPanel;
/*      */   private final GameEntityRightPanel mapsPanel;
/*      */   private final GameEntityRightPanel shaderpacksPanel;
/*      */   private final ExtendedPanel entitiesPanel;
/*      */   private final Injector injector;
/*  164 */   private final GameEntityLeftPanel modLeftPanel = new GameEntityLeftPanel();
/*  165 */   private final GameEntityLeftPanel mapLeftPanel = new GameEntityLeftPanel();
/*  166 */   private final GameEntityLeftPanel shaderpackLeftPanel = new GameEntityLeftPanel();
/*  167 */   private final GameEntityLeftPanel resourceLeftPanel = new GameEntityLeftPanel();
/*      */   
/*      */   private final SearchPanel search;
/*      */   private final GameRadioTextButton modpacks;
/*  171 */   private final ItemListener modpackBoxListener = (ItemListener)new ModpackBoxListener();
/*      */   private final EditorCheckBox favoriteCheckBox;
/*      */   private final ModpackManager manager;
/*  174 */   private GameType current = GameType.MODPACK;
/*  175 */   private GameType last = this.current;
/*  176 */   private final ButtonGroup group = new ButtonGroup();
/*      */   private final JComboBox<CategoryDTO> categoriesBox;
/*      */   private final JComboBox<GameEntitySort> sortBox;
/*  179 */   private final Map<GameType, JRadioButton> mapGameTypeAndRadioButton = new HashMap<>();
/*      */   
/*      */   private final ExecutorService modpackExecutorService;
/*      */   private final NameIdDTO anyVersionType;
/*      */   private final GameVersionDTO anyGameVersion;
/*      */   private final JLabel found;
/*      */   private final JPanel searchPanel;
/*      */   private NameIdDTO selectedVersionType;
/*      */   private GameVersionDTO selectedGameVersion;
/*      */   
/*      */   public ModpackScene(MainPane main) {
/*  190 */     super(main);
/*      */     
/*  192 */     this.search = new SearchPanel();
/*  193 */     this.categoriesBox = new JComboBox<>();
/*  194 */     this.categoriesBox.setModel((ComboBoxModel<CategoryDTO>)new CategoryComboBoxModel(new CategoryDTO[0]));
/*  195 */     this.modpacksPanel = new GameEntityRightPanel(this.localmodpacks, GameType.MODPACK);
/*  196 */     this.modsPanel = new GameEntityRightPanel(this.localmodpacks, GameType.MOD);
/*  197 */     this.resourcePackPanel = new GameEntityRightPanel(this.localmodpacks, GameType.RESOURCEPACK);
/*  198 */     this.mapsPanel = new GameEntityRightPanel(this.localmodpacks, GameType.MAP);
/*  199 */     this.shaderpacksPanel = new GameEntityRightPanel(this.localmodpacks, GameType.SHADERPACK);
/*      */     
/*  201 */     this.injector = TLauncher.getInjector();
/*  202 */     this
/*  203 */       .modpackExecutorService = (ExecutorService)this.injector.getInstance(Key.get(ExecutorService.class, (Annotation)Names.named("modpackExecutorService")));
/*  204 */     this.anyVersionType = (NameIdDTO)this.injector.getInstance(Key.get(NameIdDTO.class, (Annotation)Names.named("anyVersionType")));
/*  205 */     this.anyGameVersion = (GameVersionDTO)this.injector.getInstance(Key.get(GameVersionDTO.class, (Annotation)Names.named("anyGameVersion")));
/*      */     
/*  207 */     this.layoutCenterPanel = new JPanel(new CardLayout(0, 0));
/*      */     
/*  209 */     this.manager = (ModpackManager)this.injector.getInstance(ModpackManager.class);
/*      */     
/*  211 */     this.localmodpacks.setPreferredSize(new Dimension(200, 30));
/*      */     
/*  213 */     ExtendedPanel buttons = new ExtendedPanel()
/*      */       {
/*      */         protected void paintComponent(Graphics g0) {
/*  216 */           g0.drawImage(ImageCache.getBufferedImage("modpack-top2-background.png"), 0, 0, null);
/*      */         }
/*      */       };
/*      */     
/*  220 */     buttons.setLayout(new FlowLayout(0, 0, 0));
/*      */     
/*  222 */     JLayeredPane layeredPane = new JLayeredPane()
/*      */       {
/*      */         public boolean isOptimizedDrawingEnabled() {
/*  225 */           return false;
/*      */         }
/*      */       };
/*      */     
/*  229 */     layeredPane.setSize(SIZE);
/*  230 */     this.searchPanel = new JPanel();
/*  231 */     this.searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
/*  232 */     this.searchPanel.setVisible(false);
/*  233 */     this.searchPanel.addMouseMotionListener(new MouseMotionAdapter() {  }
/*      */       );
/*  235 */     this.searchPanel.setOpaque(true);
/*  236 */     this.searchPanel.setBackground(new Color(238, 238, 238));
/*      */     
/*  238 */     GridLayout gl = new GridLayout(9, 1);
/*  239 */     this.searchPanel.setLayout(gl);
/*  240 */     LocalizableLabel versionFieldType = new LocalizableLabel("version.manager.editor.field.type");
/*  241 */     versionFieldType.setHorizontalAlignment(0);
/*      */     
/*  243 */     this.minecraftVersionTypes = new JComboBox<>();
/*  244 */     DefaultComboBoxModel<NameIdDTO> dcm = new DefaultComboBoxModel<>();
/*  245 */     dcm.addElement(this.anyVersionType);
/*  246 */     this.minecraftVersionTypes.setModel(dcm);
/*  247 */     this.minecraftVersionTypes.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorUtil.COLOR_149));
/*  248 */     this.minecraftVersionTypes.setRenderer((ListCellRenderer<? super NameIdDTO>)new CreationMinecraftTypeComboboxRenderer());
/*  249 */     this.minecraftVersionTypes.setUI((ComboBoxUI)new CreationMinecraftTypeComboboxUI());
/*      */     
/*  251 */     JLabel versionGameLabel = new JLabel(Localizable.get("modpack.table.pack.element.version") + ":");
/*  252 */     versionGameLabel.setHorizontalAlignment(0);
/*      */     
/*  254 */     this.gameVersions = new JComboBox<>();
/*      */     
/*  256 */     this.gameVersions.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorUtil.COLOR_149));
/*  257 */     this.gameVersions.setRenderer((ListCellRenderer<? super GameVersionDTO>)new CreationModpackGameVersionComboboxRenderer());
/*  258 */     this.gameVersions.setUI((ComboBoxUI)new CreationModpackGameVersionComboboxUI());
/*      */     
/*  260 */     LocalizableLabel modpackFilterLabel = new LocalizableLabel("modpack.filter.label");
/*  261 */     modpackFilterLabel.setHorizontalAlignment(0);
/*      */     
/*  263 */     this.favoriteCheckBox = new EditorCheckBox("modpack.favorite");
/*  264 */     this.favoriteCheckBox.setIconTextGap(14);
/*  265 */     this.favoriteCheckBox.setSelected(false);
/*  266 */     this.favoriteCheckBox.setHorizontalAlignment(0);
/*      */     
/*  268 */     this.found = new JLabel();
/*  269 */     this.found.setHorizontalAlignment(0);
/*  270 */     UpdaterButton updaterButton1 = new UpdaterButton(ColorUtil.COLOR_213, ColorUtil.COLOR_195, "settings.reset.java");
/*      */     
/*  272 */     this.searchPanel.setBounds(SIZE.width - 215, 110, 215, SIZE.height - 110);
/*      */     
/*  274 */     ExtendedPanel center = new ExtendedPanel();
/*  275 */     this.entitiesPanel = new ExtendedPanel(new CardLayout(0, 0));
/*  276 */     this.panel.setSize(SIZE);
/*  277 */     SpringLayout spring = new SpringLayout();
/*  278 */     this.panel.setLayout(spring);
/*  279 */     this.panel.setOpaque(true);
/*      */     
/*  281 */     center.setLayout(new BorderLayout());
/*      */     
/*  283 */     center.setOpaque(true);
/*  284 */     center.setBackground(Color.WHITE);
/*      */     
/*  286 */     LocalizableLabel nameModpackLabel = new LocalizableLabel("modpack.button.modpack");
/*  287 */     nameModpackLabel.setHorizontalAlignment(0);
/*  288 */     nameModpackLabel.setForeground(Color.WHITE);
/*  289 */     LocalizableLabel categoryLabel = new LocalizableLabel("modpack.filter.label");
/*  290 */     categoryLabel.setHorizontalAlignment(0);
/*  291 */     categoryLabel.setForeground(Color.WHITE);
/*  292 */     categoryLabel.setPreferredSize(new Dimension(98, 55));
/*  293 */     UpdaterFullButton updaterFullButton = new UpdaterFullButton(new Color(54, 153, 208), ColorUtil.BACKGROUND_COMBO_BOX_POPUP_SELECTED, "modpack.create.button", "create-modpack.png");
/*      */ 
/*      */     
/*  296 */     updaterFullButton.setIconTextGap(16);
/*      */     
/*  298 */     LocalizableLabel localizableLabel1 = new LocalizableLabel("modpack.sort.label");
/*  299 */     ExtendedPanel extendedPanel1 = new ExtendedPanel(new FlowLayout(1, 0, 0));
/*  300 */     extendedPanel1.setBorder(BorderFactory.createEmptyBorder(7, 0, 8, 0));
/*  301 */     this.sortBox = new JComboBox<>(GameEntitySort.getClientSortList());
/*      */     
/*  303 */     this.sortBox.setUI((ComboBoxUI)new ModpackComboBoxUI()
/*      */         {
/*      */           public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
/*  306 */             g.setColor(ColorUtil.BACKGROUND_COMBO_BOX_POPUP);
/*  307 */             g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
/*  308 */             String name = Localizable.get("modpack." + (
/*  309 */                 (GameEntitySort)ModpackScene.this.sortBox.getSelectedItem()).toString().toLowerCase(Locale.ROOT));
/*  310 */             paintText(g, bounds, name);
/*      */           }
/*      */         });
/*  313 */     extendedPanel1.add(this.sortBox);
/*  314 */     this.sortBox.setBorder(BorderFactory.createLineBorder(ModpackComboxRenderer.LINE, 1));
/*  315 */     this.sortBox.setRenderer((ListCellRenderer<? super GameEntitySort>)new UserCategoryListRenderer());
/*      */     
/*  317 */     CategoryListRenderer categoryListRenderer = new CategoryListRenderer(this.categoriesBox);
/*      */     
/*  319 */     this.categoriesBox.setRenderer((ListCellRenderer<? super CategoryDTO>)categoryListRenderer);
/*  320 */     this.categoriesBox.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorUtil.COLOR_149));
/*  321 */     this.categoriesBox.setUI((ComboBoxUI)new CategoryComboBoxUI());
/*      */     
/*  323 */     GameRadioTextButton mods = new GameRadioTextButton("modpack.button.mod");
/*  324 */     GameRadioTextButton shaderpacks = new GameRadioTextButton("modpack.button.shaderpack");
/*  325 */     this.modpacks = new GameRadioTextButton("modpack.button.modpack");
/*  326 */     GameRadioTextButton resourcepacks = new GameRadioTextButton("modpack.button.resourcepack");
/*  327 */     GameRadioTextButton maps = new GameRadioTextButton("modpack.button.map");
/*      */     
/*  329 */     this.mapGameTypeAndRadioButton.put(GameType.MOD, mods);
/*  330 */     this.mapGameTypeAndRadioButton.put(GameType.MODPACK, this.modpacks);
/*  331 */     this.mapGameTypeAndRadioButton.put(GameType.RESOURCEPACK, resourcepacks);
/*  332 */     this.mapGameTypeAndRadioButton.put(GameType.MAP, maps);
/*  333 */     this.mapGameTypeAndRadioButton.put(GameType.SHADERPACK, shaderpacks);
/*  334 */     this.mapGameTypeAndRadioButton.values().forEach(this.group::add);
/*      */     
/*  336 */     final ImageButton settings = new ImageButton("settings-modpack.png");
/*  337 */     imageButton.setPreferredSize(new Dimension(66, 55));
/*  338 */     buttons.add((Component)imageButton);
/*  339 */     buttons.add(Box.createHorizontalStrut(29));
/*  340 */     buttons.add((Component)this.modpacks, Box.createHorizontalStrut(20));
/*  341 */     buttons.add((Component)mods, Box.createHorizontalStrut(20));
/*  342 */     buttons.add((Component)resourcepacks, Box.createHorizontalStrut(20));
/*  343 */     buttons.add((Component)maps, Box.createHorizontalStrut(20));
/*  344 */     buttons.add((Component)shaderpacks, Box.createHorizontalStrut(0));
/*  345 */     buttons.add(Box.createHorizontalStrut(35));
/*  346 */     UpdaterButton updaterButton2 = new UpdaterButton(ColorUtil.BLUE_MODPACK, ColorUtil.BACKGROUND_COMBO_BOX_POPUP_SELECTED, "modpack.filters");
/*      */     
/*  348 */     updaterButton2.setForeground(Color.WHITE);
/*  349 */     updaterButton2.setIconTextGap(15);
/*  350 */     updaterButton2.setPreferredSize(new Dimension(192, 40));
/*  351 */     buttons.add((Component)updaterButton2);
/*      */     
/*  353 */     this.layoutCenterPanel.add((Component)this.entitiesPanel, GameType.NOT_MODPACK.toLowerCase());
/*      */     
/*  355 */     CardLayout emptyRightLayout = new CardLayout();
/*  356 */     ExtendedPanel extendedPanel2 = new ExtendedPanel(emptyRightLayout);
/*  357 */     extendedPanel2.add((Component)new EmptyRightView(GameType.MODPACK), EMPTY);
/*  358 */     extendedPanel2.add((Component)this.modpacksPanel, NOT_EMPTY);
/*      */     
/*  360 */     ScrollPane sp = createScrollWrapper((JComponent)extendedPanel2);
/*  361 */     sp.getVerticalScrollBar().addAdjustmentListener((AdjustmentListener)new RightPanelAdjustmentListener(this.modpacksPanel, this));
/*  362 */     this.layoutCenterPanel.add((Component)sp, GameType.MODPACK.toLowerCase());
/*      */     
/*  364 */     this.entitiesPanel.add((Component)new GameCenterPanel(this.modLeftPanel, this.modsPanel, GameType.MOD), GameType.MOD.toLowerCase());
/*  365 */     this.entitiesPanel.add((Component)new GameCenterPanel(this.resourceLeftPanel, this.resourcePackPanel, GameType.RESOURCEPACK), GameType.RESOURCEPACK
/*  366 */         .toLowerCase());
/*  367 */     this.entitiesPanel.add((Component)new GameCenterPanel(this.mapLeftPanel, this.mapsPanel, GameType.MAP), GameType.MAP.toLowerCase());
/*  368 */     this.entitiesPanel.add((Component)new GameCenterPanel(this.shaderpackLeftPanel, this.shaderpacksPanel, GameType.SHADERPACK), GameType.SHADERPACK
/*  369 */         .toLowerCase());
/*      */     
/*  371 */     center.add(this.layoutCenterPanel, "Center");
/*  372 */     JPanel upPanel = new JPanel(new FlowLayout(0, 0, 0));
/*  373 */     upPanel.setBackground(new Color(60, 170, 232));
/*  374 */     BackPanel back = new UniverseBackPanel(new Color(54, 153, 208));
/*      */     
/*  376 */     back.setPreferredSize(new Dimension(66, 55));
/*  377 */     nameModpackLabel.setPreferredSize(new Dimension(114, 55));
/*  378 */     this.localmodpacks.setPreferredSize(new Dimension(172, 36));
/*  379 */     updaterFullButton.setPreferredSize(new Dimension(132, 36));
/*  380 */     this.search.setPreferredSize(new Dimension(194, 55));
/*  381 */     JPanel gup1 = new JPanel();
/*  382 */     gup1.setPreferredSize(new Dimension(1, 55));
/*  383 */     Color line = new Color(92, 190, 246);
/*  384 */     gup1.setBackground(line);
/*  385 */     JPanel gup2 = new JPanel();
/*  386 */     gup2.setPreferredSize(new Dimension(1, 55));
/*  387 */     gup2.setBackground(line);
/*  388 */     localizableLabel1.setPreferredSize(new Dimension(125, 55));
/*  389 */     localizableLabel1.setHorizontalAlignment(0);
/*  390 */     this.sortBox.setPreferredSize(new Dimension(192, 40));
/*  391 */     extendedPanel1.setPreferredSize(new Dimension(192, 55));
/*      */     
/*  393 */     upPanel.add((Component)back);
/*  394 */     upPanel.add((Component)nameModpackLabel);
/*  395 */     upPanel.add((Component)this.localmodpacks);
/*  396 */     upPanel.add(Box.createHorizontalStrut(16));
/*  397 */     upPanel.add((Component)updaterFullButton);
/*  398 */     upPanel.add(Box.createHorizontalStrut(34));
/*      */     
/*  400 */     upPanel.add(gup1);
/*  401 */     upPanel.add(this.search);
/*  402 */     upPanel.add(gup2);
/*  403 */     upPanel.add((Component)localizableLabel1);
/*  404 */     upPanel.add((Component)extendedPanel1);
/*      */     
/*  406 */     spring.putConstraint("West", upPanel, 0, "West", (Component)this.panel);
/*  407 */     spring.putConstraint("East", upPanel, 0, "East", (Component)this.panel);
/*  408 */     spring.putConstraint("North", upPanel, 0, "North", (Component)this.panel);
/*  409 */     spring.putConstraint("South", upPanel, 55, "North", (Component)this.panel);
/*  410 */     this.panel.add(upPanel);
/*      */     
/*  412 */     spring.putConstraint("West", (Component)buttons, 0, "West", (Component)this.panel);
/*  413 */     spring.putConstraint("East", (Component)buttons, 0, "East", (Component)this.panel);
/*  414 */     spring.putConstraint("North", (Component)buttons, 0, "South", upPanel);
/*  415 */     spring.putConstraint("South", (Component)buttons, 55, "South", upPanel);
/*  416 */     this.panel.add((Component)buttons);
/*      */     
/*  418 */     spring.putConstraint("West", (Component)center, 0, "West", (Component)this.panel);
/*  419 */     spring.putConstraint("East", (Component)center, 0, "East", (Component)this.panel);
/*  420 */     spring.putConstraint("North", (Component)center, 110, "North", (Component)this.panel);
/*  421 */     spring.putConstraint("South", (Component)center, 0, "South", (Component)this.panel);
/*      */     
/*  423 */     this.panel.add((Component)center);
/*      */     
/*  425 */     layeredPane.add(this.searchPanel);
/*  426 */     layeredPane.add((Component)this.panel);
/*  427 */     add(layeredPane);
/*      */     
/*  429 */     this.searchPanel.add((Component)versionFieldType);
/*  430 */     this.searchPanel.add(this.minecraftVersionTypes);
/*  431 */     this.searchPanel.add(versionGameLabel);
/*  432 */     this.searchPanel.add(this.gameVersions);
/*  433 */     this.searchPanel.add((Component)modpackFilterLabel);
/*  434 */     this.searchPanel.add(this.categoriesBox);
/*  435 */     this.searchPanel.add((Component)this.favoriteCheckBox);
/*  436 */     this.searchPanel.add(this.found);
/*  437 */     this.searchPanel.add((Component)updaterButton1);
/*      */     
/*  439 */     SwingUtil.changeFontFamily((JComponent)this.localmodpacks, FontTL.ROBOTO_REGULAR, 14);
/*  440 */     SwingUtil.changeFontFamily(this.search, FontTL.ROBOTO_REGULAR, 14);
/*  441 */     SwingUtil.changeFontFamily(this.sortBox, FontTL.ROBOTO_REGULAR, 14);
/*  442 */     SwingUtil.changeFontFamily(this.categoriesBox, FontTL.ROBOTO_REGULAR, 14);
/*  443 */     SwingUtil.changeFontFamily((JComponent)updaterButton2, FontTL.ROBOTO_REGULAR, 14);
/*      */     
/*  445 */     SwingUtil.changeFontFamily((JComponent)nameModpackLabel, FontTL.ROBOTO_REGULAR, 14);
/*  446 */     SwingUtil.changeFontFamily((JComponent)updaterFullButton, FontTL.ROBOTO_REGULAR, 14);
/*  447 */     SwingUtil.changeFontFamily((JComponent)localizableLabel1, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
/*  448 */     SwingUtil.changeFontFamily((JComponent)mods, FontTL.ROBOTO_REGULAR, 14);
/*  449 */     SwingUtil.changeFontFamily((JComponent)shaderpacks, FontTL.ROBOTO_REGULAR, 14);
/*  450 */     SwingUtil.changeFontFamily((JComponent)this.modpacks, FontTL.ROBOTO_REGULAR, 14);
/*  451 */     SwingUtil.changeFontFamily((JComponent)resourcepacks, FontTL.ROBOTO_REGULAR, 14);
/*  452 */     SwingUtil.changeFontFamily((JComponent)maps, FontTL.ROBOTO_REGULAR, 14);
/*  453 */     SwingUtil.changeFontFamily((JComponent)categoryLabel, FontTL.ROBOTO_MEDIUM, 14, Color.WHITE);
/*      */     
/*  455 */     SwingUtil.changeFontFamily((JComponent)versionFieldType, FontTL.ROBOTO_MEDIUM, 14);
/*  456 */     SwingUtil.changeFontFamily(this.minecraftVersionTypes, FontTL.ROBOTO_MEDIUM, 14);
/*  457 */     SwingUtil.changeFontFamily(versionGameLabel, FontTL.ROBOTO_MEDIUM, 14);
/*  458 */     SwingUtil.changeFontFamily(this.gameVersions, FontTL.ROBOTO_MEDIUM, 14);
/*  459 */     SwingUtil.changeFontFamily((JComponent)modpackFilterLabel, FontTL.ROBOTO_MEDIUM, 14);
/*  460 */     SwingUtil.changeFontFamily(this.categoriesBox, FontTL.ROBOTO_MEDIUM, 14);
/*  461 */     SwingUtil.changeFontFamily((JComponent)this.favoriteCheckBox, FontTL.ROBOTO_MEDIUM, 14);
/*  462 */     SwingUtil.changeFontFamily(this.found, FontTL.ROBOTO_MEDIUM, 14);
/*      */     
/*  464 */     updaterFullButton.addActionListener(e -> {
/*      */           if (this.searchPanel.isVisible()) {
/*      */             this.searchPanel.setVisible(false);
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           CompletableFuture.runAsync((), this.modpackExecutorService).handle(());
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  481 */     updaterFullButton.addMouseListener(new MouseAdapter()
/*      */         {
/*      */           public void mouseEntered(MouseEvent e) {}
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  489 */     this.modpacks.addActionListener(e -> this.current = GameType.MODPACK);
/*      */ 
/*      */     
/*  492 */     mods.addActionListener(e -> this.current = GameType.MOD);
/*      */ 
/*      */     
/*  495 */     resourcepacks.addActionListener(e -> this.current = GameType.RESOURCEPACK);
/*      */ 
/*      */     
/*  498 */     maps.addActionListener(e -> this.current = GameType.MAP);
/*      */ 
/*      */     
/*  501 */     shaderpacks.addActionListener(e -> this.current = GameType.SHADERPACK);
/*      */ 
/*      */ 
/*      */     
/*  505 */     this.sortBox.addItemListener(e -> fillGameEntitiesPanel(true));
/*      */ 
/*      */     
/*  508 */     imageButton.addMouseListener(new MouseAdapter()
/*      */         {
/*      */           public void mousePressed(MouseEvent e) {
/*  511 */             if (SwingUtilities.isLeftMouseButton(e)) {
/*      */               
/*  513 */               ModpackPopup modpackPopup = new ModpackPopup(ModpackScene.this.localmodpacks);
/*  514 */               modpackPopup.show((Component)settings, e.getX(), e.getY());
/*      */             } 
/*      */           }
/*      */         });
/*  518 */     final ActionListener modpackListener = e -> SwingUtilities.invokeLater(());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  535 */     this.localmodpacks.addPopupMenuListener(new PopupMenuListener()
/*      */         {
/*      */           public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
/*  538 */             ModpackScene.this.localmodpacks.addActionListener(modpackListener);
/*      */           }
/*      */ 
/*      */           
/*      */           public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
/*  543 */             ModpackScene.this.localmodpacks.removeActionListener(modpackListener);
/*      */           }
/*      */ 
/*      */           
/*      */           public void popupMenuCanceled(PopupMenuEvent e) {
/*  548 */             ModpackScene.this.localmodpacks.removeActionListener(modpackListener);
/*      */           }
/*      */         });
/*      */ 
/*      */     
/*  553 */     this.manager.addGameListener(GameType.MOD, (GameEntityListener)this.modLeftPanel);
/*  554 */     this.manager.addGameListener(GameType.MOD, (GameEntityListener)this.modsPanel);
/*  555 */     this.manager.addGameListener(GameType.RESOURCEPACK, (GameEntityListener)this.resourceLeftPanel);
/*  556 */     this.manager.addGameListener(GameType.RESOURCEPACK, (GameEntityListener)this.resourcePackPanel);
/*  557 */     this.manager.addGameListener(GameType.MAP, (GameEntityListener)this.mapLeftPanel);
/*  558 */     this.manager.addGameListener(GameType.MAP, (GameEntityListener)this.mapsPanel);
/*  559 */     this.manager.addGameListener(GameType.SHADERPACK, (GameEntityListener)this.shaderpackLeftPanel);
/*  560 */     this.manager.addGameListener(GameType.SHADERPACK, (GameEntityListener)this.shaderpacksPanel);
/*      */     
/*  562 */     this.manager.addGameListener(GameType.MODPACK, (GameEntityListener)this.localmodpacks);
/*  563 */     this.manager.addGameListener(GameType.MODPACK, (GameEntityListener)this.modpacksPanel);
/*  564 */     this.mapGameTypeAndRadioButton.forEach((e, v) -> v.addActionListener(()));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  575 */     this.modpacks.setSelected(true);
/*  576 */     updaterButton2.addMouseListener(new MouseAdapter() {
/*      */           public void mousePressed(MouseEvent e) {
/*  578 */             CompletableFuture.runAsync(() -> DesktopUtil.uncheckCall(()), ModpackScene.this
/*      */ 
/*      */ 
/*      */                 
/*  582 */                 .modpackExecutorService).handle((r, t) -> {
/*      */                   if (Objects.nonNull(t)) {
/*      */                     U.log(new Object[] { t });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/*      */                     Alert.showLocMessage("modpack.internet.update");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/*      */                     return null;
/*      */                   } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/*      */                   SwingUtilities.invokeLater(());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/*      */                   return null;
/*      */                 });
/*      */           }
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  618 */     this.minecraftVersionTypes.addItemListener(item -> {
/*      */           NameIdDTO nid = (NameIdDTO)item.getItem(); DefaultComboBoxModel<GameVersionDTO> m = new DefaultComboBoxModel<>();
/*      */           m.addElement(this.anyGameVersion);
/*      */           this.gameVersions.setModel(m);
/*      */           GameVersionDTO gtmp = this.selectedGameVersion;
/*      */           if (item.getStateChange() == 1 && this.manager.getGameVersions().containsKey(nid)) {
/*      */             List<GameVersionDTO> list = (List<GameVersionDTO>)this.manager.getGameVersions().get(nid);
/*      */             list.forEach(m::addElement);
/*      */             NameIdDTO g = (NameIdDTO)item.getItem();
/*      */             if (this.anyVersionType.equals(g)) {
/*      */               this.selectedVersionType = null;
/*      */             } else {
/*      */               this.selectedVersionType = g;
/*      */             } 
/*      */             if (list.contains(gtmp)) {
/*      */               this.gameVersions.setSelectedItem(gtmp);
/*      */             } else {
/*      */               this.selectedGameVersion = null;
/*      */             } 
/*      */             fillGameEntitiesPanel(true);
/*      */           } 
/*      */         });
/*  640 */     this.gameVersions.addItemListener(item -> {
/*      */           if (item.getStateChange() == 1) {
/*      */             GameVersionDTO g = (GameVersionDTO)item.getItem();
/*      */             if (this.anyGameVersion.equals(g)) {
/*      */               this.selectedGameVersion = null;
/*      */             } else {
/*      */               this.selectedGameVersion = g;
/*      */             } 
/*      */             fillGameEntitiesPanel(true);
/*      */           } 
/*      */         });
/*  651 */     this.favoriteCheckBox.addItemListener(item -> {
/*      */           if (item.getStateChange() == 1) {
/*      */             SwingUtilities.invokeLater(());
/*      */           }
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  670 */     back.addBackListener(new MouseAdapter() {
/*      */           public void mousePressed(MouseEvent e) {
/*  672 */             if (SwingUtilities.isLeftMouseButton(e)) {
/*  673 */               ModpackScene.this.searchPanel.setVisible(false);
/*      */             }
/*      */           }
/*      */         });
/*  677 */     this.localmodpacks.addItemListener(e -> {
/*      */           if (e.getStateChange() == 1) {
/*      */             SwingUtilities.invokeLater(());
/*      */           }
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  689 */     this.categoriesBox.addActionListener(e -> fillGameEntitiesPanel(true));
/*      */ 
/*      */     
/*  692 */     updaterButton1.addActionListener(a -> {
/*      */           resetSearch();
/*      */           this.minecraftVersionTypes.setSelectedItem(this.anyVersionType);
/*      */           this.gameVersions.setSelectedItem(this.anyGameVersion);
/*      */           this.favoriteCheckBox.setSelected(false);
/*      */           this.categoriesBox.setSelectedIndex(0);
/*      */         });
/*      */   }
/*      */   
/*      */   private void refillSearchKeys() {
/*  702 */     if (GameType.MODPACK.equals(this.current)) {
/*  703 */       resetSearch();
/*  704 */     } else if (isSelectedCompleteVersion()) {
/*      */       try {
/*  706 */         ModpackVersionDTO mv = (ModpackVersionDTO)getSelectedCompleteVersion().getModpack().getVersion();
/*  707 */         this.selectedGameVersion = this.manager.getGameVersion(mv);
/*  708 */         this.selectedVersionType = mv.findFirstMinecraftVersionType();
/*  709 */         ((CategoryComboBoxModel)this.categoriesBox.getModel()).cleanAllSelection();
/*  710 */         this.favoriteCheckBox.setSelected(false);
/*  711 */       } catch (IOException e1) {
/*  712 */         U.log(new Object[] { e1 });
/*      */       } 
/*      */     } else {
/*  715 */       resetSearch();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void resetSearch() {
/*  720 */     this.selectedGameVersion = null;
/*  721 */     this.selectedVersionType = null;
/*  722 */     this.favoriteCheckBox.setSelected(false);
/*  723 */     ((CategoryComboBoxModel)this.categoriesBox.getModel()).cleanAllSelection();
/*      */   }
/*      */   
/*      */   public static ScrollPane createScrollWrapper(JComponent panel) {
/*  727 */     ScrollPane pane = new ScrollPane(panel, ScrollPane.ScrollBarPolicy.AS_NEEDED, ScrollPane.ScrollBarPolicy.NEVER);
/*  728 */     pane.getVerticalScrollBar().setUnitIncrement(5);
/*  729 */     pane.getVerticalScrollBar().setPreferredSize(new Dimension(13, 0));
/*  730 */     pane.setPreferredSize(new Dimension(200, 100));
/*  731 */     pane.getVerticalScrollBar().setUI((ScrollBarUI)new ModpackScrollBarUI());
/*  732 */     pane.setBorder(BorderFactory.createEmptyBorder());
/*  733 */     return pane;
/*      */   }
/*      */   
/*      */   private void createTextField(LocalizableTextField search, final DeferredDocumentListener listener) {
/*  737 */     search.getDocument().addDocumentListener((DocumentListener)listener);
/*  738 */     search.addFocusListener(new FocusListener()
/*      */         {
/*      */           public void focusGained(FocusEvent e) {
/*  741 */             listener.start();
/*      */           }
/*      */ 
/*      */           
/*      */           public void focusLost(FocusEvent e) {
/*  746 */             listener.stop();
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   public void prepareView(List<CompleteVersion> modpackVersions) {
/*  752 */     SwingUtilities.invokeLater(() -> {
/*      */           prepareLocalModpack(modpackVersions);
/*      */           if (getRightPanelByType(this.current).getModel().getRowCount() == 0)
/*      */             fillGameEntitiesPanel(true); 
/*      */         });
/*      */   }
/*      */   
/*      */   public void fillGameEntitiesPanel(boolean clean) {
/*  760 */     GameType gt = this.current;
/*  761 */     GameEntityRightPanel gerp = getRightPanelByType(gt);
/*  762 */     if (clean) {
/*  763 */       gerp.setNextPageIndex(Integer.valueOf(0));
/*  764 */       gerp.setNextPage(true);
/*      */     } 
/*      */     
/*  767 */     if (gerp.isProcessingRequest() || !gerp.isNextPage())
/*      */       return; 
/*  769 */     U.log(new Object[] { "request" });
/*  770 */     gerp.setProcessingRequest(true);
/*  771 */     GameVersionDTO selectGTmp = this.selectedGameVersion;
/*  772 */     NameIdDTO selectVTTmp = this.selectedVersionType;
/*      */     
/*  774 */     CompletableFuture.runAsync(() -> (CommonPage)DesktopUtil.uncheckCall(()), this.modpackExecutorService)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  795 */       .exceptionally(e -> {
/*      */           U.log(new Object[] { e });
/*      */           SwingUtilities.invokeLater(());
/*      */           return null;
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetSelectedRightElement() {
/*  808 */     getRightPanelByType(this.current).filterRightPanel(this.current);
/*      */   }
/*      */ 
/*      */   
/*      */   private void changeEntityView(GameType gameType) {
/*  813 */     if (this.last == GameType.MODPACK && gameType != GameType.MODPACK) {
/*  814 */       ((CardLayout)this.layoutCenterPanel.getLayout()).show(this.layoutCenterPanel, GameType.NOT_MODPACK.toLowerCase());
/*  815 */     } else if (gameType == GameType.MODPACK) {
/*  816 */       ((CardLayout)this.layoutCenterPanel.getLayout()).show(this.layoutCenterPanel, GameType.MODPACK.toLowerCase());
/*  817 */       this.last = gameType;
/*      */       return;
/*      */     } 
/*  820 */     this.last = this.current;
/*  821 */     ((CardLayout)this.entitiesPanel.getLayout()).show((Container)this.entitiesPanel, gameType.toLowerCase());
/*      */   }
/*      */   
/*      */   private GameEntityRightPanel getRightPanelByType(GameType type) {
/*  825 */     switch (type) {
/*      */       case MOD:
/*  827 */         return this.modsPanel;
/*      */       case MAP:
/*  829 */         return this.mapsPanel;
/*      */       case RESOURCEPACK:
/*  831 */         return this.resourcePackPanel;
/*      */       case MODPACK:
/*  833 */         return this.modpacksPanel;
/*      */       case SHADERPACK:
/*  835 */         return this.shaderpacksPanel;
/*      */     } 
/*  837 */     throw new NullPointerException();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void fillLeftPanel(GameType gt) {
/*  843 */     if (gt == GameType.MODPACK)
/*      */       return; 
/*  845 */     if (this.localmodpacks.getSelectedIndex() < 1) {
/*  846 */       getLeftPanelByType(gt).cleanLeftPanel();
/*  847 */       getLeftPanelByType(gt).fireCounterChanged();
/*      */     } else {
/*  849 */       this.manager.checkFolderSubGameEntity((CompleteVersion)this.localmodpacks.getSelectedValue(), gt);
/*      */       
/*  851 */       ModpackVersionDTO version = (ModpackVersionDTO)((CompleteVersion)this.localmodpacks.getSelectedValue()).getModpack().getVersion();
/*  852 */       getLeftPanelByType(gt).addElements(version.getByType(gt), gt);
/*      */     } 
/*  854 */     getLeftPanelByType(gt).revalidate();
/*  855 */     getLeftPanelByType(gt).repaint();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private GameEntityLeftPanel getLeftPanelByType(GameType type) {
/*  861 */     switch (type) {
/*      */       case MOD:
/*  863 */         return this.modLeftPanel;
/*      */       case MAP:
/*  865 */         return this.mapLeftPanel;
/*      */       case RESOURCEPACK:
/*  867 */         return this.resourceLeftPanel;
/*      */       case SHADERPACK:
/*  869 */         return this.shaderpackLeftPanel;
/*      */     } 
/*  871 */     throw new NullPointerException();
/*      */   }
/*      */ 
/*      */   
/*      */   public void onResize() {
/*  876 */     super.onResize();
/*  877 */     this.panel.setLocation(getWidth() / 2 - this.panel.getWidth() / 2, (
/*  878 */         getHeight() - LoginForm.LOGIN_SIZE.height) / 2 - this.panel.getHeight() / 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void prepareLocalModpack(List<CompleteVersion> modpackVersions) {
/*  883 */     int index = TLauncher.getInstance().getConfiguration().getInteger("modpack.combobox.index");
/*      */     
/*  885 */     this.localmodpacks.removeAllItems();
/*  886 */     for (CompleteVersion v : modpackVersions) {
/*  887 */       this.localmodpacks.addItem(v);
/*      */     }
/*  889 */     if (this.localmodpacks.getModel().getSize() > index) {
/*  890 */       this.localmodpacks.setSelectedIndex(index);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setShown(boolean shown, boolean animate) {
/*  895 */     if (shown) {
/*  896 */       this.localmodpacks.addItemListener(this.modpackBoxListener);
/*      */     } else {
/*  898 */       this.localmodpacks.removeItemListener(this.modpackBoxListener);
/*      */     } 
/*  900 */     super.setShown(shown, animate);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setShown(boolean shown) {
/*  905 */     super.setShown(shown);
/*      */   }
/*      */   
/*      */   public CompleteVersion getSelectedCompleteVersion() {
/*  909 */     return (CompleteVersion)this.localmodpacks.getSelectedValue();
/*      */   }
/*      */   
/*      */   public boolean isSelectedCompleteVersion() {
/*  913 */     return (this.localmodpacks.getSelectedIndex() > 0);
/*      */   }
/*      */   
/*      */   public CompleteVersion getCompleteVersion(ModpackDTO dto, VersionDTO versionDTO) {
/*  917 */     return this.localmodpacks.findByModpack(dto, versionDTO);
/*      */   }
/*      */   
/*      */   private void prepareRightPanel(GameType gt) {
/*  921 */     getRightPanelByType(gt).filterRightPanel(gt);
/*  922 */     fillLeftPanel(gt);
/*  923 */     changeEntityView(gt);
/*      */   }
/*      */   
/*      */   public GameType getCurrent() {
/*  927 */     return this.current;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onMinecraftPrepare() {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onMinecraftAbort() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void onMinecraftLaunch() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void onMinecraftClose() {
/*  947 */     if (this.current != GameType.MODPACK) {
/*  948 */       fillLeftPanel(this.current);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onMinecraftError(Throwable e) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void onMinecraftKnownError(MinecraftException e) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void onMinecraftCrash(Crash crash) {}
/*      */ 
/*      */   
/*      */   public enum UserCategory1
/*      */   {
/*  967 */     POPULATE_MONTH, FAVORITE, NAME, POPULATE_ALL_TIME, DATE;
/*      */ 
/*      */     
/*      */     public String toString() {
/*  971 */       return name().toLowerCase(Locale.ROOT);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private class EmptyRightView
/*      */     extends ExtendedPanel
/*      */   {
/*      */     LocalizableHTMLLabel jLabel;
/*      */     
/*      */     private final GameType gameType;
/*      */     
/*      */     public EmptyRightView(GameType gameType) {
/*  984 */       this.gameType = gameType;
/*  985 */       setLayout(new BorderLayout());
/*  986 */       this.jLabel = new LocalizableHTMLLabel("")
/*      */         {
/*      */           public void updateLocale() {
/*  989 */             ModpackScene.EmptyRightView.this.updateText();
/*      */           }
/*      */         };
/*  992 */       this.jLabel.setHorizontalAlignment(0);
/*  993 */       this.jLabel.setAlignmentY(0.0F);
/*  994 */       SwingUtil.setFontSize((JComponent)this.jLabel, 18.0F, 1);
/*  995 */       add((Component)this.jLabel, "Center");
/*  996 */       updateText();
/*      */     }
/*      */     
/*      */     public void updateText() {
/* 1000 */       String value = Localizable.get("modpack.criteria.not.found." + this.gameType.toLowerCase());
/* 1001 */       String additional = "";
/* 1002 */       if (!this.gameType.equals(GameType.MODPACK) && ModpackScene.this.localmodpacks.getSelectedIndex() > 0)
/* 1003 */         additional = Localizable.get("modpack.search.without.modpack", new Object[] {
/* 1004 */               Localizable.get("modpack.local.box.default") }); 
/* 1005 */       String text = value + additional;
/* 1006 */       this.jLabel.setText(String.format("<div WIDTH=%d><center>%s</center></div>", new Object[] { Integer.valueOf(600), text }));
/*      */     }
/*      */   }
/*      */   
/*      */   private class PreparingRightView extends EmptyRightView {
/*      */     public PreparingRightView() {
/* 1012 */       super(null);
/*      */     }
/*      */ 
/*      */     
/*      */     public void updateText() {
/* 1017 */       this.jLabel.setText(String.format("<div WIDTH=%d><center>%s</center></div>", new Object[] { Integer.valueOf(600), 
/* 1018 */               Localizable.get("autoupdater.preparing") }));
/*      */     }
/*      */   }
/*      */   
/*      */   class GameCenterPanel
/*      */     extends ExtendedPanel {
/* 1024 */     Color color241 = new Color(241, 241, 241);
/* 1025 */     private final String COLLAPSE = "0";
/* 1026 */     private final String NOT_COLLAPSE = "1";
/*      */ 
/*      */ 
/*      */     
/*      */     public GameCenterPanel(ModpackScene.GameEntityLeftPanel leftEntityPanel, GameEntityRightPanel rightPanel, GameType gameType) {
/* 1031 */       setLayout(new BorderLayout(0, 0));
/* 1032 */       ShadowButton collapse = new ShadowButton(this.color241, ColorUtil.COLOR_215, "modpack.element.collapse", "left-array-collapse.png");
/*      */ 
/*      */       
/* 1035 */       collapse.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
/* 1036 */       collapse.setPreferredSize(new Dimension(ModpackScene.LEFT_WIDTH, 39));
/* 1037 */       collapse.setIconTextGap(15);
/* 1038 */       collapse.setForeground(Color.BLACK);
/*      */       
/* 1040 */       ImageUdaterButton imageUdaterButton = new ImageUdaterButton(new Color(200, 200, 200), new Color(200, 200, 200), "un-collapse-arrow.png", "un-collapse-arrow-up.png");
/*      */       
/* 1042 */       JPanel leftPanel = new JPanel(new BorderLayout());
/* 1043 */       leftPanel.setBackground(Color.WHITE);
/*      */       
/* 1045 */       JPanel search = new JPanel(new BorderLayout());
/* 1046 */       search.setBackground(Color.WHITE);
/*      */       
/* 1048 */       LocalizableTextField field = new LocalizableTextField("modpack.search.textfield." + gameType.toLowerCase())
/*      */         {
/*      */           protected void onFocusLost() {
/* 1051 */             super.onFocusLost();
/* 1052 */             setForeground(Color.BLACK);
/*      */           }
/*      */ 
/*      */           
/*      */           protected void onFocusGained() {
/* 1057 */             super.onFocusGained();
/* 1058 */             setForeground(Color.BLACK);
/*      */           }
/*      */ 
/*      */           
/*      */           public void updateLocale() {
/* 1063 */             super.updateLocale();
/* 1064 */             setForeground(Color.BLACK);
/*      */           }
/*      */         };
/*      */       
/* 1068 */       JLabel searchLabel = new JLabel(ImageCache.getNativeIcon("search-left-panel.png"));
/* 1069 */       searchLabel.setPreferredSize(new Dimension(38, 38));
/* 1070 */       search.setPreferredSize(new Dimension(0, 38));
/* 1071 */       field.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 7));
/* 1072 */       search.add((Component)field, "Center");
/* 1073 */       search.add(searchLabel, "West");
/*      */       
/* 1075 */       leftPanel.add(search, "North");
/*      */ 
/*      */       
/* 1078 */       CardLayout emptyLayout = new CardLayout();
/* 1079 */       ExtendedPanel extendedPanel1 = new ExtendedPanel(emptyLayout);
/* 1080 */       extendedPanel1.add((Component)new EmptyGameEntityView(gameType), ModpackScene.EMPTY);
/* 1081 */       ScrollPane scrollPane1 = ModpackScene.createScrollWrapper((JComponent)leftEntityPanel);
/* 1082 */       scrollPane1.setPreferredSize(new Dimension(ModpackScene.LEFT_WIDTH, (getPreferredSize()).height));
/* 1083 */       extendedPanel1.add((Component)scrollPane1, ModpackScene.NOT_EMPTY);
/* 1084 */       leftEntityPanel.addCounterListener(currentCounter -> {
/*      */             if (currentCounter == 0) {
/*      */               emptyLayout.show(emptyPanel, ModpackScene.EMPTY);
/*      */             } else {
/*      */               emptyLayout.show(emptyPanel, ModpackScene.NOT_EMPTY);
/*      */             } 
/*      */           });
/* 1091 */       leftEntityPanel.fireCounterChanged();
/*      */       
/* 1093 */       leftPanel.add((Component)extendedPanel1, "Center");
/* 1094 */       leftPanel.add((Component)collapse, "South");
/*      */       
/* 1096 */       final CardLayout card = new CardLayout();
/* 1097 */       final JPanel leftCenterPanel = new JPanel(card);
/* 1098 */       leftCenterPanel.setPreferredSize(new Dimension(ModpackScene.LEFT_WIDTH, 0));
/* 1099 */       leftCenterPanel.add(leftPanel, "1");
/* 1100 */       leftCenterPanel.add((Component)imageUdaterButton, "0");
/* 1101 */       add(leftCenterPanel, "West");
/*      */ 
/*      */ 
/*      */       
/* 1105 */       SwingUtil.changeFontFamily((JComponent)field, FontTL.ROBOTO_REGULAR, 14, new Color(16, 16, 16));
/* 1106 */       SwingUtil.changeFontFamily((JComponent)collapse, FontTL.ROBOTO_BOLD, 14);
/*      */       
/* 1108 */       CardLayout emptyRightLayout = new CardLayout();
/* 1109 */       ExtendedPanel extendedPanel2 = new ExtendedPanel(emptyRightLayout);
/* 1110 */       ModpackScene.EmptyRightView emptyRightView = new ModpackScene.EmptyRightView(gameType);
/* 1111 */       extendedPanel2.add((Component)new ModpackScene.PreparingRightView(), ModpackScene.PREPARING);
/* 1112 */       extendedPanel2.add((Component)emptyRightView, ModpackScene.EMPTY);
/* 1113 */       extendedPanel2.add((Component)rightPanel, ModpackScene.NOT_EMPTY);
/* 1114 */       emptyRightLayout.show((Container)extendedPanel2, ModpackScene.PREPARING);
/* 1115 */       ScrollPane sp = ModpackScene.createScrollWrapper((JComponent)extendedPanel2);
/* 1116 */       add((Component)sp, "Center");
/* 1117 */       sp.getVerticalScrollBar()
/* 1118 */         .addAdjustmentListener((AdjustmentListener)new RightPanelAdjustmentListener(rightPanel, ModpackScene.this));
/*      */       
/* 1120 */       card.show(leftCenterPanel, "1");
/* 1121 */       DeferredDocumentListener listener = new DeferredDocumentListener(500, e -> { if (ModpackScene.this.localmodpacks.getSelectedIndex() > 0) { List<? extends GameEntityDTO> list = ((ModpackVersionDTO)((CompleteVersion)ModpackScene.this.localmodpacks.getSelectedValue()).getModpack().getVersion()).getByType(gameType); String text = field.getValue(); if (text == null || text.isEmpty()) { leftEntityPanel.addElements((List)list, gameType); } else { List<GameEntityDTO> res = (new BaseModpackFilter(new Filter[] { (Filter)new NameFilter(text) })).findAll(list); leftEntityPanel.addElements((List)res, gameType); }  leftEntityPanel.revalidate(); leftEntityPanel.repaint(); }  }false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1136 */       collapse.addMouseListener(new MouseAdapter()
/*      */           {
/*      */             public void mousePressed(MouseEvent e)
/*      */             {
/* 1140 */               if (SwingUtilities.isLeftMouseButton(e)) {
/* 1141 */                 card.show(leftCenterPanel, "0");
/* 1142 */                 leftCenterPanel.setPreferredSize(new Dimension(30, 0));
/* 1143 */                 ModpackScene.GameCenterPanel.this.revalidate();
/* 1144 */                 ModpackScene.GameCenterPanel.this.repaint();
/*      */               } 
/*      */             }
/*      */           });
/* 1148 */       ModpackScene.this.createTextField(field, listener);
/*      */       
/* 1150 */       imageUdaterButton.addActionListener(e -> {
/*      */             card.show(leftCenterPanel, "1");
/*      */             leftCenterPanel.setPreferredSize(new Dimension(ModpackScene.LEFT_WIDTH, 0));
/*      */             revalidate();
/*      */             repaint();
/*      */           });
/* 1156 */       ModpackScene.this.localmodpacks.addItemListener(e -> {
/*      */             if (e.getStateChange() == 1)
/*      */               emptyRightView.updateText(); 
/*      */           });
/*      */     }
/*      */     
/*      */     private class EmptyGameEntityView
/*      */       extends ExtendedPanel
/*      */       implements LocalizableComponent {
/*      */       private final JLabel notHaveSumEntities;
/*      */       private final JLabel intallThem;
/*      */       private final GameType gameType;
/*      */       
/*      */       public EmptyGameEntityView(GameType gameType) {
/* 1170 */         this.gameType = gameType;
/* 1171 */         setLayout(new FlowLayout(0, 0, 0));
/* 1172 */         JLabel image = new JLabel((Icon)ImageCache.getIcon("empty-left-panel.png", 225, 165));
/* 1173 */         image.setHorizontalAlignment(0);
/* 1174 */         this.notHaveSumEntities = (JLabel)new LocalizableLabel("modpack.left.empty.mod.label." + gameType.toLowerCase());
/* 1175 */         this.intallThem = (JLabel)new LocalizableLabel("modpack.left.fast.install");
/* 1176 */         this.notHaveSumEntities.setHorizontalAlignment(0);
/* 1177 */         this.intallThem.setHorizontalAlignment(0);
/* 1178 */         this.notHaveSumEntities.setVerticalAlignment(3);
/* 1179 */         this.intallThem.setVerticalAlignment(1);
/* 1180 */         this.notHaveSumEntities.setPreferredSize(new Dimension(ModpackScene.LEFT_WIDTH, 40));
/* 1181 */         this.intallThem.setPreferredSize(new Dimension(ModpackScene.LEFT_WIDTH, 40));
/* 1182 */         SwingUtil.changeFontFamily(this.notHaveSumEntities, FontTL.ROBOTO_BOLD, 14);
/* 1183 */         SwingUtil.changeFontFamily(this.intallThem, FontTL.ROBOTO_BOLD, 14);
/* 1184 */         setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));
/* 1185 */         add(image);
/* 1186 */         add(this.notHaveSumEntities);
/* 1187 */         add(this.intallThem);
/*      */       }
/*      */ 
/*      */       
/*      */       public void updateLocale() {
/* 1192 */         this.notHaveSumEntities.setText("modpack.left.empty.mod.label." + this.gameType.toLowerCase());
/* 1193 */         this.intallThem.setText("modpack.left.fast.install");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   class GameLeftElement
/*      */     extends ExtendedPanel implements UpdateGameListener {
/* 1200 */     private final JLabel name = new JLabel();
/*      */     private final StateModpackElementButton clickButton;
/* 1202 */     private final Dimension DEFAULT_SIZE = new Dimension(ModpackScene.LEFT_WIDTH, 39);
/* 1203 */     private final Color MOUSE_UNDER_BACKGROUND = new Color(242, 242, 242);
/* 1204 */     private final Color BACKGROUND_COLOR = new Color(235, 235, 235);
/*      */     private final SubModpackDTO entity;
/*      */     private final GameType type;
/*      */     private final JLabel leftLabel;
/*      */     
/*      */     public GameLeftElement(SubModpackDTO e, final GameType type) {
/* 1210 */       this.type = type;
/* 1211 */       setPreferredSize(this.DEFAULT_SIZE);
/* 1212 */       setMaximumSize(this.DEFAULT_SIZE);
/* 1213 */       setMinimumSize(this.DEFAULT_SIZE);
/* 1214 */       setLayout(new BorderLayout());
/*      */       
/* 1216 */       this.entity = e;
/*      */       
/* 1218 */       SpringLayout layout = new SpringLayout();
/* 1219 */       ExtendedPanel extendedPanel = new ExtendedPanel(layout);
/* 1220 */       extendedPanel.setPreferredSize(new Dimension(67, 39));
/*      */       
/* 1222 */       this.clickButton = new StateModpackElementButton(e, type);
/* 1223 */       if (this.entity.isUserInstall()) {
/* 1224 */         this.leftLabel = new JLabel((Icon)ImageCache.getIcon("modpack-element-left-hanlde.png"));
/*      */       } else {
/* 1226 */         this.leftLabel = new JLabel((Icon)ImageCache.getIcon("modpack-element-left.png"));
/*      */       } 
/* 1228 */       layout.putConstraint("West", (Component)this.clickButton, 0, "West", (Component)extendedPanel);
/* 1229 */       layout.putConstraint("East", (Component)this.clickButton, -16, "East", (Component)extendedPanel);
/* 1230 */       layout.putConstraint("North", (Component)this.clickButton, 10, "North", (Component)extendedPanel);
/* 1231 */       layout.putConstraint("South", (Component)this.clickButton, -9, "South", (Component)extendedPanel);
/* 1232 */       extendedPanel.add((Component)this.clickButton);
/*      */       
/* 1234 */       this.leftLabel.setPreferredSize(new Dimension(39, 29));
/* 1235 */       add(this.leftLabel, "West");
/* 1236 */       final Border nameBorder = BorderFactory.createEmptyBorder(10, 1, 10, 1);
/* 1237 */       this.name.setBorder(nameBorder);
/* 1238 */       add(this.name, "Center");
/* 1239 */       if (GameType.MAP != type) {
/* 1240 */         add((Component)extendedPanel, "East");
/*      */       }
/* 1242 */       initGameEntity();
/* 1243 */       setOpaque(true);
/*      */       
/* 1245 */       SwingUtil.changeFontFamily(this.name, FontTL.ROBOTO_BOLD, 14, ColorUtil.COLOR_16);
/*      */       
/* 1247 */       MouseAdapter click = new MouseAdapter()
/*      */         {
/*      */           public void mousePressed(MouseEvent e) {
/* 1250 */             if (SwingUtilities.isRightMouseButton(e)) {
/* 1251 */               ModpackPopup modpackPopup = new ModpackPopup();
/* 1252 */               ModpackPopup.ModpackMenuItem modpackMenuItem = new ModpackPopup.ModpackMenuItem("modpack.popup.delete");
/* 1253 */               modpackPopup.add((JMenuItem)modpackMenuItem);
/* 1254 */               modpackMenuItem.addActionListener(e1 -> {
/*      */                     popupMenu.setVisible(false);
/*      */                     
/*      */                     List<GameEntityDTO> list = ModpackScene.this.manager.findDependenciesFromGameEntityDTO((GameEntityDTO)ModpackScene.GameLeftElement.this.entity);
/*      */                     StringBuilder b = ModpackUtil.buildMessage(list);
/*      */                     if (list.isEmpty()) {
/*      */                       ModpackScene.this.manager.removeEntity((GameEntityDTO)ModpackScene.GameLeftElement.this.entity, ModpackScene.GameLeftElement.this.entity.getVersion(), type, false);
/*      */                     } else if (Alert.showQuestion("", Localizable.get("modpack.left.element.remove.question", new Object[] { ModpackScene.GameLeftElement.access$1200(this.this$1).getName(), b.toString() }))) {
/*      */                       ModpackScene.this.manager.removeEntity((GameEntityDTO)ModpackScene.GameLeftElement.this.entity, ModpackScene.GameLeftElement.this.entity.getVersion(), type, false);
/*      */                     } 
/*      */                   });
/* 1265 */               modpackPopup.show(e.getComponent(), e.getX(), e.getY());
/*      */             } else {
/* 1267 */               if (ModpackScene.GameLeftElement.this.entity.isUserInstall())
/*      */                 return; 
/* 1269 */               ModpackScene.this.manager.showFullGameEntity((GameEntityDTO)ModpackScene.GameLeftElement.this.entity, type);
/*      */             } 
/*      */           }
/*      */         };
/*      */ 
/*      */       
/* 1275 */       MouseAdapter backgroundListener = new MouseAdapter()
/*      */         {
/*      */           public void mouseEntered(MouseEvent e) {
/* 1278 */             ModpackScene.GameLeftElement.this.setBackground(ModpackScene.GameLeftElement.this.MOUSE_UNDER_BACKGROUND);
/*      */           }
/*      */ 
/*      */           
/*      */           public void mouseExited(MouseEvent e) {
/* 1283 */             ModpackScene.GameLeftElement.this.setBackground(ModpackScene.GameLeftElement.this.BACKGROUND_COLOR);
/*      */           }
/*      */         };
/* 1286 */       final AtomicInteger padding = new AtomicInteger();
/* 1287 */       final Timer timer = new Timer(30, e12 -> {
/*      */             int width = this.name.getWidth();
/*      */             if (padding.get() + width < 0) {
/*      */               padding.set(width - 20);
/*      */             }
/*      */             this.name.setBorder(new EmptyBorder(0, padding.getAndDecrement(), 0, 0));
/*      */           });
/* 1294 */       this.name.addMouseListener(new MouseAdapter()
/*      */           {
/*      */             public void mouseEntered(MouseEvent e) {
/* 1297 */               padding.set(0);
/* 1298 */               if ((ModpackScene.GameLeftElement.this.name.getPreferredSize()).width > 120) {
/* 1299 */                 timer.restart();
/*      */               }
/*      */             }
/*      */             
/*      */             public void mouseExited(MouseEvent e) {
/* 1304 */               timer.stop();
/* 1305 */               ModpackScene.GameLeftElement.this.name.setBorder(nameBorder);
/*      */             }
/*      */           });
/*      */       
/* 1309 */       addMouseListenerOriginally(click);
/* 1310 */       this.name.addMouseListener(click);
/* 1311 */       this.leftLabel.addMouseListener(click);
/*      */ 
/*      */       
/* 1314 */       addMouseListener(backgroundListener);
/* 1315 */       addMouseListenerOriginally(backgroundListener);
/*      */     }
/*      */ 
/*      */     
/*      */     public void initGameEntity() {
/* 1320 */       this.name.setText(this.entity.getName());
/* 1321 */       if (this.type != GameType.MAP) {
/* 1322 */         this.clickButton.setState(this.entity.getStateGameElement());
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void processingActivation() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void processingInstall() {}
/*      */ 
/*      */     
/*      */     public SubModpackDTO getEntity() {
/* 1336 */       return this.entity;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void paintComponent(Graphics g) {
/* 1341 */       super.paintComponent(g);
/* 1342 */       Rectangle rec = getVisibleRect();
/* 1343 */       SwingUtil.paintShadowLine(rec, g, getBackground().getRed(), 12);
/*      */     }
/*      */   }
/*      */   
/*      */   public class SearchPanel
/*      */     extends JPanel {
/*      */     private final LocalizableTextField field;
/*      */     
/*      */     public SearchPanel() {
/* 1352 */       setOpaque(false);
/* 1353 */       setPreferredSize(new Dimension(194, 55));
/* 1354 */       SpringLayout spring = new SpringLayout();
/* 1355 */       setLayout(spring);
/* 1356 */       this.field = new LocalizableTextField("modpack.search.text") {
/*      */           public void setBackColor() {
/* 1358 */             if (!OS.is(new OS[] { OS.LINUX })) {
/* 1359 */               setForeground(Color.WHITE);
/*      */             }
/*      */           }
/*      */           
/*      */           protected void onFocusLost() {
/* 1364 */             super.onFocusLost();
/* 1365 */             setBackColor();
/*      */           }
/*      */ 
/*      */           
/*      */           protected void onFocusGained() {
/* 1370 */             super.onFocusGained();
/* 1371 */             setBackColor();
/*      */           }
/*      */ 
/*      */           
/*      */           public void updateLocale() {
/* 1376 */             super.updateLocale();
/* 1377 */             setBackColor();
/*      */           }
/*      */         };
/*      */       
/* 1381 */       this.field.setOpaque(false);
/* 1382 */       this.field.setBorder(BorderFactory.createEmptyBorder());
/*      */       
/* 1384 */       this.field.setCaretColor(Color.WHITE);
/* 1385 */       if (!OS.is(new OS[] { OS.LINUX })) {
/* 1386 */         SwingUtil.changeFontFamily((JComponent)this.field, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
/*      */       }
/* 1388 */       spring.putConstraint("West", (Component)this.field, 17, "West", this);
/* 1389 */       spring.putConstraint("East", (Component)this.field, -50, "East", this);
/* 1390 */       spring.putConstraint("North", (Component)this.field, 17, "North", this);
/* 1391 */       spring.putConstraint("South", (Component)this.field, -20, "South", this);
/* 1392 */       add((Component)this.field);
/*      */       
/* 1394 */       Color lineYellow = new Color(254, 254, 168);
/* 1395 */       JPanel linePanel = new JPanel();
/* 1396 */       linePanel.setBackground(lineYellow);
/* 1397 */       spring.putConstraint("West", linePanel, 17, "West", this);
/* 1398 */       spring.putConstraint("East", linePanel, -17, "East", this);
/* 1399 */       spring.putConstraint("North", linePanel, -12, "South", this);
/* 1400 */       spring.putConstraint("South", linePanel, -11, "South", this);
/* 1401 */       add(linePanel);
/*      */       
/* 1403 */       JLabel image = new JLabel(ImageCache.getNativeIcon("search-modpack-element.png"));
/* 1404 */       spring.putConstraint("West", image, -35, "East", this);
/* 1405 */       spring.putConstraint("East", image, -19, "East", this);
/* 1406 */       spring.putConstraint("North", image, 18, "North", this);
/* 1407 */       spring.putConstraint("South", image, -21, "South", this);
/* 1408 */       add(image);
/*      */       
/* 1410 */       DeferredDocumentListener listener = new DeferredDocumentListener(1000, (ActionListener)new ModpackSearchListener(ModpackScene.this, "", this.field), false);
/*      */       
/* 1412 */       ModpackScene.this.createTextField(this.field, listener);
/*      */     }
/*      */     
/*      */     public boolean isNotEmpty() {
/* 1416 */       return (this.field.getValue() != null && !this.field.getValue().isEmpty());
/*      */     }
/*      */     
/*      */     public String getSearchLine() {
/* 1420 */       return this.field.getValue();
/*      */     }
/*      */     
/*      */     public void reset() {
/* 1424 */       this.field.setValue(null);
/*      */     }
/*      */   }
/*      */   
/*      */   class GameEntityLeftPanel
/*      */     extends GameEntityPanel {
/*      */     private final List<ModpackScene.ElementCounterListener> observer;
/*      */     
/*      */     public GameEntityLeftPanel() {
/* 1433 */       setLayout(new BoxLayout((Container)this, 1));
/* 1434 */       this.observer = new ArrayList<>();
/*      */     }
/*      */     
/*      */     public void addElements(List<? extends SubModpackDTO> list, GameType type) {
/* 1438 */       cleanLeftPanel();
/* 1439 */       list.stream().forEach(e -> add((Component)new ModpackScene.GameLeftElement(e, type)));
/* 1440 */       fireCounterChanged();
/*      */     }
/*      */     
/*      */     protected void cleanLeftPanel() {
/* 1444 */       Iterator<Component> it = Arrays.<Component>asList(getComponents()).iterator();
/* 1445 */       while (it.hasNext()) {
/* 1446 */         Component c = it.next();
/* 1447 */         if (c instanceof ModpackScene.GameLeftElement) {
/* 1448 */           remove(c);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void activationStarted(GameEntityDTO e) {
/* 1455 */       Iterator<Component> it = Arrays.<Component>asList(getComponents()).iterator();
/* 1456 */       while (it.hasNext()) {
/* 1457 */         Component c = it.next();
/* 1458 */         if (c instanceof ModpackScene.GameLeftElement) {
/* 1459 */           ((ModpackScene.GameLeftElement)c).processingActivation();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void removeEntity(GameEntityDTO e) {
/* 1466 */       ModpackScene.GameLeftElement elem = find(e);
/* 1467 */       if (elem != null) {
/* 1468 */         remove((Component)elem);
/* 1469 */         revalidate();
/* 1470 */         repaint();
/*      */       } 
/* 1472 */       fireCounterChanged();
/*      */     }
/*      */ 
/*      */     
/*      */     public void activation(GameEntityDTO e) {
/* 1477 */       Iterator<Component> it = Arrays.<Component>asList(getComponents()).iterator();
/* 1478 */       while (it.hasNext()) {
/* 1479 */         Component c = it.next();
/* 1480 */         if (c instanceof ModpackScene.GameLeftElement) {
/* 1481 */           ((ModpackScene.GameLeftElement)c).initGameEntity();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void activationError(GameEntityDTO e, Throwable t) {
/* 1488 */       Iterator<Component> it = Arrays.<Component>asList(getComponents()).iterator();
/* 1489 */       while (it.hasNext()) {
/* 1490 */         Component c = it.next();
/* 1491 */         if (c instanceof ModpackScene.GameLeftElement) {
/* 1492 */           ((ModpackScene.GameLeftElement)c).initGameEntity();
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     private ModpackScene.GameLeftElement find(GameEntityDTO e) {
/* 1498 */       Iterator<Component> it = Arrays.<Component>asList(getComponents()).iterator();
/* 1499 */       while (it.hasNext()) {
/* 1500 */         Component c = it.next();
/* 1501 */         if (c instanceof ModpackScene.GameLeftElement) {
/* 1502 */           ModpackScene.GameLeftElement el = (ModpackScene.GameLeftElement)c;
/* 1503 */           if (el.getEntity().getId().equals(e.getId()))
/* 1504 */             return el; 
/*      */         } 
/*      */       } 
/* 1507 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public void installEntity(GameEntityDTO e, GameType type) {
/* 1512 */       add((Component)new ModpackScene.GameLeftElement((SubModpackDTO)e, type));
/* 1513 */       revalidate();
/* 1514 */       repaint();
/* 1515 */       fireCounterChanged();
/*      */     }
/*      */     
/*      */     public void fireCounterChanged() {
/* 1519 */       for (ModpackScene.ElementCounterListener el : this.observer) {
/* 1520 */         el.changeCounter(getComponentCount());
/*      */       }
/*      */     }
/*      */     
/*      */     public void addCounterListener(ModpackScene.ElementCounterListener l) {
/* 1525 */       this.observer.add(l);
/*      */     }
/*      */   }
/*      */   
/*      */   private void fillCategories() {
/* 1530 */     this.categoriesBox.setModel((ComboBoxModel<CategoryDTO>)new CategoryComboBoxModel(this.manager.getLocalCategories(this.current)));
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateFavoriteValue() {
/* 1535 */     SwingUtilities.invokeLater(() -> {
/*      */           getRightPanelByType(this.current).filterRightPanel(this.current);
/*      */           repaint();
/*      */         });
/*      */   }
/*      */   
/*      */   static interface ElementCounterListener {
/*      */     void changeCounter(int param1Int);
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\scenes\ModpackScene.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */