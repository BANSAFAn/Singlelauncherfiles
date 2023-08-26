/*     */ package org.tlauncher.tlauncher.ui.versions;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.List;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.ListModel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ import javax.swing.plaf.ScrollBarUI;
/*     */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*     */ import org.tlauncher.tlauncher.managers.VersionManager;
/*     */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*     */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.server.BackPanel;
/*     */ import org.tlauncher.tlauncher.ui.swing.ImageButton;
/*     */ import org.tlauncher.tlauncher.ui.swing.ScrollPane;
/*     */ import org.tlauncher.tlauncher.ui.swing.SimpleListModel;
/*     */ import org.tlauncher.tlauncher.ui.swing.VersionCellRenderer;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.BorderPanel;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.tlauncher.ui.swing.scroll.VersionScrollBarUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VersionList
/*     */   extends CenterPanel
/*     */   implements VersionHandlerListener
/*     */ {
/*     */   private static final long serialVersionUID = -7192156096621636270L;
/*     */   final VersionHandler handler;
/*     */   public final SimpleListModel<VersionSyncInfo> model;
/*     */   public final JList<VersionSyncInfo> list;
/*     */   VersionDownloadButton download;
/*     */   VersionRemoveButton remove;
/*     */   public final ImageButton refresh;
/*     */   
/*     */   VersionList(VersionHandler h) {
/*  45 */     super(squareInsets);
/*     */     
/*  47 */     this.handler = h;
/*     */     
/*  49 */     BorderPanel panel = new BorderPanel(0, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  58 */     BackPanel backPanel = new BackPanel("version.manager.list", new MouseAdapter() { public void mousePressed(MouseEvent e) { if (SwingUtilities.isLeftMouseButton(e)) VersionList.this.handler.exitEditor();  } }, ImageCache.getIcon("back-arrow.png"));
/*  59 */     panel.setNorth((Component)backPanel);
/*     */     
/*  61 */     this.model = new SimpleListModel();
/*  62 */     this.list = new JList<>((ListModel<VersionSyncInfo>)this.model);
/*  63 */     this.list.setCellRenderer((ListCellRenderer<? super VersionSyncInfo>)new VersionListCellRenderer(this));
/*  64 */     this.list.setSelectionMode(2);
/*  65 */     this.list.addListSelectionListener(new ListSelectionListener()
/*     */         {
/*     */           public void valueChanged(ListSelectionEvent e) {
/*  68 */             VersionList.this.handler.onVersionSelected(VersionList.this.list.getSelectedValuesList());
/*     */           }
/*     */         });
/*  71 */     ScrollPane pane = new ScrollPane(this.list, ScrollPane.ScrollBarPolicy.AS_NEEDED, ScrollPane.ScrollBarPolicy.NEVER);
/*  72 */     pane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
/*  73 */     pane.getVerticalScrollBar().setUI((ScrollBarUI)new VersionScrollBarUI());
/*  74 */     panel.setCenter((Component)pane);
/*     */     
/*  76 */     ExtendedPanel buttons = new ExtendedPanel(new GridLayout(0, 3));
/*  77 */     buttons.setInsets(0, -1, 0, -1);
/*     */     
/*  79 */     this.refresh = (ImageButton)new VersionRefreshButton(this);
/*  80 */     buttons.add((Component)this.refresh);
/*     */     
/*  82 */     this.download = new VersionDownloadButton(this);
/*  83 */     buttons.add((Component)this.download);
/*     */     
/*  85 */     this.remove = new VersionRemoveButton(this);
/*  86 */     buttons.add((Component)this.remove);
/*     */     
/*  88 */     panel.setSouth((Component)buttons);
/*  89 */     add((Component)panel);
/*     */     
/*  91 */     this.handler.addListener(this);
/*     */     
/*  93 */     setSize(500, 400);
/*     */   }
/*     */   
/*     */   void select(List<VersionSyncInfo> list) {
/*  97 */     if (list == null) {
/*     */       return;
/*     */     }
/* 100 */     int size = list.size();
/* 101 */     int[] indexes = new int[list.size()];
/*     */     
/* 103 */     for (int i = 0; i < size; i++) {
/* 104 */       indexes[i] = this.model.indexOf(list.get(i));
/*     */     }
/* 106 */     this.list.setSelectedIndices(indexes);
/*     */   }
/*     */   
/*     */   void deselect() {
/* 110 */     this.list.clearSelection();
/*     */   }
/*     */   
/*     */   void refreshFrom(VersionManager manager) {
/* 114 */     setRefresh(false);
/*     */     
/* 116 */     List<VersionSyncInfo> list = manager.getVersions(null, false);
/* 117 */     this.model.addAll(list);
/*     */   }
/*     */   
/*     */   void setRefresh(boolean refresh) {
/* 121 */     this.model.clear();
/*     */     
/* 123 */     if (refresh) {
/* 124 */       this.model.add(VersionCellRenderer.LOADING);
/*     */     }
/*     */   }
/*     */   
/*     */   public void block(Object reason) {
/* 129 */     Blocker.blockComponents(reason, new Component[] { this.list, (Component)this.refresh, (Component)this.remove });
/*     */   }
/*     */ 
/*     */   
/*     */   public void unblock(Object reason) {
/* 134 */     Blocker.unblockComponents(reason, new Component[] { this.list, (Component)this.refresh, (Component)this.remove });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVersionRefreshing(VersionManager vm) {
/* 139 */     setRefresh(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVersionRefreshed(VersionManager vm) {
/* 144 */     refreshFrom(vm);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onVersionSelected(List<VersionSyncInfo> version) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onVersionDeselected() {}
/*     */ 
/*     */   
/*     */   public void onVersionDownload(List<VersionSyncInfo> list) {
/* 157 */     select(list);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\versions\VersionList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */