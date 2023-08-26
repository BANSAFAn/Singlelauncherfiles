/*     */ package org.tlauncher.tlauncher.ui.versions;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import net.minecraft.launcher.updater.LocalVersionList;
/*     */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*     */ import org.tlauncher.tlauncher.managers.VersionManager;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*     */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*     */ import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VersionRemoveButton
/*     */   extends ImageUdaterButton
/*     */   implements VersionHandlerListener, Blockable
/*     */ {
/*     */   private static final long serialVersionUID = 427368162418879141L;
/*     */   private static final String ILLEGAL_SELECTION_BLOCK = "illegal-selection";
/*     */   private static final String PREFIX = "version.manager.delete.";
/*     */   private static final String ERROR = "version.manager.delete.error.";
/*     */   private static final String ERROR_TITLE = "version.manager.delete.error.title";
/*     */   private static final String MENU = "version.manager.delete.menu.";
/*     */   private final VersionHandler handler;
/*     */   private final JPopupMenu menu;
/*     */   private final LocalizableMenuItem onlyJar;
/*     */   private final LocalizableMenuItem withLibraries;
/*     */   private boolean libraries;
/*     */   
/*     */   VersionRemoveButton(VersionList list) {
/*  43 */     super(Color.WHITE, "delete-version.png");
/*     */     
/*  45 */     this.handler = list.handler;
/*  46 */     this.handler.addListener(this);
/*     */     
/*  48 */     this.menu = new JPopupMenu();
/*     */     
/*  50 */     this.onlyJar = new LocalizableMenuItem("version.manager.delete.menu.jar");
/*  51 */     this.onlyJar.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/*  54 */             VersionRemoveButton.this.onChosen(false);
/*     */           }
/*     */         });
/*  57 */     this.menu.add((JMenuItem)this.onlyJar);
/*     */     
/*  59 */     this.withLibraries = new LocalizableMenuItem("version.manager.delete.menu.libraries");
/*  60 */     this.withLibraries.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/*  63 */             VersionRemoveButton.this.onChosen(true);
/*     */           }
/*     */         });
/*  66 */     this.menu.add((JMenuItem)this.withLibraries);
/*     */     
/*  68 */     addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/*  71 */             VersionRemoveButton.this.onChosen(false);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   void onPressed() {
/*  77 */     this.menu.show((Component)this, 0, getHeight());
/*     */   }
/*     */   
/*     */   void onChosen(boolean removeLibraries) {
/*  81 */     this.libraries = removeLibraries;
/*  82 */     this.handler.thread.deleteThread.iterate();
/*     */   }
/*     */   
/*     */   void delete() {
/*  86 */     if (this.handler.selected != null) {
/*  87 */       LocalVersionList localList = this.handler.vm.getLocalList();
/*  88 */       List<Throwable> errors = new ArrayList<>();
/*     */       
/*  90 */       for (VersionSyncInfo version : this.handler.selected) {
/*  91 */         if (version.isInstalled())
/*     */           try {
/*  93 */             localList.deleteVersion(version.getID(), this.libraries);
/*  94 */           } catch (Throwable e) {
/*  95 */             errors.add(e);
/*     */           }  
/*     */       } 
/*  98 */       if (!errors.isEmpty()) {
/*  99 */         String title = Localizable.get("version.manager.delete.error.title");
/* 100 */         String message = Localizable.get("version.manager.delete.error." + ((errors.size() == 1) ? "single" : "multiply"), new Object[] { errors });
/*     */         
/* 102 */         Alert.showError(title, message);
/*     */       } 
/*     */     } 
/*     */     
/* 106 */     this.handler.refresh();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onVersionRefreshing(VersionManager vm) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onVersionRefreshed(VersionManager vm) {}
/*     */ 
/*     */   
/*     */   public void onVersionSelected(List<VersionSyncInfo> versions) {
/* 119 */     boolean onlyRemote = true;
/*     */     
/* 121 */     for (VersionSyncInfo version : versions) {
/* 122 */       if (version.isInstalled()) {
/* 123 */         onlyRemote = false;
/*     */         break;
/*     */       } 
/*     */     } 
/* 127 */     Blocker.setBlocked(this, "illegal-selection", onlyRemote);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVersionDeselected() {
/* 132 */     Blocker.block(this, "illegal-selection");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onVersionDownload(List<VersionSyncInfo> list) {}
/*     */ 
/*     */   
/*     */   public void block(Object reason) {
/* 141 */     setEnabled(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void unblock(Object reason) {
/* 146 */     setEnabled(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\versions\VersionRemoveButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */