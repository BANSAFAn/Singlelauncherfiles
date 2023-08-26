/*     */ package org.tlauncher.tlauncher.ui.login;
/*     */ 
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.SwingUtilities;
/*     */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import net.minecraft.launcher.versions.Version;
/*     */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*     */ import org.tlauncher.tlauncher.listeners.auth.LoginProcessListener;
/*     */ import org.tlauncher.tlauncher.managers.VersionManager;
/*     */ import org.tlauncher.tlauncher.managers.VersionManagerListener;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*     */ import org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
/*     */ import org.tlauncher.tlauncher.ui.swing.SimpleComboBoxModel;
/*     */ import org.tlauncher.tlauncher.ui.swing.VersionCellRenderer;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedComboBox;
/*     */ 
/*     */ public class VersionComboBox
/*     */   extends ExtendedComboBox<VersionSyncInfo>
/*     */   implements Blockable, VersionManagerListener, LocalizableComponent, LoginProcessListener, GameEntityListener {
/*     */   private static final long serialVersionUID = -9122074452728842733L;
/*  30 */   private static final VersionSyncInfo LOADING = VersionCellRenderer.LOADING;
/*  31 */   private static final VersionSyncInfo EMPTY = VersionCellRenderer.EMPTY;
/*     */   
/*     */   private final VersionManager manager;
/*     */   
/*     */   private final LoginForm loginForm;
/*     */   
/*     */   private final SimpleComboBoxModel<VersionSyncInfo> model;
/*     */   private String selectedVersion;
/*     */   
/*     */   VersionComboBox(LoginForm lf) {
/*  41 */     super((ListCellRenderer)new VersionCellRenderer()
/*     */         {
/*     */           public boolean getShowTLauncherVersions() {
/*  44 */             return false;
/*     */           }
/*     */         });
/*  47 */     this.model = getSimpleModel();
/*  48 */     this.loginForm = lf;
/*     */     
/*  50 */     this.manager = TLauncher.getInstance().getVersionManager();
/*  51 */     this.manager.addListener(this);
/*     */     
/*  53 */     addItemListener(e -> {
/*     */           this.loginForm.play.updateState();
/*     */           
/*     */           VersionSyncInfo selected = getVersion();
/*     */           if (selected != null) {
/*     */             this.selectedVersion = selected.getID();
/*     */           }
/*     */         });
/*  61 */     this.selectedVersion = lf.global.get("login.version.game");
/*     */   }
/*     */   
/*     */   public VersionSyncInfo getVersion() {
/*  65 */     VersionSyncInfo selected = (VersionSyncInfo)getSelectedItem();
/*  66 */     return (selected == null || selected.equals(LOADING) || selected.equals(EMPTY)) ? null : selected;
/*     */   }
/*     */ 
/*     */   
/*     */   public void validatePreGameLaunch() throws LoginException {
/*  71 */     VersionSyncInfo selected = getVersion();
/*     */     
/*  73 */     if (selected == null) {
/*  74 */       throw new LoginWaitException("Version list is empty, refreshing", () -> {
/*     */             this.manager.refresh();
/*     */             if (getVersion() == null) {
/*     */               Alert.showLocError("versions.notfound");
/*     */             }
/*     */             throw new LoginException("Giving user a second chance to choose correct version...");
/*     */           });
/*     */     }
/*  82 */     TLauncher.getInstance().getConfiguration().setForcefully("login.version.game", selected.getID(), false);
/*  83 */     if (!selected.hasRemote() || !selected.isInstalled() || selected.isUpToDate()) {
/*     */       return;
/*     */     }
/*  86 */     if (!Alert.showLocQuestion("versions.found-update")) {
/*     */       try {
/*  88 */         CompleteVersion complete = this.manager.getLocalList().getCompleteVersion(selected.getLocal());
/*  89 */         complete.setUpdatedTime(selected.getLatestVersion().getUpdatedTime());
/*  90 */         this.manager.getLocalList().saveVersion(complete);
/*  91 */       } catch (IOException e) {
/*  92 */         Alert.showLocError("versions.found-update.error");
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*  97 */     this.loginForm.versionPanel.forceupdate.setSelected(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateLocale() {
/* 102 */     updateList(this.manager.getVersions(), (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVersionsRefreshing(VersionManager vm) {
/* 107 */     updateList((List<VersionSyncInfo>)null, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVersionsRefreshingFailed(VersionManager vm) {
/* 112 */     updateList(this.manager.getVersions(), (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVersionsRefreshed(VersionManager vm) {
/* 117 */     updateList(this.manager.getVersions(), (String)null);
/*     */   }
/*     */   
/*     */   void updateList(List<VersionSyncInfo> list, String select) {
/* 121 */     SwingUtilities.invokeLater(() -> {
/*     */           String select1 = select;
/*     */           if (select == null && this.selectedVersion != null) {
/*     */             select1 = this.selectedVersion;
/*     */           }
/*     */           removeAllItems();
/*     */           if (list == null) {
/*     */             addItem(LOADING);
/*     */             return;
/*     */           } 
/*     */           if (list.isEmpty()) {
/*     */             addItem(EMPTY);
/*     */           } else {
/*     */             this.model.addElements(list);
/*     */             for (VersionSyncInfo version : list) {
/*     */               if (select1 != null && version.getID().equals(select1)) {
/*     */                 setSelectedItem(version);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void block(Object reason) {
/* 147 */     setEnabled(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void unblock(Object reason) {
/* 152 */     setEnabled(true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void activationStarted(GameEntityDTO e) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void activation(GameEntityDTO e) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void activationError(GameEntityDTO e, Throwable t) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void processingStarted(GameEntityDTO e, VersionDTO version) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void installEntity(GameEntityDTO e, GameType type) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void installEntity(CompleteVersion e) {
/* 178 */     VersionSyncInfo versionSyncInfo = new VersionSyncInfo((Version)e, null);
/* 179 */     this.model.insertElementAt(versionSyncInfo, 0);
/* 180 */     this.model.setSelectedItem(versionSyncInfo);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeEntity(GameEntityDTO e) {
/* 185 */     SwingUtilities.invokeLater(this::repaint);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeCompleteVersion(CompleteVersion e) {
/* 190 */     for (int i = 0; i < this.model.getSize(); i++) {
/* 191 */       if (((VersionSyncInfo)this.model.getElementAt(i)).getLocal() != null && ((VersionSyncInfo)this.model
/* 192 */         .getElementAt(i)).getLocal().getID().equals(e.getID())) {
/* 193 */         this.model.removeElementAt(i);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void populateStatus(GameEntityDTO status, GameType type, boolean state) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateVersion(CompleteVersion old, CompleteVersion newVersion) {
/* 210 */     for (int i = 0; i < this.model.getSize(); i++) {
/* 211 */       VersionSyncInfo versionSyncInfo = (VersionSyncInfo)this.model.getElementAt(i);
/* 212 */       if (versionSyncInfo.getLocal() != null && versionSyncInfo
/* 213 */         .getLocal().getID().equals(old.getID())) {
/* 214 */         versionSyncInfo.setLocal((Version)newVersion);
/* 215 */         repaint();
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\VersionComboBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */