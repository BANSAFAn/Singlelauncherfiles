/*     */ package org.tlauncher.tlauncher.ui;
/*     */ 
/*     */ import java.awt.Image;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.listener.UpdateUIListener;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.swing.ImagePanel;
/*     */ import org.tlauncher.tlauncher.updater.client.Update;
/*     */ import org.tlauncher.tlauncher.updater.client.Updater;
/*     */ import org.tlauncher.tlauncher.updater.client.UpdaterListener;
/*     */ 
/*     */ 
/*     */ public class SideNotifier
/*     */   extends ImagePanel
/*     */   implements UpdaterListener
/*     */ {
/*     */   private static final String LANG_PREFIX = "notifier.";
/*     */   private NotifierStatus status;
/*     */   private Update update;
/*     */   
/*     */   SideNotifier() {
/*  24 */     super((Image)null, 1.0F, 0.75F, false);
/*     */     
/*  26 */     TLauncher.getInstance().getUpdater().addListener(this);
/*     */   }
/*     */   
/*     */   public NotifierStatus getStatus() {
/*  30 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setStatus(NotifierStatus status) {
/*  34 */     if (status == null) {
/*  35 */       throw new NullPointerException();
/*     */     }
/*  37 */     this.status = status;
/*     */     
/*  39 */     setImage(status.getImage());
/*  40 */     if (status == NotifierStatus.NONE) { hide(); }
/*  41 */     else { show(); }
/*     */   
/*     */   }
/*     */   
/*     */   protected boolean onClick() {
/*  46 */     boolean result = processClick();
/*     */     
/*  48 */     if (result) {
/*  49 */       hide();
/*     */     }
/*  51 */     return result; } private boolean processClick() { String prefix; String title;
/*     */     String question;
/*     */     boolean ask;
/*     */     UpdateUIListener listener;
/*  55 */     if (!super.onClick()) {
/*  56 */       return false;
/*     */     }
/*  58 */     switch (this.status) {
/*     */       case FAILED:
/*  60 */         Alert.showWarning(
/*  61 */             Localizable.get("notifier.failed.title"), 
/*  62 */             Localizable.get("notifier.failed"));
/*     */ 
/*     */       
/*     */       case FOUND:
/*  66 */         if (this.update == null) {
/*  67 */           throw new IllegalStateException("Update is NULL!");
/*     */         }
/*     */         
/*  70 */         prefix = "notifier." + this.status.toString() + ".";
/*  71 */         title = prefix + "title";
/*  72 */         question = prefix + "question";
/*     */ 
/*     */         
/*  75 */         ask = Alert.showQuestion(
/*  76 */             Localizable.get(title), 
/*  77 */             Localizable.get(question, new Object[] { Double.valueOf(this.update.getVersion()) }), this.update
/*  78 */             .getDescription());
/*     */ 
/*     */         
/*  81 */         if (!ask) {
/*  82 */           return false;
/*     */         }
/*  84 */         listener = new UpdateUIListener(this.update);
/*  85 */         listener.push();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case NONE:
/*  94 */         return true;
/*     */     } 
/*     */     throw new IllegalStateException("Unknown status: " + this.status); }
/*     */   
/*     */   public void onUpdaterRequesting(Updater u) {
/*  99 */     setFoundUpdate((Update)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdaterErrored(Updater.SearchFailed failed) {
/* 104 */     setStatus(NotifierStatus.FAILED);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdaterSucceeded(Updater.SearchSucceeded succeeded) {
/* 109 */     Update update = succeeded.getResponse();
/* 110 */     setFoundUpdate(update.isApplicable() ? update : null);
/*     */   }
/*     */   
/*     */   private void setFoundUpdate(Update upd) {
/* 114 */     this.update = upd;
/* 115 */     setStatus((upd == null) ? NotifierStatus.NONE : NotifierStatus.FOUND);
/*     */   }
/*     */   
/*     */   public enum NotifierStatus {
/* 119 */     FAILED("warning.png"), FOUND("down32.png"), NONE;
/*     */     
/*     */     private final Image image;
/*     */     
/*     */     NotifierStatus(String imagePath) {
/* 124 */       this.image = (imagePath == null) ? null : ImageCache.getImage(imagePath);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Image getImage() {
/* 132 */       return this.image;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 137 */       return super.toString().toLowerCase();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\SideNotifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */