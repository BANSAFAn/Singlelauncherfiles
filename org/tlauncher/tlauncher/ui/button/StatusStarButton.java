/*    */ package org.tlauncher.tlauncher.ui.button;
/*    */ 
/*    */ import by.gdev.util.DesktopUtil;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.awt.event.MouseListener;
/*    */ import java.io.IOException;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.SwingUtilities;
/*    */ import org.apache.http.client.ClientProtocolException;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.tlauncher.exceptions.RequiredTLAccountException;
/*    */ import org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException;
/*    */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StatusStarButton
/*    */   extends JLabel
/*    */ {
/*    */   private static final long serialVersionUID = 8192841246854925487L;
/*    */   private volatile boolean status;
/*    */   private ModpackManager manager;
/* 34 */   private static final Object OBJECT = new Object();
/*    */   private GameEntityDTO entityDTO;
/*    */   
/*    */   public StatusStarButton(final GameEntityDTO entityDTO, final GameType type) {
/* 38 */     this.entityDTO = entityDTO;
/* 39 */     this.manager = (ModpackManager)TLauncher.getInjector().getInstance(ModpackManager.class);
/* 40 */     updateStatus();
/* 41 */     addMouseListener(new MouseAdapter()
/*    */         {
/*    */           public void mousePressed(MouseEvent e) {
/* 44 */             if (SwingUtilities.isLeftMouseButton(e)) {
/* 45 */               CompletableFuture.runAsync(() -> DesktopUtil.uncheckCall(()), StatusStarButton.this
/*    */ 
/*    */                   
/* 48 */                   .manager.getModpackExecutorService()).exceptionally(t -> {
/*    */                     SwingUtilities.invokeLater(());
/*    */                     return null;
/*    */                   });
/*    */             }
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setStatus(boolean status) {
/* 62 */     this.status = status;
/* 63 */     setIcon((Icon)ImageCache.getIcon("star-" + status + ".png"));
/*    */   }
/*    */   
/*    */   public void updateStatus() {
/* 67 */     setStatus(this.manager.getFavoriteGameEntitiesByAccount().contains(this.entityDTO.getId()));
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void addMouseListener(MouseListener l) {
/* 72 */     if (l instanceof org.tlauncher.tlauncher.ui.listener.BlockClickListener) {
/*    */       return;
/*    */     }
/* 75 */     super.addMouseListener(l);
/*    */   }
/*    */ 
/*    */   
/*    */   private void syncSending(GameEntityDTO entityDTO, GameType type) throws ClientProtocolException, IOException, RequiredTLAccountException {
/* 80 */     synchronized (OBJECT) {
/*    */       
/*    */       try {
/* 83 */         if (this.status) {
/* 84 */           this.manager.deleteFavoriteGameEntities(entityDTO, type);
/*    */         } else {
/* 86 */           this.manager.addFavoriteGameEntities(entityDTO, type);
/*    */         } 
/* 88 */         setStatus(!this.status);
/* 89 */       } catch (RequiredTLAccountException e) {
/* 90 */         Alert.showLocError("modpack.right.panel.required.tl.account.title", 
/* 91 */             Localizable.get("modpack.right.panel.required.tl.account", new Object[] {
/* 92 */                 Localizable.get("loginform.button.settings.account")
/*    */               }), null);
/* 94 */       } catch (SelectedAnyOneTLAccountException e) {
/* 95 */         Alert.showLocError("modpack.right.panel.required.tl.account.title", "modpack.right.panel.select.account.tl", null);
/*    */       }
/* 97 */       catch (Exception e) {
/* 98 */         e.printStackTrace();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\button\StatusStarButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */