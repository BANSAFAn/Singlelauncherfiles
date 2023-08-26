/*     */ package org.tlauncher.tlauncher.ui.scenes;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.MouseInputAdapter;
/*     */ import org.tlauncher.tlauncher.entity.hot.AdditionalHotServer;
/*     */ import org.tlauncher.tlauncher.entity.hot.HotBanner;
/*     */ import org.tlauncher.tlauncher.managers.popup.menu.HotServerManager;
/*     */ import org.tlauncher.tlauncher.ui.BackgroundPanel;
/*     */ import org.tlauncher.tlauncher.ui.MainPane;
/*     */ import org.tlauncher.tlauncher.ui.button.RoundImageButton;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.menu.PopupMenuView;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.async.AsyncThread;
/*     */ import org.tlauncher.util.swing.FontTL;
/*     */ 
/*     */ public class AdditionalHostServerScene extends PseudoScene {
/*  39 */   private final Integer rowHeight = Integer.valueOf(48); private static final long serialVersionUID = 8975208936840346013L; private final PopupMenuView popupMenuView;
/*     */   private final HotServerManager hotServerManager;
/*     */   private boolean loadOnce;
/*     */   private JTable table;
/*     */   private JButton back;
/*     */   private JLabel serverTitle;
/*     */   
/*     */   public AdditionalHostServerScene(MainPane mp) {
/*  47 */     super(mp);
/*  48 */     this.hotServerManager = (HotServerManager)TLauncher.getInjector().getInstance(HotServerManager.class);
/*  49 */     BackgroundPanel backgroundPanel = new BackgroundPanel("hot/hot-servers-background.png");
/*  50 */     backgroundPanel.setSize(MainPane.SIZE.width, MainPane.SIZE.height);
/*  51 */     backgroundPanel.setLocation(0, 0);
/*  52 */     add((Component)backgroundPanel);
/*  53 */     this.popupMenuView = new PopupMenuView((JLayeredPane)this);
/*     */     
/*  55 */     this.back = (JButton)new RoundImageButton(ImageCache.getImage("hot/back.png"), ImageCache.getImage("hot/back-active.png"));
/*  56 */     this.back.setBorder(BorderFactory.createEmptyBorder());
/*  57 */     this.serverTitle = (JLabel)new LocalizableLabel("server.page.title");
/*     */     
/*  59 */     this.table = new JTable();
/*     */ 
/*     */     
/*  62 */     this.table.setRowHeight(this.rowHeight.intValue());
/*  63 */     this.table.setBorder(BorderFactory.createEmptyBorder());
/*  64 */     this.table.setOpaque(false);
/*  65 */     this.table.setShowGrid(false);
/*  66 */     this.table.getColumnModel().setColumnSelectionAllowed(true);
/*  67 */     this.table.setIntercellSpacing(new Dimension(0, 0));
/*     */     
/*  69 */     SwingUtil.changeFontFamily(this.serverTitle, FontTL.ROBOTO_MEDIUM, 20);
/*  70 */     this.serverTitle.setForeground(Color.WHITE);
/*     */     
/*  72 */     add(this.back);
/*  73 */     add(this.serverTitle);
/*  74 */     add(this.table);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentShown(ComponentEvent e) {
/*  82 */             AdditionalHostServerScene.this.prepare();
/*     */           }
/*     */         });
/*  85 */     this.back.addActionListener(e -> mp.setScene(mp.defaultScene));
/*  86 */     RollOverListener lst = new RollOverListener(this.table);
/*  87 */     this.table.addMouseMotionListener(lst);
/*  88 */     this.table.addMouseListener(lst);
/*  89 */     this.popupMenuView.addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentHidden(ComponentEvent e) {
/*  92 */             AdditionalHostServerScene.this.table.clearSelection();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void prepare() {
/*  98 */     prepareRemoteServerData(this.hotServerManager.getAdditionalHotServers().getList());
/*  99 */     if (this.loadOnce)
/*     */       return; 
/* 101 */     this.loadOnce = true;
/* 102 */     int rows = this.hotServerManager.getAdditionalHotServers().getList().size();
/* 103 */     this.table.setSize(672, rows * this.rowHeight.intValue());
/* 104 */     this.back.setSize(new Dimension(187, 30));
/* 105 */     this.serverTitle.setSize(new Dimension(200, 24));
/*     */     
/* 107 */     this.table.setLocation(187, (MainPane.SIZE.height - rows * this.rowHeight.intValue()) / 2);
/*     */     
/* 109 */     this.back.setLocation(200, (this.table.getLocation()).y - 26);
/* 110 */     this.serverTitle.setLocation(630, (this.table.getLocation()).y - 24);
/*     */     
/* 112 */     this.table.setModel(new TableModel(this.hotServerManager.getAdditionalHotServers().getList()));
/* 113 */     this.table.getColumnModel().getColumn(0).setPreferredWidth(632);
/* 114 */     this.table.getColumnModel().getColumn(1).setPreferredWidth(40);
/* 115 */     this.table.getColumnModel().getColumn(0).setCellRenderer(new AdditionalServerRenderer());
/* 116 */     this.table.getColumnModel().getColumn(1).setCellRenderer(new Column1Renderer());
/*     */     
/* 118 */     AsyncThread.execute(() -> {
/*     */           try {
/*     */             HotBanner up = this.hotServerManager.getAdditionalHotServers().getUpBanner();
/*     */ 
/*     */ 
/*     */             
/*     */             if (Objects.nonNull(up)) {
/*     */               RoundImageButton upButton = new RoundImageButton(up.getImage(), up.getMouseOnImage());
/*     */ 
/*     */ 
/*     */               
/*     */               SwingUtilities.invokeLater(());
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/*     */             HotBanner down = this.hotServerManager.getAdditionalHotServers().getDownBanner();
/*     */ 
/*     */             
/*     */             if (Objects.nonNull(down)) {
/*     */               RoundImageButton downButton = new RoundImageButton(down.getImage(), down.getMouseOnImage());
/*     */ 
/*     */               
/*     */               SwingUtilities.invokeLater(());
/*     */             } 
/* 143 */           } catch (RuntimeException e) {
/*     */             U.log(new Object[] { e });
/*     */           } 
/*     */           SwingUtilities.invokeLater(());
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void prepareRemoteServerData(List<AdditionalHotServer> list) {
/* 152 */     for (int i = 0; i < list.size(); i++) {
/* 153 */       int finalI = i;
/* 154 */       AsyncThread.execute(() -> {
/*     */             AdditionalHotServer s = list.get(finalI);
/*     */             try {
/*     */               if (Objects.nonNull(s.getUpdated()) && s.getUpdated().after(new Date()))
/*     */                 return; 
/*     */               this.hotServerManager.fillServer(s);
/*     */               ((TableModel)this.table.getModel()).fireTableCellUpdated(finalI, 0);
/* 161 */             } catch (Throwable e) {
/*     */               U.log(new Object[] { s, e });
/*     */             } 
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   public PopupMenuView getPopupMenuView() {
/* 169 */     return this.popupMenuView;
/*     */   }
/*     */   
/*     */   private class TableModel extends AbstractTableModel {
/*     */     private List<AdditionalHotServer> list;
/*     */     
/*     */     TableModel(List<AdditionalHotServer> list) {
/* 176 */       this.list = list;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRowCount() {
/* 181 */       return this.list.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getColumnCount() {
/* 186 */       return 2;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getValueAt(int row, int column) {
/* 191 */       if (column == 0) {
/* 192 */         return this.list.get(row);
/*     */       }
/* 194 */       return ((AdditionalHotServer)this.list.get(row)).getTMonitoringLink();
/*     */     }
/*     */   }
/*     */   
/*     */   private class AdditionalServerRenderer extends DefaultTableCellRenderer { private AdditionalServerRenderer() {}
/*     */     
/*     */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/*     */       BackgroundPanel backgroundPanel;
/* 202 */       AdditionalHotServer s = (AdditionalHotServer)value;
/* 203 */       if (Objects.isNull(s)) {
/* 204 */         return null;
/*     */       }
/* 206 */       int gap = 0;
/* 207 */       if (isSelected) {
/* 208 */         backgroundPanel = new BackgroundPanel(String.format("hot/%s-hot-servers-active.png", new Object[] { s.getImageNumber() }));
/* 209 */         gap = 1;
/*     */       } else {
/* 211 */         backgroundPanel = new BackgroundPanel(String.format("hot/%s-hot-servers.png", new Object[] { s.getImageNumber() }));
/* 212 */       }  backgroundPanel.setBorder(BorderFactory.createEmptyBorder());
/* 213 */       JLabel shortDesc = new JLabel(s.getShortDescription());
/* 214 */       JLabel desc = new JLabel(s.getAddDescription());
/* 215 */       LocalizableLabel localizableLabel1 = new LocalizableLabel("server.page.online");
/* 216 */       LocalizableLabel localizableLabel2 = new LocalizableLabel("version.release");
/* 217 */       JLabel online = new JLabel((s.getOnline().intValue() == -1) ? "???" : (s.getOnline() + "/" + s.getMax()));
/* 218 */       JLabel startVersion = new JLabel(s.getVersionDescription());
/* 219 */       SpringLayout spring = new SpringLayout();
/* 220 */       backgroundPanel.setLayout(spring);
/*     */       
/* 222 */       if (!isSelected) {
/* 223 */         JLabel image; if (Objects.nonNull(s.getImage()))
/* 224 */         { image = new JLabel(new ImageIcon(s.getImage().getScaledInstance(44, 44, 4))); }
/* 225 */         else { image = new JLabel((Icon)ImageCache.getIcon("hot/tl-icon.png")); }
/* 226 */          spring.putConstraint("West", image, 1, "West", (Component)backgroundPanel);
/* 227 */         spring.putConstraint("East", image, 45, "West", (Component)backgroundPanel);
/* 228 */         spring.putConstraint("North", image, 1, "North", (Component)backgroundPanel);
/* 229 */         spring.putConstraint("South", image, -1, "South", (Component)backgroundPanel);
/* 230 */         backgroundPanel.add(image);
/*     */       } 
/* 232 */       spring.putConstraint("West", shortDesc, -564, "East", (Component)backgroundPanel);
/* 233 */       spring.putConstraint("East", shortDesc, -144, "East", (Component)backgroundPanel);
/* 234 */       spring.putConstraint("North", shortDesc, gap, "North", (Component)backgroundPanel);
/* 235 */       spring.putConstraint("South", shortDesc, gap + 24, "North", (Component)backgroundPanel);
/* 236 */       backgroundPanel.add(shortDesc);
/*     */       
/* 238 */       spring.putConstraint("West", desc, -564, "East", (Component)backgroundPanel);
/* 239 */       spring.putConstraint("East", desc, -144, "East", (Component)backgroundPanel);
/* 240 */       spring.putConstraint("North", desc, gap + 26, "North", (Component)backgroundPanel);
/* 241 */       spring.putConstraint("South", desc, -gap - 2, "South", (Component)backgroundPanel);
/* 242 */       backgroundPanel.add(desc);
/*     */       
/* 244 */       spring.putConstraint("West", (Component)localizableLabel1, -142, "East", (Component)backgroundPanel);
/* 245 */       spring.putConstraint("East", (Component)localizableLabel1, -62, "East", (Component)backgroundPanel);
/* 246 */       spring.putConstraint("North", (Component)localizableLabel1, gap, "North", (Component)backgroundPanel);
/* 247 */       spring.putConstraint("South", (Component)localizableLabel1, gap + 24, "North", (Component)backgroundPanel);
/* 248 */       backgroundPanel.add((Component)localizableLabel1);
/*     */       
/* 250 */       spring.putConstraint("West", online, -142, "East", (Component)backgroundPanel);
/* 251 */       spring.putConstraint("East", online, -62, "East", (Component)backgroundPanel);
/* 252 */       spring.putConstraint("North", online, gap + 26, "North", (Component)backgroundPanel);
/* 253 */       spring.putConstraint("South", online, gap + 46, "North", (Component)backgroundPanel);
/* 254 */       backgroundPanel.add(online);
/*     */       
/* 256 */       spring.putConstraint("West", (Component)localizableLabel2, -60, "East", (Component)backgroundPanel);
/* 257 */       spring.putConstraint("East", (Component)localizableLabel2, 0, "East", (Component)backgroundPanel);
/* 258 */       spring.putConstraint("North", (Component)localizableLabel2, gap, "North", (Component)backgroundPanel);
/* 259 */       spring.putConstraint("South", (Component)localizableLabel2, gap + 24, "North", (Component)backgroundPanel);
/* 260 */       backgroundPanel.add((Component)localizableLabel2);
/*     */       
/* 262 */       spring.putConstraint("West", startVersion, -60, "East", (Component)backgroundPanel);
/* 263 */       spring.putConstraint("East", startVersion, 0, "East", (Component)backgroundPanel);
/* 264 */       spring.putConstraint("North", startVersion, gap + 26, "North", (Component)backgroundPanel);
/* 265 */       spring.putConstraint("South", startVersion, gap + 46, "North", (Component)backgroundPanel);
/* 266 */       backgroundPanel.add(startVersion);
/*     */ 
/*     */       
/* 269 */       localizableLabel1.setHorizontalAlignment(0);
/* 270 */       localizableLabel2.setHorizontalAlignment(0);
/* 271 */       online.setHorizontalAlignment(0);
/* 272 */       startVersion.setHorizontalAlignment(0);
/*     */       
/* 274 */       SwingUtil.changeFontFamily(shortDesc, FontTL.ROBOTO_BOLD, 16);
/* 275 */       SwingUtil.changeFontFamily(desc, FontTL.ROBOTO_REGULAR, 12);
/* 276 */       SwingUtil.changeFontFamily((JComponent)localizableLabel1, FontTL.ROBOTO_BOLD, 12);
/* 277 */       SwingUtil.changeFontFamily((JComponent)localizableLabel2, FontTL.ROBOTO_BOLD, 12);
/* 278 */       SwingUtil.changeFontFamily(online, FontTL.ROBOTO_MEDIUM, 12);
/* 279 */       SwingUtil.changeFontFamily(startVersion, FontTL.ROBOTO_MEDIUM, 12);
/*     */ 
/*     */       
/* 282 */       shortDesc.setForeground(Color.WHITE);
/* 283 */       desc.setForeground(Color.WHITE);
/* 284 */       localizableLabel1.setForeground(Color.WHITE);
/* 285 */       localizableLabel2.setForeground(Color.WHITE);
/* 286 */       online.setForeground(Color.WHITE);
/* 287 */       startVersion.setForeground(Color.WHITE);
/* 288 */       if (isSelected)
/* 289 */         return (Component)backgroundPanel; 
/* 290 */       JPanel jPanel = new JPanel(new BorderLayout(0, 0));
/* 291 */       jPanel.setBorder(BorderFactory.createEmptyBorder(1, 13, 1, 0));
/* 292 */       jPanel.add((Component)backgroundPanel, "Center");
/* 293 */       jPanel.setOpaque(false);
/* 294 */       return jPanel;
/*     */     } }
/*     */   
/*     */   private class Column1Renderer implements TableCellRenderer { private Column1Renderer() {}
/*     */     
/*     */     private Component get(Object value, boolean active) {
/*     */       JLabel label;
/* 301 */       if (Objects.isNull(value)) {
/* 302 */         return new JLabel();
/*     */       }
/* 304 */       if (active)
/* 305 */       { label = new JLabel((Icon)ImageCache.getIcon("hot/hot-server-site-active.png")); }
/* 306 */       else { label = new JLabel((Icon)ImageCache.getIcon("hot/hot-server-site.png")); }
/* 307 */        return label;
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/* 312 */       return get(value, isSelected);
/*     */     } }
/*     */ 
/*     */   
/*     */   private class RollOverListener extends MouseInputAdapter {
/*     */     JTable table;
/*     */     
/*     */     RollOverListener(JTable table) {
/* 320 */       this.table = table;
/*     */     }
/*     */     
/*     */     public void mouseExited(MouseEvent e) {
/* 324 */       if (!AdditionalHostServerScene.this.popupMenuView.isVisible()) {
/* 325 */         this.table.clearSelection();
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseMoved(MouseEvent e) {
/* 330 */       int row = this.table.rowAtPoint(e.getPoint());
/* 331 */       int column = this.table.columnAtPoint(e.getPoint());
/* 332 */       if (this.table.getSelectedRow() != row || this.table.getSelectedColumn() != column) {
/* 333 */         this.table.changeSelection(row, column, false, false);
/*     */       }
/* 335 */       if (row == -1 && this.table.getSelectedRow() > -1) {
/* 336 */         this.table.clearSelection();
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseClicked(MouseEvent e) {
/* 341 */       int row = this.table.rowAtPoint(e.getPoint());
/* 342 */       int col = this.table.columnAtPoint(e.getPoint());
/* 343 */       if (row >= 0 && col >= 0) {
/* 344 */         Object value = this.table.getModel().getValueAt(row, col);
/* 345 */         if (col == 1) {
/* 346 */           if (Objects.nonNull(value)) {
/* 347 */             OS.openLink((String)value);
/*     */           }
/* 349 */         } else if (e.getPoint().getX() < 490.0D) {
/* 350 */           AdditionalHotServer s = (AdditionalHotServer)value;
/* 351 */           AdditionalHostServerScene.this.hotServerManager.processingEvent(s.getServerId());
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\scenes\AdditionalHostServerScene.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */