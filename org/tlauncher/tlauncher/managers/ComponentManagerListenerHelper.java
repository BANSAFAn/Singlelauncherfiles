/*    */ package org.tlauncher.tlauncher.managers;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.tlauncher.tlauncher.component.LauncherComponent;
/*    */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*    */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*    */ 
/*    */ public class ComponentManagerListenerHelper
/*    */   extends LauncherComponent
/*    */   implements Blockable, VersionManagerListener, TlauncherManagerListener
/*    */ {
/*    */   private final List<ComponentManagerListener> listeners;
/*    */   
/*    */   public ComponentManagerListenerHelper(ComponentManager manager) throws Exception {
/* 17 */     super(manager);
/*    */     
/* 19 */     this
/* 20 */       .listeners = Collections.synchronizedList(new ArrayList<>());
/*    */     
/* 22 */     ((VersionManager)manager.<VersionManager>getComponent(VersionManager.class)).addListener(this);
/*    */   }
/*    */   
/*    */   public void addListener(ComponentManagerListener listener) {
/* 26 */     if (listener == null) {
/* 27 */       throw new NullPointerException();
/*    */     }
/* 29 */     this.listeners.add(listener);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onVersionsRefreshing(VersionManager manager) {
/* 34 */     Blocker.block(this, manager);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onVersionsRefreshingFailed(VersionManager manager) {
/* 39 */     Blocker.unblock(this, manager);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onVersionsRefreshed(VersionManager manager) {
/* 44 */     Blocker.unblock(this, manager);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void block(Object reason) {
/* 51 */     for (ComponentManagerListener listener : this.listeners) {
/* 52 */       listener.onComponentsRefreshing(this.manager);
/*    */     }
/*    */   }
/*    */   
/*    */   public void unblock(Object reason) {
/* 57 */     for (ComponentManagerListener listener : this.listeners) {
/* 58 */       listener.onComponentsRefreshed(this.manager);
/*    */     }
/*    */   }
/*    */   
/*    */   public void onTlauncherUpdating(TLauncherManager manager) {
/* 63 */     Blocker.block(this, manager);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onTlauncherUpdated(TLauncherManager manager) {
/* 68 */     Blocker.unblock(this, manager);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\managers\ComponentManagerListenerHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */