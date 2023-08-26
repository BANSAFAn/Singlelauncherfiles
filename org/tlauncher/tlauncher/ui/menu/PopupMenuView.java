/*     */ package org.tlauncher.tlauncher.ui.menu;
/*     */ import java.awt.AWTException;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.MouseInfo;
/*     */ import java.awt.Point;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.event.PopupMenuEvent;
/*     */ import javax.swing.event.PopupMenuListener;
/*     */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*     */ import org.tlauncher.tlauncher.managers.popup.menu.HotServerManager;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.util.ColorUtil;
/*     */ 
/*     */ public class PopupMenuView extends JPanel {
/*  28 */   private static final Color BACKGROUND_TITLE = new Color(60, 170, 232); private static final int MAX_SIZE_VERSION = 30;
/*  29 */   private static final Icon TLAUNCHER_USER_ICON = (Icon)ImageCache.getIcon("tlauncher-user.png");
/*  30 */   private static final Icon TLAUNCHER_USER_ICON_GRAY = (Icon)ImageCache.getIcon("tlauncher-user-gray.png");
/*  31 */   private static final Icon MOJANG_USER_ICON = (Icon)ImageCache.getIcon("mojang-user.png");
/*     */   private final JLabel title;
/*     */   private final JComboBox<VersionSyncInfo> box;
/*     */   private final JLabel versionLabel;
/*  35 */   private final DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
/*     */   private Point point;
/*     */   private final JLayeredPane defaultScene;
/*     */   private final PopupUpdaterButton start;
/*     */   private final PopupUpdaterButton copy;
/*     */   private final PopupUpdaterButton favorite;
/*     */   
/*     */   public PopupMenuView(JLayeredPane defaultScene) {
/*  43 */     setVisible(false);
/*  44 */     this.defaultScene = defaultScene;
/*  45 */     SpringLayout springLayout = new SpringLayout();
/*  46 */     setLayout(springLayout);
/*  47 */     setSize(290, 150);
/*  48 */     setBackground(Color.WHITE);
/*  49 */     this.versionLabel = new JLabel(Localizable.get().get("menu.version"));
/*  50 */     this.versionLabel.setForeground(ColorUtil.COLOR_77);
/*  51 */     this.box = (JComboBox<VersionSyncInfo>)new TlauncherCustomBox();
/*  52 */     this.box.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
/*     */           JLabel renderer = (JLabel)this.defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/*     */           
/*     */           if (value == null) {
/*     */             return null;
/*     */           }
/*     */           
/*     */           boolean skin = TLauncher.getInstance().getTLauncherManager().useTLauncherAccount(value.getAvailableVersion());
/*     */           if (skin && TLauncher.getInstance().getConfiguration().getBoolean("skin.status.checkbox.state")) {
/*     */             renderer.setIcon(TLAUNCHER_USER_ICON);
/*     */           } else if (skin) {
/*     */             renderer.setIcon(TLAUNCHER_USER_ICON_GRAY);
/*     */           } 
/*     */           renderer.setText(trimId(VersionCellRenderer.getLabelFor(value)));
/*     */           renderer.setAlignmentY(0.5F);
/*     */           renderer.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 0));
/*     */           renderer.setOpaque(true);
/*     */           if (isSelected) {
/*     */             renderer.setBackground(ColorUtil.COLOR_213);
/*     */           } else {
/*     */             renderer.setBackground(Color.white);
/*     */           } 
/*     */           renderer.setForeground(ColorUtil.COLOR_77);
/*     */           return renderer;
/*     */         });
/*  77 */     this.box.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(149, 149, 149)));
/*     */ 
/*     */     
/*  80 */     this.title = new JLabel();
/*  81 */     this.title.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
/*  82 */     springLayout.putConstraint("North", this.title, 0, "North", this);
/*  83 */     springLayout.putConstraint("West", this.title, 0, "West", this);
/*  84 */     springLayout.putConstraint("South", this.title, 30, "North", this);
/*  85 */     springLayout.putConstraint("East", this.title, 0, "East", this);
/*  86 */     add(this.title);
/*     */     
/*  88 */     this.title.setOpaque(true);
/*  89 */     this.title.setForeground(Color.WHITE);
/*  90 */     this.title.setBackground(BACKGROUND_TITLE);
/*  91 */     SwingUtil.setFontSize(this.title, 13.0F);
/*     */ 
/*     */     
/*  94 */     this.start = new PopupUpdaterButton(Color.white, Localizable.get().get("menu.start"));
/*  95 */     this.start.setBorder(BorderFactory.createEmptyBorder());
/*  96 */     springLayout.putConstraint("North", (Component)this.start, 30, "North", this);
/*  97 */     springLayout.putConstraint("West", (Component)this.start, 0, "West", this);
/*  98 */     springLayout.putConstraint("South", (Component)this.start, 60, "North", this);
/*  99 */     springLayout.putConstraint("East", (Component)this.start, 0, "East", this);
/* 100 */     add((Component)this.start);
/*     */     
/* 102 */     this.copy = new PopupUpdaterButton(Color.white, Localizable.get().get("menu.copy"));
/* 103 */     this.copy.setBorder(BorderFactory.createEmptyBorder());
/* 104 */     springLayout.putConstraint("North", (Component)this.copy, 60, "North", this);
/* 105 */     springLayout.putConstraint("West", (Component)this.copy, 0, "West", this);
/* 106 */     springLayout.putConstraint("South", (Component)this.copy, 90, "North", this);
/* 107 */     springLayout.putConstraint("East", (Component)this.copy, 0, "East", this);
/* 108 */     add((Component)this.copy);
/*     */     
/* 110 */     this.favorite = new PopupUpdaterButton(Color.white, Localizable.get().get("menu.favorite"));
/* 111 */     this.favorite.setBorder(BorderFactory.createEmptyBorder());
/* 112 */     springLayout.putConstraint("North", (Component)this.favorite, 90, "North", this);
/*     */     
/* 114 */     springLayout.putConstraint("West", (Component)this.favorite, 0, "West", this);
/* 115 */     springLayout.putConstraint("South", (Component)this.favorite, 120, "North", this);
/*     */     
/* 117 */     springLayout.putConstraint("East", (Component)this.favorite, 0, "East", this);
/* 118 */     add((Component)this.favorite);
/*     */ 
/*     */     
/* 121 */     springLayout.putConstraint("North", this.versionLabel, 122, "North", this);
/* 122 */     springLayout.putConstraint("West", this.versionLabel, 10, "West", this);
/* 123 */     springLayout.putConstraint("South", this.versionLabel, 146, "North", this);
/* 124 */     springLayout.putConstraint("East", this.versionLabel, 90, "West", this);
/* 125 */     add(this.versionLabel);
/*     */     
/* 127 */     springLayout.putConstraint("North", this.box, 122, "North", this);
/* 128 */     springLayout.putConstraint("West", this.box, 90, "West", this);
/* 129 */     springLayout.putConstraint("South", this.box, 146, "North", this);
/* 130 */     springLayout.putConstraint("East", this.box, -10, "East", this);
/* 131 */     add(this.box);
/*     */     
/* 133 */     HotServerManager manager = (HotServerManager)TLauncher.getInjector().getInstance(HotServerManager.class);
/* 134 */     this.favorite.addActionListener(e -> manager.addServerToList(true, (VersionSyncInfo)this.box.getSelectedItem()));
/*     */     
/* 136 */     this.copy.addActionListener(e -> manager.copyAddress());
/* 137 */     this.start.addActionListener(e -> {
/*     */           manager.launchGame((VersionSyncInfo)this.box.getSelectedItem());
/*     */           
/*     */           setVisible(false);
/*     */         });
/* 142 */     this.box.addPopupMenuListener(new PopupMenuListener()
/*     */         {
/*     */           public void popupMenuWillBecomeVisible(PopupMenuEvent e)
/*     */           {
/* 146 */             PopupMenuView.this.point = MouseInfo.getPointerInfo().getLocation();
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void popupMenuCanceled(PopupMenuEvent e) {}
/*     */         });
/* 159 */     this.box.addActionListener(e -> {
/*     */           try {
/*     */             (new Robot()).mouseMove(this.point.x, this.point.y);
/* 162 */           } catch (AWTException e1) {
/*     */             log(e1);
/*     */           } 
/*     */         });
/*     */ 
/*     */     
/* 168 */     MouseAdapter m = new MouseAdapter()
/*     */       {
/*     */         public void mouseExited(MouseEvent e) {
/* 171 */           if (PopupMenuView.this.isShowing() && !PopupMenuView.this.mouseIsOverDisplayPanel(PopupMenuView.this))
/* 172 */             PopupMenuView.this.setVisible(false); 
/*     */         }
/*     */       };
/* 175 */     addMouseListener(m);
/* 176 */     this.start.addMouseListener(m);
/* 177 */     this.copy.addMouseListener(m);
/* 178 */     this.favorite.addMouseListener(m);
/* 179 */     this.box.addMouseListener(m);
/*     */     
/* 181 */     this.box.addPopupMenuListener(new PopupMenuListener()
/*     */         {
/*     */           public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
/* 188 */             if (PopupMenuView.this.isShowing() && !PopupMenuView.this.mouseIsOverDisplayPanel(PopupMenuView.this)) {
/* 189 */               PopupMenuView.this.setVisible(false);
/*     */             }
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void popupMenuCanceled(PopupMenuEvent e) {}
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean mouseIsOverDisplayPanel(Component component) {
/* 201 */     if (this.box.isPopupVisible())
/* 202 */       return true; 
/* 203 */     Point c = component.getLocationOnScreen();
/* 204 */     Point m = MouseInfo.getPointerInfo().getLocation();
/* 205 */     return (m.x >= c.x && m.x < c.x + component.getWidth() - 1 && m.y >= c.y && m.y <= c.y + component.getHeight());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String trimId(String version) {
/* 215 */     if (version.length() > 30) {
/* 216 */       return version.substring(0, 30);
/*     */     }
/*     */     
/* 219 */     return version;
/*     */   }
/*     */   
/*     */   private void log(Object e) {
/* 223 */     U.log(new Object[] { "[PopupMenuView] ", e });
/*     */   }
/*     */   
/*     */   public void showSelectedModel(PopupMenuModel model) {
/* 227 */     SwingUtilities.invokeLater(() -> {
/*     */           String v = ViewlUtil.addSpaces(Localizable.get().get("menu.chooseVersion") + " " + model.getName(), model.getName());
/*     */           this.title.setText(v);
/*     */           this.start.setText(Localizable.get().get("menu.start"));
/*     */           this.start.setIcon(null);
/*     */           if (model.getInfo().isMojangAccount())
/*     */             this.start.setIcon(MOJANG_USER_ICON); 
/*     */           this.copy.setText(Localizable.get().get("menu.copy"));
/*     */           this.favorite.setText(Localizable.get().get("menu.favorite"));
/*     */           this.versionLabel.setText(Localizable.get().get("menu.version"));
/*     */           this.box.setModel(new DefaultComboBoxModel<>(model.getServers().toArray(new VersionSyncInfo[0])));
/*     */           Point p = MouseInfo.getPointerInfo().getLocation();
/*     */           if (p.getY() > 680.0D)
/*     */             p.y = 680; 
/*     */           p = new Point(p.x - 35, p.y - 30);
/*     */           SwingUtilities.convertPointFromScreen(p, this.defaultScene);
/*     */           setLocation(p);
/*     */           setVisible(true);
/*     */         });
/*     */   }
/*     */   
/*     */   private class PopupUpdaterButton
/*     */     extends UpdaterButton
/*     */   {
/*     */     PopupUpdaterButton(Color color, String value) {
/* 252 */       setText(value);
/* 253 */       setOpaque(true);
/* 254 */       setBackground(color);
/* 255 */       setForeground(ColorUtil.COLOR_77);
/* 256 */       addMouseListener(new MouseAdapter()
/*     */           {
/*     */             public void mouseEntered(MouseEvent e) {
/* 259 */               PopupMenuView.PopupUpdaterButton.this.setBackground(ColorUtil.COLOR_235);
/*     */             }
/*     */ 
/*     */             
/*     */             public void mouseExited(MouseEvent e) {
/* 264 */               PopupMenuView.PopupUpdaterButton.this.setBackground(Color.WHITE);
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
/* 272 */       Graphics2D g2d = (Graphics2D)g;
/* 273 */       g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/* 274 */       FontMetrics fm = g2d.getFontMetrics();
/* 275 */       Rectangle2D r = fm.getStringBounds(text, g2d);
/* 276 */       g.setFont(getFont());
/* 277 */       int x = 10;
/* 278 */       int y = (getHeight() - (int)r.getHeight()) / 2 + fm.getAscent();
/* 279 */       g2d.drawString(text, x, y);
/* 280 */       g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
/* 281 */       Icon icon = getIcon();
/* 282 */       if (Objects.nonNull(icon))
/* 283 */         icon.paintIcon(c, g, (int)(r.getWidth() + 15.0D), (
/* 284 */             getHeight() - icon.getIconHeight()) / 2); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\menu\PopupMenuView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */