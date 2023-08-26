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
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FlowLayout;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.GridBagLayout;
/*      */ import java.awt.GridLayout;
/*      */ import java.awt.Image;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.AdjustmentListener;
/*      */ import java.awt.event.ComponentAdapter;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Date;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.ConcurrentLinkedDeque;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import java.util.stream.Collectors;
/*      */ import javax.swing.AbstractButton;
/*      */ import javax.swing.BorderFactory;
/*      */ import javax.swing.ButtonGroup;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JFrame;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JLayeredPane;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JTable;
/*      */ import javax.swing.JTextArea;
/*      */ import javax.swing.SpringLayout;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.plaf.ScrollBarUI;
/*      */ import javax.swing.table.AbstractTableModel;
/*      */ import javax.swing.table.DefaultTableCellRenderer;
/*      */ import javax.swing.table.JTableHeader;
/*      */ import javax.swing.table.TableCellEditor;
/*      */ import javax.swing.table.TableCellRenderer;
/*      */ import net.minecraft.launcher.versions.CompleteVersion;
/*      */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*      */ import org.tlauncher.modpack.domain.client.GameVersionDTO;
/*      */ import org.tlauncher.modpack.domain.client.share.CategoryDTO;
/*      */ import org.tlauncher.modpack.domain.client.share.GameType;
/*      */ import org.tlauncher.modpack.domain.client.share.NameIdDTO;
/*      */ import org.tlauncher.modpack.domain.client.site.CommonPage;
/*      */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*      */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*      */ import org.tlauncher.tlauncher.modpack.ModpackUtil;
/*      */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*      */ import org.tlauncher.tlauncher.ui.MainPane;
/*      */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*      */ import org.tlauncher.tlauncher.ui.button.StatusStarButton;
/*      */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*      */ import org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener;
/*      */ import org.tlauncher.tlauncher.ui.listener.mods.GameEntityPanel;
/*      */ import org.tlauncher.tlauncher.ui.listener.mods.UpdateFavoriteValueListener;
/*      */ import org.tlauncher.tlauncher.ui.listener.mods.VersionsAdjustmentListener;
/*      */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*      */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*      */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*      */ import org.tlauncher.tlauncher.ui.loc.UpdaterFullButton;
/*      */ import org.tlauncher.tlauncher.ui.loc.modpack.GameRightButton;
/*      */ import org.tlauncher.tlauncher.ui.loc.modpack.ModpackActButton;
/*      */ import org.tlauncher.tlauncher.ui.loc.modpack.ModpackTableVersionButton;
/*      */ import org.tlauncher.tlauncher.ui.loc.modpack.UpInstallButton;
/*      */ import org.tlauncher.tlauncher.ui.login.LoginForm;
/*      */ import org.tlauncher.tlauncher.ui.menu.ModpackCategoryPopupMenu;
/*      */ import org.tlauncher.tlauncher.ui.modpack.AddedButtonOldVersion;
/*      */ import org.tlauncher.tlauncher.ui.modpack.AddedModpackStuffFrame;
/*      */ import org.tlauncher.tlauncher.ui.modpack.DiscussionPanel;
/*      */ import org.tlauncher.tlauncher.ui.modpack.GroupPanel;
/*      */ import org.tlauncher.tlauncher.ui.modpack.PicturePanel;
/*      */ import org.tlauncher.tlauncher.ui.modpack.filter.BaseModpackFilter;
/*      */ import org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm;
/*      */ import org.tlauncher.tlauncher.ui.server.BackPanel;
/*      */ import org.tlauncher.tlauncher.ui.swing.GameRadioButton;
/*      */ import org.tlauncher.tlauncher.ui.swing.HtmlTextPane;
/*      */ import org.tlauncher.tlauncher.ui.swing.ScrollPane;
/*      */ import org.tlauncher.tlauncher.ui.swing.TextWrapperLabel;
/*      */ import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
/*      */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*      */ import org.tlauncher.tlauncher.ui.swing.renderer.JTableButtonRenderer;
/*      */ import org.tlauncher.tlauncher.ui.ui.ModpackScrollBarUI;
/*      */ import org.tlauncher.util.ColorUtil;
/*      */ import org.tlauncher.util.OS;
/*      */ import org.tlauncher.util.SwingUtil;
/*      */ import org.tlauncher.util.U;
/*      */ import org.tlauncher.util.swing.FontTL;
/*      */ 
/*      */ public class CompleteSubEntityScene
/*      */   extends PseudoScene
/*      */   implements UpdateFavoriteValueListener {
/*  115 */   public static final AtomicBoolean b = new AtomicBoolean(false);
/*  116 */   protected final ExtendedPanel panel = new ExtendedPanel(new GridLayout(1, 1, 0, 0));
/*      */   
/*      */   private JLayeredPane layeredPane;
/*  119 */   protected final String REVIEW_S = "REVIEW"; protected final String VERSION_S = "VERSIONS"; protected final String PICTURES_S = "PICTURES"; protected final String DISCUSSION_S = "DISCUSSION";
/*      */   
/*      */   protected final ModpackManager manager;
/*  122 */   private static Executor singleDownloadExecutor = (Executor)TLauncher.getInjector()
/*  123 */     .getInstance(Key.get(Executor.class, (Annotation)Names.named("singleDownloadExecutor")));
/*      */   protected FullGameEntity fullGameEntity;
/*      */   public static final int BUTTON_PANEL_SUB_VIEW = 130;
/*      */   private static final int activeColumn = 5;
/*      */   private CommentCreationForm commentCreationForm;
/*      */   
/*      */   public CompleteSubEntityScene(MainPane main) {
/*  130 */     super(main);
/*  131 */     this.manager = (ModpackManager)TLauncher.getInjector().getInstance(ModpackManager.class);
/*  132 */     this.layeredPane = new JLayeredPane()
/*      */       {
/*      */         public boolean isOptimizedDrawingEnabled() {
/*  135 */           return false;
/*      */         }
/*      */       };
/*      */     
/*  139 */     this.panel.setOpaque(true);
/*  140 */     this.panel.setForeground(Color.WHITE);
/*  141 */     this.panel.setSize(ModpackScene.SIZE);
/*  142 */     this.layeredPane.setSize(ModpackScene.SIZE);
/*  143 */     this.commentCreationForm = (CommentCreationForm)TLauncher.getInjector().getInstance(CommentCreationForm.class);
/*  144 */     this.layeredPane.add((Component)this.commentCreationForm);
/*  145 */     this.layeredPane.add((Component)this.panel);
/*  146 */     add(this.layeredPane);
/*      */   }
/*      */ 
/*      */   
/*      */   public void onResize() {
/*  151 */     super.onResize();
/*  152 */     this.layeredPane.setLocation(getWidth() / 2 - this.layeredPane.getWidth() / 2, (
/*  153 */         getHeight() - LoginForm.LOGIN_SIZE.height) / 2 - this.layeredPane.getHeight() / 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void showFullGameEntity(GameEntityDTO gameEntityDTO, final GameType type) {
/*  158 */     clean(type);
/*  159 */     U.debug(new Object[] { "open " + gameEntityDTO.getName() + " " + U.memoryStatus() });
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
/*  170 */     BackPanel backPanel = new BackPanel("", new MouseAdapter() { public void mousePressed(MouseEvent e) { if (SwingUtilities.isLeftMouseButton(e)) { CompleteSubEntityScene.this.getMainPane().setScene((CompleteSubEntityScene.this.getMainPane()).modpackScene); CompleteSubEntityScene.this.clean(type); SwingUtilities.invokeLater(() -> (CompleteSubEntityScene.this.getMainPane()).modpackScene.resetSelectedRightElement()); }  } }, ImageCache.getIcon("back-arrow.png"));
/*      */     
/*  172 */     this.fullGameEntity = new FullGameEntity(gameEntityDTO, backPanel, type);
/*  173 */     this.panel.removeAll();
/*      */     
/*  175 */     this.panel.add((Component)this.fullGameEntity);
/*  176 */     if (getMainPane().getScene() != this) {
/*  177 */       getMainPane().setScene(this);
/*      */     } else {
/*  179 */       this.panel.revalidate();
/*  180 */       this.panel.repaint();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void clean(GameType type) {
/*  185 */     if (this.fullGameEntity != null) {
/*  186 */       this.panel.remove((Component)this.fullGameEntity);
/*  187 */       this.fullGameEntity.clearContent();
/*  188 */       this.manager.removeGameListener(type, (GameEntityListener)this.fullGameEntity);
/*  189 */       this.fullGameEntity = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void showModpackElement(GameEntityDTO completeGameEntity, final GameType type) {
/*  194 */     clean(type);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  203 */     BackPanel backPanel = new BackPanel("", new MouseAdapter() { public void mousePressed(MouseEvent e) { if (SwingUtilities.isLeftMouseButton(e)) { CompleteSubEntityScene.this.getMainPane().setScene((CompleteSubEntityScene.this.getMainPane()).modpackEnitityScene); CompleteSubEntityScene.this.clean(type); }  } }, ImageCache.getIcon("back-arrow.png"));
/*  204 */     this.fullGameEntity = new FullGameEntity(completeGameEntity, backPanel, type);
/*  205 */     this.panel.add((Component)this.fullGameEntity);
/*  206 */     revalidate();
/*  207 */     repaint();
/*  208 */     getMainPane().setScene(this);
/*      */   }
/*      */   
/*      */   public static class ImagePanel extends JLayeredPane {
/*      */     private GameRightButton gameRightButton;
/*  213 */     private static final ConcurrentLinkedDeque<List<String>> images = new ConcurrentLinkedDeque<>();
/*      */     private final GameEntityDTO entity;
/*      */     private final JLabel label;
/*      */     
/*      */     public ImagePanel(GameEntityDTO entity) {
/*  218 */       this.entity = entity;
/*  219 */       setOpaque(true);
/*  220 */       this.label = new JLabel()
/*      */         {
/*      */           protected void paintComponent(Graphics g) {
/*  223 */             super.paintComponent(g);
/*  224 */             CompleteSubEntityScene.ImagePanel.this.initPicture(g);
/*      */           }
/*      */         };
/*      */       
/*  228 */       this.label.setOpaque(true);
/*  229 */       add(this.label, 1);
/*  230 */       this.label.setBounds(0, 0, 111, 111);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addMoapckActButton(GameRightButton actButton) {
/*  235 */       this.gameRightButton = actButton;
/*  236 */       add((Component)actButton, 0);
/*  237 */       actButton.setBounds(10, 80, 90, 23);
/*      */     }
/*      */     
/*      */     public void initPicture(Graphics g) {
/*  241 */       if (this.entity.getPicture() != null) {
/*      */         
/*      */         try {
/*  244 */           List<String> picture = ModpackUtil.getPictureURL(this.entity.getPicture(), "logo");
/*  245 */           Optional<String> op = picture.stream().filter(ImageCache::imageInCache).findFirst();
/*  246 */           if (!op.isPresent()) {
/*  247 */             images.push(picture);
/*  248 */             if (images.size() == 1) {
/*  249 */               loadImages();
/*      */             }
/*      */           } else {
/*  252 */             Image image = ImageCache.loadImage(picture, false);
/*  253 */             if (image != null)
/*  254 */               g.drawImage(image, 0, 0, null); 
/*      */           } 
/*  256 */         } catch (Exception e) {
/*  257 */           U.log(new Object[] { e });
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     private void loadImages() {
/*  264 */       CompletableFuture.runAsync(() -> {
/*      */             try {
/*      */               Set<List<String>> set = new HashSet<>();
/*      */               
/*      */               Thread.sleep(100L);
/*      */               
/*      */               int size = (images.size() > 3) ? 3 : images.size();
/*      */               
/*      */               for (int i = 0; i < size; i++) {
/*      */                 set.add(images.pop());
/*      */               }
/*      */               
/*      */               images.clear();
/*      */               
/*      */               List<CompletableFuture<Void>> c = new ArrayList<>();
/*      */               
/*      */               for (List<String> list : set) {
/*      */                 c.add(CompletableFuture.runAsync(()));
/*      */               }
/*      */               try {
/*      */                 CompletableFuture.allOf((CompletableFuture<?>[])c.<CompletableFuture>toArray(new CompletableFuture[0])).get();
/*  285 */               } catch (ExecutionException|InterruptedException e) {
/*      */                 U.log(new Object[] { "problem with pictures", e }, );
/*      */               } 
/*      */               if (this.gameRightButton != null) {
/*      */                 this.gameRightButton.updateRow();
/*      */               }
/*      */               this.label.repaint();
/*  292 */             } catch (Exception e) {
/*      */               U.log(new Object[] { e }, );
/*      */             } 
/*  295 */           }CompleteSubEntityScene.singleDownloadExecutor);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class DescriptionGamePanel
/*      */     extends JPanel {
/*      */     protected StatusStarButton statusStarButton;
/*      */     protected SpringLayout descriptionLayout;
/*      */     protected JLabel name;
/*      */     protected JTextArea description;
/*      */     protected JLabel downloadLabel;
/*      */     protected JLabel updateLabel;
/*      */     protected JLabel gameVersion;
/*      */     protected CompleteSubEntityScene.ImagePanel imagePanel;
/*  309 */     private final int gupPair = 30;
/*  310 */     private final int gup = 5;
/*  311 */     private final SimpleDateFormat format = new SimpleDateFormat("dd MMMM YYYY", Localizable.get().getSelected());
/*      */     
/*      */     public StatusStarButton getStatusStarButton() {
/*  314 */       return this.statusStarButton;
/*      */     }
/*      */ 
/*      */     
/*      */     public DescriptionGamePanel(GameEntityDTO entity, GameType type) {
/*  319 */       this.descriptionLayout = new SpringLayout();
/*  320 */       this.statusStarButton = new StatusStarButton(entity, type);
/*  321 */       setLayout(this.descriptionLayout);
/*  322 */       this.imagePanel = new CompleteSubEntityScene.ImagePanel(entity);
/*      */       
/*  324 */       ExtendedPanel descriptionEntityPanel = new ExtendedPanel();
/*  325 */       this.name = new JLabel(entity.getName());
/*  326 */       LocalizableLabel localizableLabel1 = new LocalizableLabel("modpack.complete.author");
/*  327 */       JLabel authorValue = new JLabel(entity.getAuthor());
/*  328 */       this.downloadLabel = (JLabel)new LocalizableLabel("modpack.description.download");
/*  329 */       JLabel downloadValue = new JLabel(getStringDownloadingCount(entity.getDownloadALL()));
/*  330 */       this.updateLabel = (JLabel)new LocalizableLabel("modpack.description.date");
/*      */       
/*  332 */       JLabel updateValue = new JLabel(this.format.format(new Date(entity.getUpdate().longValue())));
/*      */       
/*  334 */       this.gameVersion = new JLabel();
/*      */       
/*  336 */       LocalizableLabel localizableLabel2 = new LocalizableLabel("modpack.creation.version.game");
/*  337 */       if (Objects.isNull(entity.getLastGameVersion())) {
/*  338 */         this.gameVersion.setVisible(false);
/*  339 */         localizableLabel2.setVisible(false);
/*      */       } else {
/*  341 */         this.gameVersion.setText(entity.getLastGameVersion().getName());
/*      */       } 
/*  343 */       this.description = (JTextArea)new TextWrapperLabel(entity.getShortDescription());
/*  344 */       this.description.setVisible(false);
/*      */       
/*  346 */       authorValue.setHorizontalAlignment(2);
/*  347 */       downloadValue.setHorizontalAlignment(2);
/*  348 */       updateValue.setHorizontalAlignment(2);
/*  349 */       this.gameVersion.setHorizontalAlignment(2);
/*      */       
/*  351 */       SwingUtil.changeFontFamily(this.name, FontTL.ROBOTO_BOLD, 18);
/*  352 */       SwingUtil.changeFontFamily((JComponent)localizableLabel1, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_193);
/*  353 */       SwingUtil.changeFontFamily(this.downloadLabel, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
/*  354 */       SwingUtil.changeFontFamily((JComponent)localizableLabel2, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
/*  355 */       SwingUtil.changeFontFamily(this.updateLabel, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
/*      */       
/*  357 */       SwingUtil.changeFontFamily(authorValue, FontTL.ROBOTO_REGULAR, 14, ColorUtil.BLUE_MODPACK);
/*  358 */       SwingUtil.changeFontFamily(downloadValue, FontTL.ROBOTO_REGULAR, 14, ColorUtil.BLUE_MODPACK);
/*  359 */       SwingUtil.changeFontFamily(updateValue, FontTL.ROBOTO_REGULAR, 14, ColorUtil.BLUE_MODPACK);
/*  360 */       SwingUtil.changeFontFamily(this.gameVersion, FontTL.ROBOTO_REGULAR, 14, ColorUtil.BLUE_MODPACK);
/*      */       
/*  362 */       SwingUtil.changeFontFamily(this.description, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_64);
/*      */       
/*  364 */       SpringLayout descriptionSpring = new SpringLayout();
/*      */       
/*  366 */       descriptionEntityPanel.setLayout(descriptionSpring);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  371 */       this.descriptionLayout.putConstraint("West", this.imagePanel, 66, "West", this);
/*  372 */       this.descriptionLayout.putConstraint("East", this.imagePanel, 177, "West", this);
/*  373 */       this.descriptionLayout.putConstraint("North", this.imagePanel, 25, "North", this);
/*  374 */       this.descriptionLayout.putConstraint("South", this.imagePanel, -25, "South", this);
/*  375 */       add(this.imagePanel);
/*      */       
/*  377 */       this.descriptionLayout.putConstraint("West", (Component)descriptionEntityPanel, 13, "East", this.imagePanel);
/*      */       
/*  379 */       this.descriptionLayout.putConstraint("East", (Component)descriptionEntityPanel, 0, "East", this);
/*  380 */       this.descriptionLayout.putConstraint("North", (Component)descriptionEntityPanel, 25, "North", this);
/*  381 */       this.descriptionLayout.putConstraint("South", (Component)descriptionEntityPanel, -20, "South", this);
/*  382 */       add((Component)descriptionEntityPanel);
/*      */       
/*  384 */       descriptionSpring.putConstraint("West", this.name, 0, "West", (Component)descriptionEntityPanel);
/*  385 */       descriptionSpring.putConstraint("East", this.name, 250, "West", (Component)descriptionEntityPanel);
/*  386 */       descriptionSpring.putConstraint("North", this.name, 0, "North", (Component)descriptionEntityPanel);
/*  387 */       descriptionSpring.putConstraint("South", this.name, 23, "North", (Component)descriptionEntityPanel);
/*  388 */       descriptionEntityPanel.add(this.name);
/*      */       
/*  390 */       descriptionSpring.putConstraint("West", (Component)localizableLabel1, 0, "West", (Component)descriptionEntityPanel);
/*      */       
/*  392 */       descriptionSpring.putConstraint("East", (Component)localizableLabel1, (localizableLabel1.getPreferredSize()).width, "West", (Component)descriptionEntityPanel);
/*      */       
/*  394 */       descriptionSpring.putConstraint("North", (Component)localizableLabel1, 23, "North", (Component)descriptionEntityPanel);
/*      */       
/*  396 */       descriptionSpring.putConstraint("South", (Component)localizableLabel1, 42, "North", (Component)descriptionEntityPanel);
/*      */       
/*  398 */       descriptionEntityPanel.add((Component)localizableLabel1);
/*      */       
/*  400 */       descriptionSpring.putConstraint("West", authorValue, 5, "East", (Component)localizableLabel1);
/*  401 */       descriptionSpring.putConstraint("East", authorValue, 0, "East", (Component)descriptionEntityPanel);
/*      */       
/*  403 */       descriptionSpring.putConstraint("North", authorValue, 23, "North", (Component)descriptionEntityPanel);
/*      */       
/*  405 */       descriptionSpring.putConstraint("South", authorValue, 42, "North", (Component)descriptionEntityPanel);
/*      */       
/*  407 */       descriptionEntityPanel.add(authorValue);
/*  408 */       descriptionSpring.putConstraint("West", this.description, 0, "West", (Component)descriptionEntityPanel);
/*      */       
/*  410 */       descriptionSpring.putConstraint("East", this.description, -100, "East", (Component)descriptionEntityPanel);
/*      */       
/*  412 */       descriptionSpring.putConstraint("North", this.description, 46, "North", (Component)descriptionEntityPanel);
/*      */       
/*  414 */       descriptionSpring.putConstraint("South", this.description, 85, "North", (Component)descriptionEntityPanel);
/*      */       
/*  416 */       descriptionEntityPanel.add(this.description);
/*      */       
/*  418 */       descriptionSpring.putConstraint("West", this.downloadLabel, 0, "West", (Component)descriptionEntityPanel);
/*      */       
/*  420 */       descriptionSpring.putConstraint("East", this.downloadLabel, (this.downloadLabel.getPreferredSize()).width, "West", (Component)descriptionEntityPanel);
/*      */       
/*  422 */       descriptionSpring.putConstraint("North", this.downloadLabel, 90, "North", (Component)descriptionEntityPanel);
/*      */       
/*  424 */       descriptionSpring.putConstraint("South", this.downloadLabel, 0, "South", (Component)descriptionEntityPanel);
/*      */       
/*  426 */       descriptionEntityPanel.add(this.downloadLabel);
/*      */       
/*  428 */       descriptionSpring.putConstraint("West", downloadValue, 5, "East", this.downloadLabel);
/*  429 */       descriptionSpring.putConstraint("East", downloadValue, 
/*  430 */           (downloadValue.getPreferredSize()).width + 5, "East", this.downloadLabel);
/*  431 */       descriptionSpring.putConstraint("North", downloadValue, 90, "North", (Component)descriptionEntityPanel);
/*      */       
/*  433 */       descriptionSpring.putConstraint("South", downloadValue, 0, "South", (Component)descriptionEntityPanel);
/*      */       
/*  435 */       descriptionEntityPanel.add(downloadValue);
/*      */       
/*  437 */       descriptionSpring.putConstraint("West", this.updateLabel, 30, "East", downloadValue);
/*  438 */       descriptionSpring.putConstraint("East", this.updateLabel, 30 + 
/*  439 */           (this.updateLabel.getPreferredSize()).width, "East", downloadValue);
/*  440 */       descriptionSpring.putConstraint("North", this.updateLabel, 90, "North", (Component)descriptionEntityPanel);
/*      */       
/*  442 */       descriptionSpring.putConstraint("South", this.updateLabel, 0, "South", (Component)descriptionEntityPanel);
/*      */       
/*  444 */       descriptionEntityPanel.add(this.updateLabel);
/*      */       
/*  446 */       descriptionSpring.putConstraint("West", updateValue, 5, "East", this.updateLabel);
/*  447 */       descriptionSpring.putConstraint("East", updateValue, (updateValue.getPreferredSize()).width + 5, "East", this.updateLabel);
/*      */       
/*  449 */       descriptionSpring.putConstraint("North", updateValue, 90, "North", (Component)descriptionEntityPanel);
/*      */       
/*  451 */       descriptionSpring.putConstraint("South", updateValue, 0, "South", (Component)descriptionEntityPanel);
/*      */       
/*  453 */       descriptionEntityPanel.add(updateValue);
/*      */       
/*  455 */       descriptionSpring.putConstraint("West", (Component)localizableLabel2, 30, "East", updateValue);
/*      */       
/*  457 */       descriptionSpring.putConstraint("East", (Component)localizableLabel2, 30 + 
/*  458 */           (localizableLabel2.getPreferredSize()).width, "East", updateValue);
/*  459 */       descriptionSpring.putConstraint("North", (Component)localizableLabel2, 90, "North", (Component)descriptionEntityPanel);
/*      */       
/*  461 */       descriptionSpring.putConstraint("South", (Component)localizableLabel2, 0, "South", (Component)descriptionEntityPanel);
/*      */       
/*  463 */       descriptionEntityPanel.add((Component)localizableLabel2);
/*  464 */       descriptionSpring.putConstraint("West", this.gameVersion, 5, "East", (Component)localizableLabel2);
/*  465 */       descriptionSpring.putConstraint("East", this.gameVersion, (this.gameVersion.getPreferredSize()).width + 5, "East", (Component)localizableLabel2);
/*      */       
/*  467 */       descriptionSpring.putConstraint("North", this.gameVersion, 90, "North", (Component)descriptionEntityPanel);
/*      */       
/*  469 */       descriptionSpring.putConstraint("South", this.gameVersion, 0, "South", (Component)descriptionEntityPanel);
/*      */       
/*  471 */       descriptionEntityPanel.add(this.gameVersion);
/*      */ 
/*      */ 
/*      */       
/*  475 */       ExtendedPanel pictureCategories = new ExtendedPanel();
/*      */       
/*  477 */       int count = 0;
/*  478 */       for (CategoryDTO c : entity.getCategories()) {
/*      */         try {
/*  480 */           Icon icon = ImageCache.getNativeIcon("category/" + c.getName() + ".png");
/*  481 */           count++;
/*  482 */           final JLabel label = new JLabel(icon);
/*  483 */           label.setHorizontalAlignment(0);
/*  484 */           label.setAlignmentY(0.5F);
/*  485 */           pictureCategories.add(label);
/*  486 */           final ModpackCategoryPopupMenu popupMenu = new ModpackCategoryPopupMenu(c, label);
/*  487 */           label.addMouseListener(new MouseAdapter()
/*      */               {
/*      */                 public void mouseEntered(MouseEvent e) {
/*  490 */                   popupMenu.show(label, e.getX() + 15, e.getY() + 15);
/*      */                 }
/*      */ 
/*      */                 
/*      */                 public void mouseExited(MouseEvent e) {
/*  495 */                   popupMenu.setVisible(false);
/*      */                 }
/*      */               });
/*  498 */         } catch (Throwable e) {
/*  499 */           U.log(new Object[] { "problem with category " + c.getName() });
/*      */         } 
/*      */       } 
/*  502 */       pictureCategories.setLayout(new GridLayout(1, count, 0, 10));
/*      */       
/*  504 */       this.descriptionLayout.putConstraint("West", (Component)pictureCategories, -200, "East", this);
/*  505 */       this.descriptionLayout.putConstraint("East", (Component)pictureCategories, -25 - (5 - count) * 35, "East", this);
/*      */       
/*  507 */       this.descriptionLayout.putConstraint("North", (Component)pictureCategories, 24, "North", this);
/*  508 */       this.descriptionLayout.putConstraint("South", (Component)pictureCategories, 50, "North", this);
/*  509 */       add((Component)pictureCategories);
/*      */       
/*  511 */       this.descriptionLayout.putConstraint("West", (Component)this.statusStarButton, -38, "East", this);
/*  512 */       this.descriptionLayout.putConstraint("East", (Component)this.statusStarButton, -25, "East", this);
/*  513 */       this.descriptionLayout.putConstraint("North", (Component)this.statusStarButton, -35, "South", this);
/*  514 */       this.descriptionLayout.putConstraint("South", (Component)this.statusStarButton, -22, "South", this);
/*  515 */       add((Component)this.statusStarButton);
/*      */     }
/*      */     
/*      */     private String getStringDownloadingCount(Long i) {
/*  519 */       String res = "";
/*  520 */       if (i.longValue() < 1000L)
/*  521 */         return i.toString(); 
/*  522 */       if (i.longValue() < 1000000L) {
/*  523 */         res = (i.longValue() / 1000L) + " " + Localizable.get("modpack.thousand");
/*      */       } else {
/*  525 */         res = (i.longValue() / 1000000L) + " " + Localizable.get("modpack.million");
/*      */       } 
/*  527 */       if (Objects.nonNull(TLauncher.getInstance().getConfiguration().getLocale()) && "en"
/*  528 */         .equals(TLauncher.getInstance().getConfiguration().getLocale().getLanguage()))
/*  529 */         res = res.replace(" ", ""); 
/*  530 */       return res;
/*      */     } }
/*      */   private abstract class BaseSubtypeModel<T extends BaseModelElement> extends GameEntityTableModel { protected List<T> list;
/*      */     protected final SimpleDateFormat format;
/*      */     
/*      */     private BaseSubtypeModel() {
/*  536 */       this.list = new ArrayList<>();
/*  537 */       this.format = new SimpleDateFormat("dd/MM/YYYY", Localizable.get().getSelected());
/*      */     }
/*      */ 
/*      */     
/*      */     public int find(GameEntityDTO entity) {
/*  542 */       for (int i = 0; i < this.list.size(); i++) {
/*  543 */         if (entity.getId().equals(((CompleteSubEntityScene.BaseModelElement)this.list.get(i)).getEntity().getId())) {
/*  544 */           return i;
/*      */         }
/*      */       } 
/*  547 */       return -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {
/*  552 */       int index = find(e);
/*  553 */       if (index != -1) {
/*  554 */         ((CompleteSubEntityScene.BaseModelElement)this.list.get(index)).getModpackActButton().reset();
/*  555 */         fireTableCellUpdated(index, 5);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void installEntity(GameEntityDTO e, GameType type) {
/*  561 */       int index = find(e);
/*  562 */       if (index != -1) {
/*  563 */         ((CompleteSubEntityScene.BaseModelElement)this.list.get(index)).getModpackActButton().setTypeButton("REMOVE");
/*  564 */         fireTableCellUpdated(index, 5);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void removeEntity(GameEntityDTO e) {
/*  570 */       int index = find(e);
/*  571 */       if (index != -1) {
/*  572 */         ((CompleteSubEntityScene.BaseModelElement)this.list.get(index)).getModpackActButton().setTypeButton("INSTALL");
/*  573 */         fireTableCellUpdated(index, 5);
/*      */       } 
/*      */     } public abstract GameEntityDTO getRowObject(int param1Int); }
/*      */   protected static class BaseModelElement {
/*      */     private ModpackActButton modpackActButton; private GameEntityDTO entity;
/*  578 */     public void setModpackActButton(ModpackActButton modpackActButton) { this.modpackActButton = modpackActButton; } public void setEntity(GameEntityDTO entity) { this.entity = entity; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof BaseModelElement)) return false;  BaseModelElement other = (BaseModelElement)o; if (!other.canEqual(this)) return false;  Object this$modpackActButton = getModpackActButton(), other$modpackActButton = other.getModpackActButton(); if ((this$modpackActButton == null) ? (other$modpackActButton != null) : !this$modpackActButton.equals(other$modpackActButton)) return false;  Object this$entity = getEntity(), other$entity = other.getEntity(); return !((this$entity == null) ? (other$entity != null) : !this$entity.equals(other$entity)); } protected boolean canEqual(Object other) { return other instanceof BaseModelElement; } public int hashCode() { int PRIME = 59; result = 1; Object $modpackActButton = getModpackActButton(); result = result * 59 + (($modpackActButton == null) ? 43 : $modpackActButton.hashCode()); Object $entity = getEntity(); return result * 59 + (($entity == null) ? 43 : $entity.hashCode()); } public String toString() { return "CompleteSubEntityScene.BaseModelElement(modpackActButton=" + getModpackActButton() + ", entity=" + getEntity() + ")"; }
/*      */ 
/*      */     
/*      */     public BaseModelElement(ModpackActButton modpackActButton, GameEntityDTO entity) {
/*  582 */       this.modpackActButton = modpackActButton;
/*  583 */       this.entity = entity;
/*      */     }
/*      */     
/*  586 */     public ModpackActButton getModpackActButton() { return this.modpackActButton; } public GameEntityDTO getEntity() {
/*  587 */       return this.entity;
/*      */     }
/*      */   } private class VersionModelElement extends BaseModelElement { private VersionDTO version;
/*  590 */     public void setVersion(VersionDTO version) { this.version = version; } public String toString() { return "CompleteSubEntityScene.VersionModelElement(version=" + getVersion() + ")"; }
/*  591 */     public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof VersionModelElement)) return false;  VersionModelElement other = (VersionModelElement)o; if (!other.canEqual(this)) return false;  if (!super.equals(o)) return false;  Object this$version = getVersion(), other$version = other.getVersion(); return !((this$version == null) ? (other$version != null) : !this$version.equals(other$version)); } protected boolean canEqual(Object other) { return other instanceof VersionModelElement; } public int hashCode() { int PRIME = 59; result = super.hashCode(); Object $version = getVersion(); return result * 59 + (($version == null) ? 43 : $version.hashCode()); }
/*      */      public VersionDTO getVersion() {
/*  593 */       return this.version;
/*      */     }
/*      */     public VersionModelElement(ModpackActButton modpackActButton, GameEntityDTO entity, VersionDTO version) {
/*  596 */       super(modpackActButton, entity);
/*  597 */       this.version = version;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract class GameEntityTableModel
/*      */     extends AbstractTableModel
/*      */     implements GameEntityListener
/*      */   {
/*      */     public void activationStarted(GameEntityDTO e) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void activation(GameEntityDTO e) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void activationError(GameEntityDTO e, Throwable t) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void processingStarted(GameEntityDTO e, VersionDTO version) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void installEntity(GameEntityDTO e, GameType type) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void installEntity(CompleteVersion e) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void removeEntity(GameEntityDTO e) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void removeCompleteVersion(CompleteVersion e) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void populateStatus(GameEntityDTO status, GameType type, boolean state) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void updateVersion(CompleteVersion v, CompleteVersion newVersion) {}
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected class ModpackTable
/*      */     extends JTable
/*      */   {
/*      */     protected void init() {
/*  661 */       setRowHeight(58);
/*  662 */       getColumnModel().setColumnSelectionAllowed(false);
/*  663 */       setShowVerticalLines(false);
/*  664 */       setCellSelectionEnabled(false);
/*  665 */       setGridColor(ColorUtil.COLOR_244);
/*  666 */       JTableHeader header = getTableHeader();
/*  667 */       header.setPreferredSize(new Dimension((header.getPreferredSize()).width - 20, 48));
/*  668 */       header.setDefaultRenderer(new TableCellRenderer() {
/*  669 */             final DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/*  675 */               DefaultTableCellRenderer comp = (DefaultTableCellRenderer)this.cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
/*  676 */               comp.setBorder(BorderFactory.createEmptyBorder());
/*  677 */               comp.setBackground(new Color(63, 186, 255));
/*  678 */               comp.setHorizontalAlignment(0);
/*  679 */               SwingUtil.changeFontFamily(comp, FontTL.ROBOTO_REGULAR, 12, Color.WHITE);
/*  680 */               return comp;
/*      */             }
/*      */           });
/*  683 */       CompleteSubEntityScene.ModpackTableRenderer centerRenderer = new CompleteSubEntityScene.ModpackTableRenderer();
/*  684 */       for (int i = 0; i < getModel().getColumnCount() - 1; i++)
/*  685 */         getColumnModel().getColumn(i).setCellRenderer(centerRenderer); 
/*  686 */       getColumnModel().getColumn(1).setPreferredWidth(250);
/*  687 */       getTableHeader().setReorderingAllowed(false);
/*  688 */       setDefaultEditor(CompleteSubEntityScene.BaseModelElement.class, (TableCellEditor)new JTableButtonRenderer());
/*  689 */       setDefaultRenderer(CompleteSubEntityScene.BaseModelElement.class, (TableCellRenderer)new JTableButtonRenderer());
/*      */     }
/*      */     
/*      */     public ModpackTable(AbstractTableModel model) {
/*  693 */       super(model);
/*  694 */       init();
/*      */     }
/*      */   }
/*      */   
/*      */   protected class ModpackTableRenderer
/*      */     extends DefaultTableCellRenderer
/*      */   {
/*      */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/*  702 */       DefaultTableCellRenderer cell = (DefaultTableCellRenderer)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
/*      */       
/*  704 */       cell.setHorizontalAlignment(0);
/*  705 */       if (column == 1)
/*  706 */         cell.setHorizontalAlignment(2); 
/*  707 */       SwingUtil.changeFontFamily(cell, FontTL.ROBOTO_REGULAR, 12, ColorUtil.COLOR_25);
/*  708 */       cell.setFocusable(false);
/*  709 */       if (hasFocus) {
/*  710 */         setBorder(BorderFactory.createEmptyBorder());
/*      */       }
/*  712 */       return cell;
/*      */     }
/*      */   }
/*      */   
/*      */   public class FullGameEntity
/*      */     extends GameEntityPanel {
/*  718 */     private final Color UP_BACKGROUND = new Color(60, 170, 232);
/*      */     
/*      */     private final UpInstallButton installButton;
/*      */     private final GameEntityDTO entity;
/*      */     private final GameType type;
/*      */     private final CompleteSubEntityScene.DescriptionGamePanel viewEntity;
/*      */     private final Injector injector;
/*      */     private final GroupPanel centerButtons;
/*      */     private final JPanel centerView;
/*      */     private JTable table;
/*      */     
/*      */     public Integer getNextPageIndex() {
/*  730 */       return this.nextPageIndex; } public void setNextPageIndex(Integer nextPageIndex) {
/*  731 */       this.nextPageIndex = nextPageIndex;
/*  732 */     } private Integer nextPageIndex = Integer.valueOf(0);
/*  733 */     private boolean nextPage = true; private boolean processingRequest; private HtmlTextPane descriptionFull; private JScrollPane jScrollPane; public boolean isNextPage() { return this.nextPage; } public void setNextPage(boolean nextPage) {
/*  734 */       this.nextPage = nextPage;
/*      */     }
/*  736 */     public boolean isProcessingRequest() { return this.processingRequest; } public void setProcessingRequest(boolean processingRequest) {
/*  737 */       this.processingRequest = processingRequest;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public GroupPanel getCenterButtons() {
/*  747 */       return this.centerButtons;
/*      */     }
/*      */     
/*      */     public void clearContent() {
/*  751 */       U.log(new Object[] { "clean" });
/*  752 */       this.descriptionFull.setText("");
/*      */     }
/*      */ 
/*      */     
/*      */     public JPanel getCenterView() {
/*  757 */       return this.centerView;
/*      */     }
/*      */ 
/*      */     
/*      */     private FullGameEntity(final GameEntityDTO entity, BackPanel backPanel, final GameType type) {
/*  762 */       this.entity = entity;
/*  763 */       this.type = type;
/*  764 */       this.injector = TLauncher.getInjector();
/*  765 */       SpringLayout spring = new SpringLayout();
/*  766 */       setLayout(spring);
/*      */       
/*  768 */       UpdaterFullButton updaterFullButton1 = new UpdaterFullButton(this.UP_BACKGROUND, ColorUtil.BLUE_MODPACK_BUTTON_UP, "modpack.complete.site.button", "official-site.png");
/*      */       
/*  770 */       updaterFullButton1.setIconTextGap(15);
/*      */       
/*  772 */       UpdaterFullButton updaterFullButton2 = new UpdaterFullButton(this.UP_BACKGROUND, ColorUtil.BLUE_MODPACK_BUTTON_UP, "tlmods.open.link", "official-site.png");
/*      */       
/*  774 */       updaterFullButton2.setIconTextGap(15);
/*  775 */       if (entity.getLinkProject() == null) {
/*  776 */         updaterFullButton1.setVisible(false);
/*      */       }
/*  778 */       if (Objects.isNull(entity.getTlmodsLinkProject())) {
/*  779 */         updaterFullButton2.setVisible(false);
/*      */       }
/*      */       
/*  782 */       ButtonGroup group = new ButtonGroup();
/*  783 */       GameRadioButton reviewButton = new GameRadioButton("modpack.complete.review.button");
/*  784 */       GameRadioButton picturesButton = new GameRadioButton("modpack.complete.picture.button");
/*      */       
/*  786 */       GameRadioButton discussionButton = new GameRadioButton(Localizable.get("modpack.complete.discussion.button") + String.format(" (%s)", new Object[] { Long.valueOf(entity.getTotalComments()) }));
/*  787 */       reviewButton.setSelected(true);
/*  788 */       reviewButton.setActionCommand("REVIEW");
/*  789 */       picturesButton.setActionCommand("PICTURES");
/*  790 */       discussionButton.setActionCommand("DISCUSSION");
/*      */       
/*  792 */       group.add((AbstractButton)reviewButton);
/*  793 */       group.add((AbstractButton)picturesButton);
/*  794 */       group.add((AbstractButton)discussionButton);
/*      */       
/*  796 */       GameRadioButton versionsButton = new GameRadioButton("modpack.complete.versions.button");
/*  797 */       versionsButton.setActionCommand("VERSIONS");
/*  798 */       group.add((AbstractButton)versionsButton);
/*  799 */       final Color backgroundOldButtonColor = new Color(213, 213, 213);
/*  800 */       final UpdaterButton oldButton = new UpdaterButton(backgroundOldButtonColor, "modpack.complete.old.button");
/*  801 */       oldButton.addMouseListener(new MouseAdapter()
/*      */           {
/*      */             public void mouseEntered(MouseEvent e) {
/*  804 */               oldButton.setBackground(new Color(160, 160, 160));
/*      */             }
/*      */ 
/*      */             
/*      */             public void mouseExited(MouseEvent e) {
/*  809 */               oldButton.setBackground(backgroundOldButtonColor);
/*      */             }
/*      */           });
/*      */       
/*  813 */       oldButton.addActionListener(new ActionListener()
/*      */           {
/*      */             public void actionPerformed(ActionEvent e)
/*      */             {
/*  817 */               CompletableFuture.runAsync(() -> DesktopUtil.uncheckCall(()))
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
/*  832 */                 .exceptionally(t -> {
/*      */                     SwingUtilities.invokeLater(());
/*      */ 
/*      */ 
/*      */                     
/*      */                     U.log(new Object[] { t });
/*      */ 
/*      */ 
/*      */                     
/*      */                     return null;
/*      */                   });
/*      */             }
/*      */           });
/*      */ 
/*      */       
/*  847 */       final JLabel originalEnDescription = new JLabel((Icon)ImageCache.getIcon("modpack-original-transation.png"));
/*  848 */       originalEnDescription.setPreferredSize(new Dimension(66, 52));
/*      */       
/*  850 */       originalEnDescription.setBorder(BorderFactory.createEmptyBorder());
/*  851 */       if (TLauncher.getInstance().getConfiguration().getLocale().getLanguage().equals("en")) {
/*  852 */         originalEnDescription.setVisible(false);
/*      */       }
/*      */       
/*  855 */       this.installButton = new UpInstallButton(entity, type, (CompleteSubEntityScene.this.getMainPane()).modpackScene.localmodpacks);
/*  856 */       this.installButton.setBorder(BorderFactory.createEmptyBorder(0, 19, 0, 0));
/*  857 */       this.installButton.setIconTextGap(18);
/*      */       
/*  859 */       ExtendedPanel extendedPanel1 = new ExtendedPanel();
/*  860 */       extendedPanel1.setLayout(new FlowLayout(0, 0, 0));
/*  861 */       extendedPanel1.setOpaque(true);
/*  862 */       extendedPanel1.setBackground(ColorUtil.COLOR_246);
/*      */       
/*  864 */       ExtendedPanel extendedPanel2 = new ExtendedPanel();
/*  865 */       extendedPanel2.setLayout(new BorderLayout());
/*  866 */       this.viewEntity = new CompleteDescriptionGamePanel(entity, type);
/*  867 */       final VersionModel model = new VersionModel();
/*      */       
/*  869 */       backPanel.addBackListener(new MouseAdapter()
/*      */           {
/*      */             public void mousePressed(MouseEvent e) {
/*  872 */               if (SwingUtilities.isLeftMouseButton(e)) {
/*  873 */                 CompleteSubEntityScene.this.manager.removeGameListener(type, model);
/*      */               }
/*      */             }
/*      */           });
/*  877 */       CompleteSubEntityScene.this.manager.addGameListener(type, model);
/*  878 */       this.table = new CompleteSubEntityScene.ModpackTable(model);
/*      */       
/*  880 */       this.table.setBackground(Color.WHITE);
/*  881 */       ScrollPane scrollPane = ModpackScene.createScrollWrapper(this.table);
/*      */       
/*  883 */       extendedPanel2.add((Component)scrollPane, "Center");
/*  884 */       SpringLayout upSpring = new SpringLayout();
/*  885 */       ExtendedPanel upButtons = new ExtendedPanel(upSpring);
/*  886 */       upButtons.setOpaque(true);
/*  887 */       upButtons.setBackground(this.UP_BACKGROUND);
/*  888 */       JPanel centerButtonsWrapper = new JPanel(new BorderLayout(0, 0));
/*  889 */       this.centerButtons = new GroupPanel(242);
/*  890 */       this.centerButtons.setLayout(new GridBagLayout());
/*  891 */       this.centerButtons.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
/*      */       
/*  893 */       this.centerButtons.addInGroup((AbstractButton)reviewButton, 0);
/*      */       
/*  895 */       this.centerButtons.addInGroup((AbstractButton)picturesButton, 1);
/*  896 */       this.centerButtons.addInGroup((AbstractButton)versionsButton, 2);
/*  897 */       if (!GameType.MODPACK.equals(type)) {
/*  898 */         this.centerButtons.addInGroup((AbstractButton)discussionButton, 3);
/*  899 */         this.centerButtons.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 320));
/*      */       } else {
/*  901 */         this.centerButtons.addInGroup((AbstractButton)discussionButton, 7);
/*      */       } 
/*      */       
/*  904 */       this.centerView = new JPanel(new CardLayout(0, 0));
/*  905 */       this.centerView.setBackground(Color.WHITE);
/*  906 */       this.centerView.setOpaque(true);
/*      */       
/*  908 */       SwingUtil.configHorizontalSpingLayout(upSpring, (JComponent)backPanel, (JComponent)upButtons, 66);
/*  909 */       upSpring.putConstraint("West", (Component)backPanel, 0, "West", (Component)upButtons);
/*  910 */       upSpring.putConstraint("East", (Component)backPanel, 66, "West", (Component)upButtons);
/*  911 */       upButtons.add((Component)backPanel);
/*  912 */       SwingUtil.configHorizontalSpingLayout(upSpring, (JComponent)this.installButton, (JComponent)backPanel, 168);
/*  913 */       upButtons.add((Component)this.installButton);
/*      */       
/*  915 */       SwingUtil.configHorizontalSpingLayout(upSpring, (JComponent)updaterFullButton2, (JComponent)this.installButton, 0);
/*  916 */       upSpring.putConstraint("West", (Component)updaterFullButton2, 312, "East", (Component)this.installButton);
/*  917 */       upSpring.putConstraint("East", (Component)updaterFullButton2, 562, "East", (Component)this.installButton);
/*  918 */       upButtons.add((Component)updaterFullButton2);
/*  919 */       SwingUtil.configHorizontalSpingLayout(upSpring, (JComponent)updaterFullButton1, (JComponent)this.installButton, 0);
/*  920 */       upSpring.putConstraint("West", (Component)updaterFullButton1, 563, "East", (Component)this.installButton);
/*  921 */       upSpring.putConstraint("East", (Component)updaterFullButton1, 762, "East", (Component)this.installButton);
/*  922 */       upButtons.add((Component)updaterFullButton1);
/*      */       
/*  924 */       int gup = 20;
/*  925 */       this.descriptionFull = new HtmlTextPane("text/html", "");
/*  926 */       this.descriptionFull.setText(entity.getDescription());
/*  927 */       this.descriptionFull.setOpaque(true);
/*      */       
/*  929 */       this.descriptionFull.setBackground(ColorUtil.COLOR_246);
/*  930 */       this.jScrollPane = new JScrollPane((Component)this.descriptionFull, 20, 31);
/*      */       
/*  932 */       this.jScrollPane.getVerticalScrollBar().setUI((ScrollBarUI)new ModpackScrollBarUI()
/*      */           {
/*      */             public Dimension getPreferredSize(JComponent c)
/*      */             {
/*  936 */               return new Dimension(13, (super.getPreferredSize(c)).height);
/*      */             }
/*      */           });
/*  939 */       this.jScrollPane.setBorder(BorderFactory.createEmptyBorder());
/*  940 */       SwingUtilities.invokeLater(() -> this.jScrollPane.getVerticalScrollBar().setValue(0));
/*      */       
/*  942 */       int height = 318;
/*  943 */       ExtendedPanel extendedPanel3 = new ExtendedPanel(new FlowLayout(0, 0, 0));
/*  944 */       extendedPanel3.setPreferredSize(new Dimension(ModpackScene.SIZE.width, height));
/*  945 */       this.jScrollPane.setPreferredSize(new Dimension(ModpackScene.SIZE.width - gup * 2, height - 40));
/*  946 */       extendedPanel3.setBorder(BorderFactory.createEmptyBorder(20, gup, 20, gup));
/*  947 */       extendedPanel3.add(this.jScrollPane);
/*  948 */       extendedPanel1.add((Component)extendedPanel3);
/*  949 */       SwingUtil.changeFontFamily((JComponent)this.installButton, FontTL.ROBOTO_BOLD, 14, Color.WHITE);
/*  950 */       SwingUtil.changeFontFamily((JComponent)updaterFullButton1, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
/*  951 */       SwingUtil.changeFontFamily((JComponent)updaterFullButton2, FontTL.ROBOTO_REGULAR, 14, Color.YELLOW);
/*      */       
/*  953 */       SwingUtil.changeFontFamily((JComponent)reviewButton, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
/*  954 */       SwingUtil.changeFontFamily((JComponent)versionsButton, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
/*  955 */       SwingUtil.changeFontFamily((JComponent)picturesButton, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
/*  956 */       SwingUtil.changeFontFamily((JComponent)discussionButton, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
/*  957 */       SwingUtil.changeFontFamily((JComponent)oldButton, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
/*  958 */       PicturePanel picturePanel = new PicturePanel(entity, type);
/*  959 */       this.centerView.setBackground(Color.WHITE);
/*  960 */       this.centerView.add((Component)extendedPanel1, "REVIEW");
/*  961 */       this.centerView.add((Component)picturePanel, "PICTURES");
/*  962 */       this.centerView.add((Component)extendedPanel2, "VERSIONS");
/*  963 */       DiscussionPanel dp = (DiscussionPanel)this.injector.getInstance(DiscussionPanel.class);
/*  964 */       dp.setDto(entity);
/*  965 */       dp.setType(type);
/*  966 */       dp.setCommentCreationForm(CompleteSubEntityScene.this.commentCreationForm);
/*      */       
/*  968 */       this.centerView.add(dp.getScrollPane(), "DISCUSSION");
/*      */       
/*  970 */       spring.putConstraint("North", (Component)upButtons, 0, "North", (Component)this);
/*  971 */       spring.putConstraint("West", (Component)upButtons, 0, "West", (Component)this);
/*  972 */       spring.putConstraint("South", (Component)upButtons, 56, "North", (Component)this);
/*  973 */       spring.putConstraint("East", (Component)upButtons, 0, "East", (Component)this);
/*      */       
/*  975 */       add((Component)upButtons);
/*      */       
/*  977 */       spring.putConstraint("North", this.viewEntity, 0, "South", (Component)upButtons);
/*  978 */       spring.putConstraint("West", this.viewEntity, 0, "West", (Component)this);
/*  979 */       spring.putConstraint("South", this.viewEntity, 159, "South", (Component)upButtons);
/*  980 */       spring.putConstraint("East", this.viewEntity, 0, "East", (Component)this);
/*  981 */       add(this.viewEntity);
/*  982 */       centerButtonsWrapper.add((Component)this.centerButtons, "Center");
/*  983 */       JPanel rightPart = new JPanel(new BorderLayout());
/*  984 */       rightPart.add(originalEnDescription, "East");
/*  985 */       rightPart.add((Component)oldButton, "West");
/*  986 */       oldButton.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
/*      */       
/*  988 */       centerButtonsWrapper.add(rightPart, "East");
/*      */       
/*  990 */       spring.putConstraint("North", centerButtonsWrapper, 0, "South", this.viewEntity);
/*  991 */       spring.putConstraint("West", centerButtonsWrapper, 0, "West", (Component)this);
/*  992 */       spring.putConstraint("South", centerButtonsWrapper, 52, "South", this.viewEntity);
/*  993 */       spring.putConstraint("East", centerButtonsWrapper, 0, "East", (Component)this);
/*  994 */       add(centerButtonsWrapper);
/*      */       
/*  996 */       spring.putConstraint("North", this.centerView, 0, "South", centerButtonsWrapper);
/*  997 */       spring.putConstraint("West", this.centerView, 0, "West", (Component)this);
/*  998 */       spring.putConstraint("South", this.centerView, 321, "South", centerButtonsWrapper);
/*  999 */       spring.putConstraint("East", this.centerView, 0, "East", (Component)this);
/* 1000 */       add(this.centerView);
/*      */ 
/*      */       
/* 1003 */       ModpackManager modpackManager = (ModpackManager)this.injector.getInstance(ModpackManager.class);
/* 1004 */       ActionListener listener = new ActionListener()
/*      */         {
/*      */           public void actionPerformed(ActionEvent e) {
/* 1007 */             ((CardLayout)CompleteSubEntityScene.FullGameEntity.this.centerView.getLayout()).show(CompleteSubEntityScene.FullGameEntity.this.centerView, e.getActionCommand());
/*      */           }
/*      */         };
/* 1010 */       reviewButton.addActionListener(listener);
/* 1011 */       versionsButton.addActionListener(listener);
/* 1012 */       picturesButton.addActionListener(listener);
/* 1013 */       discussionButton.addActionListener(listener);
/*      */       
/* 1015 */       updaterFullButton1.addActionListener(new ActionListener()
/*      */           {
/*      */             public void actionPerformed(ActionEvent e) {
/* 1018 */               OS.openLink(entity.getLinkProject());
/*      */             }
/*      */           });
/*      */       
/* 1022 */       updaterFullButton2.addActionListener(ev -> OS.openLink(entity.getTlmodsLinkProject()));
/*      */       
/* 1024 */       modpackManager.addGameListener(type, (GameEntityListener)this);
/* 1025 */       backPanel.addBackListener(new MouseAdapter()
/*      */           {
/*      */             public void mousePressed(MouseEvent e) {
/* 1028 */               if (SwingUtilities.isLeftMouseButton(e)) {
/* 1029 */                 CompleteSubEntityScene.this.manager.removeGameListener(type, (GameEntityListener)CompleteSubEntityScene.FullGameEntity.this);
/*      */               }
/*      */             }
/*      */           });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1041 */       originalEnDescription.addMouseListener(new MouseAdapter()
/*      */           {
/*      */             private boolean active = true;
/*      */             
/*      */             public void mouseClicked(MouseEvent mouseEvent) {
/* 1046 */               if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
/* 1047 */                 int currentValue = CompleteSubEntityScene.FullGameEntity.this.jScrollPane.getVerticalScrollBar().getValue();
/* 1048 */                 if (this.active) {
/* 1049 */                   CompletableFuture.runAsync(() -> DesktopUtil.uncheckCall(()))
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                     
/* 1058 */                     .exceptionally(ex -> {
/*      */                         U.log(new Object[] { ex });
/*      */                         
/*      */                         Alert.showError("", Localizable.get("modpack.remote.not.found", new Object[] { Localizable.get("modpack.try.later") }), null);
/*      */                         return null;
/*      */                       });
/*      */                 } else {
/* 1065 */                   CompleteSubEntityScene.FullGameEntity.this.descriptionFull.setText(entity.getDescription());
/* 1066 */                   this.active = true;
/*      */                 } 
/*      */               } 
/*      */             }
/*      */ 
/*      */ 
/*      */             
/*      */             public void mouseEntered(MouseEvent mouseEvent) {
/* 1074 */               originalEnDescription.setIcon((Icon)ImageCache.getIcon("modpack-original-transation-up.png"));
/*      */             }
/*      */ 
/*      */             
/*      */             public void mouseExited(MouseEvent mouseEvent) {
/* 1079 */               originalEnDescription.setIcon((Icon)ImageCache.getIcon("modpack-original-transation.png"));
/*      */             }
/*      */           });
/*      */       
/* 1083 */       extendedPanel2.addComponentListener(new ComponentAdapter()
/*      */           {
/*      */             public void componentShown(ComponentEvent e) {
/* 1086 */               if (CompleteSubEntityScene.FullGameEntity.this.table.getModel().getRowCount() > 0)
/*      */                 return; 
/* 1088 */               CompleteSubEntityScene.FullGameEntity.this.fillVersions();
/*      */             }
/*      */           });
/* 1091 */       scrollPane.getVerticalScrollBar().addAdjustmentListener((AdjustmentListener)new VersionsAdjustmentListener(this.table, this));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void processingStarted(GameEntityDTO e, VersionDTO version) {}
/*      */ 
/*      */     
/*      */     public void installEntity(GameEntityDTO e, GameType type) {
/* 1100 */       this.installButton.installEntity(e, type);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {}
/*      */ 
/*      */     
/*      */     public void populateStatus(GameEntityDTO entity, GameType type, boolean state) {
/* 1109 */       if (entity.getId().equals(this.entity.getId())) {
/* 1110 */         this.viewEntity.getStatusStarButton().setStatus(state);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void removeEntity(GameEntityDTO e) {
/* 1116 */       this.installButton.removeEntity(e);
/*      */     }
/*      */     
/*      */     public void fillVersions() {
/* 1120 */       CompletableFuture.runAsync(() -> DesktopUtil.uncheckCall(()))
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
/* 1136 */         .exceptionally(t -> {
/*      */             setProcessingRequest(false);
/*      */             U.log(new Object[] { t });
/*      */             return null;
/*      */           });
/*      */     }
/*      */     
/*      */     class VersionModel
/*      */       extends CompleteSubEntityScene.BaseSubtypeModel<CompleteSubEntityScene.VersionModelElement> {
/*      */       public void addElements(List<? extends VersionDTO> list) {
/* 1146 */         ModpackComboBox modpackComboBox = (TLauncher.getInstance().getFrame()).mp.modpackScene.localmodpacks;
/* 1147 */         BaseModpackFilter<VersionDTO> bmd = BaseModpackFilter.getBaseModpackStandardFilters(CompleteSubEntityScene.FullGameEntity.this.entity, CompleteSubEntityScene.FullGameEntity.this.type, modpackComboBox);
/*      */         
/* 1149 */         for (VersionDTO v : list) {
/* 1150 */           ModpackTableVersionButton button = new ModpackTableVersionButton(CompleteSubEntityScene.FullGameEntity.this.entity, CompleteSubEntityScene.FullGameEntity.this.type, modpackComboBox, v, bmd);
/*      */           
/* 1152 */           this.list.add(new CompleteSubEntityScene.VersionModelElement((ModpackActButton)button, CompleteSubEntityScene.FullGameEntity.this.entity, v));
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public int getRowCount() {
/* 1159 */         return this.list.size();
/*      */       }
/*      */ 
/*      */       
/*      */       public int getColumnCount() {
/* 1164 */         return 6;
/*      */       }
/*      */ 
/*      */       
/*      */       public Object getValueAt(int rowIndex, int columnIndex) {
/* 1169 */         VersionDTO v = ((CompleteSubEntityScene.VersionModelElement)this.list.get(rowIndex)).getVersion();
/* 1170 */         switch (columnIndex) {
/*      */           case 0:
/* 1172 */             return this.format.format(new Date(v.getUpdateDate().longValue()));
/*      */           case 1:
/* 1174 */             return v.getName();
/*      */           case 2:
/* 1176 */             return (Objects.isNull(v.getMinecraftVersionTypes()) || v.getMinecraftVersionTypes().isEmpty()) ? 
/* 1177 */               Localizable.get("modpack.version.any") : v
/* 1178 */               .getMinecraftVersionTypes().stream()
/* 1179 */               .map(vv -> Localizable.get("modpack.version." + vv.getName()))
/* 1180 */               .collect(Collectors.joining(", "));
/*      */           case 3:
/* 1182 */             return v.getType();
/*      */           case 4:
/* 1184 */             if (v.getGameVersionsDTO().isEmpty())
/* 1185 */               return Localizable.get("modpack.version.any"); 
/* 1186 */             return v.getGameVersionsDTO().stream().map(GameVersionDTO::getName)
/* 1187 */               .collect(Collectors.joining(", "));
/*      */           case 5:
/* 1189 */             return ((CompleteSubEntityScene.VersionModelElement)this.list.get(rowIndex)).getModpackActButton();
/*      */         } 
/* 1191 */         return null;
/*      */       }
/*      */ 
/*      */       
/*      */       public Class<?> getColumnClass(int columnIndex) {
/* 1196 */         if (columnIndex == 5) {
/* 1197 */           return CompleteSubEntityScene.BaseModelElement.class;
/*      */         }
/* 1199 */         return super.getColumnClass(columnIndex);
/*      */       }
/*      */ 
/*      */       
/*      */       public String getColumnName(int column) {
/* 1204 */         String line = "";
/* 1205 */         switch (column) {
/*      */           case 0:
/* 1207 */             line = Localizable.get("version.manager.editor.field.time");
/* 1208 */             return line.substring(0, line.length() - 1);
/*      */           case 1:
/* 1210 */             return Localizable.get("version.release");
/*      */           case 2:
/* 1212 */             line = Localizable.get("version.manager.editor.field.type");
/* 1213 */             return line.substring(0, line.length() - 1);
/*      */           case 3:
/* 1215 */             return Localizable.get("modpack.table.version.maturiry");
/*      */           case 4:
/* 1217 */             return Localizable.get("modpack.table.pack.element.version");
/*      */           case 5:
/* 1219 */             return Localizable.get("modpack.table.pack.element.operation");
/*      */         } 
/* 1221 */         return "";
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void installEntity(GameEntityDTO e, GameType type) {
/* 1227 */         int index = findByVersion(e, e.getVersion());
/* 1228 */         if (index != -1) {
/* 1229 */           ((CompleteSubEntityScene.VersionModelElement)this.list.get(index)).getModpackActButton().setTypeButton("REMOVE");
/* 1230 */           fireTableCellUpdated(index, 5);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*      */       public void removeEntity(GameEntityDTO e) {
/* 1236 */         int index = findByVersion(e, e.getVersion());
/* 1237 */         if (index != -1) {
/* 1238 */           ((CompleteSubEntityScene.VersionModelElement)this.list.get(index)).getModpackActButton().setTypeButton("INSTALL");
/* 1239 */           fireTableCellUpdated(index, 5);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*      */       public void processingStarted(GameEntityDTO e, VersionDTO version) {
/* 1245 */         if (Objects.nonNull(version) && e.getId().equals(CompleteSubEntityScene.FullGameEntity.this.entity.getId())) {
/* 1246 */           for (int i = 0; i < this.list.size(); i++) {
/* 1247 */             if (((CompleteSubEntityScene.VersionModelElement)this.list.get(i)).getVersion().getId().equals(version.getId())) {
/* 1248 */               ((CompleteSubEntityScene.VersionModelElement)this.list.get(i)).getModpackActButton().setTypeButton("PROCESSING");
/* 1249 */               fireTableCellUpdated(i, 5);
/*      */               return;
/*      */             } 
/*      */           } 
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*      */       public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {
/* 1258 */         int index = findByVersion(e, v);
/* 1259 */         if (index != -1) {
/* 1260 */           ((CompleteSubEntityScene.VersionModelElement)this.list.get(index)).getModpackActButton().reset();
/* 1261 */           fireTableCellUpdated(index, 5);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean isCellEditable(int rowIndex, int columnIndex) {
/* 1267 */         return (columnIndex == 5);
/*      */       }
/*      */       
/*      */       public int findByVersion(GameEntityDTO e, VersionDTO v) {
/* 1271 */         if (e.getId().equals(CompleteSubEntityScene.FullGameEntity.this.entity.getId())) {
/* 1272 */           for (int i = 0; i < this.list.size(); i++) {
/* 1273 */             CompleteSubEntityScene.VersionModelElement element = this.list.get(i);
/* 1274 */             if (element.getVersion().getId().equals(v.getId())) {
/* 1275 */               return i;
/*      */             }
/*      */           } 
/*      */         }
/* 1279 */         return -1;
/*      */       }
/*      */ 
/*      */       
/*      */       public GameEntityDTO getRowObject(int rowIndex) {
/* 1284 */         return null;
/*      */       }
/*      */     }
/*      */     
/*      */     class CompleteDescriptionGamePanel extends CompleteSubEntityScene.DescriptionGamePanel {
/*      */       public static final int SHADOW_PANEL = 223;
/*      */       
/*      */       public CompleteDescriptionGamePanel(GameEntityDTO entity, GameType type) {
/* 1292 */         super(entity, type);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       protected void paintComponent(Graphics g0) {
/* 1298 */         super.paintComponent(g0);
/* 1299 */         Rectangle rec = getVisibleRect();
/* 1300 */         int y = rec.y, i = 223;
/*      */         
/* 1302 */         Graphics2D g2 = (Graphics2D)g0;
/* 1303 */         for (; y < rec.height + rec.y; y++) {
/* 1304 */           g2.setColor(new Color(i, i, i));
/* 1305 */           if (i != 255) {
/* 1306 */             i++;
/*      */           }
/* 1308 */           g2.drawLine(rec.x, y, rec.x + rec.width, y);
/*      */         } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateFavoriteValue() {
/* 1318 */     if (Objects.nonNull(this.fullGameEntity))
/* 1319 */       this.fullGameEntity.viewEntity.statusStarButton.updateStatus(); 
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\scenes\CompleteSubEntityScene.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */