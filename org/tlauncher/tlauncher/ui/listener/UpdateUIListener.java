/*    */ package org.tlauncher.tlauncher.ui.listener;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*    */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*    */ import org.tlauncher.tlauncher.updater.client.Update;
/*    */ import org.tlauncher.tlauncher.updater.client.UpdateListener;
/*    */ import org.tlauncher.util.OS;
/*    */ 
/*    */ public class UpdateUIListener
/*    */   implements UpdateListener {
/*    */   private final TLauncher t;
/*    */   private final Update u;
/*    */   
/*    */   public UpdateUIListener(Update u) {
/* 19 */     if (u == null) {
/* 20 */       throw new NullPointerException();
/*    */     }
/* 22 */     this.t = TLauncher.getInstance();
/* 23 */     this.u = u;
/*    */     
/* 25 */     u.addListener(this);
/*    */   }
/*    */   
/*    */   public void push() {
/* 29 */     block();
/* 30 */     this.u.download(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdateError(Update u, Throwable e) {
/* 35 */     if (Alert.showLocQuestion("updater.error.title", "updater.download-error", e))
/* 36 */       openUpdateLink(u.getlastDownloadedLink()); 
/* 37 */     unblock();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onUpdateDownloading(Update u) {}
/*    */ 
/*    */   
/*    */   public void onUpdateDownloadError(Update u, Throwable e) {
/* 46 */     onUpdateError(u, e);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdateReady(Update u) {
/* 51 */     onUpdateReady(u, false);
/*    */   }
/*    */   
/*    */   private static void onUpdateReady(Update u, boolean showChangeLog) {
/* 55 */     Alert.showLocWarning("updater.downloaded", showChangeLog ? u.getDescription() : null);
/* 56 */     u.apply();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onUpdateApplying(Update u) {}
/*    */ 
/*    */   
/*    */   public void onUpdateApplyError(Update u, Throwable e) {
/* 65 */     if (Alert.showLocQuestion("updater.save-error", e)) {
/* 66 */       openUpdateLink(u.getlastDownloadedLink());
/*    */     }
/* 68 */     unblock();
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean openUpdateLink(String link) {
/*    */     try {
/* 74 */       if (OS.openLink(new URI(link), false))
/* 75 */         return true; 
/* 76 */     } catch (URISyntaxException uRISyntaxException) {}
/*    */ 
/*    */     
/* 79 */     Alert.showLocError("updater.found.cannotopen", link);
/* 80 */     return false;
/*    */   }
/*    */   
/*    */   private void block() {
/* 84 */     Blocker.block((Blockable)(this.t.getFrame()).mp, "updater");
/*    */   }
/*    */   
/*    */   private void unblock() {
/* 88 */     Blocker.unblock((Blockable)(this.t.getFrame()).mp, "updater");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\listener\UpdateUIListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */