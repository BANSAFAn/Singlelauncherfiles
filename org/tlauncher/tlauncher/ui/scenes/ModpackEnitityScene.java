/*     */ package org.tlauncher.tlauncher.ui.scenes;
/*     */ 
/*     */ import by.gdev.util.DesktopUtil;
/*     */ import java.awt.CardLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.MainPane;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.loc.modpack.ModpackActButton;
/*     */ import org.tlauncher.tlauncher.ui.loc.modpack.ModpackTableInstallButton;
/*     */ import org.tlauncher.tlauncher.ui.modpack.GroupPanel;
/*     */ import org.tlauncher.tlauncher.ui.swing.GameRadioButton;
/*     */ import org.tlauncher.tlauncher.ui.swing.ScrollPane;
/*     */ import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
/*     */ import org.tlauncher.util.ColorUtil;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.TlauncherUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.swing.FontTL;
/*     */ 
/*     */ public class ModpackEnitityScene extends CompleteSubEntityScene {
/*     */   private static final String EMPTY = "EMPTY";
/*     */   
/*     */   public ModpackEnitityScene(MainPane main) {
/*  45 */     super(main);
/*     */   }
/*     */   private static final String ROWS = "ROWS";
/*     */   public void showModpackEntity(final GameEntityDTO completeGameEntity) {
/*  49 */     showFullGameEntity(completeGameEntity, GameType.MODPACK);
/*  50 */     GameRadioButton modReview = new GameRadioButton("modpack.complete.review.mod");
/*  51 */     modReview.setActionCommand(GameType.MOD.toString());
/*  52 */     GameRadioButton resourceReview = new GameRadioButton("modpack.complete.review.resource");
/*  53 */     resourceReview.setActionCommand(GameType.RESOURCEPACK.toString());
/*  54 */     GameRadioButton mapReview = new GameRadioButton("modpack.complete.review.map");
/*  55 */     mapReview.setActionCommand(GameType.MAP.toString());
/*  56 */     GameRadioButton shaderpackReview = new GameRadioButton("modpack.button.shaderpack");
/*  57 */     shaderpackReview.setActionCommand(GameType.SHADERPACK.toString());
/*     */     
/*  59 */     SwingUtil.changeFontFamily((JComponent)mapReview, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
/*  60 */     SwingUtil.changeFontFamily((JComponent)shaderpackReview, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
/*     */     
/*  62 */     SwingUtil.changeFontFamily((JComponent)modReview, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
/*  63 */     SwingUtil.changeFontFamily((JComponent)resourceReview, FontTL.ROBOTO_REGULAR, 14, ColorUtil.COLOR_25);
/*     */     
/*  65 */     GroupPanel centerButtons = this.fullGameEntity.getCenterButtons();
/*  66 */     centerButtons.addInGroup((AbstractButton)modReview, 3);
/*  67 */     centerButtons.addInGroup((AbstractButton)resourceReview, 4);
/*  68 */     centerButtons.addInGroup((AbstractButton)mapReview, 5);
/*  69 */     centerButtons.addInGroup((AbstractButton)shaderpackReview, 6);
/*     */     
/*  71 */     final JPanel centerView = this.fullGameEntity.getCenterView();
/*  72 */     GameType.getSubEntities().forEach(t -> {
/*     */           final GameEntityTable table = new GameEntityTable(completeGameEntity, t);
/*     */           ScrollPane scrollPane = ModpackScene.createScrollWrapper(table);
/*     */           final JPanel panel = new JPanel();
/*     */           CardLayout c = new CardLayout();
/*     */           panel.setLayout(c);
/*     */           LocalizableLabel emptyLabel = new LocalizableLabel("modpack.table.empty." + t.toLowerCase());
/*     */           emptyLabel.setHorizontalAlignment(0);
/*     */           emptyLabel.setAlignmentX(0.0F);
/*     */           SwingUtil.changeFontFamily((JComponent)emptyLabel, FontTL.ROBOTO_BOLD, 18, ColorUtil.COLOR_16);
/*     */           panel.add((Component)scrollPane, "ROWS");
/*     */           panel.add((Component)emptyLabel, "EMPTY");
/*     */           emptyLabel.setBounds(0, 160, MainPane.SIZE.width, 22);
/*     */           panel.setBounds(0, 0, MainPane.SIZE.width, 321);
/*     */           centerView.add(panel, t.toLowerCase());
/*     */           panel.addComponentListener(new ComponentAdapter()
/*     */               {
/*     */                 public void componentShown(ComponentEvent e)
/*     */                 {
/*  91 */                   if (table.getModel().getRowCount() > 0) {
/*     */                     return;
/*     */                   }
/*  94 */                   CompletableFuture.runAsync(() -> DesktopUtil.uncheckCall(()))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                     
/* 106 */                     .exceptionally(ex -> {
/*     */                         U.log(new Object[] { ex });
/*     */                         return null;
/*     */                       });
/*     */                 }
/*     */               });
/*     */         });
/* 113 */     centerView.revalidate();
/* 114 */     centerView.repaint();
/*     */     
/* 116 */     modReview.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/* 120 */             ((CardLayout)centerView.getLayout()).show(centerView, GameType.MOD.toLowerCase());
/*     */           }
/*     */         });
/*     */     
/* 124 */     resourceReview.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 127 */             ((CardLayout)centerView.getLayout()).show(centerView, GameType.RESOURCEPACK.toLowerCase());
/*     */           }
/*     */         });
/*     */     
/* 131 */     mapReview.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 134 */             ((CardLayout)centerView.getLayout()).show(centerView, GameType.MAP.toLowerCase());
/*     */           }
/*     */         });
/* 137 */     shaderpackReview.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 140 */             ((CardLayout)centerView.getLayout()).show(centerView, GameType.SHADERPACK.toLowerCase());
/*     */           }
/*     */         });
/*     */     
/* 144 */     ((CardLayout)centerView.getLayout()).show(centerView, "REVIEW");
/*     */   }
/*     */   
/*     */   private class RemoteEntityModel extends BaseSubtypeModel<CompleteSubEntityScene.BaseModelElement> {
/* 148 */     private SimpleDateFormat format = new SimpleDateFormat("dd MMMM YYYY", Localizable.get().getSelected());
/*     */     private GameType type;
/*     */     
/*     */     RemoteEntityModel(GameType type) {
/* 152 */       this.type = type;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRowCount() {
/* 157 */       return this.list.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getColumnCount() {
/* 162 */       return 6;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getValueAt(int rowIndex, int columnIndex) {
/* 167 */       switch (columnIndex) {
/*     */         case 0:
/* 169 */           return this.format.format(((CompleteSubEntityScene.BaseModelElement)this.list.get(rowIndex)).getEntity().getVersion().getUpdateDate());
/*     */         case 1:
/* 171 */           return ((CompleteSubEntityScene.BaseModelElement)this.list.get(rowIndex)).getEntity().getName();
/*     */         case 2:
/* 173 */           return ((CompleteSubEntityScene.BaseModelElement)this.list.get(rowIndex)).getEntity().getAuthor();
/*     */         case 3:
/* 175 */           return ((CompleteSubEntityScene.BaseModelElement)this.list.get(rowIndex)).getEntity().getVersion().getName();
/*     */         case 4:
/* 177 */           return ((CompleteSubEntityScene.BaseModelElement)this.list.get(rowIndex)).getEntity().getVersion().getType();
/*     */         case 5:
/* 179 */           ((CompleteSubEntityScene.BaseModelElement)this.list.get(rowIndex)).getModpackActButton().initButton();
/* 180 */           return ((CompleteSubEntityScene.BaseModelElement)this.list.get(rowIndex)).getModpackActButton();
/*     */       } 
/* 182 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getColumnName(int column) {
/* 187 */       String line = "";
/* 188 */       switch (column) {
/*     */         case 0:
/* 190 */           line = Localizable.get("version.manager.editor.field.time");
/* 191 */           return line.substring(0, line.length() - 1);
/*     */         case 1:
/* 193 */           return Localizable.get("modpack.table.pack.element.name");
/*     */         case 2:
/* 195 */           return Localizable.get("modpack.table.pack.element.author");
/*     */         case 3:
/* 197 */           return Localizable.get("version.release");
/*     */         case 4:
/* 199 */           line = Localizable.get("version.manager.editor.field.type");
/* 200 */           return line.substring(0, line.length() - 1);
/*     */         case 5:
/* 202 */           return Localizable.get("modpack.table.pack.element.operation");
/*     */       } 
/* 204 */       return "";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public GameEntityDTO getRowObject(int rowIndex) {
/* 210 */       return ((CompleteSubEntityScene.BaseModelElement)this.list.get(rowIndex)).getEntity();
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getColumnClass(int columnIndex) {
/* 215 */       if (columnIndex == 5) {
/* 216 */         return CompleteSubEntityScene.BaseModelElement.class;
/*     */       }
/* 218 */       return super.getColumnClass(columnIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isCellEditable(int rowIndex, int columnIndex) {
/* 223 */       return (columnIndex == 5);
/*     */     }
/*     */     
/*     */     public void addElements(List<GameEntityDTO> list) {
/* 227 */       ModpackComboBox modpackComboBox = (TLauncher.getInstance().getFrame()).mp.modpackScene.localmodpacks;
/*     */       
/* 229 */       for (GameEntityDTO entity : list) {
/* 230 */         this.list.add(new CompleteSubEntityScene.BaseModelElement((ModpackActButton)new ModpackTableInstallButton(entity, this.type, modpackComboBox), entity));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class GameEntityTable
/*     */     extends CompleteSubEntityScene.ModpackTable
/*     */   {
/*     */     GameEntityTable(final GameEntityDTO parent, final GameType type) {
/* 239 */       super(new ModpackEnitityScene.RemoteEntityModel(ModpackEnitityScene.this, type));
/*     */       
/* 241 */       getSelectionModel().addListSelectionListener(new ListSelectionListener()
/*     */           {
/*     */             public void valueChanged(ListSelectionEvent e) {
/* 244 */               if (ModpackEnitityScene.GameEntityTable.this.getSelectedRow() == -1)
/*     */                 return; 
/* 246 */               int column = ModpackEnitityScene.GameEntityTable.this.getSelectedColumn();
/* 247 */               if (column != 5) {
/*     */ 
/*     */                 
/* 250 */                 GameEntityDTO gameEntity = ((ModpackEnitityScene.BaseSubtypeModel)ModpackEnitityScene.GameEntityTable.this.getModel()).getRowObject(ModpackEnitityScene.GameEntityTable.this.getSelectedRow());
/* 251 */                 U.log(new Object[] { "test12" });
/* 252 */                 CompletableFuture.runAsync(() -> DesktopUtil.uncheckCall(()))
/*     */ 
/*     */                   
/* 255 */                   .exceptionally(ex -> {
/*     */                       Alert.showError("", Localizable.get("modpack.remote.not.found", new Object[] { Localizable.get("modpack.try.later") }), null);
/*     */                       
/*     */                       TlauncherUtil.sendLog(ex);
/*     */                       
/*     */                       return null;
/*     */                     });
/*     */               } 
/*     */               
/* 264 */               ModpackEnitityScene.GameEntityTable.this.getSelectionModel().clearSelection();
/*     */             }
/*     */           });
/* 267 */       ModpackEnitityScene.this.manager.addGameListener(type, (GameEntityListener)getModel());
/*     */     }
/*     */     
/*     */     public void addElements(List<GameEntityDTO> list) {
/* 271 */       ModpackEnitityScene.RemoteEntityModel rm = (ModpackEnitityScene.RemoteEntityModel)getModel();
/* 272 */       rm.addElements(list);
/*     */     } }
/*     */   
/*     */   private abstract class BaseSubtypeModel<T extends CompleteSubEntityScene.BaseModelElement> extends CompleteSubEntityScene.GameEntityTableModel {
/*     */     private BaseSubtypeModel() {
/* 277 */       this.list = new ArrayList<>();
/*     */     }
/*     */     protected List<T> list;
/*     */     
/*     */     public T find(GameEntityDTO entity) {
/* 282 */       for (CompleteSubEntityScene.BaseModelElement baseModelElement : this.list) {
/* 283 */         if (entity.getId().equals(baseModelElement.getEntity().getId())) {
/* 284 */           return (T)baseModelElement;
/*     */         }
/*     */       } 
/* 287 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void processingStarted(GameEntityDTO e, VersionDTO version) {
/* 292 */       CompleteSubEntityScene.BaseModelElement baseModelElement = (CompleteSubEntityScene.BaseModelElement)find(e);
/* 293 */       if (baseModelElement != null) {
/* 294 */         baseModelElement.getModpackActButton().setTypeButton("PROCESSING");
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {
/* 300 */       CompleteSubEntityScene.BaseModelElement baseModelElement = (CompleteSubEntityScene.BaseModelElement)find(e);
/* 301 */       if (baseModelElement != null) {
/* 302 */         baseModelElement.getModpackActButton().reset();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void installEntity(GameEntityDTO e, GameType type) {
/* 308 */       CompleteSubEntityScene.BaseModelElement baseModelElement = (CompleteSubEntityScene.BaseModelElement)find(e);
/* 309 */       if (baseModelElement != null) {
/* 310 */         baseModelElement.getModpackActButton().setTypeButton("REMOVE");
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void removeEntity(GameEntityDTO e) {
/* 316 */       CompleteSubEntityScene.BaseModelElement baseModelElement = (CompleteSubEntityScene.BaseModelElement)find(e);
/* 317 */       if (baseModelElement != null)
/* 318 */         baseModelElement.getModpackActButton().setTypeButton("INSTALL"); 
/*     */     }
/*     */     
/*     */     public abstract GameEntityDTO getRowObject(int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\scenes\ModpackEnitityScene.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */