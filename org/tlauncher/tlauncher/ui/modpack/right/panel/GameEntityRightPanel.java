/*     */ package org.tlauncher.tlauncher.ui.modpack.right.panel;
/*     */ 
/*     */ import java.awt.CardLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseWheelEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import javax.swing.AbstractCellEditor;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.table.TableCellEditor;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*     */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.listener.BlockClickListener;
/*     */ import org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener;
/*     */ import org.tlauncher.tlauncher.ui.listener.mods.UpdateGameListener;
/*     */ import org.tlauncher.tlauncher.ui.loc.modpack.GameRightButton;
/*     */ import org.tlauncher.tlauncher.ui.scenes.CompleteSubEntityScene;
/*     */ import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
/*     */ import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
/*     */ import org.tlauncher.util.ColorUtil;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GameEntityRightPanel
/*     */   extends JTable
/*     */   implements GameEntityListener
/*     */ {
/*     */   private final ModpackComboBox localmodpacks;
/*     */   private final GameType type;
/*     */   private static final int HEIGHT_RIGHT_ELEMENT = 159;
/*  53 */   private List<Long> changeableElements = new ArrayList<>();
/*  54 */   public Integer getNextPageIndex() { return this.nextPageIndex; } public void setNextPageIndex(Integer nextPageIndex) {
/*  55 */     this.nextPageIndex = nextPageIndex;
/*  56 */   } private boolean nextPage; private Integer nextPageIndex = Integer.valueOf(0); private boolean processingRequest; public boolean isNextPage() {
/*  57 */     return this.nextPage; } public void setNextPage(boolean nextPage) {
/*  58 */     this.nextPage = nextPage;
/*     */   }
/*  60 */   public boolean isProcessingRequest() { return this.processingRequest; } public void setProcessingRequest(boolean processingRequest) {
/*  61 */     this.processingRequest = processingRequest;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  66 */   int currentSelectedIndex = -1;
/*     */ 
/*     */   
/*     */   public GameEntityRightPanel(ModpackComboBox localmodpacks, GameType type) {
/*  70 */     this.localmodpacks = localmodpacks;
/*  71 */     this.type = type;
/*  72 */     setBackground(ColorUtil.COLOR_233);
/*     */     
/*  74 */     setRowHeight(159);
/*     */     
/*  76 */     setColumnSelectionAllowed(true);
/*  77 */     setRowSelectionAllowed(true);
/*     */     
/*  79 */     setShowGrid(false);
/*  80 */     setIntercellSpacing(new Dimension(0, 0));
/*     */     
/*  82 */     setDefaultEditor(GameRightElement.class, new RightRenderer());
/*  83 */     setDefaultRenderer(GameRightElement.class, new RightRenderer());
/*  84 */     setModel(new RightTableModel<>());
/*  85 */     MouseAdapter mouse = new MouseAdapter()
/*     */       {
/*     */         public void mouseMoved(MouseEvent e)
/*     */         {
/*  89 */           check(e);
/*     */         }
/*     */ 
/*     */         
/*     */         public void mouseWheelMoved(MouseWheelEvent e) {
/*  94 */           GameEntityRightPanel.this.getParent().dispatchEvent(e);
/*  95 */           check(e);
/*     */         }
/*     */         
/*     */         private void check(MouseEvent e) {
/*  99 */           int i = GameEntityRightPanel.this.rowAtPoint(e.getPoint());
/* 100 */           if (i != -1 && i != GameEntityRightPanel.this.currentSelectedIndex) {
/* 101 */             GameEntityRightPanel.this.editCellAt(i, 0);
/* 102 */             GameEntityRightPanel.this.currentSelectedIndex = i;
/*     */           } 
/*     */         }
/*     */       };
/*     */     
/* 107 */     addMouseWheelListener(mouse);
/* 108 */     addMouseMotionListener(mouse);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addElements(List<GameEntityDTO> list, boolean clean) {
/* 113 */     RightTableModel<GameEntityDTO> rightTableModel = (RightTableModel<GameEntityDTO>)getModel();
/* 114 */     rightTableModel.addElements(list, clean);
/*     */   }
/*     */ 
/*     */   
/*     */   public void processingStarted(GameEntityDTO e, VersionDTO version) {
/* 119 */     this.changeableElements.add(e.getId());
/* 120 */     Iterator<Component> it = Arrays.<Component>asList(getComponents()).iterator();
/* 121 */     while (it.hasNext()) {
/* 122 */       Component c = it.next();
/* 123 */       if (c instanceof GameRightElement) {
/* 124 */         GameRightElement el = (GameRightElement)c;
/* 125 */         if (el.getEntity().getId().equals(e.getId())) {
/* 126 */           el.processingInstall();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void installEntity(GameEntityDTO e, GameType type) {
/* 133 */     this.changeableElements.remove(e.getId());
/* 134 */     repaint();
/* 135 */     updateRow();
/*     */   }
/*     */ 
/*     */   
/*     */   public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {
/* 140 */     this.changeableElements.remove(e.getId());
/* 141 */     GameRightElement elem = find(e);
/* 142 */     if (elem != null) {
/* 143 */       elem.modpackActButton.reset();
/*     */     }
/*     */   }
/*     */   
/*     */   private GameRightElement find(GameEntityDTO e) {
/* 148 */     Iterator<Component> it = Arrays.<Component>asList(getComponents()).iterator();
/* 149 */     while (it.hasNext()) {
/* 150 */       Component c = it.next();
/* 151 */       if (c instanceof GameRightElement) {
/* 152 */         GameRightElement el = (GameRightElement)c;
/* 153 */         if (el.getEntity().getId().equals(e.getId()))
/* 154 */           return el; 
/*     */       } 
/*     */     } 
/* 157 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void populateStatus(GameEntityDTO e, GameType type, boolean state) {
/* 162 */     GameRightElement elem = find(e);
/* 163 */     if (elem != null) {
/* 164 */       elem.getStatusStarButton().setStatus(state);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeEntity(GameEntityDTO e) {
/* 170 */     this.changeableElements.remove(e.getId());
/* 171 */     repaint();
/* 172 */     updateRow();
/*     */   }
/*     */ 
/*     */   
/*     */   void updateRow() {
/* 177 */     int row = getEditingRow();
/* 178 */     if (row >= 0)
/* 179 */       SwingUtilities.invokeLater(() -> editCellAt(row, 0)); 
/*     */   }
/*     */   
/*     */   public class GameRightElement
/*     */     extends CompleteSubEntityScene.DescriptionGamePanel
/*     */     implements UpdateGameListener
/*     */   {
/*     */     private GameEntityDTO entity;
/*     */     private int row;
/*     */     private GameRightButton modpackActButton;
/*     */     
/*     */     public GameEntityDTO getEntity() {
/* 191 */       return this.entity;
/*     */     }
/*     */     
/*     */     public GameRightElement(final GameEntityDTO entity, final GameType type, int row) {
/* 195 */       super(entity, type);
/* 196 */       this.row = row;
/* 197 */       this.description.setVisible(true);
/* 198 */       this.entity = entity;
/* 199 */       this.modpackActButton = new GameRightButton(entity, type, GameEntityRightPanel.this.localmodpacks)
/*     */         {
/*     */           
/*     */           public void updateRow()
/*     */           {
/* 204 */             GameEntityRightPanel.this.repaint();
/*     */           }
/*     */         };
/* 207 */       JLabel shadow = new JLabel()
/*     */         {
/*     */           protected void paintComponent(Graphics g) {
/* 210 */             Rectangle rec = getBounds();
/* 211 */             SwingUtil.paintShadowLine(rec, g, getParent().getBackground().getRed() - 14, 14);
/*     */           }
/*     */         };
/* 214 */       shadow.setBackground(Color.green);
/* 215 */       this.descriptionLayout.putConstraint("West", shadow, 0, "West", (Component)this);
/* 216 */       this.descriptionLayout.putConstraint("East", shadow, 0, "East", (Component)this);
/* 217 */       this.descriptionLayout.putConstraint("North", shadow, 0, "North", (Component)this);
/* 218 */       this.descriptionLayout.putConstraint("South", shadow, 14, "North", (Component)this);
/* 219 */       add(shadow);
/*     */       
/* 221 */       this.modpackActButton.initButton();
/* 222 */       this.imagePanel.addMoapckActButton(this.modpackActButton);
/* 223 */       this.descriptionLayout.putConstraint("West", (Component)this.imagePanel, 27, "West", (Component)this);
/* 224 */       this.descriptionLayout.putConstraint("East", (Component)this.imagePanel, 138, "West", (Component)this);
/*     */       class MouseBackgroundListener
/*     */         extends MouseAdapter
/*     */         implements BlockClickListener
/*     */       {
/*     */         public void mousePressed(MouseEvent e) {
/* 230 */           if (e.getButton() == 1) {
/* 231 */             ((ModpackManager)TLauncher.getInjector().getInstance(ModpackManager.class)).showFullGameEntity(entity, type);
/*     */           }
/*     */         }
/*     */       };
/*     */       
/* 236 */       MouseBackgroundListener adapter = new MouseBackgroundListener();
/* 237 */       addMouseListener(adapter);
/* 238 */       for (Component comp : getComponents())
/* 239 */         comp.removeMouseListener(adapter); 
/* 240 */       this.description.addMouseListener(adapter);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void processingActivation() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void processingInstall() {
/* 252 */       this.modpackActButton.setTypeButton("PROCESSING");
/* 253 */       ((RightTableModel)GameEntityRightPanel.this.getModel()).fireTableCellUpdated(this.row, 0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void initGameEntity() {}
/*     */   }
/*     */ 
/*     */   
/*     */   public void filterRightPanel(GameType current) {
/* 263 */     Container container = getParent();
/* 264 */     CardLayout cardLayout = (CardLayout)container.getLayout();
/*     */     
/* 266 */     if (getModel().getRowCount() > 0) {
/* 267 */       cardLayout.show(container, ModpackScene.NOT_EMPTY);
/*     */     } else {
/* 269 */       cardLayout.show(container, ModpackScene.EMPTY);
/*     */     } 
/* 271 */     if (isEditing())
/* 272 */       this.cellEditor.cancelCellEditing(); 
/* 273 */     this.currentSelectedIndex = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class RightRenderer
/*     */     extends AbstractCellEditor
/*     */     implements TableCellRenderer, TableCellEditor
/*     */   {
/*     */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/* 283 */       if (Objects.isNull(value))
/* 284 */         return null; 
/* 285 */       GameEntityRightPanel.GameRightElement el = new GameEntityRightPanel.GameRightElement((GameEntityDTO)value, GameEntityRightPanel.this.type, row);
/* 286 */       if (GameEntityRightPanel.this.changeableElements.contains(((GameEntityDTO)value).getId())) {
/* 287 */         el.modpackActButton.setTypeButton("PROCESSING");
/*     */       }
/* 289 */       el.setBackground(ColorUtil.COLOR_247);
/* 290 */       return (Component)el;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
/* 296 */       if (Objects.isNull(value))
/* 297 */         return null; 
/* 298 */       GameEntityRightPanel.GameRightElement el = new GameEntityRightPanel.GameRightElement((GameEntityDTO)value, GameEntityRightPanel.this.type, row);
/* 299 */       if (GameEntityRightPanel.this.changeableElements.contains(((GameEntityDTO)value).getId())) {
/* 300 */         el.modpackActButton.setTypeButton("PROCESSING");
/*     */       }
/* 302 */       el.setBackground(Color.WHITE);
/* 303 */       return (Component)el;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getCellEditorValue() {
/* 308 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void installEntity(CompleteVersion e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activationStarted(GameEntityDTO e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activation(GameEntityDTO e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activationError(GameEntityDTO e, Throwable t) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateVersion(CompleteVersion v, CompleteVersion newVersion) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeCompleteVersion(CompleteVersion e) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getColumnClass(int column) {
/* 344 */     return GameRightElement.class;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\right\panel\GameEntityRightPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */