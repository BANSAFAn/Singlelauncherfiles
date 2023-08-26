/*    */ package org.tlauncher.tlauncher.ui.browser;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.MainPane;
/*    */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*    */ import org.tlauncher.tlauncher.ui.block.Unblockable;
/*    */ import org.tlauncher.tlauncher.ui.swing.ResizeableComponent;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.BorderPanel;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BrowserHolder
/*    */   extends BorderPanel
/*    */   implements ResizeableComponent, Unblockable
/*    */ {
/*    */   private static BrowserHolder browserHolder;
/*    */   MainPane pane;
/* 19 */   final BrowserFallback fallback = new BrowserFallback(this);
/*    */   private BrowserHolder() {
/* 21 */     BrowserPanel browser_ = null;
/*    */     
/*    */     try {
/* 24 */       browser_ = new BrowserPanel(this);
/* 25 */     } catch (Throwable e) {
/* 26 */       log(new Object[] { "Cannot load BrowserPanel. Will show BrowserFallback panel.", e });
/*    */     } 
/*    */     
/* 29 */     this.browser = browser_;
/* 30 */     setBrowserShown("fallback");
/*    */   }
/*    */   final BrowserPanel browser;
/*    */   public void setBrowserShown(Object reason, boolean shown) {
/* 34 */     if (!shown && !Blocker.isBlocked(this.fallback)) {
/* 35 */       this.fallback.unblock(reason);
/*    */     } else {
/* 37 */       Blocker.setBlocked(this.fallback, reason, shown);
/*    */     } 
/*    */   }
/*    */   public void setBrowserContentShown(Object reason, boolean shown) {
/* 41 */     if (this.browser != null) {
/* 42 */       Blocker.setBlocked(this.browser, reason, !shown);
/*    */     }
/*    */   }
/*    */   
/*    */   public void onResize() {
/* 47 */     if (this.pane == null) {
/* 48 */       log(new Object[] { "pane = null so it'c can't resize" });
/*    */       
/*    */       return;
/*    */     } 
/* 52 */     int width = this.pane.getWidth(), height = this.pane.getHeight();
/* 53 */     setSize(width, height);
/*    */   }
/*    */   
/*    */   private static void log(Object... o) {
/* 57 */     U.log(new Object[] { "[BrowserHolder]", o });
/*    */   }
/*    */   
/*    */   public MainPane getPane() {
/* 61 */     return this.pane;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPane(MainPane pane) {
/* 66 */     this.pane = pane;
/*    */   }
/*    */   
/*    */   public static synchronized BrowserHolder getInstance() {
/* 70 */     if (browserHolder == null)
/* 71 */       browserHolder = new BrowserHolder(); 
/* 72 */     return browserHolder;
/*    */   }
/*    */   
/*    */   public BrowserPanel getBrowser() {
/* 76 */     return this.browser;
/*    */   }
/*    */   
/*    */   public void setBrowserShown(String reason) {
/* 80 */     setBrowserShown(reason, (this.browser != null));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\browser\BrowserHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */